package evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.strategies;


import java.util.Iterator;
import java.util.LinkedList;

import codeContext.processors.export.ExportedContext;
import codeContext.processors.export.ExportedInterface;
import codeContext.processors.export.ExportedObjectInterface;


/**
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class PlanAssetWithSkips implements AssetPlanningStrategy {
	
	/**
	 * The sequence of used particular assets each associated with particular evolution phase performed order
	 */
	private LinkedList<String> assetSequence;
	
	/**
	 * Actual position is asset sequence, initialized to 0 - no currently executed evolution iteration phases
	 */
	private int sequencePosition;
	
	/**
	 * The gap expressed in number of evolution iteration phases between use of the same functionality
	 */
	private int seekPosition;

	
	/**
	 * Instantiates the non-restrictive asset planning across evolution phases or their subsequence7
	 * 
	 * @param seekPosition - the gap expressed in number of evolution iteration phases between use of the same functionality
	 */
	public PlanAssetWithSkips(int seekPosition) {
		this.sequencePosition = 0;
		this.seekPosition = seekPosition;
		this.assetSequence = new LinkedList<String>();
	}

	@Override
	public void planAsset(String exportType, ExportedInterface exportedInterface) {
		String callableFunctionalityIdentifier = exportedInterface.getCallableStr();
		this.planAsset(callableFunctionalityIdentifier);
	}

	@Override
	public void planAsset(String assetIdentifier) {
		// no planning is required
	}

	@Override
	public boolean canUseAsset(String assetIdentifier) {
		Iterator<String> reversedIterator = this.assetSequence.descendingIterator();
		String sequenceString;
		while (reversedIterator.hasNext()) {
			sequenceString = reversedIterator.next();
			if(sequenceString.equals(assetIdentifier)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void useAsset(String assetIdentifier) throws AssetMisuse {
		if (!this.canUseAsset(assetIdentifier)) {
			throw new AssetMisuse("Cannot use assets if is aleready used! Checking is probably missing...");
		}
		this.assetSequence.addLast(assetIdentifier);
		this.sequencePosition = this.sequencePosition + 1;
		if (this.seekPosition > this.assetSequence.size()) {
			this.assetSequence.removeFirst();
		}
	}
}
