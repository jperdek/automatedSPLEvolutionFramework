package dividedAstExport.recursionCycleFinder;


/**
 * Creates code area boundary - intended to find cycles in code blocks (areas)
 * -the boundary is bounded with start and end position in AST
 * -the name is associated
 * 
 * @author Jakub Perdek
 *
 */
public class CodeArea {

	/**
	 * Associated boundary/code area name
	 */
	private String codeName;
	
	/**
	 * Start position of boundary in analyzed text
	 */
	private Long startPosition;
	
	/**
	 * End position of boundary in analyzed text
	 */
	private Long endPosition;
	
	
	/**
	 * Creates code area (boundary)
	 * 
	 * @param codeName - associated boundary/code area name
	 * @param startPosition - start position of boundary in analyzed text
	 * @param endPosition - end position of boundary in analyzed text
	 */
	public CodeArea(String codeName, Long startPosition, Long endPosition) {
		this.codeName = codeName;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
	}
	
	/**
	 * Returns associated boundary/code area name
	 * 
	 * @return associated boundary/code area name
	 */
	public String getCodeName() { return this.codeName; }
	
	/**
	 * Returns the start position of boundary in analyzed text
	 * 
	 * @return start position of boundary in analyzed text
	 */
	public Long getStartPosition() { return this.startPosition; }
	
	/**
	 * Returns the end position of boundary in analyzed text
	 * 
	 * @return end position of boundary in analyzed text
	 */
	public Long getEndPosition() { return this.endPosition; }
}
