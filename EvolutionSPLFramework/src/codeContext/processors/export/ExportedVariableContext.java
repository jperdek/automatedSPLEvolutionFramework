package codeContext.processors.export;

import org.json.simple.JSONObject;
import codeContext.InnerContext;


/**
 * Representation of exported context for a variable
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class ExportedVariableContext extends ExportedContext {
	
	/**
	 * The information if variable is declared as global - true if variable is declared as global otherwise false
	 */
	private boolean isGlobal;

	
	/**
	 * Creates the exported context for the variable
	 * 
	 * @param exportedObject - the exported object/variable
	 * @param fileName - the name of file containing processed export
	 * @param baseInnerContext - the associated base/root inner context with the exported object - pointing to hierarchy of inner contexts
	 * @param ifVariableThenGlobal - the information if variable is declared as global - true if variable is declared as global otherwise false
	 */
	public ExportedVariableContext(ExportedObjectInterface exportedObject, String fileName, 
			InnerContext baseInnerContext, boolean ifVariableThenGlobal) {
		super(exportedObject, fileName, baseInnerContext);
		this.isGlobal = ifVariableThenGlobal;
	}
	
	/**
	 * Creates JSON with information about the exported object/exported function 
	 * 
	 * @param exportedObject - the exported object
	 * @param baseInnerContext - the associated base/root inner context with the exported object - pointing to hierarchy of inner contexts
	 * @return the context information about exported variable represented in JSON format
	 */
	protected JSONObject createContextInformation(ExportedObjectInterface exportedObject, InnerContext baseInnerContext) {
		JSONObject contextInformation = super.createContextInformation(exportedObject, baseInnerContext);
		JSONObject variableObject = new JSONObject();
		variableObject.put(this.callableStr, this.callableType);
		variableObject.put("isGlobal", this.isGlobal);
		if (isGlobal) {
			contextInformation.put("global", variableObject);
		} else {
			contextInformation.put("local", variableObject);
		}
		return contextInformation;
	}
}
