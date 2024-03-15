package evolutionSimulation.orchestrationOfEvolutionIterations.test;

import java.io.IOException;

import codeContext.processors.NotFoundVariableDeclaration;
import dataRepresentationsExtensions.DataRepresentationsConfiguration;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import evolutionSimulation.EvolutionConfiguration;
import evolutionSimulation.iteration.AlreadyMappedVariationPointContentsInjection;
import evolutionSimulation.iteration.EvolutionIteration;
import evolutionSimulation.iteration.FiveEdgeEvolutionIterationTest;
import evolutionSimulation.iteration.FractalIterationInitializationCodeFragments;
import evolutionSimulation.iteration.EvolutionSamples;
import evolutionSimulation.productAssetsInitialization.CanvasBasedResource;
import evolutionSimulation.productAssetsInitialization.SharedConfiguration;
import evolutionSimulation.productAssetsInitialization.UnknownResourceToProcessException;
import positiveVariabilityManagement.UnmappedContextException;
import positiveVariabilityManagement.VariationPointPlaceInArrayNotFound;
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
	
	public EvolutionConfiguration prepareInitialIteration() {
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
	
	public static void main(String args[]) throws NotFoundVariableDeclaration, IOException, InterruptedException,
		InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint,
		DuplicatedAnnotation, DuplicateCandidateIdentifier, AlreadyProvidedArgumentInConfigurationExpressionPlace,
		MethodToEvaluateComplexityNotFoundException, DuplicatedContextIdentifier, UnmappedContextException,
		DifferentlyAggregatedLocation, VariationPointPlaceInArrayNotFound, UnknownResourceToProcessException, 
		AlreadyMappedVariationPointContentsInjection {
		CompletePositiveVariabilityFocusedEvolutionTest completeIyterativeDevelopment = new  CompletePositiveVariabilityFocusedEvolutionTest();
		EvolutionConfiguration evolutionConfiguration = completeIyterativeDevelopment.prepareInitialIteration();
		
		//evolutionConfiguration.setTemplateConfigurationPath(templateRelativePath);
		EvolutionIteration evolutionSimulationTest = new EvolutionIteration();
		evolutionSimulationTest.runEvolutioIteration(evolutionConfiguration);
	}
}
