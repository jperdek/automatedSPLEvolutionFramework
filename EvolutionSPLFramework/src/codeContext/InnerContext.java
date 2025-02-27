package codeContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codeContext.objects.VariableObject;
import codeContext.processors.ASTContextProcessor;
import codeContext.processors.export.ExportAggregator;
import codeContext.processors.export.ExportedContextInterface;
import codeContext.processors.export.ExportedInterface;
import codeContext.processors.export.ExportedObjectInterface;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.ActualScriptVariablesToSubstituteConfiguration;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.LanguageSpecificVariableSubstitutionConfiguration;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.VariableObjectCollector;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.VariableObjectInHierarchyCollector;
import splEvolutionCore.DebugInformation;


/**
 * Representation of inner context that si organized into hierarchy
 * 
 * @author Jakub Perdek
 *
 */
public class InnerContext implements ExportedContextInterface, ExportedObjectInterface, ExportedInterface {
	
	/**
	 * Logger to track information about inner context
	 */
	private static final Logger logger = LoggerFactory.getLogger(InnerContext.class);
	
	/**
	 * Used parameters of inner context
	 */
	//protected UsedVariables usedParameters;
	
	/**
	 * Used variables that are declared in inner context
	 */
	protected UsedVariables usedVariables;
	
	/**
	 * Sorted contexts according to their position/occurence in the processed application script/application AST
	 */
	protected SortedMap<Long, InnerContext> orderedContexts;
	
	/**
	 * The end position of inner context in application AST
	 */
	protected long originalEndPosition = -1;
	
	/**
	 * The start position of inner context in application AST
	 */
	protected long originalStartPosition = -1;
	
	/**
	 * The base - the root of inner context
	 */
	protected InnerContext baseFirstInnerContext;
	
	/**
	 * information if given inner context is exported, true if given context is exported otherwise not
	 */
	protected boolean isExported = false;
	
	/**
	 * information if types should be used, true if types are used otherwise not
	 */
	protected boolean useTypes = true;
	
	/**
	 * The direction of the search
	 * -from the left - start position
	 * -from the end - end position
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum Direction {LEFT_FROM_POSITION, RIGHT_FROM_POSITION}

	
	/**
	 * Creates the inner context
	 * 
	 * @param baseFirstInnerContext - the actually processed/parent inner context
	 * @param originalStartPosition - the start position of the inner context in the application AST
	 * @param originalEndPosition - the end position of the inner context in the application AST
	 * @param useTypes - information if the types are used - true if types are used otherwise not
	 * @param isExported - information if this inner context is exported, true if this inner context is exported otherwise false
	 */
	public InnerContext(InnerContext baseFirstInnerContext, long originalStartPosition, 
			long originalEndPosition, boolean useTypes, boolean isExported) {
		this.originalStartPosition = originalStartPosition;
		this.originalEndPosition = originalEndPosition;
		this.orderedContexts = new TreeMap<Long, InnerContext>();
		this.useTypes = useTypes;
		this.isExported = isExported;

		//this.usedParameters = new UsedVariables();
		this.usedVariables = new UsedVariables();
		if (baseFirstInnerContext == null) {
			this.baseFirstInnerContext = this;
		} else {
			this.baseFirstInnerContext = baseFirstInnerContext;
		}
	}
	
	/**
	 * Creates the inner context
	 * 
	 * @param baseFirstInnerContext - the actually processed/parent inner context
	 * @param originalStartPosition - the start position of the inner context in the application AST
	 * @param originalEndPosition - the end position of the inner context in the application AST
	 * @param upperInnerContextVariables - the variables declared in upper contexts (are propagated to this context)
	 * @param useTypes - information if the types are used - true if types are used otherwise not
	 * @param isExported - information if this inner context is exported, true if this inner context is exported otherwise false
	 */
	public InnerContext(InnerContext baseFirstInnerContext, long originalStartPosition, long originalEndPosition,
			UsedVariables upperInnerContextVariables, 
			boolean useTypes, boolean isExported) {
		this.useTypes = useTypes;
		this.originalStartPosition = originalStartPosition;
		this.originalEndPosition = originalEndPosition;
		this.orderedContexts = new TreeMap<Long, InnerContext>();
		this.baseFirstInnerContext = baseFirstInnerContext;
		this.isExported = isExported;

		this.usedVariables = new UsedVariables(upperInnerContextVariables.getUsedVariableObjects());
	}
	
	/**
	 * Returns the end position of inner context in application AST
	 * 
	 * @return the end position of inner context in application AST
	 */
	public long getActualEndPosition() { return this.originalEndPosition; }
	
