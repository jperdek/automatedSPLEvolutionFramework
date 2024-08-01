package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import java.util.HashSet;
import java.util.Set;


public class InjectionCandidateVariationPoint implements ExportedObjectOrAvailableVariable {

	private Set<String> variationPointsIdentifiers;
	//private Set<String> variableNamesStoredUnderSharedType;
	///private boolean fixedNames = false;
	
	public InjectionCandidateVariationPoint(String variationPointIdentifier) {
		this.variationPointsIdentifiers = new HashSet<String>();
		this.variationPointsIdentifiers.add(variationPointIdentifier);
		//this.variableNamesStoredUnderSharedType = new HashSet<String>();
	}
	
	public InjectionCandidateVariationPoint(String variationPointIdentifier, Set<String> allowedVariableNames) {
		this.variationPointsIdentifiers = new HashSet<String>();
		this.variationPointsIdentifiers.add(variationPointIdentifier);
		//this.variableNamesStoredUnderSharedType = new HashSet<String>(allowedVariableNames);
		//fixedNames = true;
	}
	
	/*public void insertVariableNameUnderSharedType(String variableName) {
		if (!fixedNames) {
			if (this.variableNamesStoredUnderSharedType.contains(variableName)) {
				System.out.println("The variable name: " + variableName + " is already associated with type and variation points.");
			}
			this.variableNamesStoredUnderSharedType.add(variableName);
		}
	}*/
	
	public Set<String> getVariationPointIdentifiers() { return this.variationPointsIdentifiers; }
	
	public void insertDependenciesOnVariationPoints(Set<String> possibleVariationPointDependencies) {
		this.variationPointsIdentifiers.addAll(possibleVariationPointDependencies);
	}
	
	public void insertDependencyOnVariationPoints(String possibleVariationPointDependency) {
		this.variationPointsIdentifiers.add(possibleVariationPointDependency);
	}
}
