package positiveVariabilityManagement;


/**
 * Signals the difference between the original/template AST part and the part of resulting synthesized AST before the injection of code constructs
 * -if these places do not match then the injection results in the inconsistencies
 * 
 * @author Jakub Perdek
 *
 */
public class VariationPointPlaceInArrayNotFound extends Exception {

	/**
	 * Serial version ID for Variation point place in array not found exception
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates the exception to signal difference between the original/template AST part and the part of resulting synthesized AST before the injection of the code constructs
	 * 
	 * @param message - the associated message with describing the difference between the original/template AST part and the part of resulting synthesized AST before the injection of code constructs
	 */
	public VariationPointPlaceInArrayNotFound(String message) {
		super(message);
	}
}
