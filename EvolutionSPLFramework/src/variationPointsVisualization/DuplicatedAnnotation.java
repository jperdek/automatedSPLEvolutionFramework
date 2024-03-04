package variationPointsVisualization;


/**
 * Exception capturing duplicated annotation that is found on AST
 * 
 * @author Jakub Perdek
 *
 */
public class DuplicatedAnnotation extends Exception {

	/**
	 * Serial version UID for DuplicatedAnnotation exception
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates DuplicatedAnnotation
	 * 
	 * @param message - associated message to found duplicated annotation
	 */
	public DuplicatedAnnotation(String message) {
		super(message);
	}
}
