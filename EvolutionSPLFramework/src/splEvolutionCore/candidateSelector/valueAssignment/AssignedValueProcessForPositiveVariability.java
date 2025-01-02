package splEvolutionCore.candidateSelector.valueAssignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codeContext.processors.export.exportedFileUnits.FileExportsUnits;
import positiveVariabilityManagement.UnmappedContextException;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.AlreadyChosenVariationPointForInjectionException;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.CallableConstruct;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.CallsInstantiationFromTemplate;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.ActualScriptVariablesToSubstituteConfiguration;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.AllVariablesMapper;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.ParameterInjectionPositionObservation;
import positiveVariabilityManagement.callsTemplateSelectionStrategies.CallsFromPositiveVariationPointCreator;
import positiveVariabilityManagement.entities.DuplicatedContextIdentifier;
import positiveVariabilityManagement.parameterVaribleMatchingStrategies.ParameterMatchingStrategy;
import splEvolutionCore.DebugInformation;
import splEvolutionCore.candidateSelector.DifferentlyAggregatedLocation;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidate;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;
import splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment.MethodToEvaluateComplexityNotFoundException;
import splEvolutionCore.derivation.DerivationResourcesManager;


/**
 * Manages value assignment from various measured metrics to manage positive variability
 * -various strategies are applied to challenge extensive pre-solution space and properly direct evolution
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class AssignedValueProcessForPositiveVariability extends AssignedValueProcess<ChosenValueAssignmentStrategyForPositiveVariability> {
	
	/**
	 * Logger to track value assignment process performed for positive variability
	 */
	private static final Logger logger = LoggerFactory.getLogger(AssignedValueProcessForPositiveVariability.class);
	
	/**
	 * Strategy to load possible calls from the positive variation point marker
	 */
	private CallsFromPositiveVariationPointCreator positiveVariabilityCreatorStrategy;
	
	/**
	 * Strategy to manage instantiation of calls from the template - the parameterized call of function/constructor
	 */
	private CallsInstantiationFromTemplate callsInstantiationFromTemplateStrategy;
	
	/**
	 * NOT INCORPORATED IN THIS VERSION
	 * Strategy to manage injections/substitutions of parameters to instantiated calls from template
	 */
	private ParameterMatchingStrategy parameterMatchingStrategy;
	
	/**
	 * Manages available variables for given variation point
	 */
	private AllVariablesMapper allVariablesMapper;
	

	/**
	 * Creates manager to manage instantiation, selection amongst available variable constructs, use of various 
	 * value-assignment strategies to assign given value, and stores first assigned strategy
	 * 
	 * @param positiveVariabilityCreatorStrategy - strategy to load possible calls from the positive variation point marker
	 * @param callsInstantiationFromTemplateStrategy - strategy to manage instantiation of calls from the template - the parameterized call of function/constructor
	 * @param chosenValueAssignmentStrategyForNegativeVariability - value assignment strategy to assign value/score of given measurement to instantiated callable construct 
	 */
	public AssignedValueProcessForPositiveVariability(CallsFromPositiveVariationPointCreator positiveVariabilityCreatorStrategy, 
			CallsInstantiationFromTemplate callsInstantiationFromTemplateStrategy,
			ChosenValueAssignmentStrategyForPositiveVariability  chosenValueAssignmentStrategyForNegativeVariability) {
		super(chosenValueAssignmentStrategyForNegativeVariability);
		this.positiveVariabilityCreatorStrategy = positiveVariabilityCreatorStrategy;
		this.callsInstantiationFromTemplateStrategy = callsInstantiationFromTemplateStrategy;
	}

	/**
	 * Creates manager to manage instantiation, selection amongst available variable constructs, use of various 
	 * value-assignment strategies to assign given value, and stores the list of assigned strategies
	 * 
	 * @param positiveVariabilityCreatorStrategy - strategy to load possible calls from the positive variation point marker
	 * @param callsInstantiationFromTemplateStrategy - strategy to manage instantiation of calls from the template - the parameterized call of function/constructor
	 * @param chosenValueAssignmentStrategyForNegativeVariabilities - value assignment strategy to assign value/score of given measurement to instantiated callable construct 
	 * @param availableExportUnits - the entities with the external exported resources, especially variables
	 * @param parameterInjectionPositionObservation - instance managing injection of actually available parameters/variables
	 * @throws DuplicatedContextIdentifier - the exception thrown if duplicated identifiers are assigned amongst contexts
	 */
	public AssignedValueProcessForPositiveVariability(CallsFromPositiveVariationPointCreator positiveVariabilityCreatorStrategy, 
			CallsInstantiationFromTemplate callsInstantiationFromTemplateStrategy,
			List<ChosenValueAssignmentStrategyForPositiveVariability> chosenValueAssignmentStrategyForNegativeVariabilities, 
			FileExportsUnits availableExportUnits,
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration,
			ParameterInjectionPositionObservation parameterInjectionPositionObservation) throws DuplicatedContextIdentifier {
		super(chosenValueAssignmentStrategyForNegativeVariabilities);
		this.positiveVariabilityCreatorStrategy = positiveVariabilityCreatorStrategy;
		this.callsInstantiationFromTemplateStrategy = callsInstantiationFromTemplateStrategy;
		this.allVariablesMapper = new AllVariablesMapper(availableExportUnits, 
				actualScriptVariablesToSubstituteConfiguration, parameterInjectionPositionObservation);
	}
	
	/**
	 * Creates manager to manage instantiation, selection amongst available variable constructs, use of various 
	 * value-assignment strategies to assign given value, and stores the array of assigned strategies
	 * 
	 * @param positiveVariabilityCreatorStrategy - strategy to load possible calls from the positive variation point marker
	 * @param callsInstantiationFromTemplateStrategy - strategy to manage instantiation of calls from the template - the parameterized call of function/constructor
	 * @param chosenValueAssignmentStrategyForNegativeVariabilities - value assignment strategy to assign value/score of given measurement to instantiated callable construct 
	 */
	public AssignedValueProcessForPositiveVariability(CallsFromPositiveVariationPointCreator positiveVariabilityCreatorStrategy,
			CallsInstantiationFromTemplate callsInstantiationFromTemplateStrategy,
			ChosenValueAssignmentStrategyForPositiveVariability chosenValueAssignmentStrategyForNegativeVariabilities[]) {
		super(chosenValueAssignmentStrategyForNegativeVariabilities);
		this.positiveVariabilityCreatorStrategy = positiveVariabilityCreatorStrategy;
		this.callsInstantiationFromTemplateStrategy = callsInstantiationFromTemplateStrategy;
	}
	
	/**
	 * The process of assigning the value/score to given positive variability
	 * -iterates through the list of all available templates (selected parameterized unsubstantiated calls) with positive variability variation points
	 * -performs given measurement under evaluated variation point
	 * -inserts value from performed measurements
	 * 
	 * -optional removal of variation point according to inner demands
	 *   -during extraction of templates
	 * 
	 * @param positiveVariationPointCandidatesTemplate - the list of processed positive variation points - information about them
	 * @throws MethodToEvaluateComplexityNotFoundException
	 * @throws IOException - the exception for various problems with file loading
	 * @throws InterruptedException - the exception thrown during interruption
	 * @throws UnmappedContextException - the exception informing about impossibility to map content on the resulting AST of final product in the synthesis process
	 * @throws DifferentlyAggregatedLocation - the exception referring that aggregated exports for different metrics of the same callable construct are different
	 * @throws AlreadyChosenVariationPointForInjectionException 
	 */
	public void assignValuesProcess(List<PositiveVariationPointCandidateTemplates> positiveVariationPointCandidatesTemplate) throws 
			MethodToEvaluateComplexityNotFoundException, IOException, InterruptedException, UnmappedContextException, DifferentlyAggregatedLocation, AlreadyChosenVariationPointForInjectionException {
		boolean notRejectVariationPoint;
		List<PositiveVariationPointCandidateTemplates> positiveVariationPointCandidatesTemplateToRemove = new ArrayList<PositiveVariationPointCandidateTemplates>();
		for (PositiveVariationPointCandidateTemplates positiveVariationPointCandidateTemplates: positiveVariationPointCandidatesTemplate) {
			notRejectVariationPoint = this.assignValues(positiveVariationPointCandidateTemplates);  // measures outcome - usually of the available constructs on variation point
						
			if (!notRejectVariationPoint) { // optional removal - !!!number of variation points is going to decrease - better is to have empty values
				positiveVariationPointCandidatesTemplateToRemove.add(positiveVariationPointCandidateTemplates);
			}
		}
		// removal of redundant positive variation points - with low quality
		positiveVariationPointCandidatesTemplate.removeAll(positiveVariationPointCandidatesTemplateToRemove);
	}
	
	/**
	 * Assigns value to given positive variability variation point
	 * - extracts selected templates according to configured strategy (type of CallsFromPositiveVariationPointCreator) from given positive variation point
	 * - extracts selected constructs according to configured strategy (type of CallsInstantiationFromTemplate) from given selected templates in previous step
	 * - for each callable construct iterates through all available value assignment strategies
	 * - performs given measurement under evaluated variation point
	 * - inserts value from performed measurements
	 * - stores information about quality/score of all selected callable constructs and associates them with positive variation point candidate template
	 * REPEATEDLY CALLED!!!
	 * 
	 * @param positiveVariationPointCandidateTemplates - processed positive variation point which quality of selected callable constructs from selected templates will be evaluated
	 * @return true if value assignment is done correctly otherwise false
	 * @throws MethodToEvaluateComplexityNotFoundException
	 * @throws IOException - the exception for various problems with file loading
	 * @throws InterruptedException - the exception thrown during interruption
	 * @throws UnmappedContextException - the exception informing about impossibility to map content on the resulting AST of final product in the synthesis process
	 * @throws DifferentlyAggregatedLocation - the exception referring that aggregated exports for different metrics of the same callable construct are different
	 * @throws AlreadyChosenVariationPointForInjectionException 
	 */
	private boolean assignValues(PositiveVariationPointCandidateTemplates positiveVariationPointCandidateTemplates) throws 
			MethodToEvaluateComplexityNotFoundException, IOException, InterruptedException, UnmappedContextException, DifferentlyAggregatedLocation, AlreadyChosenVariationPointForInjectionException {
		//  Creating code templates started...
		boolean rejectVariationPointTemplate = positiveVariationPointCandidateTemplates.extractCallTemplatesAccordingToStrategyWithDefaultValue(this.positiveVariabilityCreatorStrategy);
		if (!rejectVariationPointTemplate) { return false; } // optional removal 
		if (DebugInformation.SHOW_POLLUTING_INFORMATION) { logger.info("Processed variation point: " + positiveVariationPointCandidateTemplates.getVariationPointName()); }
		//  done
		
		PositiveVariationPointCandidate positiveVariationPointCandidate = new PositiveVariationPointCandidate(
				positiveVariationPointCandidateTemplates.getVariationPointData());
		
		//  Creating callable constructs from templates started...
		Queue<CallableConstruct> callableConstructs = this.callsInstantiationFromTemplateStrategy.instantiateCallsFromTemplate(
				positiveVariationPointCandidateTemplates, this.allVariablesMapper);
		//  done

		String instantiatedCall;
		Map<String, Map<String, AssignedValue>> assignedValues;
		Map<String, AssignedValue> chosenStrategyAssignedValues;
		//   Assigning values to callable constructs started...
		if (callableConstructs != null) {
			for (CallableConstruct callableConstruct: callableConstructs) {
				instantiatedCall = callableConstruct.instantiateCallableConstruct();
				if (instantiatedCall != null) {
					for (ChosenValueAssignmentStrategyForPositiveVariability chosenStrategy: this.chosenValueAssignmentStrategies) {
						chosenStrategyAssignedValues = chosenStrategy.getStrategyAssignedValues(
								positiveVariationPointCandidateTemplates, instantiatedCall); // evaluates assigned value
						positiveVariationPointCandidate.putAssignedValue(instantiatedCall, 	 // inserts assigned value
								chosenStrategyAssignedValues, callableConstruct.getExportedDependenciesOfFutureImports());
					}
				}
			}
			
			//  done
			//stores information about quality/score of all selected callable constructs and associates them with positive variation point candidate template
			positiveVariationPointCandidateTemplates.associateVariationPointCandidate(positiveVariationPointCandidate);
			return true;
		} else {
			logger.info("Callable constructs are not available for variation point: " + positiveVariationPointCandidateTemplates.getVariationPointName());
		}
		return false;
	}
}