	/**
	 * Returns the start position of inner context in application AST
	 * 
	 * @return the start position of inner context in application AST
	 */
	public long getActualStartPosition() { return this.originalStartPosition; }
	
	/**
	 * Returns all children/aggregated context in form of new array list
	 *  
	 * @return all children/aggregated context in form of new array list
	 */
	public List<InnerContext> getAllInnerChildContext() { return new ArrayList<InnerContext>(this.orderedContexts.values()); }
	
	/**
	 * Returns the base inner context
	 * 
	 * @return the base inner context
	 */
	public InnerContext getBaseContext() { return this.baseFirstInnerContext; }
	
	/**
	 * Returns information if this inner context is exported, true if this inner context is exported otherwise false
	 * 
	 * @return information if this inner context is exported, true if this inner context is exported otherwise false
	 */
	public boolean isExported() { return this.isExported; }

	/**
	 * Sets this inner context as exported
	 */
	public void setAsExported() { this.isExported=true; }
	
	/**
	 * Finds function context according function name that is provided in form of the parameter otherwise returns null
	 * 
	 * @param contextName - the name of function and searched function context
	 * @return the found function context otherwise returns null
	 */
	public FunctionContext findFunctionContext(String contextName) {
		FunctionContext functionContextDeep;
		for (FunctionContext functionContext: this.getAllFunctions()) {
			if (functionContext.getFunctionName().contains(contextName)) { return functionContext; }
			functionContextDeep = functionContext.findFunctionContext(contextName);
			if (functionContextDeep != null) { return functionContextDeep; }
		}
		return null;
	}
	
	/**
	 * Returns all available function contexts from ordered contexts
	 * 
	 * @return all available functions from ordered contexts
	 */
	public List<FunctionContext> getAllFunctions() {
		return (List<FunctionContext>)(List<?>) this.orderedContexts.values().stream().filter(innerContext -> innerContext instanceof FunctionContext).toList();
	}
	
	/**
	 * Returns all available class contexts from ordered contexts
	 * 
	 * @return all available class contexts from ordered contexts
	 */
	public List<ClassContext> getAllClasses() {
		return (List<ClassContext>)(List<?>) this.orderedContexts.values().stream().filter(innerContext -> innerContext instanceof ClassContext).toList();
	}
	
	/**
	 * Returns the all actual up to given context
	 * -from the left if direction is set to Direction.LEFT_FROM_POSITION
	 * -from the right if direction is not set to Direction.LEFT_FROM_POSITION
	 * 
	 * @param position - the actual position to represent actual contexts
	 * @param direction - the direction from which contexts should be harvested
	 * @return the actual contexts up to given position
	 */
	public SortedMap<Long, InnerContext> getActualContext(long position, Direction direction) {
		//return this.orderedContexts;
		if (direction == Direction.LEFT_FROM_POSITION) {
			return this.orderedContexts.headMap(position);
		}
		return this.orderedContexts.tailMap(position);
	}
	
	/**
	 * Returns the the nearest actual context or null
	 * -from the left if direction is set to Direction.LEFT_FROM_POSITION - it selects the last such element
	 * -from the right if direction is not set to Direction.LEFT_FROM_POSITION - it selects the first such element
	 * 
	 * @param position - the actual position to represent actual contexts
	 * @param direction - the direction from which contexts should be harvested
	 * @return the actual nearest context to given position
	 */
	public InnerContext getNearestActualContext(long position, Direction direction) {
		SortedMap<Long, InnerContext> subMap;
		if (direction == Direction.LEFT_FROM_POSITION) {
			subMap = this.orderedContexts.headMap(position);
		} else {
			subMap = this.orderedContexts.tailMap(position);
		}
		if (subMap.isEmpty()) { return null; }
		if (direction == Direction.LEFT_FROM_POSITION) {
			return subMap.get(subMap.lastKey());
		}
		return subMap.get(subMap.firstKey());
	}
	
	/**
	 * Adds inner context to the inner contexts - ordered contexts according to its position 
	 * 
	 * @param actualEndPosition - the position of context
	 * @param newActualContext - the new inserted inner context
	 */
	public void addContext(long actualEndPosition, InnerContext newActualContext) {
		this.orderedContexts.put(actualEndPosition, newActualContext);
	}

	/**
	 * Inserts/adds the variable to used variables in this context
	 * 
	 * @param partOfVariable - the part AST that contains the variable
	 * @param astRoot - the root of application AST
	 * @param variableName - the name of the variable
	 * @param isDeclaration - information if variable is declared, true if variable is declared otherwise false
	 * @param isDirectlyExported - information if the variable is directly exported, true if the variable is directly exported otherwise false
	 */
	public void addVariable(JSONObject partOfVariable, JSONObject astRoot, String variableName, 
			boolean isDeclaration, boolean isDirectlyExported) {
		this.usedVariables.addVariable(variableName, (long) partOfVariable.get(ASTContextProcessor.SearchPositions.END.label), 
				astRoot, partOfVariable, isDeclaration, false, isDirectlyExported);
	}
	
