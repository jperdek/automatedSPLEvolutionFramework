package astFileProcessor.annotationManagment;

/**
 * Base representation of AST annotation
 * 
 * @author Jakub Perdek
 *
 */
public class AnnotationInstance {

	/**
	 * Name of the annotation
	 */
	private String annotationName;
	
	
	/**
	 * Annotation instance
	 * 
	 * @param annotationName - the name of the annotation
	 */
	public AnnotationInstance(String annotationName) {
		this.annotationName = annotationName;
	}
	
	/**
	 * Returns the name of the annotation
	 * 
	 * @return the name of annotation
	 */
	public String getAnnotationName() { return this.annotationName; }
}
