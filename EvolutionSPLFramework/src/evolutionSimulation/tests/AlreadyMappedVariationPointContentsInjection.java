package evolutionSimulation.tests;


/**
 * Exception representing already mapped variation points content
 * 
 * @author Jakub Perdek
 *
 */
public class AlreadyMappedVariationPointContentsInjection extends Exception {

	/**
	 *  Serial version UID for AlreadyMappedVariationPointContentsInjection exception
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates the exception for already mapped variation point contents referring that the id for given variation point is not unique
	 * 
	 * @param message - the name of already mapped variation point content that is not unique
	 */
	public AlreadyMappedVariationPointContentsInjection(String message) {
		super(message);
	}
}
