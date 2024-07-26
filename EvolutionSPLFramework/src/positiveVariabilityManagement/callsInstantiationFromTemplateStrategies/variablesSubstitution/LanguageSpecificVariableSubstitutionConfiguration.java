package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;


/**
 * Configuration to restrict substitution of invalid variables during creation of callable constructs from templates
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class LanguageSpecificVariableSubstitutionConfiguration {

	/**
	 * Information if max harvesting depth level of parameters should be applied to omit declared variables in nested structures
	 */
	private boolean canApplyMaxHarvestingDepthLevelForParameters = true;
	
	/**
	 * The maximally allowed harvesting depth level to restrict application of parameters declared in nested structures
	 */
	private int maxHarvestingParametersDepthLevelAcrossHierarchies = 1;
	
	/**
	 * Information if max harvesting depth level of local variables should be applied to omit declared variables in nested structures
	 */
	private boolean canApplyMaxHarvestingDepthLevelForLocalVariables = true;
	
	/**
	 * The maximally allowed harvesting depth level to restrict application of local variables declared in nested structures
	 */
	private int maxHarvestingLocalVariablesDepthLevelAcrossHierarchies = 1;
	
	/**
	 * Omits parsing side inner context while getting parameters and variables for substitution if true otherwise false
	 * -in JavaScript inner content is usually encapsulated and thus not accessible for substitution such as in JavaScript/TypeScript
	 */
	private boolean omitParsingSideInnerContexts = true;
	
	/**
	 * Initializes configuration for particular programming language to prevent defects caused with wrongly substituted variables 
	 * 
	 * @param canApplyMaxHarvestingDepthLevelForParameters - true if max harvesting depth level of parameters should be applied to omit use of parameters in nested structures otherwise false
	 * @param maxHarvestingParametersDepthLevelAcrossHierarchies - the maximally allowed harvesting depth level to restrict application of parameters declared in nested structures
	 * @param canApplyMaxHarvestingDepthLevelForLocalVariables - true if max harvesting depth level of variables should be applied to omit declared local variables in nested structures otherwise false
	 * @param maxHarvestingLocalVariablesDepthLevelAcrossHierarchies - the maximally allowed harvesting depth level to restrict application of variables declared in nested structures
	 * @param omitParsingSideInnerContexts - omits parsing side inner context while getting parameters and variables for substitution if true otherwise false
	 */
	public LanguageSpecificVariableSubstitutionConfiguration(
			boolean canApplyMaxHarvestingDepthLevelForParameters, int maxHarvestingParametersDepthLevelAcrossHierarchies,
			boolean canApplyMaxHarvestingDepthLevelForLocalVariables, int maxHarvestingLocalVariablesDepthLevelAcrossHierarchies, 
			boolean omitParsingSideInnerContexts) {
		this.canApplyMaxHarvestingDepthLevelForParameters = canApplyMaxHarvestingDepthLevelForParameters;
		this.canApplyMaxHarvestingDepthLevelForLocalVariables = canApplyMaxHarvestingDepthLevelForLocalVariables;
		this.maxHarvestingParametersDepthLevelAcrossHierarchies = maxHarvestingParametersDepthLevelAcrossHierarchies;
		this.maxHarvestingLocalVariablesDepthLevelAcrossHierarchies = maxHarvestingLocalVariablesDepthLevelAcrossHierarchies;
		this.omitParsingSideInnerContexts =  omitParsingSideInnerContexts;
	}
	
	/**
	 * Returns true if max harvesting depth level of parameters should be applied to omit use of parameters in nested structures otherwise false
	 * 
	 * @return true if max harvesting depth level of parameters should be applied to omit use of parameters in nested structures otherwise false
	 */
	public boolean isHarvestingParameterDepthLevelAllowed() { return this.canApplyMaxHarvestingDepthLevelForParameters; }
	
	/**
	 * Returns the maximally allowed harvesting depth level to restrict application of parameters declared in nested structures
	 * 
	 * @return the maximally allowed harvesting depth level to restrict application of parameters declared in nested structures
	 */
	public int getMaxHarvestingParametersDepthLevelAcrossHierarchies() {
		return this.maxHarvestingParametersDepthLevelAcrossHierarchies;
	}
	
	/**
	 * Returns true if max harvesting depth level of local variables should be applied to omit declared local variables in nested structures otherwise false
	 * 
	 * @return true if max harvesting depth level of local variables should be applied to omit declared local variables in nested structures otherwise false
	 */
	public boolean isHarvestingLocalVariablesDepthLevelAllowed() { return this.canApplyMaxHarvestingDepthLevelForParameters; }
	
	/**
	 * Returns the maximally allowed harvesting depth level to restrict application of local variables declared in nested structures
	 * 
	 * @return the maximally allowed harvesting depth level to restrict application of local variables declared in nested structures
	 */
	public int getMaxHarvestingLocalVariablesDepthLevelAcrossHierarchies() {
		return this.maxHarvestingLocalVariablesDepthLevelAcrossHierarchies;
	}
	
	/**
	 * Returns true if parsing side inner context while getting parameters and variables for substitution should be omitted otherwise false
	 * 
	 * @return true if parsing side inner context while getting parameters and variables for substitution should be omitted otherwise false
	 */
	public boolean shouldOmitParsingSideInnerContexts() { return this.omitParsingSideInnerContexts; }
}
