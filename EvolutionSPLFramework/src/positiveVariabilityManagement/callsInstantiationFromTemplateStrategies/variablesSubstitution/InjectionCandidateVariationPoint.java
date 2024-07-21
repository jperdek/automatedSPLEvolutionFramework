package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import java.util.HashSet;
import java.util.Set;

public class InjectionCandidateVariationPoint implements ExportedObjectOrAvailableVariable {

	private String variationPointIdentifier;
	private Set<String> variableNamesStoredUnderSharedType;
	
	public InjectionCandidateVariationPoint(String variationPointIdentifier) {
		this.variationPointIdentifier = variationPointIdentifier;
		this.variableNamesStoredUnderSharedType = new HashSet<String>();
	}
	
	public void insertVariableNameUnderSharedType(String variableName) {
		if (this.variableNamesStoredUnderSharedType.contains(variableName)) {
			System.out.println("The variable name: " + variableName + " is already associated with type and variation point: " + this.variationPointIdentifier);
		}
		this.variableNamesStoredUnderSharedType.add(variableName);
	}
	
	public String getVariationPointIdentifier() { return this.variationPointIdentifier; }
}
