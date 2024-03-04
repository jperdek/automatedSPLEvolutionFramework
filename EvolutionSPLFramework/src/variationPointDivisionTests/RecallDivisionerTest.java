package variationPointDivisionTests;

import java.io.IOException;
import codeContext.processors.NotFoundVariableDeclaration;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import divisioner.divisionStrategies.RecallStrategy;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;


/**
 * Small test of Recall division strategy
 * -divisions AST of converted analyzed application into negative and positive variability in all places
 * 
 * @author Jakub Perdek
 *
 */
public class RecallDivisionerTest {
	
	/**
	 * Main method to test usage of division strategy - its applicability
	 * -paths should be replaced
	 * 
	 * @param args - used arguments (not necessary)
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public static void main(String args[]) throws NotFoundVariableDeclaration, IOException, InterruptedException, InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		//String filePath = "E:\\aspects\\spaProductLine\\VariationPointDivisioner\\src\\testFiles\\platnoJSIndirrect2.js";
		String filePath = "E:\\aspects\\spaProductLine\\VariationPointDivisioner\\src\\testFiles\\platnoJSIndirrectAllTyped.js";
		String fileOutputAstPath = "E:\\aspects\\spaProductLine\\VariationPointDivisioner\\output\\markedVariationPoints.json";
		String fileOutputVariationPointsPath = "E:\\aspects\\spaProductLine\\VariationPointDivisioner\\output\\harvestedVariationPoints.json";
		new RecallStrategy().division(filePath, fileOutputAstPath, fileOutputVariationPointsPath);
	}
}
