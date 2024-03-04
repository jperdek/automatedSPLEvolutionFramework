package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies;

import codeContext.processors.export.ExportedContext;
import codeContext.processors.export.exportedFileUnits.FileExportsUnits;
import positiveVariabilityManagement.UnmappedContextException;
import positiveVariabilityManagement.entities.CallableConstructTemplate;
import positiveVariabilityManagement.entities.DuplicatedContextIdentifier;
import positiveVariabilityManagement.entities.VariablesForSubstantiation;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * Representation of mapper with mapping of variable/parameter types to related information 
 * - the count of variables.parameters with types
 * - available variables for substitution purposed
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
	 */
	private VariablesForSubstantiation variablesForSubstantiation;
	

	/**
	 * Initializes the functionality to effectively manage substitution of function parameters with variables
	 * 
	 * @param fileExportUnits - the manager of exports across multiple files
	 * @throws DuplicatedContextIdentifier
	 */
	public AllVariablesMapper(FileExportsUnits fileExportUnits) throws DuplicatedContextIdentifier {
		this.fileExportsUnits = fileExportsUnits;
		this.variablesForSubstantiation = new VariablesForSubstantiation();
		this.evaluateNewSimilarityParts(fileExportUnits);
	}
	
	/**
	 * Evaluates the similarity of parts - all exported variables are loaded into substantiation entity
	 * 
	 * @param fileExportsUnits - the manager of exports across multiple files
	 * @throws DuplicatedContextIdentifier
	 */
	private void evaluateNewSimilarityParts(FileExportsUnits fileExportsUnits) throws DuplicatedContextIdentifier {
		this.variablesForSubstantiation.loadAllExportedVariables(fileExportsUnits);
	}
	
	/**
	 * Search for the information about parameters of particular callable construct template
	 * - getting mapping of each parameter types to associated variable context 
	 * 
	 * @param callableConstructTemplate - the template with information about callable constructs and associated information such as its parameters
	 * @return the mapping of each parameter types to associated variable context (instance of ParsedTypeOfVariableData)
	 * 
	 * @throws UnmappedContextException
	 */
	public Map<String, ParsedTypeOfVariableData> findParameterInformation(CallableConstructTemplate callableConstructTemplate) throws UnmappedContextException {
		Set<Entry<String, String>> parameterNamesToTypeMapping = callableConstructTemplate.getParameterNamesToTypeMappingEntries();
		Map<String, ExportedContext> nameToContextMapping;
		String parameterType;
		
		Map<String, ParsedTypeOfVariableData> parsedTypesMapping = new HashMap<String, ParsedTypeOfVariableData>(); 
		ParsedTypeOfVariableData parsedTypeOfVariableData;
		for(Entry<String, String> parameterNamesToTypeEntry: parameterNamesToTypeMapping) {
			parameterType = parameterNamesToTypeEntry.getValue();
			if (!parsedTypesMapping.containsKey(parameterType)) {
				nameToContextMapping = this.variablesForSubstantiation.getAssociatedVariableContextMapping(parameterType);
				parsedTypeOfVariableData = new ParsedTypeOfVariableData(nameToContextMapping);
				parsedTypesMapping.put(parameterType, parsedTypeOfVariableData);
			} else {
				parsedTypeOfVariableData = parsedTypesMapping.get(parameterType);
				parsedTypeOfVariableData.increaseOccurence();
			}
		}
		return parsedTypesMapping;
	}
}
