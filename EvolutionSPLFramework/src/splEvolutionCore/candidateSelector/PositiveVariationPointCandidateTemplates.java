package splEvolutionCore.candidateSelector;

import positiveVariabilityManagement.callsTemplateSelectionStrategies.CallsFromPositiveVariationPointCreator;
import splEvolutionCore.candidateSelector.valueAssignment.AssignedValue;
import positiveVariabilityManagement.entities.CallableConstructTemplate;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Parameterized form of callable code constructs for given positive variation point
 * -instantiated callable constructs are assigned later 
 * -stores also basic information about variation point
 * 
 * @author Jakub Perdek
 *
 */
public class PositiveVariationPointCandidateTemplates extends VariationPointCandidate {

	/**
	 * Assigned values/quality/measures/scores to given positive variation point
	 * -its mapping of the metric/method name to represented value and its interpretation
	 *     MAP[method name, assigned value], template quality evaluation
	 */
	private Map<String, AssignedValue> assignedValues; 
	
	/**
	 * The mapping of calls to all associated information of instantiated/substituted construct templates
	 */
	private Map<String, CallableConstructTemplate> callableConstructTemplates;
	
	/**
	 * Optionally associated callable variation point candidate to manage callable construct templates with their quality
	 */
	private PositiveVariationPointCandidate associatedCallableVariationPointCandidate = null;
	

	/**
	 * Creates positive variation point candidate template and associates information about processed positive variation point
	 * 
	 * @param variationPointData - the associated information with variation point including code, positions in AST, user annotations and other attributes
	 */
	public PositiveVariationPointCandidateTemplates(JSONObject variationPointData) {
		super(variationPointData);
		this.assignedValues = new HashMap<String, AssignedValue>();
		this.callableConstructTemplates = new HashMap<String, CallableConstructTemplate>();
	}
	
	/**
	 * Extracts selected calls from positive variation point, creates template and assigns equal default value to all templates
	 * 
	 * @param positiveVariabilityCreatorStrategy - the strategy to extract selected calls from positive variation point
	 * @return true if creation of template from executable code does not fail otherwise false
	 */
	public boolean extractCallTemplatesAccordingToStrategyWithDefaultValue(
			CallsFromPositiveVariationPointCreator positiveVariabilityCreatorStrategy) {
		List<String> executedCodeTemplates = positiveVariabilityCreatorStrategy.extractCallsFromPositiveVariationPoint(this);
		if (executedCodeTemplates == null) { return false; }
		for (String executedCodeTemplate: executedCodeTemplates) {
			this.putAssignedValue(executedCodeTemplate, new AssignedValue(1, 1, true));
		}
		return true;
	}

	/**
	 * Returns the new list of callable construct templates
	 * 
	 * @return the new list of callable construct templates
	 */
	public List<CallableConstructTemplate> getCallableConstructTemplates() { return new ArrayList<
			CallableConstructTemplate>(this.callableConstructTemplates.values()); }
	
	/**
	 * Assigns value to processed executed code template
	 * 
	 * @param executedCodeTemplate - the string representation of parameterized/template code call
	 * @param assignedValue - the value of performed measurement that is going to be associated with parameterized template call
	 */
	public void putAssignedValue(String executedCodeTemplate, AssignedValue assignedValue) {
		this.assignedValues.put(executedCodeTemplate, assignedValue);
		this.callableConstructTemplates.put(executedCodeTemplate, new CallableConstructTemplate(executedCodeTemplate));
	}
	
	/**
	 * Returns callable construct template according its string representation of parameterized/template code call
	 * 
	 * @param executedCodeTemplate - the string representation of parameterized/template code call
	 * @return
	 */
	public CallableConstructTemplate getCallableConstructTemplate(String executedCodeTemplate) {
		return this.callableConstructTemplates.get(executedCodeTemplate);
	}
	
	/**
	 * Evaluates the overall value/score of parameterized/template code call from all assigned values
	 *  
	 * @return the overall value/score of parameterized/template code call from all assigned values
	 */
	public double evaluateCandidateValue() {
		double candidateValue = 0;
		for(AssignedValue processedAssignedValue: assignedValues.values()) {
			candidateValue = candidateValue + processedAssignedValue.calculateOverallValue();
		}
		return candidateValue;
	}
	
	/**
	 * Associates variation point candidate with this object (positive variation point candidate template) to store quality 
	 * measures/scores/values about all selected and instantiated callable constructs
	 * 
	 * @param associatedVariationPointCandidate - associated positive variation point candidate template
	 */
	public void associateVariationPointCandidate(PositiveVariationPointCandidate associatedVariationPointCandidate) {
		this.associatedCallableVariationPointCandidate = associatedVariationPointCandidate;
	}
	
	/**
	 * Returns the positive variation point candidate template with all measurements/metrics that are associated with this parameterized template
	 * 
	 * @return the positive variation point candidate template with all measurements/metrics that are associated with this parameterized template
	 */
	public PositiveVariationPointCandidate getAssociatedPositiveVariabilityConstructsCandidate() {
		return this.associatedCallableVariationPointCandidate;
	}
}
