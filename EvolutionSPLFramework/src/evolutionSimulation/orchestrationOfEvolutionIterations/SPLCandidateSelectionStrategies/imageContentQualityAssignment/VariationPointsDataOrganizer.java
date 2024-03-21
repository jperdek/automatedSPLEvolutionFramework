package evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.imageContentQualityAssignment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import variationPointsVisualization.AnnotationExtensionMarker.SystemAnnotationType;


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
		OUTSIDE_RECURSION("outsideRecursion"),
		IS_VARIABLE("isVariable"),
		IS_CONSTRUCTOR("isConstructor"),
		IS_PARAMETER("isParameter"),
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
	private Map<CapturedRelations, Set<VariationPointView>> relatednessAccordingToRelations;
	
	/**
	 * Get the set of variation point views associated with particular relation
	 * 
	 * @return the set of variation point views associated with particular relation
	 */
	public Set<VariationPointView> getVariationPointViewsInSetAccordingToRelation(CapturedRelations capturedRelationForVariationPoint) { 
		return this.relatednessAccordingToRelations.get(capturedRelationForVariationPoint); 
	}
	
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
		JSONObject variationPointRepresentation;
		for (Object variationPointsData: variationPoints) {
			variationPointRepresentation = (JSONObject) variationPointsData;
			variationPointView = new VariationPointView(productIdentifier, variationPointRepresentation);
			variationPointCodeFragmentHash = variationPointView.getVariationPointName().split("_")[1];
			variationPointViewAggregation = relatednessAccordingToFunctionality.get(variationPointCodeFragmentHash);
			if (variationPointViewAggregation == null) {
				variationPointViewAggregation = new HashSet<VariationPointView>();
				relatednessAccordingToFunctionality.put(variationPointCodeFragmentHash, variationPointViewAggregation);
			}
			
			variationPointViewAggregation.add(variationPointView);
			this.organizeInRelation(variationPointRepresentation, variationPointView, 
					CapturedRelations.INSIDE_CLASS, CapturedRelations.OUTSIDE_CLASS, "classRelated");
			this.organizeInRelation(variationPointRepresentation, variationPointView, 
					CapturedRelations.POSITIVE_VARIABILITY, CapturedRelations.NEGATIVE_VARIABILITY, "newVariationPoint");
			this.organizeInRelation(variationPointRepresentation, variationPointView, 
					CapturedRelations.INSIDE_RECURSION, CapturedRelations.OUTSIDE_RECURSION, "isInsideRecursion");
			
			if (variationPointRepresentation.containsKey("newVariationPoint") && (boolean) variationPointRepresentation.get("newVariationPoint") == false) {
				this.organizeInCategories(variationPointRepresentation, variationPointView, 
						CapturedRelations.IS_FUNCTION, SystemAnnotationType.FUNCTION.label, "annotationVPType");
				this.organizeInCategories(variationPointRepresentation, variationPointView, 
						CapturedRelations.IS_FUNCTION, SystemAnnotationType.CLASS_FUNCTION.label, "annotationVPType");
				this.organizeInCategories(variationPointRepresentation, variationPointView, 
						CapturedRelations.IS_VARIABLE, SystemAnnotationType.CLASS_VARIABLE.label, "annotationVPType");
				this.organizeInCategories(variationPointRepresentation, variationPointView, 
						CapturedRelations.IS_VARIABLE, SystemAnnotationType.VARIABLE.label, "annotationVPType");
				this.organizeInCategories(variationPointRepresentation, variationPointView, 
						CapturedRelations.IS_PARAMETER, SystemAnnotationType.CONSTRUCTOR_PARAMETER.label, "annotationVPType");
				this.organizeInCategories(variationPointRepresentation, variationPointView, 
						CapturedRelations.IS_PARAMETER, SystemAnnotationType.PARAMETER.label, "annotationVPType");
			} 
		}
	}
	
	/**
	 * Organizes/associates the view on processed variation point into the group/aggregation of related entities under particular relation/property
	 *  
	 * @param variationPoint - the representation of processed variation point
	 * @param variationPointView - the view on processed variation point
	 * @param directRelation - the relation that holds for processed variation point
	 * @param oppositeRelation - the opposite relation to the relation that holds for processed variation point
	 * @param variableName - the name of variable from which information about particular relation/property should be extracted
	 */
	private void organizeInRelation(JSONObject variationPoint, VariationPointView variationPointView, 
			CapturedRelations directRelation, CapturedRelations oppositeRelation, String variableName) {
		Set<VariationPointView> variationPointViewsSet;
		if (variationPoint.containsKey(variableName) && (boolean) variationPoint.get(variableName)) {
			variationPointViewsSet = this.relatednessAccordingToRelations.get(directRelation);
			if (variationPointViewsSet == null) { 
				variationPointViewsSet = new HashSet<VariationPointView>();
				variationPointViewsSet.add(variationPointView);
				this.relatednessAccordingToRelations.put(directRelation, variationPointViewsSet);
			} else {
				variationPointViewsSet.add(variationPointView);
			}
		} else {
			variationPointViewsSet = this.relatednessAccordingToRelations.get(oppositeRelation);
			if (variationPointViewsSet == null) { 
				variationPointViewsSet = new HashSet<VariationPointView>();
				variationPointViewsSet.add(variationPointView);
				this.relatednessAccordingToRelations.put(oppositeRelation, variationPointViewsSet);
			} else {
				variationPointViewsSet.add(variationPointView);
			}
		}
	}
	
	/**
	 * Organizes/associates the view on processed variation point into the group/aggregation of related entities under 
	 * the category of particular relation/property
	 * 
	 * @param variationPoint - the representation of processed variation point
	 * @param variationPointView - the view on processed variation point
	 * @param directRelation - the relation that holds for processed variation point
	 * @param contentStringMatch - the exact string identifying the category characteristic for the particular relation/property which hold for processed variation point 
	 * @param variableName - the name of variable from which information about particular relation/property should be extracted
	 */
	private void organizeInCategories(JSONObject variationPoint, VariationPointView variationPointView, 
			CapturedRelations directRelation, String contentStringMatch, String variableName) {
		Set<VariationPointView> variationPointViewsSet;
		if (variationPoint.containsKey(variableName) && ((String) variationPoint.get(variableName)).equals(contentStringMatch)) {
			variationPointViewsSet = this.relatednessAccordingToRelations.get(directRelation);
			if (variationPointViewsSet == null) { 
				variationPointViewsSet = new HashSet<VariationPointView>();
				variationPointViewsSet.add(variationPointView);
				this.relatednessAccordingToRelations.put(directRelation, variationPointViewsSet);
			} else {
				variationPointViewsSet.add(variationPointView);
			}
		}
	}
}
