package variationPointUpdates;

import org.json.simple.JSONObject;

import codeConstructsEvaluation.transformation.JSONResponseReader;
import divisioner.VariationPointDivisionConfiguration;
import variationPointsVisualization.AnnotationExtensionMarker;


/**
 * Helper to create and customize various annotations from their AST
 * 
 * @author Jakub Perdek
 *
 */
public class AnnotationStubs {

	/**
	 * Creates annotation for global variable represented as AST
	 * -named according to VariationPointDivisionConfiguration.USER_GLOBAL_VARIABLE_DECLARATION_ANNOTATION_NAME
	 * 
	 * @param isUser - if created annotation should belong/be marked and treated as user otherwise will be treated as system
	 * @return annotation for global variable represented as AST
	 */
	public static JSONObject getGlobalVariableAnnotationAst(boolean isUser) {
		String variableDeclarationGlobal;
		if (isUser) { 
			variableDeclarationGlobal = VariationPointDivisionConfiguration.USER_GLOBAL_VARIABLE_DECLARATION_ANNOTATION_NAME;
		} else {
			variableDeclarationGlobal = AnnotationExtensionMarker.SystemAnnotationType.VARIABLE.label + "Global";
		} 
		String userGlobalVariableAnnotation = "{\"pos\":0,\"end\":63,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":33562625,\"kind\":167,\"expression\":{\"pos\":1,\"end\":63,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":210,\"expression\":{\"pos\":1,\"end\":26,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":79,\"escapedText\":\"" + variableDeclarationGlobal + "\"},\"arguments\":[],\"multiLine\":false}]}}";
		return JSONResponseReader.loadJSONConfig(userGlobalVariableAnnotation);
	}
	
	/**
	 * Creates annotation for local variable represented as AST
	 * -named according to VariationPointDivisionConfiguration.USER_LOCAL_VARIABLE_DECLARATION_ANNOTATION_NAME
	 * 
	 * @param isUser - if created annotation should belong/be marked and treated as user otherwise will be treated as system
	 * @return annotation for local variable represented as AST
	 */
	public static JSONObject getLocalVariableAnnotationAst(boolean isUser) {
		String variableDeclarationLocal;
		if (isUser) { 
			variableDeclarationLocal = VariationPointDivisionConfiguration.USER_LOCAL_VARIABLE_DECLARATION_ANNOTATION_NAME;
		} else {
			variableDeclarationLocal = AnnotationExtensionMarker.SystemAnnotationType.VARIABLE.label + "Local";
		} 
		String userLocalVariableAnnotation = "{\"pos\":348,\"end\":413,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":33562625,\"kind\":167,\"expression\":{\"pos\":352,\"end\":413,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":210,\"expression\":{\"pos\":352,\"end\":376,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":79,\"escapedText\":\"" + variableDeclarationLocal + "\"},\"arguments\":[],\"multiLine\":false}]}}";
		return JSONResponseReader.loadJSONConfig(userLocalVariableAnnotation);
	}
	
	/**
	 * Creates annotation for class variable represented as AST
	 * -named according to VariationPointDivisionConfiguration.USER_CLASS_VARIABLE_DECLARATION_ANNOTATION_NAME
	 * 
	 * @param isUser - if created annotation should belong/be marked and treated as user otherwise will be treated as system
	 * @return annotation for class variable represented as AST
	 */
	public static JSONObject getClassVariableAnnotationAst(boolean isUser) {
		String variableDeclarationClass = VariationPointDivisionConfiguration.USER_CLASS_VARIABLE_DECLARATION_ANNOTATION_NAME;
		if (isUser) { 
			variableDeclarationClass = VariationPointDivisionConfiguration.USER_LOCAL_VARIABLE_DECLARATION_ANNOTATION_NAME;
		} else {
			variableDeclarationClass = AnnotationExtensionMarker.SystemAnnotationType.CLASS_VARIABLE.label;
		} 
		String newParameter = "{\"pos\":799,\"end\":855,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":33562625,\"kind\":167,\"expression\":{\"pos\":803,\"end\":855,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":210,\"expression\":{\"pos\":803,\"end\":827,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":79,\"escapedText\":\"" + variableDeclarationClass + "\"},\"arguments\":[],\"multiLine\":false}]}}";
		return JSONResponseReader.loadJSONConfig(newParameter);
	}
	
	/**
	 * Creates annotation for class represented as AST
	 * -named according to VariationPointDivisionConfiguration.USER_CLASS_DECLARATION_ANNOTATION_NAME
	 * 
	 * @param isUser - if created annotation should belong/be marked and treated as user otherwise will be treated as system
	 * @return annotation for class represented as AST
	 */
	public static JSONObject getClassAnnotationAst(boolean isUser) {
		String wholeClass;
		if (isUser) { 
			wholeClass = VariationPointDivisionConfiguration.USER_CLASS_DECLARATION_ANNOTATION_NAME;
		} else {
			wholeClass = AnnotationExtensionMarker.SystemAnnotationType.CLASS.label;
		} 
		String userClassAnnotation = "{\"pos\":744,\"end\":787,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":33562625,\"kind\":167,\"expression\":{\"pos\":749,\"end\":787,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":210,\"expression\":{\"pos\":749,\"end\":759,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":79,\"escapedText\":\"" + wholeClass + "\"},\"arguments\":[],\"multiLine\":false}]}}";
		return JSONResponseReader.loadJSONConfig(userClassAnnotation);
	}
	
