package variationPointsVisualization;

import java.io.IOException;
import org.json.simple.JSONArray;
import divisioner.VariationPointDivisionConfiguration;
import variationPointsVisualization.AnnotationExtensionMarker.SystemAnnotationType;


/**
 * Verify if variation point belongs to negative variability and its consistency
 * -checks if annotation is user annotation or system annotation and thus belongs to negative variation point annotations
 * -consistency is verified according pre-configured settings
 * 		for example if user annotation is used then no system annotations are used in one code fragment
 * 
 * @author Jakub Perdek
 *
 */
public class NegativeVariabilityDecoratorConsistenceVerifier {

	/**
	 * Returns type of negative variability system annotation otherwise null
	 * 
	 * @param annotationName - the name of actually verified annotation
	 * @return given type of system negative variability annotation otherwise null
	 */
	public static AnnotationExtensionMarker.SystemAnnotationType classifyNegativeVariabilitySystemAnnotation(String annotationName) {
		if (annotationName.contains(AnnotationExtensionMarker.SystemAnnotationType.FUNCTION.label)) {
			return AnnotationExtensionMarker.SystemAnnotationType.FUNCTION;
		} else if(annotationName.contains(AnnotationExtensionMarker.SystemAnnotationType.CLASS_FUNCTION.label)) {
			return AnnotationExtensionMarker.SystemAnnotationType.CLASS_FUNCTION;
		} else if(annotationName.contains(AnnotationExtensionMarker.SystemAnnotationType.CLASS_VARIABLE.label)) {
			return AnnotationExtensionMarker.SystemAnnotationType.CLASS_VARIABLE;
		} else if(annotationName.contains(AnnotationExtensionMarker.SystemAnnotationType.CLASS.label)) {
			return AnnotationExtensionMarker.SystemAnnotationType.CLASS;
		}  else if(annotationName.contains(AnnotationExtensionMarker.SystemAnnotationType.CONSTRUCTOR_PARAMETER.label)) {
			return AnnotationExtensionMarker.SystemAnnotationType.CONSTRUCTOR_PARAMETER;
		} else if(annotationName.contains(AnnotationExtensionMarker.SystemAnnotationType.PARAMETER.label)) {
			return AnnotationExtensionMarker.SystemAnnotationType.PARAMETER;
		} else if(annotationName.contains(AnnotationExtensionMarker.SystemAnnotationType.VARIABLE.label)) {
			return AnnotationExtensionMarker.SystemAnnotationType.VARIABLE;
		}
		return null;
	}
	
	/**
	 * Verifies if variation point belongs to negative variability
	 * 
	 * @param decoratorName - the name of actually verified annotation
	 * @return true if annotation is negative variation point annotation otherwise false
	 */
	public static boolean isNegativeVariabilityAnnotation(String decoratorName) {
		SystemAnnotationType expressionName = NegativeVariabilityDecoratorConsistenceVerifier.classifyNegativeVariabilitySystemAnnotation(decoratorName);
		if (expressionName != null) {
			return true; //negative variability system annotation
		} else if(VariationPointDivisionConfiguration.RESERVED_SELECTED_ANNOTATIONS.contains(decoratorName)) {
			return true; //user annotation
		} 
		return false;	// business annotations
	}

	/**
	 * Checks consistency of all used decorator under given variation point
	 * 
	 * @param usedDecorators - array of decorators that annotate given variation point
	 * @param isSystemAnnotation - information is system annotation is used
	 * @return true if decorators are used consistently used otherwise false
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public static boolean areDecoratorsConsistent(JSONArray usedDecorators, boolean isSystemAnnotation) 
			throws IOException, InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		NegativeVariabilityAnnotationAggregator negativeVariabilityAnnotationAggregator = new NegativeVariabilityAnnotationAggregator();
		negativeVariabilityAnnotationAggregator.aggregateNegativeVariabilityDecorators(usedDecorators);
		return negativeVariabilityAnnotationAggregator.checkConsistency(isSystemAnnotation);
	}
}