	/**
	 * Returns the used variables representation
	 * 
	 * @return the used variables representation
	 */
	public UsedVariables getUsedVariables() { return this.usedVariables; }
	
	
	/**
	 * Collects variable objects such as local variables and parameters from the hierarchy of contexts according to provided configuration
	 * 
	 * @param searchedChildContext - the child context which is processed and variable objects on the path to it have to be found in hierarchy
	 * @param currentPosition - position of search object/context, behind this position variable objects are unknown
	 * @param startSearchPosition - the start position in AST of searched object if this object is not provided (null)
	 * @param endSearchPosition - the end position in AST of searched object if this object is not provided (null)
	 * @param direction - the direction how search is applied to get all sorted elements from the sorted tree
	 * @param actualScriptVariablesToSubstituteConfiguration
	 * @return object with collected variables and parameters in flattened form and applied restrictions holding for depth in AST
	 */
	public VariableObjectCollector collectVariableObjects(InnerContext searchedChildContext,
			long currentPosition, long startSearchPosition, long endSearchPosition, Direction direction, 
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration) {
		// CURRENT OBJECT'S DATA
		//List<VariableObject> extractedParameters = this.usedParameters.getAllActualVariableObject(currentPosition, actualScriptVariablesToSubstituteConfiguration); 
		//List<VariableObject> extractedLocalVariables = this.usedVariables.getAllActualVariableObject(currentPosition, actualScriptVariablesToSubstituteConfiguration); 
		
		
		// CURRENT OBJECT'S DATA + PARENTS - DATA OF PREVIOUSLY DECLARED OBJECTS
		VariableObjectInHierarchyCollector variableObjectInHierarchyCollector = new VariableObjectInHierarchyCollector(actualScriptVariablesToSubstituteConfiguration);
		InnerContext rootContext = this.getBaseContext(); //to capture real context data
		if (rootContext == null) { logger.error("Error: root context is unavailable!"); }
		rootContext.collectVariableObjectsInAllParents(variableObjectInHierarchyCollector, null, currentPosition, 
				startSearchPosition, endSearchPosition, direction, actualScriptVariablesToSubstituteConfiguration);
		// after current position in hierarchy is found - inserts data
		//variableObjectInHierarchyCollector.setCurrentObjectDepthForCollectedObjectsInHierarhy(-1); //to correctly filter objects according to the depth
		//variableObjectInHierarchyCollector.collectParametersOnCurrentDepth(extractedParameters);
		//variableObjectInHierarchyCollector.collectLocalVariablesOnCurrentDepth(extractedLocalVariables);
		//logger.debug("Local depth for parameters: " + variableObjectInHierarchyCollector.getMaximalReachedDepthDuringParameterCollection());
		//logger.debug("Local depth for local variables: " + variableObjectInHierarchyCollector.getMaximalReachedDepthDuringCollectionOfLocalVariables());
		
		
		// CHILDREN - DECLARED OBJECTS ON THE LEVEL OF CURRENT OBJECT - inner entity
		if (searchedChildContext == null) {
			logger.debug("Searched context is null. Getting observed context according to position...");
			searchedChildContext = variableObjectInHierarchyCollector.getSearchedContextIfExists(); 
		} 
		if (searchedChildContext != null) {
			searchedChildContext.collectVariableObjectsInAllChildren(variableObjectInHierarchyCollector, 
					searchedChildContext, currentPosition, direction, actualScriptVariablesToSubstituteConfiguration);
		}
		
		return variableObjectInHierarchyCollector.prepareCollectedVariableObjects();
	}
	
	/**
	 * Collects variable objects such as local variables and parameters from the parents of searched object in the hierarchy of contexts according to provided configuration
	 * 
	 * @param variableObjectInHierarchyCollector - the collector of variable objects from the hierarchy with mapped depth
	 * @param searchedChildContext - the child context which is processed and variable objects on the path to it have to be found in hierarchy
	 * @param currentPosition - position of search object/context, behind this position variable objects are unknown
	 * @param startSearchPosition - the start position in AST of searched object if this object is not provided (null)
	 * @param endSearchPosition - the end position in AST of searched object if this object is not provided (null)
	 * @param direction - the direction how search is applied to get all sorted elements from the sorted tree
	 * @param actualScriptVariablesToSubstituteConfiguration
	 */
	public void collectVariableObjectsInAllParents(VariableObjectInHierarchyCollector variableObjectInHierarchyCollector, InnerContext searchedChildContext,
			long currentPosition, long startSearchPosition, long endSearchPosition, Direction direction, 
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration) {
		this.searchVariableObjectsInAllParents(searchedChildContext, currentPosition, startSearchPosition, endSearchPosition,
				direction, variableObjectInHierarchyCollector, actualScriptVariablesToSubstituteConfiguration, 0);
	}
	
