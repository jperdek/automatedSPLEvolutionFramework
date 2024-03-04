package splEvolutionCore.candidateSelector.valueAssignment.selectionAndUnselectionStrategies;

import java.util.List;
import splEvolutionCore.candidateSelector.NegativeVariationPointCandidate;


/**
 * Strategies to manage annotated parts of negative variability
 * -selection of candidates that will belong to variability
 * -deselection of candidates to assign them to commonality
 * 
 * @author Jakub Perdek
 *
 */
public interface SelectionAndUnselectionStrategy {

	/**
	 * Selecting decorator-annotable code fragment candidates that were previously unselected according to implemented strategy
	 * 
	 * @param candidatesToSelect - the list of unselected decorator-annotable code fragments or preferred unselected candidates to be selected
	 * @param alreadySelectedCandidates - the list of already selected decorator-annotable code fragments
	 */
	public void selectCandidates(List<NegativeVariationPointCandidate> candidatesToSelect, 
			List<NegativeVariationPointCandidate> alreadySelectedCandidates);
	
	/**
	 * Deselecting some of decorator-annotable code fragment candidates according to implemented strategy 
	 * 
	 * @param alreadySelectedCandidates - the list already selected decorator-annotable code fragments to apply deselection strategy
	 */
	public void unselectCandidates(List<NegativeVariationPointCandidate> alreadySelectedCandidates);
}
