package codeContext.processors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import codeContext.CodeContext;


/**
 * Functionality to process import statements (imports) in AST processed part
 * 
 * @author Jakub Perdek
 *
 */
public class ImportProcessor {

	/**
	 * Extracts the import statements (imports) and associates them with processed code context
	 *  
	 * @param astRoot - the root of application AST
	 * @param processedBlock - actually/currently processed block in AST
	 * @param codeContext - the code context to associate import statements (found imports)
	 */
	public static void processImportsInAstPart(JSONObject astRoot, JSONObject processedBlock, CodeContext codeContext) {
		JSONArray statementsList = (JSONArray) processedBlock.get("statements");
		
		if (statementsList != null) {
			for (Object importAst: statementsList) {
				codeContext.addImport((JSONObject) importAst);
			}
		}
	}
}