	/**
	 * Collects variable objects such as local variables and parameters from the children of searched object in the hierarchy of contexts according to provided configuration
	 * 
	 * @param variableObjectInHierarchyCollector - the collector of variable objects from the hierarchy with mapped depth
	 * @param searchedChildContext - the child context which is processed and variable objects on the path to it have to be found in hierarchy
	 * @param currentPosition - position of search object/context, behind this position variable objects are unknown
	 * @param direction - the direction how search is applied to get all sorted elements from the sorted tree
	 * @param actualScriptVariablesToSubstituteConfiguration
	 * @param depth - currently processed depth in hierarchy of contexts
	 */
	public void collectVariableObjectsInAllChildren(VariableObjectInHierarchyCollector variableObjectInHierarchyCollector, 
			InnerContext searchedChildContext, long currentPosition, Direction direction, 
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration, int depth) {
		List<VariableObject> childParameters, childLocalVariables;
		SortedMap<Long, InnerContext> currentlyAvailableContexts = this.getActualContext(currentPosition, direction);
		
		int parametersDepth = variableObjectInHierarchyCollector.getMaximalReachedDepthDuringCollectionOfParameters() + 2;
		int localVariablesDepth = variableObjectInHierarchyCollector.getMaximalReachedDepthDuringCollectionOfLocalVariables() + 2;
		for (InnerContext currentlyAvailableContext: currentlyAvailableContexts.values()) {
			if (currentPosition <= currentlyAvailableContext.getActualEndPosition()) {
				if (currentlyAvailableContext instanceof FunctionContext) {
					childParameters = ((FunctionContext) currentlyAvailableContext).getFunctionParameters(currentPosition, actualScriptVariablesToSubstituteConfiguration);
					variableObjectInHierarchyCollector.collectParameters(childParameters, parametersDepth);
				} else if (currentlyAvailableContext instanceof ClassContext) { 
					childParameters = ((ClassContext) currentlyAvailableContext).getConstructorParameters(currentPosition, actualScriptVariablesToSubstituteConfiguration);
					variableObjectInHierarchyCollector.collectParameters(childParameters, parametersDepth);
				}
				
				
				childLocalVariables = currentlyAvailableContext.getVariables(currentPosition);
				variableObjectInHierarchyCollector.collectLocalVariables(childLocalVariables, localVariablesDepth);
				
				currentlyAvailableContext.collectVariableObjectsInAllChildren(
						variableObjectInHierarchyCollector, searchedChildContext, currentPosition,
						direction, actualScriptVariablesToSubstituteConfiguration);
			}
		}
	}
	
	/**
	 * Collects variable objects such as local variables and parameters from the previously declared child objects/contexts  
	 * 
	 * @param variableObjectInHierarchyCollector - the collector of variable objects from the hierarchy with mapped depth
	 * @param searchedChildContext - the child context which is processed and variable objects on the path to it have to be found in hierarchy
	 * @param currentPosition - position of search object/context, behind this position variable objects are unknown
	 * @param direction - the direction how search is applied to get all sorted elements from the sorted tree
	 * @param actualScriptVariablesToSubstituteConfiguration
	 */
	public void collectVariableObjectsInAllChildren(VariableObjectInHierarchyCollector variableObjectInHierarchyCollector, 
			InnerContext searchedChildContext, long currentPosition, Direction direction, 
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration) {
		this.collectVariableObjectsInAllChildren(variableObjectInHierarchyCollector, searchedChildContext, currentPosition, direction, 
				actualScriptVariablesToSubstituteConfiguration, 0);
	}
	
