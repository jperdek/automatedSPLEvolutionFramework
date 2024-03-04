package splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment;


/**
 * Exception thrown when method which complexity is evaluated is not found in results
 * 
 * @author Jakub Perdek
 *
 */
public class MethodToEvaluateComplexityNotFoundException extends Exception {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates exception covering not found complexity records for analyzed method
	 * 
	 * @param message - associated message which matod does not have associated and evaluated complexity metrics
	 */
	public MethodToEvaluateComplexityNotFoundException(String message) {
		super(message);
	}
}
