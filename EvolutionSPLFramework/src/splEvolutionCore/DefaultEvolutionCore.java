package splEvolutionCore;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import codeConstructsEvaluation.transformation.ASTConverterClient;
import codeContext.CodeContext;
import codeContext.persistence.UpdatedTreePersistence;
import codeContext.processors.NotFoundVariableDeclaration;
import codeContext.processors.export.exportedFileUnits.FileExportsUnits;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import divisioner.VariationPointDivisionConfiguration;
import divisioner.VariationPointDivisioning;
import evolutionSimulation.EvolutionConfiguration;
import evolutionSimulation.iteration.AlreadyMappedVariationPointContentsInjection;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.ExportAssetPlanner;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.strategies.AssetMisuse;
import evolutionSimulation.productAssetsInitialization.SharedConfiguration;
import evolutionSimulation.productAssetsInitialization.UnknownResourceToProcessException;
import positiveVariabilityManagement.NewContextsSynthesizer;
import positiveVariabilityManagement.SynthesizedContent;
import positiveVariabilityManagement.UnmappedContextException;
import positiveVariabilityManagement.VariationPointPlaceInArrayNotFound;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.AlreadyChosenVariationPointForInjectionException;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.ActualScriptVariablesToSubstituteConfiguration;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.ParameterInjectionPositionObservation;
import positiveVariabilityManagement.entities.DuplicatedContextIdentifier;
import splEvolutionCore.candidateSelector.AlreadyProvidedArgumentInConfigurationExpressionPlace;
import splEvolutionCore.candidateSelector.DifferentlyAggregatedLocation;
import splEvolutionCore.candidateSelector.DuplicateCandidateIdentifier;
import splEvolutionCore.candidateSelector.NegativeVariationPointCandidate;
import splEvolutionCore.candidateSelector.NegativeVariationPointCandidateSelection;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateSelection;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;
import splEvolutionCore.candidateSelector.valueAssignment.AssignedValueProcessForNegativeVariability;
import splEvolutionCore.candidateSelector.valueAssignment.AssignedValueProcessForPositiveVariability;
import splEvolutionCore.candidateSelector.valueAssignment.ChosenValueAssignmentStrategyForNegativeVariability;
import splEvolutionCore.candidateSelector.valueAssignment.cleaning.AllMarkerRemover;
import splEvolutionCore.candidateSelector.valueAssignment.cleaning.AnnotationsFromCodeRemover;
import splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment.MethodToEvaluateComplexityNotFoundException;
import splEvolutionCore.candidateSelector.valueAssignment.selectionAndUnselectionStrategies.FixedMaximizedCandidatesSelection;
import splEvolutionCore.candidateSelector.valueAssignment.selectionAndUnselectionStrategies.SelectionAndUnselectionStrategy;
import splEvolutionCore.derivation.DerivationResourcesManager;
import variationPointUpdates.VariationPointUpdater;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;


/**
 * Default strategy to evolve SPL project/script
 */
public class DefaultEvolutionCore implements EvolutionCoreStrategies {
	
