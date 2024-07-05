package evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment;

import codeContext.processors.export.ExportedInterface;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.strategies.AssetMisuse;


/**
 * Base abstract functionality to planning limits and policies of assets
 * 
 * @author Jakub Perdek
 *
 */
public abstract class ExportAssetPlanner {

	/**
	 * Inserts the asset to its planner to guard asset planning limits and policies
	 * 
	 * @param exportType - the name/identifier or particular asset that should be checked and used
	 * @param exportedObject - the prescribed support for handling exports 
	 */
	public abstract void putAssetToPlanner(String exportType, ExportedInterface exportedObject);
	
	/**
	 * 
	 * @param exportType
	 */
	public abstract boolean canUseAsset(String assetIdentifier);
	
	/**
	 * 
	 * @param exportType
	 */
	public abstract void useAsset(String assetIdentifier, boolean usedInEvolutionIteration) throws AssetMisuse;
}
