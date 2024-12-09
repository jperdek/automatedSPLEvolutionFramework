package divisioner;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import codeConstructsEvaluation.transformation.ASTConverterClient;
import codeConstructsEvaluation.transformation.PostRequester;
import codeContext.CodeContext;
import codeContext.processors.NotFoundVariableDeclaration;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import divisioner.divisionStrategies.RecallStrategy;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;


/**
 * Manages the divisioning process into variation points according to provided strategy and implementation
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class VariationPointDivisioning {

	/**
	 * Implementation of particular strategy for divisioning the program into variation points
	 */
	private VariationPointsDivisioningStrategy variationPointsDivisioningStrategy;
	
	/**
	 * Functionality to division AST into positive and negative variability variation points
	 */
	private DivisioningInterface divisioner;
	
	/**
	 * Creates entity to manage divisioning process according to particular strategy
	 * 
	 * @param variationPointsDivisioningStrategy
	 */
	public VariationPointDivisioning(VariationPointsDivisioningStrategy variationPointsDivisioningStrategy) {
		this.variationPointsDivisioningStrategy = variationPointsDivisioningStrategy;
		this.divisioner = new Divisioner(this.variationPointsDivisioningStrategy);
	}
	
	/**
	 * Creates entity to manage divisioning process accoridng to particular strategy
	 */
	public VariationPointDivisioning() {
		this(new RecallStrategy());
	}
	
	/**
	 * Returns code context with hierarchical information obtained from divisioning
	 * 
	 * @return the code context with hierarchical information obtained from divisioning
	 */
	public CodeContext getCodeContextFromDivision() {
		return this.divisioner.getCodeContextFromDivision();
	}
	
	/**
	 * Loads, divisions the code in form of application AST into variation points (negative and/or positive variability) and persists the resulting 
	 * highlighted/marked/annotated AST and the list with variation points into appropriate files
	 * 
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
	public void division(String inputCodeFilePath, String outputVisualizedAstPath, String fileOutputVariationPointsPath) 
			throws NotFoundVariableDeclaration, IOException, InterruptedException, InvalidSystemVariationPointMarkerException, 
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		this.variationPointsDivisioningStrategy.division(this.divisioner, inputCodeFilePath, outputVisualizedAstPath, fileOutputVariationPointsPath);
	}
	
	/**
	 * Divisions the content of base script/evolved file and extracts, collects, and returns information about variation points - variation points data
	 * 
	 * @param inputCodeFilePath - the path to the JavaScript/TypeScript script that is going to be loaded
	 * @return information about variation points - variation points data (for both variability - positive and negative)
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public JSONArray divisionAndGetVariationPointsData(String inputCodeFilePath) throws NotFoundVariableDeclaration, IOException, InterruptedException, 
			InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		return this.variationPointsDivisioningStrategy.divisionAndGetVariationPointsData(this.divisioner, inputCodeFilePath);
	}

	/**
	 * Loads and divisions the code in form of application AST into variation points (negative and/or positive variability) and returns the resulting highlighted/marked/annotated AST
	 * -includes transformation of the code into the application AST
	 * 
	 * @param divisioner - object instance to manage and customize division process into variation points
	 * @param inputCodeFilePath - the path to the JavaScript/TypeScript script that is going to be loaded
	 * @param originalAst - 
	 * @return the resulting highlighted/marked/annotated AST
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public JSONObject divisionAndGetHighlightedAst(String inputCodeFilePath, JSONObject originalAst) throws NotFoundVariableDeclaration, IOException, InterruptedException, 
			InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		System.out.println("HEEERRRRRRRRRRRRRREEEEEEEEEEEEE: " + inputCodeFilePath);
		JSONObject astTreeRoot = ASTConverterClient.convertFromCodeToASTJSON(PostRequester.loadFileContent(inputCodeFilePath));
		JSONObject highlightedAst = this.variationPointsDivisioningStrategy.divisionAndGetHighlightedAst(this.divisioner, astTreeRoot, inputCodeFilePath);
		System.out.println("FAIILURREEE: " + highlightedAst.toString());
		return highlightedAst;
	}

	/**
	 * Loads and divisions the application/script AST into variation points (negative and/or positive variability) 
	 * and returns the resulting highlighted/marked/annotated AST
	 * 
	 * @param loadedCodeAst - the root of the application/script AST 
	 * @param inputCodeFilePath - the path to the JavaScript/TypeScript script that is going to be loaded 
	 * @return the resulting highlighted/marked/annotated AST
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public JSONObject divisionAndGetHighlightedAst(JSONObject loadedCodeAst, String inputCodeFilePath) throws NotFoundVariableDeclaration, IOException, InterruptedException, 
			InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		return this.variationPointsDivisioningStrategy.divisionAndGetHighlightedAst(this.divisioner, loadedCodeAst, inputCodeFilePath);
	}

	
	/**
	 * Exports and collects information about variation points (variation points data) from highlighted.annotated/marked application AST
	 * 
	 * @param highlightedAst - the AST of the original application with inserted markers and annotations - marked nagative and positive variability
	 * @param originalAst
	 * @return information about variation points - variation points data (for both variability - positive and negative)
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public JSONArray getVariationPointsData(JSONObject highlightedAst, JSONObject originalAst) throws NotFoundVariableDeclaration, 
			IOException, InterruptedException, InvalidSystemVariationPointMarkerException, 
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		return this.variationPointsDivisioningStrategy.getVariationPointsData(highlightedAst, originalAst);
	}

	/**
	 * Loads and divisions the code in form of application AST into variation points (negative and/or positive variability) and returns the resulting highlighted/marked/annotated AST
	 * -includes transformation of the code into the application AST
	 * 
	 * @param fileName - the name of the file that is loaded/processed/updated
	 * @param inputCodeFilePath - the path to the loaded JavaScript/TypeScript script
	 * @return the resulting highlighted/marked/annotated AST
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public JSONObject divisionAndGetHighlightedAst(String fileName, String inputCodeFilePath) throws 
			NotFoundVariableDeclaration, IOException, InterruptedException, InvalidSystemVariationPointMarkerException,
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		return this.variationPointsDivisioningStrategy.divisionAndGetHighlightedAst(fileName, inputCodeFilePath, this.divisioner);
	}
}
