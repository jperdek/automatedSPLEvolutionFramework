package codeContext.processors.export;

import org.json.simple.JSONObject;

import codeContext.InnerContext;


/**
 * Representation of function exported context
 * 
 * @author Jakub Perdek
 *
 */
public class ExportedFunctionContext extends ExportedContext {

	/**
	 * The name of the exported function
	 */
	public String functionName;
	
	
	/**
	 * Creates the exported class representation
	 * 
	 * @param exportedObject - the exported object/function
	 * @param fileName - the name of file containing processed export
	 * @param baseInnerContext - the associated base/root inner context with the exported object - pointing to hierarchy of inner contexts
	 * @param functionName - the name of the exported function
	 */
	public ExportedFunctionContext(ExportedObjectInterface exportedObject, String fileName,
			InnerContext baseInnerContext, String functionName) {
		super(exportedObject, fileName, baseInnerContext);
		this.functionName = functionName;
	}

	/**
	 * Creates JSON with information about the exported object/exported function 
	 * 
	 * @param exportedObject - the exported object
	 * @param baseInnerContext - the associated base/root inner context with the exported object - pointing to hierarchy of inner contexts
	 * @return the context information about exported function represented in JSON format
	 */
	protected JSONObject createContextInformation(ExportedObjectInterface exportedObject, InnerContext baseInnerContext) {
		JSONObject contextInformation = super.createContextInformation(exportedObject, baseInnerContext);
		contextInformation.put("functionName", this.functionName);
		return contextInformation;
	}
}
