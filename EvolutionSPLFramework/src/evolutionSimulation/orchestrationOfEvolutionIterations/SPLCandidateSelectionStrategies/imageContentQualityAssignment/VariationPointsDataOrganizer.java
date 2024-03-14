package evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.imageContentQualityAssignment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class VariationPointsDataOrganizer {

	
	public static enum CapturedRelations {
		INSIDE_CLASS("inClass"),
		OUTSIDE_CLASS("outClass"),
		POSITIVE_VARIABILITY("positiveVariability"),
		NEGATIVE_VARIABILITY("negativeVariability"),
		INSIDE_RECURSION("insideRecursion"),
		IS_VARIABLE("isVariable"),
		IS_CONSTRUCTOR("isConstructor"),
		IS_FUNCTION("isFunction");
		

		public final String label;

	    private CapturedRelations(String label) {
	        this.label = label;
	    }
	    
	    public String getName() { return this.label; }
	}

	public Map<CapturedRelations, Set<VariationPointView>> relatednessAccordingToRelations;
	
	public VariationPointsDataOrganizer(JSONArray variationPoints, 
			Map<String, Set<VariationPointView>> relatednessAccordingToFunctionality, String productIdentifier) {
		this.relatednessAccordingToRelations = new HashMap<CapturedRelations, Set<VariationPointView>>();
		
		String variationPointCodeFragmentHash;
		VariationPointView variationPointView;
		Set<VariationPointView> variationPointViewAggregation;
		for (Object variationPointsData: variationPoints) {
			variationPointView = new VariationPointView(productIdentifier, (JSONObject) variationPointsData);
			variationPointCodeFragmentHash = variationPointView.getVariationPointName().split("_")[1];
			variationPointViewAggregation = relatednessAccordingToFunctionality.get(variationPointCodeFragmentHash);
			if (variationPointViewAggregation == null) {
				variationPointViewAggregation = new HashSet<VariationPointView>();
			}
			variationPointViewAggregation.add(variationPointView);
		}
	}
}
