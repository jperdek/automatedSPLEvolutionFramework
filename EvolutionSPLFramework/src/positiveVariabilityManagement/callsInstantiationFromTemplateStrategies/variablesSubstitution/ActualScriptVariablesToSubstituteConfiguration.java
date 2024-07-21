package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;


/**
 * Configuration for getting actually available functionality that can be substituted in code
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class ActualScriptVariablesToSubstituteConfiguration {

	/**
	 * Allows to use actual/currently available script variables, parameters and directly created particular types
	 */
	private boolean useActualScriptVariables = true;
	
	/**
	 * Allows to harvest method or class parameters and optionally to use them for substitution if true otherwise not
	 */
	private boolean useParameters = false;
	
	/**
	 * Allows to harvest global variables and to use them for substitution
	 */
	private boolean useGlobalVariables = false;
	
	/**
	 * Restricts the harvesting only the variables on current hierarchy level 
	 */
	private boolean useCurrentLevelVariablesOnly = false;
	
	/**
	 * Allows to instantiate new entities according to type
	 */
	private boolean instantiateNewEntitiesAccordingToType = false;
	
	
	/**
	 * Instantiates the guard entity to manage the use of actual/currently available script variables, 
	 * parameters and directly created particular types
	 * 
	 * @param useActualScriptVariables
	 */
	public ActualScriptVariablesToSubstituteConfiguration(boolean useActualScriptVariables) {
		this.useActualScriptVariables = useActualScriptVariables;
	}
	
	public ActualScriptVariablesToSubstituteConfiguration(
			boolean useActualScriptVariables, boolean useParameters, boolean useGlobalVariables, 
			boolean useCurrentLevelVariablesOnly, boolean instantiateNewEntitiesAccordingToType) {
		this.useActualScriptVariables = useActualScriptVariables;
		this.useParameters = useParameters;
		this.useGlobalVariables = useGlobalVariables;
		this.useCurrentLevelVariablesOnly = useCurrentLevelVariablesOnly;
		this.instantiateNewEntitiesAccordingToType = instantiateNewEntitiesAccordingToType;
	}
	
	public void setUseActualScriptVariables(boolean useActualScriptVariables) { this.useActualScriptVariables = useActualScriptVariables; }
	
	public boolean useActualScriptVariables() { return this.useActualScriptVariables; }
	
	public void setUseParameters(boolean useParameters) { this.useParameters = useParameters; }
	
	public boolean useParameters() { return this.useParameters; }
	
	public void setGlobalVariables(boolean useGlobalVariables) { this.useGlobalVariables = useGlobalVariables; }
	
	public boolean useGlobalVariables() { return this.useGlobalVariables; }
	
	public void setCurrentLevelVariablesOnly(boolean useCurrentLevelVariablesOnly) { 
		this.useCurrentLevelVariablesOnly = useCurrentLevelVariablesOnly; 
	}
	
	public boolean useCurrentLevelVariablesOnly() { return this.useCurrentLevelVariablesOnly; }
	
	public void setInstantiateNewEntitiesAccordingToType(boolean instantiateNewEntitiesAccordingToType) { 
		this.instantiateNewEntitiesAccordingToType = instantiateNewEntitiesAccordingToType; 
	}
	
	public boolean useInstantiateNewEntitiesAccordingToType() { return this.instantiateNewEntitiesAccordingToType; }
}
