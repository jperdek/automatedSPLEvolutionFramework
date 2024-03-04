package positiveVariabilityManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import positiveVariabilityManagement.fragmentManagement.model.CodeFragment;


/**
 * Representation of resources for code content injection in place multiple selected variation point into the resulting AST
 * 
 * @author Jakub Perdek
 *
 */
public class VariationPointsContentInjection {	
	
	/**
	 * The mapping of unique variation point identifier to selected code fragments for further injection
	 */
	private Map<String, CodeFragment> contentsOfVariationPointsMap;
	
	/**
	 * The last added/used code fragment to the mapping
	 * - only for variability management purposes
	 */
	private CodeFragment lastUsedCodeFragment = null;
	
	
	/**
	 * Initializes the functionality for managing the resources for code content injection in place multiple selected variation point into the resulting AST
	 */
	public VariationPointsContentInjection() {
		this.contentsOfVariationPointsMap = new HashMap<String, CodeFragment>();
	}
	
	/**
	 * Initializes the functionality for managing the resources for code content injection from another instance of a similar type
	 * -last used code fragment is set
	 * 
	 * @param variationPointsContentInjection - another instance of a similar type providing the initial settings
	 */
	public VariationPointsContentInjection(VariationPointsContentInjection variationPointsContentInjection) {
		this.lastUsedCodeFragment = variationPointsContentInjection.getLastUsedCodeFragment();
		this.contentsOfVariationPointsMap = new HashMap<String, CodeFragment>(
				variationPointsContentInjection.getContentsOfVariationPoints());
	}
	
	/**
	 * Adds code fragment into mapping of unique variation point identifiers to selected code fragments for further injection
	 * -last used code fragment is set
	 * 
	 * @param variationPointMarkerName - the unique variation point identifier, specifically the variation point marker name
	 * @param codeFragment - selected code fragment for further injection (which is going to be added into the map)
	 */
	public void addCodeFragmentReference(String variationPointMarkerName, CodeFragment codeFragment) {
		this.lastUsedCodeFragment = codeFragment;
		this.contentsOfVariationPointsMap.put(variationPointMarkerName, codeFragment);
	}
	
	/**
	 * Returns the last used code fragment (added into the mapping)
	 * 
	 * @return the last used code fragment (added into the mapping)
	 */
	public CodeFragment getLastUsedCodeFragment() { return this.lastUsedCodeFragment; }
	
	/**
	 * Returns the mapping of unique positive variability variation point identifier to selected code fragments for further injection
	 * 
	 * @return the mapping of unique positive variability variation point identifier to selected code fragments for further injection
	 */
	public Map<String, CodeFragment> getContentsOfVariationPoints() { return this.contentsOfVariationPointsMap; }
	
	/**
	 * Returns the associated code fragment with provided marker name (the unique positive variability variation point identifier) in the parameter of this function
	 *  
	 * @param markerName - the unique positive variability variation point identifier, specifically the variation point marker name
	 * @return the associated code fragment with provided marker name (the unique positive variability variation point identifier) in the parameter of this function
	 */
	public CodeFragment getContentAccordingToMarkerName(String markerName) {
		return this.contentsOfVariationPointsMap.get(markerName);
	}
	
	/**
	 * Returns the list of code fragments from all added/selected variation points associated with this instance
	 * 
	 * @return The list of code fragments from all added/selected variation points associated with this instance
	 */
	public List<CodeFragment> getCodeFragments() { return new ArrayList<CodeFragment>(this.contentsOfVariationPointsMap.values()); }
}
