package dataRepresentationsExtensions.stack;


import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import astFileProcessor.ASTLoader;
import codeConstructsEvaluation.transformation.ASTConverterClient;


/**
 * Functionality to inject stack into existing functionality/code and harvest all possible entity calls during run-time
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class FunctionStackLogger {

	/**
	 * Initializes stack logger
	 */
	public FunctionStackLogger() {}

	
	/**
	 * Creates stack logger functionality to manage injection of the stack functionality into code
	 * 
	 * @param className - the name of the class
	 * @param additionalObjectString - the comma delimited additional data in JSON key value format, especially function parameters
	 * @param statementsArray - actual statements array where stack functionality should be pushed as start of the method or constructor
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void createReferencedInstance(String className, String additionalObjectString, JSONArray statementsArray) throws IOException, InterruptedException {
		String functionNameMap = "\"fname\": arguments.callee.toString().substring(0, arguments.callee.toString().split(\"(\")[0].length) ";
		String classNameMap = "\"fname\": \"" + className + "\"";
		String parameterObject = " { " + functionNameMap + ", " + classNameMap + ", " + additionalObjectString + " } "; //to enable parse JavaScript call to be executed as JavaScript object
		this.createStackFunctionality(parameterObject, statementsArray);
	}
	
	/**
	 * Creates stack logger functionality to manage injection of the stack functionality into code
	 * 
	 * @param insertedObjectDefinition - the comma delimited additional data in JSON key value format, especially function parameters 
	 * @param statementsArray - actual statements array where stack functionality should be pushed as start of the method or constructor
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void createStackFunctionality(String insertedObjectDefinition, JSONArray statementsArray) throws IOException, InterruptedException {
		String functionPush = "pushData(" + insertedObjectDefinition + ")";
		JSONObject astPush = ASTConverterClient.getFirstStatementFromASTFile(ASTConverterClient.convertFromCodeToASTJSON(functionPush));
		statementsArray.add(0, astPush); //put to function start
		
		//AstRoot astPush = new Parser().parse(functionPush, null, 1);
		JSONObject astPop = ASTLoader.loadASTFromString("{\"ast\":{\"pos\":0,\"end\":10,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":308,\"statements\":[{\"pos\":0,\"end\":10,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":241,\"expression\":{\"pos\":0,\"end\":9,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":210,\"expression\":{\"pos\":0,\"end\":7,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":79,\"escapedText\":\"popData\"},\"arguments\":[]}}],\"endOfFileToken\":{\"pos\":10,\"end\":10,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":1},\"fileName\":\"x.ts\",\"text\":\"popData();\",\"languageVersion\":99,\"languageVariant\":0,\"scriptKind\":3,\"isDeclarationFile\":false,\"hasNoDefaultLib\":false,\"bindDiagnostics\":[],\"pragmas\":{},\"referencedFiles\":[],\"typeReferenceDirectives\":[],\"libReferenceDirectives\":[],\"amdDependencies\":[],\"nodeCount\":5,\"identifierCount\":1,\"identifiers\":{},\"parseDiagnostics\":[]}}");
		if (astPop != null) { // the same, but code should be converted into AST first
			String functionPop = "popData();";
			astPop = ASTConverterClient.getFirstStatementFromASTFile(ASTConverterClient.convertFromCodeToASTJSON(functionPush));
		}
		statementsArray.add(statementsArray.size(), astPop); //put to the function end
	}
	
	/**
	 * Creates JavaScript stack stub for creating graph representation
	 * 
	 * @return the JavaScript stack stub for creating graph representation
	 */
	public static String constructStackStub() {
		return String.join("\n",
						   "var stackStub = [];",
						   "var initialGraphRoot = {\"pointsTo\": [] }",
						   "function pushData(insertedObject) {",
						   " 	if (stackStub.length === 0) {  ",
						   "		if (initialGraphRoot[\"pointsTo\"] === undefined) { initialGraphRoot[\"pointsTo\"] = []; } ",
						   "		initialGraphRoot[\"pointsTo\"].push(insertedObject);",
						   " 	} else  { ",
						   "		if (stackStub[stackStub.length - 1][\"pointsTo\"] === undefined) { stackStub[stackStub.length - 1][\"pointsTo\"] = []; } ",
						   "    	stackStub[stackStub.length - 1][\"pointsTo\"].push(insertedObject); ",
						   "	} ",
						   "	stackStub.push(insertedObject);",
						   "}",
						   "function popData() {",
						   "	if( stackStub.length > 0) { ",
						   "    	stackStub.pop(); ",
						   "	}",
						   "}"
						   );
		
	}
	
	/**
	 * Initializes the stack stub on base evolved AST of particular derived product
	 * 
	 * @param astRoot - the root of the base updated AST of final product script
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void initializeStackStubOnAst(JSONObject astRoot) throws IOException, InterruptedException {
		JSONObject stackStubAst = (JSONObject) ASTConverterClient.convertFromCodeToASTJSON(FunctionStackLogger.constructStackStub()).get("ast");
		JSONArray stackStubStatements = (JSONArray) stackStubAst.get("statements");
		
		int indexToPlace = 0;
		JSONArray baseSplStatements = (JSONArray) astRoot.get("statements");
		for (Object baseAstStatementObject: baseSplStatements) {
			if (!((JSONObject) baseAstStatementObject).containsKey("importClause")) {
				break;
			}
			indexToPlace++;
		}
		if (baseSplStatements.size() == indexToPlace) { indexToPlace--; }
		
		baseSplStatements.addAll(indexToPlace, stackStubStatements);
	}
}
