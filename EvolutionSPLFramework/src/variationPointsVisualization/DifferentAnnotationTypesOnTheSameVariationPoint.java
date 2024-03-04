package variationPointsVisualization;


/**
 * Exception capturing different annotation types on the same variation point
 * -for example annotation for class cannot be inserted in place where method annotation is placed
 * 
 * @author Jakub Perdek
 *
 */
public class DifferentAnnotationTypesOnTheSameVariationPoint extends Exception {

	/**
	 * Serial Version UID for DifferentAnnotationTypesOnTheSameVariationPoint exception
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates DifferentAnnotationTypesOnTheVariationPoint exception
	 * 
	 * @param message - message associated with different types on the same variation point
	 */
	public DifferentAnnotationTypesOnTheSameVariationPoint(String message) {
		super(message);
	}
}
