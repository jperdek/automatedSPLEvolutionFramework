package evolutionSimulation.iteration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import codeContext.processors.NotFoundVariableDeclaration;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import evolutionSimulation.EvolutionConfiguration;
import evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.SPLProjectCandidateToPopulationOfEvolIterationSelector;
import evolutionSimulation.productAssetsInitialization.UnknownResourceToProcessException;
import positiveVariabilityManagement.UnmappedContextException;
import positiveVariabilityManagement.VariationPointPlaceInArrayNotFound;
import positiveVariabilityManagement.entities.DuplicatedContextIdentifier;
import splEvolutionCore.EvolutionCoreSettings;
import splEvolutionCore.candidateSelector.AlreadyProvidedArgumentInConfigurationExpressionPlace;
import splEvolutionCore.candidateSelector.DifferentlyAggregatedLocation;
import splEvolutionCore.candidateSelector.DuplicateCandidateIdentifier;
import splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment.MethodToEvaluateComplexityNotFoundException;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;


/**
 * Executes previously configured evolution iteration sequentially in pipeline
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class EvolutionIterationsPipeline {

	/**
	 * The sequence of evolution iterations that should be executed
	 */
	private List<EvolutionIteration> sequenceOfEvolutionIterations;
	
	/**
	 * Creates the evolution iteration pipeline
	 */
	public EvolutionIterationsPipeline() {
		this.sequenceOfEvolutionIterations = new ArrayList<EvolutionIteration>();
	}
	
	/**
	 * Creates the evolution iteration pipeline with specified configuration for each evolution iteration
	 * 
	 * @param sequenceOfEvolutionIterations - the sequence of evolution iterations that should be executed
	 */
	public EvolutionIterationsPipeline(List<EvolutionIteration> sequenceOfEvolutionIterations) {
		this.sequenceOfEvolutionIterations = sequenceOfEvolutionIterations;
	}
	
	/**
	 * Adds the configuration for the new evolution iteration (phase) to the end of the sequence of evolution iterations that should be executed
	 * 
	 * @param newIteration - the configuration for the new evolution iteration (phase)
	 */
	public void addEvolutionIterationToSequence(EvolutionIteration newIteration) {
		this.sequenceOfEvolutionIterations.add(newIteration);
	}
	
	/**
	 * Adds the configuration for the new evolution iteration (phase) to the specified position in the sequence of evolution iterations that should be executed
	 * 
	 * @param newIteration - the configuration for the new evolution iteration (phase)
	 * @param position - the specified position in the sequence of evolution iterations where new configuration should be inserted
	 */
	public void addEvolutionIterationToPositionInSequence(EvolutionIteration newIteration, int position) {
		this.sequenceOfEvolutionIterations.add(position, newIteration);
	}

	/**
	 * Runs the evolution pipeline (for particular evolution or sub-evolution and according to the specified configuration)
	 * 
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
	public void runEvolutionPipeline(EvolutionConfiguration evolutionConfiguration) throws NotFoundVariableDeclaration, IOException, InterruptedException, InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation, DuplicateCandidateIdentifier, AlreadyProvidedArgumentInConfigurationExpressionPlace, MethodToEvaluateComplexityNotFoundException, DuplicatedContextIdentifier, UnmappedContextException, DifferentlyAggregatedLocation, VariationPointPlaceInArrayNotFound, UnknownResourceToProcessException, AlreadyMappedVariationPointContentsInjection {
		Iterator<EvolutionIteration> evolutionIterationIterator = this.sequenceOfEvolutionIterations.iterator();
		String pathToEvolvedSPLProjectsDirectory = evolutionConfiguration.getPathToEvolvedSPLProjectDirectory();
		
		SPLProjectCandidateToPopulationOfEvolIterationSelector candidateForPopulationSelector = null;
		EvolutionIteration evolutionIteration;
		EvolutionCoreSettings evolutionCoreSettings;
		List<String> pathsToScriptInputFilePath;

		while(evolutionIterationIterator.hasNext()) {
			evolutionIteration = evolutionIterationIterator.next();
			evolutionCoreSettings = evolutionIteration.getAssociatedEvolutionCoreSettings();
			
			if (pathToEvolvedSPLProjectsDirectory == null || pathToEvolvedSPLProjectsDirectory.equals("")) {
				evolutionIteration.runEvolutioIteration(evolutionConfiguration);
			
			} else {
				candidateForPopulationSelector = new SPLProjectCandidateToPopulationOfEvolIterationSelector(candidateForPopulationSelector);
				pathsToScriptInputFilePath = candidateForPopulationSelector.getPathsToEachSPLProjectCandidateFromPopulation(pathToEvolvedSPLProjectsDirectory, null);
				for (String pathToScriptInputFilePath: pathsToScriptInputFilePath) {
					evolutionIteration.runEvolutioIteration(pathToScriptInputFilePath, evolutionConfiguration, evolutionCoreSettings);
				}
			}
			
			evolutionConfiguration.setPathToEvolvedSPLProjectDirectoryFromLatestEvolution();
			if (evolutionConfiguration.shouldTerminateEvolution()) {
				break;
			}
		}
	}
}
