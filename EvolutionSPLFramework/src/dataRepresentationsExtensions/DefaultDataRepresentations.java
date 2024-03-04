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
	public static final boolean LOG_FUNCTIONALITY = true;
	
	/**
	 * Decides about injection of functionality to simulate stack
	 */
	public static final boolean SIMULATE_STACK = false;
	
	

	/**
	 * Decides about injection of functionality to (not) log objects
	 */
	public static final boolean NOT_LOG_OBJECTS = false;
	
	/**
	 * Decides about injection of functionality to (not) log functions
	 */
	public static final boolean NOT_LOG_FUNCTIONS = false;
	
	/**
	 * Decides about injection of functionality to (not) log class functions
	 */
	public static final boolean NOT_LOG_CLASS_FUNCTIONS = false;
	
	/**
	 * Decides about injection of functionality to (not) constructor (functions for class instantiation)
	 */
	public static final boolean NOT_LOG_CLASS_CONSTRUCTOR_FUNCTIONS = false;
}
