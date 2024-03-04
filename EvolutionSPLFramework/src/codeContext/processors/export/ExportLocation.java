package codeContext.processors.export;

import java.io.IOException;
import org.json.simple.JSONObject;
import codeConstructsEvaluation.transformation.ASTConverterClient;


/**
 * The representation of the location of exported object
 * 
 * @author Jakub Perdek
 *
 */
public class ExportLocation {

	/**
	 * The import path of exported object
	 */
	protected String importPath;
	
	/**
	 * The name of exported object/construct
	 */
	protected String constructName;
	
	/**
	 * The import path of exported object converted into AST
	 */
	protected JSONObject importAst;
	
	/**
	 * The file name where exported object/construct belongs
	 */
	protected String fileName;
	
	
	/**
	 * Creates instance of Export location
	 * 
	 * @param fileName - the file name where exported object/construct belongs
	 * @param constructName - the name of exported object/construct
	 */
	public ExportLocation(String fileName, String constructName) {
		this.fileName = fileName;
		this.constructName = constructName;
		this.importPath = "";
	}
	
	/**
	 * Creates instance of Export location from information from previous export location object
	 * - initializes file name and import path of exported location
	 * 
	 * @param exportLocation - the exported location object with associated information 
	 */
	public ExportLocation(ExportLocation exportLocation) {
		if (exportLocation.getFileName().replace("\\", "/").indexOf('/') > -1) {
			this.fileName = exportLocation.getFileName();
			this.importPath = exportLocation.getImportPath();
			//if (this.importPath == null || this.importPath.equals("")) {
			//	this.importPath = exportLocation.getFileName();
			//}
			//this.fileName = exportLocation.getFileName().substring(exportLocation.getFileName().lastIndexOf('/') + 1);
		} else {	
			this.fileName = exportLocation.getFileName();
			this.importPath = exportLocation.getImportPath();
		}
		this.constructName = exportLocation.getConstructName();
	}
	
	/**
	 * Sets the import path of exported object
	 * 
	 * @param importPath - the import path of exported object
	 */
	public void setImportPath(String importPath) { this.importPath = importPath; }
	
	/**
	 * Returns the import path of exported object
	 * 
	 * @return the import path of exported object
	 */
	public String getImportPath() { return this.importPath; }
	
	/**
	 * Returns the construct name of exported object
	 * 
	 * @return the construct name of exported object 
	 */
	public String getConstructName() { return this.constructName; }
	
	/**
	 * Returns the file name of exported object
	 */
	public String getFileName() { return this.fileName; }
	
	/**
	 * Prepares the import statement created from the export location and converted into AST
	 * 
	 * @return the import statement created from the export location and converted into AST
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public JSONObject createImportToAST() throws IOException, InterruptedException {
		return ASTConverterClient.getFirstStatementFromASTFile(ASTConverterClient.convertFromCodeToASTJSON(
				"import {" + this.constructName + "} from '" + this.importPath + "';"));
	}
}
