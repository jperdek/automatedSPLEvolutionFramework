package positiveVariabilityManagement.fragmentManagement.classIntroduction;

import positiveVariabilityManagement.fragmentManagement.model.AggregatedCodeFragment;
import positiveVariabilityManagement.fragmentManagement.model.CodeFragment;
import positiveVariabilityManagement.fragmentManagement.model.LineOfCode;
import positiveVariabilityManagement.fragmentManagement.model.VariableDeclarationLineOfCode;
import splEvolutionCore.SPLEvolutionCore;
import java.util.List;


/**
 * Functionality to managed transformation into class from code fragments consisting from variable declarations
 * 
 * @author Jakub Perdek
 *
 */
public class ClassAsVariableDeclarationAggregation implements ClassIntroductionTemplate {

	/**
	 * Checks if is beneficial to transform code fragments consisting from variable declarations into class
	 * 
	 * @param processedCodeFragment - instance of code fragment that is introduced
	 * @return condition if class is beneficial to introduce
	 */
	public boolean isConditionToIntroduceClassMet(CodeFragment codeFragment) {
		List<CodeFragment> codeFragments;
		int numberOfVariableDeclarations = 0;
		if (codeFragment instanceof LineOfCode && SPLEvolutionCore.NUMBER_DECLARATIONS_TO_INTRODUCE_CLASS > 1) {
			return false;
		}
		if (codeFragment instanceof AggregatedCodeFragment && SPLEvolutionCore.NUMBER_DECLARATIONS_TO_INTRODUCE_CLASS > 1) {
			codeFragments = ((AggregatedCodeFragment) codeFragment).getCodeFragments();
			for (CodeFragment analyzedCodeFragment: codeFragments) {
				if (analyzedCodeFragment instanceof VariableDeclarationLineOfCode) {
					numberOfVariableDeclarations++;
				}
			}
			
			if (numberOfVariableDeclarations >= SPLEvolutionCore.NUMBER_DECLARATIONS_TO_INTRODUCE_CLASS) {
				return true;
			}
		}
		
		return false;
	}
}
