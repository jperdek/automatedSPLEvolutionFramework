package splEvolutionCore;


import positiveVariabilityManagement.AllVariationPointContentInjectionAggregator;
import positiveVariabilityManagement.SelectionOfConstructsAcrossSelectedVariationPointsStrategies;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.CallsInstantiationFromTemplate;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.WrapperBasedInstantiationsFromTemplate;
import positiveVariabilityManagement.callsTemplateSelectionStrategies.CallsFromPositiveVariationPointCreator;
import positiveVariabilityManagement.callsTemplateSelectionStrategies.RecursionCallsFromPositiveVariationPointCreator;
import positiveVariabilityManagement.fragmentManagement.CodeIncrementGranularityManagementStrategy;
import positiveVariabilityManagement.fragmentManagement.QuantityBasedGranularityManagementStrategy;
import positiveVariabilityManagement.parameterVaribleMatchingStrategies.ParameterMatchingStrategy;
import positiveVariabilityManagement.parameterVaribleMatchingStrategies.SimilarityMatchingOfParameters;
import splEvolutionCore.SPLEvolutionCore.SyntetizeConstructsOptions;
import splEvolutionCore.candidateSelector.valueAssignment.ChosenValueAssignmentStrategyForNegativeVariability;
import splEvolutionCore.candidateSelector.valueAssignment.ChosenValueAssignmentStrategyForPositiveVariability;
import splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment.ComplexityValueAssignmentNegativeVariability;
import splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment.ComplexityValueAssignmentPositiveVariability;
import splEvolutionCore.candidateSelector.valueAssignment.featureSelectionStrategies.constructSelection.AffectUpToConstantPlacesByFeatureConstructsStrategy;
import splEvolutionCore.candidateSelector.valueAssignment.featureSelectionStrategies.constructSelection.FeatureConstructsSelectionStrategy;
import splEvolutionCore.candidateSelector.valueAssignment.featureSelectionStrategies.templateSelection.FeatureSelectionStrategy;
import splEvolutionCore.candidateSelector.valueAssignment.featureSelectionStrategies.templateSelection.SelectUpToConstantConstructsAsFeatures;
import divisioner.VariationPointDivisionConfiguration;
import divisioner.divisionStrategies.RecallStrategy;
import java.util.ArrayList;
import java.util.List;


/**
 * Supported configurations to manage evolution phase/phases
 * 
 * @author Jakub Perdek
 *
 */
public class EvolutionConfigurations {

	/**
	 * Configuration based on semantic relations
	 * 
	 * @return the configuration to reach maximal benfits from semantic orientation
	 */
	public static EvolutionCoreSettings getMaximalSemanticOrientedConfiguration() {
		EvolutionCoreStrategies evolutionCoreStrategies = new DefaultEvolutionCore();
		CallsFromPositiveVariationPointCreator positiveVariabilityCreatorStrategy = new RecursionCallsFromPositiveVariationPointCreator();
		//new NoneEntityCreationFromPositiveVariationPointCreator();
		CallsInstantiationFromTemplate callsInstantiationFromTemplateStrategy = new WrapperBasedInstantiationsFromTemplate();
		ParameterMatchingStrategy parameterMatchingStrategy = new SimilarityMatchingOfParameters();
		List<ChosenValueAssignmentStrategyForNegativeVariability> negativeVariabilityValueAssignmentStrategies = 
				new ArrayList<ChosenValueAssignmentStrategyForNegativeVariability>(List.of(new ComplexityValueAssignmentNegativeVariability()));
		negativeVariabilityValueAssignmentStrategies.add(new ComplexityValueAssignmentNegativeVariability());
		List<ChosenValueAssignmentStrategyForPositiveVariability> positiveVariabilityValueAssignmentStrategies = 
				new ArrayList<ChosenValueAssignmentStrategyForPositiveVariability>(
						List.of(new ComplexityValueAssignmentPositiveVariability()));
		positiveVariabilityValueAssignmentStrategies.add(new ComplexityValueAssignmentPositiveVariability());
		FeatureSelectionStrategy featureSelectionStrategy = new SelectUpToConstantConstructsAsFeatures(
				SPLEvolutionCore.DEFAULT_NUMBER_OF_FEATURES_TO_SELECT);
		FeatureConstructsSelectionStrategy featureSelectionConstructsStrategy = 
				new AffectUpToConstantPlacesByFeatureConstructsStrategy(
						SPLEvolutionCore.DEFAULT_NUMBER_OF_FEATURE_CONSTRUCTS_TO_SELECT, SyntetizeConstructsOptions.UP_TO_SELECTED_FEATURES);
		CodeIncrementGranularityManagementStrategy codeIncrementGranularityManagementStrategy = 
				new QuantityBasedGranularityManagementStrategy(SPLEvolutionCore.NUMBER_LINES_TO_INTRODUCE_METHOD, 
						SPLEvolutionCore.NUMBER_METHODS_TO_INTRODUCE_CLASS);
		VariationPointDivisionConfiguration variationPointDivisionConfiguration = new RecallStrategy();
		SelectionOfConstructsAcrossSelectedVariationPointsStrategies selectionOfConstructsAcrossSelectedVariationPointsStrategies = 
				new AllVariationPointContentInjectionAggregator();//new TopologicallyDiverseConstructsSelection(); 
		EvolutionCoreSettings maximalSemanticOrientedConfiguration = new EvolutionCoreSettings(evolutionCoreStrategies,
				positiveVariabilityCreatorStrategy, callsInstantiationFromTemplateStrategy, parameterMatchingStrategy,
				negativeVariabilityValueAssignmentStrategies, positiveVariabilityValueAssignmentStrategies, 
				featureSelectionStrategy, featureSelectionConstructsStrategy, 
				codeIncrementGranularityManagementStrategy, selectionOfConstructsAcrossSelectedVariationPointsStrategies,
				variationPointDivisionConfiguration);
		return maximalSemanticOrientedConfiguration;
	}
	
	/**
	 * Creates actual configuration according to settings (of variables) in SPLEvolutionCore
	 * 
	 * @return evolution configurations according to settings (of variables) from SPLEvolutionCore file
	 */
	public static EvolutionCoreSettings getActualConfiguration() { return new EvolutionCoreSettings(); }
}
