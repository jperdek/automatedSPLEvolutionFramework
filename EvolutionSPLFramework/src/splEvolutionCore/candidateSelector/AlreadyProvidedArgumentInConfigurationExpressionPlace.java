package splEvolutionCore.candidateSelector;


/**
 * Exception referring to already provided argument in configuration expression place
 * -annotation has already assigned arguments
 * 
 * @author Jakub Perdek
 *
 */
public class AlreadyProvidedArgumentInConfigurationExpressionPlace extends Exception {

	/**
	 * Serial version UID to AlreadyProvidedArgumentInConfigurationExpressionPlace exception
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates exception referring to already provided argument in configuration expression place
	 * 
	 * @param message - the information about already provided argument in configuration expression place
	 */
	public AlreadyProvidedArgumentInConfigurationExpressionPlace(String message) {
		super(message);
	}
}
