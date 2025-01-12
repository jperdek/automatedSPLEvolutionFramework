package evolutionSimulation.orchestrationOfEvolutionIterations;


/**
 * Exception thrown if no candidate has been evolved in previous evolution iteration
 * 
 * @author Jakub Perdek
 *
 */
public class NoCandidateException extends Exception {

	/**
	 * Exception serial ID
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Exception thrown if no candidate has been evolved in previous evolution iteration
	 * 
	 * @param message - information about location with no software product line from previous iteration to be evolved
	 */
	public NoCandidateException(String message) {
		super(message);
	}
}
