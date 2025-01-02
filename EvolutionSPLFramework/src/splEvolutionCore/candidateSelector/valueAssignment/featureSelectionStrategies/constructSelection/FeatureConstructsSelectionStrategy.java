package splEvolutionCore.candidateSelector.valueAssignment.featureSelectionStrategies.constructSelection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;

import splEvolutionCore.DebugInformation;
import splEvolutionCore.SPLEvolutionCore.SyntetizeConstructsOptions;
import splEvolutionCore.candidateSelector.valueAssignment.AssignedValue;
import splEvolutionCore.derivation.VariationPointConjunctor;


/**
 * Selection strategy of feature constructs from features
 * 
 * @author Jakub Perdek
 *
 */
public interface FeatureConstructsSelectionStrategy {
	
	/**
	 * Logger to track information from feature constructs selection strategy
	 */
	static final Logger logger = LoggerFactory.getLogger(FeatureConstructsSelectionStrategy.class);
	
	/**
	 * Generates implemented feature constructs according to implementation of given strategy
	 * 
	 * @param positiveVariationPointCandidateConstructs - 
	 * @return the list of selected constructs from given positive variation point
	 */
	public List<Entry<String, Map<String, AssignedValue>>> selectFeatures(
			List<Entry<String, Map<String, AssignedValue>>> positiveVariationPointCandidateConstructs);
	
	/**
	 * Generates all possible selections of feature constructs from given positive variation point
	 * 
	 * @param positiveVariationPointCandidateConstructs - constructs from 
	 * @return the list of all possible selections of feature constructs from given variation point
	 */
	public List<List<Entry<String, Map<String, AssignedValue>>>> selectFeaturesAllCases(
			List<Entry<String, Map<String, AssignedValue>>> positiveVariationPointCandidateConstructs);

	/**
	 * Generates all possible selections of feature constructs from given positive variation point
	 * 
	 * @param positiveVariationPointCandidateConstructsSelection - the list of selected constructs mapped to associated and assigned metrics 
	 * @param optionForSynthesis - decision which construct selection strategy should be used
	 * @return the list of all possible selections of feature constructs from given variation point
	 */
	public static List<List<Entry<String, Map<String, AssignedValue>>>> selectFeaturesAllCases(
			List<Entry<String, Map<String, AssignedValue>>> positiveVariationPointCandidateConstructsSelection, 
			SyntetizeConstructsOptions optionForSynthesis) {
		List<List<Entry<String, Map<String, AssignedValue>>>> constructFeaturesForResultingProduct;

		List<Entry<String, Map<String, AssignedValue>>> newSubList;
		if (optionForSynthesis == SyntetizeConstructsOptions.ALL_SELECTED) {
			constructFeaturesForResultingProduct = new ArrayList<List<Entry<String, Map<String, AssignedValue>>>>();
			constructFeaturesForResultingProduct.add(positiveVariationPointCandidateConstructsSelection);
		} else if (optionForSynthesis == SyntetizeConstructsOptions.EXACTLY_ONE_SELECTED) {
			constructFeaturesForResultingProduct = new ArrayList<List<Entry<String, Map<String, AssignedValue>>>>();
			for (Entry<String, Map<String, AssignedValue>> exactlyOneConstruct: positiveVariationPointCandidateConstructsSelection) {
				newSubList = new ArrayList<Entry<String, Map<String, AssignedValue>>>();
				newSubList.add(exactlyOneConstruct);
				constructFeaturesForResultingProduct.add(newSubList);
			}
		} else if (optionForSynthesis == SyntetizeConstructsOptions.UP_TO_SELECTED_FEATURES) {
			constructFeaturesForResultingProduct = new LinkedList<List<Entry<String, Map<String, AssignedValue>>>>();
			for (Entry<String, Map<String, AssignedValue>> exactlyOneConstruct: positiveVariationPointCandidateConstructsSelection) {
				newSubList = new ArrayList<Entry<String, Map<String, AssignedValue>>>();
				
				newSubList.add(exactlyOneConstruct);
				constructFeaturesForResultingProduct.add(newSubList);
			}
			
			if (optionForSynthesis == SyntetizeConstructsOptions.UP_TO_SELECTED_FEATURES) {
				constructFeaturesForResultingProduct = FeatureConstructsSelectionStrategy.createUpToGivenFeatureConstructs(
						positiveVariationPointCandidateConstructsSelection, constructFeaturesForResultingProduct.size());
			}
		} else {
			constructFeaturesForResultingProduct = new ArrayList<List<Entry<String, Map<String, AssignedValue>>>>();
			constructFeaturesForResultingProduct.add(positiveVariationPointCandidateConstructsSelection);
		}
		return constructFeaturesForResultingProduct;
	}
	
