package divisioner.variabilityASTAntagonistMapping;

/**
 * Entity holding information from particular part of AST and functionality to transform it into associated part of another AST
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class VariationPointTransformationBetweenAsts {

	/**
	 * The name of variation point inside or bounded with start and end positions
	 * -depends on how transformation is applied 
	 */
	private String variationPointName;
	
	/**
	 * Start position of particular entity/part of AST
	 * -after transformation the transformed values are stored here
	 */
	private long startPosition;
	
	/**
	 * End position of particular entity/part of AST
	 * -after transformation the transformed values are stored here
	 */
	private long endPosition;
	

	/**
	 * Instantiates the entity holding information about particular part of AST and functionality to transform it into associated part of another AST
	 * 
	 * @param variationPointName - the name of variation point inside or bounded with start and end positions
	 * @param variabilityAstStartPosition - start position of particular entity/part of AST before transformation
	 * @param variabilityAstEndPosition - end position of particular entity/part of AST before transformation
	 */
	public VariationPointTransformationBetweenAsts(String variationPointName, long variabilityAstStartPosition, long variabilityAstEndPosition) {
		this.variationPointName = variationPointName;
		this.startPosition = variabilityAstStartPosition;
		this.endPosition = variabilityAstEndPosition;
	}
	
	/**
	 * Inserts the start and end positions of antagonist (the AST without system annotations) to this transformed entity
	 * 
	 * @param antagonistBlockAstStartPosition - start position of particular entity/part of antagonist AST (the AST without system annotations)
	 * @param antagonistBlockAstEndPosition - end position of particular entity/part of antagonist AST (the AST without system annotations)
	 */
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
	
	/**
	 * Transforms the start and end positions inserted during this instance creation from variability
	 *  AST (the AST with system annotations) according to start and end positions from antagonist (the AST without system annotations)
	 *  
	 * @param antagonistBlockAstStartPosition - start position of particular entity/part of antagonist AST (the AST without system annotations)
	 * @param antagonistBlockAstEndPosition - end position of particular entity/part of antagonist AST (the AST without system annotations)
	 * @param wholeVariableAstLength - the length of processed block
	 */
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
	
	/**
	 * Transforms the start and end positions inserted during this instance creation from variability
	 *  AST (the AST with system annotations) according to start and end positions from antagonist (the AST without system annotations)
	 *  
	 * @param variabilityBlockAstStartPosition
	 * @param variabilityBlockAstEndPosition
	 * @param antagonistBlockAstStartPosition - start position of particular entity/part of antagonist AST (the AST without system annotations)
	 * @param antagonistBlockAstEndPosition - end position of particular entity/part of antagonist AST (the AST without system annotations)
	 */
	public void doTransformation(long variabilityBlockAstStartPosition, long variabilityBlockAstEndPosition, 
			long antagonistBlockAstStartPosition, long antagonistBlockAstEndPosition) {
		///long variabilityBlockAstSize = variabilityBlockAstEndPosition - variabilityBlockAstStartPosition;
		this.startPosition = antagonistBlockAstStartPosition;// * (variabilityBlockAstStartPosition / variabilityBlockAstSize);
		this.endPosition = antagonistBlockAstEndPosition;// * (variabilityBlockAstEndPosition / variabilityBlockAstSize);
	}
	
	/**
	 * Returns the name of variation point inside or bounded with start and end positions
	 * 
	 * @return the name of variation point inside or bounded with start and end positions
	 */
	public String getVariationPointName() { return this.variationPointName; }
	
	/**
	 * Returns the start position of particular entity/part of AST
	 * 
	 * @return the start position of particular entity/part of AST
	 */
	public long getTransformedStartPosition() { return this.startPosition; }
	
	/**
	 * Returns the end position of particular entity/part of AST
	 * 
	 * @return the end position of particular entity/part of AST
	 */
	public long getTransformedEndPosition() { return this.endPosition; }
}
