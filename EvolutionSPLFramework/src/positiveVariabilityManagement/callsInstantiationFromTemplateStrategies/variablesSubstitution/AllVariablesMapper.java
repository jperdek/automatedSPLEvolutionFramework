package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import codeContext.CodeContext;
import codeContext.objects.VariableObject;
import codeContext.processors.export.ExportedContext;
import codeContext.processors.export.exportedFileUnits.FileExportsUnits;
import divisioner.VariationPointDivisioning;
import positiveVariabilityManagement.UnmappedContextException;
import positiveVariabilityManagement.entities.CallableConstructTemplate;
import positiveVariabilityManagement.entities.DuplicatedContextIdentifier;
import positiveVariabilityManagement.entities.VariablesForSubstantiation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * Representation of mapper with mapping of variable/parameter types to related information 
 * - the count of variables.parameters with types
 * - available variables for substitution purposes
 * 
 * - for search optimizations under FileExportsUnits
 * 
 * @author Jakub Perdek
 *
 */
public class AllVariablesMapper {
	
	/**
	 * The aggregations of file exports units in the mapping under the file name
	 */
	private FileExportsUnits fileExportsUnits;
	
	/**
	 * The instance used to substitute parameters according to configuration settings 
	 * TO LOAD EXTERNAL GLOBAL VARIABLES - manages also their imports
	 */
	private VariablesForSubstantiation variablesForSubstantiation;
	
	/**
	 * Configuration to get actually available functionality that can be substituted or directly create it
	 */
	private ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration;
	
	/**
	 * 
	 */
	private ParameterInjectionPositionObservation parameterInjectionPositionObservation;
	
	/**
	 * Initializes the functionality to effectively manage substitution of function parameters with variables
	 * 
	 * @param fileExportUnits - the manager of exports across multiple files
	 * @param actualScriptVariablesToSubstituteConfiguration - configuration to get actually available functionality that can be substituted or directly create it 
	 * @param parameterInjectionPositionObservation - 
	 * @throws DuplicatedContextIdentifier - the exception thrown if duplicated identifiers are assigned amongst contexts
	 */
	public AllVariablesMapper(FileExportsUnits fileExportUnits, 
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration,
			ParameterInjectionPositionObservation parameterInjectionPositionObservation) throws DuplicatedContextIdentifier {
		this.fileExportsUnits = fileExportsUnits;
		this.variablesForSubstantiation = new VariablesForSubstantiation();
		this.evaluateNewSimilarityParts(fileExportUnits, actualScriptVariablesToSubstituteConfiguration);
		this.parameterInjectionPositionObservation = parameterInjectionPositionObservation;
	}
	
	/**
	 * Returns the configuration to get actually available functionality that can be substituted or directly create it 
	 * 
	 * @return the configuration to get actually available functionality that can be substituted or directly create it 
	 */
	public ActualScriptVariablesToSubstituteConfiguration getActualScriptVariablesToSubstituteConfiguration() {
		return this.actualScriptVariablesToSubstituteConfiguration;
	}
	
	/**
	 * Evaluates the similarity of parts - all exported variables are loaded into substantiation entity
	 * 
	 * @param fileExportsUnits - the manager of exports across multiple files
	 * @param actualScriptVariablesToSubstituteConfiguration - configuration to get actually available functionality that can be substituted or directly create it 
	 * @throws DuplicatedContextIdentifier - the exception thrown if duplicated identifiers are assigned amongst contexts
	 */
	private void evaluateNewSimilarityParts(FileExportsUnits fileExportsUnits,
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration) throws DuplicatedContextIdentifier {
		this.variablesForSubstantiation.loadAllExportedVariables(fileExportsUnits);
	}
	
	/**
	 * Search for the information about parameters of particular callable construct template
	 * - getting mapping of each parameter types to associated variable context 
	 * 
	 * @param callableConstructTemplate - the template with information about callable constructs and associated information such as its parameters
	 * @return the mapping of each parameter types to associated variable context (instance of ParsedTypeOfVariableData)
	 * 
	 * @throws UnmappedContextException - the exception informing about impossibility to map content on the resulting AST of final product in the synthesis process
	 */
	public Map<String, ParsedTypeOfVariableData> findExternalScriptsParameterInformation(CallableConstructTemplate callableConstructTemplate) throws UnmappedContextException {
		Set<Entry<String, String>> parameterNamesToTypeMapping = callableConstructTemplate.getParameterNamesToTypeMappingEntries();
		Map<String, ExportedContext> nameToContextMapping;
		String parameterType;
		
		Map<String, ParsedTypeOfVariableData> parsedTypesMapping = new HashMap<String, ParsedTypeOfVariableData>(); 
		ParsedTypeOfVariableData parsedTypeOfVariableData;
		for(Entry<String, String> parameterNamesToTypeEntry: parameterNamesToTypeMapping) {
			parameterType = parameterNamesToTypeEntry.getValue();
			if (!parsedTypesMapping.containsKey(parameterType)) {
				nameToContextMapping = this.variablesForSubstantiation.getAssociatedVariableContextMapping(parameterType);
				parsedTypeOfVariableData = new ParsedTypeOfVariableImportedData(nameToContextMapping);
				parsedTypesMapping.put(parameterType, parsedTypeOfVariableData);
			} else {
				parsedTypeOfVariableData = parsedTypesMapping.get(parameterType);
				parsedTypeOfVariableData.increaseOccurence();
			}
		}
		return parsedTypesMapping;
	}
	
	/**
	 * Extracts the currently available variables that can be substituted into templates
	 * 
	 * @param variationPointDivisioning
	 * @param callableConstructTemplate
	 * @return
	 * @throws UnmappedContextException
	 */
	public Map<String, ParsedTypeOfVariableData> findActualContextParameterInformation(CallableConstructTemplate callableConstructTemplate) throws UnmappedContextException { 
		Set<Entry<String, String>> parameterNamesToTypeMapping = callableConstructTemplate.getParameterNamesToTypeMappingEntries();
		Set<Entry<String, String>> variableNamesWithVariationPointSet;
		Map<String, InjectionCandidateVariationPoint> nameToContextMapping;
		String parameterType;
		
		Map<String, ParsedTypeOfVariableData> parsedTypesMapping = new HashMap<String, ParsedTypeOfVariableData>(); 
		ParsedTypeOfVariableData parsedTypeOfVariableData;
		for(Entry<String, String> parameterNamesToTypeEntry: parameterNamesToTypeMapping) {
			parameterType = parameterNamesToTypeEntry.getValue();
			if (!parsedTypesMapping.containsKey(parameterType)) {
				parameterType = parameterNamesToTypeEntry.getValue();
				
				nameToContextMapping = this.parameterInjectionPositionObservation.getVariableToVariationPointMappping(parameterType);
				if (nameToContextMapping != null) {
					parsedTypeOfVariableData = new ParsedTypeOfVariableDataWithVPMapping(nameToContextMapping);
					parsedTypesMapping.put(parameterType, parsedTypeOfVariableData);
				}
			} else {
				parsedTypeOfVariableData = parsedTypesMapping.get(parameterType);
				parsedTypeOfVariableData.increaseOccurence();
			}
		}
		return parsedTypesMapping;
	}
}
