 package variationPointsVisualization;

import org.json.simple.JSONObject;
import codeConstructsEvaluation.transformation.JSONResponseReader;
import divisioner.VariationPointDivisionConfiguration;


/**
 * Manages creations and recognitions of system annotations for negative variability
 * 
 * @author Jakub Perdek
 *
 */
public class AnnotationExtensionMarker {
	
	private static int usedIndexInner = 1;
	
	/**
	 * Enumeration with different system negative variability annotation types
	 * -descriptive names that are used in final SPL are associated to them
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum SystemAnnotationType {
		VARIABLE("variableVP"),
		FUNCTION("functionVP"),
		PARAMETER("parameterVP"),
		CONSTRUCTOR_PARAMETER("constructorParameterVP"),
		CLASS_FUNCTION("classFunctionVP"),
		CLASS_VARIABLE("classVariableVP"),
		CLASS("classVP");
		
		/**
		 * name of given system annotation that is used in final/specific SPL
		 */
		public final String label;

		/**
		 * Creates system annotation type
		 * 
		 * @param label - name of given system annotation that is used in final/specific SPL
		 */
	    private SystemAnnotationType(String label) {
	        this.label = label;
	    }
	    
	    /**
	     * Returns name of system annotation
	     * 
	     * @return name of system annotation
	     */
	    public String getName() { return this.label; }
	}
	
	/**
	 * Observes if annotation is system one - matches with one name from SystemAnnotationType enumeration
	 * 
	 * @param analyzedAnnotation - analyzed annotation name
	 * @return true if annotation is system annotation
	 */
	public static boolean isSystemAnnotation(String analyzedAnnotation) {
		if(analyzedAnnotation == null) { return false; }
		if (analyzedAnnotation.contains(SystemAnnotationType.VARIABLE.label)) {
			return true;
		} else if (analyzedAnnotation.contains(SystemAnnotationType.FUNCTION.label)) {
			return true;
		} else if (analyzedAnnotation.contains(SystemAnnotationType.PARAMETER.label)) {
			return true;
		} else if (analyzedAnnotation.contains(SystemAnnotationType.CONSTRUCTOR_PARAMETER.label)) {
			return true;
		} else if (analyzedAnnotation.contains(SystemAnnotationType.CLASS_FUNCTION.label)) {
			return true;
		} else if (analyzedAnnotation.contains(SystemAnnotationType.CLASS_VARIABLE.label)) {
			return true;
		} else if (analyzedAnnotation.contains(SystemAnnotationType.CLASS.label)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Creates system annotation named according to its type and configured variable on path in AST 
	 * -as expression is used VariationPointDivisionConfiguration.ANNOTATION_OF_VP
	 * -as name is used given SystemAnnotationType
	 * 
	 * @param expressionName - type of expression as system annotation that shhould be created
	 * @return AST of system negative variability annotation
	 */
	public static JSONObject generateMarkerForVariableInAst(SystemAnnotationType expressionName) {
		String annotationName = VariationPointDivisionConfiguration.ANNOTATION_OF_VP + AnnotationExtensionMarker.usedIndexInner;
		AnnotationExtensionMarker.usedIndexInner = AnnotationExtensionMarker.usedIndexInner + 1;
		String expressionNameStr = expressionName.label;
		String newAnnotation = "{\"pos\":0,\"end\":18,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":33562625,\"kind\":167,\"expression\":{\"pos\":1,\"end\":18,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":210,\"expression\":{\"pos\":1,\"end\":16,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":208,\"expression\":{\"pos\":1,\"end\":4,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":79,\"originalKeywordKind\":145,\"escapedText\":\"" + annotationName + "\"},\"name\":{\"pos\":5,\"end\":16,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":79,\"escapedText\":\"" + expressionNameStr + "\"}},\"arguments\":[]}}";
		return JSONResponseReader.loadJSONConfig(newAnnotation);
	}
	
	/**
	 * Creates system annotation named according to its name and expression name on path in AST 
	 * 
	 * @param annotationName - name of annotation
	 * @param expressionName - expression with name on annotation path
	 * @return AST of custom (negative variability) annotation
	 */
	public static JSONObject generateMarkerForVariableInAst(String annotationName, String expressionName) {
		String newAnnotation = "{\"pos\":0,\"end\":18,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":33562625,\"kind\":167,\"expression\":{\"pos\":1,\"end\":18,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":210,\"expression\":{\"pos\":1,\"end\":16,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":208,\"expression\":{\"pos\":1,\"end\":4,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":79,\"originalKeywordKind\":145,\"escapedText\":\"" + annotationName + "\"},\"name\":{\"pos\":5,\"end\":16,\"flags\":16384,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":79,\"escapedText\":\"" + annotationName + "\"}},\"arguments\":[]}}";
		return JSONResponseReader.loadJSONConfig(newAnnotation);
	}
}
