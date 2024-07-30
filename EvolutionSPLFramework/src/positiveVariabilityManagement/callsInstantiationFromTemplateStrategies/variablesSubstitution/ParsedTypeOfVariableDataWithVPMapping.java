package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


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
	 * Name of variation point where content should be injected
	 */
	private Set<VariableAggregationUnderVariationPoint> nameToContextMappingDependenciesSet = null;
	
	/**
	 * Used type to reconstruct requested variables
	 */
	private String variableType;
	
	/**
	 * Instantiates and initializes the entity to manage parsed type of variable data with the mapping 
	 * of each variable/parameter name to exported context (import/export/dependency)
	 * 
	 * @param variableType - the variable type which should be injected in form of one of its parameters
	 * @param nameToContextMappingDependenciesSet - the set of the currently satisfying variation points with their variables"
	 */
	public ParsedTypeOfVariableDataWithVPMapping(String variableType, Set<VariableAggregationUnderVariationPoint> nameToContextMappingDependenciesSet) {
		super();
		this.variableType = variableType;
		// if (randomInjectionCandidate != null) { this.variationPointName = randomInjectionCandidate.getVariationPointIdentifier(); }
		this.nameToContextMappingDependenciesSet = nameToContextMappingDependenciesSet;
	}
	
	/**
	 * Returns the set if destination variation points satisfying conditions to inject mapped parameters where content should be injected
	 * 
	 * @return the set if destination variation points satisfying conditions to inject mapped parameters where content should be injected
	 */
	public Set<VariableAggregationUnderVariationPoint> getVariationPointDependenciesSet() { return this.nameToContextMappingDependenciesSet; }
	
	/**
	 * Returns the mapping of currently available variable name to specific variation point name/identifier
	 * 
	 * @return the mapping of currently available variable name to specific variation point name/identifier
	 */
	public Map<String, ? extends ExportedObjectOrAvailableVariable> getNameToContextMapping() {
		Map<String, InjectionCandidateVariationPoint> variableNameToVariationPointNameMapping = new HashMap<String, InjectionCandidateVariationPoint>();
		Map<String, InjectionCandidateVariationPoint> extractedMapping;
		String processedVariationPoint;
		Set<String> possibleVariationPointDependencies = new HashSet<String>();
		for (VariableAggregationUnderVariationPoint processedVariationPointDependency: this.nameToContextMappingDependenciesSet) {
			extractedMapping = processedVariationPointDependency.getVariationPointDependenciesAccordingToType(this.variableType);
			if (extractedMapping != null) {
				variableNameToVariationPointNameMapping.putAll(extractedMapping);
				
				processedVariationPoint = processedVariationPointDependency.getVariationPointIdentifier();
				possibleVariationPointDependencies.add(processedVariationPoint);
			} else {
				System.out.println("Fatal error: mapping for type not found: " + this.variableType);
			}
		}
		
		for (InjectionCandidateVariationPoint processedInjection: variableNameToVariationPointNameMapping.values()) {
			processedInjection.insertDependenciesOnVariationPoints(possibleVariationPointDependencies);
		}
		System.out.println("Dependencies..................................: " + possibleVariationPointDependencies.size());
		System.out.println("Preparing..................................: " + variableNameToVariationPointNameMapping.size());
		return variableNameToVariationPointNameMapping;
	}
}
