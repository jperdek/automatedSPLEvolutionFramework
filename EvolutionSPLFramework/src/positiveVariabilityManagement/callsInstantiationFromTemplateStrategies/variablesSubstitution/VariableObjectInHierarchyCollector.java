package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codeContext.InnerContext;
import codeContext.objects.VariableObject;
import splEvolutionCore.DebugInformation;


/**
 * Collector of currently available variables objects including variables and parameters performed under hierarchies
 * -the collecting should be configured for particular programming language to prevent defects and inconsistencies
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class VariableObjectInHierarchyCollector {

	/**
	 * Logger to track harvesting of parameters from hierarchic entity
	 */
	private static final Logger logger = LoggerFactory.getLogger(VariableObjectInHierarchyCollector.class);

	/**
	 * Path from initial context in hierarchy to current place in hierarchy
	 */
	private List<InnerContext> pathInHierarchyToCurrentPosition;
	
	/**
	 * Configuration for getting actually available functionality that can be substituted in code
	 */
	private ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration;
	
	/**
	 * The aggregation of all collected parameters for particular point in the program aggregated according to depth in the hierarchy
	 */
	private Map<Integer, List<VariableObject>> collectedParametersInHierarchy;
	
	/**
	 * The aggregation of all collected local variables for particular point in the program aggregated according to depth in the hierarchy
	 */
	private Map<Integer, List<VariableObject>> collectedLocalVariablesInHierarchy;
	
	/**
	 * Depth of current searched object
	 */
	private int currentSearchedObjectDepth = -1;
	
	
	
	//global variables are not persisted in hierarchy but they are determined with their position
	/**
	 * Initializes the lists to manage collected variables and parameters in the hierarchy
	 * 
	 * @param actualScriptVariablesToSubstituteConfiguration - configuration for getting actually available functionality that can be substituted in code
	 */
	public VariableObjectInHierarchyCollector(ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration) {
		this.actualScriptVariablesToSubstituteConfiguration = actualScriptVariablesToSubstituteConfiguration;
		this.pathInHierarchyToCurrentPosition = new ArrayList<InnerContext>();
		this.collectedLocalVariablesInHierarchy = new HashMap<Integer, List<VariableObject>>();
		this.collectedParametersInHierarchy = new HashMap<Integer, List<VariableObject>>();
	}
	
	/**
	 * Observed the current depth of searched object for collected objects in hierarchy
	 */
	public void setCurrentObjectDepthForCollectedObjectsInHierarhy(int currentSearchedObjectDepth) {
		this.currentSearchedObjectDepth = currentSearchedObjectDepth;
	}
	
	/**
	 * Returns the last context lying on the path - this is search context
	 * - works only if data have been already collected 
	 * 
	 * @return the last context lying on the path - this is search context
	 */
	public InnerContext getSearchedContextIfExists() {
		int lastContextFromPath = this.pathInHierarchyToCurrentPosition.size() - 1;
		if (lastContextFromPath == -1) { return null; } 
		return this.pathInHierarchyToCurrentPosition.get(lastContextFromPath);
	}
	
	/**
	 * Inserts inner code context into paths directly accoring to that order from the root to the current place
	 *  
	 * @param innerContext
	 */
	public void addContextToPath(InnerContext innerContext) {
		this.pathInHierarchyToCurrentPosition.add(innerContext);
	}
	
	/**
	 * Collects and aggregates the harvested parameters under current depth
	 * 
	 * @param harvestedParameters - the list of local variables harvested from currently available parameters for observed point in program
	 * @param currentDepth - current depth in hierarchy where provided list of parameters were harvested
	 */
	public void collectParameters(List<VariableObject> harvestedParameters, int currentDepth) {
		List<VariableObject> collectedParametersFromCurrentLayer;
		
		if (!this.collectedParametersInHierarchy.containsKey(currentDepth)) {
			collectedParametersFromCurrentLayer = new ArrayList<VariableObject>();
		} else {
			collectedParametersFromCurrentLayer = this.collectedParametersInHierarchy.get(currentDepth);
		}
		collectedParametersFromCurrentLayer.addAll(harvestedParameters);
		this.collectedParametersInHierarchy.put(currentDepth, collectedParametersFromCurrentLayer);
	}
	
	/**
	 * Returns the maximal depth reached during parameter collection
	 * 
	 * @return the maximal depth reached during parameter collection
	 */
	public int getMaximalReachedDepthDuringParameterCollection() { return this.collectedParametersInHierarchy.size(); }
	
	/**
	 * Collects and aggregates the harvested local variables under current depth
	 * 
	 * @param harvestedLocalVariables - the list of local variables harvested from currently available objects for observed point in program
	 * @param currentDepth - current depth in hierarchy where provided list of local variables were harvested
	 */
	public void collectLocalVariables(List<VariableObject> harvestedLocalVariables, int currentDepth) {
		List<VariableObject> collectedLocalVariablesFromCurrentLayer;
		
		if (!this.collectedLocalVariablesInHierarchy.containsKey(currentDepth)) {
			collectedLocalVariablesFromCurrentLayer = new ArrayList<VariableObject>();
		} else {
			collectedLocalVariablesFromCurrentLayer = this.collectedLocalVariablesInHierarchy.get(currentDepth);
		}
		collectedLocalVariablesFromCurrentLayer.addAll(harvestedLocalVariables);
		this.collectedLocalVariablesInHierarchy.put(currentDepth, collectedLocalVariablesFromCurrentLayer);
	}
	
	/**
	 * Returns the maximal depth reached during local variables collection
	 * 
	 * @return the maximal depth reached during local variables collection
	 */
	public int getMaximalReachedDepthDuringCollectionOfLocalVariables() { return this.collectedLocalVariablesInHierarchy.size(); }

	/**
	 * Returns the maximal depth reached during collection of parameters
	 * 
	 * @return the maximal depth reached during collection of parameters
	 */
	public int getMaximalReachedDepthDuringCollectionOfParameters() { return this.collectedParametersInHierarchy.size(); }
	
	/**
	 * Collect variable objects including variables and parameters into one list
	 * 
	 * @return the list of collected variable objects including variables and parameters
	 */
	public VariableObjectCollector prepareCollectedVariableObjects() {
		VariableObjectCollector variableObjectCollector = new VariableObjectCollector();
		List<VariableObject> foundParameters = this.getVariableObjectsAccordingToConfiguration(
				this.collectedParametersInHierarchy, this.actualScriptVariablesToSubstituteConfiguration, true);
		variableObjectCollector.addParameters(foundParameters);
		
		List<VariableObject> foundLocalVariables = this.getVariableObjectsAccordingToConfiguration(
				this.collectedParametersInHierarchy, this.actualScriptVariablesToSubstituteConfiguration, false);
		variableObjectCollector.addLocalVariables(foundLocalVariables);
		
		return variableObjectCollector;
	}
	
	/**
	 * Returns variable objects according to configuration settings (if they fit requirements on depth) 
	 * 
	 * @param foundParametersInHierarchy - the aggregation of parameters according to depth in their positioning in hierarchy
	 * @param actualScriptVariablesToSubstituteConfiguration - configuration for getting actually available functionality that can be substituted in code
	 * @return variable objects according to configuration settings (if they fit requirements on depth)
	 */
	private List<VariableObject> getVariableObjectsAccordingToConfiguration(
			Map<Integer, List<VariableObject>> foundParametersInHierarchy,
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration, 
			boolean parametersProcessed) {
		LanguageSpecificVariableSubstitutionConfiguration languageSpecificVariableSubstitutionConfiguration = 
				actualScriptVariablesToSubstituteConfiguration.getLanguageSpecificVariableSubstitutionConfiguration();
		
		List<VariableObject> foundParameters = new ArrayList<VariableObject>();
		List<VariableObject> parametersInHierarchy;
		Iterator<List<VariableObject>> listIterator;
		int depth;
		int parametersDepth, localVariablesDepth;
		if ((parametersProcessed && languageSpecificVariableSubstitutionConfiguration.isHarvestingParameterDepthLevelAllowed()) 
				|| (!parametersProcessed && languageSpecificVariableSubstitutionConfiguration.isHarvestingLocalVariablesDepthLevelAllowed())) {

			// Iterate in reverse.
			for (Entry<Integer, List<VariableObject>> parametersInDepth: foundParametersInHierarchy.entrySet()) {
				depth = parametersInDepth.getKey();
				parametersDepth = this.currentSearchedObjectDepth + languageSpecificVariableSubstitutionConfiguration.getMaxHarvestingParametersDepthLevelAcrossHierarchies();
				localVariablesDepth =  this.currentSearchedObjectDepth + languageSpecificVariableSubstitutionConfiguration.getMaxHarvestingLocalVariablesDepthLevelAcrossHierarchies();
				if (DebugInformation.SHOW_POLLUTING_INFORMATION) {
					logger.debug("Current hierarchy depth parameters: " + this.currentSearchedObjectDepth + " variables: " + this.currentSearchedObjectDepth);
					logger.debug("Allowed parameters depth: " + this.currentSearchedObjectDepth + " to " + parametersDepth + " Depth: " + depth);
					logger.debug("Allowed local variables depth: " + this.currentSearchedObjectDepth + " to " + localVariablesDepth + " Depth: " + depth);
				}
				parametersInHierarchy = parametersInDepth.getValue();
				/*
				for (VariableObject vo: parametersInHierarchy) {
					  System.out.println("Test Depth: " + depth + " Taken variable/parameter name: " + vo.getExportName() + " as " + vo.getExportType());
				}*/
				
				if ((parametersProcessed && depth >= this.currentSearchedObjectDepth && depth < parametersDepth)
						 || (!parametersProcessed && depth >= this.currentSearchedObjectDepth && depth < localVariablesDepth)
						) {
					if (DebugInformation.SHOW_POLLUTING_INFORMATION) {
					  for (VariableObject vo: parametersInHierarchy) {
						  logger.debug("Depth: " + depth + " Taken variable/parameter name: " + vo.getExportName() + " as " + vo.getExportType());
					  }
					}
					foundParameters.addAll(parametersInHierarchy);
				}
				depth--;
			}
			//if (foundParameters.size() > 0) { System.exit(5); }
			return foundParameters;
		} 
		
		for(List<VariableObject> parametersInHierarchyEntity: foundParametersInHierarchy.values()) {
			foundParameters.addAll(parametersInHierarchyEntity);
		}
		return foundParameters;
	}
}
