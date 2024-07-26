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
	 * Language specific configuration for substitution of variables and parameters
	 */
	private LanguageSpecificVariableSubstitutionConfiguration languageSpecificVariableSubstitutionConfiguration;
	
	/**
	 * Allows to use actual/currently available script variables, parameters and directly created particular types
	 */
	private boolean useActualScriptVariables = true;
	
	/**
	 * Allows to harvest method or class parameters and optionally to use them for substitution if true otherwise not
	 */
	private boolean useParameters = true;
	
	/**
	 * Allows to harvest global variables and to use them for substitution
	 */
	private boolean useGlobalVariables = true;
	
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
		this(useActualScriptVariables, true, true, true, false, new LanguageSpecificVariableSubstitutionConfiguration(true, 1, false, 1, true));
		this.useActualScriptVariables = useActualScriptVariables;
	}
	
	public ActualScriptVariablesToSubstituteConfiguration(
			boolean useActualScriptVariables, boolean useParameters, boolean useGlobalVariables, 
			boolean useCurrentLevelVariablesOnly, boolean instantiateNewEntitiesAccordingToType, 
			LanguageSpecificVariableSubstitutionConfiguration languageSpecificVariableSunstitutionConfiguration) {
		this.useActualScriptVariables = useActualScriptVariables;
		this.useParameters = useParameters;
		this.useGlobalVariables = useGlobalVariables;
		this.useCurrentLevelVariablesOnly = useCurrentLevelVariablesOnly;
		this.instantiateNewEntitiesAccordingToType = instantiateNewEntitiesAccordingToType;
		this.languageSpecificVariableSubstitutionConfiguration = languageSpecificVariableSunstitutionConfiguration;
	}
	
	public LanguageSpecificVariableSubstitutionConfiguration getLanguageSpecificVariableSubstitutionConfiguration() {
		return this.languageSpecificVariableSubstitutionConfiguration;
	}
	
	public void setLanguageSpecificVariableSubstitutionConfiguration(
			LanguageSpecificVariableSubstitutionConfiguration languageSpecificVariableSubstitutionConfiguration) {
		this.languageSpecificVariableSubstitutionConfiguration = languageSpecificVariableSubstitutionConfiguration;
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
