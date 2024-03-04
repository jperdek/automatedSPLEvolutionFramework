package codeContext.objects;

import java.util.Comparator;


/**
 * Allows to sort by context - according to stored position
 * 
 * @author Jakub Perdek
 *
 */
public class SortByCodeContext implements Comparator<CodeContextObject> {

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
