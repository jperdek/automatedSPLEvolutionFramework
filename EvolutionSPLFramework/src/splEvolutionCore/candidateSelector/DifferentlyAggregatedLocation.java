package splEvolutionCore.candidateSelector;


/**
 * Exception referring that aggregated exports for different metrics of the same callable construct are different
 * 
 * @author Jakub Perdek
 *
 */
public class DifferentlyAggregatedLocation extends Exception {

	/**
	 * Default serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates exception referring that aggregated exports for different metrics of the same callable construct are different
	 * 
	 * @param message - the specific referenced message
	 */
	public DifferentlyAggregatedLocation(String message) {
		super(message);
	}
}
