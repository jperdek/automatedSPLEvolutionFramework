package positiveVariabilityManagement.fragmentManagement.classIntroduction;

import positiveVariabilityManagement.fragmentManagement.model.CodeFragment;


/**
 * Interface for class introduction functionality
 * 
 * @author Jakub Perdek
 *
 */
public interface ClassIntroductionTemplate {

	/**
	 * Evaluates  condition if it beneficial to introduce class according to initialized/static settings
	 * 
	 * @param codeFragment - code fragment representation of 
	 * @return condition to decide if is beneficial to introduce class
	 */
	boolean isConditionToIntroduceClassMet(CodeFragment codeFragment);
}
