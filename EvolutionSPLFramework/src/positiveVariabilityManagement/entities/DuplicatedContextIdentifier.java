package positiveVariabilityManagement.entities;


/**
 * Exception thrown if duplicated identifiers are assigned amongst contexts
 * 
 * @author Jakub Perdek
 *
 */
public class DuplicatedContextIdentifier extends Exception {
	
	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates duplicate context identifier exception
	 * 
	 * @param message - captures which context identifier is duplicated
	 */
	public DuplicatedContextIdentifier(String message) {
		super(message);
	}
}
