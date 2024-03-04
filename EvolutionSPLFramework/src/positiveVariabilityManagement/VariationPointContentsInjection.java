package positiveVariabilityManagement;

import java.util.List;
import positiveVariabilityManagement.fragmentManagement.model.CodeFragment;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Representation of resources for code content injection in place of one particular variation point into the resulting AST
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class VariationPointContentsInjection {

	/**
	 * Template with associated code callable forms - selected set of calls of existing or merged functionality
	 */
	private PositiveVariationPointCandidateTemplates positiveVariationPointCandidateTemplates;
	
	/**
	 *  The list of code fragments for further synthesis, which holding (after) all decisions  under the positive variability (selections, structural information)
	 */
	private List<CodeFragment> functionalityOnVarPointsToPossibilitiesMap;
	
	
	/**
	 * Instantiates the instance of VariationPointContentInjection to perform content injection
	 * 
	 * @param positiveVariationPointCandidateTemplates - template with associated code callable forms - selected set of calls of existing or merged functionality
	 * @param functionalityOnVarPointsToPossibilitiesMap - the list of code fragments for further synthesis, which holding all decisions (selections, structural information)
	 */
	public VariationPointContentsInjection(PositiveVariationPointCandidateTemplates positiveVariationPointCandidateTemplates,
			List<CodeFragment> functionalityOnVarPointsToPossibilitiesMap) {
		this.positiveVariationPointCandidateTemplates = positiveVariationPointCandidateTemplates;
		this.functionalityOnVarPointsToPossibilitiesMap = functionalityOnVarPointsToPossibilitiesMap;
	}
	
	/**
	 * Returns the list of code fragments for further synthesis, which holding all decisions under the positive variability (selections, structural information)
	 * 
	 * @return the list of code fragments for further synthesis, which holding all decisions under the positive variability (selections, structural information)
	 */
	public PositiveVariationPointCandidateTemplates getPositiveVariationPointCandidateTemplates() {
		return this.positiveVariationPointCandidateTemplates;
	}
	
	/**
	 * Returns the number of code fragments for further synthesis, which holding all decisions under the positive variability (selections, structural information)
	 * 
	 * @return the number of code fragments for further synthesis, which holding all decisions under the positive variability (selections, structural information)
	 */
	public int getNumberOfCodeFragments() { return this.functionalityOnVarPointsToPossibilitiesMap.size(); }
	
	/**
	 * Returns the i-th code fragment for further synthesis, which holding all decisions under the positive variability (selections, structural information)
	 * 
	 * @param codeFragmentNumber - the index to i-th code fragment
	 * @return the i-th code fragment for further synthesis, which holding all decisions under the positive variability (selections, structural information)
	 */
	public CodeFragment getCodeFragment(int codeFragmentNumber) {
		return this.functionalityOnVarPointsToPossibilitiesMap.get(codeFragmentNumber); 
	}
	
	/**
	 * Evaluates the similarity between the dependencies in comparison with another instance of VariationPointContentsInjection
	 * 
	 * @param variationPointContentsInjection - another instance of VariationPointContentsInjection for comparable purposes to evaluate similarity
	 * @return true if both instances are similar otherwise false
	 */
	public boolean evaluateSimilarityOnDependencies(VariationPointContentsInjection variationPointContentsInjection) {
		if (this.functionalityOnVarPointsToPossibilitiesMap.size() != variationPointContentsInjection.getNumberOfCodeFragments()) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns the list of code fragments for further synthesis, which holding all decisions under the positive variability (selections, structural information)
	 * 
	 * @return the list of code fragments for further synthesis, which holding all decisions under the positive variability (selections, structural information)
	 */
	public List<CodeFragment> getCodeFragments() { return this.functionalityOnVarPointsToPossibilitiesMap; } 
}
