package positiveVariabilityManagement.fragmentManagement.classIntroduction;

import java.util.List;

import positiveVariabilityManagement.fragmentManagement.model.AggregatedCodeFragment;
import positiveVariabilityManagement.fragmentManagement.model.CodeFragment;
import positiveVariabilityManagement.fragmentManagement.model.annotable.ClassContextCodeFragment;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Functionality to introduce class during evolution of positive variability
 * -variables are moved, encapsulated and initialized in class
 * -main function is transformed into constructor and other are connected to the class
 * 
 * @author Jakub Perdek
 *
 */
public abstract class ClassIntroduction {
	/**
	 * ID of new class, used in case that names are not automatically generated
	 */
	private static int classIntroductionId = 0;
	/**
	 * Helper to create class as method based aggregation
	 */
	private static ClassAsMethodBasedAggregation classAsMethodBasedAggregation = new ClassAsMethodBasedAggregation();
	/**
	 * Helper to create class as variable based aggregation
	 */
	private static ClassAsVariableDeclarationAggregation classAsVariableDeclarationAggregation = new ClassAsVariableDeclarationAggregation();
	
	/**
	 * Condition if is beneficial to introduce class is checked
	 * 
	 * @param processedCodeFragment - instance of code fragment that is introduced
	 * @return condition if class can be introduced or is beneficial to introduce
	 */
	public static boolean checkConditionForClassIntroduction(CodeFragment processedCodeFragment) {
		if (ClassIntroduction.classAsMethodBasedAggregation.isConditionToIntroduceClassMet(processedCodeFragment) ||
				ClassIntroduction.classAsVariableDeclarationAggregation.isConditionToIntroduceClassMet(processedCodeFragment)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Introduces class from available code fragment
	 * 
	 * @param codeFragment - used code fragment that will be recreated into class
	 * @param selectedTemplate - template to load used imports
	 * @return representation of class code fragment with associated content
	 */
	public static ClassContextCodeFragment incorporateIntoClass(CodeFragment codeFragment, PositiveVariationPointCandidateTemplates selectedTemplate) {
		String generatedClassName = "ClassIntroduction" + ClassIntroduction.classIntroductionId; // TO-DO genrate according to context
		ClassIntroduction.classIntroductionId += 1;
		ClassContextCodeFragment introducedClass = new ClassContextCodeFragment(generatedClassName, selectedTemplate);
		if (ClassIntroduction.checkConditionForClassIntroduction(codeFragment)) { return null; }
		
		List<CodeFragment> codeFragments;
		if (codeFragment instanceof AggregatedCodeFragment) {
			codeFragments = ((AggregatedCodeFragment) codeFragment).getCodeFragments();
			for (CodeFragment analyzedCodeFragment: codeFragments) {
				introducedClass.addCodeFragment(analyzedCodeFragment);
			}
		}
		return introducedClass;
	}
}
