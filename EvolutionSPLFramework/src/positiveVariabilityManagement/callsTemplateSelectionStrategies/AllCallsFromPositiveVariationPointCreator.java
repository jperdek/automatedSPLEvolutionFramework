package positiveVariabilityManagement.callsTemplateSelectionStrategies;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import positiveVariabilityManagement.entities.VariablesForSubstantiation;
import splEvolutionCore.DebugInformation;
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
	 * Logger to track strategy to get all calls from positive variation point creator
	 */
	private static final Logger logger = LoggerFactory.getLogger(AllCallsFromPositiveVariationPointCreator.class);
	
	/**
	 * Instantiates AllCallsFromPositiveVariationPointCreator
	 */
	public AllCallsFromPositiveVariationPointCreator() {}
	
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
				if (DebugInformation.SHOW_POLLUTING_INFORMATION) { logger.debug("Extracted template call: " + callTemplate); }
				calls.add(callTemplate);
			}
		}
		return calls;
	}
}
