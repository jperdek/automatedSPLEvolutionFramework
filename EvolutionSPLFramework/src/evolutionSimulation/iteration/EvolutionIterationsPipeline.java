package evolutionSimulation.iteration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import codeContext.processors.NotFoundVariableDeclaration;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import evolutionSimulation.EvolutionConfiguration;
import evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.SPLNextEvolutionIterationCandidateSelectionStrategy;
import evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.SPLProjectCandidateToPopulationOfEvolIterationSelector;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.ExportAssetPlanner;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.strategies.AssetMisuse;
import evolutionSimulation.productAssetsInitialization.UnknownResourceToProcessException;
import positiveVariabilityManagement.UnmappedContextException;
import positiveVariabilityManagement.VariationPointPlaceInArrayNotFound;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.AlreadyChosenVariationPointForInjectionException;
import positiveVariabilityManagement.entities.DuplicatedContextIdentifier;
import splEvolutionCore.DebugInformation;
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
		if (!this.sequenceOfEvolutionIterations.isEmpty()) {
			EvolutionIteration lastIteration = this.sequenceOfEvolutionIterations.get(this.sequenceOfEvolutionIterations.size() - 1);
			EvolutionCoreSettings lastEvolutionCoreSettings = lastIteration.getAssociatedEvolutionCoreSettings();
			newIteration.setEvolutionCoreSettings(lastEvolutionCoreSettings);
		}
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
	 * - manages evolution process that can terminate if termination conditions are fulfilled or all iterations are executed
	 * 
	 * @param evolutionConfiguration
	 * @param exportAssetPlanner
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
	 */
	public void runEvolutionPipeline(EvolutionConfiguration evolutionConfiguration, 
			ExportAssetPlanner exportAssetPlanner) throws NotFoundVariableDeclaration, 
				IOException, InterruptedException, InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint, 
				DuplicatedAnnotation, DuplicateCandidateIdentifier, AlreadyProvidedArgumentInConfigurationExpressionPlace, 
				MethodToEvaluateComplexityNotFoundException, DuplicatedContextIdentifier, UnmappedContextException,
				DifferentlyAggregatedLocation, VariationPointPlaceInArrayNotFound, UnknownResourceToProcessException, 
				AlreadyMappedVariationPointContentsInjection, AssetMisuse, AlreadyChosenVariationPointForInjectionException {
		Iterator<EvolutionIteration> evolutionIterationIterator = this.sequenceOfEvolutionIterations.iterator();
		String pathToEvolvedSPLProjectsDirectory = evolutionConfiguration.getPathToEvolvedSPLProjectDirectory();
		
		SPLNextEvolutionIterationCandidateSelectionStrategy strategySPLNextEvolutionIterationCandidateSelection;
		SPLProjectCandidateToPopulationOfEvolIterationSelector candidateForPopulationSelector = null;
		EvolutionIteration evolutionIteration;
		EvolutionCoreSettings evolutionCoreSettings = null;
		String pathToScriptInputFilePath;
		List<String> inputPaths;
		int numberEvolvedCandidatesFromLastIteration;

		EvolutionConfiguration customizedEvolutionConfiguration = evolutionConfiguration;
		while(evolutionIterationIterator.hasNext()) {
			pathToEvolvedSPLProjectsDirectory = customizedEvolutionConfiguration.getPathToEvolvedSPLProjectDirectory();
			
			evolutionIteration = evolutionIterationIterator.next();
			customizedEvolutionConfiguration = evolutionIteration.getAssociatedEvolutionConfiguration();
	
			if (customizedEvolutionConfiguration == null) { customizedEvolutionConfiguration = evolutionConfiguration; }
			if (DebugInformation.PROCESS_STEP_INFORMATION) { System.out.println("Performing evolution iteration number: " + customizedEvolutionConfiguration.getIteration()); }
			
			customizedEvolutionConfiguration.updateIteration(evolutionConfiguration);
			customizedEvolutionConfiguration.updatePathToEvolvedSPLProjectDirectory();
			numberEvolvedCandidatesFromLastIteration = customizedEvolutionConfiguration.getNumberOfEvolvedMembersInPopulation();
			if (evolutionIteration.getAssociatedEvolutionCoreSettings() != null) {
				evolutionCoreSettings = evolutionIteration.getAssociatedEvolutionCoreSettings();
			}
			
			String pathForNextIteration = null;
			if (pathToEvolvedSPLProjectsDirectory == null || pathToEvolvedSPLProjectsDirectory.equals("")) {
				System.out.println("NO Path to evolved directory!");
				if (DebugInformation.PROCESS_STEP_INFORMATION) { customizedEvolutionConfiguration.printCurrentConfiguration(); }
				evolutionIteration.runEvolutioIteration(evolutionConfiguration, exportAssetPlanner);
				// introduces the core settings from this iteration
				evolutionCoreSettings = evolutionIteration.getAssociatedEvolutionCoreSettings();
			} else {
				System.out.println("Begin with path to evolved directory: " + pathToEvolvedSPLProjectsDirectory);
				strategySPLNextEvolutionIterationCandidateSelection = 
						evolutionIteration.getEvolutionIterationCandidateSelectionMechanism();
				
				System.out.println("CANDIDATE POPULATION SELECTION ");
				candidateForPopulationSelector = new SPLProjectCandidateToPopulationOfEvolIterationSelector(candidateForPopulationSelector);
				inputPaths = candidateForPopulationSelector.getPathsToEachSPLProjectCandidateFromPopulation(
						numberEvolvedCandidatesFromLastIteration, 
						pathToEvolvedSPLProjectsDirectory, strategySPLNextEvolutionIterationCandidateSelection, evolutionConfiguration);
				System.out.println("EVOLUTION ITERATION: " + customizedEvolutionConfiguration.getIteration() + " Input Paths number: " + inputPaths.size());
				for (String inputPath: inputPaths) {
					pathToScriptInputFilePath = inputPath + evolutionConfiguration.getCurrentEvolvedScriptRelativePath();
					System.out.println("Input Path: " + inputPath);
					System.out.println("Evolved Path: " + pathToScriptInputFilePath);
					
					if (pathToScriptInputFilePath.contains("_XXX__VariationPointData.json")) { continue; }
					if (DebugInformation.PROCESS_STEP_INFORMATION) { customizedEvolutionConfiguration.printCurrentConfiguration(); }
					
					evolutionIteration.runEvolutioIteration(pathToScriptInputFilePath, 
							customizedEvolutionConfiguration, evolutionCoreSettings, exportAssetPlanner);
					//ADAPTATIONS ARE MISSING!!!!
					//evolutionCoreSettings = evolutionIteration.getAssociatedEvolutionCoreSettings();
					pathForNextIteration = pathToScriptInputFilePath.substring(pathToScriptInputFilePath.lastIndexOf("/") + 1);
					pathForNextIteration = pathForNextIteration.substring(pathForNextIteration.lastIndexOf("/") + 1);
				}
			}
			
			// switch to the next iteration - changing paths
			if (evolutionConfiguration.shouldTerminateEvolution()) { 
				if (DebugInformation.PROCESS_STEP_INFORMATION) { System.out.println("Terminating SPL (inner-)evolution..."); }
				break;
			}
			
			if (DebugInformation.PROCESS_STEP_INFORMATION) { System.out.println("Terminating evolution iteration and beginning with iteration number: " + customizedEvolutionConfiguration.getIteration()); }
			
			customizedEvolutionConfiguration.incrementIteration();
			evolutionConfiguration.updateIteration(customizedEvolutionConfiguration);
			evolutionConfiguration.setPathToEvolvedSPLProjectDirectoryFromLatestEvolution(customizedEvolutionConfiguration, pathForNextIteration);
			System.out.println("PATH CLOSED");
			customizedEvolutionConfiguration = evolutionConfiguration;
		}
	}
}
