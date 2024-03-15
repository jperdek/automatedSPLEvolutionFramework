package evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.imageContentQualityAssignment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * Organizes the variation points according to the related attributes/characteristics/captured relations
 * 
 * @author Jakub Perdek
 *
 */
public class VariationPointsDataOrganizer {

	/**
	 * The captured relations/attributes/characteristics of particular variation point from particular SPL
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum CapturedRelations {
		INSIDE_CLASS("inClass"),
		OUTSIDE_CLASS("outClass"),
		POSITIVE_VARIABILITY("positiveVariability"),
		NEGATIVE_VARIABILITY("negativeVariability"),
		INSIDE_RECURSION("insideRecursion"),
		IS_VARIABLE("isVariable"),
		IS_CONSTRUCTOR("isConstructor"),
		IS_FUNCTION("isFunction");
		
		/**
		 * The name of captured relation/attribute/characteristic of particular variation point
		 */
		public final String label;

		/**
		 * Instantiates the representation of captured relation/attribute/characteristic of particular variation point
		 * 
		 * @param label - the name of captured relation/attribute/characteristic of particular variation point
		 */
	    private CapturedRelations(String label) {
	        this.label = label;
	    }
	    
	    /**
	     * Return the name of captured relation/attribute/characteristic of particular variation point
	     * 
	     * @return the name of captured relation/attribute/characteristic of particular variation point
	     */
	    public String getName() { return this.label; }
	}

	/**
	 * The mapping of relation/attribute/characteristic of particular variation point to associated variation points according to compliance with this relation/attribute/characteristic
	 */
	public Map<CapturedRelations, Set<VariationPointView>> relatednessAccordingToRelations;
	
	/**
	 * Creates the organizer and performs the categorization of provided variation points of particular SPL into available categories
	 * 
	 * @param variationPoints - the (provided) variation points of particular SPL
	 * @param relatednessAccordingToFunctionality - the mapping of the calculated hash from string code representation to the particular variation point
	 * @param productIdentifier - the identifier of actually processed particular SPL (to which associated variation points data belongs to)
	 */
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
