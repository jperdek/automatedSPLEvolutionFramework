package evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies;

import java.util.List;


/**
 * Selects the candidate SPLs for the next evolution iteration process according to the quality of taken images
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class ImageQualitySPLCandidateSelection implements SPLNextEvolutionIterationCandidateSelectionStrategy {

	/**
	 * Creates the instance to manage candidates based on performed image screenshot quality
	 * 
	 */
	public ImageQualitySPLCandidateSelection() {
	}


	/**
	 * Randomly selects the candidates for the next evolution iteration phase from the previous evolution phase/or initial samples
	 * 
	 * @param numberOfNextEvolutionCandidates - the number of the next evolution candidates
	 * @param evolutionDirectory - the path to the SPL projects from the previous evolution iteration/or their initial versions
	 * @param listOfCandidateSPLFileNamesFromEachPopulationMember - the list of candidate SPL file names from each previous evolution iteration population member
	 * @param candidateSelectorAssessingAdapters - helper entities to collect variation point/SPL/product 
	 * related data and use it for selection making under current candidates
	 * @return the list of selected next evolution iteration candidates according to implemented strategy
	 */
	@Override
	public List<CandidateSelectorAssessingAdapter> getAssociatedCandidateSelectorAssessingAdapters() {
		return null;
	}


	/**
	 * Returns associated candidate selector assessing adapters
	 * - the helper entities to collect variation point/SPL/product related data and use it for selection making under current candidates
	 * 
	 * @return the associated candidate selector assessing adapters - the helper entities to collect variation point/SPL/product 
	 * related data and use it for selection making under current candidates
	 */
	@Override
	public List<String> selectNextEvolutionIterationCandidates(int numberOfNextEvolutionCandidates, String evolutionDirectory,
			List<String> listOfCandidateSPLFileNamesFromEachPopulationMember,
			CandidateSelectorAssessingAdapter... candidateSelectorAssessingAdapters) {
		return null;
	}
}
