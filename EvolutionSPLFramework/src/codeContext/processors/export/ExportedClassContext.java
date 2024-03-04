package codeContext.processors.export;

import org.json.simple.JSONObject;
import codeContext.InnerContext;


/**
 * Representation of class exported context
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class ExportedClassContext extends ExportedContext {

	/**
	 * The name of the exported class
	 */
	public String className;
	

	/**
	 * Creates the exported class representation
	 * 
	 * @param exportedObject - the exported object/class
	 * @param fileName - the name of file containing processed export
	 * @param baseInnerContext - the associated base/root inner context with the exported object - pointing to hierarchy of inner contexts
	 * @param className - the name of the exported class
	 */
	public ExportedClassContext(ExportedObjectInterface exportedObject, String fileName,
			InnerContext baseInnerContext, String className) {
		super(exportedObject, fileName, baseInnerContext);
		this.className = className;
	}
	
	/**
	 * Creates JSON with information about the exported object/exported class 
	 * 
	 * @param exportedObject - the exported object
	 * @param baseInnerContext - the associated base/root inner context with the exported object - pointing to hierarchy of inner contexts
	 * @return the context information about exported class represented in JSON format
	 */
	protected JSONObject createContextInformation(ExportedObjectInterface exportedObject, InnerContext baseInnerContext) {
		JSONObject contextInformation = super.createContextInformation(exportedObject, baseInnerContext);
		contextInformation.put("className", this.className);
		return contextInformation;
	}
}
