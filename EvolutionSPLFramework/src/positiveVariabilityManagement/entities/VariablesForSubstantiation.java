package positiveVariabilityManagement.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import codeContext.processors.export.ExportedContext;
import codeContext.processors.export.exportedFileUnits.FileExportsUnits;
import positiveVariabilityManagement.UnmappedContextException;
import splEvolutionCore.DebugInformation;


/**
 * Manages the substantiation of variables
 * 
 * @author Jakub Perdek
 *
 */
public class VariablesForSubstantiation {
	
	/**
	 * Set of used unique export names
	 */
	private Set<String> usedNames;
	
	/**
	 * The mapping of variable type to all available (exported) variable names
	 * MAP[variableType, allAvailableVariableNames]
	 */
	private Map<String, Set<String>> variableTypeToImportedVariableNamesMapping; 
	
	/**
	 * The mapping of given unique variable name/identifier to exported context with more information about variable
	 */
	private Map<String, ExportedContext> variableIdentifierNameToContextMapping;
	
	/**
	 * The mapping of exported context to unique variable name/identifier
	 */
	private Map<ExportedContext, List<String>> contextMappingToVariableIdentifierName;
	
	/**
	 * The set of not associated variables to optionally show warning about their unusability
	 */
	private static Set<String> notAssociatedVariables = new HashSet<String>();
	
	/**
	 * Creates instance to manage substantiation of variables to templates
	 */
	public VariablesForSubstantiation() {
		this.usedNames = new HashSet<String>();
		this.variableTypeToImportedVariableNamesMapping = new HashMap<String, Set<String>>();
		//this.variableIdentifierNameToImportedVariableNamesMapping = new HashMap<String, String>();
		this.variableIdentifierNameToContextMapping = new HashMap<String, ExportedContext>();
		this.contextMappingToVariableIdentifierName = new HashMap<ExportedContext, List<String>>();
	}
	
	/**
	 * Loads all exported variables from selected files
	 * -the selection is made through configuration
	 * 
	 * @param fileExportsUnits - the object with exported contexts from selected files
	 * @throws DuplicatedContextIdentifier
	 */
	public void loadAllExportedVariables(FileExportsUnits fileExportsUnits) throws DuplicatedContextIdentifier {
		for (ExportedContext processedExportedContext: fileExportsUnits.getAllExportedContexts()) {
			this.loadExportedVariablesFromExportedContext(processedExportedContext);
		}
	}
	
	/**
	 * Creates unique import name
	 * 
	 * @param exportedContext - the actually processed exported context to construct import name
	 * @param usedNames - the previously used import names
	 * @return created unique import name
	 */
	private String createImportName(ExportedContext exportedContext, Set<String> usedNames) {
		String importName = exportedContext.getCallableStr() + exportedContext.getFileName().substring(0,1).toUpperCase() + exportedContext.getFileName().substring(1);
		String newImportName = importName;
		int number = 2;
		while(usedNames.contains(newImportName)) {
			newImportName = importName + Integer.toString(number);
			number = number + 1;
		}
		return importName + Integer.toString(number - 1);
	}
	
	/**
	 * Extracts associated variable context mapping with their additional information (exported context)
	 * 
	 * @param requiredType - the type that all selected variables must comply with
	 * @return the mapping of selected variables that comply with given type to their exported context (additional information)
	 * @throws UnmappedContextException
	 */
	public Map<String, ExportedContext> getAssociatedVariableContextMapping(String requiredType) throws UnmappedContextException {
		requiredType = requiredType.strip();
		Map<String, ExportedContext> variableContextMapping = new HashMap<String, ExportedContext>();
		if (requiredType.equals("any")) { return variableContextMapping; }
		
		Set<String> exportedVariablesWithTheSameType = this.variableTypeToImportedVariableNamesMapping.get(requiredType);
		ExportedContext mappedContext;
		if (exportedVariablesWithTheSameType != null) {
			for (String variableNameWithTheSameType: exportedVariablesWithTheSameType) {
				// still variables mapped to other variables are not mapped
				mappedContext = this.variableIdentifierNameToContextMapping.get(variableNameWithTheSameType);
				for (String variableNameWithTheSameTypeUnderContext: this.contextMappingToVariableIdentifierName.get(mappedContext)) {
					variableContextMapping.put(variableNameWithTheSameTypeUnderContext, mappedContext);
				}
			}
		} else {
			if (DebugInformation.SHOW_MISSING_EVOLUTION_ENHANCEMENTS && 
					!VariablesForSubstantiation.notAssociatedVariables.contains(requiredType)) {
				VariablesForSubstantiation.notAssociatedVariables.add(requiredType);
				System.out.println("Type " + requiredType + " has no associated variables. Declare them if possible...");
			}
		}
		return variableContextMapping;
	}
	
	/**
	 * Loads exported variables from exported context
	 * 
	 * @param exportedContext - the context that is exported
	 * @throws DuplicatedContextIdentifier
	 */
	public void loadExportedVariablesFromExportedContext(ExportedContext exportedContext) throws DuplicatedContextIdentifier {
		String variableName = exportedContext.getCallableStr();
		String variableType = exportedContext.getCallableType();
		List<String> contextMapping;
		String importName;
		if (this.usedNames.contains(variableName)) {
			importName = this.createImportName(exportedContext, this.usedNames);
		} else {
			importName = variableName;
		}
		this.usedNames.add(importName);
		
		if (!this.variableIdentifierNameToContextMapping.containsKey(importName)) {
			//this.variableIdentifierNameToImportedVariableNamesMapping.put(variableIdentifier, importName);
			this.variableIdentifierNameToContextMapping.put(importName, exportedContext);
			if (!this.contextMappingToVariableIdentifierName.containsKey(exportedContext)) {
				contextMapping = new ArrayList<String>();
			} else {
				contextMapping = this.contextMappingToVariableIdentifierName.get(exportedContext);
			}
			contextMapping.add(importName);
			this.contextMappingToVariableIdentifierName.put(exportedContext, contextMapping);
		} else {
			throw new DuplicatedContextIdentifier("Exported context identifier: " + importName +
					" is used twice. Exported identifier has the similar name and comes from the similar file");
		}
		Set<String> variablesGroupedUnderType;
		if (this.variableTypeToImportedVariableNamesMapping.containsKey(variableType)) {
			variablesGroupedUnderType = this.variableTypeToImportedVariableNamesMapping.get(variableType);
			if (variablesGroupedUnderType == null) {
				variablesGroupedUnderType = new HashSet<String>();
				this.variableTypeToImportedVariableNamesMapping.put(variableType, variablesGroupedUnderType);
			}
			variablesGroupedUnderType.add(importName);
		} else {
			variablesGroupedUnderType = new HashSet<String>();
			variablesGroupedUnderType.add(importName);
			this.variableTypeToImportedVariableNamesMapping.put(variableType, variablesGroupedUnderType);
		}
	}
}
