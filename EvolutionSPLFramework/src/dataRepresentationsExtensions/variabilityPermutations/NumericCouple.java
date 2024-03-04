package dataRepresentationsExtensions.variabilityPermutations;


/**
 * Representation of the numeric couple
 * 
 * 
 * @author Jakub Perdek
 *
 * @param <T> type of object with the associated number
 */
public class NumericCouple<T> {
	
	/**
	 * The object with the associated number
	 */
	private T wholeIdentifier;
	
	/**
	 * The associated number/index (to form a couple)
	 */
	private int lastNumber;
	
	
	/**
	 * Creates the representation of the numeric couple (object with associated number/index)
	 * 
	 * @param wholeIdentifier - the object with the associated number 
	 * @param lastNumber - the associated number/index (to form a couple)
	 */
	public NumericCouple(T wholeIdentifier, int lastNumber) {
		this.lastNumber = lastNumber;
		this.wholeIdentifier = wholeIdentifier;
	}
	
	/**
	 * Returns the associated number/index
	 * 
	 * @return the associated number/index
	 */
	public int getLastNumber() { return this.lastNumber; }
	
	/**
	 * Returns the associated number/index (to form a couple)
	 * 
	 * @return the associated number/index (to form a couple)
	 */
	public T getIdentifier() { return this.wholeIdentifier; }
}
