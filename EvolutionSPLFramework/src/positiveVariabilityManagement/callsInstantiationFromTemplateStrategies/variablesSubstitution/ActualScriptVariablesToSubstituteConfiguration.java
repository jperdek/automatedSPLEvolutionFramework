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
	 * -not implemented/supported
	 */
	private boolean instantiateNewEntitiesAccordingToType = false;
	
	
	/**
	 * Instantiates the guard entity to manage the use of actual/currently available script variables, 
	 * parameters and directly created particular types
	 * 
	 * @param useActualScriptVariables - enables or disables overall functionality to use variables/parameters during handling of positive variability (substitute them from processed script into callable constructs)
	 */
	public ActualScriptVariablesToSubstituteConfiguration(boolean useActualScriptVariables) {
		this(useActualScriptVariables, true, true, true, false, new LanguageSpecificVariableSubstitutionConfiguration(true, 1, false, 0, true));
		this.useActualScriptVariables = useActualScriptVariables;
	}
	
	/**
	 * Instantiates the guard entity to manage the use of actual/currently available script variables, 
	 * parameters and directly created particular types
	 * 
	 * @param useActualScriptVariables - enables or disables overall functionality to use variables/parameters during handling of positive variability (substitute them from processed script into callable constructs)
	 * @param useParameters - configuration setting to use parameters during handling of positive variability (substitute them from processed script into callable constructs)
	 * @param useGlobalVariables - configuration setting to use global variables for substitution into callable constructs during positive variability handling
	 * @param useCurrentLevelVariablesOnly - configuration setting to use variables/parameters on current level (for non-hierarchical languages such as JavaScript) for substitution into callable constructs during positive variability handling
	 * @param instantiateNewEntitiesAccordingToType - configuration setting to instantiate entities according to the type - created with constructor (not supported functionality)
	 * @param languageSpecificVariableSunstitutionConfiguration - entity allowing to configure programming language specific settings to substitute variables and parameters without defects
	 */
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
	
	/**
	 * Returns the entity allowing to configure programming language specific settings to substitute variables and parameters without defects
	 * 
	 * @return the entity allowing to configure programming language specific settings to substitute variables and parameters without defects
	 */
	public LanguageSpecificVariableSubstitutionConfiguration getLanguageSpecificVariableSubstitutionConfiguration() {
		return this.languageSpecificVariableSubstitutionConfiguration;
	}
	
	/**
	 * Sets/associates the entity allowing to configure programming language specific settings to substitute variables and parameters without defects
	 * 
	 * @param languageSpecificVariableSubstitutionConfiguration - the entity allowing to configure programming language specific settings to substitute variables and parameters without defects
	 */
	public void setLanguageSpecificVariableSubstitutionConfiguration(
			LanguageSpecificVariableSubstitutionConfiguration languageSpecificVariableSubstitutionConfiguration) {
		this.languageSpecificVariableSubstitutionConfiguration = languageSpecificVariableSubstitutionConfiguration;
	}
	
	/**
	 * Enables or disables overall functionality to use variables/parameters during handling of positive variability (substitute them from processed script into callable constructs)
	 *  
	 * @param useActualScriptVariables true if overall functionality to use variables/parameters during handling of positive variability (substitute them from processed script into callable constructs) is enabled otherwise is disabled (false)
	 */
	public void setUseActualScriptVariables(boolean useActualScriptVariables) { this.useActualScriptVariables = useActualScriptVariables; }
	
	/**
	 * Returns information if overall functionality to use variables/parameters during handling of positive variability (substitute them from processed script into callable constructs) is enabled or disabled
	 *  
	 * @return true if overall functionality to use variables/parameters during handling of positive variability (substitute them from processed script into callable constructs) is enabled otherwise is disabled (false)
	 */
	public boolean useActualScriptVariables() { return this.useActualScriptVariables; }
	
	/**
	 * Sets configuration to use parameters during handling of positive variability (substitute them from processed script into callable constructs)
	 * 
	 * @param useParameters - true if parameters from local scripts should be used for substitution into callable constructs during positive variability handling otherwise false
	 */
	public void setUseParameters(boolean useParameters) { this.useParameters = useParameters; }
	
	/**
	 * Returns information if parameters from local scripts should be used for substitution into callable constructs during positive variability handling
	 * 
	 * @return true if parameters from local scripts should be used for substitution into callable constructs during positive variability handling otherwise false
	 */
	public boolean useParameters() { return this.useParameters; }
	
	/**
	 * Sets configuration to use global variables for substitution into callable constructs during positive variability handling
	 * 
	 * @param useGlobalVariables - true if global variables from local scripts should be used for substitution into callable constructs during positive variability handling otherwise false
	 */
	public void setGlobalVariables(boolean useGlobalVariables) { this.useGlobalVariables = useGlobalVariables; }
	
	/**
	 * Returns information if global variables should be used for substitution into callable constructs during positive variability handling
	 * 
	 * @return true if global variables from local scripts should be used for substitution into callable constructs during positive variability handling otherwise false
	 */
	public boolean useGlobalVariables() { return this.useGlobalVariables; }
	
	/**
	 * Sets configuration to use variables/parameters on current level (for non-hierarchic languages such as JavaScript) for substitution into callable constructs during positive variability handling
	 *
	 * @param useCurrentLevelVariablesOnly - true if variables/parameters should be used on current level (for non-hierarchic languages such as JavaScript) for substitution into callable constructs during positive variability handling otherwise false
	 */
	public void setCurrentLevelVariablesOnly(boolean useCurrentLevelVariablesOnly) { 
		this.useCurrentLevelVariablesOnly = useCurrentLevelVariablesOnly; 
	}
	
	/**
	 * Returns information if variables/parameters should be used on current level (for non-hierarchic languages such as JavaScript) for substitution into callable constructs during positive variability handling
	 *  
	 * @return true if variables/parameters should be used on current level (for non-hierarchic languages such as JavaScript) for substitution into callable constructs during positive variability handling otherwise false
	 */
	public boolean useCurrentLevelVariablesOnly() { return this.useCurrentLevelVariablesOnly; }
	
	/**
	 * Sets configuration to instantiate entities according to the type - created with constructor (not supported functionality)
	 * -not implemented/supported
	 * 
	 * @param instantiateNewEntitiesAccordingToType - true if entities for substitution can be instantiated according to their type otherwise false
	 */
	public void setInstantiateNewEntitiesAccordingToType(boolean instantiateNewEntitiesAccordingToType) { 
		this.instantiateNewEntitiesAccordingToType = instantiateNewEntitiesAccordingToType; 
	}
	
	/**
	 * Returns information if entities are allowed to be instantiated according to their type for substitution into callable constructs during positive variability handling - created with constructor (not supported functionality)
	 * -not implemented/supported
	 * 
	 * @return true if entities are allowed to be instantiated according to their type for substitution into callable constructs during positive variability handling - created with constructor (not supported functionality) otherwise false
	 */
	public boolean useInstantiateNewEntitiesAccordingToType() { return this.instantiateNewEntitiesAccordingToType; }
}
