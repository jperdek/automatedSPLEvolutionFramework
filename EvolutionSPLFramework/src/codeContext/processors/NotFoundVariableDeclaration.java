package codeContext.processors;


/**
 * Exception thrown when variable declaration is not found
 * 
 * @author Jakub Perdek
 *
 */
public class NotFoundVariableDeclaration extends Exception {
	
	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates exception for not found variable declaration
	 * -allows to tune functionality (regexes) to support more declarations and to adapt on new languages
	 * 
	 * @param message - contains information about variable declaration statement that is not found
	 */
	public NotFoundVariableDeclaration(String message) {
		super(message);
	}
}
