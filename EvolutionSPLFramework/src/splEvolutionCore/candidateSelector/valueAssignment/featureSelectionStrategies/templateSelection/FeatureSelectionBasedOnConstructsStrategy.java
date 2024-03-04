package splEvolutionCore.candidateSelector.valueAssignment.featureSelectionStrategies.templateSelection;

import java.util.List;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidate;


/**
 * Selecting features based on intersection of constructs
 * 
 * @author Jakub Perdek
 *
 */
public interface FeatureSelectionBasedOnConstructsStrategy {

	/**
	 * Selects given features according to their construct value
	 * -constructs are evaluated amongst many variation points and chosen as the final choice
	 * 
	 * @param positiveVariationPointCandidates - the list of positive variation point candidates where given number of them is going to be selected
	 */
	public List<PositiveVariationPointCandidate> selectFeatures(
			List<PositiveVariationPointCandidate> positiveVariationPointCandidates);
}
