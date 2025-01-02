package evolutionSimulation.productAssetsInitialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import evolutionSimulation.EvolutionConfiguration;
import evolutionSimulation.iteration.WrappedTypeScriptContentInVariable;
import splEvolutionCore.DebugInformation;
import splEvolutionCore.SPLEvolutionCore;


/**
 * Injects and initializes custom annotations in form of decorators to manage script variability 
 * -support incorporated for templates
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class CustomAnnotationsAsDecoratorsImporter {

	/**
	 * Logger to track injection and initialization of custom annotations in form of decorators
	 */
	private static final Logger logger = LoggerFactory.getLogger(CustomAnnotationsAsDecoratorsImporter.class);

	/**
	 * Introducing definition of custom variability managing decorators/annotations in new SPL instance if is not already available/included
	 * 
	 * @param splDestinationPath - the path to evolved SPL instance to its directory in particular evolution iteration
	 * @param templateHeadersPart - the header part of HTML template to inject custom defined decorators/annotations to manage variability
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void injectCustomDecorators(String splDestinationPath, Element templateHeadersPart) throws IOException, InterruptedException {
		String relativeScriptPath = SPLEvolutionCore.DECORATOR_CONFIGURATION_ANNOTATIONS_FILE_PATH;
		String resultingScriptPath = splDestinationPath + "/" + relativeScriptPath;
		File resultingScriptFile = new File(resultingScriptPath);
		if (!resultingScriptFile.exists()) {
			logger.debug("Creating decorator annotation file: " + resultingScriptPath + " and injecting it into template.");
			String customDecoratorsFunctionality = CustomAnnotationsAsDecoratorsImporter.loadsCustomDecoratorsFunctionality();
			WrappedTypeScriptContentInVariable wrappedCustomDecoratorFunctionalityInVariable = 
					new WrappedTypeScriptContentInVariable("decoratorsSPL = `" + customDecoratorsFunctionality + "`");
			String decoratorAnnotationsConfigurationScriptContent = 
					wrappedCustomDecoratorFunctionalityInVariable.persistTypeScriptContent(customDecoratorsFunctionality);
			String variableName = wrappedCustomDecoratorFunctionalityInVariable.getVariableName();
			String innerScriptCode = "const decoratorsCode = window.ts.transpile(" + variableName + ".replaceAll(\"export\", \"window.export\")); eval(decoratorsCode);";
			
			Element firstScript = CustomAnnotationsAsDecoratorsImporter.createImportScriptWithSrc(relativeScriptPath);
			templateHeadersPart.appendChild(firstScript);
			Element secondScript = CustomAnnotationsAsDecoratorsImporter.createImportScriptWithContent(innerScriptCode);
			templateHeadersPart.appendChild(secondScript);
			
			FileOutputStream fos = new FileOutputStream(resultingScriptFile);
			try (Writer writer = new OutputStreamWriter(fos)) {
				writer.append(decoratorAnnotationsConfigurationScriptContent);
			} finally {
				fos.close();
			}
		} else {
			logger.debug("Configured annotations/decorators to manage variability are already defined in: " + resultingScriptPath);
		}
	}
	
	/**
	 * Loads the content of file with custom annotations/decorators to manage variability in this SPL
	 * 
	 * @return loaded content of file with custom annotations/decorators to manage variability in this SPL
	 * @throws IOException
	 */
	private static String loadsCustomDecoratorsFunctionality() throws IOException {
		String customAnnotationScriptFile = "./src/evolutionSimulation/productAssetsInitialization/" 
								+ SPLEvolutionCore.DECORATOR_CONFIGURATION_ANNOTATIONS_FILE_NAME;
		if(DebugInformation.SHOW_POLLUTING_INFORMATION) {
			logger.debug("Loading configurationscript with custom annotation: " +  customAnnotationScriptFile);
		}

		if (!System.getenv().getOrDefault("localhost", "DOCKER_HOST").equals("localhost")) {
			customAnnotationScriptFile = SharedConfiguration.PROJECT_PATH + "/resources/" + SPLEvolutionCore.DECORATOR_CONFIGURATION_ANNOTATIONS_FILE_NAME;
		}
		File file = new File(customAnnotationScriptFile);
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
	 * Creates the import script with support of loaded content - allows to define custom annotations/decorators
	 * 
	 * @param relativeImportPath - relative path to executable script with custom annotations/decorators
	 * @return  the import script with associated import path to relevant content/script
	 */
	private static Element createImportScriptWithSrc(String relativeImportPath) {
		Element scriptElement = new Element(Tag.valueOf("script"), "");
		scriptElement.attr("src", relativeImportPath);
		return scriptElement;
	}
	
	/**
	 * Creates the import script with directly executing content
	 * 
	 * @param scriptExecutableContent - the content that should be executed
	 * @return  the script with executable content to load new functionality/ custom decorators/annotations
	 */
	private static Element createImportScriptWithContent(String scriptExecutableContent) {
		Element scriptElement = new Element(Tag.valueOf("script"), "");
		scriptElement.appendText(scriptExecutableContent);
		return scriptElement;
	}
}
