package evolutionSimulation.tests;


import codeConstructsEvaluation.transformation.ASTConverterClient;
import codeConstructsEvaluation.transformation.PostRequester;
import codeContext.processors.NotFoundVariableDeclaration;
import codeContext.processors.export.exportedFileUnits.FileExportUnitsToMerge;
import codeContext.processors.export.exportedFileUnits.FileExportsUnits;
import dataRepresentationsExtensions.DataRepresentationsConfiguration;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import divisioner.VariationPointDivisionConfiguration;
import evolutionSimulation.productAssetsInitialization.CanvasBasedResource;
import evolutionSimulation.productAssetsInitialization.UnknownResourceToProcessException;
import positiveVariabilityManagement.UnmappedContextException;
import positiveVariabilityManagement.VariationPointPlaceInArrayNotFound;
import positiveVariabilityManagement.entities.DuplicatedContextIdentifier;
import splEvolutionCore.CanvasBasedApplicationConfiguration;
import splEvolutionCore.EvolutionConfigurations;
import splEvolutionCore.EvolutionCoreSettings;
import splEvolutionCore.EvolutionCoreStrategies;
import splEvolutionCore.candidateSelector.AlreadyProvidedArgumentInConfigurationExpressionPlace;
import splEvolutionCore.candidateSelector.DifferentlyAggregatedLocation;
import splEvolutionCore.candidateSelector.DuplicateCandidateIdentifier;
import splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment.MethodToEvaluateComplexityNotFoundException;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.IOException;


/**
 * Simulates default evolution on chosen fractal with associated extensions
 * 
 * @author Jakub Perdek
 *
 */
public class EvolutionSimulationTest {


	/**
	 * Creates the instance to simulate the evolution process
	 */
	public EvolutionSimulationTest() {		
	}
	
	/**
	 * Evolution iteration to evolve Five Side fractal;
	 * 
	 * 
	 * @param evolutionConfiguration - the configuration to drive evolution process, especially executed evolution iterations
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
	 */
	public void evolveFiveEdgeBased(EvolutionConfiguration evolutionConfiguration) throws NotFoundVariableDeclaration, IOException, 
				InterruptedException, InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint,
				DuplicatedAnnotation, DuplicateCandidateIdentifier, AlreadyProvidedArgumentInConfigurationExpressionPlace, 
				MethodToEvaluateComplexityNotFoundException, DuplicatedContextIdentifier, UnmappedContextException, 
				DifferentlyAggregatedLocation, VariationPointPlaceInArrayNotFound, UnknownResourceToProcessException, 
				AlreadyMappedVariationPointContentsInjection {
		String pathToScriptInputFilePath = evolutionConfiguration.getPathToScriptInputFile();
		EvolutionCoreStrategies evolutionCoreStrategy;
		EvolutionCoreSettings evolutionCoreSettings = EvolutionConfigurations.getMaximalSemanticOrientedConfiguration();
		
		VariationPointDivisionConfiguration variationPointDivisionConfiguration = evolutionCoreSettings.getVariationPointDivisionConfiguration();
		//variationPointDivisionConfiguration.divisionAndGetHighlightedAst(inputFilePath, fileOutputAstPath, fileOutputVariationPointsPath);
		String fileContent = PostRequester.loadFileContent(pathToScriptInputFilePath);
		WrappedTypeScriptContentInVariable wrappedTypeScriptContentInVariable = new WrappedTypeScriptContentInVariable(fileContent);

		JSONObject astTreeRoot = ASTConverterClient.convertFromCodeToASTJSON(wrappedTypeScriptContentInVariable.getScript());
		JSONObject highlightedAst = variationPointDivisionConfiguration.divisionAndGetHighlightedAst(astTreeRoot, pathToScriptInputFilePath);
		
		JSONArray harvestedVariationPoints = variationPointDivisionConfiguration.getVariationPointsData(highlightedAst);

		FileExportsUnits availableExportUnits = FileExportUnitsToMerge.prepareDefaultFileExportUnitsToMerge(
				evolutionConfiguration.getSelectedExportedContentPaths(),
				evolutionConfiguration.getPathExtensionToGetScript());
		availableExportUnits.printContentOfExportUnits();
		
		evolutionCoreStrategy = evolutionCoreSettings.getEvolutionCoreStrategy();
		evolutionCoreStrategy.evolve(highlightedAst, harvestedVariationPoints, 
				availableExportUnits, evolutionCoreSettings, evolutionConfiguration);
	}
	
