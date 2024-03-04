package positiveVariabilityManagement.fragmentManagement.model;

import java.io.IOException;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import codeConstructsEvaluation.transformation.ASTConverterClient;
import codeContext.processors.export.ExportLocationAggregation;
import splEvolutionCore.candidateSelector.valueAssignment.AssignedValue;
import variationPointsVisualization.VariationPointsWithVariablesMarker.DeclarationTypes;


/**
 * Representation of variable declaration in form of one line of code
 * 
 * @author Jakub Perdek
 *
 */
public class VariableDeclarationLineOfCode extends LineOfCode {

	/**
	 * Creates Variable declaration from one line of code
	 * 
	 * @param callableForm - callable representation string of variable declaration in form of one code line
	 * @param codeConstructValue - assigned value to each part of variable declaration in form of one line code fragment
	 * @param importsDependencies - imported dependencies of  variable declaration in form of one line code fragment
	 */
	public VariableDeclarationLineOfCode(String callableForm, Map<String, AssignedValue> codeConstructValue, 
			ExportLocationAggregation importsDependencies) {
		super(callableForm, codeConstructValue, importsDependencies);
	}
	
	/**
	 * Returns AST of Variable declaration represented as one line code fragment
	 * 
	 * @param useInsideClass - information if variable is declared as class variable to declare it correctly
	 * @return returns JSON array from code statements
	 */
	public JSONArray getCodeAst(boolean useInsideClass) throws IOException, InterruptedException {
		String callableForm = this.callableForm;
		if (useInsideClass) {
			callableForm = callableForm.replace(DeclarationTypes.CONST.label, "")
					.replace(DeclarationTypes.LET.label, "").replace(DeclarationTypes.VAR.label, "");
		}
		JSONObject lineOfCodeAst = ASTConverterClient.convertFromCodeToASTJSON(callableForm);
		return (JSONArray) ((JSONObject) lineOfCodeAst.get("ast")).get("statements");
	}
}
