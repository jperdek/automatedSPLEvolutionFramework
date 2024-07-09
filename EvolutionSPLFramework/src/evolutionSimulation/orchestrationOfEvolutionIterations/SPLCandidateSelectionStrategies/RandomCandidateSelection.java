package evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


/**
 * Randomly selects the candidates from the previous iteration
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class RandomCandidateSelection implements SPLNextEvolutionIterationCandidateSelectionStrategy {

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
	public List<String> selectNextEvolutionIterationCandidates(int numberOfNextEvolutionCandidates,
			String evolutionDirectory, List<String> listOfCandidateSPLFileNamesFromEachPopulationMember,
			CandidateSelectorAssessingAdapter... candidateSelectorAssessingAdapters) {
		Random rndm = new Random();
		int selectedProductIndex;
		Set<String> usedCandidates = new HashSet<String>();
		String selectedCandidate = null;
		if (listOfCandidateSPLFileNamesFromEachPopulationMember.size() == 0) { return new ArrayList<String>(); }
		for (int i = 0; i < numberOfNextEvolutionCandidates; i++) {
			do {
				selectedProductIndex = rndm.nextInt(listOfCandidateSPLFileNamesFromEachPopulationMember.size());
				selectedCandidate = listOfCandidateSPLFileNamesFromEachPopulationMember.get(selectedProductIndex);
			} while (selectedCandidate == null || (numberOfNextEvolutionCandidates < listOfCandidateSPLFileNamesFromEachPopulationMember.size() && usedCandidates.contains(selectedCandidate)));
			usedCandidates.add(selectedCandidate);
		}
		return new ArrayList<String>(usedCandidates);
	}

	/**
	 * Returns associated candidate selector assessing adapters - NONE for this selecting strategy!
	 * - the helper entities to collect variation point/SPL/product related data and use it for selection making under current candidates
	 * 
	 * @return the associated candidate selector assessing adapters - the helper entities to collect variation point/SPL/product 
	 * related data and use it for selection making under current candidates
	 */
	@Override
	public List<CandidateSelectorAssessingAdapter> getAssociatedCandidateSelectorAssessingAdapters() {
		return null;
	}
}
