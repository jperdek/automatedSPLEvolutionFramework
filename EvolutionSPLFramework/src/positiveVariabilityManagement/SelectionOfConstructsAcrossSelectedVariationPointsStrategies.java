package positiveVariabilityManagement;

import java.util.List;
import java.util.Map;


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
	 * @param availableFunctionalitiesToVariationPointsMap - the maping of particular variation point identifier to selected and applicable injections of code contents 
	 * @return the list of content injection for selected variation points
	 */
	public List<VariationPointsContentInjection> aggregateAllPossibleInjections(
			Map<String, VariationPointContentsInjection> availableFunctionalitiesToVariationPointsMap);
}
