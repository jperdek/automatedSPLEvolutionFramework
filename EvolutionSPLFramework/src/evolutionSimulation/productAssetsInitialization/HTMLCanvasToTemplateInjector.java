package evolutionSimulation.productAssetsInitialization;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import codeConstructsEvaluation.transformation.PostRequester;
import evolutionSimulation.EvolutionConfiguration;
import evolutionSimulation.iteration.EvolutionSamples;
import evolutionSimulation.iteration.EvolutionVariables;
import evolutionSimulation.iteration.WrappedTypeScriptContentInVariable;
import positiveVariabilityManagement.SynthesizedContent;
import splEvolutionCore.CanvasBasedApplicationConfiguration;
import splEvolutionCore.DebugInformation;
import splEvolutionCore.SPLEvolutionCore;


/**
 * Injects HTML canvas to the HTML template and manages its resources
 *  
 * @author Jakub Perdek
 *
 */
public class HTMLCanvasToTemplateInjector {

	/**
	 * The variable ID to produce unique variables
	 */
	private static int counterVariableID = 1;
	
	/**
	 * The list of scripts that should be omitted during the processing of the resource - serve as libraries 
	 */
	private static List<String> scriptsToOmit = List.of(CanvasBasedApplicationConfiguration.CANTO_TARGET_LOCATION); 
	
	/**
	 * Loads HTML template file content - if file does not exist then is created
	 * 
	 * @param htmlTemplateToInjectPath - the path where given HTML template should be created
	 * @return loaded HTML file content
	 * @throws IOException
	 */
	public String optionalyCreateHTMLFileAndGetContent(String htmlTemplateToInjectPath) throws IOException {
		if(DebugInformation.SHOW_POLLUTING_INFORMATION) {
			System.out.println("Creating or Loading file from: " +  htmlTemplateToInjectPath);
		}

		File file = new File(htmlTemplateToInjectPath);
		if (!file.exists()) { file.createNewFile(); return ""; }
		FileInputStream fos = new FileInputStream(file);
		try (Scanner scanner = new Scanner(fos)) {
			String content = "";
			while (scanner.hasNextLine()) {
			    content = content + scanner.nextLine();
			}
			return content;
		} finally {
			fos.close();
		}
	}
	
	/**
	 * Creates HTML script element and puts prepared (initialization) script inside
	 *  - when the script will be inserted to template DOM then will be executed during the opening of the template in the browser
	 *  
	 * @param content - the content with the (initialization) script
	 * @return the script element with prepared script content
	 */
	private Element createScriptWithContent(String content) {
		Element scriptElement = new Element(Tag.valueOf("script"), "");
		scriptElement.html(content);
		return scriptElement;
	}
	
	/**
	 * Creates the canvas element 
	 * - the width and height is set according to CanvasBasedResource instance
	 * - the id is also set according to CanvasBasedResource instance
	 * 
	 * @param canvasBasedResource - the resource with information about canvas element
	 * @return the created canvas element according to its configuration from CanvasBasedResource instance
	 */
	private Element createCanvasElement(CanvasBasedResource canvasBasedResource) {
		String canvasElementName = canvasBasedResource.getCanvasId(); 
		int canvasWidth = canvasBasedResource.getCanvasWidth();
		int canvasHeight = canvasBasedResource.getCanvasHeight();
		Element scriptElement = new Element(Tag.valueOf("script"), "");
		scriptElement.attr("width", Integer.toString(canvasWidth));
		scriptElement.attr("height", Integer.toString(canvasHeight));
		// scriptElement.id("script" + canvasElementName); // ID should be unique and occur only once in HTML file
		return scriptElement;
	}
	
