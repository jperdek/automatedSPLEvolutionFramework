package dividedAstExport.recursionCycleFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codeContext.processors.ASTTextExtractorTools;
import splEvolutionCore.DebugInformation;


/**
 * Tool to find cycles in code blocks
 * -classes and methods
 * 
 * @author Jakub Perdek
 *
 */
public class RecursionCycleFinder {
	
	/**
	 * Logger to track finding recursive functions by detecting cycles 
	 */
	private static final Logger logger = LoggerFactory.getLogger(RecursionCycleFinder.class);
	
	/**
	 * List of declared areas
	 */
	private List<CodeArea> areaDeclaration = new ArrayList<CodeArea>();
	
	/**
	 * Mapping of functionality names to all called functionalities
	 */
	private Map<String, Set<String>> connectionsInCalls = new HashMap<String, Set<String>>();
	
	/**
	 * Ignores overlap between class and function names
	 * -defaults to true (full distinction between functions and classes is not implemented)
	 */
	private boolean ignoreClassFunctionOverlap =  true; // TO-DO otherwise preserve class list + verify if position is inside class and start search
	 

	/**
	 * Creates instance of recursion cycle finder
	 * -still differentiate files is needed to find cycles from functionality across multiple files  
	 * 
	 * @param astRoot - the root of analyzed AST from the base code
	 */
	public RecursionCycleFinder(JSONObject astRoot) {
		this.initializeCycleFinder(astRoot, astRoot);
		this.areaDeclaration.sort((o1, o2) -> (int) Math.toIntExact(o2.getEndPosition() - o1.getEndPosition()));
		this.areaDeclaration.sort((o1, o2) -> (int) Math.toIntExact(o2.getStartPosition() - o1.getStartPosition()));
	}
	
	/**
	 * Checks if instance is instance inside recursion according to boundaries of searched functionality in original AST
	 * 
	 * @param startPosition -the start position of processed element in AST
	 * @param endPosition - the end position of processed element in AST
	 * @param astRoot - the root of AST to get specific code according to the positions if recursion is found only
	 * @return true if bounded functionality is inside recursion (cycle is found) otherwise false
	 */
	public boolean checkIfIsInsideRecursion(long startPosition, long endPosition, JSONObject astRoot) {
		int indexStart = 0, indexEnd = this.areaDeclaration.size() - 1;
		for (int highIndex=Math.toIntExact(this.areaDeclaration.size()) - 1; highIndex >= 0; highIndex--) {
    		if (this.areaDeclaration.get((int) highIndex).getEndPosition() > endPosition) {
    			indexEnd = highIndex;
    		} else if (this.areaDeclaration.get((int) highIndex).getEndPosition() == endPosition) {
    			indexEnd = highIndex;
    			break;
    		} else {
    			break;
    		}
    	}
		
		for (int lowIndex=Math.toIntExact(0); lowIndex <= this.areaDeclaration.size() - 1; lowIndex++) {
    		if (this.areaDeclaration.get((int) lowIndex).getStartPosition() < startPosition) {
    			indexStart = lowIndex;
    		} else if (this.areaDeclaration.get((int) lowIndex).getStartPosition() == startPosition) {
    			indexStart = lowIndex;
    			break;
    		} else {
    			break;
    		}
    	}
		String codeName;
		CodeArea processedCodeArea;
		long startCodeAreaPosition, endCodeAreaPosition;
		boolean isInsideRecursion;
		for (int index=indexStart; index < indexEnd; index++) {
			processedCodeArea = this.areaDeclaration.get(index);
			startCodeAreaPosition = processedCodeArea.getStartPosition();
			endCodeAreaPosition = processedCodeArea.getEndPosition();
			if (startCodeAreaPosition <= startPosition && startCodeAreaPosition <= endPosition && 
					endCodeAreaPosition >= startPosition && endPosition <= endCodeAreaPosition) {
				codeName = processedCodeArea.getCodeName();
				//logger.debug(codeName + " [ " + startCodeAreaPosition + " <= "+ startPosition + " <= "+ endCodeAreaPosition  + " ]["+ startCodeAreaPosition + " <= "+ endPosition + " <= "+ endCodeAreaPosition  + " ]");
				isInsideRecursion = this.isCycle(codeName);
				if (isInsideRecursion && DebugInformation.SHOW_POLLUTING_INFORMATION) {
					logger.debug("Found recursion AT: " + codeName + " recursion: " + isInsideRecursion);
					logger.debug(((String) astRoot.get("text")).substring(
							Math.toIntExact(startCodeAreaPosition), Math.toIntExact(endCodeAreaPosition)));
				}
				if (isInsideRecursion) { return true; }
			}
		}
		return false;
	}
	
