package codeContext.processors.export;

/**
 * Interface to handle narrowed Exported context
 * 
 * @author Jakub Perdek
 *
 */
public interface ExportInterface {

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