	/**
	 * Collects context information about declared local variables and parameters
	 * - optionally prints their name and type in particular depth
	 * 
	 * @param observedChildContext
	 * @param currentPosition - position of search object/context, behind this position variable objects are unknown
	 * @param depth - currently processed depth in hierarchy of contexts
	 * @param variableObjectInHierarchyCollector - the collector of variable objects from the hierarchy with mapped depth
	 * @param actualScriptVariablesToSubstituteConfiguration - the configuration for extraction of parameters and variables for further substitution
	 * @param languageSpecificVariablesToSubstituteConfiguration - the programming language specific configuration for extracting local variables and parameters
	 * @param onPath - if information is extracted from contexts on the path from the root to the searched context in the hierarchy
	 */
	private static void collectContextInformation(InnerContext observedChildContext, long currentPosition,  int depth, VariableObjectInHierarchyCollector variableObjectInHierarchyCollector, 
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration, LanguageSpecificVariableSubstitutionConfiguration languageSpecificVariablesToSubstituteConfiguration, boolean onPath) {
		List<VariableObject> extractedParameters, extractedLocalVariables;
		if (onPath) {
			if (languageSpecificVariablesToSubstituteConfiguration.isHarvestingParameterDepthLevelAllowed()) {
				if (observedChildContext instanceof FunctionContext) {
					extractedParameters = ((FunctionContext) observedChildContext).getFunctionParameters(currentPosition, actualScriptVariablesToSubstituteConfiguration);
				} else if (observedChildContext instanceof ClassContext) {
					extractedParameters = ((ClassContext) observedChildContext).getConstructorParameters(currentPosition, actualScriptVariablesToSubstituteConfiguration);
				} else {
					extractedParameters = null;
				}
				if (DebugInformation.SHOW_POLLUTING_INFORMATION) {
					if (extractedParameters != null && extractedParameters.size() > 0) {
						logger.debug("Harvested parameters in depth " + depth + " :");
						for (VariableObject vo: extractedParameters) {
							logger.debug(vo.getExportName() + " ___ " + vo.getExportType());
						}
					}
				}
				variableObjectInHierarchyCollector.collectParameters(extractedParameters, depth);
			}
		}
		if (languageSpecificVariablesToSubstituteConfiguration.isHarvestingLocalVariablesDepthLevelAllowed() &&
				!actualScriptVariablesToSubstituteConfiguration.useCurrentLevelVariablesOnly()) {
			//local variables higher/in parent are usually not accessible
			extractedLocalVariables = observedChildContext.getVariables(currentPosition);
			if (DebugInformation.SHOW_POLLUTING_INFORMATION) {
				if (extractedLocalVariables.size() > 0) {
					logger.debug("Harvested variables in depth " + depth + " :");
					for (VariableObject vo: extractedLocalVariables) {
						logger.debug(vo.getExportName() + " ___ " + vo.getExportType());
					}
				}
			}
			variableObjectInHierarchyCollector.collectLocalVariables(extractedLocalVariables, depth);
		}
	}
	
