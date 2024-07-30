package positiveVariabilityManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.ExportAssetPlanner;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.AlreadyChosenVariationPointForInjectionException;
import positiveVariabilityManagement.fragmentManagement.model.CodeFragment;
import splEvolutionCore.DebugInformation;
import splEvolutionCore.SPLEvolutionCore;


/**
 * Creates all possible code injections from the selected code constructs (content) in place of selected variation points
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class AllVariationPointContentInjectionAggregator implements SelectionOfConstructsAcrossSelectedVariationPointsStrategies {

	/**
	 * Instantiates the instance with implementation of strategy to select constructs among one or multiple previously selected variation points 
	 */
	public AllVariationPointContentInjectionAggregator() {}
	
	/**
	 * Selects constructs among one or multiple previously selected variation points from which the content injections 
	 * with all information about positive variability (new functionality) required for derivation of final products are created
	 * 
	 * @param availableFunctionalitiesToVariationPointsMap - the mapping of selected code constructs to variation points according to previously processed the context information
	 * @param exportAssetPlanner - asset planning instance that provides the checking if injection can be created for particular asset
	 * @return the list of all possible content injections where each is used for further product derivation
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws AlreadyChosenVariationPointForInjectionException 
	 */
	public List<VariationPointsContentInjection> aggregateAllPossibleInjections(
			Map<String, VariationPointContentsInjection> availableFunctionalitiesToVariationPointsMap,
			ExportAssetPlanner exportAssetPlanner) throws IOException, InterruptedException, AlreadyChosenVariationPointForInjectionException {
		Map<VariationPointsContentInjection, String> contentInjectionToNameMap = new HashMap<VariationPointsContentInjection, String>();
		Map<CodeFragment, Integer> codeFragmentToIndexMap = new HashMap<CodeFragment, Integer>();
		Queue<VariationPointsContentInjection> harvestedContentMarkerCapacities = new LinkedList<VariationPointsContentInjection>();
		Queue<VariationPointsContentInjection> harvestedContentMarkerCapacities2 = new LinkedList<VariationPointsContentInjection>();
		List<VariationPointsContentInjection> harvestedContentMarkerCapacitiesAll = new ArrayList<VariationPointsContentInjection>();
		Queue<VariationPointsContentInjection> helper;
		VariationPointsContentInjection variationPointsContentInjectionPart;
		VariationPointsContentInjection variationPointsContentInjection, newVariationPointsContentInjection;

		String variationPointMarkerName;
		VariationPointContentsInjection variationPointContentsInjection;
		
		int index = 0;
		for (Entry<String, VariationPointContentsInjection> availableFunctionalitiesEntry:
				availableFunctionalitiesToVariationPointsMap.entrySet()) {
			variationPointMarkerName = availableFunctionalitiesEntry.getKey();
			variationPointContentsInjection = availableFunctionalitiesEntry.getValue();
			if(DebugInformation.PROCESS_STEP_INFORMATION) {
				if (variationPointContentsInjection.getCodeFragments().size() == 0 
						&& !DebugInformation.SHOW_POLLUTING_INFORMATION) { continue; }
				
				System.out.println("Added vp name: " + variationPointMarkerName + " with fragments: " + 
						variationPointContentsInjection.getCodeFragments().size());
				
			}
					
			for (CodeFragment codeFragment: variationPointContentsInjection.getCodeFragments()) {
				newVariationPointsContentInjection = new VariationPointsContentInjection(exportAssetPlanner);
				if (newVariationPointsContentInjection.addCodeFragmentReference(variationPointMarkerName, codeFragment)) {
					contentInjectionToNameMap.put(newVariationPointsContentInjection, variationPointMarkerName);
					codeFragmentToIndexMap.put(codeFragment, index);
					index++;
				} else {
					System.out.println("Injection not prepared from code fragment in variation point named: " + variationPointMarkerName);
				}
			}
		}

		harvestedContentMarkerCapacities.add(new VariationPointsContentInjection(exportAssetPlanner));
		Entry<VariationPointsContentInjection, String> availableFunctionalitiesEntry;
		List<Entry<VariationPointsContentInjection, String>> availableFunctionalitiesEntryList = 
				new ArrayList<Entry<VariationPointsContentInjection, String>>(contentInjectionToNameMap.entrySet());
		if(DebugInformation.PROCESS_STEP_INFORMATION) {
			System.out.println();
			System.out.print("AGGREGATING...........");
		}

		int startIndex;
		CodeFragment initialCodeFragment;
		for (int iteration = 0; iteration < SPLEvolutionCore.DEFAULT_NUMBER_OF_AGGREGATED_OVERALL_CONSTRUCTS_TO_SELECT; iteration++) {
			while(!harvestedContentMarkerCapacities.isEmpty()) {
				variationPointsContentInjection = harvestedContentMarkerCapacities.poll();
				initialCodeFragment = variationPointsContentInjection.getLastUsedCodeFragment();
				if (initialCodeFragment == null) {
					startIndex = 0;
				} else {
					startIndex = codeFragmentToIndexMap.get(initialCodeFragment);
				}
				
				for (int innerIndex = startIndex + 1; innerIndex < availableFunctionalitiesEntryList.size(); innerIndex++) {
					availableFunctionalitiesEntry = availableFunctionalitiesEntryList.get(innerIndex);
					variationPointMarkerName = availableFunctionalitiesEntry.getValue();
					variationPointsContentInjectionPart = availableFunctionalitiesEntry.getKey();
		
					for (CodeFragment codeFragment: variationPointsContentInjectionPart.getCodeFragments()) {
						newVariationPointsContentInjection = new VariationPointsContentInjection(variationPointsContentInjection, exportAssetPlanner);
						if(DebugInformation.SHOW_POLLUTING_INFORMATION) {
							System.out.println(variationPointMarkerName + " <---*****---> " + codeFragment.getCode());
						}
						if (newVariationPointsContentInjection.addCodeFragmentReference(variationPointMarkerName, codeFragment)) {
							harvestedContentMarkerCapacities2.add(newVariationPointsContentInjection);
						} else {
							System.out.println("Injection not prepared from code fragment in variation point named: " + variationPointMarkerName);
						}
					}
				}
			}
			
			harvestedContentMarkerCapacitiesAll.addAll(new ArrayList<VariationPointsContentInjection>(harvestedContentMarkerCapacities2));
			helper = harvestedContentMarkerCapacities;
			harvestedContentMarkerCapacities = harvestedContentMarkerCapacities2;
			harvestedContentMarkerCapacities2 = helper;
			
			if (SPLEvolutionCore.MAX_SPL_INSTANCES_TO_DERIVE < harvestedContentMarkerCapacitiesAll.size()) {
				System.out.println("Maximal number of instances is reached.");
				break;
			}
		}
		if(DebugInformation.PROCESS_STEP_INFORMATION) {
			System.out.println("..........." + harvestedContentMarkerCapacitiesAll.size());
		}
		return harvestedContentMarkerCapacitiesAll;
	}
}
