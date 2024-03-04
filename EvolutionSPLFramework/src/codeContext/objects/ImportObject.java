package codeContext.objects;


/**
 * The representation of imported object
 * 
 * @author Jakub Perdek
 *
 */
public class ImportObject extends CodeContextObject {

	/**
	 * The name of the imported object
	 */
	private String importName;
	
	/**
	 * The path of the imported object
	 */
	private String importPath;
	
	/**
	 * Creates the imported object
	 * 
	 * @param importName - the name of the imported object
	 * @param importPath - the path of the imported object
	 * @param position - the position of the AST import
	 */
	public ImportObject(String importName, String importPath, long position) {
		super(position);
		this.importName = importName;
		this.importPath = importPath;
	}
	
	/**
	 * Returns the name of the imported object
	 * 
	 * @return the name of the imported object
	 */
	public String getImportName() { return this.importName; }
	
	/**
	 * Returns the path of the imported object
	 * 
	 * @return the path of the imported object
	 */
	public String getImportType() { return this.importPath; }
}
