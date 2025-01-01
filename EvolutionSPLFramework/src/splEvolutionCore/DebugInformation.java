package splEvolutionCore;


/**
 * Interface to manage logs and informative outputs from the evolution process and its associated parts
 *  
 * @author Jakub Perdek
 *
 */
public interface DebugInformation {

	/**
	 * Outputs files during execution
	 */
	public static boolean OUTPUT_DEBUG_FILES = Boolean.parseBoolean(
			System.getenv().getOrDefault("OUTPUT_DEBUG_FILES", "false"));
	
	/**
	 * Persists the configuration output files of annotated/marked/highlighted AST for further processing and debugging 
	 */
	public static boolean OUTPIT_FILES_AS_ANNOTATED_AST_AND_CODE = Boolean.parseBoolean(
			System.getenv().getOrDefault("OUTPIT_FILES_AS_ANNOTATED_AST_AND_CODE", "false"));
	
	/**
	 * Shows selected synthesized code constructs to better debug incorporated changes that are done in the positive variability management
	 */
	public static boolean SHOW_SELECTED_SYNTESIZED_CODE_CONSTRUCTS = Boolean.parseBoolean(
			System.getenv().getOrDefault("SHOW_SELECTED_SYNTESIZED_CODE_CONSTRUCTS", "true"));
	
	/**
	 * Shows information about possible enhancements in form of warnings during the processing
	 */
	public static boolean SHOW_MISSING_EVOLUTION_ENHANCEMENTS = Boolean.parseBoolean(
			System.getenv().getOrDefault("SHOW_MISSING_EVOLUTION_ENHANCEMENTS", "true"));
	
	/**
	 * The exhaustive/detail information from various processes 
	 */
	public static boolean SHOW_POLLUTING_INFORMATION = Boolean.parseBoolean(
			System.getenv().getOrDefault("SHOW_POLLUTING_INFORMATION", "false"));		
	
	/**
	 * Show the structure and the content of some entities across the application
	 */
	public static boolean SHOW_CREATED_ENTITIES = Boolean.parseBoolean(
			System.getenv().getOrDefault("SHOW_CREATED_ENTITIES", "false"));
	
	/**
	 * Shows the information about exports
	 */
	public static boolean SHOW_INFORMATION_ABOUT_EXPORTS = Boolean.parseBoolean(
			System.getenv().getOrDefault("SHOW_INFORMATION_ABOUT_EXPORTS", "false"));
	
	/**
	 * Shows which project/SPL assets are copied and where - their source and destination paths
	 * - only initial from the initial phase before update
	 */
	public static boolean SHOW_INITIAL_COPIED_INFORMATION = Boolean.parseBoolean(
			System.getenv().getOrDefault("SHOW_INITIAL_COPIED_INFORMATION", "true")); 
	
	/**
	 * Shows AST of added/incorporated/extendable code fragment into new derivation
	 */
	public static boolean SHOW_POSITIVE_VARIABILITY_INCREMENT_CODE_FRAGMENT = Boolean.parseBoolean(
			System.getenv().getOrDefault("SHOW_POSITIVE_VARIABILITY_INCREMENT_CODE_FRAGMENT", "true"));
	
	/**
	 * Shows information about taken steps during evolution process - very descriptive and so much non-repetitive/concise
	 */
	public static boolean PROCESS_STEP_INFORMATION = Boolean.parseBoolean(
			System.getenv().getOrDefault("PROCESS_STEP_INFORMATION", "true"));
	
	/**
	 * Shows information about derived project
	 */
	public static boolean SHOW_DERIVED_PROJECT_INFORMATION = Boolean.parseBoolean(
			System.getenv().getOrDefault("SHOW_DERIVED_PROJECT_INFORMATION", "true"));
}
