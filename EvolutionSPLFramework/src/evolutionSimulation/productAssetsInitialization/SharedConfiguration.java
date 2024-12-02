package evolutionSimulation.productAssetsInitialization;


/**
 * Shared configuration of SPL evolution
 * 
 * 
 * @author Jakub Perdek
 *
 */
public interface SharedConfiguration {

	/**
	 * Path to Automated SPL framework project folder
	 */
	public static final String PROJECT_PATH = System.getenv().getOrDefault("PROJECT_PATH", "E://aspects/automatedSPLEvolutionFramework/EvolutionSPLFramework");

	/**
	 * Path to Automated SPL evolution project directory - all evolved SPLs versions, products and configuration artifacts will be stored here
	 */
	public static final String PATH_TO_EVOLUTION_DIRECTORY = System.getenv().getOrDefault("PATH_TO_EVOLUTION_DIRECTORY", "E://aspects/automatedSPLEvolutionFramework/EvolutionSPLFramework/evolutionDirectory");
	
	/**
	 * The location of CantoJS script (perceived as resource or one of SPL assets) - used if CantoJS wrapping is turned on
	 */
	public final static String CANTO_SCRIPT_RESOURCE_LOCATION = System.getenv().getOrDefault("CANTO_SCRIPT_RESOURCE_LOCATION", "E://aspects/automatedSPLEvolutionFramework/EvolutionSPLFramework/resources/canto/canto-0.15.js");
}
