package evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment;

import java.util.Map;
import java.util.HashMap;


/**
 * Guards the limits in the planning of particular assets
 * 
 * @author Jakub Perdek
 *
 */
public class LoadedAssetsFrequencyManager {

	/**
	 * The name of script which assets are guarded according to specified limits
	 */
	private String scriptName;
	
	/**
	 * The usage frequency of particular asset during its planning for evolution or its subsequence
	 */
	private Map<String, Integer> exportLocationUsageFrequency;
	
	/**
	 * The limits that hold for frequency of particular asset planning
	 */
	private Map<String, Integer> exportLocationUsageFrequencyLimits;
	
	
	/**
	 * Instantiates the manager to guard the limits during particular asset planning
	 */
	public LoadedAssetsFrequencyManager() { this(""); }
	
	/**
	 * Instantiates the manager to guard the limits during particular asset planning
	 * 
	 * @param scriptName - the name of script which assets are guarded according to specified limits
	 */
	public LoadedAssetsFrequencyManager(String scriptName) {
		this.scriptName = scriptName;
		this.exportLocationUsageFrequency = new HashMap<String, Integer>();
		this.exportLocationUsageFrequencyLimits = new HashMap<String, Integer>();
	}
	
	/**
	 * Checks the possibility to use particular asset and marks its use if true
	 * 
	 * @param constructName - the name/identifier or particular asset that should be checked and used
	 * @return true if particular asset is allowed to be used otherwise false
	 */
	public boolean checkConstructUseAndMarkIfTrue(String constructName) {
		Integer maximalFrequency = this.exportLocationUsageFrequencyLimits.get(constructName);
		Integer locationUsageFrequency = this.exportLocationUsageFrequency.get(constructName); 
		if (maximalFrequency == null) { maximalFrequency = -1; }
		if (locationUsageFrequency == null) {
			this.exportLocationUsageFrequency.put(constructName, locationUsageFrequency.intValue() + 1);
			return true;
		}
		if (maximalFrequency.intValue() == -1 || 
				locationUsageFrequency.intValue() < maximalFrequency.intValue()) {
			this.exportLocationUsageFrequency.put(constructName, locationUsageFrequency.intValue() + 1);
			return true;
		}
		return false;
	}
	
	/**
	 * Inserts the assets with associated limits if are not set to be guarded otherwise not
	 * 
	 * @param constructName - the name/identifier or particular asset that should be checked and used
	 * @param usageLimit - the limit that will guard the possibility to plan particular asset
	 */
	public void addLocationIfNotExistWithLimit(String constructName, int usageLimit) {
		if (this.exportLocationUsageFrequencyLimits.containsKey(constructName)) {
			this.exportLocationUsageFrequencyLimits.put(constructName, usageLimit);
			this.exportLocationUsageFrequency.put(constructName, 0);
		}
	}
	
	/**
	 * Returns the current usage frequency of particular asset during its planning in evolution (iterations) or its subsequence
	 * 
	 * @param constructName - the name/identifier or particular asset that should be checked and used
	 * @return - the current usage frequency of particular asset during its planning in evolution (iterations) or its subsequence
	 */
	public int getExportLocationUsageFrequency(String constructName) {
		if (this.exportLocationUsageFrequency.containsKey(constructName)) {
			return this.exportLocationUsageFrequency.get(constructName);
		}
		return 0;
	}
	
	/**
	 * Inserts asset with its limits that should guarded
	 * 
	 * @param constructName - the name*identifier of particular asset
	 * @param usageLimit - the limit that will guard the possibility to plan particular asset
	 */
	public void addLocationWithLimit(String constructName, int usageLimit) {
		this.exportLocationUsageFrequencyLimits.put(constructName, usageLimit);
		this.exportLocationUsageFrequency.put(constructName, 0);
	}
	
	/**
	 * Returns the name (if any) of used script which assets are guarded
	 * 
	 * @return the name of used script (if any) which assets are guarded
	 */
	public String getScriptName() { return this.scriptName; }
}
