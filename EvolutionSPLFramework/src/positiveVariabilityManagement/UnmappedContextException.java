package positiveVariabilityManagement;


/**
 * The exception informing about impossibility to map content on the resulting AST of final product in the synthesis process
 * -have not been fully incorporated so far
 *
 * 
 * @author Jakub Perdek
 *
 */
public class UnmappedContextException extends Exception {

	/**
	 * Serial version UID of unmapped content exception
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates the exception for unmapped content with exact name of unmapped entity
	 * 
	 * @param message - the information about code construct that has not been mapped so far
	 */
	public UnmappedContextException(String message) {
		super(message);
	}
}
