package divisioner.divisionStrategies;

import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import codeConstructsEvaluation.transformation.ASTConverterClient;
import codeConstructsEvaluation.transformation.PostRequester;
import codeContext.persistence.UpdatedTreePersistence;
import codeContext.processors.NotFoundVariableDeclaration;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import dividedAstExport.VPDividedExporter;
import divisioner.DivisioningInterface;
import divisioner.VariationPointDivisionConfiguration;
import divisioner.VariationPointsDivisioningStrategy;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;


/**
 * Strategy to mark/annotate all positive and/or negative variability variation points for their further analysis/management
 * 
 * @author Jakub Perdek
 *
 */
public class RecallStrategy implements VariationPointsDivisioningStrategy {

	/**
	 * Creates the recall strategy instance
	 */
	public RecallStrategy() {}
	
	/**
	 * Loads, divisions the code in form of application AST into variation points (negative and/or positive variability) and persists the resulting 
	 * highlighted/marked/annotated AST and the list with variation points into appropriate files
	 * 
	 * @param divisioner - object instance to manage and customize division process into variation points
	 * @param inputCodeFilePath - the path to the JavaScript/TypeScript script that is going to be loaded
	 * @param outputVisualizedAstPath - the output path of visualized/marked/annotated/highlighted AST to visualize 
	 * positions/locations of markers and annotations as the result of transformation from source AST
	 * @param fileOutputVariationPointsPath - the output path for the list with information about variation points
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public void division(DivisioningInterface divisioner, String inputCodeFilePath, String outputVisualizedAstPath, 
			String fileOutputVariationPointsPath) throws NotFoundVariableDeclaration, IOException, 
	InterruptedException, InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		JSONObject astTreeRoot = ASTConverterClient.convertFromCodeToASTJSON(PostRequester.loadFileContent(inputCodeFilePath));
		JSONObject highlightedAst = this.divisionAndGetHighlightedAst(divisioner, astTreeRoot, inputCodeFilePath);
		this.persistsHighlightedAst(inputCodeFilePath, highlightedAst, outputVisualizedAstPath);
		JSONArray harvestedVariationPoints = this.getVariationPointsData(highlightedAst, astTreeRoot);
		UpdatedTreePersistence.persistsAstInFile(fileOutputVariationPointsPath, 
				harvestedVariationPoints.toString());
	}
	
	/**
	 * Loads and divisions the application/script AST into variation points (negative and/or positive variability) 
	 * and returns the resulting highlighted/marked/annotated AST
	 * 
	 * @param divisioner - object instance to manage and customize division process into variation points 
	 * @param astTreeRoot - the root of the application/script AST 
	 * @param inputCodeFilePath - the path to the JavaScript/TypeScript script that is going to be loaded 
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public JSONObject divisionAndGetHighlightedAst(DivisioningInterface divisioner, JSONObject astTreeRoot, String inputCodeFilePath) throws NotFoundVariableDeclaration,
			IOException, InterruptedException, InvalidSystemVariationPointMarkerException, 
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		String fileName = inputCodeFilePath.replace('\\', '/');
		fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
		JSONObject highlightedAst = divisioner.divisionToVPHierarchic(
				astTreeRoot, fileName, VariationPointDivisionConfiguration.USE_TYPES);
		return highlightedAst;
	}
	
	/**
	 * Loads and divisions the code in form of application AST into variation points (negative and/or positive variability) and returns the resulting highlighted/marked/annotated AST
	 * -includes transformation of the code into the application AST
	 * 
	 * @param fileName - the name of the file that is loaded/processed/updated
	 * @param fileContent - the content of the loaded JavaScript/TypeScript script
	 * @param divisioner - object instance to manage and customize division process into variation points
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public JSONObject divisionAndGetHighlightedAst(String fileName, String fileContent, DivisioningInterface divisioner) 
					throws NotFoundVariableDeclaration, IOException, InterruptedException, InvalidSystemVariationPointMarkerException,
					DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		JSONObject astTreeRoot = ASTConverterClient.convertFromCodeToASTJSON(fileContent);
		JSONObject highlightedAst = divisioner.divisionToVPHierarchic(astTreeRoot, fileName, VariationPointDivisionConfiguration.USE_TYPES);
		return highlightedAst;
	}

	/**
	 * Persists highlighted/marked/annotated AST that is converted into code in the file - provides the visualization of markers inside code 
	 * (suitable for debugging purposes)
	 * 
	 * @param inputCodeFilePath - the path to the JavaScript/TypeScript script that is going to be loaded
	 * @param highlightedAst - the AST of the original application with inserted markers and annotations - marked nagative and positive variability
	 * @param outputVisualizedAstPath - the output path of visualized/marked/annotated/highlighted AST to visualize 
	 * positions/locations of markers and annotations as the result of transformation from source AST
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public void persistsHighlightedAst(String inputCodeFilePath, JSONObject highlightedAst, String outputVisualizedAstPath) 
			throws NotFoundVariableDeclaration, IOException, InterruptedException, 
			InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		UpdatedTreePersistence.persistsAstInFile(outputVisualizedAstPath, highlightedAst);
		String resultingCode = ASTConverterClient.convertFromASTToCode(highlightedAst.toString());
		UpdatedTreePersistence.persistsAstInFile(outputVisualizedAstPath.replace(".json", ".txt"), resultingCode);
	}
	
	/**
	 * Exports and collects information about variation points (variation points data) from highlighted.annotated/marked application AST
	 * 
	 * @param highlightedAst - the AST of the original application with inserted markers and annotations - marked nagative and positive variability
	 * @param originalAst
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public JSONArray getVariationPointsData(JSONObject highlightedAst, JSONObject originalAst) throws NotFoundVariableDeclaration, IOException, 
			InterruptedException, InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint,
			DuplicatedAnnotation {
		VPDividedExporter vpDividedExporter = new VPDividedExporter();
		return vpDividedExporter.collectAndProcessAllVariationPoints(highlightedAst, originalAst);
	}
	
	/**
	 * Divisions the content of base script/evolved file and extracts, collects, and returns information about variation points - variation points data
	 * 
	 * @param divisioner - 
	 * @param inputCodeFilePath - the path to the JavaScript/TypeScript script that is going to be loaded
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public JSONArray divisionAndGetVariationPointsData(DivisioningInterface divisioner, String inputCodeFilePath) throws NotFoundVariableDeclaration, IOException, InterruptedException, 
			InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		JSONObject astTreeRoot = ASTConverterClient.convertFromCodeToASTJSON(PostRequester.loadFileContent(inputCodeFilePath));
		JSONObject highlightedAst = this.divisionAndGetHighlightedAst(divisioner, astTreeRoot, inputCodeFilePath);
		JSONArray harvestedVariationPoints = this.getVariationPointsData(highlightedAst, astTreeRoot);
		return harvestedVariationPoints;
	}
}
