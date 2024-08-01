package divisioner;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import codeContext.processors.NotFoundVariableDeclaration;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;


/**
 * Provides interface for implementation of functionality for divisioning the program into variation points
 *  
 * 
 * @author Jakub Perdek
 *
 */
public interface VariationPointsDivisioningStrategy {
		
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
	public void division(DivisioningInterface divisioner, String inputCodeFilePath, String outputVisualizedAstPath, String fileOutputVariationPointsPath) 
			throws NotFoundVariableDeclaration, IOException, InterruptedException, InvalidSystemVariationPointMarkerException, 
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation;
	
	/**
	 * Divisions the content of base script/evolved file and extracts, collects, and returns information about variation points - variation points data
	 * 
	 * @param divisioner - object instance to manage and customize division process into variation points
	 * @param inputCodeFilePath - the path to the JavaScript/TypeScript script that is going to be loaded
	 * @return information about variation points - variation points data (for both variability - positive and negative)
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public JSONArray divisionAndGetVariationPointsData(DivisioningInterface divisioner, String inputCodeFilePath) throws NotFoundVariableDeclaration, IOException, InterruptedException, 
			InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation;

	/**
	 * Loads and divisions the application/script AST into variation points (negative and/or positive variability) 
	 * and returns the resulting highlighted/marked/annotated AST
	 * 
	 * @param divisioner - object instance to manage and customize division process into variation points
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
	public JSONObject divisionAndGetHighlightedAst(DivisioningInterface divisioner, JSONObject loadedCodeAst, String inputCodeFilePath) throws NotFoundVariableDeclaration, IOException, InterruptedException, 
			InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation;

	
	/**
	 * Exports and collects information about variation points (variation points data) from highlighted.annotated/marked application AST
	 * 
	 * @param highlightedAst - the AST of the original application with inserted markers and annotations - marked nagative and positive variability
	 * @param originalAst - 
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
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation;

	/**
	 * Loads and divisions the code in form of application AST into variation points (negative and/or positive variability) and returns the resulting highlighted/marked/annotated AST
	 * -includes transformation of the code into the application AST
	 * 
	 * @param fileName - the name of the file that is loaded/processed/updated
	 * @param inputCodeFilePath - the path to the loaded JavaScript/TypeScript script
	 * @param divisioner - object instance to manage and customize division process into variation points
	 * @return the resulting highlighted/marked/annotated AST
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public JSONObject divisionAndGetHighlightedAst(String fileName, String inputCodeFilePath, DivisioningInterface divisioner) throws 
			NotFoundVariableDeclaration, IOException, InterruptedException, InvalidSystemVariationPointMarkerException,
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation;
}
