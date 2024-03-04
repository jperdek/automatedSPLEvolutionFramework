package splEvolutionCore.candidateSelector;

import splEvolutionCore.candidateSelector.valueAssignment.AssignedValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.simple.JSONObject;
import codeContext.processors.export.ExportLocationAggregation;


/**
 * Representation of positive variation point candidate that allows to manage quality and dependencies for 
 * each of associated callable constructs to given positive variation point
 * 
 * @author Jakub Perdek
 *
 */
public class PositiveVariationPointCandidate extends VariationPointCandidate {

	/**
	 * The mapping of measured metric names to measured score/value/quality
	 */
	private Map<String, Map<String, AssignedValue>> possibleInstantiationsAssignedValue;
	
	/**
	 * The mapping between candidate name and aggregated dependencies across all of its parameters/arguments
	 */
	private Map<String, ExportLocationAggregation> instantiationsToImportMapping;

	
	/**
	 * Creates the positive variation point candidate and initializes the quality and dependency mappings
	 * 
	 * @param variationPointData - the associated information with variation point including code, positions in AST, user annotations and other attributes
	 */
	public PositiveVariationPointCandidate(JSONObject variationPointData) {
		super(variationPointData);
		this.possibleInstantiationsAssignedValue = new HashMap<String, Map<String, AssignedValue>>();
		this.instantiationsToImportMapping = new HashMap<String, ExportLocationAggregation>();
	}

	/**
	 * Assigns the measured score/value/quality to given candidate (code construct/string representation)
	 * 
	 * @param instantiatedCodeForm - the string representation (usually callable code construct with substituted parameters) used as input to measurement/evaluations
	 * @param assignedValues - the value of performed measurement that is going to be associated with callable construct and used dependencies
	 * @param exportLocationAggregation - aggregation of used dependencies to manage them properly
	 * @throws DifferentlyAggregatedLocation
	 */
	public void putAssignedValue(String instantiatedCodeForm, Map<String, AssignedValue> assignedValues, 
			ExportLocationAggregation exportLocationAggregation) throws DifferentlyAggregatedLocation {
		Map<String, AssignedValue> assignedValuesNew = new HashMap<String, AssignedValue>(assignedValues);
		
		ExportLocationAggregation storedExportLocationAggregation;
		if(!this.possibleInstantiationsAssignedValue.containsKey(instantiatedCodeForm)) {
			this.possibleInstantiationsAssignedValue.put(instantiatedCodeForm, assignedValuesNew);
			this.instantiationsToImportMapping.put(instantiatedCodeForm, exportLocationAggregation);
		} else {
			this.possibleInstantiationsAssignedValue.get(instantiatedCodeForm).putAll(assignedValuesNew);
			storedExportLocationAggregation = this.instantiationsToImportMapping.get(instantiatedCodeForm);
			//if (storedExportLocationAggregation != exportLocationAggregation) {
			//	throw new DifferentlyAggregatedLocation(
			//			"Aggregated exports for different metrics of the same callable construct are different!");
			//}
		}
	}
	
	/**
	 * Returns the new list of entries each with construct name that is mapped to performed measurements 
	 * (given as mapping where each metric/measurement name is mapped to measured value/score/quality)
	 * 
	 * @return the new list of entries each with construct name that is mapped to performed measurements 
	 * (given as mapping where each metric/measurement name is mapped to measured value/score/quality)
	 */
	public List<Entry<String, Map<String, AssignedValue>>> getPositiveVariationPointConstructs() {
		return new ArrayList<Entry<String, Map<String, AssignedValue>>>(this.possibleInstantiationsAssignedValue.entrySet());
	}
	
	/**
	 * Returns the used dependencies across all arguments/parameters of given instantiated template call
	 * 
	 * @param instantiatedCodeForm - the name/whole call of instantiated callable construct (that was previously used as key)
	 * @return the used dependencies across all arguments/parameters of given instantiated template call
	 */
	public ExportLocationAggregation getExportLocationAggregationsAccordingToInstantiatedCallableForm(String instantiatedCodeForm) {
		return this.instantiationsToImportMapping.get(instantiatedCodeForm);
	}
	
	/**
	 * Returns the names of positive variation point candidates, especially callable instantiated constructs
	 * 
	 * @return the names of positive variation point candidates, especially callable instantiated constructs
	 */
	public Set<String> getPositiveVariationPointCandidates() { return this.possibleInstantiationsAssignedValue.keySet(); }
	
	/**
	 * Evaluates value of given positive variation point according to values/scores of all associated metrics/measures
	 * 
	 * @param instantiatedCodeForm - the instantiated code form
	 * @return the overall value/score of evaluated candidate value
	 */
	public double evaluateCandidateValue(String instantiatedCodeForm) {
		double candidateValue = 0;
		Map<String, AssignedValue> assignedValues = this.possibleInstantiationsAssignedValue.get(instantiatedCodeForm);
		if (assignedValues != null) {
			for(AssignedValue processedAssignedValue: assignedValues.values()) {
				candidateValue = candidateValue + processedAssignedValue.calculateOverallValue();
			}
		}
		return candidateValue;
	}
}
