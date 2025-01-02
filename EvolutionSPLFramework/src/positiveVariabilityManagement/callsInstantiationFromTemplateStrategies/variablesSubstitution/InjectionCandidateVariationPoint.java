package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import java.util.HashSet;
import java.util.Set;


public class InjectionCandidateVariationPoint implements ExportedObjectOrAvailableVariable {

	private Set<String> variationPointsIdentifiers;
	
	
	public InjectionCandidateVariationPoint(String variationPointIdentifier) {
		this.variationPointsIdentifiers = new HashSet<String>();
		this.variationPointsIdentifiers.add(variationPointIdentifier);
	}
	
	public InjectionCandidateVariationPoint(String variationPointIdentifier, Set<String> allowedVariableNames) {
		this.variationPointsIdentifiers = new HashSet<String>();
		this.variationPointsIdentifiers.add(variationPointIdentifier);
	}
	
	
	public Set<String> getVariationPointIdentifiers() { return this.variationPointsIdentifiers; }
	
	public void insertDependenciesOnVariationPoints(Set<String> possibleVariationPointDependencies) {
		this.variationPointsIdentifiers.addAll(possibleVariationPointDependencies);
	}
	
	public void insertDependencyOnVariationPoints(String possibleVariationPointDependency) {
		this.variationPointsIdentifiers.add(possibleVariationPointDependency);
	}
}
