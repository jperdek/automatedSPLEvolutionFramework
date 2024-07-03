package codeContext.processors.export;

import org.json.simple.JSONArray;


/**
 * Interface to integrate all code object representations that can be/are exported
 * 
 * 
 * @author Jakub Perdek
 *
 */
public interface ExportedObjectInterface {
	
	/**
	 * Returns the name of the export
	 * 
	 * @return the name of the export
	 */
	public String getExportName();
	
	/**
	 * Returns the type of the export
	 * 
	 * @return the type of the export
	 */
	public String getExportType();
	
	/**
	 * Returns the callable (line/lines of) code
	 * 
	 * @return the callable (line/lines of) code
	 */
	public String constructCallableForm();
	
	/**
	 * Returns the JSON array of all available calls under given type/object
	 * 
	 * @return the JSON array of all available calls under given type/object
	 */
	public JSONArray constructAllAvailableCallsUnderType();
	
	/**
	 * Returns the JSON array of all available calls under given type/object
	 * 
	 * @param initialVariableName
	 * @return the JSON array of all available calls under given type/object
	 */
	public JSONArray constructAllAvailableCallsUnderType(String initialVariableName); 
	
	/**
	 * Returns the exported object according to its type
	 * 
	 * @param innerObjectType - the type of object used to select object
	 * @return the exported object according to its type 
	 */
	public ExportedObjectInterface getExtendableInnerObjectAccordingToType(String innerObjectType);

	/**
	 * Information if particular instance is exported
	 * 
	 * @return true if this instance is exported otherwise false
	 */
	public boolean isExported();
	
	/**
	 * Sets the instance as exported
	 */
	public void setAsExported();

	/**
	 * Returns the export represented as string
	 * 
	 * @return the export represented as string
	 */
	public String getCallableStr();
	
	/**
	 * Returns the code representation AST in String
	 * 
	 * @return the code representation AST in String
	 */
	public String getIdentificationAST();
}
