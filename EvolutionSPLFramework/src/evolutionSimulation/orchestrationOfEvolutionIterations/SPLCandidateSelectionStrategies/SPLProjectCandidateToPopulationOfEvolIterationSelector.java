package evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateLoadingMechanism;
import evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.imageContentQualityAssignment.VariationPointsDataAggregations;


/**
 * Responsible to manage overall candidate selection process according to configuration for particular (sub-)evolution iteration
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class SPLProjectCandidateToPopulationOfEvolIterationSelector {

	/**
	 * Instance to load variation points data from SPL candidates 
	 */
	private SPLCandidateLoadingMechanism mechanismForSPLCandidateLoading;
	
	/**
	 * Instance managing information in aggregations of variation point views for decision-making performed by particular SPL candidate selector strategy
	 */
	private VariationPointsDataAggregations variationPointsDataAggregations;
	
	/**
	 * The queue of previously used candidate selectors
	 */
	private PreviousProjectCandidatesSelectorsQueue previousProjectCandidatesSelectorsQueue = null;
	
	/**
	 * Instantiates and initializes entities for SPL project candidate selection to produce population for actually processed/initialized iteration
	 */
	public SPLProjectCandidateToPopulationOfEvolIterationSelector() {	
		this(null);
	}
	
	/**
	 * Initializes the SPL project candidate selector instance for the next SPL (sub-)evolution iteration
	 * 
	 * @param previousCandidateToPopulationSelector - the previously used SPL project candidate selector instance
	 */
	public SPLProjectCandidateToPopulationOfEvolIterationSelector(
			SPLProjectCandidateToPopulationOfEvolIterationSelector previousCandidateToPopulationSelector) {	
		this.mechanismForSPLCandidateLoading = new SPLCandidateLoadingMechanism();
		this.variationPointsDataAggregations = new VariationPointsDataAggregations();
		this.previousProjectCandidatesSelectorsQueue = previousCandidateToPopulationSelector.getPreviousProjectCandidatesSelectorsQueueForNewSelectorInstance();
	}
	
	/**
	 * Returns the previous project candidate selector queue with direct insertion of caller instance
	 *  
	 * @return the previous project candidate selector queue with direct insertion of caller instance
	 */
	private PreviousProjectCandidatesSelectorsQueue getPreviousProjectCandidatesSelectorsQueueForNewSelectorInstance() {
		if (this.previousProjectCandidatesSelectorsQueue == null) {
			return new PreviousProjectCandidatesSelectorsQueue(null);
		}
		this.previousProjectCandidatesSelectorsQueue.addProjectToQueueOfPreviousProjects(this);
		return this.previousProjectCandidatesSelectorsQueue;
	}
	
	/**
	 * Selects the most suitable candidates according to the settings in configuration 
	 * and returns the list of paths to their project directories in directory of the previously used sub-evolution iteration
	 * 
	 * @param previousEvolutionDirectory - previously used evolution directory
	 * @param evolIterationCandidateSelectionStrategy - the strategy to select the most suitable candidates according to the settings in configuration 
	 * @return the list of paths to project directories of the most suitable candidates selected according to 
	 * the settings in configuration which are located in directory of the previously used sub-evolution iteration
	 * 
	 * @throws IOException
	 */
	public List<String> getPathsToEachSPLProjectCandidateFromPopulation(int numberOfNextEvolutionCandidates, 
			String previousEvolutionDirectory, SPLNextEvolutionIterationCandidateSelectionStrategy
			evolIterationCandidateSelectionStrategy) throws IOException {
		
		mechanismForSPLCandidateLoading.loadAndParseSPLCandidates(previousEvolutionDirectory, variationPointsDataAggregations);
		List<String> listOfCandidateSPLFileNamesFromEachPopulationMember = this.mechanismForSPLCandidateLoading.getListOfCandidateSPLFileNamesFromEachPopulationMember();
		List<String> selectedCandidates = 
				evolIterationCandidateSelectionStrategy.selectNextEvolutionIterationCandidates(numberOfNextEvolutionCandidates,
						previousEvolutionDirectory, listOfCandidateSPLFileNamesFromEachPopulationMember, 
						variationPointsDataAggregations);

		for (int i=0; i<selectedCandidates.size(); i++) {
			selectedCandidates.set(i, mechanismForSPLCandidateLoading.getCandidatePath(selectedCandidates.get(i)));
		}
		return selectedCandidates;
	}
}
