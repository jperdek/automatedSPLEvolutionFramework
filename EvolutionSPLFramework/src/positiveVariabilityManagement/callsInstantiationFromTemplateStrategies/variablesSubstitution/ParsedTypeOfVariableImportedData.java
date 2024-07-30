package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import java.util.Map;
import codeContext.processors.export.ExportedContext;


/**
 * The manager of dependencies for used functionality from additional scripts as extension
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class ParsedTypeOfVariableImportedData extends ParsedTypeOfVariableData {
	
	/**
	 * The mapping of variable/parameter name to exported context (import/export/dependency)
	 */
	private Map<String, ExportedContext> nameToContextMapping;
	
	/**
	 * Instantiates and initializes the entity to manage parsed type of variable data with the mapping 
	 * of each variable/parameter name to exported context (import/export/dependency)
	 * 
	 * @param nameToContextMapping - the mapping of each variable/parameter name to exported context (import/export/dependency)
	 */
	public ParsedTypeOfVariableImportedData(Map<String, ExportedContext> nameToContextMapping) {
		super();
		this.nameToContextMapping = nameToContextMapping;
	}
	
	/**
	 * Returns the mapping of variable/parameter name to exported context (import/export/dependency)
	 * 
	 * @return the mapping of variable/parameter name to exported context (import/export/dependency)
	 */
	public Map<String, ? extends ExportedObjectOrAvailableVariable> getNameToContextMapping() { return  this.nameToContextMapping; }
}
