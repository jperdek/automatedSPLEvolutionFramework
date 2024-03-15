package evolutionSimulation;

import java.util.ArrayList;
import java.util.List;


/**
 * The sequence of evolution processes which are focused on different targets such as concerns, features or quality attributes
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class EvolutionConfigurationSequence {


	/**
	 * The sequence of sub-evolution processes (usually with different focuses)
	 */
	private List<EvolutionConfiguration> evolutionConfigurationSequence;
	
	/**
	 * Instantiates the manger for whole sub-evolution phases without any specified
	 */
	public EvolutionConfigurationSequence() {
		this.evolutionConfigurationSequence = new ArrayList<EvolutionConfiguration>();
	}
	
	/**
	 * Instantiates the manger for whole sub-evolution phases
	 * 
	 * @param subEvolutionConfiguration - the sub-evolution configuration for the next sub-evolution phase
	 */
	public void concatenateConfgiruationOfSubEvolutionProcess(EvolutionConfiguration subEvolutionConfiguration) {
		this.evolutionConfigurationSequence.add(subEvolutionConfiguration);
	}
	
	/**
	 * Creates evolution configuration for new evolution-subprocess with necessary configuration from previous sub-evolution (configuration)
	 * -the new evolution configuration is concatenated/added to the end of evolution configuration sequence (available configuration of evolution subprocesses)7
	 * 
	 * @return the evolution configuration for new evolution-subprocess with necessary configuration from previous sub-evolution (configuration)
	 */
	public EvolutionConfiguration createAndConcatenateEvolutionConfigurationAccordingToLastEvolutionConfiguration() {
		EvolutionConfiguration newEvolutionConfiguration = null;
		return newEvolutionConfiguration;
	}
}
