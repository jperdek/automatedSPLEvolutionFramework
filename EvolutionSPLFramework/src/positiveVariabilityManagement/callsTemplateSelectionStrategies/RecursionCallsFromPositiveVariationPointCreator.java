package positiveVariabilityManagement.callsTemplateSelectionStrategies;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Strategy to extract only calls inside recursively called function/constructor
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class RecursionCallsFromPositiveVariationPointCreator implements CallsFromPositiveVariationPointCreator {

	/**
	 * Logger to track strategy to get recursive calls from positive variation point creator
	 */
	private static final Logger logger = LoggerFactory.getLogger(RecursionCallsFromPositiveVariationPointCreator.class);
	
	/**
	 * Extracts only calls inside recursively called function/constructor
	 * 
	 * @param variationPointCandidateTemplate - the template with information about particular/processed positive variability variation point (where functionality can be injected)
	 * @return the list of extracted calls inside recursively called function/constructor from positive variation point
	 */
	@Override
	public List<String> extractCallsFromPositiveVariationPoint(
			PositiveVariationPointCandidateTemplates variationPointCandidateTemplate) {
		JSONObject variationPointData = variationPointCandidateTemplate.getVariationPointData();
		JSONArray callableObjects = (JSONArray) variationPointData.get("allAvailableCalls");
		
		List<String> calls = new ArrayList<String>();
		String callTemplate;
		boolean isPartOfRecursion = (boolean) variationPointData.get("isInsideRecursion");
		if(callableObjects == null) { return calls; } 
		if (isPartOfRecursion) {
			for (Object callableStringObject: callableObjects) {
				callTemplate = (String) callableStringObject;
				if (callTemplate.strip().startsWith("new ")) {
					continue; // omits creation of entities
				}
				if (callTemplate.strip().startsWith(".")) { //removes dot from the beginning
					//continue;
					callTemplate = callTemplate.strip().substring(1);
				}
				logger.debug("Extracted template call: " + callTemplate);
				calls.add(callTemplate);
			}
			if (calls.size() > 0) {
				return calls;
			}
		}
		return calls; //null;
	}
}
