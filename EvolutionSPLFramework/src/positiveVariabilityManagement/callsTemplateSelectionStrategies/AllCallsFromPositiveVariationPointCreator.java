package positiveVariabilityManagement.callsTemplateSelectionStrategies;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Strategy to extract all available calls from positive variation point representation
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class AllCallsFromPositiveVariationPointCreator implements CallsFromPositiveVariationPointCreator {

	/**
	 * Extracts all calls available at positive variation point except constructor calls (creation of entities)
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
					//continue;
					callTemplate = callTemplate.strip().substring(1);
				}
				System.out.println(callTemplate);
				calls.add(callTemplate);
			}
		}
		return calls;
	}
}
