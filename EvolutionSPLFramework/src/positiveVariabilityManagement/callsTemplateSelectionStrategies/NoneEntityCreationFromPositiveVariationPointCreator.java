package positiveVariabilityManagement.callsTemplateSelectionStrategies;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Strategy to extract available calls from positive variation point representation 
 * except calls with intervention of any entity (entity creation or accessing its functions or attributes where dot notation is used...)
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class NoneEntityCreationFromPositiveVariationPointCreator implements CallsFromPositiveVariationPointCreator {

	/**
	 * Extracts calls except calls with intervention of any entity (entity creation or accessing its functions or attributes where dot notation is used...)
	 * 
	 * @param variationPointCandidateTemplate - the template with information about particular/processed positive variability variation point (where functionality can be injected)
	 * @return the list of extracted calls from positive variation point
	 */
	@Override
	public List<String> extractCallsFromPositiveVariationPoint(
			PositiveVariationPointCandidateTemplates variationPointCandidateTemplate) {
		JSONObject variationPointData = variationPointCandidateTemplate.getVariationPointData();
		JSONArray callableObjects = (JSONArray) variationPointData.get("allAvailableCalls");
		
		List<String> calls = new ArrayList<String>();
		String callTemplate;
		if (callableObjects != null) {
			for (Object callableStringObject: callableObjects) {
				callTemplate = (String) callableStringObject;
				if (callTemplate.strip().startsWith("new ")) {
					continue; // omits creation of entities
				}
				if (callTemplate.strip().startsWith(".")) {
					continue;
					//callTemplate = callTemplate.strip().substring(1);
				}
				calls.add(callTemplate);
			}
		}
		return calls;
	}
}
