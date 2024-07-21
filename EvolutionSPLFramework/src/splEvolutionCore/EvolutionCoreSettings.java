package splEvolutionCore;

import positiveVariabilityManagement.AllVariationPointContentInjectionAggregator;
import positiveVariabilityManagement.SelectionOfConstructsAcrossSelectedVariationPointsStrategies;
import positiveVariabilityManagement.TopologicallyDiverseConstructsSelection;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.AllInstantiationsFromTemplate;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.CallsInstantiationFromTemplate;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.UniqueParametersInInstantiationsFromTemplate;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.WrapperBasedInstantiationsFromTemplate;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.ActualScriptVariablesToSubstituteConfiguration;
import positiveVariabilityManagement.callsTemplateSelectionStrategies.AllCallsFromPositiveVariationPointCreator;
import positiveVariabilityManagement.callsTemplateSelectionStrategies.CallsFromPositiveVariationPointCreator;
import positiveVariabilityManagement.callsTemplateSelectionStrategies.NoneEntityCreationFromPositiveVariationPointCreator;
import positiveVariabilityManagement.callsTemplateSelectionStrategies.RecursionReducedCallsFromPositiveVariationPointCreator;
import positiveVariabilityManagement.fragmentManagement.CodeIncrementGranularityManagementStrategy;
import positiveVariabilityManagement.fragmentManagement.QuantityBasedGranularityManagementStrategy;
import positiveVariabilityManagement.fragmentManagement.SimilarityBasedGranularityManagementStrategy;
import splEvolutionCore.candidateSelector.valueAssignment.ChosenValueAssignmentStrategyForNegativeVariability;
import splEvolutionCore.candidateSelector.valueAssignment.ChosenValueAssignmentStrategyForPositiveVariability;
import splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment.ComplexityValueAssignmentNegativeVariability;
import splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment.ComplexityValueAssignmentPositiveVariability;
import splEvolutionCore.candidateSelector.valueAssignment.featureSelectionStrategies.constructSelection.AffectUpToConstantPlacesByFeatureConstructsStrategy;
import splEvolutionCore.candidateSelector.valueAssignment.featureSelectionStrategies.constructSelection.FeatureConstructsSelectionStrategy;
import splEvolutionCore.candidateSelector.valueAssignment.featureSelectionStrategies.templateSelection.FeatureSelectionStrategy;
import splEvolutionCore.candidateSelector.valueAssignment.featureSelectionStrategies.templateSelection.SelectUpToConstantConstructsAsFeatures;
import positiveVariabilityManagement.parameterVaribleMatchingStrategies.MatchAllParameters;
import positiveVariabilityManagement.parameterVaribleMatchingStrategies.ParameterMatchingStrategy;
import positiveVariabilityManagement.parameterVaribleMatchingStrategies.SimilarityMatchingOfParameters;
import splEvolutionCore.SPLEvolutionCore.CodeIncrementGranularityManagementStrategies;
import splEvolutionCore.SPLEvolutionCore.EvolutionStrategies;
import splEvolutionCore.SPLEvolutionCore.ExtractedCallsFromPositiveVariationPointCreatorStrategies;
import splEvolutionCore.SPLEvolutionCore.ExtractedCallsInstantiationFromTemplateStrategies;
import splEvolutionCore.SPLEvolutionCore.FeatureConstructsSelectionStrategies;
import splEvolutionCore.SPLEvolutionCore.FeatureSelectionStrategies;
import splEvolutionCore.SPLEvolutionCore.ParameterMatchingStrategyStrategies;
import splEvolutionCore.SPLEvolutionCore.SyntetizeConstructsOptions;
import splEvolutionCore.SPLEvolutionCore.VariationPointDivisionConfigurationStrategies;
import divisioner.VariationPointDivisioning;
import divisioner.divisionStrategies.RecallStrategy;
import java.util.ArrayList;
import java.util.List;

import dataRepresentationsExtensions.DefaultDataRepresentations;


/**
 * Instantiates strategies according to configuration from SPLEvolutionCore
 * 
 * @author Jakub Perdek
 *
 */
public class EvolutionCoreSettings {

	private CallsFromPositiveVariationPointCreator positiveVariabilityCreatorStrategy;
	private CallsInstantiationFromTemplate callsInstantiationFromTemplateStrategy;
	private ParameterMatchingStrategy parameterMatchingStrategy;
	private List<ChosenValueAssignmentStrategyForNegativeVariability> negativeVariabilityValueAssignmentStrategies;
	private List<ChosenValueAssignmentStrategyForPositiveVariability> positiveVariabilityValueAssignmentStrategies;
	
