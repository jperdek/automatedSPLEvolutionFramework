package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies;


import java.util.Queue;
import positiveVariabilityManagement.UnmappedContextException;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Prescribes contract for strategies to performs instantiations from selected callable constructs which parameters are substituted with given imported or available variables
 * 
 * 
 * @author Jakub Perdek
 *
 */
public interface CallsInstantiationFromTemplate {

	/**
	 * Prescribe the function to perform instantiations from selected callable constructs which parameters are substituted with given imported or available variables
	 * 
	 * @param variationPointCandidateTemplate - the template with information about particular/processed positive variability variation point (where functionality can be injected)
	 * @param allVariablesMapper - the mapping of variable/parameter types to related information 
	 * @return the queue with instantiated callable constructs
	 * 
	 * @throws UnmappedContextException - the exception informing about impossibility to map content on the resulting AST of final product in the synthesis process
	 */
	public Queue<CallableConstruct> instantiateCallsFromTemplate(
			PositiveVariationPointCandidateTemplates variationPointCandidateTemplate, 
			AllVariablesMapper allVariablesMapper) throws UnmappedContextException;
}