	/**
	 * Processing cycles across multiple files
	 * -the names cannot overlap
	 * 
	 * @param exportedCalls - mapping of exported calls from another file
	 * @throws OverlappingFunctionalitiesException
	 */
	public void connectFromAnotherFile(Map<String, Set<String>> exportedCalls) throws OverlappingFunctionalitiesException {
		for (String exportedKey: exportedCalls.keySet()) {
			if (this.connectionsInCalls.containsKey(exportedKey)) {
				throw new OverlappingFunctionalitiesException(
						"Functionality cannot be merged due to duplicate keys: " + exportedKey);
			}
		}
		this.connectionsInCalls.putAll(exportedCalls);
	}
	
	/**
	 * Initializes the functionality to find cycles
	 * -for particular code fragment finds all calls/declarations and inserts it in form of set under selected name
	 * -
	 * @param astPart - actually processed AST element
	 * @param astParent - the parent of actually processed AST element
	 */
	private void initializeCycleFinder(JSONObject astPart, JSONObject astParent) {
		if (astPart == null) { return; }
		
		this.cycleSearch(astPart, astParent);
		String key;
		Object entryValue;
		JSONObject entryJSONObject;
		for(Object entryKey: astPart.keySet()) {
			key = (String) entryKey;
			entryValue = astPart.get(key);
			//if (key.equals("illegalDecorators")) {	continue; }
			if (entryValue instanceof JSONObject) {
				entryJSONObject = (JSONObject) entryValue;
				this.initializeCycleFinder(entryJSONObject, astPart);
			} else if(entryValue instanceof JSONArray) {
				for (Object arrayPart: ((JSONArray) entryValue)) {
					entryJSONObject = (JSONObject) arrayPart;
					this.initializeCycleFinder(entryJSONObject, astPart);
				}
			}
		}
	}
	
	/**
	 * Manages naming of functionalities that can recursively occur in the way to be easily retrievable
	 * -method names
	 * -class names (separated with dot) - constructor calls
	 * -class functions
	 * 
	 * @param astPart - actually processed AST element
	 * @param astParent - the parent of actually processed AST element
	 * @param astGrandParent - the grand-parent of actually processed AST element
	 * @param connectionSet - the set of all called/declared functionality
	 */
	private void harvestInnerCalls(JSONObject astPart, JSONObject astParent, JSONObject astGrandParent, Set<String> connectionSet) {
		if (astPart == null) { return ; }
		JSONObject astElement;
		String astName, astClassName;
		//astParent.containsKey("body") &&
		if ( astPart.containsKey("statements")) {
			for (Object arrayPart: ((JSONArray) astPart.get("statements"))) {
				astElement = (JSONObject) arrayPart;
				if (astElement.containsKey("declarationList") || astElement.containsKey("body") || astElement.containsKey("parameters")) {
					continue;
				}
				astName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(astElement);
				if (astGrandParent.containsKey("members") && astGrandParent.containsKey("name")) {
					astClassName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(astGrandParent);
					if (DebugInformation.SHOW_POLLUTING_INFORMATION) { logger.debug("--> "  + astClassName + "." + astName); }
					if (astClassName.equals(astName)) {
						if (DebugInformation.SHOW_POLLUTING_INFORMATION) { logger.debug("--> " + astName); }
						connectionSet.add(astName);
					} else {
						if (DebugInformation.SHOW_POLLUTING_INFORMATION) { logger.debug("--> " + astClassName + "." + astName); }
						if (this.ignoreClassFunctionOverlap) { connectionSet.add(astName); }
						connectionSet.add(astClassName + "." + astName);
					}
				} else {
					if (DebugInformation.SHOW_POLLUTING_INFORMATION) { logger.debug("--> " + astName); }
					connectionSet.add(astName);
				}
			}
		}
	
		String key;
		Object entryValue;
		JSONObject entryJSONObject;
		for(Object entryKey: astPart.keySet()) {
			key = (String) entryKey;
			entryValue = astPart.get(key);
			//if (key.equals("illegalDecorators")) {	continue; }
			if (entryValue instanceof JSONObject) {
				entryJSONObject = (JSONObject) entryValue;
				this.harvestInnerCalls(entryJSONObject, astPart, astParent, connectionSet);
			} else if(entryValue instanceof JSONArray) {
				for (Object arrayPart: ((JSONArray) entryValue)) {
					entryJSONObject = (JSONObject) arrayPart;
					this.harvestInnerCalls(entryJSONObject, astPart, astParent, connectionSet);
				}
			}
		}
	}

