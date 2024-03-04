package dividedAstExport;


import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import astFileProcessor.ASTLoader;
import codeConstructsEvaluation.transformation.ASTConverterClient;


/**
 * Manages conversions of configuration expressions in JSON into AST and back  
 * -other extraction operations are included
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class ExpressionConverter {

	/**
	 * Extracts configuration expression from TypeScript decorator in form of AST
	 * 
	 * @param decoratorAst - TypeScript decorator represented in AST (in form of AST)
	 * @return configuration expression in JSON
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static JSONObject extractExpressionFromDecoratorAst(JSONObject decoratorAst) throws IOException, InterruptedException {
		return ExpressionConverter.extractExpressionFromDecoratorAst(decoratorAst, 0);
	}
	
	/**
	 * Extracts configuration expression from decorator in AST and converts it into JSON
	 * 
	 * @param decoratorAst - TypeScript decorator represented in AST (in form of AST)
	 * @param expressionPosition - the start occurrence position in AST
	 * @return extracted configuration expression in JSON
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static JSONObject extractExpressionFromDecoratorAst(JSONObject decoratorAst, int expressionPosition) throws IOException, InterruptedException {
		JSONArray decoratorArguments = (JSONArray) ((JSONObject) decoratorAst.get("expression")).get("arguments");
		if (decoratorArguments == null || decoratorArguments.size() <= expressionPosition) {
			return null;
		}
		JSONObject expressionAst = (JSONObject) decoratorArguments.get(expressionPosition);
		JSONObject jsonExpressionAst = (JSONObject) expressionAst.get("initializer");

		//HANDLING UNASIGNED JSON PART
		if (jsonExpressionAst == null) { 
			try {
				jsonExpressionAst = (JSONObject) new JSONParser().parse("{\"pos\": 27, \"kind\": 207, \"multiLine\": false, \"flags\": 0, \"modifierFlagsCache\": 0, \"end\": 62, \"properties\": [], \"transformFlags\": 0}");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			jsonExpressionAst.put("properties", (JSONArray) expressionAst.get("properties"));
		}
		JSONObject jsonExpression = ExpressionConverter.getJSONObjectFromVariable(jsonExpressionAst);
		return jsonExpression;
	}

	/**
	 * Converts configuration expression in AST part representing argument of particular functionality into JSON
	 * 
	 * @param expressionAsArgument - configuration expression in AST part representing argument of particular functionality
	 * @return configuration expression in JSON format
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static JSONObject getJSONObjectFromVariable(JSONObject expressionAsArgument) throws IOException, InterruptedException {
		String initializedExpressionCodeConstruct = "var variableE = {};";
		JSONObject defaultClassAst = (JSONObject) ASTConverterClient.convertFromCodeToASTJSON(initializedExpressionCodeConstruct).get("ast");
		((JSONObject) ((JSONArray) ((JSONObject) ((JSONObject) ((JSONArray) defaultClassAst.get(
				"statements")).get(0)).get("declarationList")).get("declarations")).get(0)).put("initializer", expressionAsArgument);
		String codeWithVariableExpression = ASTConverterClient.convertFromASTToCode(defaultClassAst.toString());
		codeWithVariableExpression = codeWithVariableExpression.strip();
		if (codeWithVariableExpression.endsWith(";")) { codeWithVariableExpression = codeWithVariableExpression.substring(0, codeWithVariableExpression.length() - 1); }
		return ASTLoader.loadASTFromString(codeWithVariableExpression.substring(codeWithVariableExpression.indexOf('=') + 1));
	}
	
	/**
	 * Converts configuration expression in JSON into AST (AST is also in JSON)
	 * 
	 * @param expressionJSON - configuration expression in JSON format
	 * @return extracted configuration expression in AST (AST is also in JSON)
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static JSONObject getJSONObjectFromConfigurationExpressionAst(JSONObject expressionJSON) throws IOException, InterruptedException {
		String initializedExpressionCodeConstruct = "var variableE = " + expressionJSON.toString() + ";";
		JSONObject defaultClassAst = (JSONObject) ASTConverterClient.convertFromCodeToASTJSON(initializedExpressionCodeConstruct).get("ast");
		JSONObject configurationExpressionAst = (JSONObject) ((JSONObject) ((JSONArray) ((JSONObject) ((JSONObject) ((JSONArray) defaultClassAst.get(
				"statements")).get(0)).get("declarationList")).get("declarations")).get(0)).get("initializer");
		return configurationExpressionAst;
	}
}