	/**
	 * Creates up to given number of feature constructs from given variation point
	 * 
	 * @param constructFeaturesForResultingProduct - the list of constructs from given variation point
	 * @param maximalNumberOfConstructs - the maximal number/constant of feature constructs
	 * @return created list of all selections up to given number/constant of callable constructs from positive variation point
	 */
	private static List<List<Entry<String, Map<String, AssignedValue>>>> createUpToGivenFeatureConstructs(
			List<Entry<String, Map<String, AssignedValue>>> constructFeaturesForResultingProduct, int maximalNumberOfConstructs) {
		Queue<IndexedEntity<List<Entry<String, Map<String, AssignedValue>>>>> baseConstructFeaturesForResultingProduct = 
				new LinkedList<IndexedEntity<List<Entry<String, Map<String, AssignedValue>>>>>();
		Queue<IndexedEntity<List<Entry<String, Map<String, AssignedValue>>>>> baseConstructFeaturesForResultingProduct2, swap;
		
		List<List<Entry<String, Map<String, AssignedValue>>>> finalConstructFeaturesForResultingProduct = 
				new ArrayList<List<Entry<String, Map<String, AssignedValue>>>>();
		
		Entry<String, Map<String, AssignedValue>> processedEntity;
		IndexedEntity<List<Entry<String, Map<String, AssignedValue>>>> indexedEntity;
		IndexedEntity<List<Entry<String, Map<String, AssignedValue>>>> initialIndexedEntity;
		List<Entry<String, Map<String, AssignedValue>>> actualConstructFeaturesForResultingProduct;
		List<Entry<String, Map<String, AssignedValue>>> actualComposedConstructFeaturesForResultingProduct;
		List<IndexedEntity<List<Entry<String, Map<String, AssignedValue>>>>> actualIterationConstructFeatures;
		
		for(int index = 0; index < constructFeaturesForResultingProduct.size(); index++) {
			actualComposedConstructFeaturesForResultingProduct = new ArrayList<Entry<String, Map<String, AssignedValue>>>();
			actualComposedConstructFeaturesForResultingProduct.add(constructFeaturesForResultingProduct.get(index));
			indexedEntity = new IndexedEntity<List<Entry<String, Map<String, AssignedValue>>>>(
					actualComposedConstructFeaturesForResultingProduct, index);
			
			baseConstructFeaturesForResultingProduct.add(indexedEntity);
			finalConstructFeaturesForResultingProduct.add(actualComposedConstructFeaturesForResultingProduct);
		}
		if (DebugInformation.SHOW_POLLUTING_INFORMATION) {
			logger.debug("MAXIMAL NUMBER OF CONSTUCTS: " + maximalNumberOfConstructs);
			logger.debug("CONSTRUCTS FOR RESULTING PRODUCT: " + constructFeaturesForResultingProduct.size());
		}

		int index1;
		for(int iteration = 0; iteration < maximalNumberOfConstructs; iteration++) {
			baseConstructFeaturesForResultingProduct2 = new LinkedList<IndexedEntity<List<Entry<String, Map<String, AssignedValue>>>>>();

			while (!baseConstructFeaturesForResultingProduct.isEmpty()) {
				actualIterationConstructFeatures = new ArrayList<IndexedEntity<List<Entry<String, Map<String, AssignedValue>>>>>();
				initialIndexedEntity = baseConstructFeaturesForResultingProduct.poll(); 
				index1 = initialIndexedEntity.getIndex();
				
				actualConstructFeaturesForResultingProduct = new ArrayList<Entry<String, Map<String, AssignedValue>>>();
				//processedEntity = initialIndexedEntity.getMappedObject();
				actualIterationConstructFeatures.add(initialIndexedEntity);
				
				for(int index2 = index1 + 1; index2 < constructFeaturesForResultingProduct.size(); index2++) {
					processedEntity = constructFeaturesForResultingProduct.get(index2);
					
					actualComposedConstructFeaturesForResultingProduct = new ArrayList<
							Entry<String, Map<String, AssignedValue>>>(actualConstructFeaturesForResultingProduct);
					actualComposedConstructFeaturesForResultingProduct.add(processedEntity);
					
					indexedEntity = new IndexedEntity<List<Entry<String, Map<String, AssignedValue>>>>(
							actualComposedConstructFeaturesForResultingProduct, index2);
					baseConstructFeaturesForResultingProduct2.add(indexedEntity);
					
					finalConstructFeaturesForResultingProduct.add(actualComposedConstructFeaturesForResultingProduct);
				}
			}
			swap = baseConstructFeaturesForResultingProduct;
			baseConstructFeaturesForResultingProduct = baseConstructFeaturesForResultingProduct2;
			baseConstructFeaturesForResultingProduct2 = swap;
		}
		return finalConstructFeaturesForResultingProduct;
	}
}
