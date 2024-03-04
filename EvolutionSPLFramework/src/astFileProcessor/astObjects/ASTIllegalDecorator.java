package astFileProcessor.astObjects;

import org.json.simple.JSONObject;


/**
 * Representation of AST illegal decorator
 * 
 * @author Jakub Perdek
 *
 */
public class ASTIllegalDecorator extends ASTGenericDecorator {
	
	/**
	 * Redundant code represented as AST associated with use of illegal decorator
	 */
	private JSONObject redundantCode = null;
	
	/**
	 * Redundant code represented as string associated with use of illegal decorator
	 */
	private String redundantCodeString = null;
	
	
	/**
	 * Creates illegal decorator representation to handle illegal decorators in AST
	 * -stores all parameters
	 * 
	 * @param name - the name of illegal decorator
	 * @param astRepresentation - AST of illegal decorator
	 * @param coveredCode - covered code with illegal decorator
	 * @param redundantCode - redundant code represented as AST necessary to use illegal decorator
	 */
	public ASTIllegalDecorator(String name, JSONObject astRepresentation, 
			JSONObject coveredCode, JSONObject redundantCode) {
		super(name, astRepresentation, coveredCode);
		this.redundantCode = redundantCode;
	}
	
	/**
	 * Creates illegal decorator representation to handle illegal decorators in AST
	 * -stores all parameters
	 * 
	 * @param name - the name of illegal decorator
	 * @param astRepresentation - AST of illegal decorator
	 * @param coveredCode - covered code with illegal decorator
	 * @param redundantCode - redundant code represented as AST necessary to use illegal decorator
	 * @param redundantCodeString - - redundant code necessary to use illegal decorator
	 */
	public ASTIllegalDecorator(String name, JSONObject astRepresentation, 
			JSONObject coveredCode, JSONObject redundantCode, String redundantCodeString) {
		this(name, astRepresentation, coveredCode, redundantCode);
		this.redundantCodeString = redundantCodeString;
	}
	
	/**
	 * Returns redundant code necessary to use illegal decorator that is represented as AST
	 * 
	 * @return redundant code necessary to use illegal decorator that is represented as AST
	 */
	public JSONObject getRedundantCode() { return this.redundantCode; }
	
	/**
	 * Returns redundant code necessary to use illegal decorator that is represented as string
	 * 
	 * @return redundant code necessary to use illegal decorator that is represented as AST
	 */
	public String getRedundantCodeString() { return this.redundantCodeString; }
}
