package evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.imageContentQualityAssignment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;

import evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.CandidateSelectorAssessingAdapter;


/**
 * Manages aggregations of variation points across the population of evolved SPLs (or optionally derived products)
 *  
 * 
 * @author Jakub Perdek
 *
 */
public class VariationPointsDataAggregations implements CandidateSelectorAssessingAdapter {

	/**
	 * The mapping of each derived SPL/product into the organization of variation points 
	 */
	private Map<String, VariationPointsDataOrganizer> organizedVariationPoints;
	
	/**
	 * The mapping of related SPL functionality to the set of variation points
	 */
	private Map<String, Set<VariationPointView>> relatednessAccordingToFunctionality;
	

	/**
	 * Initializes the instance with aggregations of variation points from the population of evolved SPLs (or optionally derived products)
	 */
	public VariationPointsDataAggregations() {
		this.organizedVariationPoints = new HashMap<String, VariationPointsDataOrganizer>();
		this.relatednessAccordingToFunctionality = new HashMap<String, Set<VariationPointView>>(); 
	}
	
	/**
	 * Processes the variation points from particular evolved SPLs (or optionally derived product)
	 * 
	 * @param variationPointsJSONData - the variation point data of particular evolved SPL (or optionally derived product)
	 * @param productSPLIdentifier - the identifier of particular evolved SPL (or optionally derived product)
	 */
	public void processVariationPointsData(JSONArray variationPointsJSONData, String productSPLIdentifier) {
		VariationPointsDataOrganizer variationPointsDataOrganizer = new VariationPointsDataOrganizer(
				variationPointsJSONData, this.relatednessAccordingToFunctionality, productSPLIdentifier);
		this.organizedVariationPoints.put(productSPLIdentifier, variationPointsDataOrganizer);
	}
	
	/**
	 * Returns the entity with organized variation points from particular evolved SPL (or optionally derived product) 
	 * 
	 * @param productSPLIdentifier - the identifier of particular evolved SPL (or optionally derived product)
	 * @return the entity with organized variation points from particular evolved SPL (or optionally derived product) 
	 */
	public VariationPointsDataOrganizer getVariationPointOraganizerForParticularSPL(String productSPLIdentifier) {
		return this.organizedVariationPoints.get(productSPLIdentifier);
	}
	
	/**
	 * Returns the aggregated sets of related variation points under the name of related functionality
	 * 
	 * @return the aggregated sets of related variation points under the name of related functionality
	 */
	public Map<String, Set<VariationPointView>> getSetsOfRelatedVariationPointsAccordingToAssociatedGroup() {
		return this.relatednessAccordingToFunctionality;
	}
}