	/**
	 * Applies/Evolves SPL project/script in default evolution process
	 * 
	 * @param splAstTree - the root of base AST
	 * @param variationPointsArray - the array of representatives from used variation points
	 * @param availableExportUnits - available exports
	 * @param evolutionCoreSettings - strategies instantiated for given evolution phase/phases
	 * @param evolutionConfiguration - the configuration for given evolution phase
	 * @param variationPointDivisioning - 
	 * @param exportAssetPlanner
	 * @throws DuplicateCandidateIdentifier
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 * @throws AlreadyProvidedArgumentInConfigurationExpressionPlace
	 * @throws NotFoundVariableDeclaration
	 * @throws MethodToEvaluateComplexityNotFoundException
	 * @throws DuplicatedContextIdentifier
	 * @throws UnmappedContextException
	 * @throws DifferentlyAggregatedLocation
	 * @throws VariationPointPlaceInArrayNotFound
	 * @throws UnknownResourceToProcessException
	 * @throws AlreadyMappedVariationPointContentsInjection
	 * @throws AssetMisuse 
	 * @throws AlreadyChosenVariationPointForInjectionException 
	 */
	public void evolve(JSONObject splAstTree, JSONArray variationPointsArray, 
			FileExportsUnits availableExportUnits, EvolutionCoreSettings evolutionCoreSettings, 
			EvolutionConfiguration evolutionConfiguration, VariationPointDivisioning variationPointDivisioning, ExportAssetPlanner exportAssetPlanner) throws DuplicateCandidateIdentifier, 
			IOException, InterruptedException, InvalidSystemVariationPointMarkerException, 
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation, 
			AlreadyProvidedArgumentInConfigurationExpressionPlace, NotFoundVariableDeclaration, 
			MethodToEvaluateComplexityNotFoundException, DuplicatedContextIdentifier, 
			UnmappedContextException, DifferentlyAggregatedLocation, VariationPointPlaceInArrayNotFound,
			UnknownResourceToProcessException, AlreadyMappedVariationPointContentsInjection, AssetMisuse, AlreadyChosenVariationPointForInjectionException {
		System.out.println("------------------------------------->");
		System.out.println(variationPointsArray.toString());
		int numberSelectedCandidates = 3;
		List<ChosenValueAssignmentStrategyForNegativeVariability> chosenValueAssignmentStrategyForNegativeVariabilities = 
				evolutionCoreSettings.getChosenValueAssignmentStrategiesForNegativeVariability();
		SelectionAndUnselectionStrategy selectionAndUnselectionFixedCandidatesStrategy = 
				new FixedMaximizedCandidatesSelection(numberSelectedCandidates);
		
		NegativeVariationPointCandidateSelection negativeVariationPointCandidateSelection = 
				new NegativeVariationPointCandidateSelection(selectionAndUnselectionFixedCandidatesStrategy);
		List<NegativeVariationPointCandidate> negativeVariationPointCandidates = 
				NegativeVariationPointCandidateSelection.createNegativeVariabilityCandidates(variationPointsArray);
		
		AssignedValueProcessForNegativeVariability assignedValueNegativeVariabilityProcess = 
				new AssignedValueProcessForNegativeVariability(chosenValueAssignmentStrategyForNegativeVariabilities);
		assignedValueNegativeVariabilityProcess.assignValuesProcess(negativeVariationPointCandidates);
		
		Map<String, NegativeVariationPointCandidate> chosenVariationPoints = 
				negativeVariationPointCandidateSelection.manageCandidateSelectionAndUnselections(negativeVariationPointCandidates);
		
		VariationPointUpdater variationPointsUpdater = new VariationPointUpdater();
		//this.clearUnusedAnnotationsAndMarkers(splAstTree, variationPointsArray);
		DefaultEvolutionCore.clearNegativeVariabilityAnnotationsAndMarkers(splAstTree);
		variationPointsUpdater.updateAstAccordingToNegativeVariabilitySelections(splAstTree, chosenVariationPoints);
		
		DerivationResourcesManager derivationResourcesManager = new DerivationResourcesManager(
				splAstTree, evolutionConfiguration, variationPointsArray);
		
		/*if (DebugInformation.OUTPIT_FILES_AS_ANNOTATED_AST_AND_CODE) {
			UpdatedTreePersistence.persistsAstInFile(SharedConfiguration.PROJECT_PATH + "/evolutionDirectory/evolNum1/conccustom/vpData.json",
					variationPointsArray.toString());
			UpdatedTreePersistence.persistsAstInFile(SharedConfiguration.PROJECT_PATH + "/evolutionDirectory/evolNum1/conccustom/ast.json",
					splAstTree.toString());
			UpdatedTreePersistence.persistsAstInFile(SharedConfiguration.PROJECT_PATH + "/evolutionDirectory/evolNum1/conccustom/ast.txt",
				ASTConverterClient.convertFromASTToCode(splAstTree.toString()));
		}*/
		System.out.println(variationPointsArray.toString());
		System.out.println(variationPointsArray);
		List<PositiveVariationPointCandidateTemplates> positiveVariationPointCandidatesTemplates = 
				PositiveVariationPointCandidateSelection.createPositiveVariabilityCandidates(variationPointsArray);
		if (DebugInformation.PROCESS_STEP_INFORMATION) {
			System.out.println("Number of positive variation point candidates are: " + positiveVariationPointCandidatesTemplates.size());
		}
		ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration =
				evolutionCoreSettings.getActualScriptVariablesToSubstituteConfiguration();
		CodeContext codeContext = variationPointDivisioning.getCodeContextFromDivision();
		ParameterInjectionPositionObservation parameterInjectionPositionObservation = new ParameterInjectionPositionObservation();
		parameterInjectionPositionObservation.extractRelatedVariationPointData(codeContext, 
				positiveVariationPointCandidatesTemplates, actualScriptVariablesToSubstituteConfiguration);
		
		AssignedValueProcessForPositiveVariability assignedValuePositiveVariabilityProcess = new AssignedValueProcessForPositiveVariability(
				evolutionCoreSettings.getCallsFromPositiveVariationPointCreator(), 
				evolutionCoreSettings.getCallsInstantiationFromTemplate(),
				evolutionCoreSettings.getChosenValueAssignmentStrategiesForPositiveVariability(), 
				availableExportUnits,
				actualScriptVariablesToSubstituteConfiguration,
				parameterInjectionPositionObservation);
		
		
		if (DebugInformation.PROCESS_STEP_INFORMATION) {
			System.out.println("Number of positive variation point candidates are: " + positiveVariationPointCandidatesTemplates.size());
		}
		assignedValuePositiveVariabilityProcess.assignValuesProcess(positiveVariationPointCandidatesTemplates);
		NewContextsSynthesizer newContextsSynthesizer = new NewContextsSynthesizer(
				evolutionCoreSettings.getFeatureSelectionStrategy(), 
				evolutionCoreSettings.getFeatureConstructSelectionStrategy(), 
				evolutionCoreSettings.getCodeIncrementGranularityManagementStrategy(),
				evolutionCoreSettings.getSelectionOfConstructsSelectionStrategies(),
				derivationResourcesManager, evolutionConfiguration.getEvolvedContentName(), exportAssetPlanner);
		
		List<SynthesizedContent> synthesizedContents = newContextsSynthesizer.selectAndSynthetizeContexts(
				splAstTree, positiveVariationPointCandidatesTemplates, true);
		DefaultEvolutionCore.clearNegativeVariabilityAnnotationsAndMarkers(synthesizedContents);
		//this.clearPositiveVariabilityAnnotationsAndMarkers(splAstTree);
	}
	
