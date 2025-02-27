package evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies;

import java.util.List;


/**
 * Interface to allow the implementation of various strategies to select candidates for the next evolution iteration
 * 
 * 
 * @author Jakub Perdek
 *
 */
public interface SPLNextEvolutionIterationCandidateSelectionStrategy {

	/**
	 * Selects the candidates for the next evolution iteration phase from the previous evolution phase/or initial samples
	 * 
	 * @param numberOfNextEvolutionCandidates - the number of the next evolution candidates
	 * @param evolutionDirectory - the path to the SPL projects from the previous evolution iteration/or their initial versions
	 * @param listOfCandidateSPLFileNamesFromEachPopulationMember - the list of candidate SPL file names from each previous evolution iteration population member
	 * @param candidateSelectorAssessingAdapters - helper entities to collect variation point/SPL/product 
	 * related data and use it for selection making under current candidates
	 * @return the list of selected next evolution iteration candidates according to implemented strategy
	 */
	public List<String> selectNextEvolutionIterationCandidates(int numberOfNextEvolutionCandidates,
			String evolutionDirectory, List<String> listOfCandidateSPLFileNamesFromEachPopulationMember,
			CandidateSelectorAssessingAdapter... candidateSelectorAssessingAdapters);
	
	/**
	 * Returns associated candidate selector assessing adapters
	 * - the helper entities to collect variation point/SPL/product related data and use it for selection making under current candidates
	 * 
	 * @return the associated candidate selector assessing adapters - the helper entities to collect variation point/SPL/product 
	 * related data and use it for selection making under current candidates
	 */
	public List<CandidateSelectorAssessingAdapter> getAssociatedCandidateSelectorAssessingAdapters();
}
