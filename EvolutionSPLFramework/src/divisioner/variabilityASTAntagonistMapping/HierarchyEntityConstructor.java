package divisioner.variabilityASTAntagonistMapping;

import org.json.simple.JSONObject;

/**
 * Creates string label with entities in hierarchy strictly recognizable
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class HierarchyEntityConstructor {

	/**
	 * Creates a label for particular important entity in AST if such entity is found otherwise returns empty string
	 * Supported entities: class and function
	 * 
	 * @param hierarchyName - the previously created label during hierarchy creation otherwise empty string should be used
	 * @param processedAstPart - the part of AST that is currently processed
	 * @return label for particular important entity in AST if such entity is found otherwise returns empty string
	 */
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
	
	/**
	 * Creates and returns the string to express the class used as label
	 * 
	 * @param className - the class name used in class label to express the class
	 * @return the string to express the class used as label
	 */
	public static String createClassLabel(String className) { return "[C|" + className + "]"; }
	
	/**
	 * Creates and returns the string to express the function used as label
	 * 
	 * @param functionName - the function name used in function label to express the function
	 * @return the string to express the function used as label
	 */
	public static String createFunctionLabel(String functionName) { return "[F|" + functionName + "]"; }
	
	/**
	 * Creates and returns the class label if such entity is currently found and can be processed from the AST
	 * 
	 * @param processedAstPart - the part of AST holding class to extract particular information about it and to create class label
	 * @return the created class label if such entity is currently found and can be processed from the AST
	 */
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
	
	/**
	 * Creates and returns the function label if such entity is currently found and can be processed from the AST
	 * 
	 * @param processedAstPart - the part of AST holding function to extract particular information about it and to create function label 
	 * @return the created function label if such entity is currently found and can be processed from the AST
	 */
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
