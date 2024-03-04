package codeContext.processors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import codeContext.CodeContext;
import codeContext.GlobalContext;
import codeContext.InnerContext;
import codeContext.processors.export.ExportsProcessor;


/**
 * Functionality to process variable declarations in AST part and inserts it to given context
 * -the responsibility to process it correctly are propagated to UsedVariables entities
 * 
 * @author Jakub Perdek
 *
 */
public class VariableProcessor {

	/**
	 * Processes variable declarations in AST part and inserts it to given context
	 * - the responsibility to process it correctly are propagated to UsedVariables entities
	 * - checks if "declarationList" is inside processed block and loads variables
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param innerContext - inner context - the context known as local context, context inside functions, classes
	 * @param astRoot - the root of application AST
	 * @param processedBlock - actually/currently processed block in AST 
	 * @throws NotFoundVariableDeclaration
	 */
	public static void processVariablesInAstPart(GlobalContext globalContext, InnerContext innerContext, 
			JSONObject astRoot, JSONObject processedBlock) throws NotFoundVariableDeclaration {
		JSONObject declarationList = (JSONObject) processedBlock.get("declarationList");
		
		String contentText;
		String declarationName;
		boolean isDirectlyExported;
		if (declarationList != null) {
			contentText = (String) astRoot.get("text");
			for (Object declarationAst: (JSONArray) declarationList.get("declarations")) {
				JSONObject textObject = ((JSONObject) ((JSONObject) declarationAst).get("name"));
				if (textObject.containsKey("escapedText")) {
					declarationName = (String) textObject.get("escapedText");
				} else {
					declarationName = (String) textObject.get("text");
				}
				isDirectlyExported = ExportsProcessor.isConstructMarkedAsDirectExport(processedBlock);
				CodeContext.addVariable(globalContext, innerContext, astRoot, (JSONObject) declarationAst, declarationName, contentText, isDirectlyExported);
			}
		}
	}
	
}
