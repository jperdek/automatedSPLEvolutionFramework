package splEvolutionCore;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import codeContext.processors.NotFoundVariableDeclaration;
import codeContext.processors.export.exportedFileUnits.FileExportsUnits;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import evolutionSimulation.productAssetsInitialization.UnknownResourceToProcessException;
import evolutionSimulation.tests.AlreadyMappedVariationPointContentsInjection;
import evolutionSimulation.tests.EvolutionConfiguration;
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
 * Interface to prescribe format for various evolution strategies
 * 
 * @author Jakub Perdek
 *
 */
public interface EvolutionCoreStrategies {

	/**
	 * Prescribes format for various evolution strategies
	 * 
	 * @param splAstTree - the root of base AST
	 * @param variationPointsArray - the array of representatives from used variation points
	 * @param availableExportUnits - available exports
	 * @param evolutionCoreSettings - strategies instantiated for given evolution phase/phases
	 * @param evolutionConfiguration - the configuration for given evolution phase
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
	 */
	public void evolve(JSONObject splAstTree, JSONArray variationPointsArray, 
			FileExportsUnits availableExportUnits, EvolutionCoreSettings evolutionCoreSettings, 
			EvolutionConfiguration evolutionConfiguration) throws DuplicateCandidateIdentifier, 
			IOException, InterruptedException, InvalidSystemVariationPointMarkerException, 
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation, 
			AlreadyProvidedArgumentInConfigurationExpressionPlace, NotFoundVariableDeclaration, 
			MethodToEvaluateComplexityNotFoundException, DuplicatedContextIdentifier, 
			UnmappedContextException, DifferentlyAggregatedLocation, VariationPointPlaceInArrayNotFound,
			UnknownResourceToProcessException, AlreadyMappedVariationPointContentsInjection;
}
