package splEvolutionCore.candidateSelector;


/**
 * Exception referring to duplicate candidate identifier amongst (negative variability) variation point candidates
 * 
 * @author Jakub Perdek
 *
 */
public class DuplicateCandidateIdentifier extends Exception {

	/**
	 * Default serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates exception referring to duplicate candidate identifier amongst (negative variability) variation point candidates
	 * 
	 * @param message - the associated message that is referring to duplicate candidate name/identifier
	 */
	public DuplicateCandidateIdentifier(String message) {
		super(message);
	}
}
