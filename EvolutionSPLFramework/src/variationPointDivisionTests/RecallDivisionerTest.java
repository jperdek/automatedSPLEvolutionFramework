package variationPointDivisionTests;

import java.io.IOException;
import codeContext.processors.NotFoundVariableDeclaration;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import divisioner.Divisioner;
import divisioner.DivisioningInterface;
import divisioner.VariationPointDivisionConfiguration;
import divisioner.VariationPointDivisioning;
import divisioner.divisionStrategies.RecallStrategy;
import evolutionSimulation.productAssetsInitialization.SharedConfiguration;
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
		// String filePath = SharedConfiguration.PROJECT_PATH + "\\src\\testFiles\\platnoJSIndirrect2.js";
		String filePath = SharedConfiguration.PROJECT_PATH + "\\src\\testFiles\\platnoJSIndirrectAllTyped.js";
		String fileOutputAstPath = SharedConfiguration.PROJECT_PATH + "\\output\\markedVariationPoints.json";
		String fileOutputVariationPointsPath = SharedConfiguration.PROJECT_PATH + "\\output\\harvestedVariationPoints.json";
		VariationPointDivisioning variationPointDivisioning = new VariationPointDivisioning();
		variationPointDivisioning.division(filePath, fileOutputAstPath, fileOutputVariationPointsPath);
	}
}
