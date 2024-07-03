package positiveVariabilityManagement;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.ExportAssetPlanner;


/**
 * Predefines methods to select code injections according to performed strategy from the selected code constructs (content) in place of selected variation points
 * 
 * 
 * @author Jakub Perdek
 *
 */
public interface SelectionOfConstructsAcrossSelectedVariationPointsStrategies {

	/**
	 * Predefines the selection of code injections according to performed strategy from the selected code constructs (content) in place of selected variation points
	 * 
	 * @param availableFunctionalitiesToVariationPointsMap - the mapping of particular variation point identifier to selected and applicable injections of code contents 
	 * @param exportAssetPlanner - asset planning instance that provides the checking if injection can be created for particular asset
	 * @return the list of content injection for selected variation points
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	public List<VariationPointsContentInjection> aggregateAllPossibleInjections(
			Map<String, VariationPointContentsInjection> availableFunctionalitiesToVariationPointsMap, 
			ExportAssetPlanner exportAssetPlanner) throws InterruptedException, IOException;
}