	/**
	 * Collects variable objects such as local variables and parameters from the parents of searched object in the hierarchy of contexts according to provided configuration
	 *  
	 * @param searchedChildContext - the child context which is processed and variable objects on the path to it have to be found in hierarchy
	 * @param currentPosition - position of search object/context, behind this position variable objects are unknown
	 * @param startSearchPosition - the start position in AST of searched object if this object is not provided (null)
	 * @param endSearchPosition - the end position in AST of searched object if this object is not provided (null)
	 * @param direction - the direction how search is applied to get all sorted elements from the sorted tree
	 * @param variableObjectInHierarchyCollector
	 * @param actualScriptVariablesToSubstituteConfiguration
	 * @param depth - currently processed depth in hierarchy of contexts
	 */
	public void searchVariableObjectsInAllParents(InnerContext searchedChildContext,
			long currentPosition, long startSearchPosition, long endSearchPosition, Direction direction, VariableObjectInHierarchyCollector variableObjectInHierarchyCollector, 
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration, int depth) {
		Long startSearchedObjectPosition, endSearchObjectPosition;
		LanguageSpecificVariableSubstitutionConfiguration languageSpecificVariablesToSubstituteConfiguration = 
				actualScriptVariablesToSubstituteConfiguration.getLanguageSpecificVariableSubstitutionConfiguration();
		List<VariableObject> classVariables;
		if (searchedChildContext != null) {
			startSearchedObjectPosition = searchedChildContext.getActualStartPosition();
			endSearchObjectPosition = searchedChildContext.getActualEndPosition();
		} else {
			startSearchedObjectPosition = startSearchPosition;
			endSearchObjectPosition = endSearchPosition;
		}
		Long startSearchedChildPosition, endSearchedChildPosition;
		ClassContext identifiedClassContext;
		// no child and no parents exist
		if (searchedChildContext != null && this == searchedChildContext) { return; } 
		if (this.originalStartPosition == startSearchPosition && this.originalEndPosition == endSearchPosition) {
			InnerContext.collectContextInformation(this, currentPosition, depth, 
					variableObjectInHierarchyCollector, actualScriptVariablesToSubstituteConfiguration, 
					languageSpecificVariablesToSubstituteConfiguration, true);
			
			if (DebugInformation.SHOW_POLLUTING_INFORMATION) { logger.debug("Searched objects depth: " + depth); }
			variableObjectInHierarchyCollector.setCurrentObjectDepthForCollectedObjectsInHierarhy(depth);
			return;
		}
		
		SortedMap<Long, InnerContext> currentlyAvailableContexts = this.orderedContexts;// this.getActualContext(currentPosition, direction);
		if (DebugInformation.SHOW_POLLUTING_INFORMATION) {
			logger.debug("Number candidates in hiearachy tree to process: " + currentlyAvailableContexts.values().size());
		}
		for (InnerContext observedChildContext: currentlyAvailableContexts.values()) {
			// child is (equals to) searched object, [this - parent, searchedChildContext - child]
			if (DebugInformation.SHOW_POLLUTING_INFORMATION) {
				logger.debug("CURRENT: " + currentPosition + " REQUESTED OBJECT: [" + startSearchedObjectPosition + " , " + endSearchObjectPosition + "]  CHILD: " + observedChildContext.getActualStartPosition() + " ,  " + observedChildContext.getActualEndPosition() );
			}
			if ((searchedChildContext != null && observedChildContext == searchedChildContext) || 
					(searchedChildContext == null && 
					observedChildContext.getActualStartPosition() == startSearchedObjectPosition 
					&& observedChildContext.getActualEndPosition() == endSearchObjectPosition)) {
				variableObjectInHierarchyCollector.addContextToPath(observedChildContext);
				
				if (this instanceof ClassContext) {
					identifiedClassContext = (ClassContext) this;
					classVariables = identifiedClassContext.getClassVariables(currentPosition, actualScriptVariablesToSubstituteConfiguration);
					variableObjectInHierarchyCollector.collectLocalVariables(classVariables, depth + 1);
				}
				
				InnerContext.collectContextInformation(observedChildContext, currentPosition, depth + 1, 
						variableObjectInHierarchyCollector, actualScriptVariablesToSubstituteConfiguration, 
						languageSpecificVariablesToSubstituteConfiguration, true);
				
				if (DebugInformation.SHOW_POLLUTING_INFORMATION) { logger.debug("Searched objects depth: " + (depth + 1)); }
				variableObjectInHierarchyCollector.setCurrentObjectDepthForCollectedObjectsInHierarhy(depth + 1);
				return;

			// child can be in hierarchy on path to his declaration
			} else {
				startSearchedChildPosition = observedChildContext.getActualStartPosition();
				endSearchedChildPosition = observedChildContext.getActualEndPosition();
				
				
				if (startSearchedChildPosition <= startSearchedObjectPosition 
						&& endSearchedChildPosition >= endSearchObjectPosition) {
					if (DebugInformation.SHOW_POLLUTING_INFORMATION) { logger.debug("Found instance on path."); }
					variableObjectInHierarchyCollector.addContextToPath(observedChildContext);
					InnerContext.collectContextInformation(observedChildContext, currentPosition, depth + 1, 
							variableObjectInHierarchyCollector, actualScriptVariablesToSubstituteConfiguration, 
							languageSpecificVariablesToSubstituteConfiguration, true);
					variableObjectInHierarchyCollector.setCurrentObjectDepthForCollectedObjectsInHierarhy(depth + 1);
					observedChildContext.searchVariableObjectsInAllParents(searchedChildContext, currentPosition, startSearchPosition, endSearchPosition,
							direction, variableObjectInHierarchyCollector, actualScriptVariablesToSubstituteConfiguration, depth + 1);
					return; //in sorted way contexts not interfere with their positions into each other
				} else if (startSearchedChildPosition > currentPosition) {
					// BEHIND SEARCHED POSITION
					// still relations valid in current entity (this) can apply
					//break; //can break, is sorted
					continue; // if children weren't sorted
					
				} else { // object is in sorted child objects somewhere before:
						 //               endSearchObjectPosition < startSearchedObjectPosition
					// CONTEXT IS NOT ON PATH, BUT SOME OF GLOBAL SETTINGS CAN APPLY
					
					// NO PARAMETERS CAN AFFECT CURRENT INSTANCE - parameters of other objects
					// can change for other programming languages
					if (!languageSpecificVariablesToSubstituteConfiguration.shouldOmitParsingSideInnerContexts()) {
						InnerContext.collectContextInformation(observedChildContext, currentPosition, depth + 1, 
								variableObjectInHierarchyCollector, actualScriptVariablesToSubstituteConfiguration, 
								languageSpecificVariablesToSubstituteConfiguration, false);
						observedChildContext.searchVariableObjectsInAllParents(searchedChildContext, currentPosition, startSearchPosition, endSearchPosition,
								direction, variableObjectInHierarchyCollector, actualScriptVariablesToSubstituteConfiguration, depth + 1);
					}
					continue;
				}
			}
		}
	}
	