	/**
	 * Creates and inserts code are from the provided astPart
	 * 
	 * @param astPart - actually processed AST element
	 * @param constructName - the name of processed bounded code construct
	 */
	private void createAndInsertCodeArea(JSONObject astPart, String constructName) {
		Long startInitializerPosition = (Long) astPart.get("pos");
		Long endInitializerPosition = (Long) astPart.get("end");
		CodeArea newCodeArea = new CodeArea(constructName, startInitializerPosition, endInitializerPosition);
		this.areaDeclaration.add(newCodeArea);
	}
	
	/**
	 * Search for calls in particular AST part (code fragment)
	 * -retrieves code fragments that can cycle (method, class methods and class constructors)
	 * 
	 * @param astPart - actually processed AST element
	 * @param astParent - the parent of actually processed AST element
	 */
	private void cycleSearch(JSONObject astPart, JSONObject astParent) {
		String constructName, parentName;
		Set<String> harvestedInnerCalls;
		
		if (astParent.containsKey("members") && astPart.containsKey("parameters") && astPart.containsKey("name")) { //GET ANNOTATIONS FOR CLASS FUNCTION DECORATORS
			parentName = ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(astParent);
			constructName = ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(astPart);
			if (DebugInformation.SHOW_POLLUTING_INFORMATION) { 
				logger.debug("CLASS FUNCTION: " + parentName + "." + constructName);
			}
			
			harvestedInnerCalls = new HashSet<String>();
			this.harvestInnerCalls(astPart, astPart, astParent, harvestedInnerCalls);
			this.connectionsInCalls.put(parentName + "." + constructName, harvestedInnerCalls);
			if (this.ignoreClassFunctionOverlap) { this.connectionsInCalls.put(constructName, harvestedInnerCalls); }
			this.createAndInsertCodeArea(astPart, constructName);
		} else if (astParent.containsKey("members") && !astParent.containsKey("parameters") && astParent.containsKey("name")) { //GET CLASS
			parentName = ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(astParent);
			if (DebugInformation.SHOW_POLLUTING_INFORMATION) { logger.debug("CLASS: " + parentName); }
			
			harvestedInnerCalls = new HashSet<String>();
			this.harvestInnerCalls(astParent, astParent, astParent, harvestedInnerCalls);
			this.connectionsInCalls.put(parentName, harvestedInnerCalls);
			this.createAndInsertCodeArea(astParent, parentName);
		} else if (!astParent.containsKey("members") && astPart.containsKey("parameters") && astPart.containsKey("body")) { //GET ANNOTATIONS FOR FUNCTION DECORATORS //directly from part
			if (!astPart.containsKey("name")) { // CLASS CONSTRUCTOR
				// this can cycle - constructor and class creation
				constructName = ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(astPart);
				if (DebugInformation.SHOW_POLLUTING_INFORMATION) { logger.debug("CONSTRUCTOR: " + constructName); }
				
				harvestedInnerCalls = new HashSet<String>();
				this.harvestInnerCalls(astPart, astPart, astParent, harvestedInnerCalls);
				this.connectionsInCalls.put(constructName, harvestedInnerCalls);
				this.createAndInsertCodeArea(astPart, constructName);
			} else {
				// FUNCTION BODY
				constructName = ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(astPart);
				if (DebugInformation.SHOW_POLLUTING_INFORMATION) { logger.debug("NON-CLASS FUNCTION: " + constructName); }

				harvestedInnerCalls = new HashSet<String>();
				this.harvestInnerCalls(astPart, astParent, astParent, harvestedInnerCalls);
				this.connectionsInCalls.put(constructName, harvestedInnerCalls);
				this.createAndInsertCodeArea(astPart, constructName);
			}
		}  else if (astParent.containsKey("declarationList") && astPart.containsKey("declarations")) {	//GET ANNOTATION FOR GLOBAL CONTEXT VARIABLES
			// variables cannot cycle
		} else if (astPart.containsKey("members") && astPart.containsKey("name")) {	//CLASS ANNOTATION - processed directly
			// class annotations cannot cycle
		} else if (astPart.containsKey("parameters")) {	//PARAMETER ANNOTATION - processed directly
			// parameter annotations cannot cycle
		} else {
			// remaining annotations cannot cycle
		}
	}
	
