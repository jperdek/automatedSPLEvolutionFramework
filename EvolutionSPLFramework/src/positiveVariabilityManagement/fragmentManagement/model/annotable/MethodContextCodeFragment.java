package positiveVariabilityManagement.fragmentManagement.model.annotable;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import codeConstructsEvaluation.transformation.ASTConverterClient;
import codeContext.processors.export.ExportLocationAggregation;
import positiveVariabilityManagement.fragmentManagement.model.CodeFragment;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Representation of method code fragment 
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class MethodContextCodeFragment implements CodeFragment {
	
	/**
	 * The name of method
	 */
	private String newMethodName;
	
	/**
	 * The list of code fragments of inner methods (declared inside method)
	 */
	protected List<MethodContextCodeFragment> methodContextCodeFragment;
	
	/**
	 * The mapping of dependencies from callable form according to associated template from PositiveVariationPointCandidateTemplates
	 */
	protected Map<String, String> methodDependenciesAsParameters;
	
	/**
	 * The template with associated callable forms from associated variation point
	 */
	protected PositiveVariationPointCandidateTemplates selectedTemplate;
	
	/**
	 * The import dependencies of this method
	 */
	protected ExportLocationAggregation importsDependencies;
	
	/**
	 * The main code fragment of the method - the represented as functionality that is performed inside method
	 */
	protected CodeFragment codeFragment;
	
	/**
	 * The string representation specifying how to call this method - callable representation string of this method
	 */
	protected String methodCall;
	
	/**
	 * true if method is class method otherwise not
	 */
	protected boolean isClass = false;
	
	
	/**
	 * Instantiates the code fragment for method/class method
	 * 
	 * @param newMethodName - the name of method
	 * @param isClass - specifying if method belongs to class - true if method is class method otherwise not
	 * @param selectedTemplate - the template with associated callable forms from associated variation point
	 */
	public MethodContextCodeFragment(String newMethodName, boolean isClass, 
			PositiveVariationPointCandidateTemplates selectedTemplate) {
		this.methodDependenciesAsParameters = new HashMap<String, String>();
		this.selectedTemplate = selectedTemplate;
		this.newMethodName = newMethodName;
		this.importsDependencies = new ExportLocationAggregation();
		this.isClass = isClass;
	}

	@Override
	/**
	 * Returns the code with the call of the method (in JavaScript/TypeScript)
	 * 
	 * @return the code with the call of the method (in JavaScript/TypeScript)
	 */
	public String getCode() {
		if (this.isClass) {
			return "function " + this.newMethodName + "()";
		} else {
			return null;
		}
	}
	
	/**
	 * Inserts the code fragment inside body of method
	 * 
	 * @param innerFragment - the code fragment that is going to be inserted into the body of the method
	 */
	public void addCodeFragment(CodeFragment innerFragment) { 
		this.codeFragment = innerFragment;
		this.methodDependenciesAsParameters.putAll(innerFragment.getDependencies(this.selectedTemplate));
	}

	/**
	 * Returns the main code fragment of the method - the represented as functionality that is performed inside method
	 * 
	 * @return the main code fragment of the method - the represented as functionality that is performed inside method
	 */
	public CodeFragment getCodeFragment() { return this.codeFragment; }
	
	@Override
	/**
	 * Returns mapping of dependencies from callable form according to associated template from PositiveVariationPointCandidateTemplates
	 * 
	 * @param positiveVariationPointCandidateTemplates - template with associated callable forms
	 * @return the mapping of the dependency names to their source (for each callable construct inside body of this represented method)
	 */
	public Map<String, String> getDependencies(PositiveVariationPointCandidateTemplates positiveVariationPointCandidateTemplates) {
		return this.methodDependenciesAsParameters;
	}

	@Override
	/**
	 * Returns AST of Method representation
	 * 
	 * @return returns JSON array of code statements from the method body
	 */
	public JSONArray getCodeAst() throws IOException, InterruptedException {
		JSONObject lineOfCodeAst = ASTConverterClient.convertFromCodeToASTJSON(this.getCode());
		return (JSONArray) ((JSONObject) lineOfCodeAst.get("ast")).get("statements");
	}

	@Override
	/**
	 * Create code from all import dependencies necessary for this method representation 
	 * 
	 * @return the code of all import dependencies necessary for this method representation 
	 */
	public String getImportDependenciesAsCode() {
		return this.importsDependencies.buildAllImports();
	}

	@Override
	/**
	 * Get import dependencies in form of AST tree
	 * 
	 * @return JSON Array of dependencies exported to AST
	 */
	public JSONArray getImportdependenciesAsAst() throws IOException, InterruptedException {
		JSONObject lineOfCodeAst = ASTConverterClient.convertFromCodeToASTJSON(this.getImportDependenciesAsCode());
		return (JSONArray) ((JSONObject) lineOfCodeAst.get("ast")).get("statements");
	}

	@Override
	/**
	 * Returns ExportLocationAggregation object with aggregated and used imports
	 * 
	 * @return ExportLocationAggregation object with aggregated and used imports
	 */
	public ExportLocationAggregation getExportLocationAggregation() {
		return this.importsDependencies;
	}

	@Override
	/**
	 * Integration of used imports to AST of base application
	 * 
	 * @param originalAst - native AST from base application where imports should be integrated
	 */
	public void integrateImports(JSONObject originalAst) throws IOException, InterruptedException {
		this.importsDependencies.integrateImports(originalAst);
	}
	
	/**
	 * Compares this (method code fragment) and another code fragment representations according to their imported 
	 * dependencies (ExportLocationAggregation objects)
	 * 
	 * @return the set of not matched exports from imports aggregation from this method in comparison with the one (from another code fragment) provided in argument
	 */
	public Set<String> compareExportLocationAggregations(CodeFragment codeFragment) {
		return this.importsDependencies.compareImports(codeFragment.getExportLocationAggregation());
	}
}
