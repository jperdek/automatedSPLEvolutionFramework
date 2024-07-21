package positiveVariabilityManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.ExportAssetPlanner;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.AlreadyChosenVariationPointForInjectionException;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.InjectionIntoVariationPointValidator;
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
	 * Asset planning instance that provides the checking if injection can be created for particular asset
	 */
	private ExportAssetPlanner exportAssetPlanner;
	
	
	/**
	 * Initializes the functionality for managing the resources for code content injection in place multiple selected variation point into the resulting AST
	 * 
	 * @param exportAssetPlanner - asset planning instance that provides the checking if injection can be created for particular asset
	 */
	public VariationPointsContentInjection(ExportAssetPlanner exportAssetPlanner) {
		this.exportAssetPlanner = exportAssetPlanner;
		this.contentsOfVariationPointsMap = new HashMap<String, CodeFragment>();
	}
	
	/**
	 * Initializes the functionality for managing the resources for code content injection from another instance of a similar type
	 * -last used code fragment is set
	 * 
	 * @param variationPointsContentInjection - another instance of a similar type providing the initial settings
	 * @param exportAssetPlanner - asset planning instance that provides the checking if injection can be created for particular asset
	 * @throws AlreadyChosenVariationPointForInjectionException 
	 */
	public VariationPointsContentInjection(VariationPointsContentInjection variationPointsContentInjection, ExportAssetPlanner exportAssetPlanner) throws AlreadyChosenVariationPointForInjectionException {
		this.exportAssetPlanner = exportAssetPlanner;
		this.lastUsedCodeFragment = variationPointsContentInjection.getLastUsedCodeFragment();
		
		Map<String, CodeFragment> contentsOfVariationPointsOriginalMap = variationPointsContentInjection.getContentsOfVariationPoints();
		InjectionIntoVariationPointValidator.checkCodeFragmentsDependencies(contentsOfVariationPointsOriginalMap);
		this.contentsOfVariationPointsMap = new HashMap<String, CodeFragment>(contentsOfVariationPointsOriginalMap);
	}
	
	/**
	 * Adds code fragment into mapping of unique variation point identifiers to selected code fragments for further injection
	 * -last used code fragment is set
	 * 
	 * @param variationPointMarkerName - the unique variation point identifier, specifically the variation point marker name
	 * @param codeFragment - selected code fragment for further injection (which is going to be added into the map)
	 * @return true if code content is successfully prepared for content injection otherwise false
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws AlreadyChosenVariationPointForInjectionException 
	 */
	public boolean addCodeFragmentReference(String variationPointMarkerName, CodeFragment codeFragment) throws IOException, InterruptedException, AlreadyChosenVariationPointForInjectionException {
		InjectionIntoVariationPointValidator.checkDependencies(variationPointMarkerName, codeFragment);
		
		if (this.exportAssetPlanner.canUseAsset(codeFragment.getCodeAst().toJSONString())) {
			this.lastUsedCodeFragment = codeFragment;
			this.contentsOfVariationPointsMap.put(variationPointMarkerName, codeFragment);
			System.out.println("Code fragment has been added into assets.............................................." + codeFragment.getCode());
			System.out.println("....... Dependencies" + codeFragment.getImportDependenciesAsCode());
			return true;
		}
		System.out.println("Code fragment has not been added into assets.............................................." + codeFragment.getCode());
		System.out.println("....... Dependencies" + codeFragment.getImportDependenciesAsCode());
		return false;
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
