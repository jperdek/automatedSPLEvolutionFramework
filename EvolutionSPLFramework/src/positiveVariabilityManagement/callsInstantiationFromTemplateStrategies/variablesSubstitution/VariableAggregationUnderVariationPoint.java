package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


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
		//injectionCandidateVariationPoint.insertVariableNameUnderSharedType(variableName);
		variableTypeToInjectionMap.put(variableName, injectionCandidateVariationPoint);
		
		this.variationPointToVariablesMap.put(variableName, injectionCandidateVariationPoint);
		this.extractedVariablesOrganizedAccordingToType.put(variableType, variableTypeToInjectionMap);
		if(this.variationPointIdentifier.contains("VP238")) {
			System.out.println("--------------------------------------------->" +variableType + " VP  " +  variableName);
			if(variableName.contains("radius")) {
				System.exit(5);
			}
		}
	}
	
	/**
	 * Checks if variable/parameter name is available
	 * -variable names must match exactly with all characters with available parameter names
	 *  
	 * @param variableName - variable/parameter name to check if variable or parameter exists for it
	 * @return true if variable or parameter exists for it otherwise false
	 */
	public boolean checkVariableName(String variableName) {
		for (String variableNameInCollection: this.variationPointToVariablesMap.keySet()) {
			if (variableNameInCollection.contains(variableName)) { return true; }
		}
		return false;
	}
	
	public Set<String> getAllowedVariablesToSubstitute(String variableName) {
		Set<String> allowedVariablesToSubstitute = new HashSet<String>();
		for (String variableNameInCollection: this.variationPointToVariablesMap.keySet()) {
			if (variableNameInCollection.contains(variableName)) { 
				allowedVariablesToSubstitute.add(variableNameInCollection); 
			}
		}
		return allowedVariablesToSubstitute;
	}
	
	public Map<String, InjectionCandidateVariationPoint> getVariationPointDependenciesAccordingToType(String variableType) {
		return this.extractedVariablesOrganizedAccordingToType.get(variableType.strip());
	}
	
	public InjectionCandidateVariationPoint getVariationPointDependenciesAccordingToVariableName(String variableName) {
		return this.variationPointToVariablesMap.get(variableName);
	}
	
	public void printVariablesUnderThisVariationPoint() {
		System.out.println("Available variables falling under: " + this.variationPointIdentifier);
		for (String variableName: this.variationPointToVariablesMap.keySet()) {
			System.out.println(variableName);
		}
		System.out.println("-------------------------------------------------------------------->");
	}
}
