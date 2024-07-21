package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import java.util.Map;
import codeContext.processors.export.ExportedContext;


/**
 * The manager of dependencies for used functionality from additional scripts 
 * as extension with mapping of used variables to particular variation point
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class ParsedTypeOfVariableDataWithVPMapping extends ParsedTypeOfVariableData {
	
	/**
	 * The mapping of the currently available variable name to variation point name/identifier mapping
	 */
	private Map<String, InjectionCandidateVariationPoint> variableNameToVariationPointNameMapping;
	
	/**
	 * Name of variation point where content should be injected
	 */
	private String variationPointName = "NOT INITIALIZED";
	
	/**
	 * Instantiates and initializes the entity to manage parsed type of variable data with the mapping 
	 * of each variable/parameter name to exported context (import/export/dependency)
	 * 
	 * @param variableNameToVariationPointNameMapping - the mapping of the currently available variable name to variation point name/identifier mapping
	 */
	public ParsedTypeOfVariableDataWithVPMapping(Map<String, InjectionCandidateVariationPoint> variableNameToVariationPointNameMapping) {
		super();
		InjectionCandidateVariationPoint randomInjectionCandidate = variableNameToVariationPointNameMapping.values().stream().findFirst().get();
		if (randomInjectionCandidate != null) { this.variationPointName = randomInjectionCandidate.getVariationPointIdentifier(); }
		this.variableNameToVariationPointNameMapping = variableNameToVariationPointNameMapping;
	}
	
	/**
	 * Returns the name of destination variation point name where content should be injected
	 * 
	 * @return the name of destination variation point name where content should be injected
	 */
	public String getVariationPointName() { return this.variationPointName; }
	
	/**
	 * Returns the mapping of currently available variable name to specific variation point name/identifier
	 * 
	 * @return the mapping of currently available variable name to specific variation point name/identifier
	 */
	public Map<String, ? extends ExportedObjectOrAvailableVariable> getNameToContextMapping() { return this.variableNameToVariationPointNameMapping;}
}
