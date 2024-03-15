package evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.imageContentQualityAssignment;

import org.json.simple.JSONObject;

import splEvolutionCore.candidateSelector.VariationPointCandidate;


/**
 * The view on (potential) Variation point of particular SPL candidate for the next evolution iteration process
 * 
 * @author Jakub Perdek
 *
 */
public class VariationPointView extends VariationPointCandidate {

	/**
	 * The unique product identifier of this variation point
	 */
	private String productIdentifier;
	

	/**
	 * Instantiates the view on particular variation point
	 * 
	 * @param productIdentifier - the unique identifier of particular variation point candidate
	 * @param variationPointData - the data associated with particular variation point
	 */
	public VariationPointView(String productIdentifier, JSONObject variationPointData) {
		super(variationPointData);
		this.productIdentifier = productIdentifier;
	}
	
	/**
	 * Returns the unique product identifier of this variation point
	 * 
	 * @return the unique product identifier of this variation point
	 */
	public String getProductIdentifier() { return this.productIdentifier; }
}
