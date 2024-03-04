package codeContext.objects;

import java.util.Comparator;


/**
 * Representation of the basis of Code context
 * 
 * @author Jakub Perdek
 *
 */
public class CodeContextObject implements Comparator<CodeContextObject> {
	
	/**
	 * The position of code context in AST 
	 */
	protected long position;
	
	
	/**
	 * Creates the base code context with its position in AST
	 * 
	 * @param position - the position of code context in the application AST 
	 */
	public CodeContextObject(long position) {
		this.position = position;
	}

	/**
	 * Returns the position of Code context in the application AST
	 * 
	 * @return the position of Code context in the application AST
	 */
	public long getPosition() { return position; }
	
	/**
	 * Information if this context is already reached during parsing of/walk through the application AST
	 * 
	 * @param currentPosition - the actual position in the application AST that is used to compare state (if this code context is reached)
	 * @return true if the this code context is before or at current position
	 */
	public boolean isActual(long currentPosition) { return this.position <= currentPosition; }

	@Override
	/**
	 * Compares the positions of the two provided code contexts
	 * 
	 * @param o1 - the code context of the first object
	 * @param o2 - the code context of the second object
	 */
	public int compare(CodeContextObject o1, CodeContextObject o2) {
		return Math.toIntExact(o1.getPosition() - o2.getPosition());
	}
}
