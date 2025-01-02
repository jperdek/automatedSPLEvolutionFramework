package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codeContext.CodeContext;
import codeContext.InnerContext.Direction;
import codeContext.objects.VariableObject;
import divisioner.variabilityASTAntagonistMapping.VariabilityToAntagonistVariationPointMappings;
import divisioner.variabilityASTAntagonistMapping.VariationPointTransformationBetweenAsts;
import splEvolutionCore.DebugInformation;
import splEvolutionCore.SPLEvolutionCore;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Entity managing harvesting, collection and aggregation of variables and parameters according to their observed type, name,
 *  and dependency on particular variation point with bindings to particular programming language
 * 
 * @author Jakub Perdek
 *
 */
public class ParameterInjectionPositionObservation {
	
	/**
	 * Logger to track harvesting, collection and aggregation of variables and parameters according to their observed type, name,
	 *  and dependency on particular variation point with bindings to particular programming language
	 */
	private static final Logger logger = LoggerFactory.getLogger(ParameterInjectionPositionObservation.class);
	
	/**
	 * Aggregation of all existing aggregations of variables/parameters (under variation point and variable names) according to their observed type
	 */
	private Map<String, Set<VariableAggregationUnderVariationPoint>> extractedVariablesOrganizedAccoringType;
	
	/**
	 * Aggregation of particular aggregation of variables/parameters (under variation point and variable names) according to variation point name/identifier
	 */
	private Map<String, VariableAggregationUnderVariationPoint> variationPointIdentifierToAggregationMap;
	
	/**
	 * Instantiates and initializes entity used to manage harvesting, collection and aggregation of variables and parameters according to their observed type, name,
	 *  and dependency on particular variation point with bindings to particular programming language
	 */
	public ParameterInjectionPositionObservation() {
		this.extractedVariablesOrganizedAccoringType = new HashMap<String, Set<VariableAggregationUnderVariationPoint>>();
		this.variationPointIdentifierToAggregationMap = new HashMap<String, VariableAggregationUnderVariationPoint>();
	}
	
	/**
	 * Prints ranges of particular variation points together with their associated names harvested in form of variation point data
	 * 
	 * @param codeContext - the code context with prepared hierarchy during previous AST parsing and global variables
	 * @param positiveVariationPointCandidatesTemplates - the list of templates with associated variation point data for positive variability handling
	 */
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
			logger.debug("Used position: [" + startSearchPosition + ", " + endSearchPosition + "] to get inner data from variation point: " + variationPointIDName + " variables and parameters: " +  actuallyProcessedVariationPointData.toString());	
		}
	}
	
	/**
	 * Extracts variation points data and uses them to harvest parameters/variables from processed script to substitute them further
	 *  
	 * @param codeContext - the code context with prepared hierarchy during previous AST parsing and global variables
	 * @param positiveVariationPointCandidatesTemplates - the list of templates with associated variation point data for positive variability handling
	 * @param actualScriptVariablesToSubstituteConfiguration - the configuration for harvesting with followed substitution of variables and parameters from processed script
	 */
	public void extractRelatedVariationPointData(CodeContext codeContext, 
			List<PositiveVariationPointCandidateTemplates> positiveVariationPointCandidatesTemplates,
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration) {

		String variationPointIDName;
		String variableName, variableType;
		Set<VariableAggregationUnderVariationPoint> variableTypeToInjectedVariableMap;
		VariableAggregationUnderVariationPoint variableAggregationUnderVariationPoint;
		long searchPosition, startSearchPosition, endSearchPosition;
		if (DebugInformation.SHOW_CREATED_ENTITIES) { this.printRangesAndParsedData(codeContext, positiveVariationPointCandidatesTemplates); }
		// this.printRangesAndParsedData(codeContext, positiveVariationPointCandidatesTemplates); 
		VariabilityToAntagonistVariationPointMappings variabilityToAntagonistVariationPointMappings = new VariabilityToAntagonistVariationPointMappings();
		boolean onlyToBlockTransformation = true;
		variabilityToAntagonistVariationPointMappings.loadAntagonistBoundaries(positiveVariationPointCandidatesTemplates, codeContext.getInnerContext(), onlyToBlockTransformation);
		
		for (VariationPointTransformationBetweenAsts variationPointTransformationBetweenAsts: 
			               variabilityToAntagonistVariationPointMappings.getVariationPointTransformationBetweenAsts()) {
			variationPointIDName = variationPointTransformationBetweenAsts.getVariationPointName();
		
			searchPosition =  startSearchPosition = variationPointTransformationBetweenAsts.getTransformedStartPosition();
			endSearchPosition = variationPointTransformationBetweenAsts.getTransformedEndPosition();
			logger.debug("Used position: [" + startSearchPosition + ", " + endSearchPosition + "] to get inner data from variation point: " + variationPointIDName);
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
	
	/**
	 * Prints information about aggregation of found variables/parameters from code entities 
	 *  under their observed/determined type and dependency on variation point
	 */
	private void printAggregatedVariablesUnderType() {
		Set<VariableAggregationUnderVariationPoint> parameterDependencySet;
		String variableType;
		for (Entry<String, Set<VariableAggregationUnderVariationPoint>> parameter: 
			this.extractedVariablesOrganizedAccoringType.entrySet()) {
			variableType = parameter.getKey();
			logger.debug("|====== EXTRACTED VARIABLE AGGREGATED UNDER TYPE ========> " + variableType + " <======= |");
			parameterDependencySet = parameter.getValue();
			for (VariableAggregationUnderVariationPoint aggregationUnderVariationPoint: parameterDependencySet) {
				aggregationUnderVariationPoint.printVariablesUnderThisVariationPoint();
			}
			logger.debug("| <===========================================================================> |");
		}
	}
	
	/**
	 * Returns the aggregation/set of variation point dependency entity holding aggregation of found variables, 
	 * their type under particular variation point dependency
	 * 
	 * @param parameterType - the observed type which has to be substituted during instantiation of call of particular functionality
	 * @return the aggregation/set of variation point dependency entity holding aggregation of found variables, 
	 * their type under particular variation point dependency
	 */
	public Set<VariableAggregationUnderVariationPoint> getVariableToVariationPointMappping(String parameterType) {
		return this.extractedVariablesOrganizedAccoringType.get(parameterType.strip());
	}	
}
