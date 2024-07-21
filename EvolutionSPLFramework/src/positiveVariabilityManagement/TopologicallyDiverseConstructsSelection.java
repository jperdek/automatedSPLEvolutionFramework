package positiveVariabilityManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.ExportAssetPlanner;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.AlreadyChosenVariationPointForInjectionException;


/**
 * Selects code injections according to similarities between dependencies (semantic nature) between selected code constructs (content) in place of selected variation points
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class TopologicallyDiverseConstructsSelection implements SelectionOfConstructsAcrossSelectedVariationPointsStrategies {


	@Override
	/**
	 * Selects code injections according to similarities between dependencies (semantic nature) between selected code constructs (content) in place of selected variation points
	 * 
	 * @param availableFunctionalitiesToVariationPointsMap - the maping of particular variation point identifier to selected and applicable injections of code contents
	 * @param exportAssetPlanner
	 * @return the list of content injection for selected variation points
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public List<VariationPointsContentInjection> aggregateAllPossibleInjections(
			Map<String, VariationPointContentsInjection> availableFunctionalitiesToVariationPointsMap,
			ExportAssetPlanner exportAssetPlanner) throws IOException, InterruptedException, AlreadyChosenVariationPointForInjectionException {
		
		Map<String, VariationPointContentsInjection> topologicallyDiverseConstructsSelection 
				= new HashMap<String, VariationPointContentsInjection>(availableFunctionalitiesToVariationPointsMap);	
		VariationPointContentsInjection comparedInjection1, comparedInjection2;
		List<Entry<String, VariationPointContentsInjection>> variationPointsContentInjection 
					= new ArrayList<Entry<String, VariationPointContentsInjection>>(
							availableFunctionalitiesToVariationPointsMap.entrySet());
		Entry<String, VariationPointContentsInjection> constructEntry1, constructEntry2;

		String chosenVariationPointName;
		for (int index1 = 0; index1 < variationPointsContentInjection.size(); index1++) {
			for (int index2 = index1 + 1; index2 < variationPointsContentInjection.size(); index2++) {
				constructEntry1 = variationPointsContentInjection.get(index1);
				constructEntry2 = variationPointsContentInjection.get(index2);
				comparedInjection1 = constructEntry1.getValue();
				comparedInjection2 = constructEntry2.getValue();
		
				if (comparedInjection1.evaluateSimilarityOnDependencies(comparedInjection2)) {
					chosenVariationPointName = constructEntry2.getKey();
					topologicallyDiverseConstructsSelection.remove(chosenVariationPointName);
				}
			}
		}
		System.out.println(topologicallyDiverseConstructsSelection.size());
		return new AllVariationPointContentInjectionAggregator().aggregateAllPossibleInjections(topologicallyDiverseConstructsSelection, exportAssetPlanner);
	}
}
