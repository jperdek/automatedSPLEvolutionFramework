package dataRepresentationsExtensions.stack;


import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import astFileProcessor.ASTLoader;
import codeConstructsEvaluation.transformation.ASTConverterClient;
import dataRepresentationsExtensions.DefaultDataRepresentations;


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
		if (className != null && !className.strip().equals("") && !className.contains("null")) { functionNameMap = functionNameMap.strip() + " + \" \" + \"" + className + "\" "; }
		String classNameMap = "\"className\": \"" + className + "\"";
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
			astPop = ASTConverterClient.getFirstStatementFromASTFile(ASTConverterClient.convertFromCodeToASTJSON(functionPop));
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
	 * Creates JavaScript helper construct to harvest created graph representation if getting its hierarchical form fails
	 * 
	 * @return the JavaScript helper construct to harvest created graph representation if getting its hierarchical form fail
	 */
	public static String constructDataHarvesterForStack() {
		return String.join("\n",
				"",
				"function createJSON(iteratedObject, flattened) {",
				"	let identifier = 0;",
				"	let createdObjectId = identifier;",
				"	iteratedObject[\"mergeId\"] = createdObjectId.toString();",
				"",
				"	let stackForJSON = [];",
				"	stackForJSON.push(iteratedObject);",
				"	while(stackForJSON.length !== 0) {",
				"		let processedObject = stackForJSON.pop();",
				"		let newRecord = {\"pointsTo\": [], \"mergeId\": processedObject[\"mergeId\"]};",
				"",
				"		for (let processedObjectField in processedObject){",
				"			if(processedObject.hasOwnProperty( processedObjectField ) ){",
				"				let processedObjectFieldPart = processedObject[processedObjectField];",
				"				if (processedObjectField === \"pointsTo\") {",
				"					for (let innerObjectIndex = 0;  innerObjectIndex< processedObject[processedObjectField].length; innerObjectIndex++) {",
				"						let newChildInstance = processedObjectFieldPart[innerObjectIndex];",
				"						identifier = identifier + 1;",
				"						createdObjectId = identifier;",
				"						newChildInstance[\"mergeId\"] = createdObjectId.toString();",
				"",
				"						newRecord[\"pointsTo\"].push(createdObjectId.toString());",
				"						stackForJSON.push(newChildInstance);",
				"					}",
				"				} else if (typeof(name) === \"object\") {",
				"					newRecord[processedObjectField] = JSON.parse(JSON.stringify(processedObjectFieldPart));",
				"				} else {",
				"					newRecord[processedObjectField] = processedObjectFieldPart;",
				"				}",
				"			}",
				"		}",
				"		flattened.push(newRecord);",
				"	}",
				"}",
				"",
				"function reorderMergeIdsToArray(flattenedArray) {",
				"	let helperVariable;",
				"	for (let i = 0;  i< flattenedArray.length; i++) {",
				"		do {",
				"			helperVariable = flattenedArray[i];",
				"			mergedIndex = parseInt(helperVariable[\"mergeId\"]);",
				"			flattenedArray[i] = flattenedArray[mergedIndex];",
				"			flattenedArray[mergedIndex] = helperVariable;",
				"		} while (mergedIndex != i);",
				"	}",
				"}",
				"",
				"function harvestStackData() {",
				"	let flattened = [];",
				"	createJSON(initialGraphRoot, flattened);",
				"	reorderMergeIdsToArray(flattened);",
				"	return flattened;",
				"}");
		
	}

	/**
	 * Initializes the stack stub on base evolved AST of particular derived product
	 * -helper functionality to extract deeply nested data is optionally inserted according to configuration
	 * 
	 * @param astRoot - the root of the base updated AST of final product script
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void initializeStackStubOnAst(JSONObject astRoot) throws IOException, InterruptedException {
		JSONObject stackStubAst = (JSONObject) ASTConverterClient.convertFromCodeToASTJSON(FunctionStackLogger.constructStackStub()).get("ast");
		JSONArray stackStubStatements = (JSONArray) stackStubAst.get("statements");
		if (DefaultDataRepresentations.INSERTS_HELPER_FUNCTIONALITY_TO_HARVEST_DATA_FROM_STACK) {
			JSONObject dataHarvesterForStack = (JSONObject) ASTConverterClient.convertFromCodeToASTJSON(
					FunctionStackLogger.constructDataHarvesterForStack()).get("ast");
			JSONArray dataHarvesterForStackStatements = (JSONArray) dataHarvesterForStack.get("statements");
			stackStubStatements.addAll(dataHarvesterForStackStatements);
		}
		
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