	private FeatureSelectionStrategy featureSelectionStrategy;
	private FeatureConstructsSelectionStrategy featureConstructsSelectionStrategy;
	private CodeIncrementGranularityManagementStrategy codeIncrementGranularityManagementStrategy;
	private SelectionOfConstructsAcrossSelectedVariationPointsStrategies selectionOfConstructsAcrossSelectedVariationPointsStrategies;
	private VariationPointDivisioning variationPointDivisioning;
	private EvolutionCoreStrategies evolutionCoreStrategy;

	
	/**
	 * Configuration to get actually available functionality that can be substituted or directly create it
	 */
	private ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration;
	
	/**
	 * Creates evolution phase configuration according to settings (of variables) from SPLEvolutionCore 
	 */
	public EvolutionCoreSettings() {
		this.evolutionCoreStrategy = EvolutionCoreSettings.createSelectedEvolutionCoreStrategy();
		this.positiveVariabilityCreatorStrategy = EvolutionCoreSettings.createSelectedCallsFromPositiveVariationPointCreator();
		this.callsInstantiationFromTemplateStrategy = EvolutionCoreSettings.createSelectedCallsInstantiationFromTemplate();
		this.parameterMatchingStrategy = EvolutionCoreSettings.createSelectedCallsForParameterMatchingStrategy();
		this.negativeVariabilityValueAssignmentStrategies = EvolutionCoreSettings.createChosenValueAssignmentStrategiesForNegativeVariability();
		this.positiveVariabilityValueAssignmentStrategies = EvolutionCoreSettings.createChosenValueAssignmentStrategiesForPositiveVariability();
		this.featureSelectionStrategy = EvolutionCoreSettings.createSelectedFeatureSelectionStrategy();
		this.featureConstructsSelectionStrategy = EvolutionCoreSettings.createSelectedFeatureConstructsSelectionStrategy(
				SyntetizeConstructsOptions.TOPOLOGICALLY_DIVERSE_SELECTED);
		this.codeIncrementGranularityManagementStrategy = EvolutionCoreSettings.createSelectedCodeGranularityManagementStrategy();
		this.variationPointDivisioning = EvolutionCoreSettings.createVariationPointDivisioning();
		this.selectionOfConstructsAcrossSelectedVariationPointsStrategies = 
				EvolutionCoreSettings.createSelectionOfConstructsAcrossSelectedVariationPointsStrategies();
		this.actualScriptVariablesToSubstituteConfiguration = EvolutionCoreSettings.createActualScriptVariablesToSubstituteConfiguration();
	}
	
	/**
	 * Creates evolution phase configuration according to user selected strategies
	 * 
	 * @param evolutionCoreStrategy
	 * @param positiveVariabilityCreatorStrategy
	 * @param callsInstantiationFromTemplateStrategy
	 * @param parameterMatchingStrategy
	 * @param negativeVariabilityValueAssignmentStrategies
	 * @param positiveVariabilityValueAssignmentStrategies
	 * @param featureSelectionStrategy
	 * @param featureConstructsSelectionStrategy
	 * @param codeIncrementGranularityManagementStrategy
	 * @param selectionOfConstructsAcrossSelectedVariationPointsStrategies
	 * @param variationPointDivisionConfiguration
	 * @param actualScriptVariablesToSubstituteConfiguration
	 */
	public EvolutionCoreSettings(
			EvolutionCoreStrategies evolutionCoreStrategy,
			CallsFromPositiveVariationPointCreator positiveVariabilityCreatorStrategy,
			CallsInstantiationFromTemplate callsInstantiationFromTemplateStrategy,
			ParameterMatchingStrategy parameterMatchingStrategy,
			List<ChosenValueAssignmentStrategyForNegativeVariability> negativeVariabilityValueAssignmentStrategies,
			List<ChosenValueAssignmentStrategyForPositiveVariability> positiveVariabilityValueAssignmentStrategies,
			FeatureSelectionStrategy featureSelectionStrategy,
			FeatureConstructsSelectionStrategy featureConstructsSelectionStrategy,
			CodeIncrementGranularityManagementStrategy codeIncrementGranularityManagementStrategy,
			SelectionOfConstructsAcrossSelectedVariationPointsStrategies selectionOfConstructsAcrossSelectedVariationPointsStrategies,
			VariationPointDivisioning variationPointDivisioning, 
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration) {
		this.evolutionCoreStrategy = evolutionCoreStrategy;
		this.positiveVariabilityCreatorStrategy = positiveVariabilityCreatorStrategy;
		this.callsInstantiationFromTemplateStrategy = callsInstantiationFromTemplateStrategy;
		this.parameterMatchingStrategy = parameterMatchingStrategy;
		this.negativeVariabilityValueAssignmentStrategies = new ArrayList<ChosenValueAssignmentStrategyForNegativeVariability>(
				negativeVariabilityValueAssignmentStrategies);
		this.positiveVariabilityValueAssignmentStrategies = new ArrayList<ChosenValueAssignmentStrategyForPositiveVariability>(
				positiveVariabilityValueAssignmentStrategies);
		this.featureSelectionStrategy = featureSelectionStrategy;
		this.featureConstructsSelectionStrategy = featureConstructsSelectionStrategy;
		this.codeIncrementGranularityManagementStrategy = codeIncrementGranularityManagementStrategy;
		this.variationPointDivisioning = variationPointDivisioning;
		this.selectionOfConstructsAcrossSelectedVariationPointsStrategies = 
				selectionOfConstructsAcrossSelectedVariationPointsStrategies;
		this.actualScriptVariablesToSubstituteConfiguration = actualScriptVariablesToSubstituteConfiguration;
	}
	
