package splEvolutionCore.candidateSelector.valueAssignment.featureSelectionStrategies.templateSelection;


import java.util.ArrayList;
import java.util.List;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Selection of up to constant number of variation points 
 * - the strategy to select from positive variation points 
 * 
 * @author Jakub Perdek
 *
 */
public class SelectUpToConstantConstructsAsFeatures implements FeatureSelectionStrategy {

	/**
	 * Maximal number of positive variation points that will be selected and processed
	 */
	protected int numberSelectedFeatures;
	
	
	/**
	 * Creates selection strategy to select up to given constant number of positive variation points
	 *  
	 * @param numberSelectedFeatures - maximal number of positive variation points that will be selected and processed
	 */
	public SelectUpToConstantConstructsAsFeatures(int numberSelectedFeatures) {
		this.numberSelectedFeatures = numberSelectedFeatures;
	}
	
	/**
	 * Sort positive variation points according to the value of their candidates
	 * 
	 * @param list - the list of positive variation points
	 */
	private static void sort(List<PositiveVariationPointCandidateTemplates> list) {
        list.sort((o1, o2) -> (int) Math.round(o2.evaluateCandidateValue() - o1.evaluateCandidateValue()));
    }
	 
	/**
	 * Reverse sort of positive variation point candidate templates according their evaluated candidate value (from chosen metrics)
	 * -FROM HIGHEST VALUE TO LOWER - good to put empty code candidates to the end
	 * 
	 * @param list - the list of positive variation point candidate templates that are goiung to be sorted with reverse sort
	 */
	private static void sortReverse(List<PositiveVariationPointCandidateTemplates> list) {
        list.sort((o1, o2) -> (int) Math.round(o1.evaluateCandidateValue() - o2.evaluateCandidateValue()));
    }
	
	@Override
	/**
	 * Selects features in place of variation points to apply further selections
	 * 
	 * @param positiveVariationPointCandidatesTemplates - the list of features/positive variation points representation that will be selected
	 * @return the list of selected positive variation points where new functionality should be inserted  
	 */
	public List<PositiveVariationPointCandidateTemplates> selectFeatures(
			List<PositiveVariationPointCandidateTemplates> positiveVariationPointCandidates) {
		this.sort(positiveVariationPointCandidates); //good to put empty code candidates to the end
		int maxFeatureCandidates = this.numberSelectedFeatures;
		if (positiveVariationPointCandidates.size() < this.numberSelectedFeatures) {
			maxFeatureCandidates = positiveVariationPointCandidates.size() - 1;
		}
		return new ArrayList<PositiveVariationPointCandidateTemplates>(
				positiveVariationPointCandidates.subList(0, maxFeatureCandidates));
	}

}
