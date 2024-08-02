package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * The aggregation of previously harvested variables/parameters from variation points according to 
 * variation points in this entity, but directly by name and type 
 * 
 * @author Jakub Perdek
 *
 */
public class VariableAggregationUnderVariationPoint {

	/**
	 * The variation point identifier from actually processed script
	 */
	private String variationPointIdentifier;
	
	/**
	 * Aggregation of aggregations of extracted variables/parameters (under variable/parameter name) according to their observed type
	 */
	private Map<String, Map<String, InjectionCandidateVariationPoint>> extractedVariablesOrganizedAccordingToType;
	
	/**
	 * Aggregation of variables/parameters according to variable/parameter name
	 */
	private Map<String, InjectionCandidateVariationPoint> variationPointToVariablesMap;
	
	/**
	 * Instantiates the entity managing aggregation of particular variables/parameters according to name and type, 
	 * but especially according to variation point (identifier) in this entity
	 * 
	 * @param variationPointIdentifier - the variation point identifier from actually processed script
	 */
	public VariableAggregationUnderVariationPoint(String variationPointIdentifier) {
		this.extractedVariablesOrganizedAccordingToType = new HashMap<String, Map<String, InjectionCandidateVariationPoint>>();
		this.variationPointToVariablesMap = new HashMap<String, InjectionCandidateVariationPoint>();
		this.variationPointIdentifier = variationPointIdentifier;
	}
	
	/**
	 * Returns the variation point identifier from actually processed script
	 * 
	 * @return the variation point identifier from actually processed script
	 */
	public String getVariationPointIdentifier() { return this.variationPointIdentifier; }
	
	/**
	 * Inserts and aggregates particular variable/parameter with its type under variation point represented with this instance, but also according to the name and type itself
	 * 
	 * @param variableType - the name of inserted variable/parameter valid under the variation point represented by this instance
	 * @param variableName - the type of inserted variable/parameter valid under the variation point represented by this instance
	 */
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
		variableTypeToInjectionMap.put(variableName, injectionCandidateVariationPoint);
		
		this.variationPointToVariablesMap.put(variableName, injectionCandidateVariationPoint);
		this.extractedVariablesOrganizedAccordingToType.put(variableType, variableTypeToInjectionMap);
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
	
	/**
	 * Returns allowed variables/parameters to substitute according to variable/parameter name
	 * -for the semantical reasons the name should be contained in available parameter name for particular variation point
	 * 
	 * @param variableName - variable/parameter name which is checker and optionally added into group allowed for substitution
	 * @return allowed variables/parameters to substitute according to variable/parameter name
	 */
	public Set<String> getAllowedVariablesToSubstitute(String variableName) {
		Set<String> allowedVariablesToSubstitute = new HashSet<String>();
		for (String variableNameInCollection: this.variationPointToVariablesMap.keySet()) {
			if (variableNameInCollection.contains(variableName)) { 
				allowedVariablesToSubstitute.add(variableNameInCollection); 
			}
		}
		return allowedVariablesToSubstitute;
	}
	
	/**
	 * Returns the aggregation of variables/parameters according to variable/parameter name (to injection variables/parameters) aggregated according to the observed type
	 * 
	 * @param variableType - the observed type of used variable/parameter
	 * @return the variation point dependencies in form of injection of variables/parameters aggregated according to the observed type
	 */
	public Map<String, InjectionCandidateVariationPoint> getVariationPointDependenciesAccordingToType(String variableType) {
		return this.extractedVariablesOrganizedAccordingToType.get(variableType.strip());
	}
	
	/**
	 * Returns the representation of injection of variables/parameters with their dependency on variation point (to injection variables/parameters) according to variable/parameter name
	 * 
	 * @param variableName - variable/parameter name associated with their prepared injection with accompanying dependencies on variation points
	 * @return the representation of injection of variables/parameters with their dependency on variation point (to injection variables/parameters) according to variable/parameter name
	 */
	public InjectionCandidateVariationPoint getVariationPointDependenciesAccordingToVariableName(String variableName) {
		return this.variationPointToVariablesMap.get(variableName);
	}
	
	/**
	 * Prints aggregation of variable/parameter names under particular variation point (represented with identifier)
	 */
	public void printVariablesUnderThisVariationPoint() {
		System.out.println("Available variables falling under: " + this.variationPointIdentifier);
		for (String variableName: this.variationPointToVariablesMap.keySet()) {
			System.out.println(variableName);
		}
		System.out.println("-------------------------------------------------------------------->");
	}
}