	/**
	 * Persists processed and cleared AST with chosen negative variability variation points
	 * -AST is not processed or cleared from variability markers in this method
	 * 
	 * @param splAstTreeTargetPath - the target/destination path to persist processed and cleared AST in file
	 * @param splAstTree - the root of processed application/script AST
	 * @param chosenVariationPointsTargetPath - the target/destination path to persist AST in file
	 * @param chosenVariationPoints - the map of unique identifiers of chosen negative variability variation points to their candidate representation 
	 */
	public static void persistProcessedAndClearedAstWithChosenVariationPoints(
			String splAstTreeTargetPath, JSONObject splAstTree, String chosenVariationPointsTargetPath, 
			Map<String, NegativeVariationPointCandidate> chosenVariationPoints) {
		UpdatedTreePersistence.persistsAstInFile(splAstTreeTargetPath, splAstTree.toString());
		JSONArray chosenVariationPointsArray = new JSONArray();
		for (NegativeVariationPointCandidate chosenVariationPoint: chosenVariationPoints.values()) {
			chosenVariationPointsArray.add(chosenVariationPoint.getVariationPointData());
		}
		UpdatedTreePersistence.persistsAstInFile(chosenVariationPointsTargetPath, chosenVariationPointsArray.toString());
	}

	/**
	 * Removes all negative and positive variability annotations and markers
	 * 
	 * @param splAstTree - the root of processed application/script AST
	 */
	private static void clearUnusedAnnotationsAndMarkers(JSONObject splAstTree) {
		// only if no positive variability should be added
		AllMarkerRemover.removeAllPositiveVariabilityMarkers(splAstTree, splAstTree);
		if (VariationPointDivisionConfiguration.REMOVE_ALL_VARIABILITY_ANNOTATIOS_BEFORE_UPDATE) {
			AnnotationsFromCodeRemover.removeAllVariabilityAnnotations(splAstTree, splAstTree);
		}
	}
	
	/**
	 * Removes all negative variability annotations and markers (restrictively adapted only in form of decorators) from all provided processed 
	 * application/script AST that are loaded from the list of synthesized content instances
	 * 
	 * @param synthesizedContents - the list of the sources necessary to manage synthesis of the evolved part 
	 * - used to extract the reference to updated application/SPL AST from each instance
	 */
	private static void clearNegativeVariabilityAnnotationsAndMarkers(List<SynthesizedContent> synthesizedContents) {
		JSONObject processedSynthesizedAst;
		if (synthesizedContents != null) {
			for (SynthesizedContent processedSynthesizedContent: synthesizedContents) {
				processedSynthesizedAst = processedSynthesizedContent.getReferenceToProcessedAST();
				AnnotationsFromCodeRemover.removeAllVariabilityAnnotations(processedSynthesizedAst, processedSynthesizedAst);
			}
		}
	}
	
	/**
	 * Removes all negative variability annotations and markers (restrictively adapted only in form of decorators) from processed application/script AST
	 * 
	 * @param processedSynthesizedContent - the sources necessary to manage synthesis of the evolved part - used to extract the reference to updated application/SPL AST
	 */
	public static void clearNegativeVariabilityAnnotationsAndMarkers(SynthesizedContent processedSynthesizedContent) {
		JSONObject processedSynthesizedAst = processedSynthesizedContent.getReferenceToProcessedAST();
		AnnotationsFromCodeRemover.removeAllVariabilityAnnotations(processedSynthesizedAst, processedSynthesizedAst);
	}
	
	/**
	 * Removes all negative variability annotations and markers (restrictively adapted only in form of 
	 * decorators) from processed application/script AST
	 * 
	 * @param splAstTree - the root of processed application/script AST
	 */
	public static void clearNegativeVariabilityAnnotationsAndMarkers(JSONObject splAstTree) {
		if (VariationPointDivisionConfiguration.REMOVE_ALL_VARIABILITY_ANNOTATIOS_BEFORE_UPDATE) {
			AnnotationsFromCodeRemover.removeAllVariabilityAnnotations(splAstTree, splAstTree);
		}
	}
	
	/**
	 * Removes all positive variability annotations and markers
	 * 
	 * @param splAstTree - the root of processed application/script AST
	 */
	private static void clearPositiveVariabilityAnnotationsAndMarkers(JSONObject splAstTree) {
		AllMarkerRemover.removeAllPositiveVariabilityMarkers(splAstTree, splAstTree);
	}
}
