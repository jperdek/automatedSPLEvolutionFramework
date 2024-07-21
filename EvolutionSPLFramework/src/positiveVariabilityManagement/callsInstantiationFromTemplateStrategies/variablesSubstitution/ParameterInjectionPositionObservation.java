package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import codeContext.CodeContext;
import codeContext.objects.VariableObject;
import splEvolutionCore.SPLEvolutionCore;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


public class ParameterInjectionPositionObservation {
	
	private Map<String, Map<String, InjectionCandidateVariationPoint>> extractedVariablesOrganizedAccoringType;
	private Map<String, InjectionCandidateVariationPoint> variationPointToVariablesMap;
	
	public ParameterInjectionPositionObservation() {
		this.extractedVariablesOrganizedAccoringType = new HashMap<String, Map<String, InjectionCandidateVariationPoint>>();
		this.variationPointToVariablesMap = new HashMap<String, InjectionCandidateVariationPoint>();
	}
	
	public void extractRelatedVariationPointData(CodeContext codeContext, 
			List<PositiveVariationPointCandidateTemplates> positiveVariationPointCandidatesTemplates,
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration) {
		JSONObject actuallyProcessedVariationPointData;
		String variationPointIDName;
		String variableName, variableType;
		String variableNameWithVariationPointName;
		String variationPointNameWithVariableType;
		InjectionCandidateVariationPoint injectionCandidateVariationPoint;
		Map<String, InjectionCandidateVariationPoint> variableTypeToInjectedVariableMap;
		long searchPosition;
		
		for (PositiveVariationPointCandidateTemplates positiveVariationPointCandidateTemplate: positiveVariationPointCandidatesTemplates) {
			actuallyProcessedVariationPointData = positiveVariationPointCandidateTemplate.getVariationPointData();
			variationPointIDName = (String) actuallyProcessedVariationPointData.get("variationPointName");
			searchPosition = (long) actuallyProcessedVariationPointData.get("startPosition");
			System.out.println(actuallyProcessedVariationPointData.toString());
			for (VariableObject processedVariable: codeContext.getActualVariables(
					searchPosition, actualScriptVariablesToSubstituteConfiguration)) {
				variableName = processedVariable.getExportName();
				variableType = processedVariable.getExportType();
				if (!this.extractedVariablesOrganizedAccoringType.containsKey(variableType)) {
					variableTypeToInjectedVariableMap = new HashMap<String, InjectionCandidateVariationPoint>();
				} else {
					variableTypeToInjectedVariableMap = this.extractedVariablesOrganizedAccoringType.get(variableType);
				}
				variableNameWithVariationPointName = variableName + SPLEvolutionCore.CURRENTLY_AVAILABLE_VARIABLE_DELIMITER + variationPointIDName;
				variationPointNameWithVariableType = variableType + SPLEvolutionCore.CURRENTLY_AVAILABLE_VARIABLE_DELIMITER + variationPointIDName;
				if (this.variationPointToVariablesMap.containsKey(variationPointNameWithVariableType)) {
					injectionCandidateVariationPoint = this.variationPointToVariablesMap.get(variationPointNameWithVariableType);
				} else {
					injectionCandidateVariationPoint = new InjectionCandidateVariationPoint(variationPointIDName);
				}
				injectionCandidateVariationPoint.insertVariableNameUnderSharedType(variableName);
				variableTypeToInjectedVariableMap.put(variableNameWithVariationPointName, injectionCandidateVariationPoint);
				this.extractedVariablesOrganizedAccoringType.put(variableType, variableTypeToInjectedVariableMap);
			}
		}
	}
	
	public  Map<String, InjectionCandidateVariationPoint> getVariableToVariationPointMappping(String parameterType) {
		return this.extractedVariablesOrganizedAccoringType.get(parameterType);
	}	
}
