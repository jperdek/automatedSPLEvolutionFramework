package positiveVariabilityManagement.fragmentManagement;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import positiveVariabilityManagement.fragmentManagement.model.CodeFragment;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;
import splEvolutionCore.candidateSelector.valueAssignment.AssignedValue;

/**
 * Management of similarity-based code aggregations that belongs to positive variability
 * 
 * @author Jakub Perdek
 *
 */
public class SimilarityBasedGranularityManagementStrategy implements CodeIncrementGranularityManagementStrategy {

	/**
	 * Creates similarity based granularity management strategy
	 */
	public SimilarityBasedGranularityManagementStrategy() {}

	@Override
	/**
	 * Associates/synthesizes the code constructs together under given code structures and according to similarities between each pair of constructs
	 * 
	 * @param variationPointConstructs - the list of associated code constructs with selected variation point that can be aggregated under new code construct 
	 * @param selectedTemplate - to find used positive variability variation point where code should be aggregated and
	 *  get relevant export locations of associated code constructs
	 * @return the code fragment that is created from code constructs in place of selected positive variability variation point
	 */
	public CodeFragment associateConstructsTogether(
			List<Entry<String, Map<String, AssignedValue>>> variationPointConstructs,
			PositiveVariationPointCandidateTemplates selectedTemplate) {
		
		throw new UnsupportedOperationException("Not implemented!");
		//return null;
	}
}
