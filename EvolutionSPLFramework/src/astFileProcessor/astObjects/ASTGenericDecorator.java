package astFileProcessor.astObjects;

import java.util.List;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import astFileProcessor.annotationManagment.AnnotationInstance;


/**
 * Representation of AST decorator
 * 
 * @author Jakub Perdek
 *
 */
public class ASTGenericDecorator extends AnnotationInstance {

	/**
	 * Possible associated code structures of the decorator
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public enum DecoratorAssociatedWith {CLASS, METHOD, FILE_ONLY, UNKNOWN};
	
	/**
	 * The decorator represented as JSON object with AST
	 */
	private JSONObject astRepresentation;
	
	/**
	 * AST of the code that is covered with decorator
	 */
	private List<JSONObject> coveredCodes;
	
	/**
	 * The associated type to which is decorator applied
	 */
	private DecoratorAssociatedWith associationWith;
	
	
	/**
	 * Creates ASt generic decorator instance
	 * 
	 * @param name - the name of the decorator
	 * @param astRepresentation - JSON object with AST
	 * @param coveredCode - AST of the code that is covered with decorator
	 */
	public ASTGenericDecorator(String name, JSONObject astRepresentation, JSONObject coveredCode) {
		super(name);
		this.astRepresentation = astRepresentation;
		this.coveredCodes = new ArrayList<JSONObject>();
		this.coveredCodes.add(coveredCode);
	}
	
	/**
	 * Sets the associated type to which is decorator applied
	 *  
	 * @param associationWith - the associated type to which is decorator applied
	 */
	public void setAssociatedWith(DecoratorAssociatedWith associationWith) { this.associationWith = associationWith; }
	
	/**
	 * Checks if decorator is associated with provided associated type in parameter
	 * 
	 * @param associationWith - another associated type to which can be decorator applied for comparison 
	 * @return true if the associated type to which is decorator applied match with provided one called associationWith otherwise false
	 */
	public boolean isAssociatedWith(DecoratorAssociatedWith associationWith) {
		return this.associationWith == associationWith;
	}

	/**
	 * Returns the decorator name
	 * 
	 * @return decorator name
	 */
	public String getName() { return this.getAnnotationName(); }
	
	/**
	 * Returns the AST representation
	 * 
	 * @return AST representation
	 */
	public JSONObject getASTRepresentation() { return this.astRepresentation; }
	
	/**
	 * Returns the covered code on given index
	 * 
	 * @param index - the index to harvest covered code
	 * @return
	 */
	public JSONObject getCoveredCode(int index) { return this.coveredCodes.get(index); }
}
