package codeContext.processors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import divisioner.VariationPointDivisionConfiguration;


/**
 * Functionality to recognize annotations
 * 
 * @author Jakub Perdek
 *
 */
public class AnnotationProcessor {

	/**
	 * Verifies if particular AST part containing reserved negative variability system annotation from angular SPL
	 * 
	 * @param annotationObject - particular AST part possibly containing the negative variability annotation
	 * @return true if particular AST part containing reserved negative variability system annotation from angular SPL otherwise false
	 */
	public static boolean isAngularVariabilityConfigurationAnnotation(JSONObject annotationObject) {
		String annotationName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(annotationObject);
		for (String variabilityConfigurationAnnotationName: VariationPointDivisionConfiguration.RESERVED_ANGULAR_ANNOTATIONS) {
			if (annotationName.equals(variabilityConfigurationAnnotationName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Verifies if particular AST part containing selected negative variability system annotation according to configuration
	 * 
	 * @param annotationObject - particular AST part possibly containing the negative variability annotation
	 * @return true if particular AST part containing selected negative variability system annotation according to configuration otherwise false
	 */
	public static boolean isVariabilityConfigurationAnnotation(JSONObject annotationObject) {
		String annotationName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(annotationObject);
		for (String variabilityConfigurationAnnotationName: VariationPointDivisionConfiguration.RESERVED_SELECTED_ANNOTATIONS) {
			if (annotationName != null && annotationName.equals(variabilityConfigurationAnnotationName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Verifies if particular AST part containing selected negative variability system annotation according to provided names provided in parameter called variabilityConfigurationAnnotationNames
	 * 
	 * @param annotationObject - particular AST part possibly containing the negative variability annotation
	 * @param variabilityConfigurationAnnotationNames - the provided names of annotations that should be checked with extracted annotation name from annotationObject AST part
	 * @return true if particular AST part containing selected negative variability system annotation according to provided names provided in parameter called variabilityConfigurationAnnotationNames otherwise false
	 */
	public static boolean isVariabilityConfigurationAnnotation(JSONObject annotationObject, String[] variabilityConfigurationAnnotationNames) {
		String annotationName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(annotationObject);
		for (String variabilityConfigurationAnnotationName: variabilityConfigurationAnnotationNames) {
			if (annotationName.equals(variabilityConfigurationAnnotationName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Harvests all negative variability annotations from processed root
	 * -it includes the decorators (modifiers array in AST) and illegal decorators  
	 * 
	 * @param processedBlock - the root (actually processed) AST block with possible negative variability annotations
	 * @return JSON array with found negative variability annotations
	 */
	public static JSONArray getAllAnnotationsFromProcessedBlockRoot(JSONObject processedBlock) {
		JSONArray foundAnnotations = null;
		AnnotationProcessor.getAnnotationsFromArray(processedBlock, "modifiers", foundAnnotations);
		AnnotationProcessor.getAnnotationsFromArray(processedBlock, "illegalDecorators", foundAnnotations);
		return foundAnnotations;
	}
	
	/**
	 * Obtains the annotation from the actually processed element AST
	 * 
	 * @param processedBlock - the actually processed AST block with possible negative variability annotations
	 * @param astArrayName - the name (key) of AST array (can be illegalDecorators or modifiers)
	 * @param foundAnnotations - the JSON array of found annotations that will be extended with new found annotations
	 * @return JSON Array of obtained annotations from the array of annotations
	 */
	public static JSONArray getAnnotationsFromArray(JSONObject processedBlock, String astArrayName, JSONArray foundAnnotations) {
		JSONArray modifiers = (JSONArray) processedBlock.get(astArrayName);
		JSONObject modifierDecorator;
		String modifierName;
		if (modifiers != null) {
			for (Object modifierObject: modifiers) {
				modifierDecorator = (JSONObject) modifierObject;
				modifierName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(modifierDecorator);
				if (AnnotationProcessor.isVariabilityConfigurationAnnotation(modifierDecorator)) {
					if (foundAnnotations == null) { foundAnnotations = new JSONArray(); }
					foundAnnotations.add(modifierDecorator);
				}
			}
		}
		return foundAnnotations;
	}
}
