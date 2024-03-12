package positiveVariabilityManagement.fragmentManagement.model;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import codeContext.processors.export.ExportLocationAggregation;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Representation of code fragment that is inserted in case of positive variability
 * 
 * 
 * @author Jakub Perdek
 *
 */
public interface CodeFragment {

	/**
	 * Returns code from CodeFragment
	 * 
	 * @return code representation of one line code fragment
	 */
	public String getCode();
	
	/**
	 * Create code from import dependencies of given instance of CodeFragment
	 * 
	 * @return code representation of given instance of code fragment
	 */
	public String getImportDependenciesAsCode();
	
	/**
	 * Returns ExportLocationAggregation object with aggregated and used imports from CodeFragment
	 * 
	 * @return ExportLocationAggregation object with aggregated and used imports from CodeFragment
	 */
	public ExportLocationAggregation getExportLocationAggregation();
	
	/**
	 * Compares given instance of Code Fragment with another CodeFragment instance according to its imports 
	 * 
	 * @param codeFragment - an instance of CodeFragment that is compared according to its imports
	 * @return the set of exported location aggregations/dependencies that are in code 
	 * fragment and not in aggregation of this code fragment's context
	 */
	public Set<String> compareExportLocationAggregations(CodeFragment codeFragment);
	
	/**
	 * Get import dependencies from CodeFragment in form of AST tree
	 * 
	 * @return JSON Array of dependencies exported to AST
	 * @throws IOException - the exception for various problems with file loading
	 * @throws InterruptedException - the exception thrown during interruption
	 */
	public JSONArray getImportdependenciesAsAst() throws IOException, InterruptedException;
	
	/**
	 * Integration of used imports to AST of base application
	 * 
	 * @param  originalAst - native AST from base application where imports should be integrated
	 * @throws IOException - the exception for various problems with file loading
	 * @throws InterruptedException - the exception thrown during interruption
	 */
	public void integrateImports(JSONObject originalAst) throws IOException, InterruptedException;
	
	/**
	 * Returns AST of Code Fragment representation
	 * 
	 * @return returns JSON array from code statements
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public JSONArray getCodeAst() throws IOException, InterruptedException;
	
	/**
	 * Returns mapping of dependencies from callable form according to associated template from PositiveVariationPointCandidateTemplates
	 * 
	 * @param positiveVariationPointCandidateTemplates - template with associated callable forms
	 * @return code representation of one line code fragment
	 */
	public Map<String, String> getDependencies(PositiveVariationPointCandidateTemplates positiveVariationPointCandidateTemplates);
}