	/**
	 * Creates annotation for class method represented as AST
	 * -named according to VariationPointDivisionConfiguration.USER_CLASS_METHOD_ANNOTATION_NAME
	 * 
	 * @param isUser - if created annotation should belong/be marked and treated as user otherwise will be treated as system
	 * @return annotation for class method represented as AST
	 */
	public static JSONObject getClassMethodAnnotationAst(boolean isUser) {
		String wholeClassMethod;
		if (isUser) { 
			wholeClassMethod = VariationPointDivisionConfiguration.USER_CLASS_METHOD_ANNOTATION_NAME;
		} else {
			wholeClassMethod = AnnotationExtensionMarker.SystemAnnotationType.CLASS_FUNCTION.label;
		} 
		String userClassMethodAnnotation = "{\"pos\":228,\"end\":277,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":33562625,\"kind\":167,\"expression\":{\"pos\":233,\"end\":277,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":210,\"expression\":{\"pos\":233,\"end\":249,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":79,\"escapedText\":\"" + wholeClassMethod + "\"},\"arguments\":[{\"pos\":250,\"end\":276,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":207,\"properties\":[],\"multiLine\":false}]}}";
		return JSONResponseReader.loadJSONConfig(userClassMethodAnnotation);
	}
	
	/**
	 * Creates annotation for non-class method represented as AST
	 * -named according to VariationPointDivisionConfiguration.USER_METHOD_ANNOTATION_NAME
	 * 
	 * @param isUser - if created annotation should belong/be marked and treated as user otherwise will be treated as system
	 * @return annotation for non-class method represented as AST
	 */
	public static JSONObject getMethodAnnotationAst(boolean isUser) {
		String wholeBlockMethod;
		if (isUser) { 
			wholeBlockMethod = VariationPointDivisionConfiguration.USER_METHOD_ANNOTATION_NAME;
		} else {
			wholeBlockMethod = VariationPointDivisionConfiguration.ANNOTATION_OF_VP + AnnotationExtensionMarker.SystemAnnotationType.FUNCTION;
		} 
		String userMethodAnnotation = "{\"pos\":91,\"end\":140,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":33562625,\"kind\":167,\"expression\":{\"pos\":96,\"end\":140,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":210,\"expression\":{\"pos\":96,\"end\":112,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":79,\"escapedText\":\"" + wholeBlockMethod + "\"},\"arguments\":[{\"pos\":113,\"end\":139,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":207,\"properties\":[],\"multiLine\":false}]}}";
		return JSONResponseReader.loadJSONConfig(userMethodAnnotation);
	}
	
	/**
	 * Creates annotation for function parameter represented as AST
	 * -named according to VariationPointDivisionConfiguration.USER_VARIABLE_PARAMETER
	 * 
	 * @param isUser - if created annotation should belong/be marked and treated as user otherwise will be treated as system
	 * @return annotation for function parameter represented as AST
	 */
	public static JSONObject getParameterAnnotationAst(boolean isUser) {
		String variableParameter;
		if (isUser) { 
			variableParameter = VariationPointDivisionConfiguration.USER_VARIABLE_PARAMETER;
		} else {
			variableParameter = AnnotationExtensionMarker.SystemAnnotationType.PARAMETER.label;
		} 
		String userMethodAnnotation = "{\"pos\":1040,\"end\":1086,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":33562625,\"kind\":167,\"expression\":{\"pos\":1041,\"end\":1086,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":210,\"expression\":{\"pos\":1041,\"end\":1058,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":79,\"escapedText\":\"" + variableParameter + "\"},\"arguments\":[],\"multiLine\":false}]}}";
		return JSONResponseReader.loadJSONConfig(userMethodAnnotation);
	}
	
	/**
	 * Creates annotation for constructor parameter represented as AST
	 * -named according to VariationPointDivisionConfiguration.USER_VARIABLE_CONSTRUCTOR_PARAMETER
	 * 
	 * @param isUser - if created annotation should belong/be marked and treated as user otherwise will be treated as system
	 * @return annotation for constructor parameter represented as AST
	 */
	public static JSONObject getConstructorParameterAnnotationAst(boolean isUser) {
		String variableConstructorParameter;
		if (isUser) { 
			variableConstructorParameter = VariationPointDivisionConfiguration.USER_VARIABLE_CONSTRUCTOR_PARAMETER;
		} else {
			variableConstructorParameter = AnnotationExtensionMarker.SystemAnnotationType.CONSTRUCTOR_PARAMETER.label;
		} 
		String userMethodAnnotation = "{\"pos\":940,\"end\":997,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":33562625,\"kind\":167,\"expression\":{\"pos\":941,\"end\":997,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":210,\"expression\":{\"pos\":941,\"end\":969,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":79,\"escapedText\":\"" + variableConstructorParameter + "\"},\"arguments\":[],\"multiLine\":false}]}}";
		return JSONResponseReader.loadJSONConfig(userMethodAnnotation);
	}
}