	/**
	 * Checks if functionality is called in cycle according to context string identifier
	 * - tries to find cycle according to name sequence/connections
	 * 
	 * @param contextStringIdentifier - the identifier of string context
	 * @return true if functionality is called recursively otherwise false
	 */
	public boolean isCycleFromContextIdentifier(String contextStringIdentifier) {
		String contextIDParts[];
		if (this.ignoreClassFunctionOverlap) {
			contextIDParts = contextStringIdentifier.strip().split(".");
			if (contextIDParts.length == 0) { return false; }
			logger.debug("cc: " + contextIDParts[contextIDParts.length - 1]);
			return this.isCycle(contextIDParts[contextIDParts.length - 1]);
		}
		return false;
	}
	
	/**
	 * Checks if functionality is called in cycle - tries to find cycle according to name sequence/connections
	 * -the initial check from the chosen beginning of the sequence
	 * -if true then functionality is recursively called otherwise not
	 * 
	 * @param callName - the name of called functionality
	 * @return true if the functionality is recursively called - is called in cycle otherwise false
	 */
	public boolean isCycle(String callName) {
		Set<String> processedCalls = new HashSet<String>();
		Set<String> calledFunctionSet = this.connectionsInCalls.get(callName);
		if (calledFunctionSet == null) { return false; }
		if (calledFunctionSet.contains(callName)) { return true; }
		boolean isCycle;
		for (String calledFunction: calledFunctionSet) {
			isCycle = this.isCycle(calledFunction, callName, processedCalls);
			if (isCycle) { return true; }
		}
		return false;
	}
	
	/**
	 * Checks if functionality is called in cycle - tries to find cycle according to name sequence/connections
	 * -deeper checks iterating in the sequence
	 * -if true then functionality is recursively called otherwise not
	 * 
	 * @param callName - actually processed name in the sequence
	 * @param matchedName - the name that is searched in sequence (in the set of followers)
	 * @param processedCalls - the set of called functionalities from the call name functionality
	 * @return true if the functionality name sequence contains searched element (initial) - thus is cycle/recursively called otherwise returns false and is not
	 */
	private boolean isCycle(String callName, String matchedName, Set<String> processedCalls) {
		if (callName == null) { return false; }
		if (callName.equals(matchedName)) { return true; }
		if (!processedCalls.contains(callName)) {
			processedCalls.add(callName);
			Set<String> calledFunctionSet = this.connectionsInCalls.get(callName);
			if (calledFunctionSet == null) { return false; }
			if (calledFunctionSet.contains(matchedName)) { return true; }
			boolean isCycle;
			for (String calledFunction: calledFunctionSet) {
				isCycle = this.isCycle(calledFunction, matchedName, processedCalls);
				if (isCycle) { return true; }
			}
		}
		return false;
	}
}
