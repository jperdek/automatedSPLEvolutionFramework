package divisioner.variabilityASTAntagonistMapping;

/**
 * 
 * @author perde
 *
 */
public class VariationPointTransformationBetweenAsts {

	private String variationPointName;
	private long startPosition;
	private long endPosition;
	

	public VariationPointTransformationBetweenAsts(String variationPointName, long variabilityAstStartPosition, long variabilityAstEndPosition) {
		this.variationPointName = variationPointName;
		this.startPosition = variabilityAstStartPosition;
		this.endPosition = variabilityAstEndPosition;
	}
	
	public void doTransformationOnBlocks(long antagonistBlockAstStartPosition, long antagonistBlockAstEndPosition) {
		if (antagonistBlockAstStartPosition == 0) { 
			this.startPosition = 0; 
		} else {
			this.startPosition = antagonistBlockAstStartPosition; // * (this.startPosition / antagonistBlockAstStartPosition);
		}
		if (antagonistBlockAstEndPosition == 0) { 
			this.endPosition = 0; 
		} else {
			this.endPosition = antagonistBlockAstEndPosition; // * (this.endPosition / antagonistBlockAstEndPosition);
		}
	}
	
	public void doTransformation(long antagonistBlockAstStartPosition, long antagonistBlockAstEndPosition, long wholeVariableAstLength) {
		if (antagonistBlockAstStartPosition == 0) { 
			this.startPosition = 0; 
		} else {
			this.startPosition = (this.startPosition / wholeVariableAstLength) * antagonistBlockAstStartPosition;
		}
		if (antagonistBlockAstEndPosition == 0) { 
			this.endPosition = 0; 
		} else {
			this.endPosition = (this.endPosition / wholeVariableAstLength) * antagonistBlockAstEndPosition;
		}
	}
	
	public void doTransformation(long variabilityBlockAstStartPosition, long variabilityBlockAstEndPosition, 
			long antagonistBlockAstStartPosition, long antagonistBlockAstEndPosition) {
		///long variabilityBlockAstSize = variabilityBlockAstEndPosition - variabilityBlockAstStartPosition;
		this.startPosition = antagonistBlockAstStartPosition;// * (variabilityBlockAstStartPosition / variabilityBlockAstSize);
		this.endPosition = antagonistBlockAstEndPosition;// * (variabilityBlockAstEndPosition / variabilityBlockAstSize);
	}
	
	public String getVariationPointName() { return this.variationPointName; }
	
	public long getTransformedStartPosition() { return this.startPosition; }
	
	public long getTransformedEndPosition() { return this.endPosition; }
}
