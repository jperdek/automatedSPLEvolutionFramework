package splEvolutionCore;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import codeContext.processors.NotFoundVariableDeclaration;
import codeContext.processors.export.exportedFileUnits.FileExportsUnits;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import divisioner.VariationPointDivisioning;
import evolutionSimulation.EvolutionConfiguration;
import evolutionSimulation.iteration.AlreadyMappedVariationPointContentsInjection;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.ExportAssetPlanner;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.strategies.AssetMisuse;
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
 * Interface to prescribe format for various evolution strategies
 * 
 * @author Jakub Perdek
 *
 */
public interface EvolutionCoreStrategies {

	/**
	 * Prescribes format for various evolution strategies
	 * 
	 * @param splAstTree
	 * @param variationPointsArray
	 * @param availableExportUnits
	 * @param evolutionCoreSettings
	 * @param evolutionConfiguration
	 * @param variationPointDivisioning
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
			UnknownResourceToProcessException, AlreadyMappedVariationPointContentsInjection, AssetMisuse, AlreadyChosenVariationPointForInjectionException;
}
