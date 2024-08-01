package divisioner.variabilityASTAntagonistMapping;

import org.json.simple.JSONObject;

/**
 * Creates string label with entities in hierarchy strictly recognizable
 * 
 * @author Jakub Perdek
 *
 */
public class HierarchyEntityConstructor {

	public static String createLabelForParticularEntityInAST(String hierarchyName, JSONObject processedAstPart) {
		String resultingLabel = "";
		//CLASS
		if (processedAstPart.containsKey("members") && processedAstPart.containsKey("name")) {
			resultingLabel = hierarchyName + HierarchyEntityConstructor.createClassLabel(processedAstPart);
			return resultingLabel;
		}
		//FUNCTION
		if (processedAstPart.containsKey("parameters") && processedAstPart.containsKey("body") && processedAstPart.containsKey("name")) {
			resultingLabel = hierarchyName + HierarchyEntityConstructor.createFunctionLabel(processedAstPart);
			return resultingLabel;
		}
		return hierarchyName + resultingLabel;
	}
	
	public static String createClassLabel(String className) { return "[C|" + className + "]"; }
	
	public static String createFunctionLabel(String functionName) { return "[F|" + functionName + "]"; }
	
	private static String createClassLabel(JSONObject processedAstPart) {
		String className;
		JSONObject textObject = ((JSONObject) ((JSONObject)  processedAstPart).get("name"));
		if (textObject.containsKey("escapedText")) {
			className = (String) textObject.get("escapedText");
		} else {
			className = (String) textObject.get("text");
		}
		return HierarchyEntityConstructor.createClassLabel(className);
	}
	
	private static String createFunctionLabel(JSONObject processedAstPart) {
		String functionName;
		JSONObject textObject = ((JSONObject) ((JSONObject)  processedAstPart).get("name"));
		if (textObject.containsKey("escapedText")) {
			functionName = (String) textObject.get("escapedText");
		} else {
			functionName = (String) textObject.get("text");
		}
		return HierarchyEntityConstructor.createFunctionLabel(functionName);
	}
}
