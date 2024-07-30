package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import java.util.HashSet;
import java.util.Set;


public class InjectionCandidateVariationPoint implements ExportedObjectOrAvailableVariable {

	private Set<String> variationPointsIdentifiers;
	private Set<String> variableNamesStoredUnderSharedType;
	
	public InjectionCandidateVariationPoint(String variationPointIdentifier) {
		this.variationPointsIdentifiers = new HashSet<String>();
		this.variationPointsIdentifiers.add(variationPointIdentifier);
		this.variableNamesStoredUnderSharedType = new HashSet<String>();
	}
	
	public void insertVariableNameUnderSharedType(String variableName) {
		if (this.variableNamesStoredUnderSharedType.contains(variableName)) {
			System.out.println("The variable name: " + variableName + " is already associated with type and variation points.");
		}
		this.variableNamesStoredUnderSharedType.add(variableName);
	}
	
	public Set<String> getVariationPointIdentifiers() { return this.variationPointsIdentifiers; }
	
	public void insertDependenciesOnVariationPoints(Set<String> possibleVariationPointDependencies) {
		this.variationPointsIdentifiers.addAll(possibleVariationPointDependencies);
	}
}
