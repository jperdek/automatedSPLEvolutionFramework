package evolutionSimulation.orchestrationOfEvolutionIterations.test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import codeContext.processors.NotFoundVariableDeclaration;
import dataRepresentationsExtensions.DataRepresentationsConfiguration;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import evolutionSimulation.EvolutionConfiguration;
import evolutionSimulation.iteration.AlreadyMappedVariationPointContentsInjection;
import evolutionSimulation.iteration.EvolutionIteration;
import evolutionSimulation.iteration.EvolutionIterationsPipeline;
import evolutionSimulation.iteration.FractalIterationInitializationCodeFragments;
import evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.RandomCandidateSelection;
import evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.SPLNextEvolutionIterationCandidateSelectionStrategy;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.AssetPlannerBaseStrategy;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.ExportAssetPlanner;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.strategies.AssetMisuse;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.strategies.PlanAssetOnce;
import evolutionSimulation.iteration.EvolutionSamples;
import evolutionSimulation.productAssetsInitialization.CanvasBasedResource;
import evolutionSimulation.productAssetsInitialization.SharedConfiguration;
import evolutionSimulation.productAssetsInitialization.UnknownResourceToProcessException;
import positiveVariabilityManagement.UnmappedContextException;
import positiveVariabilityManagement.VariationPointPlaceInArrayNotFound;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.AlreadyChosenVariationPointForInjectionException;
import positiveVariabilityManagement.entities.DuplicatedContextIdentifier;
import splEvolutionCore.candidateSelector.AlreadyProvidedArgumentInConfigurationExpressionPlace;
import splEvolutionCore.candidateSelector.DifferentlyAggregatedLocation;
import splEvolutionCore.candidateSelector.DuplicateCandidateIdentifier;
import splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment.MethodToEvaluateComplexityNotFoundException;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;


/**
 * Testing/Running evolution focused on iterative incremental development which is preferted over quality adaptations
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class CompletePositiveVariabilityFocusedEvolutionTest {
	
	/**
	 * Instantiates CompletePositiveVariabilityFocusedEvolutionTest
	 */
	public CompletePositiveVariabilityFocusedEvolutionTest() {
	}
	
	/**
	 * Prepares the configuration for the initial evolution phase
	 * 
	 * @return the configuration for the initial evolution phase
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	public EvolutionConfiguration prepareInitialConfiguration() throws IOException, TimeoutException {
		String inputFilePath = EvolutionSamples.FIVE_EDGE_INPUT_PATHS.get(0);
		String outputFilePath = SharedConfiguration.PATH_TO_EVOLUTION_DIRECTORY;
		String currentEvolvedScriptRelativePath = "/js/platnoJS.js"; // this should be changed for each evolved project
		//String templateRelativePath = "/index.html";
		String pathToScriptInputFilePath = inputFilePath + "/" + EvolutionSamples.PATH_TO_SCRIPT;
		
		DataRepresentationsConfiguration dataRepresentationsConfiguration = new DataRepresentationsConfiguration();
		EvolutionConfiguration evolutionConfiguration = new EvolutionConfiguration(FractalIterationInitializationCodeFragments.getInitialCodeFiveSide(), "FiveSide", 
			inputFilePath, outputFilePath, pathToScriptInputFilePath, 
			currentEvolvedScriptRelativePath, dataRepresentationsConfiguration);
		
		String canvasElementName = "game";
		String templatePath = null;
		evolutionConfiguration.addInitialResource(new CanvasBasedResource(canvasElementName, templatePath));
		return evolutionConfiguration;
	}
	
	/**
	 * Runs the evolution based on evolution pipeline - sample test
	 * 
	 * 
	 * @param args - no arguments used
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 * @throws DuplicateCandidateIdentifier
	 * @throws AlreadyProvidedArgumentInConfigurationExpressionPlace
	 * @throws MethodToEvaluateComplexityNotFoundException
	 * @throws DuplicatedContextIdentifier
	 * @throws UnmappedContextException
	 * @throws DifferentlyAggregatedLocation
	 * @throws VariationPointPlaceInArrayNotFound
	 * @throws UnknownResourceToProcessException
	 * @throws AlreadyMappedVariationPointContentsInjection
	 * @throws AssetMisuse 
	 * @throws AlreadyChosenVariationPointForInjectionException 
	 * @throws TimeoutException 
	 */
	public static void main(String args[]) throws NotFoundVariableDeclaration, IOException, InterruptedException,
		InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint,
		DuplicatedAnnotation, DuplicateCandidateIdentifier, AlreadyProvidedArgumentInConfigurationExpressionPlace,
		MethodToEvaluateComplexityNotFoundException, DuplicatedContextIdentifier, UnmappedContextException,
		DifferentlyAggregatedLocation, VariationPointPlaceInArrayNotFound, UnknownResourceToProcessException, 
		AlreadyMappedVariationPointContentsInjection, AssetMisuse, AlreadyChosenVariationPointForInjectionException, TimeoutException {
		
			CompletePositiveVariabilityFocusedEvolutionTest completeIyterativeDevelopment = new  CompletePositiveVariabilityFocusedEvolutionTest();
			EvolutionConfiguration evolutionConfiguration = completeIyterativeDevelopment.prepareInitialConfiguration();
			ExportAssetPlanner exportAssetPlanner = new AssetPlannerBaseStrategy(new PlanAssetOnce());

			// evolutionConfiguration.setTemplateConfigurationPath(templateRelativePath);
			SPLNextEvolutionIterationCandidateSelectionStrategy evolution2IterationStrategy = new RandomCandidateSelection();
			SPLNextEvolutionIterationCandidateSelectionStrategy evolution3IterationStrategy = new RandomCandidateSelection();
			SPLNextEvolutionIterationCandidateSelectionStrategy evolution4IterationStrategy = new RandomCandidateSelection();
			
			EvolutionIteration evolutionIteration1 = new EvolutionIteration();
			EvolutionIteration evolutionIteration2 = new EvolutionIteration(evolution2IterationStrategy);
			EvolutionIteration evolutionIteration3 = new EvolutionIteration(evolution3IterationStrategy);
			EvolutionIteration evolutionIteration4 = new EvolutionIteration(evolution4IterationStrategy);
			EvolutionIterationsPipeline evolutionIterationsPipeline = new EvolutionIterationsPipeline();
			evolutionIterationsPipeline.addEvolutionIterationToSequence(evolutionIteration1);
			evolutionIterationsPipeline.addEvolutionIterationToSequence(evolutionIteration2);
			evolutionIterationsPipeline.addEvolutionIterationToSequence(evolutionIteration3);
			evolutionIterationsPipeline.addEvolutionIterationToSequence(evolutionIteration4);
			evolutionIterationsPipeline.runEvolutionPipeline(evolutionConfiguration, exportAssetPlanner);
	}
}
