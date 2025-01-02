package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import splEvolutionCore.DebugInformation;


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
	 * Logger to track information of parsed type of variable data with variation point mapping
	 */
	private static final Logger logger = LoggerFactory.getLogger(ParsedTypeOfVariableDataWithVPMapping.class);
	
	/**
	 * Name of variation point where content should be injected
	 */
	private Set<VariableAggregationUnderVariationPoint> nameToContextMappingDependenciesSet = null;
	
	/**
	 * Used type to reconstruct requested variables
	 */
	private String variableType;
	
	private Set<String> allowedVariableNames;
	
	/**
	 * Instantiates and initializes the entity to manage parsed type of variable data with the mapping 
	 * of each variable/parameter name to exported context (import/export/dependency)
	 * 
	 * @param variableType - the variable type which should be injected in form of one of its parameters
	 * @param nameToContextMappingDependenciesSet - the set of the currently satisfying variation points with their variables"
	 */
	public ParsedTypeOfVariableDataWithVPMapping(String variableType, Set<String> allowedVariableNames,
			Set<VariableAggregationUnderVariationPoint> nameToContextMappingDependenciesSet) {
		super();
		this.variableType = variableType;
		this.allowedVariableNames = allowedVariableNames;
		
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
		Map<String, InjectionCandidateVariationPoint> extractedMapping = new HashMap<String, InjectionCandidateVariationPoint>();
		InjectionCandidateVariationPoint injectionCandidateVariationPoint;
		String processedVariationPoint;
		Set<String> possibleVariationPointDependencies = new HashSet<String>();
		for (VariableAggregationUnderVariationPoint processedVariationPointDependency: this.nameToContextMappingDependenciesSet) {
			processedVariationPoint = processedVariationPointDependency.getVariationPointIdentifier();
			
			for (String allowedVariableName: this.allowedVariableNames) {
				injectionCandidateVariationPoint = new InjectionCandidateVariationPoint(processedVariationPoint);
				extractedMapping.put(allowedVariableName, injectionCandidateVariationPoint);
			}
			variableNameToVariationPointNameMapping.putAll(extractedMapping);
				
			possibleVariationPointDependencies.add(processedVariationPoint);
		}
		
		
		for (InjectionCandidateVariationPoint processedInjection: variableNameToVariationPointNameMapping.values()) {
			//for (String possibleVariationPointDependency: possibleVariationPointDependencies) {
			//	if (this.allowedDependencies.contains(possibleVariationPointDependency)) {
					processedInjection.insertDependenciesOnVariationPoints(possibleVariationPointDependencies);
			//		processedInjection.insertDependencyOnVariationPoints(possibleVariationPointDependency);
			//	}
			//}
		}
		if (DebugInformation.SHOW_POLLUTING_INFORMATION) {
			System.out.println("Number of dependencies..................................: " + possibleVariationPointDependencies.size());
			System.out.println("Number of preparing..................................: " + variableNameToVariationPointNameMapping.size());
		}
		return variableNameToVariationPointNameMapping;
	}
}