	/**
	 * The initialization code of HTML canvas with possibility to wrap it under Canto JS
	 *  
	 * @return the initialization code of HTML canvas with possibility to wrap it under Canto JS
	 */
	private static String getCanvasCode() {
		String canvasInitialization = "var canvas = document.getElementById(\"game\");\r\n"
				+   "		var context = canvas.getContext('2d');\r\n";
		if (CanvasBasedApplicationConfiguration.WRAP_WITH_CANTO_JS) {
			return CanvasBasedApplicationConfiguration.wrapWithCantoJS(canvasInitialization);
		}
		return canvasInitialization;
	}

	/**
	 * The initialization code to call anklet fractal rendering
	 * -includes the initialization of canvas functionality
	 * 
	 * @return the initialization code to call anklet fractal rendering
	 */
	private static String getInitialCodeAnklet() {
		return getCanvasCode()
				+ "		const circleRadius = 5;\r\n"
				+ "		const numberIterations = 5;\r\n"
				+ "		const lineLength = 500;\r\n"
				+ "		const thickness = 2;\r\n"
				+ "		const squareSideLength = lineLength / Math.pow(2, numberIterations);\r\n"
				+ "		const ankletInfo = new AnkletInfo(lineLength, circleRadius, thickness, 2*squareSideLength);\r\n"
				+ "		drawAnkletMain(canvas, context, circleRadius, numberIterations, lineLength, thickness, squareSideLength, ankletInfo);";
	}

	/**
	 * The initialization code to call Five side fractal rendering
	 * -includes the initialization of canvas functionality
	 * 
	 * @return the initialization code to call Five side fractal rendering
	 * -includes the initialization of canvas functionality
	 */
	private static String getInitialCodeFiveSide() {
		return getCanvasCode()
				+ "		const circleRadius = 5;\r\n"
				+ "		const numberIterations = 5;\r\n"
				+ "		const lineLength = 500;\r\n"
				+ "		const thickness = 2;\r\n"
				+ "		const squareSideLength = lineLength / Math.pow(2, numberIterations);\r\n"
				+ "		drawAnkletModMain(context, circleRadius, numberIterations, thickness);\r\n";
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
	 */
	public static void main(String args[]) throws NotFoundVariableDeclaration, IOException, InterruptedException,
				InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint,
				DuplicatedAnnotation, DuplicateCandidateIdentifier, AlreadyProvidedArgumentInConfigurationExpressionPlace,
				MethodToEvaluateComplexityNotFoundException, DuplicatedContextIdentifier, UnmappedContextException,
				DifferentlyAggregatedLocation, VariationPointPlaceInArrayNotFound, UnknownResourceToProcessException, 
				AlreadyMappedVariationPointContentsInjection {
		String inputFilePath = EvolutionSamples.FIVE_EDGE_INPUT_PATHS.get(0);
		String outputFilePath = "E://aspects/spaProductLine/VariationPointDivisioner/evolutionDirectory";
		String currentEvolvedScriptRelativePath = "/js/platnoJS.js"; // this should be changed for each evolved project
		//String templateRelativePath = "/index.html";
		String pathToScriptInputFilePath = inputFilePath + "/" + EvolutionSamples.PATH_TO_SCRIPT;
		
		EvolutionSimulationTest evolutionSimulationTest = new EvolutionSimulationTest();
		DataRepresentationsConfiguration dataRepresentationsConfiguration = new DataRepresentationsConfiguration();
		EvolutionConfiguration evolutionConfiguration = new EvolutionConfiguration(getInitialCodeFiveSide(), "FiveSide", 
				inputFilePath, outputFilePath, pathToScriptInputFilePath, 
				currentEvolvedScriptRelativePath, dataRepresentationsConfiguration);

		String canvasElementName = "game";
		String templatePath = null;
		evolutionConfiguration.addInitialResource(new CanvasBasedResource(canvasElementName, templatePath));
		//evolutionConfiguration.setTemplateConfigurationPath(templateRelativePath);
		evolutionSimulationTest.evolveFiveEdgeBased(evolutionConfiguration);
	}
}