	/**
	 * Checks Evolution Variables and Evolution Samples and provides absolute path to them
	 * -if they should be imported to project then the they must be copied and path to them properly set
	 *  
	 * @param absoluteOrRelativeProjectPath
	 * @return
	 */
	private String checkStaticScripts(String absoluteOrRelativeProjectPath) {
		for (String libraryPath: EvolutionSamples.getAllEvolutionSamples(null)) {
			if (absoluteOrRelativeProjectPath.replace("\\", "/").toLowerCase().contains(libraryPath.toLowerCase().replace("\\", "/"))
					|| libraryPath.toLowerCase().replace("\\", "/").contains(
							absoluteOrRelativeProjectPath.toLowerCase().replace("\\", "/"))) {
				return libraryPath; 
			}
		}
		
		for (String libraryPath: EvolutionVariables.getAllEvolutionSamples(null)) {
			if (absoluteOrRelativeProjectPath.replace("\\", "/").toLowerCase().contains(libraryPath.toLowerCase().replace("\\", "/"))
					|| libraryPath.toLowerCase().replace("\\", "/").contains(
							absoluteOrRelativeProjectPath.toLowerCase().replace("\\", "/"))) { 
				return libraryPath;
			}
		}
		return absoluteOrRelativeProjectPath;
	}
	
	/**
	 * Creates the import script with associated content
	 * 
	 * @param resource - the resource object with associated paths
	 * @return  the import script with associated import path to relevant content/script
	 */
	private Element createImportScriptWithContent(Resource resource) {
		String relativeImportPath = resource.getRelativePathFromProject();
		Element scriptElement = new Element(Tag.valueOf("script"), "");
		relativeImportPath = this.checkStaticScripts(relativeImportPath);
		if (relativeImportPath.contains(":/") || relativeImportPath.contains(":\\")) {
			relativeImportPath = "file:///" + relativeImportPath;
		}
		scriptElement.attr("src", relativeImportPath);
		return scriptElement;
	}

