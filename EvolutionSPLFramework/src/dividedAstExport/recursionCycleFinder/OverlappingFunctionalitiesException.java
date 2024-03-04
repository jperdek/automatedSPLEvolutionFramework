package dividedAstExport.recursionCycleFinder;


/**
 * Exception to capture duplicate function names during inclusion of functionality (names) from multiple files
 * -exception is thrown if functionality with particular name is already created (put to map)
 * 
 * @author Jakub Perdek
 *
 */
public class OverlappingFunctionalitiesException extends Exception {

	/**
	 * Serial version UID of the OverlappingFunctionalitiesException exception
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * Creates the instance of the OverlappingFunctionalitiesException with specification of overlapping functionality
	 * 
	 * @param message - specified name of alreadyu created functionality
	 */
	public OverlappingFunctionalitiesException(String message) {
		super(message);
	}
}
