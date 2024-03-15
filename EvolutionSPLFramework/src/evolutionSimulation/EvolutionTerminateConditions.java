package evolutionSimulation;


/**
 * Preserves the information and checks the conditions for (sub-)evolution termination
 *  
 * @author Jakub Perdek
 *
 */
public class EvolutionTerminateConditions {

	/**
	 * The maximal number of processed evolution iterations
	 */
	private int maxNumberOfIterations = 10;
	
	/**
	 * Instantiates the default evolutionTerminateConditions instance
	 */
	public EvolutionTerminateConditions() {
	}
	
	/**
	 * Instantiates the default evolutionTerminateConditions instance according to restrictions from another similar instance
	 * 
	 * @param anotherEvolutionTerminateConditions - the another instance with (sub-)evolution conditions for (sub-)evolution process termination
	 */
	public EvolutionTerminateConditions(EvolutionTerminateConditions anotherEvolutionTerminateConditions) {
		this();
		this.maxNumberOfIterations = anotherEvolutionTerminateConditions.getMaximalNumberOfIterations();
	}
	
	/**
	 * Sets the maximal number of (sub-)evolution iterations
	 * 
	 * @param maxNumberOfIterations - the maximal number of (sub-)evolution iterations
	 */
	public void setMaximalNumberOfIterations(int maxNumberOfIterations) {
		this.maxNumberOfIterations = maxNumberOfIterations;
	}
	
	/**
	 * Returns the maximal number of (sub-)evolution iterations
	 * 
	 * @return the maximal number of (sub-)evolution iterations
	 */
	public int getMaximalNumberOfIterations() { return this.maxNumberOfIterations; }
	
	/**
	 * Checks condition according to provided parameters if (sub-)evolution process should terminate or not
	 * 
	 * @param actualIterationNumber - the number (order) of actually processed evolution iterations
	 * @return true if (sub-)evolution process should terminate otherwise not
	 */
	public boolean shouldTerminate(int actualIterationNumber) {
		if (this.maxNumberOfIterations != -1 && this.maxNumberOfIterations >= actualIterationNumber) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks condition according to the values from provided evolution configuration if (sub-)evolution process should terminate or not
	 * 
	 * @param actualEvolutionConfiguration - the actually used evolution configuration to provide information to check if (sub-)evolution process should terminate or not
	 * @return true if (sub-)evolution process should terminate otherwise not
	 */
	public boolean shouldTerminate(EvolutionConfiguration actualEvolutionConfiguration) {
		return this.shouldTerminate(actualEvolutionConfiguration.getIteration());
	}
}
