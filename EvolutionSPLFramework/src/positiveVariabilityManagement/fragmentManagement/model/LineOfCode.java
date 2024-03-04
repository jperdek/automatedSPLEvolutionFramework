package positiveVariabilityManagement.fragmentManagement.model;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import codeConstructsEvaluation.transformation.ASTConverterClient;
import codeContext.processors.export.ExportLocationAggregation;
import positiveVariabilityManagement.entities.CallableConstructTemplate;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;
import splEvolutionCore.candidateSelector.valueAssignment.AssignedValue;


/**
 * One line of code
 * 
 * @author Jakub Perdek
 *
 */
public class LineOfCode implements CodeFragment {

	/**
	 * Callable representation string of one code line
	 */
	protected String callableForm;
	
	/**
	 * Assigned value to each part of one line code fragment
	 */
	protected Map<String, AssignedValue> codeConstructValue;
	
	/**
	 * Imported dependencies of one line code fragment
	 */
	protected ExportLocationAggregation importsDependencies;
	
	
	/**
	 * Creates one line code representation 
	 * 
	 * @param callableForm - callable representation string of one code line
	 * @param codeConstructValue - assigned value to each part of one line code fragment
	 * @param importsDependencies - imported dependencies of one line code fragment
	 */
	public LineOfCode(String callableForm, Map<String, AssignedValue> codeConstructValue, 
			ExportLocationAggregation importsDependencies) {
		this.callableForm = callableForm;
		this.codeConstructValue = codeConstructValue;
		this.importsDependencies = importsDependencies;
	}
	
	/**
	 * Compares LineOfCode with another CodeFragment instance according to its imports 
	 * 
	 * @param codeFragment - an instance of CodeFragment that is compared according to its imports
	 */
	public Set<String> compareExportLocationAggregations(CodeFragment codeFragment) {
		return this.importsDependencies.compareImports(codeFragment.getExportLocationAggregation());
	}
	
	/**
	 * Returns callable representation string of one code line 
	 * For example: "new A(arg1)"
	 * 
	 * @return callable representation string of one code line
	 */
	public String getCallableForm() { return this.callableForm; }
	
	/**
	 * Returns code from instantiated callable form
	 * 
	 * @return code representation of one line code fragment
	 */
	public String getCode() { return this.callableForm; }
	
	/**
	 * Returns mapping of dependencies from callable form according to associated template from PositiveVariationPointCandidateTemplates
	 * 
	 * @param positiveVariationPointCandidateTemplates - template with associated callable forms
	 * @return the mapping of the used parameter names of called functionality to their types
	 */
	public Map<String, String> getDependencies(PositiveVariationPointCandidateTemplates positiveVariationPointCandidateTemplates) {
		CallableConstructTemplate callableConstructTemplate = 
				positiveVariationPointCandidateTemplates.getCallableConstructTemplate(this.callableForm);
		return callableConstructTemplate.getParameterNamesToTypeMapping();
	}

	/**
	 * Returns AST of Line of code representation
	 * 
	 * @return returns JSON array from code statements
	 */
	@Override
	public JSONArray getCodeAst() throws IOException, InterruptedException {
		JSONObject lineOfCodeAst = ASTConverterClient.convertFromCodeToASTJSON(this.callableForm);
		return (JSONArray) ((JSONObject) lineOfCodeAst.get("ast")).get("statements");
	}
	
	/**
	 * Create code from import dependencies
	 * 
	 * @return the code of all import dependencies necessary for this method representation 
	 */
	@Override
	public String getImportDependenciesAsCode() {
		return this.importsDependencies.buildAllImports();
	}

	/**
	 * Get import dependencies in form of AST tree
	 * 
	 * @return JSON Array of dependencies exported to AST
	 */
	@Override
	public JSONArray getImportdependenciesAsAst() throws IOException, InterruptedException {
		JSONObject lineOfCodeAst = ASTConverterClient.convertFromCodeToASTJSON(this.getImportDependenciesAsCode());
		return (JSONArray) ((JSONObject) lineOfCodeAst.get("ast")).get("statements");
	}

	/**
	 * Returns ExportLocationAggregation object with aggregated and used imports
	 * 
	 * @return ExportLocationAggregation object with aggregated and used imports
	 */
	@Override
	public ExportLocationAggregation getExportLocationAggregation() {
		return this.importsDependencies;
	}

	/**
	 * Integration of used imports to AST of base application
	 * 
	 * @param originalAst - native AST from base application where imports should be integrated
	 */
	@Override
	public void integrateImports(JSONObject originalAst) throws IOException, InterruptedException {
		this.importsDependencies.integrateImports(originalAst);
	}
}
