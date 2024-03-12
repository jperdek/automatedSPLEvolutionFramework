package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies;

import java.util.Queue;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Strategy to substitute unique parameters from template
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class UniqueParametersInInstantiationsFromTemplate implements CallsInstantiationFromTemplate {
	
	/**
	 * Prescribe the function to perform instantiations from selected callable constructs which parameters are substituted with unique imported or available variables (variables are not repeated)
	 * NOT IMPLEMENTED
	 * 
	 * @param variationPointCandidateTemplate - the template with information about particular/processed positive variability variation point (where functionality can be injected)
	 * @param allVariablesMapper - the mapping of variable/parameter types to related information 
	 * @return the queue with instantiated callable constructs
	 */
	@Override
	public Queue<CallableConstruct> instantiateCallsFromTemplate(
			PositiveVariationPointCandidateTemplates variationPointCandidateTemplate,
			AllVariablesMapper allVariablesMapper) {
		
		return null;
	}
}
