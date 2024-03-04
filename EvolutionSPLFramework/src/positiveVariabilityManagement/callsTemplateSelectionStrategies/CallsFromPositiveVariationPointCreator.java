package positiveVariabilityManagement.callsTemplateSelectionStrategies;

import java.util.List;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Prescribes contract for strategies to extract calls for positive variation point
 * 
 * 
 * @author Jakub Perdek
 *
 */
public interface CallsFromPositiveVariationPointCreator {


	/**
	 * The contract prescribed a strategy to extract calls for positive variation point
	 * - to restrict callables such as recursion if function is called,...
	 * 
	 * @param variationPointCandidateTemplate - the template with information about particular/processed positive variability variation point (where functionality can be injected)
	 * @return the list of extracted calls from positive variation point
	 */
	public List<String> extractCallsFromPositiveVariationPoint(PositiveVariationPointCandidateTemplates variationPointCandidateTemplate);
}
