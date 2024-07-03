package codeContext.processors.export;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import codeContext.ClassContext;
import codeContext.FunctionContext;
import codeContext.InnerContext;
import codeContext.objects.VariableObject;
import splEvolutionCore.DebugInformation;


/**
 * Representation of exported context
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class ExportedContext implements ExportInterface {

	/**
	 * Enumeration of code context types
	 * -function
	 * -class
	 * -variable
	 * -undefined/unknown
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum CodeContextType {
		VARIABLE("variableEC"),
		FUNCTION("functionEC"),
		CLASS("classEC"),
		UNKNOWN("unknownEC");
		
		/**
		 * The exported context type
		 */
		public final String label;

		/**
		 * Creates the code context type
		 * 
		 * @param label - the name of code context
		 */
	    private CodeContextType(String label) {
	        this.label = label;
	    }
	    
	    /**
	     * Returns the exported context name/type
	     * 
	     * @return the exported context name/type
	     */
	    public String getName() { return this.label; }
	    
	    /**
	     * Finds associated exported code context type
	     * - classifies code context as CodeContextType.VARIABLE, CodeContextType.CLASS or CodeContextType.FUNCTION
	     * - if not found then returns CodeContextType.UNKNOWN
	     * 
	     * @param exportedObjectInterface - the exported code context object that is going to be classified
	     * @return the context type of associated exported code context
	     */
	    public static CodeContextType findAssociatedCodeContext(ExportedObjectInterface exportedObjectInterface) {
	    	if (exportedObjectInterface instanceof VariableObject) {
	    		return CodeContextType.VARIABLE;
	    	} else if (exportedObjectInterface instanceof ClassContext) {
	    		return CodeContextType.CLASS;
	    	} else if (exportedObjectInterface instanceof FunctionContext) {
	    		return CodeContextType.FUNCTION;
	    	} 
	    	return CodeContextType.UNKNOWN;
	    }
	}
	
	//protected JSONObject importAst;
	/**
	 * The associated exported location object containing all information associated with the location of the export
	 */
	protected ExportLocation exportedLocation; //String fileName;
	
	/**
	 * The callable (line/lines of) code
	 */
	protected String callableStr;
	
	/**
	 * The type of exported object represented as string
	 */
	protected String callableType;
	
	/**
	 * The type of exported code context represented as CodeContextType object
	 */
	protected CodeContextType type;
	
	/**
	 * The associated information with the exported context stored in JSON object
	 */
	protected JSONObject contextInformation;

	
	/**
	 * Creates the instance of exported context
	 * 
	 * @param exportedObject - the processed object that is exported
	 * @param fileName - the name of the file where script is exported/harvested
	 * @param baseInnerContext - the associated base/root inner context with the exported object - pointing to hierarchy of inner contexts
	 */
	public ExportedContext(ExportedObjectInterface exportedObject, String fileName, InnerContext baseInnerContext) {
		this.type = CodeContextType.findAssociatedCodeContext(exportedObject);
		this.callableStr = exportedObject.constructCallableForm(); //variable name in case of variable

		this.exportedLocation = new ExportLocation(fileName, this.callableStr.replace("new ", "").split("\\(")[0]);
		this.callableType = exportedObject.getExportType();
		this.contextInformation = this.createContextInformation(exportedObject, baseInnerContext);
	}
	
	/**
	 * Exports the associated information with the exported object into JSON
	 * 
	 * @param exportedObject - the processed object that is exported
	 * @param baseInnerContext - the associated base/root inner context with the exported object - pointing to hierarchy of inner contexts
	 * @return JSON object with the context information
	 */
	protected JSONObject createContextInformation(ExportedObjectInterface exportedObject, InnerContext baseInnerContext) {
		JSONObject contextInformation = new JSONObject();
		JSONArray callableForms = exportedObject.constructAllAvailableCallsUnderType();
		ExportedObjectInterface variableTypeObject;
		if (callableForms == null) {
			variableTypeObject = baseInnerContext.getExtendableInnerObjectAccordingToType(callableType);
			if (variableTypeObject != null) {
				callableForms = variableTypeObject.constructAllAvailableCallsUnderType(callableStr);
			}
		}
		contextInformation.put("allAvailableCalls", callableForms);
		contextInformation.put("importName", this.exportedLocation.getFileName());
		return contextInformation;
	}
	
	/**
	 * Returns the associated exported location object containing all information associated with the location of the export
	 * 
	 * @return the associated exported location object containing all information associated with the location of the export
	 */
	public ExportLocation getExportedLocation() { return this.exportedLocation; }
	
	/**
	 * Returns the callable (line/lines of) code
	 * 
	 * @return the callable (line/lines of) code
	 */
	public String getCallableStr() { return this.callableStr; }
	
	/**
	 * Returns the type of exported object represented as string
	 * 
	 * @return the type of exported object represented as string
	 */
	public String getCallableType() { return this.callableType; }
	
	/**
	 * Returns the name of the file where script is exported/harvested
	 * 
	 * @return the name of the file where script is exported/harvested
	 */
	public String getFileName() { return this.exportedLocation.getFileName(); }

	/**
	 * Returns the identifier (should be unique) of the exported context
	 * - form: callableString>TYPE[Ktype>]IMPORT[KfileName>]
	 * 
	 * @return the identifier (should be unique) of the exported context
	 */
	public String getIdentifier() { return this.callableStr + "TYPE[" + this.callableType 
			+ "]IMPORT[" + this.exportedLocation.getFileName() + "]"; }

	/**
	 * Prints the associated information with the context
	 */
	public void printContent() {
		if (DebugInformation.SHOW_CREATED_ENTITIES) {
			System.out.println(this.getIdentifier());
			System.out.println("CONTEXT INFORMATION: " + this.contextInformation.toString());
		}
	}

	/**
	 * Returns the code representation AST in String
	 * 
	 * @return the code representation AST in String
	 */
	public String getIdentificationAST() { return this.contextInformation.toJSONString(); }
}
