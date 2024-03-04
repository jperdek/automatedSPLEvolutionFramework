package variationPointDivisionTests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import codeContext.processors.NotFoundVariableDeclaration;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import divisioner.VariationPointDivisionConfiguration;
import divisioner.divisionStrategies.RecallStrategy;
import variationPointsVisualization.AnnotationExtensionMarker;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;


/**
 * Verification tests of harvested negative variation point of given TypeScript script
 * -recall division strategy is used
 * 
 * @author Jakub Perdek
 *
 */
class RecallDivisionerStrategyTest {

	/**
	 * Testing found cardinalities of harvested negative variation points in case when Recall strategy is applied
	 * 
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	@Test
	void test() throws NotFoundVariableDeclaration, IOException, InterruptedException, 
						InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		String filePath = "E:\\aspects\\spaProductLine\\VariationPointDivisioner\\src\\testFiles\\platnoJSIndirrectAll.js";
		JSONArray harvestedVariationPoints = new RecallStrategy().divisionAndGetVariationPointsData(filePath);
		
		JSONObject variationPoint;
		String variationPointName, variationVPType;
		int numberClassFunctions = 0, numberNonClassFunctions = 0, numberClasses = 0, numberClassVariables = 0;
		int numberParameters = 0, numberNonClassVariables = 0;
		int numberPositiveVariabilityPlaces = 0;
		boolean nothingExceptional = true;
		boolean isClassRelated;
		for (Object variationPointObject: harvestedVariationPoints) {
			variationPoint = (JSONObject) variationPointObject;
			boolean belongsToPositiveVariability = (boolean) variationPoint.get("newVariationPoint");
			if (belongsToPositiveVariability) {
				variationPointName = (String) variationPoint.get("variationPointName");
				assert(variationPointName.startsWith(VariationPointDivisionConfiguration.MARKER_VP_NAME));
				numberPositiveVariabilityPlaces++;
			}

			isClassRelated = (boolean) variationPoint.get("classRelated");
			variationVPType = (String) variationPoint.get("annotationVPType");
			if (variationVPType == null) {
				assertEquals(belongsToPositiveVariability, true);
			} else {
				if (variationVPType.equals(AnnotationExtensionMarker.SystemAnnotationType.CLASS_FUNCTION.label)){
					numberClassFunctions++;
					assertEquals(isClassRelated, true);
				} else if (variationVPType.equals(AnnotationExtensionMarker.SystemAnnotationType.FUNCTION.label)){
					numberNonClassFunctions++;
					assertEquals(isClassRelated, false);
				} else if (variationVPType.equals(AnnotationExtensionMarker.SystemAnnotationType.CLASS.label)){
					numberClasses++;
					assertEquals(isClassRelated, true);
				} else if (variationVPType.equals(AnnotationExtensionMarker.SystemAnnotationType.VARIABLE.label)){
					numberNonClassVariables++;
					assertEquals(isClassRelated, false);
				} else if (variationVPType.equals(AnnotationExtensionMarker.SystemAnnotationType.CLASS_VARIABLE.label)){
					numberClassVariables++;
					assertEquals(isClassRelated, true);
				} else if (variationVPType.equals(AnnotationExtensionMarker.SystemAnnotationType.PARAMETER.label)){
					numberParameters++;
				} else {
					nothingExceptional = false;
				}
				assertEquals(belongsToPositiveVariability, false);
			}
		}
		assertEquals(numberPositiveVariabilityPlaces, 38);
		
		if (VariationPointDivisionConfiguration.PREFER_POSITION_UPDATES_BEFORE_PERSISTING_ILLEGAL_DECORATORS_INFORMATION) {
			assertEquals(numberClassFunctions, 3); 		//without constructor
		} else {
			assertEquals(numberClassFunctions, 4); 		//with constructor
		}
		assertEquals(numberNonClassFunctions, 3); 	//also nested functions
		assertEquals(numberClasses, 2); 
		assertEquals(numberNonClassVariables, 6);
		assertEquals(numberClassVariables, 2);
		assertEquals(numberParameters, 0);
		assert(nothingExceptional);
	}
}
