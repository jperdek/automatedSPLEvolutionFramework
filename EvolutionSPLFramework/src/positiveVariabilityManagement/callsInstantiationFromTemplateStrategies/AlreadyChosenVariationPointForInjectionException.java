package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies;

/**
 * Injection thrown if variation point is already chosen and requested one violates dependency issues
 * 
 * @author Jakub Perdek
 *
 */
public class AlreadyChosenVariationPointForInjectionException extends Exception {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates the exception informing about already chosen variation points and specifies requested variation point that is violating dependency issues
	 * 
	 * @param message - the information about violated dependency issue
	 */
	public AlreadyChosenVariationPointForInjectionException(String message) {
		super(message);
	}
}
