package evolutionSimulation.productAssetsInitialization;


/**
 * Exception representing the unknown resource to process
 * -the resources have their roles and are responsible for various task - their configuration is necessary
 * 
 * @author Jakub Perdek
 *
 */
public class UnknownResourceToProcessException extends Exception {
	
	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Creates exception that some of resources are unknown or used improperly
	 * 
	 * @param message - the information about given resource that is unknown or used improperly
	 */
	public UnknownResourceToProcessException(String message) {
		super(message);
	}
}
