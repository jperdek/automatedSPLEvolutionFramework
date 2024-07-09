package splEvolutionCore.derivation;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import codeContext.persistence.UpdatedTreePersistence;
import codeContext.processors.export.ExportLocationAggregation;
import codeContext.processors.export.ExportLocations;
import evolutionSimulation.EvolutionConfiguration;
import evolutionSimulation.productAssetsInitialization.CanvasBasedResource;
import evolutionSimulation.productAssetsInitialization.HTMLCanvasToTemplateInjector;
import evolutionSimulation.productAssetsInitialization.Resource;
import evolutionSimulation.productAssetsInitialization.UnknownResourceToProcessException;
import positiveVariabilityManagement.ProjectCopier;
import positiveVariabilityManagement.SynthesizedContent;
import splEvolutionCore.DebugInformation;
import splEvolutionCore.SPLEvolutionCore;
import codeConstructsEvaluation.transformation.ASTConverterClient;
import splEvolutionCore.candidateSelector.valueAssignment.cleaning.AllMarkerRemover;
import splEvolutionCore.candidateSelector.valueAssignment.cleaning.AnnotationsFromCodeRemover;


/**
 * Conjuncting functionality on place of variation points
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class VariationPointConjunctor {

	/**
	 * The list of variation point conjunction
	 */
	private List<String> variationPointConjunction;
	
	/**
	 * Manages resources of given created project/SPL as part of iteration phase
	 */
	private DerivationResourcesManager derivationResourcesManager;

	
	/**
	 * Creates instance to manage conjuncting functionality
	 * 
	 * @param derivationResourcesManager - manager of resources of given created project/SPL as part of iteration phase
	 */
	public VariationPointConjunctor(DerivationResourcesManager derivationResourcesManager) {
		this.derivationResourcesManager = derivationResourcesManager;
		this.variationPointConjunction = new ArrayList<String>();
	}
	
	/**
	 * Converts updated AST into code and performs its serialization
	 * 
	 * @param resultingPath - target/destination path to synthesize updated and converted AST into code
	 * @param synthesizedContent - synthesized content object that is used to synthesize whole project/AST
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void serializeSythesizedContent(String resultingPath, SynthesizedContent synthesizedContent) throws IOException, InterruptedException {
		if (resultingPath.contains("file:///")) { resultingPath = "file:///" + resultingPath.replace("://", ":/"); }
		String updatedAndCurrentlyEvolvedCode = ASTConverterClient.convertFromASTToCode(
				synthesizedContent.getReferenceToProcessedAST().toString());
		if (SPLEvolutionCore.CLEAR_COMMENTS_FROM_RESULTING_SPL_DERIVATION) {
			updatedAndCurrentlyEvolvedCode = ASTConverterClient.clearComments(updatedAndCurrentlyEvolvedCode);
		}
		UpdatedTreePersistence.persistsAstInFile(resultingPath, updatedAndCurrentlyEvolvedCode);
	}
	
	/**
	 * 
	 * @param exportedAggregations
	 * @return
	 */
	private List<Resource> prepareRessourcesFromExportedAggregations(List<ExportLocationAggregation> exportedAggregations) {
		Resource importResource;
		String importPath;
		Set<String> alreadyPreparedImports = new HashSet<String>();
		List<Resource> usedResources = new ArrayList<Resource>();
		for (ExportLocationAggregation exportedAggregation: exportedAggregations) {
			for (ExportLocations exportLocations: exportedAggregation.getFileBasedLocations().values()) {
				importPath = exportLocations.getImportPath();

				if (!alreadyPreparedImports.contains(importPath)) {
					alreadyPreparedImports.add(importPath);
					
					importResource = new Resource(importPath);
					usedResources.add(importResource);
				}
			}
		}
		return usedResources;
	}
	
	/**
	 * Prepares test template and dependencies
	 * 
	 * @param projectId - unique project identifier
	 * @param evolutionConfiguration - object that manages evolution configuration
	 * @param exportedAggregations - list of exported aggregated exports that should be included as dependencies
	 * @param synthesizedContent - synthesized content object that is used to synthesize whole project/AST
	 * @throws IOException
	 * @throws UnknownResourceToProcessException
	 * @throws InterruptedException
	 */
	private void prepareTestTemplateAndDependenciesOnlyOne(String projectId, EvolutionConfiguration evolutionConfiguration, 
			List<ExportLocationAggregation> exportedAggregations, SynthesizedContent synthesizedContent) 
					throws IOException, UnknownResourceToProcessException, InterruptedException {
		String targetDestinationPath = evolutionConfiguration.getOutputFilePath(projectId);
		String templateDestinationPath = evolutionConfiguration.getTemplateConfigurationPath(null, projectId);
		HTMLCanvasToTemplateInjector htmlCanvasToTemplateInjector = new HTMLCanvasToTemplateInjector();
		List<Resource> usedResources = this.prepareRessourcesFromExportedAggregations(exportedAggregations);
		
		if(DebugInformation.PROCESS_STEP_INFORMATION || DebugInformation.SHOW_DERIVED_PROJECT_INFORMATION) {
			System.out.println("Final template path: " + templateDestinationPath);
			System.out.println("Final updated/evolved base script path: " + targetDestinationPath);
		}
		//String canvasElementName, templatePath;
		Resource canvasResource;
		for (Resource initialResource: evolutionConfiguration.getInitialResources()) {
			if (initialResource instanceof CanvasBasedResource) {
				canvasResource = (Resource) initialResource; //new CanvasBasedResource(canvasElementName, templatePath);
				usedResources.add(canvasResource);
			} else {
				throw new UnknownResourceToProcessException("Resource with path: " + 
							initialResource.getRelativePathFromProject() + " is unknown initial resource!");
			}
		}
		
		if (templateDestinationPath.contains("file:///")) {
			templateDestinationPath = templateDestinationPath.replace("file:///", "");
		}
		// FOR EXPORTS IN ADVANCED APPLICATIONS THESE IMPORTS SHOULD BE INCLUDED INTO FINAL AST OF RESULTING PROJECT
		htmlCanvasToTemplateInjector.injectToTemplate(
				this.derivationResourcesManager.getEvolutionConfigurationReference().getInitialCode(), targetDestinationPath, 
				templateDestinationPath, usedResources, evolutionConfiguration, synthesizedContent, projectId, true);
	}
	
	/**
	 * Returns list of potential names of source projects or simply folder names that needs to be checked (against correctness)
	 * 
	 * @param projectPath - path to directory of which file names should be extracted
	 * @return list of potential names of source projects or simply folder names that needs to be checked (against correctness)
	 */
	private List<String> getDirectoryProjectNames(String projectPath) {
		String fileName;
		
		List<String> sourceProjectNames = new ArrayList<String>();
		File folder = new File(projectPath);
		File[] availableFiles = folder.listFiles();

		for (File processedFile: availableFiles) {
			if (processedFile.isDirectory()) {
				fileName = processedFile.getName();
				sourceProjectNames.add(fileName);
			}
		}
		return sourceProjectNames;
	}
	
	/**
	 * 
	 * @param evolutionConfiguration
	 * @param targetProjectPath
	 * @return
	 */
	private List<String> getTargetSPLProjectPathExtensions(EvolutionConfiguration evolutionConfiguration, String targetProjectPath) {
		String sourceProjectPath = evolutionConfiguration.getInputFilePath();
		System.out.println("Used source project path for analysis: " + sourceProjectPath);
		//System.out.println("Used target project path for analysis: " + targetProjectPath);
		List<String> sourceProjectFileNames = this.getDirectoryProjectNames(sourceProjectPath);
		//List<String> targetProjectFileNames = this.getDirectoryProjectNames(targetProjectPath);
		Iterator<String> sourceProjectFileIterator = sourceProjectFileNames.iterator();
		
		List<String> targetProjectPathExtensions = new ArrayList<String>();
		String sourceProjectFileName, targetProjectFileName;
		while(sourceProjectFileIterator.hasNext()) {
			sourceProjectFileName = sourceProjectFileIterator.next();
			
			if (evolutionConfiguration.isEvolutionLastIterationProjectName(sourceProjectFileName)) {
				System.out.println("Found project path: " + sourceProjectFileName);
				targetProjectPathExtensions.add(sourceProjectFileName);
			}
		}
		
		//enable to produce exactly one product if names not match for initial iteration
		if (targetProjectPathExtensions.size() == 0) { targetProjectPathExtensions.add(""); }
		return targetProjectPathExtensions;
	}
	
	/**
	 * Prepares test template and dependencies
	 * 
	 * @param projectId - unique project identifier
	 * @param evolutionConfiguration - object that manages evolution configuration
	 * @param exportedAggregations - list of exported aggregated exports that should be included as dependencies
	 * @param synthesizedContent - synthesized content object that is used to synthesize whole project/AST
	 * @throws IOException
	 * @throws UnknownResourceToProcessException
	 * @throws InterruptedException
	 */
	private void prepareTestTemplateAndDependencies(String projectId, EvolutionConfiguration evolutionConfiguration, 
			List<ExportLocationAggregation> exportedAggregations, SynthesizedContent synthesizedContent) 
					throws IOException, UnknownResourceToProcessException, InterruptedException {
		String targetDestinationPath = evolutionConfiguration.getOutputFilePath(projectId);
		String adaptedTargetDestinationPath;
		String templateDestinationPath;
		HTMLCanvasToTemplateInjector htmlCanvasToTemplateInjector;
		List<Resource> usedResources;
		
		List<String> sourceProjectFileNames = this.getTargetSPLProjectPathExtensions(evolutionConfiguration, targetDestinationPath);
		boolean isInitialPhase;
		for (String sourceProjectFileName: sourceProjectFileNames) {
			adaptedTargetDestinationPath = targetDestinationPath;
			
			//NO SOURCE FOUND!!!
			isInitialPhase = sourceProjectFileName.equals("");
			if (!isInitialPhase) {
				adaptedTargetDestinationPath = adaptedTargetDestinationPath + "/" + sourceProjectFileName;
				templateDestinationPath = evolutionConfiguration.getTemplateConfigurationPath(adaptedTargetDestinationPath, projectId);
			} else {
				System.out.println("Generating default one/standalone initial resource.");
				templateDestinationPath = evolutionConfiguration.getTemplateConfigurationPath(null, projectId);
			}

			if(DebugInformation.PROCESS_STEP_INFORMATION || DebugInformation.SHOW_DERIVED_PROJECT_INFORMATION) {
				System.out.println("Final template path: " + templateDestinationPath);
				System.out.println("Final adapted template path: " + adaptedTargetDestinationPath);
				System.out.println("Final updated/evolved base script path: " + targetDestinationPath);
			}
			
			//String canvasElementName, templatePath;
			usedResources = this.prepareRessourcesFromExportedAggregations(exportedAggregations);
			Resource canvasResource;
			for (Resource initialResource: evolutionConfiguration.getInitialResources()) {
				if (initialResource instanceof CanvasBasedResource) {
					canvasResource = (Resource) initialResource; //new CanvasBasedResource(canvasElementName, templatePath);
					usedResources.add(canvasResource);
				} else {
					throw new UnknownResourceToProcessException("Resource with path: " + 
								initialResource.getRelativePathFromProject() + " is unknown initial resource!");
				}
			}
			
			if (templateDestinationPath.contains("file:///")) {
				templateDestinationPath = templateDestinationPath.replace("file:///", "");
			}
			// FOR EXPORTS IN ADVANCED APPLICATIONS THESE IMPORTS SHOULD BE INCLUDED INTO FINAL AST OF RESULTING PROJECT
			htmlCanvasToTemplateInjector = new HTMLCanvasToTemplateInjector();
			htmlCanvasToTemplateInjector.injectToTemplate(
					this.derivationResourcesManager.getEvolutionConfigurationReference().getInitialCode(), adaptedTargetDestinationPath, 
					templateDestinationPath, usedResources, evolutionConfiguration, synthesizedContent, projectId, isInitialPhase);
			
			if (SPLEvolutionCore.SERIALIZE_APPLICATION_AST || SPLEvolutionCore.SERIALIZE_VARIATION_POINTS) {
				this.serializeModifiedVariationPointConfiguration(adaptedTargetDestinationPath, synthesizedContent, projectId);
			}
		}
	}

	
	/**
	 * Copies base project assets as whole project directory including libraries, images, css files
	 * 
	 * @param projectId - unique project identifier
	 * @param evolutionConfiguration - object that manages evolution configuration
	 * @throws IOException
	 */
	private void copyBaseProjectAssets(String projectId, EvolutionConfiguration evolutionConfiguration) throws IOException {
		String sourceProjectPath = evolutionConfiguration.getInputFilePath();
		
		if(DebugInformation.SHOW_DERIVED_PROJECT_INFORMATION) {
			System.out.println("ProjectID: " + projectId);
			System.out.println("EvolutionID: " + evolutionConfiguration.getEvolvedContentName());
		}
		
		String destinationProjectPath = evolutionConfiguration.getOutputFilePath(projectId);
		this.createDirectoriesOnGivenPathIfAreNotExist(destinationProjectPath);
		ProjectCopier.copyExistingProject(sourceProjectPath, destinationProjectPath, true);
	}
	
	/**
	 * Manages derivation of conjuncting parts 
	 * 
	 * @param synthesizedContent - synthesized content object that is used to synthesize whole project/AST
	 * @param exportedAggregations
	 * @return
	 * @throws IOException
	 * @throws UnknownResourceToProcessException
	 * @throws InterruptedException
	 */
	public String deriveProductWithConjunctingPartsAsTestTemplate(SynthesizedContent synthesizedContent,
			List<ExportLocationAggregation> exportedAggregations) throws IOException, UnknownResourceToProcessException, InterruptedException {
		VariationPointConjunctor.clearVariabilityAnnotationsAndMarkers(synthesizedContent);
		String projectId = this.derivationResourcesManager.getEvolutionProjectFileNameIdentifier(synthesizedContent.getReferenceToProcessedAST());
		if (projectId == null) { return null; }
		EvolutionConfiguration evolutionConfiguration = this.derivationResourcesManager.getEvolutionConfigurationReference();
		this.copyBaseProjectAssets(projectId, evolutionConfiguration); //copy all files from original/ or common ones/those that should be updated
		this.prepareTestTemplateAndDependencies(projectId, evolutionConfiguration, exportedAggregations, synthesizedContent);
		return projectId;
	}
	
	/**
	 * Creates directories on given path if are not exists
	 * 
	 * @param outputPath - target path where each directory will be created if does not exist
	 * @throws IOException
	 */
	private void createDirectoriesOnGivenPathIfAreNotExist(String outputPath) throws IOException {
		Files.createDirectories(Paths.get(outputPath));
	}
	
	/**
	 * Derives product/project/SPL with conjuncting parts in form of updated and annotated base script
	 * 
	 * @param synthesizedContent - synthesized content object that is used to synthesize whole project/AST 
	 * @param exportedAggregations - list of exported aggregated exports that should be included as dependencies
	 * @return identifier of the project
	 * @throws IOException
	 * @throws UnknownResourceToProcessException
	 * @throws InterruptedException
	 */
	public String deriveProductWithConjunctingParts(SynthesizedContent synthesizedContent,
			List<ExportLocationAggregation> exportedAggregations) throws IOException, UnknownResourceToProcessException, InterruptedException {
		String projectId = this.derivationResourcesManager.getEvolutionProjectFileNameIdentifier(synthesizedContent.getReferenceToProcessedAST());
		if (projectId == null) { return null; }
		EvolutionConfiguration evolutionConfiguration = this.derivationResourcesManager.getEvolutionConfigurationReference();
		this.copyBaseProjectAssets(projectId, evolutionConfiguration);
		this.updateAstAboutExportedAggregations(projectId, evolutionConfiguration, exportedAggregations);
		
		String targetDestinationPath = evolutionConfiguration.getOutputFilePath(projectId);
		String currentScriptPath = targetDestinationPath + evolutionConfiguration.getCurrentEvolvedScriptRelativePath();
		this.serializeSythesizedContent(currentScriptPath, synthesizedContent);
		return projectId;
	}
	
	/**
	 * Updates AST about used exported aggregations given project except HTML template
	 * 
	 * @param projectId - unique project identifier
	 * @param evolutionConfiguration
	 * @param exportedAggregations - list of exported aggregated exports that should be included as dependencies
	 * @throws IOException
	 * @throws UnknownResourceToProcessException
	 */
	private void updateAstAboutExportedAggregations(String projectId, EvolutionConfiguration evolutionConfiguration, 
			List<ExportLocationAggregation> exportedAggregations) throws IOException, UnknownResourceToProcessException {
		List<Resource> usedResources = this.prepareRessourcesFromExportedAggregations(exportedAggregations);
		
	}
	
	/**
	 * Optional serialization of the main artifacts associated with evolution of given project/SPL
	 * -processed and updated AST of base script/SPL
	 * -used variation points
	 * 
	 * -target paths are generated at this point
	 * 
	 * @param targetDestinationPath - destination path to resulting directory
	 * @param synthesizedContent  synthesized content object that is used to synthesize whole project/AST 
	 * @param projectId - unique project identifier
	 */
	public void serializeModifiedVariationPointConfiguration(String targetDestinationPath, SynthesizedContent synthesizedContent, String projectId) {
		if (projectId == null) { return; }
		EvolutionConfiguration evolutionConfiguration = this.derivationResourcesManager.getEvolutionConfigurationReference();
		if (targetDestinationPath == null) { targetDestinationPath = evolutionConfiguration.getOutputFilePath(projectId); }
		String evolvedContentName = this.derivationResourcesManager.getEvolvedContentName();
		String finalEvolutionResourcesPath = targetDestinationPath + evolvedContentName + synthesizedContent.getFileName();
		this.variationPointConjunction.add(finalEvolutionResourcesPath);

		if (SPLEvolutionCore.SERIALIZE_APPLICATION_AST) {
			JSONObject processedAST = synthesizedContent.getReferenceToProcessedAST();
			UpdatedTreePersistence.persistsAstInFile(finalEvolutionResourcesPath + SPLEvolutionCore.PROCESSED_AST_NAME_ID_ENDING, processedAST);
		}
		if (SPLEvolutionCore.SERIALIZE_VARIATION_POINTS) {
			JSONArray variationPointData = synthesizedContent.getVariationPointsDataReferenceAST();
			UpdatedTreePersistence.persistsAstInFile(finalEvolutionResourcesPath + SPLEvolutionCore.VARIATION_POINTS_DATA_NAME_ID_ENDING, 
					variationPointData.toString());
		}
	}

	/**
	 * Clears all variability annotations and markers
	 * -positive variability markers
	 * -negative variability annotations
	 * 
	 * @param processedSynthesizedContent - synthesized content object that is used to synthesize whole project/AST
	 */
	public static void clearVariabilityAnnotationsAndMarkers(SynthesizedContent processedSynthesizedContent) {
		JSONObject processedSynthesizedAst = processedSynthesizedContent.getReferenceToProcessedAST();
		AllMarkerRemover.removeAllPositiveVariabilityMarkers(processedSynthesizedAst, processedSynthesizedAst);
		AnnotationsFromCodeRemover.removeAllVariabilityAnnotations(processedSynthesizedAst, processedSynthesizedAst);
	}
}
