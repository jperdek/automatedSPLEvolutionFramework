package positiveVariabilityManagement.fragmentManagement.model.annotable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import codeContext.processors.export.ExportLocationAggregation;
import positiveVariabilityManagement.fragmentManagement.model.CodeFragment;
import positiveVariabilityManagement.fragmentManagement.model.LineOfCode;
import positiveVariabilityManagement.fragmentManagement.model.VariableDeclarationLineOfCode;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Representation of class code fragment 
 * -method is perceived as constructor - main assumption
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class ClassContextCodeFragment extends MethodContextCodeFragment {
	
	/**
	 * The name of class
	 */
	private String newClassName;
	
	/**
	 * The list of method context code fragments that belongs to this class context
	 */
	private List<MethodContextCodeFragment> methodContextCodeFragment;
	
	/**
	 * The list of declared class variables that belongs to the class context
	 */
	private List<VariableDeclarationLineOfCode> classVariableDeclarations;
	
	/**
	 * The aggregation of associated imports
	 */
	private ExportLocationAggregation importsDependencies;
	
	/**
	 * The call to instantiate the class
	 */
	private String classCall;

	
	/**
	 * Instantiates the class code context and initializes aggregation of exports/imports
	 * 
	 * @param newClassName - the class name of code fragment
	 * @param selectedTemplate - the positive variation point candidate template with all all possible code in this place that form the context
	 */
	public ClassContextCodeFragment(String newClassName, PositiveVariationPointCandidateTemplates selectedTemplate) {
		super("constructor", true, selectedTemplate);
		this.importsDependencies = new ExportLocationAggregation();
		this.newClassName = newClassName;
	}
	
	/**
	 * Inserts the code fragment of any simpler type to the class
	 * 
	 * @param innerFragment - the inner code fragment that should be inserted
	 */
	public void addCodeFragment(CodeFragment innerFragment) { 
		if (innerFragment instanceof MethodContextCodeFragment) {
			this.methodContextCodeFragment.add((MethodContextCodeFragment) innerFragment);
		} else if (innerFragment instanceof VariableDeclarationLineOfCode) {
			this.classVariableDeclarations.add((VariableDeclarationLineOfCode) innerFragment);
		} else if (innerFragment instanceof LineOfCode){ //propagate to lower context
			super.addCodeFragment(innerFragment);
			return;
		}
		//super.addCodeFragment(innerFragment); //TO - DO add dependencies from calls and change init to constructs
	}
	
	/**
	 * Add functionality (code fragment) to class constructor
	 * 
	 * @param codeFragment - the code fragment with functionality which should be inserted into constructor
	 */
	public void addToConstructor(CodeFragment codeFragment) { super.addCodeFragment(codeFragment); }
	
	/**
	 * Returns the class name (the name of class context)
	 * 
	 * @return the class name (the name of class context)
	 */
	public String getClassName() { return this.newClassName; }
	
	@Override
	/**
	 * Returns the code with the call of the class constructor
	 * 
	 * @return the code with the call of the class constructor
	 */
	public String getCode() { return this.classCall; }
	
	@Override
	/**
	 * Returns the dependencies with introduced class 
	 * 
	 * @return the dependencies with introduced class 
	 */
	public Map<String, String> getDependencies(PositiveVariationPointCandidateTemplates positiveVariationPointCandidateTemplates) {
		//for (VariableDeclarationLineOfCode declarationLineOfCode: this.classVariableDeclarations) {
		//	declarationLineOfCode.getDependencies(this.selectedTemplate);
		//}
		
		return super.getDependencies(super.selectedTemplate); //as dependencies counts only constructor, each called methopd has their own
	}
	
	/**
	 * Compares export location aggregation according to the used imports/dependencies
	 * 
	 * @return the set of exported location aggregations/dependencies that are in code 
	 * fragment and not in aggregation of this class context
	 */
	public Set<String> compareExportLocationAggregations(CodeFragment codeFragment) {
		return this.importsDependencies.compareImports(codeFragment.getExportLocationAggregation());
	}
}
