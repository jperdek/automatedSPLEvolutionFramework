package dividedAstExport;


/**
 * Exception to mark situations if invalid/unknown variation point marker is used
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class InvalidSystemVariationPointMarkerException extends Exception {

	/**
	 * Serial version ID of the InvalidSystemVariationPointMarkerException exception
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * Creates exception and specifies which invalid system variation point marker is used
	 * 
	 * @param message - the specific message describing which unknown/invalid marker name is used 
	 */
	public InvalidSystemVariationPointMarkerException(String message) {
		super(message);
	}
}
