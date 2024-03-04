package splEvolutionCore.candidateSelector;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * Manages selection of positive variability variation points from the list of all managed variation points
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class PositiveVariationPointCandidateSelection {

	/**
	 * Key in JSON that represents information belongs to positive variability (if given variation point is new)
	 */
	private final static String NEW_VARIATION_POINT = "newVariationPoint";

	/**
	 * Creates the instance of PositiveVariationPointCandidateSelection
	 */
	public PositiveVariationPointCandidateSelection() {
	}
	
	/**
	 * Extracts the variation point and associates all available data to it if the given variation point is positive
	 * 
	 * @param variationPointArray - the managed JSON array of variation points
	 * @return the list of (parameterized/template) positive variation point candidate templates
	 */
	public static List<PositiveVariationPointCandidateTemplates> createPositiveVariabilityCandidates(JSONArray variationPointArray) {
		List<PositiveVariationPointCandidateTemplates> positiveVariationCandidates = new ArrayList<PositiveVariationPointCandidateTemplates>();
		PositiveVariationPointCandidateTemplates createdCandidate;
		
		JSONObject variationPointData;
		for (Object variationPointObject: variationPointArray) {
			variationPointData = (JSONObject) variationPointObject;
			// the test if variation point belongs to positive variability
			if ((boolean) variationPointData.get(PositiveVariationPointCandidateSelection.NEW_VARIATION_POINT) == true) {
				createdCandidate = new PositiveVariationPointCandidateTemplates(variationPointData);
				positiveVariationCandidates.add(createdCandidate); //candidate is appended
			}
		}
		return positiveVariationCandidates;
	}
}
