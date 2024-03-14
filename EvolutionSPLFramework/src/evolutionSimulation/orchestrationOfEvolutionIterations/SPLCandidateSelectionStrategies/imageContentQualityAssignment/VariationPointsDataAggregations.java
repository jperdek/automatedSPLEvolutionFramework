package evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.imageContentQualityAssignment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;


public class VariationPointsDataAggregations {

	private Map<String, VariationPointsDataOrganizer> organizedVariationPoints;
	private Map<String, Set<VariationPointView>> relatednessAccordingToFunctionality;
	
	public VariationPointsDataAggregations() {
		this.organizedVariationPoints = new HashMap<String, VariationPointsDataOrganizer>();
		this.relatednessAccordingToFunctionality = new HashMap<String, Set<VariationPointView>>(); 
	}
	
	public void processVariationPointsData(JSONArray variationPointsJSONData, String productIdentifier) {
		VariationPointsDataOrganizer variationPointsDataOrganizer = new VariationPointsDataOrganizer(
				variationPointsJSONData, this.relatednessAccordingToFunctionality, productIdentifier);
		this.organizedVariationPoints.put(productIdentifier, variationPointsDataOrganizer);
	}
}