	/**
	 * Instantiates strategy to manage given evolution iteration according to to settings (of variables) from SPLEvolutionCore 
	 * 
	 * @return strategy to manage given evolution iteration
	 */
	private static EvolutionCoreStrategies createSelectedEvolutionCoreStrategy() {
		if (SPLEvolutionCore.SELECTED_EVOLUTION_STRATEGY == EvolutionStrategies.DEFAULT) {
			return new DefaultEvolutionCore();
		}
		return new DefaultEvolutionCore();
	}
	
	/**
	 * Instantiates strategy to manage the divisions into positive and negative variability in the 
	 * whole evolution according to to settings (of variables) from SPLEvolutionCore in wrapped manager entity
	 *  
	 * @return strategy to manage the divisions into positive and negative variability in the 
	 * whole evolution according to to settings (of variables) from SPLEvolutionCore in wrapped manager entity
	 */
	private static VariationPointDivisioning createVariationPointDivisioning() {
		if (SPLEvolutionCore.SELECTED_VARIATION_POINT_DICISION_CONFIGURATION_STRATEGY ==
				VariationPointDivisionConfigurationStrategies.RECALL_DIVISIONING) {
			return new VariationPointDivisioning(new RecallStrategy());
		}
		return new VariationPointDivisioning(new RecallStrategy());
	}
	
	/**
	 * Instantiates strategy to manage value assignment during use of various measurements related to negative 
	 * variability according to to settings (of variables) from SPLEvolutionCore 
	 * 
	 * @return strategy to manage value assignment during use of various measurements related to negative 
	 * variability according to to settings (of variables) from SPLEvolutionCore 
	 */
	private static List<ChosenValueAssignmentStrategyForNegativeVariability> 
						createChosenValueAssignmentStrategiesForNegativeVariability() {
		List<ChosenValueAssignmentStrategyForNegativeVariability> chosenNegativeVariabilityValueAssignmentStrategies = new ArrayList<
				ChosenValueAssignmentStrategyForNegativeVariability>();
		if (SPLEvolutionCore.SELECTED_NEGATIVE_VARIABILITY_VALUE_STRATEGIES.contains(
				SPLEvolutionCore.NEGATIVE_VARIABILITY_VALUE_ASSIGNMENT_STRATEGIES.COMPLEXITY_VALUE_ASSIGNMENT_NEGATIVE_VARIABILITY)) {
			chosenNegativeVariabilityValueAssignmentStrategies.add(new ComplexityValueAssignmentNegativeVariability());
		}
		return chosenNegativeVariabilityValueAssignmentStrategies;
	}
	
