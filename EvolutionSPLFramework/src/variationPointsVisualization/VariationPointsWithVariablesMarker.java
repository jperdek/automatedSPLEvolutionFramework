package variationPointsVisualization;

import java.io.IOException;
import org.json.simple.JSONObject;
import codeConstructsEvaluation.transformation.ASTConverterClient;
import divisioner.VariationPointDivisionConfiguration;


/**
 * Manages creation of positive variation point markers in form of variables
 * 
 * @author Jakub Perdek
 *
 */
public class VariationPointsWithVariablesMarker {

	/**
	 * Index of declared positive variation points to avoid conflicts/ to preserve unique variable names
	 */
	private static int usedIndexInner = 1;
	
	/**
	 * Enumeration of all available declaration types in JavaScript/TypeScript
	 *  
	 * @author Jakub Perdek
	 *
	 */
	public static enum DeclarationTypes {
		VAR("var"),
		LET("let"),
		CONST("const"),
		STATIC("static"),
		WITHOUT("");
	
		/**
		 * The name of used declaration type (keyword in JavaScript/TypeScript)
		 */
		public final String label;

		/**
		 * Creation of label that is used to find given declaration type
		 * 
		 * @param label - string representation of declaration from code
		 */
	    private DeclarationTypes(String label) {
	        this.label = label;
	    }
	}
	
	/**
	 * Creates positive variability point marker with associated configuration expression as local variable with associated configuration expression
	 * -positive variation point marker name begins with VariationPointDivisionConfiguration.MARKER_VP_NAME
	 * 
	 * @param expression - configuration expression associated with given variation point
	 * @param usedDeclarationType - declaration type of given variable
	 * @return created positive variability point marker as class variable with associated configuration expression
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static JSONObject getClassStubPart(JSONObject expression, DeclarationTypes usedDeclarationType) throws IOException, InterruptedException {
		String initializedExpressionCodeConstruct= "class DefaultClass {" + usedDeclarationType.label + " " + VariationPointDivisionConfiguration.MARKER_VP_NAME + VariationPointsWithVariablesMarker.usedIndexInner + " = " + expression + "; }";
		VariationPointsWithVariablesMarker.usedIndexInner = VariationPointsWithVariablesMarker.usedIndexInner + 1;
		return ASTConverterClient.getFirstMemberFromASTFileFistStatement(ASTConverterClient.convertFromCodeToASTJSON(
				initializedExpressionCodeConstruct));
	}
	
	/**
	 * Creates positive variability point marker with associated configuration expression as local variable with associated configuration expression
	 * -positive variation point marker name begins with VariationPointDivisionConfiguration.MARKER_VP_NAME
	 * 
	 * @param expression - configuration expression associated with given variation point
	 * @param usedDeclarationType - declaration type of given variable
	 * @return created positive variability point marker as local variable with associated configuration expression
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static JSONObject getLocalStubPart(JSONObject expression, DeclarationTypes usedDeclarationType) throws IOException, InterruptedException {
		String initializedExpressionCodeConstruct = usedDeclarationType.label + " " + VariationPointDivisionConfiguration.MARKER_VP_NAME + VariationPointsWithVariablesMarker.usedIndexInner + " = " + expression + ";";
		VariationPointsWithVariablesMarker.usedIndexInner = VariationPointsWithVariablesMarker.usedIndexInner + 1;
		return ASTConverterClient.getFirstStatementFromASTFile(ASTConverterClient.convertFromCodeToASTJSON(
				initializedExpressionCodeConstruct));
	}

	/**
	 * Generates marker for positive variability from variable in AST according to its optional relation to class
	 * 
	 * @param expression - configuration expression associated with given variation point
	 * @param usedDeclarationType - declaration type of given variable
	 * @param belongsToClass - information if given marker belongs to class - to choose given variable declaration type
	 * @return generated marker from variable in AST
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static JSONObject generateMarkerFromVariableInAST(JSONObject expression, 
			DeclarationTypes usedDeclarationType, boolean belongsToClass) throws IOException, InterruptedException {
		JSONObject generatedAst;
		if (belongsToClass) {
			generatedAst = VariationPointsWithVariablesMarker.getClassStubPart(expression, usedDeclarationType);
		} else {
			generatedAst = VariationPointsWithVariablesMarker.getLocalStubPart(expression, usedDeclarationType);
		}
		return generatedAst;
	}
	
	/**
	 * Generates marker for positive variability from variable in AST with associated configuration expression
	 *  
	 * @param expression - configuration expression associated with given variation point
	 * @param usedIndex - index for variable marker to declare new variable in place of positive variation point
	 * @param usedDeclarationType - declaration type of given variable
	 * @return created positive variability point marker as local variable with associated configuration expression
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static JSONObject generateMarkerFromVariableInAST(
			JSONObject expression, int usedIndex, DeclarationTypes usedDeclarationType) throws IOException, InterruptedException {
		String initializedExpressionCodeConstruct = usedDeclarationType.label + " " + VariationPointDivisionConfiguration.MARKER_VP_NAME + usedIndex + " = " + expression + ";";
		return ASTConverterClient.getFirstStatementFromASTFile(ASTConverterClient.convertFromCodeToASTJSON(
				initializedExpressionCodeConstruct));
	}
}
