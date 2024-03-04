package variationPointsVisualization;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import codeContext.processors.ASTTextExtractorTools;
import divisioner.VariationPointDivisionConfiguration;
import variationPointsVisualization.AnnotationExtensionMarker.SystemAnnotationType;


/**
 * Aggregator of negative variability annotation
 * 
 * @author Jakub Perdek
 *
 */
public class NegativeVariabilityAnnotationAggregator {

	/**
	 * The captured negative variability system type
	 */
	private String capturedNegativeVariabilitySystemType = null;
	
	/**
	 * The aggregated user annotations
	 */
	private Set<String> userAnnotations;
	
	/**
	 * The aggregated system annotations
	 */
	private Set<String> systemAnnotations;
	
	/**
	 * Creates NegativeVariabilityAnnotationAggregator
	 */
	public NegativeVariabilityAnnotationAggregator() {
		this.userAnnotations = new HashSet<String>();
		this.systemAnnotations = new HashSet<String>();
	}
	
	/**
	 * Aggregates negative variability decorators
	 * -skips on empty annotation
	 * -aggregates user annotation if it is one of VariationPointDivisionConfiguration.RESERVED_SELECTED_ANNOTATIONS
	 * -aggregates system annotation if belongs to them
	 * -removes user annotations from system ones
	 * 
	 * @param usedDecorators - list of decorators that are decorating given code construct
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public void aggregateNegativeVariabilityDecorators(JSONArray usedDecorators) 
			throws IOException, InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		JSONObject usedDecoratorAst;
		String decoratorName;
		
		NegativeVariabilityAnnotationElement negativeVariabilityElement;
		String expressionJSONInStr;
	
		for (Object usedDecoratorObject: usedDecorators) {
			usedDecoratorAst = (JSONObject) usedDecoratorObject;
			decoratorName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(usedDecoratorAst);
			if(decoratorName == null) { continue; } // empty annotation
			SystemAnnotationType expressionName = NegativeVariabilityDecoratorConsistenceVerifier.classifyNegativeVariabilitySystemAnnotation(decoratorName);
			if (expressionName != null) { 			// negative variability system annotation
				if (this.capturedNegativeVariabilitySystemType == null) {
					this.capturedNegativeVariabilitySystemType = expressionName.label;
				} else if (!this.capturedNegativeVariabilitySystemType.equals(expressionName.label)) {
					throw new DifferentAnnotationTypesOnTheSameVariationPoint("Different annotation type: " + 
							expressionName.label + " on the annotation named: " + decoratorName);
				}
				negativeVariabilityElement = new NegativeVariabilityAnnotationElement(decoratorName, usedDecoratorAst, expressionName);
				expressionJSONInStr = negativeVariabilityElement.getExpressionInString();
				if (this.systemAnnotations.contains(expressionJSONInStr)) {
					throw new DuplicatedAnnotation("Duplicated annotation: " + decoratorName);
				}
				this.systemAnnotations.add(expressionJSONInStr);
			} else if(decoratorName != null && VariationPointDivisionConfiguration.RESERVED_SELECTED_ANNOTATIONS.contains(decoratorName)) { //user annotation
				negativeVariabilityElement = new NegativeVariabilityAnnotationElement(decoratorName, usedDecoratorAst);
				expressionJSONInStr = negativeVariabilityElement.getExpressionInString();
				if (this.userAnnotations.contains(expressionJSONInStr)) {
					throw new DuplicatedAnnotation("Duplicated annotation: " + decoratorName);
				}
				this.userAnnotations.add(expressionJSONInStr);
			} else {
				continue; // business annotations
			}
		}
		this.removeAllSimilarSystemAnnotationsAccordingToUserOnes();
	}
	
	/**
	 * Removes all similar user annotations from system ones
	 */
	private void removeAllSimilarSystemAnnotationsAccordingToUserOnes() {
		for(String userExpression: this.userAnnotations) {
			this.systemAnnotations.remove(userExpression);
		}
	}
	
	/**
	 * Verifies consistency of negative variability annotations according to pre-configured rules
	 * -in case of VariationPointDivisionConfiguration.PRESERVE_USER_ANNOTATION_IF_EXISTS_ONLY:
	 * 		-if system annotation is used then any use of user annotation is incorrect
	 * -in case of VariationPointDivisionConfiguration.PRESERVE_AUTOMATICALLY_ANNOTATED_NEGATIVE_VARIABILITY_PLACES:
	 * 		-all previous annotations are consistent to preserve
	 * -if system annotations are used and no user one:
	 * 		-annotations are in inconsistent state
	 * -otherwise  true
	 * 
	 * @param isSystemAnnotation - information is given annotation is system one
	 * @return true if annotations are used consistently otherwise false
	 */
	public boolean checkConsistency(boolean isSystemAnnotation) {
		if (VariationPointDivisionConfiguration.PRESERVE_USER_ANNOTATION_IF_EXISTS_ONLY) {
			if (isSystemAnnotation && this.userAnnotations.size() > 0) { return false; }
			return true;
		} else if (VariationPointDivisionConfiguration.PRESERVE_AUTOMATICALLY_ANNOTATED_NEGATIVE_VARIABILITY_PLACES) {
			return true;
		} 
		
		if (isSystemAnnotation && this.userAnnotations.size() == 0) {
			return false; //system annotation should be omitted
		}
		return true;
	}
}