	/**
	 * Instantiates strategy to manage value assignment during use of various measurements related to positive
	 * variability according to to settings (of variables) from SPLEvolutionCore 
	 * 
	 * @return strategy to manage value assignment during use of various measurements related to positive
	 *  variability according to to settings (of variables) from SPLEvolutionCore
	 */
	private static List<ChosenValueAssignmentStrategyForPositiveVariability> createChosenValueAssignmentStrategiesForPositiveVariability() {
		List<ChosenValueAssignmentStrategyForPositiveVariability> chosenPositiveVariabilityValueAssignmentStrategies = new ArrayList<
				ChosenValueAssignmentStrategyForPositiveVariability>();
		if (SPLEvolutionCore.SELECTED_POSITIVE_VARIABILITY_VALUE_STRATEGIES.contains(
				SPLEvolutionCore.POSITIVE_VARIABILITY_VALUE_ASSIGNMENT_STRATEGIES.COMPLEXITY_VALUE_ASSIGNMENT_POSITIVE_VARIABILITY)) {
			chosenPositiveVariabilityValueAssignmentStrategies.add(new ComplexityValueAssignmentPositiveVariability());
		}
		return chosenPositiveVariabilityValueAssignmentStrategies;
	}
	
	/**
	 * Instantiates strategy to manage selection of positive variability callable templates according
	 *  to settings (of variables) from SPLEvolutionCore 
	 * 
	 * @return strategy to manage selection of positive variability callable templates according
	 *  to settings (of variables) from SPLEvolutionCore 
	 */
	private static CallsFromPositiveVariationPointCreator createSelectedCallsFromPositiveVariationPointCreator() {
		if(SPLEvolutionCore.SELECTED_POSITIVE_VARIABILITY_CALLS_CREATOR_STRATEGY.equals(
				ExtractedCallsFromPositiveVariationPointCreatorStrategies.EXTRACT_ALL_EXCEPT_RECURSION_CALLS)) {
			return new RecursionReducedCallsFromPositiveVariationPointCreator();
		} else if (SPLEvolutionCore.SELECTED_POSITIVE_VARIABILITY_CALLS_CREATOR_STRATEGY.equals(
				ExtractedCallsFromPositiveVariationPointCreatorStrategies.EXTRACT_ALL_CALLS)){
			return new AllCallsFromPositiveVariationPointCreator();
		} else if (SPLEvolutionCore.SELECTED_POSITIVE_VARIABILITY_CALLS_CREATOR_STRATEGY.equals(
				ExtractedCallsFromPositiveVariationPointCreatorStrategies.NONE_ENTITY_CREATION)){
			return new NoneEntityCreationFromPositiveVariationPointCreator();
		} else if (SPLEvolutionCore.SELECTED_POSITIVE_VARIABILITY_CALLS_CREATOR_STRATEGY.equals(
				ExtractedCallsFromPositiveVariationPointCreatorStrategies.EXTRACT_RECURSION_CALLS_ONLY)) {
			return new RecursionReducedCallsFromPositiveVariationPointCreator();
		}
		return new AllCallsFromPositiveVariationPointCreator();
	}
	
	/**
	 * Instantiates strategy to manage selection of callable constructs instantiated from templates
	 * according to to settings (of variables) from SPLEvolutionCore 
	 * 
	 * @return strategy to manage selection of callable constructs instantiated from templates
	 * according to to settings (of variables) from SPLEvolutionCore 
	 */
	private static CallsInstantiationFromTemplate createSelectedCallsInstantiationFromTemplate() {
		if(SPLEvolutionCore.SELECTED_EXTRACTOR_CALLS_INSTANTION_FROM_TEMPLATE_STRATEGY.equals(
				ExtractedCallsInstantiationFromTemplateStrategies.ALL_SUBSTITUTIONS)) {
			return new AllInstantiationsFromTemplate();
		} else if (SPLEvolutionCore.SELECTED_EXTRACTOR_CALLS_INSTANTION_FROM_TEMPLATE_STRATEGY.equals(
				ExtractedCallsInstantiationFromTemplateStrategies.UNIQUE_PARAMETER_SUBSTITUTION)){
			return new UniqueParametersInInstantiationsFromTemplate();
		} else if (SPLEvolutionCore.SELECTED_EXTRACTOR_CALLS_INSTANTION_FROM_TEMPLATE_STRATEGY.equals(
				ExtractedCallsInstantiationFromTemplateStrategies.WRAPPER_BASED_SUBSTITUTION)){
			return new WrapperBasedInstantiationsFromTemplate();
		}
		return new WrapperBasedInstantiationsFromTemplate();
	}
	