	/**
	 * Generates and optionally updates imported scripts and persists the final code in template
	 * 
	 * @param targetDestinationPath - the path directing to the destination directory, to the root to target project/SPL
	 * @param importResources - the list of the project imported dependencies
	 * @param evolutionConfiguration - the configuration associated with evolution process, especially its phases to drive evolution and store relevant information
	 * @param synthesizedContent - the sources necessary to manage synthesis of the evolved part - the code with updated AST is persisted along with template
	 * @param projectId - the sources necessary to manage synthesis of the evolved part - the code with updated AST is persisted along with template
	 * @return the JavaScript code that is transpiled and prepared to be used in the HTML template and executed by a browser 
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private String generateTotranspileImportedScriptsAndPersistCode(String targetDestinationPath, 
			List<Resource> importResources,
			EvolutionConfiguration evolutionConfiguration, SynthesizedContent synthesizedContent, 
			String projectId) throws IOException, InterruptedException {
		String pathToImportInProject;
		String importedCodeToTranspile;
		String extractedVariableName;
		
		String inVariableCode;
		String fileContent;
		String processedResourceFileName;
		String destinationLibraryPath, destinationLibraryFilePath;
		String allTranspiledCode = "";
		String relativeBasePathParts[] = evolutionConfiguration.getCurrentEvolvedScriptRelativePath().split("/");
		String relativeBasePath = relativeBasePathParts[relativeBasePathParts.length - 1];
		boolean isLibrary, isBaseScript, shouldBeExcluded;
		for (Resource importResource: importResources) {
			String absoluteOrRelativeProjectPath = importResource.getRelativePathFromProject();

			shouldBeExcluded = false;
			for (String scriptToOmit: HTMLCanvasToTemplateInjector.scriptsToOmit) {
				if (absoluteOrRelativeProjectPath.toLowerCase().contains(scriptToOmit.toLowerCase()) ||
						scriptToOmit.toLowerCase().replace("\\", "/").contains(
								absoluteOrRelativeProjectPath.toLowerCase().replace("\\", "/"))) { shouldBeExcluded = true; break; }
			}
			if (shouldBeExcluded) { continue; } //skipping scripts to omit

			isLibrary = false;
			for (String libraryPath: EvolutionSamples.getAllEvolutionSamples(null)) {
				if (absoluteOrRelativeProjectPath.replace("\\", "/").toLowerCase().contains(libraryPath.toLowerCase().replace("\\", "/"))
						|| libraryPath.toLowerCase().replace("\\", "/").contains(
								absoluteOrRelativeProjectPath.toLowerCase().replace("\\", "/"))) {
					absoluteOrRelativeProjectPath = libraryPath; 
					isLibrary = true; 
					if (SPLEvolutionCore.INCLUDE_SHARED_LIBRARY) {
						processedResourceFileName = libraryPath.substring(libraryPath.replace("\\", "/").lastIndexOf("/"));
						destinationLibraryPath = targetDestinationPath + "/" + SPLEvolutionCore.SHARED_LIBRARY_LOCATION;
						destinationLibraryFilePath = destinationLibraryPath + "/" + processedResourceFileName;
						try {
							Files.createDirectories(Path.of(destinationLibraryPath));
							Files.copy(Path.of(absoluteOrRelativeProjectPath), Path.of(destinationLibraryFilePath));
						} catch (FileAlreadyExistsException e) {
						}
						importResource.setRelativeProjectPath(destinationLibraryFilePath);
					} else { break; } 
				}
			}
			
			for (String libraryPath: EvolutionVariables.getAllEvolutionSamples(null)) {
				if (absoluteOrRelativeProjectPath.replace("\\", "/").toLowerCase().contains(libraryPath.toLowerCase().replace("\\", "/"))
						|| libraryPath.toLowerCase().replace("\\", "/").contains(
								absoluteOrRelativeProjectPath.toLowerCase().replace("\\", "/"))) { 
					absoluteOrRelativeProjectPath = libraryPath;
					isLibrary = true;
					if (SPLEvolutionCore.INCLUDE_SHARED_VARIABLES_IN_LIBRARY) {
						processedResourceFileName = libraryPath.substring(libraryPath.replace("\\", "/").lastIndexOf("/"));
						destinationLibraryPath = targetDestinationPath + "/" + SPLEvolutionCore.SHARED_GLOBAL_VARIABLES_LOCATION;
						destinationLibraryFilePath = destinationLibraryPath + "/" + processedResourceFileName;
						try {
							Files.createDirectories(Path.of(destinationLibraryPath));
							Files.copy(Path.of(absoluteOrRelativeProjectPath), Path.of(destinationLibraryFilePath));
						} catch (FileAlreadyExistsException e) {
						}
						importResource.setRelativeProjectPath(destinationLibraryFilePath);
					} else {
						break;
					}
				}
			}
			if (absoluteOrRelativeProjectPath.contains("://") || absoluteOrRelativeProjectPath.contains(":\\")) {
				pathToImportInProject = absoluteOrRelativeProjectPath;
			} else {
				pathToImportInProject = targetDestinationPath + importResource.getRelativePathFromProject();
			}

			isBaseScript = absoluteOrRelativeProjectPath.contains(relativeBasePath);
			
			fileContent = PostRequester.loadFileContent(pathToImportInProject);
			if (isBaseScript) { inVariableCode = null; }	

			WrappedTypeScriptContentInVariable wrappedTypeScriptContentInVariable = new WrappedTypeScriptContentInVariable(fileContent);
				
			if (!isLibrary) {
				// force unique variable names to prevent conflicts
				inVariableCode = wrappedTypeScriptContentInVariable.forceUniqueVariableNamesAndAdaptItToStringContent(
						fileContent, synthesizedContent, isBaseScript);
		
				wrappedTypeScriptContentInVariable.persistAstFromSynthesizedContent(inVariableCode, evolutionConfiguration, 
						projectId, synthesizedContent, pathToImportInProject);
			}
			extractedVariableName = wrappedTypeScriptContentInVariable.getVariableName().replace("var ", "");
			System.out.println("Using variable name: " + extractedVariableName + " for " + absoluteOrRelativeProjectPath);
			importedCodeToTranspile = "const jsCode" + Integer.toString(HTMLCanvasToTemplateInjector.counterVariableID) + 
					" = window.ts.transpile(" + extractedVariableName + ".replaceAll(\"export\", \"window.export\"));\n eval(jsCode" + 
					Integer.toString(HTMLCanvasToTemplateInjector.counterVariableID) + ");";
			HTMLCanvasToTemplateInjector.counterVariableID = HTMLCanvasToTemplateInjector.counterVariableID + 1;
			allTranspiledCode = importedCodeToTranspile + "\n " + allTranspiledCode;
		}
		
		return allTranspiledCode;
	}
	
	/**
	 * Injects relevant/initial content with its dependencies into the template
	 * 
	 * @param callToSyntethizedFunctionality - the initial code that is going to be extended and injected to template 
	 * @param targetDestinationPath - the path directing to the destination directory, to the root to target project/SPL
	 * @param htmlTemplateToInjectPath - the path where given HTML template should be created
	 * @param resources - the list of the project resources/dependencies
	 * @param evolutionConfiguration - the configuration associated with evolution process, especially its phases to drive evolution and store relevant information
	 * @param synthesizedContent - the sources necessary to manage synthesis of the evolved part - the code with updated AST is persisted along with template
	 * @param projectId - the unique identifier of final project/SPL
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void injectToTemplate(String callToSyntethizedFunctionality, String targetDestinationPath, 
			String htmlTemplateToInjectPath, List<Resource> resources, EvolutionConfiguration evolutionConfiguration, 
			SynthesizedContent synthesizedContent, String projectId) throws IOException, InterruptedException {
		String templateContent = this.optionalyCreateHTMLFileAndGetContent(htmlTemplateToInjectPath);
		Document document;
		if (templateContent.equals("") || templateContent == null) {
			templateContent = "<html><head><title>Title</title></head><body></body></html>";
		}
		document = Jsoup.parse(templateContent);
		
		Element headersPart = document.selectFirst("head");
		Element bodyPart = document.selectFirst("body");
		Element scriptElement, canvasElement;
		
		List<Resource> importResources = new ArrayList<Resource>();
		importResources.add(new Resource(evolutionConfiguration.getCurrentEvolvedScriptRelativePath(), true));
		if (CanvasBasedApplicationConfiguration.WRAP_WITH_CANTO_JS) {
			CanvasBasedApplicationConfiguration.insertAndCopyCantoImport(resources, targetDestinationPath);
		}
		
		// inserts resources - some paths inside resources can change before this line
		for (Resource resource: resources) {
			if (!(resource instanceof CanvasBasedResource)) {
				if (resource.isBase()) { continue; }
				importResources.add(resource);
			}
		}
				
		callToSyntethizedFunctionality = this.generateTotranspileImportedScriptsAndPersistCode(
				targetDestinationPath, importResources, evolutionConfiguration, synthesizedContent, projectId) 
				+ callToSyntethizedFunctionality;
		
		// inserts resources - some paths inside resources can change before this line
		for (Resource resource: importResources) {
			if (resource instanceof CanvasBasedResource) {
				canvasElement = this.createCanvasElement((CanvasBasedResource) resource); //add canvas  
				bodyPart.appendChild(canvasElement);
			} else {
				if (resource.isBase()) { continue; }
				scriptElement = this.createImportScriptWithContent(resource); //add dependencies  
				headersPart.appendChild(scriptElement);
			}
		}
				
		scriptElement =  this.createScriptWithContent(callToSyntethizedFunctionality); 
		bodyPart.appendChild(scriptElement); //add key functionality 
		
		File file = new File(htmlTemplateToInjectPath);
		FileOutputStream fos = new FileOutputStream(file);
		try (Writer writer = new OutputStreamWriter(fos)) {
			writer.append(document.toString());
		} finally {
			fos.close();
		}
	}
}
