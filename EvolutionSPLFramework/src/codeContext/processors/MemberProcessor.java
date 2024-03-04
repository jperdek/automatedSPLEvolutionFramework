package codeContext.processors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import codeContext.ClassContext;
import codeContext.processors.export.ExportsProcessor;


/**
 * Functionality to process content inside the list of members
 * 
 * @author Jakub Perdek
 *
 */
public class MemberProcessor {

	/**
	 * Extracts the class variables from the members of the processed block and associates it with provided class context
	 * 
	 * @param classContext - the class context to associate class variables (found members)
	 * @param processedBlock - actually/currently processed block in AST
	 * @param astRoot - the root of application AST
	 */
	public static void processMembersInAstPart(ClassContext classContext, JSONObject processedBlock, JSONObject astRoot) {
		JSONArray parametersList = (JSONArray) processedBlock.get("members");

		String declarationName;
		boolean isDirectlyExported;
		if (parametersList != null) {
			isDirectlyExported = ExportsProcessor.isConstructMarkedAsDirectExport(processedBlock);
			for (Object declarationAst: parametersList) {
				JSONObject textObject = ((JSONObject) ((JSONObject) declarationAst).get("name"));
				if (textObject == null) { continue; }
				if (textObject.containsKey("escapedText")) {
					declarationName = (String) textObject.get("escapedText");
				} else {
					declarationName = (String) textObject.get("text");
				}
				classContext.addMember((JSONObject) declarationAst, astRoot, declarationName, isDirectlyExported);
			}
		}
	}
	
	/**
	 * Extracts the class variable directly from the processed block and associates it with provided class context
	 * 
	 * @param classContext - the class context to associate class variables (found members)
	 * @param processedBlock - actually/currently processed block in AST
	 * @param astRoot - the root of application AST
	 * @param notFunctions - true if functions should be omitted otherwise not
	 */
	public static void processActualMemberInAstPart(ClassContext classContext, JSONObject processedBlock, JSONObject astRoot, boolean notFunctions) {
		String declarationName;
		if (notFunctions && ((JSONObject) processedBlock).containsKey("body")) { return; } //only in case of members
		
		JSONObject textObject = ((JSONObject) ((JSONObject) processedBlock).get("name"));
		boolean isDirectlyExported = ExportsProcessor.isConstructMarkedAsDirectExport(processedBlock);
		if (textObject == null) { 
			declarationName = "constructor"; 
		} else if (textObject.containsKey("escapedText")) {
			declarationName = (String) textObject.get("escapedText");
		} else {
			declarationName = (String) textObject.get("text");
		}
		classContext.addMember((JSONObject) processedBlock, astRoot, declarationName, isDirectlyExported);
		
	}
	
	/**
	 * Extracts the class functions from the members of the processed block and associates it later with provided class context
	 * 
	 * @param classContext - the class context to associate class functions (found members)
	 * @param processedBlock - actually/currently processed block in AST
	 * @param astRoot - the root of application AST 
	 */
	public static void processMembersFunctionsInAstPart(ClassContext classContext, JSONObject processedBlock, JSONObject astRoot) {
		JSONArray parametersList = (JSONArray) processedBlock.get("members");

		if (parametersList != null) {
			for (Object declarationAst: parametersList) {
				MemberProcessor.processActualMemberFunctionInAstPartWithPrioritization(
						classContext, (JSONObject) declarationAst, astRoot);
			}
		}
	}
	
	/**
	 * Extracts the class functions directly from the processed block and associates it with provided class context
	 * 
	 * @param classContext - the class context to associate class functions (found members)
	 * @param processedBlock - actually/currently processed block in AST
	 * @param astRoot - the root of application AST
	 */
	public static void processActualMemberFunctionInAstPartWithPrioritization(ClassContext classContext, JSONObject processedBlock, JSONObject astRoot) {
		String declarationName;
		if (((JSONObject) processedBlock).containsKey("body")) { // resolve class method
			JSONObject textObject = ((JSONObject) ((JSONObject) processedBlock).get("name"));
			boolean isDirectlyExported = ExportsProcessor.isConstructMarkedAsDirectExport(processedBlock);
			if (textObject == null) { 
				declarationName = "constructor"; 
			} else if (textObject.containsKey("escapedText")) {
				declarationName = (String) textObject.get("escapedText");
			} else {
				declarationName = (String) textObject.get("text");
			}
			classContext.addMemberWithPrioritization((JSONObject) processedBlock, astRoot, declarationName, isDirectlyExported);
		}
	}
}
