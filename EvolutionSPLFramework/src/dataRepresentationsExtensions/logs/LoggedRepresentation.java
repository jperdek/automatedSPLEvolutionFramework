package dataRepresentationsExtensions.logs;

import org.json.simple.JSONObject;

import dataRepresentationsExtensions.DataRepresentationsConfiguration;
import dataRepresentationsExtensions.variabilityPermutations.VariabilityMerger;
import dataRepresentationsExtensions.variabilityPermutations.VariabilityPart;
import dataRepresentationsExtensions.variabilityPermutations.VariableAndType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Representation for incorporate logging
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class LoggedRepresentation {

	/**
	 * Instantiates representation to generate data for logging 
	 */
	public LoggedRepresentation() {}
	
	
	/**
	 * Combines chosen/preferred variables into groups up to given size to be logged
	 * 
	 * @param mainVariableName - the analyzed variable name
	 * @param representativeVariableValue - previous generated value string
	 * @param processedObject - JSON with variables where variables are usually stored as key value pair where key is the name and value is their type
	 * @param logged - the associated logging instance with mapping of related variables to types
	 * @param dataRepresentationsConfiguration - the configuration with logging detail and optionally also target variables
	 */
	public static void combine(String mainVariableName, String representativeVariableValue, JSONObject processedObject, Logged logged, 
			DataRepresentationsConfiguration dataRepresentationsConfiguration) {
		JSONObject processedJSON = (JSONObject) processedObject;
		Map<Integer, VariableAndType> variablesForNames = LoggedRepresentation.getVariableNamesWithTypes(processedJSON, dataRepresentationsConfiguration);

		int maximumLength = dataRepresentationsConfiguration.getMaximumVariableCombinationsToLog();
		
		List<VariabilityPart<Integer>> variabilityPartList = VariabilityMerger.prepareVariabilityCases(variablesForNames.size(), maximumLength);
		Iterator<VariabilityPart<Integer>> variabilityCasesIterator = variabilityPartList.iterator();
		Iterator<Integer> childrenCaseIdentifiersIterator;
		VariabilityPart<Integer> processedChild;
		Integer processedIndex;
		VariableAndType actualVariable;
		String resultVariableName = mainVariableName;
		String variableName, variableType;
		
		while(variabilityCasesIterator.hasNext()) {
			resultVariableName = "";
			processedChild = variabilityCasesIterator.next();

			childrenCaseIdentifiersIterator = (Iterator<Integer>) processedChild.getComponentsCollection().iterator();
			while (childrenCaseIdentifiersIterator.hasNext()) {
				processedIndex = childrenCaseIdentifiersIterator.next();
				actualVariable = variablesForNames.get(processedIndex);
				
				variableName = actualVariable.getVariable();
				variableType = actualVariable.getType();
				if (variableType.equals("int") || variableType.equals("float")) {
					resultVariableName = resultVariableName + "__" + variableName + "_\" + " + convertIntToVariable(variableName) + " + \"";
				} else {
					resultVariableName = resultVariableName + "__" + variableName + "_\" + " + variableName + " + \"";
				}
			}
			logged.add(resultVariableName, representativeVariableValue);
		}
	}	
	
	/**
	 * Combines chosen/preferred variables into groups up to given size to be logged
	 * 
	 * @param mainVariableName - the analyzed variable name
	 * @param representativeVariableValue - previous generated value string
	 * @param variableNames - the list of available variable names
	 * @param logged - the associated logging instance with mapping of related variables to types
	 * @param dataRepresentationsConfiguration - the configuration with logging detail and optionally also target variables
	 */
	public static void combineVariables(String mainVariableName, String representativeVariableValue, 
			List<String> variableNames, Logged logged, DataRepresentationsConfiguration dataRepresentationsConfiguration) {
		List<VariabilityPart<Integer>> variabilityPartList = VariabilityMerger.prepareVariabilityCases(variableNames.size(), dataRepresentationsConfiguration.getMaximumVariableCombinationsToLog());
		Iterator<VariabilityPart<Integer>> variabilityCasesIterator = variabilityPartList.iterator();
		Iterator<Integer> childrenCaseIdentifiersIterator;
		VariabilityPart<Integer> processedChild;
		Integer processedIndex;
		String resultVariableName = mainVariableName;
		String variableName;
		
		while(variabilityCasesIterator.hasNext()) {
			resultVariableName = representativeVariableValue + "___";
			processedChild = variabilityCasesIterator.next();

			childrenCaseIdentifiersIterator = (Iterator<Integer>) processedChild.getComponentsCollection().iterator();
			while (childrenCaseIdentifiersIterator.hasNext()) {
				processedIndex = childrenCaseIdentifiersIterator.next();
				variableName = variableNames.get(processedIndex);
				
				//resultVariableName = resultVariableName + "__" + variableName + "_\" + JSON.stringify(" + variableName + ") + \"";
				resultVariableName = resultVariableName + "__" + variableName + "_\" + ((typeof(" + variableName + ")===\"function\" || typeof(" + 
				    variableName + ")===\"object\")? \"O\" :" +  variableName + ") + \"";
			}
			logged.add(resultVariableName, representativeVariableValue);
		}
	}

	/**
	 * Converts variable into integer
	 * 
	 * @param variableName - the name of variable that needs to be converted into integer
	 * @return command to convert variable into integer
	 */
	private static String convertVariableToInt(String variableName) {
		return "parseInt(" + variableName + ")"; 
	}
	
	/**
	 * Converts variable into string
	 * 
	 * @param variableName - the name of variable that needs to be converted into string
	 * @return command to convert variable into string
	 */
	private static String convertIntToVariable(String variableName) {
		return variableName + ".toString()";
	}
	
	/**
	 * Extracts variable names with their types and prepares mapping of them under unique identifier
	 * 
	 * @param processedJSON - JSON with variables where variables are usually stored as key value pair where key is the name and value is their type
	 * @param dataRepresentationsConfiguration - the configuration with logging detail and optionally also target variables
	 * @return the map of variable id to extracted variable representation
	 */
	private static Map<Integer, VariableAndType> getVariableNamesWithTypes(JSONObject processedJSON, DataRepresentationsConfiguration dataRepresentationsConfiguration) {
		Map<Integer, VariableAndType> variablesForNames = new HashMap<Integer, VariableAndType>();
		
		String variableName, variableType;
		Iterator<Entry<String, Object>> processedEntryIterator;
        Entry<String, Object> processedEntry;
		
		int numberVariables = 0;
		for(Object nameToCombineRepr: processedJSON.entrySet()) {
			if (nameToCombineRepr instanceof String) {
				variableName = (String) nameToCombineRepr;
				variableType = "String";
				variablesForNames.put(numberVariables, new VariableAndType(variableName, variableType));
				numberVariables = numberVariables + 1;
			} else if (nameToCombineRepr instanceof Map.Entry) {
				Entry<Object, Object> objectMap = (Map.Entry<Object, Object>) nameToCombineRepr;
				variableName = (String) objectMap.getKey();
				variableType = (String) objectMap.getValue();
				variablesForNames.put(numberVariables, new VariableAndType(variableName, variableType));
				numberVariables = numberVariables + 1;
			} else {	
				processedEntryIterator = ((JSONObject) nameToCombineRepr).entrySet().iterator();

    			while(processedEntryIterator.hasNext()) {
    				processedEntry = processedEntryIterator.next();
    				variableName = (String) processedEntry.getKey();
    				variableType = (String) processedEntry.getValue();
    				variablesForNames.put(numberVariables, new VariableAndType(variableName, variableType));
    				numberVariables = numberVariables + 1;
    				//logged.add(nameToLog, valueToLog);
    			}
			}
		}
		return variablesForNames;
	}
	
	/**
	 * Extracts variables from JSON object and combines them into groups 
	 * 
	 * @param processedJSON - JSON with variables where variables are usually stored as key value pair where key is the name and value is their type
	 * @param logged - the associated logging instance with mapping of related variables to types
	 * @param dataRepresentationsConfiguration - the configuration with logging detail and optionally also target variables
	 */
	public static void extractSamples(JSONObject processedJSON, Logged logged, DataRepresentationsConfiguration dataRepresentationsConfiguration) {
		Iterator<Entry<String, Object>> processedEntryIterator = processedJSON.entrySet().iterator();
		Entry<String, Object> processedEntry;
		String nameToLog;
		String valueToLog;
		
		while(processedEntryIterator.hasNext()) {
			processedEntry = processedEntryIterator.next();
			nameToLog = processedEntry.getKey();
			valueToLog = (String) processedEntry.getValue();
			LoggedRepresentation.combine(nameToLog, valueToLog, processedJSON, logged, dataRepresentationsConfiguration);
		}
	}
}
