package codeContext.processors.export;

/**
 * Interface to narrow functionality from exported software code fragments
 * 
 * 
 * @author Jakub Perdek
 *
 */
public interface ExportedInterface extends ExportInterface, ExportedObjectInterface, ExportedContextInterface {

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
