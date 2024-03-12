package positiveVariabilityManagement.fragmentManagement.classIntroduction;

import positiveVariabilityManagement.fragmentManagement.model.AggregatedCodeFragment;
import positiveVariabilityManagement.fragmentManagement.model.CodeFragment;
import positiveVariabilityManagement.fragmentManagement.model.LineOfCode;
import positiveVariabilityManagement.fragmentManagement.model.annotable.MethodContextCodeFragment;
import splEvolutionCore.SPLEvolutionCore;
import java.util.List;


/**
 * Functionality to managed transformation into class from code fragments consisting from methods
 * 
 * @author Jakub Perdek
 *
 */
public class ClassAsMethodBasedAggregation implements ClassIntroductionTemplate {
	
	/**
	 * Checks if is beneficial to transform code fragments consisting from methods into class
	 * 
	 * @param codeFragment - instance of code fragment that is introduced
	 * @return condition if class is beneficial to introduce
	 */
	public boolean isConditionToIntroduceClassMet(CodeFragment codeFragment) {
		List<CodeFragment> codeFragments;
		int numberOfMethods = 0;
		if (codeFragment instanceof LineOfCode) { return false; }

		if (codeFragment instanceof AggregatedCodeFragment && SPLEvolutionCore.NUMBER_METHODS_TO_INTRODUCE_CLASS > 1) {
			codeFragments = ((AggregatedCodeFragment) codeFragment).getCodeFragments();
			for (CodeFragment analyzedCodeFragment: codeFragments) {
				if (analyzedCodeFragment instanceof MethodContextCodeFragment) {
					numberOfMethods++;
				}
			}
			
			if (numberOfMethods >= SPLEvolutionCore.NUMBER_METHODS_TO_INTRODUCE_CLASS) {
				return true;
			}
		}
		return false;
	}
}
