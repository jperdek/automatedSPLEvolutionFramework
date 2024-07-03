package evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.strategies;

import codeContext.processors.export.ExportedContext;
import codeContext.processors.export.ExportedContextInterface;
import codeContext.processors.export.ExportedInterface;
import codeContext.processors.export.ExportedObjectInterface;
import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.LoadedAssetsFrequencyManager;


/**
 * Employs planning of particular asset only once during evolution phases or their subsequence
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class PlanAssetOnce implements AssetPlanningStrategy {


	/**
	 * Entity guarding the number of the use of the same asset during evolution phases or their subsequence
	 */
	private LoadedAssetsFrequencyManager loadedAssetsFrequencyManagerRef;
	

	/**
	 * Instantiates the entity that plans asset only once across evolution phases or their subsequence
	 */
	public PlanAssetOnce() {
		this.loadedAssetsFrequencyManagerRef = new LoadedAssetsFrequencyManager();
	}

	@Override
	public void planAsset(String exportType, ExportedInterface exportedInterface) {
		String callableFunctionalityIdentifier = exportedInterface.getCallableStr();
		this.planAsset(callableFunctionalityIdentifier);
	}
	
	@Override
	public void planAsset(String assetIdentifier) {
		this.loadedAssetsFrequencyManagerRef.addLocationIfNotExistWithLimit(assetIdentifier, 1);
	}

	@Override
	public boolean canUseAsset(String assetIdentifier) {
		int usageFrequency = this.loadedAssetsFrequencyManagerRef.getExportLocationUsageFrequency(assetIdentifier);
		if (usageFrequency == 0) {
			return true;
		}
		return false;
	}

	@Override
	public void useAsset(String assetIdentifier) throws AssetMisuse {
		if (!this.canUseAsset(assetIdentifier)) {
			throw new AssetMisuse("Cannot use assets if is aleready used! Checking is probably missing...");
		}
		this.loadedAssetsFrequencyManagerRef.checkConstructUseAndMarkIfTrue(assetIdentifier);
	}
}
