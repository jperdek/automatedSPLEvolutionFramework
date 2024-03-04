package codeContext.processors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import codeContext.InnerContext;


/**
 * Functionality to harvest parameters from the AST part and inserts it to given inner context
 * 
 * @author Jakub Perdek
 *
 */
public class ParameterProcessor {

	/**
	 * Harvests parameters from AST part and inserts it to given inner context
	 * 
	 * @param innerContext - inner context - the context known as local context, context inside functions, classes
	 * @param processedBlock - actually/currently processed block in AST 
	 * @param astRoot - the root of application AST
	 */
	public static void processVariablesInAstPart(InnerContext innerContext, JSONObject processedBlock, JSONObject astRoot) {
		JSONArray parametersList = (JSONArray) processedBlock.get("parameters");
		
		String declarationName;
		if (parametersList != null) {
			for (Object declarationAst: parametersList) {
				JSONObject textObject = ((JSONObject) ((JSONObject) declarationAst).get("name"));
				if (textObject.containsKey("escapedText")) {
					declarationName = (String) textObject.get("escapedText");
				} else {
					declarationName = (String) textObject.get("text");
				}
				innerContext.addParameter((JSONObject) declarationAst, astRoot, declarationName);
			}
		}
	}
}
