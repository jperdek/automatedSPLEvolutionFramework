package splEvolutionCore.candidateSelector.valueAssignment.featureSelectionStrategies.templateSelection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidate;


/**
 * Selecting features/feature based on intersection of constructs
 * -constructs are evaluated amongst many variation points and chosen as the final choice
 * 
 * @author Jakub Perdek
 *
 */
public class FeatureSelectionBasedOnIntersectionOfConstructs implements FeatureSelectionBasedOnConstructsStrategy {
	
	/**
	 * Number of selected constructs from variation points
	 */
	private int numberSelectedFeaturesAsConstructs;
	
	
	/**
	 * Creates feature selection strategy that is based on selection of constructs
	 * 
	 * @param numberSelectedFeaturesAsConstructs - number of selected constructs from variation points
	 */
	public FeatureSelectionBasedOnIntersectionOfConstructs(int numberSelectedFeaturesAsConstructs) {
		this.numberSelectedFeaturesAsConstructs = numberSelectedFeaturesAsConstructs;
	}
	
	/**
	 * Sorts positive variation points according to overall candidate value
	 * 
	 * @param list - the list of positive variation point candidates that is going to be sorted
	 */
	private static void sort(List<PositiveVariationPointCandidate> list) {
        list.sort((o1, o2) -> {
        	Set<String> constructNamesSet = new HashSet<String>(o2.getPositiveVariationPointCandidates());
        	constructNamesSet.addAll(o1.getPositiveVariationPointCandidates());
        	
        	int overallScore = 0;
        	for (String constructName: constructNamesSet) {
        		overallScore = overallScore + ((int) Math.round(
        				o2.evaluateCandidateValue(constructName) - o1.evaluateCandidateValue(constructName)));
        	}
        	return overallScore;
        });
    }
	
	/**
	 * Sorts positive variation points with reverse sort according to overall candidate value
	 * 
	 * @param list - the list of positive variation point candidates that is going to be sorted with reverse sort
	 */
	private static void sortReverse(List<PositiveVariationPointCandidate> list) {
        list.sort((o1, o2) -> {
        	Set<String> constructNamesSet = new HashSet<String>(o1.getPositiveVariationPointCandidates());
        	constructNamesSet.addAll(o2.getPositiveVariationPointCandidates());
        	
        	int overallScore = 0;
        	for (String constructName: constructNamesSet) {
        		overallScore = overallScore + ((int) Math.round(
        				o1.evaluateCandidateValue(constructName) - o2.evaluateCandidateValue(constructName)));
        	}
        	return overallScore;
        });
    }

	@Override
	/**
	 * Selects given features according to their construct value
	 * 
	 * @param positiveVariationPointCandidates - the list of positive variation point candidates where given number of them is going ot be selected
	 * @return the array of selected features that represents intersection of constructs
	 */
	public List<PositiveVariationPointCandidate> selectFeatures(
			List<PositiveVariationPointCandidate> positiveVariationPointCandidates) {
		this.sortReverse(positiveVariationPointCandidates);
		return new ArrayList<PositiveVariationPointCandidate>(
				positiveVariationPointCandidates.subList(0, this.numberSelectedFeaturesAsConstructs));
	}
}
