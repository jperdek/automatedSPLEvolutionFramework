package divisioner;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import codeContext.processors.NotFoundVariableDeclaration;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;

/**
 * Configuration of divisions into variation points in processed SPL
 * 
 * @author Jakub Perdek
 *
 */
public interface VariationPointDivisionConfiguration {

	public static final boolean LONG_CONTEXT_PRINT = true;
	public static final boolean ALLOW_POSITIVE_VARIABILITY = true;  // allows to manage concerns related to positive variability
	public static final boolean ALLOW_NEGATIVE_VARIABILITY = true;	// allows to manage concerns related to negative variability
	public static final boolean GENERATE_ALL_FUNCTION_PARAMETERS = false;
	public static final String MARKER_VP_NAME = "markerVP"; 		// positive variation point markers
	public static final String ANNOTATION_OF_VP = "AnnotationVP"; 	// negative variation point system annotation
	public static final String PARAM_VP = "paramVP";				// positive variation point parameter marker
	public static final boolean INJECT_UNSUPPORTED_VP_MARKS = true;
	public static final boolean ALLOW_NEGATIVE_VARIABILITY_PARAMETERS = false;
	
	public static final boolean USE_PARAMETERIZED_FORM = true;
	public static final String PARAMETERIZED_FORM_START = "[%[";		//start marker for function/template parameters
	public static final String PARAMETERIZED_FORM_DELIMITER = "|%|";	//delimiter marker for function/template parameters
	public static final String PARAMETERIZED_FORM_END = "]%]";			//end marker for function/template parameters
	public static final boolean USE_TYPES = true;
	public static final boolean PRESERVE_MULTIPLE_USER_ANNOTATIONS = false;
	public static final boolean REMOVE_ALL_VARIABILITY_ANNOTATIOS_BEFORE_UPDATE = true; //otherwise removal will be applied locally
	public static final boolean PREFER_POSITION_UPDATES_BEFORE_PERSISTING_ILLEGAL_DECORATORS_INFORMATION = false; //will convert Ast to code and back
	public static final boolean OPTIONALLY_MERGE_ILLEGAL_DECORATORS_DURING_SELECTION = true; // uses illegal decorators if are available - are merged with legal, but marked as illegal
	public static final boolean PRESERVE_USER_ANNOTATION_IF_EXISTS_ONLY = true;
	public static final boolean PRESERVE_AUTOMATICALLY_ANNOTATED_NEGATIVE_VARIABILITY_PLACES = false; //preserves all negative variability annotations
	//DEFINE OWN ANNOTATIONS
	final static Set<String>  RESERVED_ANNOTATIONS = Set.of("wholeEntity", 
			"wholeInitlialization", "variableDeclaration", "wholeBlock", "wholeBlock2", "skipLine");
	final static Set<String> RESERVED_ANGULAR_ANNOTATIONS = Set.of(
			"wholeBlockFile", "wholeBlockMethod", "skipLineParameter", "skipLineVariableDeclaration", 
			"skipLineClassVariableDeclaration", "skipLineFile");
	
	public static final String USER_GLOBAL_VARIABLE_DECLARATION_ANNOTATION_NAME = "variableDeclarationGlobal";
	public static final String USER_LOCAL_VARIABLE_DECLARATION_ANNOTATION_NAME = "variableDeclarationLocal";
	public static final String USER_CLASS_VARIABLE_DECLARATION_ANNOTATION_NAME = "variableDeclarationClass";
	public static final String USER_CLASS_DECLARATION_ANNOTATION_NAME = "wholeClass";
	public static final String USER_METHOD_ANNOTATION_NAME = "wholeBlockMethod";
	public static final String USER_CLASS_METHOD_ANNOTATION_NAME = "wholeClassMethod";
	public static final String USER_VARIABLE_PARAMETER = "variableParameter";
	public static final String USER_VARIABLE_CONSTRUCTOR_PARAMETER = "variableParameter";
	final static Set<String> RESERVED_SEMANTIC_ANNOTATIONS = Set.of(List.of(USER_GLOBAL_VARIABLE_DECLARATION_ANNOTATION_NAME,
			USER_LOCAL_VARIABLE_DECLARATION_ANNOTATION_NAME, USER_CLASS_VARIABLE_DECLARATION_ANNOTATION_NAME,
			USER_CLASS_DECLARATION_ANNOTATION_NAME, USER_METHOD_ANNOTATION_NAME, USER_CLASS_METHOD_ANNOTATION_NAME, 
			USER_VARIABLE_PARAMETER, USER_VARIABLE_CONSTRUCTOR_PARAMETER, "skipLine").stream().distinct().toArray(String[] :: new)); 
	//"skipLine" - in code decorators cannot be used directly for this except declaration of supporting 
	//variable or using constructs from positive variability - RECURSIVE DERIVATOR ANNOTATIONS SHOULD BE USED FOR THIS PURPOSE
	
	//PUT DEFINED VALUES HERE IN PLACE OF RESERVED_SEMANTIC_ANNOTATIONS
	public final static Set<String> RESERVED_SELECTED_ANNOTATIONS = new HashSet<String>(RESERVED_SEMANTIC_ANNOTATIONS);
	
	
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
			throws NotFoundVariableDeclaration, IOException, 
			InterruptedException, InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation;
	
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
			InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation;

	/**
	 * Loads and divisions the code in form of application AST into variation points (negative and/or positive variability) and returns the resulting highlighted/marked/annotated AST
	 * -includes transformation of the code into the application AST
	 * 
	 * @param inputCodeFilePath - the path to the JavaScript/TypeScript script that is going to be loaded
	 * @return the resulting highlighted/marked/annotated AST
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public JSONObject divisionAndGetHighlightedAst(String inputCodeFilePath) throws NotFoundVariableDeclaration, IOException, InterruptedException, 
			InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation;

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
			InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation;

	
	/**
	 * Exports and collects information about variation points (variation points data) from highlighted.annotated/marked application AST
	 * 
	 * @param highlightedAst - the AST of the original application with inserted markers and annotations - marked nagative and positive variability
	 * @return information about variation points - variation points data (for both variability - positive and negative)
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public JSONArray getVariationPointsData(JSONObject highlightedAst) throws NotFoundVariableDeclaration, 
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
	public JSONObject divisionAndGetHighlightedAst(String fileName, String inputCodeFilePath, Divisioner divisioner) throws 
			NotFoundVariableDeclaration, IOException, InterruptedException, InvalidSystemVariationPointMarkerException,
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation;
}
