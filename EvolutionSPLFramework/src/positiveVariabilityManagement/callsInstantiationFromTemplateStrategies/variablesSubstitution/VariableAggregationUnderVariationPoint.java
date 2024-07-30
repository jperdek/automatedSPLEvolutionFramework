package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class VariableAggregationUnderVariationPoint {

	private String variationPointIdentifier;
	private Map<String, Map<String, InjectionCandidateVariationPoint>> extractedVariablesOrganizedAccordingToType;
	private Map<String, InjectionCandidateVariationPoint> variationPointToVariablesMap;
	

	public VariableAggregationUnderVariationPoint(String variationPointIdentifier) {
		this.extractedVariablesOrganizedAccordingToType = new HashMap<String, Map<String, InjectionCandidateVariationPoint>>();
		this.variationPointToVariablesMap = new HashMap<String, InjectionCandidateVariationPoint>();
		this.variationPointIdentifier = variationPointIdentifier;
	}
	
	public String getVariationPointIdentifier() { return this.variationPointIdentifier; }
	
	public void putTypeAndVariableName(String variableType, String variableName) {
		variableType = variableType.strip();
		Map<String, InjectionCandidateVariationPoint> variableTypeToInjectionMap;
		InjectionCandidateVariationPoint injectionCandidateVariationPoint;
		if (this.extractedVariablesOrganizedAccordingToType.containsKey(variableType)) {
			variableTypeToInjectionMap = this.extractedVariablesOrganizedAccordingToType.get(variableType);
		} else {
			variableTypeToInjectionMap = new HashMap<String, InjectionCandidateVariationPoint>();
		}
		
		if (this.variationPointToVariablesMap.containsKey(variableName)) {
			injectionCandidateVariationPoint = this.variationPointToVariablesMap.get(variableName);
		} else {
			injectionCandidateVariationPoint = new InjectionCandidateVariationPoint(this.variationPointIdentifier);
		}
		injectionCandidateVariationPoint.insertVariableNameUnderSharedType(variableName);
		variableTypeToInjectionMap.put(variableName, injectionCandidateVariationPoint);
		
		this.variationPointToVariablesMap.put(variableName, injectionCandidateVariationPoint);
		this.extractedVariablesOrganizedAccordingToType.put(variableType, variableTypeToInjectionMap);
	}
	
	public Map<String, InjectionCandidateVariationPoint> getVariationPointDependenciesAccordingToType(String variableType) {
		return this.extractedVariablesOrganizedAccordingToType.get(variableType.strip());
	}
	
	public void printVariablesUnderThisVariationPoint() {
		System.out.println("Available variables falling under: " + this.variationPointIdentifier);
		for (String variableName: variationPointToVariablesMap.keySet()) {
			System.out.println(variableName);
		}
		System.out.println("-------------------------------------------------------------------->");
	}
}
