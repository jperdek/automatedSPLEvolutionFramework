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
import evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.SPLNextEvolutionIterationCandidateSelectionStrategy;
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
	 * Associated configuration with particular sub-evolution process to which this evolution iteration belongs to
	 */
	private EvolutionConfiguration associatedEvolutionConfiguration;

	/**
	 * Associated configuration with particular evolution core settings
	 */
	private EvolutionCoreSettings associatedEvolutionCoreSettings;
	
	/**
	 * Strategy to select candidates from the previous iteration to the new one
	 */
	private SPLNextEvolutionIterationCandidateSelectionStrategy candidatesForEvolutionIterationSelector;
	
	
	/**
	 * Instantiates the functionality to perform/manage evolution iteration according to provided configuration
	 * 
	 */
	public EvolutionIteration() { this(null, null, null); }
	
	/**
	 * Instantiates the functionality to perform/manage evolution iteration according to provided configuration
	 * 
	 * @param candidatesForEvolutionSelector - strategy which should be applied for selecting the candidates from the previous evolution
	 */
	public EvolutionIteration(SPLNextEvolutionIterationCandidateSelectionStrategy candidatesForEvolutionIterationSelector) {
		this(candidatesForEvolutionIterationSelector, null, null);
	}
	
	/**
	 * Instantiates the functionality to perform/manage evolution iteration according to provided configuration
	 * 
	 * @param candidatesForEvolutionSelector - strategy which should be applied for selecting the candidates from the previous evolution
	 * @param associatedEvolutionConfiguration - associated configuration for whole evolution process if available
	 * @param associatedEvolutionCoreSettings - the configuration-settings for the decision making necessary in the evolution core processes 
	 * (variation points selection, variability constructs selection, ...)
	 */
	public EvolutionIteration(SPLNextEvolutionIterationCandidateSelectionStrategy candidatesForEvolutionIterationSelector, 
			EvolutionConfiguration associatedEvolutionConfiguration, EvolutionCoreSettings associatedEvolutionCoreSettings) {
		this.candidatesForEvolutionIterationSelector = candidatesForEvolutionIterationSelector;
		this.associatedEvolutionConfiguration = associatedEvolutionConfiguration;
		this.associatedEvolutionCoreSettings = associatedEvolutionCoreSettings;
	}
	
	/**
	 * Returns the associated candidate selection mechanism from the precious evolution iteration
	 * 
	 * @return the associated candidate selection mechanism from the precious evolution iteration
	 */
	public SPLNextEvolutionIterationCandidateSelectionStrategy getEvolutionIterationCandidateSelectionMechanism() {
		return this.candidatesForEvolutionIterationSelector; 
	}
	
	/**
	 * Returns the associated evolution core settings
	 * 
	 * @return the associated evolution core settings
	 */
	public EvolutionCoreSettings getAssociatedEvolutionCoreSettings() { return this.associatedEvolutionCoreSettings; }
	
	/**
	 * Returns the associated evolution configuration
	 * 
	 * @return the associated evolution configuration
	 */
	public EvolutionConfiguration getAssociatedEvolutionConfiguration() { return this.associatedEvolutionConfiguration; }
	
	/**
	 * Performing one evolution iteration phase according to provided configuration
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
		if (evolutionConfiguration == null) { evolutionConfiguration = this.associatedEvolutionConfiguration; }
		
		String pathToScriptInputFilePath = evolutionConfiguration.getPathToScriptInputFile();
		EvolutionCoreStrategies evolutionCoreStrategy;
		EvolutionCoreSettings evolutionCoreSettings = EvolutionConfigurations.getMaximalSemanticOrientedConfiguration();
		
		this.runEvolutioIteration(pathToScriptInputFilePath, evolutionConfiguration, evolutionCoreSettings);
	}
	
	/**
	 * Performing one evolution iteration phase according to provided configuration
	 * -allowing use specific settings for this evolution iteration, including core strategies
	 * -if these settings are not provided then class instances are used
	 * 
	 * @param evolutionCoreSettings
	 * @param evolutionCoreSettings
	 * @param evolutionConfiguration
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
	public void runEvolutioIteration(String pathToScriptInputFilePath, EvolutionConfiguration evolutionConfiguration, EvolutionCoreSettings evolutionCoreSettings) throws NotFoundVariableDeclaration, IOException, 
				InterruptedException, InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint,
				DuplicatedAnnotation, DuplicateCandidateIdentifier, AlreadyProvidedArgumentInConfigurationExpressionPlace, 
				MethodToEvaluateComplexityNotFoundException, DuplicatedContextIdentifier, UnmappedContextException, 
				DifferentlyAggregatedLocation, VariationPointPlaceInArrayNotFound, UnknownResourceToProcessException, 
				AlreadyMappedVariationPointContentsInjection {
		if (evolutionConfiguration == null) { evolutionConfiguration = this.associatedEvolutionConfiguration; }
		if (evolutionCoreSettings == null) { evolutionCoreSettings = this.associatedEvolutionCoreSettings; }
		
		evolutionConfiguration.setPathToScriptInputFile(pathToScriptInputFilePath);
		EvolutionCoreStrategies evolutionCoreStrategy;
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
