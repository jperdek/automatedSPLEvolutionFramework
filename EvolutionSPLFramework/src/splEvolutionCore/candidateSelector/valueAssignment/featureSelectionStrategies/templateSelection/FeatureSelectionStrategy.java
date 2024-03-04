package splEvolutionCore.candidateSelector.valueAssignment.featureSelectionStrategies.templateSelection;

import java.util.List;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Selection strategy to select features
 * 
 * @author Jakub Perdek
 *
 */
public interface FeatureSelectionStrategy {

	/**
	 * Selects features in place of variation points to apply further selections
	 * 
	 * @param positiveVariationPointCandidatesTemplates - the list of features/positive variation points representation that will be selected
	 * @return the list of selected positive variation points where new functionality should be inserted  
	 */
	public List<PositiveVariationPointCandidateTemplates> selectFeatures(
			List<PositiveVariationPointCandidateTemplates> positiveVariationPointCandidatesTemplates);
}
