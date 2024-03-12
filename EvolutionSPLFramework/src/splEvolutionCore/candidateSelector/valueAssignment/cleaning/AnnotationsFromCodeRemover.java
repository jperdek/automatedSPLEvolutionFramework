package splEvolutionCore.candidateSelector.valueAssignment.cleaning;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import codeContext.processors.ASTTextExtractorTools;
import variationPointsVisualization.AnnotationExtensionMarker;


/**
 * Removes the system negative variability annotations from the code
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class AnnotationsFromCodeRemover {

	/**
	 * Instantiates AnnotationsFromCodeRemover
	 */
	public AnnotationsFromCodeRemover() {}
	
	/**
	 * Recursively removes all system negative variability variability annotations from the processed AST
	 * 
	 * @param astRoot - the root of processed AST
	 * @param astPart - the actually processed element in AST
	 */
	public static void removeAllVariabilityAnnotations(JSONObject astRoot, JSONObject astPart) {
		String key;
	
		Object entryValue;
		JSONObject entryJSONObject;
		JSONArray entryArray;
		List<JSONObject> decoratorsToRemove;
		boolean areDecoratorsProcessed;
		for(Object entryKey: astPart.keySet().toArray()) {
			key = (String) entryKey;
			entryValue = astPart.get(key);
			if (entryValue instanceof JSONObject) {
				entryJSONObject = (JSONObject) entryValue;
				AnnotationsFromCodeRemover.removeAllVariabilityAnnotations(astRoot, entryJSONObject);
			} else if(entryValue instanceof JSONArray) {
				entryArray = (JSONArray) entryValue;
				decoratorsToRemove = new ArrayList<JSONObject>();
				areDecoratorsProcessed = key.equals("modifiers") || key.equals("illegalDecorators");
				
				for (int index = entryArray.size() - 1; index >= 0 ; index--) {
					entryJSONObject = (JSONObject) entryArray.get(index);
					AnnotationsFromCodeRemover.removeAllVariabilityAnnotations(astRoot, entryJSONObject);
					if (areDecoratorsProcessed && AnnotationsFromCodeRemover.checkIfIsNegativeVariabilityAnnotation(entryJSONObject)) {
						decoratorsToRemove.add(entryJSONObject);
					}
				}
				for (Object decoratorToRemove: decoratorsToRemove) {
					entryArray.remove((JSONObject) decoratorToRemove);
				}
				if (entryArray.size() == 0) { astPart.remove(key); }
			}
		}
	}
	
	/**
	 * Removes the system negative variability annotations from actually processed JSON array of modifiers or illegal decorators
	 * 
	 * @param entryArray - the processed array of modifiers or illegal decorators in AST as sources of the system negative variability annotations
	 * @param indicesToRemove - the empty list of indices that should be removed from the final processed array of modifiers or illegal decorators in AST 
	 */
	private static void removeFromArray(JSONArray entryArray, List<Integer>  indicesToRemove) {
		JSONObject entryJSONObject;
		for (int index = entryArray.size() - 1; index >= 0 ; index--) {
			entryJSONObject = (JSONObject) entryArray.get(index);
			if (AnnotationsFromCodeRemover.checkIfIsNegativeVariabilityAnnotation(entryJSONObject)) {
				indicesToRemove.add(index);
			}
		}
	}
	
	/**
	 * Removes the system negative variability annotations from actually processed layer
	 * 
	 * @param processedAstPart - the AST of processed layer
	 */
	public static void removeActualLayerVariabilityAnnotations(JSONObject processedAstPart) {
		List<Integer>  indicesToRemove1 = new ArrayList<Integer>();
		JSONArray entryArray1 = (JSONArray) processedAstPart.get("modifiers");
		if (entryArray1 != null) {
			AnnotationsFromCodeRemover.removeFromArray(entryArray1, indicesToRemove1);
			for (Integer indexToRemove: indicesToRemove1) { entryArray1.remove(indexToRemove); }
		}
		List<Integer>  indicesToRemove2 = new ArrayList<Integer>();
		JSONArray entryArray2 = (JSONArray) processedAstPart.get("illegalDecorators");
		if (entryArray2 != null) {
			AnnotationsFromCodeRemover.removeFromArray(entryArray2, indicesToRemove2);
			for (Integer indexToRemove: indicesToRemove2) { entryArray2.remove(indexToRemove); }
		}
	}
	
	/**
	 * Checks/decides if system negative annotation is processed
	 * -positive annotation is (usually - in our case always) only marker
	 * 
	 * @param annotationAst - the AST of annotation (decorator in TypeScript/JavaScript)
	 * @return the AST of annotation (decorator in TypeScript/JavaScript)
	 */
	private static boolean checkIfIsNegativeVariabilityAnnotation(JSONObject annotationAst) {
		String annotationName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(annotationAst);
		return AnnotationExtensionMarker.isSystemAnnotation(annotationName);
	}
}
