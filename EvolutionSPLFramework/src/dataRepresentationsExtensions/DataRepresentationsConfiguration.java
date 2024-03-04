package dataRepresentationsExtensions;


/**
 * Representation of configuration to incorporate support for various data representations
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class DataRepresentationsConfiguration {
	
	/**
	 * Decides about injection of functionality to Log objects
	 */
	private boolean logFunctionality = DefaultDataRepresentations.LOG_FUNCTIONALITY;
	
	/**
	 * Decides about injection of functionality to simulate stack
	 */
	private boolean simulateStack = DefaultDataRepresentations.SIMULATE_STACK;
	
	

	/**
	 * Decides about injection of functionality to (not) log objects
	 */
	private boolean notLogObjects = DefaultDataRepresentations.NOT_LOG_OBJECTS;
	
	/**
	 * Decides about injection of functionality to (not) log functions
	 */
	private boolean notLogFunctions = DefaultDataRepresentations.NOT_LOG_FUNCTIONS;
	
	/**
	 * Decides about injection of functionality to (not) log class functions
	 */
	private boolean notLogClassFunctions = DefaultDataRepresentations.NOT_LOG_CLASS_FUNCTIONS;
	
	/**
	 * Decides about injection of functionality to (not) constructor (functions for class instantiation)
	 */
	private boolean notLogClassConstructors = DefaultDataRepresentations.NOT_LOG_CLASS_CONSTRUCTOR_FUNCTIONS;

	/**
	 * Maximum variable combinations to log
	 */
	private int maximumVariableCombinationsToLog = 2;
	
	/**
	 * Instantiates configuration for data representations
	 */
	public DataRepresentationsConfiguration() {}
	
	/**
	 * Configures the injection of functionality for logging
	 * 
	 * @param decision logging is enable if true otherwise not
	 */
	public void injectLogFunctionality(boolean decision) { this.logFunctionality = decision; }
	
	/**
	 * Returns information if logging is enabled in final derived product
	 * 
	 * @return logging is enabled in final derived product if true otherwise not
	 */
	public boolean shouldInjectLogFunctionality() { return this.logFunctionality; }
	
	/**
	 * Configures optional incorporation of the stack functionality in final derived product
	 * 
	 * @param decision - stack functionality is incorporated in final derived product if true otherwise not
	 */
	public void injectStackFunctionality(boolean decision) { this.simulateStack = decision; }
	
	/**
	 * Returns information if the stack functionality is incorporated in final derived product
	 * 
	 * @return the stack functionality is incorporated in final derived product if true otherwise not
	 */
	public boolean shouldInjectStackFunctionality() { return this.simulateStack; }
	
	/**
	 * Configures to not log information about objects if logging is enabled in each final derived product
	 * 
	 * @param decision - to not log information about objects if logging is enabled in final derived product if true otherwise information about objects will be logged
	 */
	public void setToNotLogObjects(boolean decision) { this.notLogObjects = decision; }
	
	/**
	 * Returns the information if objects are not logged in each final derived product
	 * 
	 * @return information if objects are not logged if true otherwise objects are logged in each final derived product if logging is enabled
	 */
	public boolean shouldNotLogObjects() { return this.logFunctionality && this.notLogObjects; }
	
	/**
	 * Configures to not log information about functions if logging is enabled in each final derived product
	 * 
	 * @param decision - to not log information about functions if logging is enabled in each final derived product if true otherwise information about functions will be logged
	 */
	public void setToNotLogFunctions(boolean decision) { this.notLogFunctions = decision; }
	
	/**
	 * Returns the information if functions are not logged in each final derived product
	 * 
	 * @return information if functions are not logged if true otherwise functions are logged in each final derived product if logging is enabled
	 */
	public boolean shouldNotLogFunctions() { return this.logFunctionality && this.notLogFunctions; }
	
	/**
	 * Configures to not log information about class functions if logging is enabled in each final derived product
	 * 
	 * @param decision - to not log information about class functions if logging is enabled in each final derived product if true otherwise information about class functions will be logged
	 */
	public void setToNotLogClassFunctions(boolean decision) { this.notLogClassFunctions = decision; }
	
	/**
	 * Returns the information if class functions are not logged in each final derived product
	 * 
	 * @return information if class functions are not logged if true otherwise class functions are logged in each final derived product if logging is enabled
	 */
	public boolean shouldNotLogClassFunctions() { return this.logFunctionality && this.notLogClassFunctions; }
	
	/**
	 * Configures to not log information about class constructors if logging is enabled in each final derived product
	 * 
	 * @param decision - to not log information about class constructors if logging is enabled in each final derived product if true otherwise information about class constructors will be logged
	 */
	public void setToNotLogClassConstructors(boolean decision) { this.notLogObjects = decision; }
	
	/**
	 * Returns the information if class constructors are not logged in each final derived product
	 * 
	 * @return information if class functions are not logged if true otherwise information about class constructors is logged in each final derived product if logging is enabled
	 */
	public boolean shouldNotLogClassConstructors() { return this.logFunctionality && this.notLogClassConstructors; }
	
	/**
	 * Returns the maximum number of variables to be logged with particular variable as their dependencies
	 * 
	 * @return the maximum number of variables to be logged with particular variable as their dependencies
	 */
	public int getMaximumVariableCombinationsToLog() { return this.maximumVariableCombinationsToLog;}
}
