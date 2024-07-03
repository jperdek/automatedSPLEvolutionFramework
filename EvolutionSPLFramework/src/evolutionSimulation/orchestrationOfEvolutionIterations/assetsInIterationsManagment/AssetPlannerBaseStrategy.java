package evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment;

import codeContext.processors.export.ExportedContext;
import codeContext.processors.export.ExportedContextInterface;
import codeContext.processors.export.ExportedInterface;
import codeContext.processors.export.ExportedObjectInterface;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.strategies.AssetMisuse;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.strategies.AssetPlanningStrategy;


/**
 * Manages planning of assets during evolution (iterations) or its subsequence of evolution iterations
 * 
 * @author Jakub Perdek
 *
 */
public class AssetPlannerBaseStrategy extends ExportAssetPlanner {

	/**
	 * Used strategy to plan assets during evolution (iterations) or its subsequence of evolution iterations
	 */
	protected AssetPlanningStrategy assetPlanningStrategy;
	

	/**
	 * Instantiates the asset planning strategy
	 * 
	 * @param assetPlanningStrategy - used strategy to plan assets during evolution (iterations) or its subsequence of evolution iterations
	 */
	public AssetPlannerBaseStrategy(AssetPlanningStrategy assetPlanningStrategy) {
		this.assetPlanningStrategy = assetPlanningStrategy;
	}
	
	/**
	 * Inserts the asset to its planner to guard asset planning limits and policies
	 * 
	 * @param exportType - the name/identifier or particular asset that should be checked and used
	 * @param exportedContextInterface
	 */
	@Override
	public void putAssetToPlanner(String exportType, ExportedInterface exportedInterface) {
		this.assetPlanningStrategy.planAsset(exportType, exportedInterface);
	}

	@Override
	public boolean canUseAsset(String assetIdentifier) {
		return this.assetPlanningStrategy.canUseAsset(assetIdentifier);
	}

	@Override
	public void useAsset(String assetIdentifier) throws AssetMisuse { this.assetPlanningStrategy.useAsset(assetIdentifier); }
}
