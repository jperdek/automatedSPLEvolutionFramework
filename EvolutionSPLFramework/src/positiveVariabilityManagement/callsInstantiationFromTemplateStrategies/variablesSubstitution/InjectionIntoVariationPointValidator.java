package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import java.util.Map;
import java.util.Map.Entry;

import codeContext.processors.export.ExportLocationAggregation;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.AlreadyChosenVariationPointForInjectionException;
import positiveVariabilityManagement.fragmentManagement.model.CodeFragment;


/**
 * Helper functionality to validate injections into particular variation points according to their identifiers
 * -variation point identifiers are harvested from variation point markers from the code
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class InjectionIntoVariationPointValidator {

	/**
	 * Checks if selected injection dependencies hold across all used CodeFragments
	 * 
	 * @param contentsOfVariationPointsOriginalMap - the mapping of selected variation points with code fragments
	 * @throws AlreadyChosenVariationPointForInjectionException - exception thrown in case of violating injection dependency
	 */
	public static void checkCodeFragmentsDependencies(Map<String, CodeFragment> contentsOfVariationPointsOriginalMap) throws AlreadyChosenVariationPointForInjectionException {
		String variationPointNameAssociatedToCodeFragment;
		CodeFragment codeFragmentToCheck;
		for (Entry<String, CodeFragment> codeFragmentOnVariationPoint: contentsOfVariationPointsOriginalMap.entrySet()) {
			variationPointNameAssociatedToCodeFragment = codeFragmentOnVariationPoint.getKey();
			codeFragmentToCheck = codeFragmentOnVariationPoint.getValue();
			
			InjectionIntoVariationPointValidator.checkDependencies(variationPointNameAssociatedToCodeFragment, codeFragmentToCheck);
		}
	}
	
	/**
	 * Checks if selected injection dependency hold across all used CodeFragments
	 * 
	 * @param variationPointNameAssociatedToCodeFragment - the name/identifier of selected/chosen variation point where code fragment should be injected
	 * @param variationPointNameInnerDependency - the name/identifier of dependent variation point restrictively to where code fragment must be injected
	 * @throws AlreadyChosenVariationPointForInjectionException - exception thrown in case of violating injection dependency where these identifiers not match
	 */
	public static void checkDependencies(String variationPointNameAssociatedToCodeFragment, String variationPointNameInnerDependency) throws AlreadyChosenVariationPointForInjectionException {
		if (!variationPointNameAssociatedToCodeFragment.equals(variationPointNameInnerDependency)) {
			throw new AlreadyChosenVariationPointForInjectionException("Another variation point: " + 
					variationPointNameAssociatedToCodeFragment + " has already been chosen for injection. Occured in exported dependencies.");
		}
	}
	
	/**
	 * Checks if selected injection dependency hold for particular code fragment provided as argument and existing dependency represented as variation point name/identifier
	 * 
	 * @param variationPointMarkerName - the name/identifier of dependent variation point restrictively to where code fragment must be injected
	 * @param codeFragmentToCheck - code fragment which will be checked in case where injection dependency is set
	 * @throws AlreadyChosenVariationPointForInjectionException - exception thrown in case of violating injection dependency for provided code fragment
	 */
	public static void checkDependencies(String variationPointMarkerName, CodeFragment codeFragmentToCheck) throws AlreadyChosenVariationPointForInjectionException {
		ExportLocationAggregation exportLocationAggregation = codeFragmentToCheck.getExportLocationAggregation();
		String variationPointNameInnerDependency;
		if (exportLocationAggregation.belongsToParticularVariationPoint()) {
			variationPointNameInnerDependency = exportLocationAggregation.getVariationPointToInjectIdentifier();
			InjectionIntoVariationPointValidator.checkDependencies(variationPointMarkerName, variationPointNameInnerDependency);
		}
	}
	
	/**
	 * Returns information if conditions to inject content on variation point hold
	 * 
	 * @param variationPointMarkerName - the name/identifier of dependent variation point restrictively to where code fragment must be injected
	 * @param associatedAggregatedLocationExports - object with exports belonging to callable construct with information about 
	 * @return true if conditions to inject content on variation point hold otherwise false
	 * @throws AlreadyChosenVariationPointForInjectionException
	 */
	public static boolean canBeInjectedIntoVariationPoint(String variationPointMarkerName, ExportLocationAggregation associatedAggregatedLocationExports) {
		String variationPointNameInnerDependency;
		if (associatedAggregatedLocationExports.belongsToParticularVariationPoint()) {
			variationPointNameInnerDependency = associatedAggregatedLocationExports.getVariationPointToInjectIdentifier();
			if (!variationPointMarkerName.equals(variationPointNameInnerDependency)) { return false; }
		}
		return true;
	}
}
