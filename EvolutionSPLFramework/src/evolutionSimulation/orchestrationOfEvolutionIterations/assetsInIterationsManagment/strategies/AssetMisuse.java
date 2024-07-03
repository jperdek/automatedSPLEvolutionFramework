package evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.strategies;


/**
 * Exception thrown after misuse of particular asset during its planning
 * 
 * @author Jakub Perdek
 *
 */
public class AssetMisuse extends Exception {

	
	/**
	 * Assets Misuse serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates the exception with specification of how particular asset was misused during Asset planning 
	 * 
	 * @param assetsMisuseMessage - the specific information about misuse during planning of particular asset
	 */
	public AssetMisuse(String assetsMisuseMessage) {
		super(assetsMisuseMessage);
	}
}
