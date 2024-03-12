package positiveVariabilityManagement.fragmentManagement.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import codeConstructsEvaluation.transformation.ASTConverterClient;
import codeContext.processors.export.ExportLocationAggregation;
import splEvolutionCore.SPLEvolutionCore;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Representation of aggregation of code fragments
 * 
 * @author Jakub Perdek
 *
 */
public class AggregatedCodeFragment implements CodeFragment {
	
	/**
	 * Aggregated code fragments
	 */
	private List<CodeFragment> codeFragments;
	
	/**
	 * Aggregation of associated dependencies
	 */
	private ExportLocationAggregation importsDependencies;
	

	/**
	 * Creates aggregated code fragment
	 */
	public AggregatedCodeFragment() { 
		this.codeFragments = new ArrayList<CodeFragment>();
		this.importsDependencies = new ExportLocationAggregation();
	}
	
	/**
	 * Compares given instance of this code aggregation with another CodeFragment instance according to its imports 
	 * 
	 * @param codeFragment - an instance of CodeFragment that is compared according to its imports
	 * @return the set of exported location aggregations/dependencies that are in code 
	 * fragment and not the context of this aggregation (aggregated dependencies from another code fragment)
	 */
	public Set<String> compareExportLocationAggregations(CodeFragment codeFragment) {
		return this.importsDependencies.compareImports(codeFragment.getExportLocationAggregation());
	}
	
	/**
	 * Associates code fragment instance with aggregation
	 * 
	 * @param codeFragment - code fragment instance that should be associated with code fragment
	 */
	public void addCodeFragment(CodeFragment codeFragment) { this.codeFragments.add(codeFragment); }
	
	/**
	 * Returns merged code from each aggregated code fragment
	 * 
	 * @return code representation from aggregated code fragment
	 */
	public String getCode() {
		String code = null;
		for (CodeFragment codeFragment: this.codeFragments) {
			if (code == null) {
				code = codeFragment.getCode() + ";";
			} else {
				code = code + SPLEvolutionCore.CODE_FRAGMENT_SEPARATOR + codeFragment.getCode() + ";";
			}
		}
		return code;
	}
	
	/**
	 * Returns mapping of dependencies from all aggregated code fragments that are merge and resolved to associated template from PositiveVariationPointCandidateTemplates
	 * 
	 * @param positiveVariationPointCandidateTemplates - template with associated code callable forms
	 * @return code representation of one line code fragment
	 */
	public Map<String, String> getDependencies(PositiveVariationPointCandidateTemplates positiveVariationPointCandidateTemplates) {
		Map<String, String> dependenciesOfAggregatedFragment = new HashMap<String, String>();
		for (CodeFragment codeFragment: this.codeFragments) {
			this.importsDependencies.merge(codeFragment.getExportLocationAggregation());
			dependenciesOfAggregatedFragment.putAll(codeFragment.getDependencies(positiveVariationPointCandidateTemplates));
		}
		return dependenciesOfAggregatedFragment;
	}
	
	/**
	 * Returns all aggregated code fragments
	 * 
	 * @return all aggregated code fragments
	 */
	public List<CodeFragment> getCodeFragments() { return this.codeFragments; }

	/**
	 * Returns AST of Aggregated Code Fragment representation
	 * 
	 * @return returns JSON array from code statements
	 */
	@Override
	public JSONArray getCodeAst() throws IOException, InterruptedException {
		JSONObject lineOfCodeAst = ASTConverterClient.convertFromCodeToASTJSON(this.getCode());
		return (JSONArray) ((JSONObject) lineOfCodeAst.get("ast")).get("statements");
	}

	/**
	 * Get import dependencies from AggregatedCodeFragment in form of AST tree
	 * 
	 * @return JSON Array of dependencies exported to AST
	 */
	@Override
	public String getImportDependenciesAsCode() {
		return this.importsDependencies.buildAllImports();
	}

	/**
	 * Get import dependencies from this instance of AggreagtedCodeFragment in form of AST tree
	 * 
	 * @return JSON Array of dependencies exported to AST
	 */
	@Override
	public JSONArray getImportdependenciesAsAst() throws IOException, InterruptedException {
		JSONObject lineOfCodeAst = ASTConverterClient.convertFromCodeToASTJSON(this.getImportDependenciesAsCode());
		return (JSONArray) ((JSONObject) lineOfCodeAst.get("ast")).get("statements");
	}

	/**
	 * Returns ExportLocationAggregation object with aggregated and used imports from this instance of ExportLocationAggregation
	 * 
	 * @return ExportLocationAggregation object with aggregated and used imports from this instance of ExportLocationAggregation
	 */
	@Override
	public ExportLocationAggregation getExportLocationAggregation() {
		return this.importsDependencies;
	}

	/**
	 * Integration of used imports to AST of base application
	 * 
	 * @param  originalAst - native AST from base application where imports should be integrated
	 */
	@Override
	public void integrateImports(JSONObject originalAst) throws IOException, InterruptedException {
		this.importsDependencies.integrateImports(originalAst);
	}
}
