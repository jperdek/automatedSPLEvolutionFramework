package evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.strategies;

import codeContext.processors.export.ExportedContext;
import codeContext.processors.export.ExportedContextInterface;
import codeContext.processors.export.ExportedInterface;
import codeContext.processors.export.ExportedObjectInterface;


/**
 * Employs no planning of assets across evolution phases or their subsequence
 * 
 * @author Jakub Perdek
 *
 */
public class NonRestrictiveAssetPlanning implements AssetPlanningStrategy {
	
	/**
	 * Instantiates the non-restrictive asset planning across evolution phases or their subsequence
	 */
	public NonRestrictiveAssetPlanning() {
	}
	

	@Override
	public void planAsset(String exportType, ExportedInterface exportedInterface) {
		String callableFunctionalityIdentifier = exportedInterface.getCallableStr();
		this.planAsset(callableFunctionalityIdentifier);
	}

	@Override
	public void planAsset(String assetIdentifier) {
	}

	@Override
	public boolean canUseAsset(String assetIdentifier) {
		return true;
	}

	@Override
	public void useAsset(String assetIdentifier) throws AssetMisuse {
	}

}
