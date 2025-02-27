package dataRepresentationsExtensions;


/**
 * The configuration for various data representations
 * 
 * 
 * @author Jakub Perdek
 *
 */
public interface DefaultDataRepresentations {

	/**
	 * Decides about injection of functionality to Log objects
	 */
	public static final boolean LOG_FUNCTIONALITY = Boolean.parseBoolean(
			System.getenv().getOrDefault("LOG_FUNCTIONALITY", "false"));
	
	/**
	 * Decides about injection of functionality to simulate stack
	 */
	public static final boolean SIMULATE_STACK = Boolean.parseBoolean(
			System.getenv().getOrDefault("SIMULATE_STACK", "true"));
	
	

	/**
	 * Decides about insertion of helper functionality to harvest data from stack if function calls are deeply wrapped
	 */
	public static final boolean INSERTS_HELPER_FUNCTIONALITY_TO_HARVEST_DATA_FROM_STACK = Boolean.parseBoolean(
			System.getenv().getOrDefault("INSERTS_HELPER_FUNCTIONALITY_TO_HARVEST_DATA_FROM_STACK", "true"));
	
	/**
	 * Decides about injection of functionality to (not) log objects
	 */
	public static final boolean NOT_LOG_OBJECTS = Boolean.parseBoolean(
			System.getenv().getOrDefault("NOT_LOG_OBJECTS", "false"));
	
	/**
	 * Decides about injection of functionality to (not) log functions
	 */
	public static final boolean NOT_LOG_FUNCTIONS = Boolean.parseBoolean(
			System.getenv().getOrDefault("NOT_LOG_FUNCTIONS", "false"));
	
	/**
	 * Decides about injection of functionality to (not) log class functions
	 */
	public static final boolean NOT_LOG_CLASS_FUNCTIONS = Boolean.parseBoolean(
			System.getenv().getOrDefault("NOT_LOG_CLASS_FUNCTIONS", "false"));
	
	/**
	 * Decides about injection of functionality to (not) constructor (functions for class instantiation)
	 */
	public static final boolean NOT_LOG_CLASS_CONSTRUCTOR_FUNCTIONS = Boolean.parseBoolean(
			System.getenv().getOrDefault("NOT_LOG_CLASS_CONSTRUCTOR_FUNCTIONS", "false"));
}
