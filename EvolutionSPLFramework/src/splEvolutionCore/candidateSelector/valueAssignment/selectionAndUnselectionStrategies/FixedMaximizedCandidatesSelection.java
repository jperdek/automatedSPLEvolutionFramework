package splEvolutionCore.candidateSelector.valueAssignment.selectionAndUnselectionStrategies;

import java.util.LinkedList;
import java.util.List;
import splEvolutionCore.candidateSelector.NegativeVariationPointCandidate;


/**
 * Selecting and deselecting the fixed number of candidates
 * 
 * @author Jakub Perdek
 *
 */
public class FixedMaximizedCandidatesSelection implements SelectionAndUnselectionStrategy {

	/**
	 * Fixed number of candidates to change - select and deselect
	 */
	private int candidatesToChange = 2;
	
	/**
	 * Create instance of selection and deselection strategy based on fixed number of candidates
	 * 
	 * @param candidatesToChange - number of candidates to change - select and deselect
	 */
	public FixedMaximizedCandidatesSelection(int candidatesToChange) {
		this.candidatesToChange = candidatesToChange;
	}
	
	/**
	 * Sort of given negative variation point candidates
	 * 
	 * @param list - list of negative variation point candidates (some of decorator-annotable existing code fragments) to sort
	 */
	private static void sort(List<NegativeVariationPointCandidate> list) {
        list.sort((o1, o2) -> (int) Math.round(o2.evaluateCandidateValue() - o1.evaluateCandidateValue()));
    }
	
	/**
	 * Reverse sort of given negative variation point candidates
	 * 
	 * @param list - list of negative variation point candidates (some of decorator-annotable existing code fragments) to sort reversively
	 */
	private static void sortReverse(List<NegativeVariationPointCandidate> list) {
        list.sort((o1, o2) -> (int) Math.round(o1.evaluateCandidateValue() - o2.evaluateCandidateValue()));
    }
	
	/**
	 * Selecting fixed number of decorator-annotable code fragment candidates that were previously unselected according to implemented strategy
	 * 
	 * @param candidatesToSelect - the list of unselected decorator-annotable code fragments or preferred unselected candidates to be selected
	 * @param alreadySelectedCandidates - the list of already selected decorator-annotable code fragments
	 */
	@Override
	public void selectCandidates(List<NegativeVariationPointCandidate> candidatesToSelect, 
			List<NegativeVariationPointCandidate> alreadySelectedCandidates) {
		this.sort(candidatesToSelect);
		for (int ithAddedCandidate = 0; ithAddedCandidate < this.candidatesToChange 
				&& ithAddedCandidate < candidatesToSelect.size(); ithAddedCandidate++) {
			((LinkedList<NegativeVariationPointCandidate>) alreadySelectedCandidates).addLast(candidatesToSelect.get(ithAddedCandidate));
		}
	}

	/**
	 * Deselecting fixed number of decorator-annotable code fragment candidates according to implemented strategy 
	 * 
	 * @param alreadySelectedCandidates - the list already selected decorator-annotable code fragments to apply deselection strategy
	 */
	@Override
	public void unselectCandidates(List<NegativeVariationPointCandidate> alreadySelectedCandidates) {
		this.sortReverse(alreadySelectedCandidates);
		for (int ithRemovedCandidate = 0; ithRemovedCandidate < this.candidatesToChange 
				&& ithRemovedCandidate < alreadySelectedCandidates.size(); ithRemovedCandidate++) {
			((LinkedList<NegativeVariationPointCandidate>) alreadySelectedCandidates).removeFirst();
		}
	}
}
