package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONObject;

import codeContext.CodeContext;
import codeContext.InnerContext.Direction;
import codeContext.objects.VariableObject;
import divisioner.variabilityASTAntagonistMapping.VariabilityToAntagonistVariationPointMappings;
import divisioner.variabilityASTAntagonistMapping.VariationPointTransformationBetweenAsts;
import splEvolutionCore.DebugInformation;
import splEvolutionCore.SPLEvolutionCore;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


public class ParameterInjectionPositionObservation {
	
	private Map<String, Set<VariableAggregationUnderVariationPoint>> extractedVariablesOrganizedAccoringType;
	private Map<String, VariableAggregationUnderVariationPoint> variationPointIdentifierToAggregationMap;
	
	public ParameterInjectionPositionObservation() {
		this.extractedVariablesOrganizedAccoringType = new HashMap<String, Set<VariableAggregationUnderVariationPoint>>();
		this.variationPointIdentifierToAggregationMap = new HashMap<String, VariableAggregationUnderVariationPoint>();
	}
	
	private void printRangesAndParsedData(CodeContext codeContext, List<PositiveVariationPointCandidateTemplates> positiveVariationPointCandidatesTemplates) {
		Long endSearchPosition, startSearchPosition;
		JSONObject actuallyProcessedVariationPointData;
		String variationPointIDName;
		codeContext.getInnerContext().printTree(0);
		for (PositiveVariationPointCandidateTemplates positiveVariationPointCandidateTemplate: positiveVariationPointCandidatesTemplates) {
			actuallyProcessedVariationPointData = positiveVariationPointCandidateTemplate.getVariationPointData();
			variationPointIDName = (String) actuallyProcessedVariationPointData.get("variationPointName");
		
			startSearchPosition = (long) actuallyProcessedVariationPointData.get("originalASTStartPosition");
			endSearchPosition = (long) actuallyProcessedVariationPointData.get("originalASTEndPosition");
			System.out.println("Used position: [" + startSearchPosition + ", " + endSearchPosition + "] to get inner data from variation point: " + variationPointIDName + " variables and parameters: " +  actuallyProcessedVariationPointData.toString());	
		}
		//System.exit(8);
	}
	
	public void extractRelatedVariationPointData(CodeContext codeContext, 
			List<PositiveVariationPointCandidateTemplates> positiveVariationPointCandidatesTemplates,
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration) {
		JSONObject actuallyProcessedVariationPointData;
		String variationPointIDName;
		String variableName, variableType;
		Set<VariableAggregationUnderVariationPoint> variableTypeToInjectedVariableMap;
		VariableAggregationUnderVariationPoint variableAggregationUnderVariationPoint;
		long searchPosition, startSearchPosition, endSearchPosition;
		if (DebugInformation.SHOW_CREATED_ENTITIES) { this.printRangesAndParsedData(codeContext, positiveVariationPointCandidatesTemplates); }
		//this.printRangesAndParsedData(codeContext, positiveVariationPointCandidatesTemplates); 
		VariabilityToAntagonistVariationPointMappings variabilityToAntagonistVariationPointMappings = new VariabilityToAntagonistVariationPointMappings();
		boolean onlyToBlockTransformation = true;
		variabilityToAntagonistVariationPointMappings.loadAntagonistBoundaries(positiveVariationPointCandidatesTemplates, codeContext.getInnerContext(), onlyToBlockTransformation);
		
		for (VariationPointTransformationBetweenAsts variationPointTransformationBetweenAsts: 
			               variabilityToAntagonistVariationPointMappings.getVariationPointTransformationBetweenAsts()) {
			variationPointIDName = variationPointTransformationBetweenAsts.getVariationPointName();
		
			searchPosition =  startSearchPosition = variationPointTransformationBetweenAsts.getTransformedStartPosition();
			endSearchPosition = variationPointTransformationBetweenAsts.getTransformedEndPosition();
			System.out.println("Used position: [" + startSearchPosition + ", " + endSearchPosition + "] to get inner data from variation point: " + variationPointIDName);
			for (VariableObject processedVariable: codeContext.getActualVariables(null,
					searchPosition, startSearchPosition, endSearchPosition, Direction.RIGHT_FROM_POSITION, actualScriptVariablesToSubstituteConfiguration)) {
				variableName = processedVariable.getExportName().strip() + SPLEvolutionCore.CODE_FRAGMENT_SEPARATOR.strip();
				variableType = processedVariable.getExportType().strip();
				
				// GETIING MAPS AGGREGATED ACCORDING TO VARIATION POINT IDENTIFIER/NAME 
				if (!this.extractedVariablesOrganizedAccoringType.containsKey(variableType)) {
					variableTypeToInjectedVariableMap = new HashSet<VariableAggregationUnderVariationPoint>();
				} else {
					variableTypeToInjectedVariableMap = this.extractedVariablesOrganizedAccoringType.get(variableType);
				}
				
				if (!this.variationPointIdentifierToAggregationMap.containsKey(variationPointIDName)) {
					variableAggregationUnderVariationPoint = new VariableAggregationUnderVariationPoint(variationPointIDName);
				} else {
					variableAggregationUnderVariationPoint = this.variationPointIdentifierToAggregationMap.get(variationPointIDName);
				}	
				variableTypeToInjectedVariableMap.add(variableAggregationUnderVariationPoint);
		
				variableAggregationUnderVariationPoint.putTypeAndVariableName(variableType, variableName);
				this.variationPointIdentifierToAggregationMap.put(variationPointIDName, variableAggregationUnderVariationPoint);
				this.extractedVariablesOrganizedAccoringType.put(variableType, variableTypeToInjectedVariableMap);
			}
		
		}
		// TO PRINT RESULT OF COLLECTED AND AGGREGATED PARAMETERS (during errors fixing and debugging)
		if (DebugInformation.SHOW_CREATED_ENTITIES) { this.printAggregatedVariablesUnderType(); }
		//this.printAggregatedVariablesUnderType(); System.exit(5);
	}
	
	private void printAggregatedVariablesUnderType() {
		Set<VariableAggregationUnderVariationPoint> parameterDependencySet;
		String variableType;
		for (Entry<String, Set<VariableAggregationUnderVariationPoint>> parameter: 
			this.extractedVariablesOrganizedAccoringType.entrySet()) {
			variableType = parameter.getKey();
			System.out.println("|====== EXTRACTED VARIABLE AGGREGATED UNDER TYPE ========> " + variableType + " <======= |");
			parameterDependencySet = parameter.getValue();
			for (VariableAggregationUnderVariationPoint aggregationUnderVariationPoint: parameterDependencySet) {
				aggregationUnderVariationPoint.printVariablesUnderThisVariationPoint();
			}
			System.out.println("| <===========================================================================> |");
		}
	}
	
	public Set<VariableAggregationUnderVariationPoint> getVariableToVariationPointMappping(String parameterType) {
		return this.extractedVariablesOrganizedAccoringType.get(parameterType.strip());
	}	
}
