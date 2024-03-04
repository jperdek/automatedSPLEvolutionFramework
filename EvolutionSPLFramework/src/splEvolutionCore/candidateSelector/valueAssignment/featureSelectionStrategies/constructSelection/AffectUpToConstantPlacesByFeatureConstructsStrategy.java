package splEvolutionCore.candidateSelector.valueAssignment.featureSelectionStrategies.constructSelection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import splEvolutionCore.SPLEvolutionCore.SyntetizeConstructsOptions;
import splEvolutionCore.candidateSelector.valueAssignment.AssignedValue;


/**
 * The strategy to affect up to constant places by feature constructs
 * 
 * @author Jakub Perdek
 *
 */
public class AffectUpToConstantPlacesByFeatureConstructsStrategy implements FeatureConstructsSelectionStrategy {

	/**
	 * The number of different code variants
	 */
	private int numberDifferentCodeVariants;
	
	/**
	 * Selected option to select amongst feature constructs
	 */
	private SyntetizeConstructsOptions optionForSynthesis;
	

	/**
	 * Creates strategy to affect up to given number of places
	 * 
	 * @param numberDifferentCodeVariants - the number of different code variants
	 * @param optionForSynthesis - decision which construct selection strategy should be used
	 */
	public AffectUpToConstantPlacesByFeatureConstructsStrategy(
			int numberDifferentCodeVariants, SyntetizeConstructsOptions optionForSynthesis) {
		this.numberDifferentCodeVariants = numberDifferentCodeVariants;
		this.optionForSynthesis = optionForSynthesis;
	}

	/**
	 * Sorts the list of callable code constructs according to their mapped, assigned, and associated quality metrics
	 * 
	 * @param list - the list of mapped callable code constructs to their associated and assigned metrics
	 */
	private static void sort(List<Entry<String, Map<String, AssignedValue>>> list) {
        list.sort((o1, o2) -> (int) Math.round(evaluateCandidateValue(o2.getValue()) - evaluateCandidateValue(o1.getValue())));
    }
	
	/**
	 * Sorts the list of callable code constructs according to their mapped, assigned, and associated quality metrics in reverse order
	 * 
	 * @param list - the list of mapped constructs to their associated and assigned metrics
	 */
	private static void sortReverse(List<Entry<String, Map<String, AssignedValue>>> list) {
        list.sort((o1, o2) -> (int) Math.round(evaluateCandidateValue(o1.getValue()) - evaluateCandidateValue(o2.getValue())));
    }

	/**
	 * Evaluates the callable code construct value according to associated quality metrics
	 * 
	 * @param assignedValues - mapping of the name to associated value of given metrics
	 * @return evaluated candidate value according associated quality metrics
	 */
	public static double evaluateCandidateValue(Map<String, AssignedValue> assignedValues) {
		double candidateValue = 0;
		for(AssignedValue processedAssignedValue: assignedValues.values()) {
			candidateValue = candidateValue + processedAssignedValue.calculateOverallValue();
		}
		return candidateValue;
	}

	@Override
	/**
	 * Generates implemented feature constructs according to implementation of given strategy
	 * 
	 * @param positiveVariationPointCandidateConstructs - 
	 * @return the list of selected constructs from given positive variation point
	 */
	public List<Entry<String, Map<String, AssignedValue>>> selectFeatures(
			List<Entry<String, Map<String, AssignedValue>>> positiveVariationPointCandidateConstructs) {
		this.sortReverse(positiveVariationPointCandidateConstructs);
		return new ArrayList<Entry<String, Map<String, AssignedValue>>>(
				positiveVariationPointCandidateConstructs.subList(0, this.numberDifferentCodeVariants));
	}

	@Override
	/**
	 * Generates all possible selections of feature constructs from given positive variation point
	 * 
	 * @param positiveVariationPointCandidateConstructs - constructs from 
	 * @return the list of all possible selections of feature constructs from given variation point
	 */
	public List<List<Entry<String, Map<String, AssignedValue>>>> selectFeaturesAllCases(
			List<Entry<String, Map<String, AssignedValue>>> positiveVariationPointCandidateConstructs) {
		return FeatureConstructsSelectionStrategy.selectFeaturesAllCases(
				positiveVariationPointCandidateConstructs, this.optionForSynthesis);
	}	
}
