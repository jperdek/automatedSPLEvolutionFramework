package evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.imageContentQualityAssignment;

import org.json.simple.JSONObject;

import splEvolutionCore.candidateSelector.VariationPointCandidate;


public class VariationPointView extends VariationPointCandidate {

	private String productIdentifier;
	

	public VariationPointView(String productIdentifier, JSONObject variationPointData) {
		super(variationPointData);
		this.productIdentifier = productIdentifier;
	}
	
	public String getProductIdentifier() { return this.productIdentifier; }
}