	/**
	 * Instantiates strategy to manage selection of parameters according to to settings (of variables) from SPLEvolutionCore 
	 * 
	 * @return strategy to manage selection of parameters according to to settings (of variables) from SPLEvolutionCore 
	 */
	private static ParameterMatchingStrategy createSelectedCallsForParameterMatchingStrategy() {
		if(SPLEvolutionCore.SELECTED_PARAMETER_MATCHING_STRATEGY.equals(
				ParameterMatchingStrategyStrategies.MATCH_ALL)) {
			return new MatchAllParameters();
		} else if (SPLEvolutionCore.SELECTED_PARAMETER_MATCHING_STRATEGY.equals(
				ParameterMatchingStrategyStrategies.SIMILARITY_BASED_MATCHING)){
			return new SimilarityMatchingOfParameters();
		}
		return new MatchAllParameters();
	}
	
	/**
	 * Instantiates strategy to manage variation points selection according to to settings (of variables) from SPLEvolutionCore 
	 * 
	 * @return strategy to manage variation points selection according to to settings (of variables) from SPLEvolutionCore 
	 */
	private static FeatureSelectionStrategy createSelectedFeatureSelectionStrategy() {
		if(SPLEvolutionCore.SELECTED_FEATURE_SELECTION_STRATEGY.equals(
				FeatureSelectionStrategies.CONSTANT_FEATURES)) {
			return new SelectUpToConstantConstructsAsFeatures(SPLEvolutionCore.DEFAULT_NUMBER_OF_FEATURES_TO_SELECT);
		} 
		return new SelectUpToConstantConstructsAsFeatures(SPLEvolutionCore.DEFAULT_NUMBER_OF_FEATURES_TO_SELECT);
	}
	
	/**
	 * Instantiates strategy to manage variation points selection according to to settings (of variables) from SPLEvolutionCore 
	 * 
	 * @return strategy to manage variation points selection according to to settings (of variables) from SPLEvolutionCore 
	 */
	private static ActualScriptVariablesToSubstituteConfiguration createActualScriptVariablesToSubstituteConfiguration() { 
		return new ActualScriptVariablesToSubstituteConfiguration(
				SPLEvolutionCore.USE_ACTUAL_SCRIPT_VARIABLES, SPLEvolutionCore.USE_PARAMETERS, 
				SPLEvolutionCore.USE_GLOBAL_VARIABLES, SPLEvolutionCore.USE_CURRENT_LEVEL_VARIABLES_ONLY, 
				SPLEvolutionCore.INSTANTIATE_NEW_ENTITIES_ACCORDING_TO_TYPE);
	}
	
	/**
	 * Instantiates strategy to select code callable constructs for each selected variation point according to 
	 * settings (of variables) from SPLEvolutionCore 
	 * 
	 * @param syntetizeConstructsOptions
	 * @return strategy to select code callable constructs for each selected variation point according to 
	 * settings (of variables) from SPLEvolutionCore 
	 */
	private static FeatureConstructsSelectionStrategy createSelectedFeatureConstructsSelectionStrategy(
			SyntetizeConstructsOptions syntetizeConstructsOptions) {
		if(SPLEvolutionCore.SELECTED_FEATURE_CONSTRUCTS_SELECTION_STRATEGY.equals(
				FeatureConstructsSelectionStrategies.CONSTANT_CALLABLE_CONSTRUCTS)) {
			return new AffectUpToConstantPlacesByFeatureConstructsStrategy(
					SPLEvolutionCore.DEFAULT_NUMBER_OF_FEATURE_CONSTRUCTS_TO_SELECT, syntetizeConstructsOptions);
		} 
		return new AffectUpToConstantPlacesByFeatureConstructsStrategy(
				SPLEvolutionCore.DEFAULT_NUMBER_OF_FEATURE_CONSTRUCTS_TO_SELECT, syntetizeConstructsOptions);
	}
	
	/**
	 * Instantiates strategy to manage granularity of synthesized content that is represented as various data 
	 * structures according to settings (of variables) from SPLEvolutionCore 
	 * 
	 * @return strategy to manage granularity of synthesized content that is represented as various data 
	 * structures according to settings (of variables) from SPLEvolutionCore 
	 */
	private static CodeIncrementGranularityManagementStrategy createSelectedCodeGranularityManagementStrategy() {
		if(SPLEvolutionCore.SELECTED_CODE_INCREMENT_GRANULARITY_MANAGEMENT_STRATEGY.equals(
				CodeIncrementGranularityManagementStrategies.QUANTITY_BASED_GRANULARITY)) {
			return new QuantityBasedGranularityManagementStrategy(
					SPLEvolutionCore.NUMBER_LINES_TO_INTRODUCE_METHOD, 
					SPLEvolutionCore.NUMBER_METHODS_TO_INTRODUCE_CLASS);
		} else if(SPLEvolutionCore.SELECTED_CODE_INCREMENT_GRANULARITY_MANAGEMENT_STRATEGY.equals(
				CodeIncrementGranularityManagementStrategies.SIMILARITY_BASED_GRANULARITY)) {
			return new SimilarityBasedGranularityManagementStrategy();
		}
		return new QuantityBasedGranularityManagementStrategy(
				SPLEvolutionCore.NUMBER_LINES_TO_INTRODUCE_METHOD, 
				SPLEvolutionCore.NUMBER_METHODS_TO_INTRODUCE_CLASS);
	}
	