	/**
	 * Returns declared variables for this context according to currentPosition that is provided as function parameter
	 * 
	 * @param currentPosition - the position in application AST (script) which is used to decide if given variables are available/are already declared
	 * @return all actually declared (before or at currentPosition that is provided as function parameter) variables
	 */
	public List<VariableObject> getVariables(long currentPosition) { 
		return this.usedVariables.getAllActualVariableObject(currentPosition); 
	}
	
	/**
	 * Returns all actually declared variables according to currentPosition that is provided as function parameter
	 * 
	 * @param currentPosition -  the position in application AST (script) which is used to decide if given variables are available/are already declared
	 * @param direction - 
	 * @param actualScriptVariablesToSubstituteConfiguration
	 * @param depth
	 * @return all actually declared (before or at currentPosition that is provided as function parameter) variables
	 */
	public List<VariableObject> getActualVariables(long currentPosition, Direction direction, 
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration, int depth) { 
		List<VariableObject> currentlyAvailableVariables = new ArrayList<VariableObject>();
		List<VariableObject> extractedVariables = this.usedVariables.getAllActualVariableObject(currentPosition, actualScriptVariablesToSubstituteConfiguration); 
		
		currentlyAvailableVariables.addAll(extractedVariables);
		
		SortedMap<Long, InnerContext> currentlyAvailableContexts = this.getActualContext(currentPosition, direction);
		for (InnerContext currentlyAvailableContext: currentlyAvailableContexts.values()) {
			extractedVariables = currentlyAvailableContext.getActualVariables(
					currentPosition, direction, actualScriptVariablesToSubstituteConfiguration, depth + 1);
			currentlyAvailableVariables.addAll(extractedVariables);
		}
		return currentlyAvailableVariables;
	}
	
	/**
	 * Returns the empty context option representation
	 * 
	 * @param baseExecutable - the base executable expression - callable form, if none then callable form will be created
	 * @param actualContext - the actual processed context (inner)
	 * @return the empty context option representation
	 */
	public ContextOptions insertAllInstantiations(String baseExecutable, InnerContext actualContext) {
		return new ContextOptions();
	}
	
	/**
	 * Returns and creates the descriptive JSON represented output from information about the inner context
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @return created descriptive JSON represented output from information about the inner context
	 */
	public JSONObject createDescriptiveJSON(GlobalContext globalContext) {
		JSONObject descriptiveJSON = new JSONObject();
		JSONArray availableVariablesJSON = this.usedVariables.createDescriptiveJSON();
		descriptiveJSON.put("p", this.originalEndPosition);
		if (!availableVariablesJSON.isEmpty()) {
			descriptiveJSON.put("availableVariables", availableVariablesJSON);
			//descriptiveJSON.put("p", this.originalStartPosition);
		}
		return descriptiveJSON;
	}
	
	/**
	 * Returns usable variables with their type in actual context
	 * 
	 * @param availableVariablesFromActualContext - usable variables to be substituted from actual context
	 * @param actualScriptVariablesToSubstituteConfiguration - configuration for getting actually available functionality that can be substituted in code
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 */
	public void getUsableVariablesInActualContext(Set<Entry<String, String>> availableVariablesFromActualContext,
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration, GlobalContext globalContext) {
		this.usedVariables.getUsableVariablesInActualContext(
				availableVariablesFromActualContext, actualScriptVariablesToSubstituteConfiguration, globalContext);
		if (actualScriptVariablesToSubstituteConfiguration.useParameters()) {
			this.usedVariables.getUsableVariablesInActualContext(
					availableVariablesFromActualContext, actualScriptVariablesToSubstituteConfiguration, globalContext);
		}
	}
	
	/**
	 * Prints the ordered structure
	 * 
	 * @param globalContext
	 */
	public void printOrderedStructure(GlobalContext globalContext) {
		Long position;
		InnerContext innerContext;
		if (this.orderedContexts.entrySet().isEmpty()) { return; }

		if (DebugInformation.SHOW_CREATED_ENTITIES) {
			List<InnerContext> parentContexts = new ArrayList<InnerContext>();
			logger.debug("<---------------------- START ---------------------------->");
			for (Entry<Long, InnerContext> orderedContextEntry: this.orderedContexts.entrySet()) {
				position = orderedContextEntry.getKey();
				innerContext = orderedContextEntry.getValue();
				logger.debug(position + " --> " + innerContext.createDescriptiveJSON(globalContext).toString());
				parentContexts.add(innerContext);
			}
			logger.debug("<---------------------- END ---------------------------->");
			
			for(InnerContext parentContext: parentContexts) {
				parentContext.printOrderedStructure(globalContext);
			}
		}
	}

