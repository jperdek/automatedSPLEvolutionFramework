package evolutionSimulation.iteration;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import codeConstructsEvaluation.transformation.ASTConverterClient;
import codeConstructsEvaluation.transformation.PostRequester;
import codeContext.processors.NotFoundVariableDeclaration;
import codeContext.processors.export.exportedFileUnits.FileExportUnitsToMerge;
import codeContext.processors.export.exportedFileUnits.FileExportsUnits;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import divisioner.VariationPointDivisionConfiguration;
import evolutionSimulation.EvolutionConfiguration;
import evolutionSimulation.productAssetsInitialization.UnknownResourceToProcessException;
import positiveVariabilityManagement.UnmappedContextException;
import positiveVariabilityManagement.VariationPointPlaceInArrayNotFound;
import positiveVariabilityManagement.entities.DuplicatedContextIdentifier;
import splEvolutionCore.EvolutionConfigurations;
import splEvolutionCore.EvolutionCoreSettings;
import splEvolutionCore.EvolutionCoreStrategies;
import splEvolutionCore.candidateSelector.AlreadyProvidedArgumentInConfigurationExpressionPlace;
import splEvolutionCore.candidateSelector.DifferentlyAggregatedLocation;
import splEvolutionCore.candidateSelector.DuplicateCandidateIdentifier;
import splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment.MethodToEvaluateComplexityNotFoundException;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;


/**
 * The functionality to perform/manage evolution iteration according to provided configuration
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class EvolutionIteration {

	/**
	 * Instantiates the functionality to perform/manage evolution iteration according to provided configuration
	 * 
	 */
	public EvolutionIteration() {	
	}
	
	/**
	 * Performing one evolution iteration pahase according to provided configuration
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
	public void runEvolutioIteration(EvolutionConfiguration evolutionConfiguration) throws NotFoundVariableDeclaration, IOException, 
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
}
