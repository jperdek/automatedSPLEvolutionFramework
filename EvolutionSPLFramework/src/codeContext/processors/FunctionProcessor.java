package codeContext.processors;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import codeContext.FunctionContext;
import codeContext.GlobalContext;
import splEvolutionCore.DebugInformation;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;

/**
 * Class managing operations with insertion of the most of function/class function parameters/variables/entities
 * 
 * @author Jakub Perdek
 *
 */
public class FunctionProcessor {

	/**
	 * Inserts function parameters and manages function context with current AST part
	 * -additionally inserts annotations for non-class functions
	 * 
	 * @param astPart - the part of AST where function is processed
	 * @param astRoot - the root of processed AST
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param functionContext - the function context
	 * @param newAstPart - the actually processed AST part from new divisioned AST
	 * @param newAstParent - the parent of actually processed AST part from new divisioned AST
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public static void processFunctionASTContext(JSONObject astPart, JSONObject astRoot, GlobalContext globalContext, FunctionContext functionContext, JSONObject newAstPart, JSONObject newAstParent) throws IOException, InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		String functionParameterName;
		JSONArray parameterObjects;
		JSONObject parameterObject;
		if (astPart.containsKey("parameters") && astPart.containsKey("name")) {
			parameterObjects = (JSONArray) astPart.get("parameters");
			for (Object parameterPart: parameterObjects) {
				parameterObject = (JSONObject) parameterPart;
				functionParameterName = (String) ((JSONObject) parameterObject.get("name")).get("escapedText");
				if (DebugInformation.SHOW_POLLUTING_INFORMATION) { System.out.println("Inserting function parameter: " + functionParameterName); }
				functionContext.addFunctionParameter(parameterObject, astRoot, functionParameterName, false);
			}
		}

		AnnotationInjector.processAnnotationForNotClassFunctionInAstPart(globalContext, functionContext, newAstPart, newAstParent);
	}
}
