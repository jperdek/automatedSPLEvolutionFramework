package splEvolutionCore;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import evolutionSimulation.productAssetsInitialization.Resource;
import evolutionSimulation.productAssetsInitialization.SharedConfiguration;


/**
 * Integration of functionality that is responsible to extend the Canvas
 * 
 * @author Jakub Perdek
 *
 */
public interface CanvasBasedApplicationConfiguration {

	/**
	 * Allows to use canvas CantoJS wrapper along with evolved (fractal-based) content
	 * if true then CantoJS is used to wrap canvas otherwise false
	 */
	public static boolean WRAP_WITH_CANTO_JS = true;
	
	/**
	 * The location of CantoJS script (perceived as resource or one of SPL assets)
	 */
	public final static String CANTO_SCRIPT_RESOURCE_LOCATION = SharedConfiguration.CANTO_SCRIPT_RESOURCE_LOCATION;
	
	/**
	 * The relative location of CantoJS script in project/SPL directory as one of its assets 
	 */
	public final static String CANTO_TARGET_LOCATION = "js/canto-0.15.js";
	
	
	/**
	 *  Wraps each call to get context into new CantoJS instance
	 *  -the wrapper is providing the same functionality + its extensions like chaining or allows to get previously used position
	 *  -different CantoJS objects are independent of each other!!! - do not share resource even if are based on the same context!!!
	 *  
	 * @param scriptString - the script content or code written in JavaScript or TypeScript
	 * @return the script content provided in function parameter updated on places to adapt/incorporate CantoJS as HTML canvas wrapper
	 */
	public static String wrapWithCantoJS(String scriptString) {
		return scriptString.replaceAll("(\\w+)\\s*.\\s*getContext\\s*\\(\\s*[\"']2[Dd][\"']\\s*\\)", "new canto($1)");
	}
	
	/**
	 * Inserts the CantoJS import to the beginning of the HTML template for testing purposes and copies the CantoJS scripts
	 * -copies CantoJS from CanvasBasedApplicationConfiguration.CANTO_SCRIPT_RESOURCE_LOCATION 
	 *    to CanvasBasedApplicationConfiguration.CANTO_TARGET_LOCATION
	 * 
	 * @param importResources - the list of resources such as imports, template assets
	 * @param targetPath - the root from destination location of the project/SPL
	 * @throws IOException
	 */
	public static void insertAndCopyCantoImport(List<Resource> importResources, String targetPath) throws IOException { 
		String cantoTargetPath = targetPath + "/" + CanvasBasedApplicationConfiguration.CANTO_TARGET_LOCATION;
		Resource scriptResource = new Resource(CanvasBasedApplicationConfiguration.CANTO_TARGET_LOCATION);
		importResources.add(0, scriptResource); //insert to the beginning of the file due to none dependencies
		
		Path destinationLocation = Paths.get(cantoTargetPath.replace("file:///", ""));
		try { Files.createDirectories(destinationLocation); } catch(FileAlreadyExistsException e) {}
		if (DebugInformation.SHOW_INITIAL_COPIED_INFORMATION) {
			System.out.println("FROM: " + CanvasBasedApplicationConfiguration.CANTO_SCRIPT_RESOURCE_LOCATION);
			System.out.println("TO  : " + cantoTargetPath);
		}
		
		Files.copy(Paths.get(CanvasBasedApplicationConfiguration.CANTO_SCRIPT_RESOURCE_LOCATION),
				destinationLocation, StandardCopyOption.REPLACE_EXISTING);
	}
}
