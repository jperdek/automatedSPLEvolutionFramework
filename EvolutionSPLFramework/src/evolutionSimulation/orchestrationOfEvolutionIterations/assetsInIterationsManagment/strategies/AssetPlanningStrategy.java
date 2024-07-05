package evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.strategies;

import codeContext.processors.export.ExportedContext;
import codeContext.processors.export.ExportedInterface;
import codeContext.processors.export.ExportedObjectInterface;


/**
 * Prescribes methods required for assets planning across evolution phases or their subsequence
 * 
 * @author Jakub Perdek
 *
 */
public interface AssetPlanningStrategy {

	/**
	 * 
	 * @param exportType
	 * @param exportedContextInterface
	 */
	public void planAsset(String exportType, ExportedInterface exportedInterface);
	
	/**
	 * 
	 * @param assetIdentifier
	 */
	public void planAsset(String assetIdentifier);
	
	/**
	 * 
	 * @param assetIdentifier
	 * @return
	 */
	public boolean canUseAsset(String assetIdentifier);
	
	/**
	 * 
	 * @param assetIdentifier
	 * @throws AssetMisuse
	 */
	public void useAsset(String assetIdentifier, boolean usedInEvolutionIteration) throws AssetMisuse;
}