	/**
	 * Instantiates strategy to strategy to synthesize feature constructs according to to settings
	 * (of variables) from SPLEvolutionCore 
	 * 
	 * @return strategy to strategy to synthesize feature constructs according to to settings
	 * (of variables) from SPLEvolutionCore
	 */
	private static SelectionOfConstructsAcrossSelectedVariationPointsStrategies createSelectionOfConstructsAcrossSelectedVariationPointsStrategies() {
		if(SPLEvolutionCore.SYNTETIZE_FEATURE_CONSTRUCTS_OPTION.isSelectionBasedOnly()) {
			return new AllVariationPointContentInjectionAggregator();
		} else {
			if(SPLEvolutionCore.SYNTETIZE_FEATURE_CONSTRUCTS_OPTION.equals(
					SyntetizeConstructsOptions.TOPOLOGICALLY_DIVERSE_SELECTED)) {
				return new TopologicallyDiverseConstructsSelection();
			}
		}
		return new TopologicallyDiverseConstructsSelection(); // preference to more diverse constructs across varaition points
	}
	
	/**
	 * Returns strategy to manage given evolution iteration 
	 * 
	 * @return strategy to manage given evolution iteration
	 */
	public EvolutionCoreStrategies getEvolutionCoreStrategy() { return this.evolutionCoreStrategy; }
	
	/**
	 * Returns prepared strategy to manage the divisions into positive and negative variability in the 
	 * whole evolution according to to settings (of variables) from SPLEvolutionCore in wrapped manager entity
	 *  
	 * @return strategy to manage the divisions into positive and negative variability in the 
	 * whole evolution according to to settings (of variables) from SPLEvolutionCore in wrapped manager entity
	 */
	public VariationPointDivisioning getVariationPointDivisioning() { return this.variationPointDivisioning; }
	
	public FeatureConstructsSelectionStrategy getFeatureConstructSelectionStrategy() {
		return this.featureConstructsSelectionStrategy;
	}
	
	public CallsFromPositiveVariationPointCreator getCallsFromPositiveVariationPointCreator() {
		return this.positiveVariabilityCreatorStrategy;
	}
	
	public CallsInstantiationFromTemplate getCallsInstantiationFromTemplate() {
		return this.callsInstantiationFromTemplateStrategy;
	}
	
	public ParameterMatchingStrategy getParameterMatchingStrategy() {
		return this.parameterMatchingStrategy;
	}
	
	public List<ChosenValueAssignmentStrategyForNegativeVariability> getChosenValueAssignmentStrategiesForNegativeVariability() {
		return this.negativeVariabilityValueAssignmentStrategies;
	}
	
	public List<ChosenValueAssignmentStrategyForPositiveVariability> getChosenValueAssignmentStrategiesForPositiveVariability() {
		return this.positiveVariabilityValueAssignmentStrategies;
	}
	
	public FeatureSelectionStrategy getFeatureSelectionStrategy() {
		return this.featureSelectionStrategy;
	}
	
	public FeatureConstructsSelectionStrategy getFeatureConstructsSelectionStrategy() {
		return this.featureConstructsSelectionStrategy;
	}
	
	/**
	 * Returns 
	 * 
	 * @return
	 */
	public CodeIncrementGranularityManagementStrategy getCodeIncrementGranularityManagementStrategy() {
		return this.codeIncrementGranularityManagementStrategy;
	}
	
	public SelectionOfConstructsAcrossSelectedVariationPointsStrategies getSelectionOfConstructsSelectionStrategies() {
		return this.selectionOfConstructsAcrossSelectedVariationPointsStrategies;
	}
	
	public ActualScriptVariablesToSubstituteConfiguration getActualScriptVariablesToSubstituteConfiguration() {
		return this.actualScriptVariablesToSubstituteConfiguration;
	}
}