	@Override
	public ExportedObjectInterface findDefaultExport(Long startPosition, Long endPosition) {
		ExportedObjectInterface defaultExport = null;
		if (this.originalStartPosition == startPosition && this.originalEndPosition == endPosition) {
			return this;
		}
		for (InnerContext childContexts: this.orderedContexts.values()) {
			defaultExport = childContexts.findDefaultExport(startPosition, endPosition);
			if (defaultExport != null) { return defaultExport; }
		}
		defaultExport = this.usedVariables.findDefaultExport(startPosition, endPosition);
		return defaultExport;
	}

	@Override
	public void findContextToExportMapping(List<String> exportNames, ExportAggregator exportAggregator) {
		for (InnerContext childContexts: this.orderedContexts.values()) {
			childContexts.findContextToExportMapping(exportNames, exportAggregator);
		}
		this.usedVariables.findContextToExportMapping(exportNames, exportAggregator);
	}

	@Override
	public void markDirrectExportMapping(ExportAggregator exportAggregator, String fileName, InnerContext baseInnerContext) {
		for (InnerContext childContexts: this.orderedContexts.values()) {
			childContexts.markDirrectExportMapping(exportAggregator, fileName, baseInnerContext);
		}
		this.usedVariables.markDirrectExportMapping(exportAggregator, fileName, baseInnerContext);
	}

	@Override
	public String getExportName() { return null; }
	
	@Override
	public String getExportType() { return null; }

	@Override
	public String constructCallableForm() { return null; }
	
	@Override
	public JSONArray constructAllAvailableCallsUnderType() {
		return this.constructAllAvailableCallsUnderType("");
	}
	
	@Override
	public JSONArray constructAllAvailableCallsUnderType(String initialVariableName) {
		ContextOptions overallContextOptions = this.insertAllInstantiations(initialVariableName, this);
		return overallContextOptions.exportAsJSONArray();
	}

	@Override
	public ExportedObjectInterface getExtendableInnerObjectAccordingToType(String innerObjectType) {
		ExportedObjectInterface objectWithAssociatedType;
		for (InnerContext childContexts: this.orderedContexts.values()) {
			objectWithAssociatedType = childContexts.getExtendableInnerObjectAccordingToType(innerObjectType);
			if (objectWithAssociatedType != null) { return objectWithAssociatedType; }
		}
		objectWithAssociatedType = this.usedVariables.getExtendableInnerObjectAccordingToType(innerObjectType);
		return objectWithAssociatedType;
	}
	
	@Override
	public String getCallableStr() { return this.createDescriptiveJSON(null).toJSONString(); }

	@Override
	public String getIdentificationAST() { return this.createDescriptiveJSON(null).toJSONString(); }
	
	public void printContextSpecifics() {}
	
	protected void addIdentiation(int depth) {
		for (int i=0;  i <= depth; i++) {
			if (i % 2 == 0) {
				logger.debug("--->");
			} else {
				logger.debug("===>");
			}
		}
	}
	public void printTree(int depth) {
		String contextType = "UNKNOWN";
		String contextName = "Unknown";
		if (this instanceof ClassContext) {
			contextType = "CLASS";
			contextName = ((ClassContext) this).getClassName();
		} else if (this instanceof FunctionContext) {
			contextType = "FUNCTION";
			contextName = ((FunctionContext) this).getFunctionName();
		}
		this.addIdentiation(depth); logger.debug("\n");
		this.addIdentiation(depth); logger.debug("\n");
		this.addIdentiation(depth); logger.debug("\n");
		this.addIdentiation(depth); logger.debug("=-=-=-=-=-= {" + depth + "} =-=-=-=-=-=-= INNER ENTITY: " + contextName + " (" +contextType + ") [" + this.originalStartPosition + ", " + this.originalEndPosition + "]=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		List<String> extractedVariables = this.usedVariables.getUsedVariableObjectsStrings();
		this.addIdentiation(depth); logger.debug("-->===>---> USED VARIABLES: ");
		for (String extractedVariableString: extractedVariables) {
			this.addIdentiation(depth); logger.debug("-->===> " + extractedVariableString);
		}
		this.addIdentiation(depth); logger.debug("\n");
		
		logger.debug("-->===>---> CONTEXT SPECIFICS: ");
		this.addIdentiation(depth); this.printContextSpecifics();
		
		if ( this.orderedContexts.size() > 0) {
			this.addIdentiation(depth); logger.debug("-->===>---> CHILD CONTEXTS: ");
			for (InnerContext currentlyAvailableContext: this.orderedContexts.values()) {
				currentlyAvailableContext.printTree(depth + 1);
			}
			this.addIdentiation(depth); logger.debug("\n");
		}
		this.addIdentiation(depth); logger.debug("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}
}
