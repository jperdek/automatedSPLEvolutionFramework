package evolutionSimulation.iteration;

import codeContext.processors.NotFoundVariableDeclaration;
import dataRepresentationsExtensions.DataRepresentationsConfiguration;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import evolutionSimulation.EvolutionConfiguration;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.AssetPlannerBaseStrategy;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.ExportAssetPlanner;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.strategies.AssetMisuse;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.strategies.NonRestrictiveAssetPlanning;
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
import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * Simulates default evolution on chosen fractal with associated extensions
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class FiveEdgeEvolutionIterationTest extends EvolutionIteration {


	/**
	 * Creates the instance to simulate the evolution process
	 */
	public FiveEdgeEvolutionIterationTest() {
		super();
	}
	
	/**
	 * Launches the example with evolution of the Five Side fractal - with optional inclusion of Krishna anklet
	 *  
	 * @param args - arguments are not used
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
		String inputFilePath = EvolutionSamples.FIVE_EDGE_INPUT_PATHS.get(0);
		String outputFilePath = SharedConfiguration.PATH_TO_EVOLUTION_DIRECTORY;
		String currentEvolvedScriptRelativePath = "/js/platnoJS.js"; // this should be changed for each evolved project
		//String templateRelativePath = "/index.html";
		String pathToScriptInputFilePath = inputFilePath + "/" + EvolutionSamples.PATH_TO_SCRIPT;
		
		FiveEdgeEvolutionIterationTest evolutionSimulationTest = new FiveEdgeEvolutionIterationTest();
		DataRepresentationsConfiguration dataRepresentationsConfiguration = new DataRepresentationsConfiguration();
		EvolutionConfiguration evolutionConfiguration = new EvolutionConfiguration(FractalIterationInitializationCodeFragments.getInitialCodeFiveSide(), "FiveSide", 
				inputFilePath, outputFilePath, pathToScriptInputFilePath, 
				currentEvolvedScriptRelativePath, dataRepresentationsConfiguration);

		String canvasElementName = "game";
		String templatePath = null;
		evolutionConfiguration.addInitialResource(new CanvasBasedResource(canvasElementName, templatePath));
		//evolutionConfiguration.setTemplateConfigurationPath(templateRelativePath);
		ExportAssetPlanner exportAssetPlanner = new AssetPlannerBaseStrategy(new  NonRestrictiveAssetPlanning());
		evolutionSimulationTest.runEvolutioIteration(evolutionConfiguration, exportAssetPlanner);
	}
}
