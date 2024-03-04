package positiveVariabilityManagement.callsTemplateSelectionStrategies;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Strategy to extract calls outside recursively called function/constructor inside hierarchy
 * -the context identifier is used to identify classes, methods and inner objects inside hierarchy (example: ClassB.getA.setC)
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class RecursionReducedCallsFromPositiveVariationPointCreator implements CallsFromPositiveVariationPointCreator {

	/**
	 * Extracts calls outside recursively called function/constructor inside hierarchy
	 * 
	 * @param variationPointCandidateTemplate - the template with information about particular/processed positive variability variation point (where functionality can be injected)
	 * @return the list of extracted calls inside recursively called function/constructor from positive variation point
	 */
	@Override
	public List<String> extractCallsFromPositiveVariationPoint(
			PositiveVariationPointCandidateTemplates variationPointCandidateTemplate) {
		JSONObject variationPointData = variationPointCandidateTemplate.getVariationPointData();
		JSONArray callableObjects = (JSONArray) variationPointData.get("allAvailableCalls");
		String contextStringIdentifier = (String) variationPointData.get("contextStringIdentifier");
		String contextStringIdentifierParts[] = contextStringIdentifier.split(".");
		
		List<String> calls = new ArrayList<String>();
		String callTemplate, methodName;
		int bracketIndex;
		boolean found = false;
		if(callableObjects == null) { return calls; } 
		for (Object callableStringObject: callableObjects) {
			callTemplate = (String) callableStringObject;
			bracketIndex = callTemplate.indexOf('(');
			found = false;
			if (bracketIndex > 0) {
				methodName = callTemplate.substring(0, bracketIndex - 1).strip();
				for (String contextStringIdentifierPart: contextStringIdentifierParts) {
					if (contextStringIdentifierPart.strip().equals(methodName)) {
						found = true;
					}
				}
				if (callTemplate.strip().startsWith("new ")) {
					continue; // omits creation of entities
				}
				if (callTemplate.strip().startsWith(".")) {
					callTemplate = callTemplate.strip().substring(1); //inner function call
					continue;
				}
				if (!found) { calls.add(callTemplate); }
			} else {
				calls.add(callTemplate);
			}
		}
		return calls;
	}
}
