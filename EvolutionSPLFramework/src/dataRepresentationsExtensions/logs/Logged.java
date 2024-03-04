package dataRepresentationsExtensions.logs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import astFileProcessor.ASTLoader;
import codeConstructsEvaluation.transformation.ASTConverterClient;
import dataRepresentationsExtensions.DataRepresentationsConfiguration;
import dataRepresentationsExtensions.DefaultDataRepresentations;


/**
 * Manages creation and preparation of variables to be aggregated according to used abstraction level
 * - instantiates functionality to aggregate variables from different code constructs to prepare logging command
 * (variable dependencies if collected from final products)
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class Logged {

	/**
	 * The mapping of extracted variables to their types
	 */
	private Map<String, String> loggedVariables;
	
	/**
	 * String identification of variation point representation
	 */
	private String variationPointIdentifier;
	
	/**
	 * The identifier of particular logger - information about operation level under code is (should be) included during its generation 
	 */
	private String loggerId = "";
	
	/**
	 * The name of processed class (optional)
	 */
	private String className = "";
	
	/**
	 * Additional information used to identify variation point - such as function arguments,...
	 */
	private String additionalObjectString = "";
	
	/**
	 * Sets to not log objects - objects will not be logged if true otherwise they will
	 */
	private boolean notLogObjects = false;
	
	/**
	 * Sets to not log any functions - any function will not be logged if true otherwise they will
	 */
	private boolean notLogFunctions = false;

	
	/**
	 * Instantiates functionality to aggregate variables from different code constructs to prepare logging command 
	 * (variable dependencies if collected from final products)
	 * 
	 * @param variabilityPointIdentifier - string identification of variation point representation
	 * @param loggerId - the identifier of particular logger - information about operation level under code is (should be) included during its generation 
	 */
	public Logged(String variabilityPointIdentifier, String loggerId) {
		this(variabilityPointIdentifier, loggerId, "", "", DefaultDataRepresentations.NOT_LOG_OBJECTS, 
				DefaultDataRepresentations.NOT_LOG_FUNCTIONS);
	}
	
	/**
	 * Instantiates functionality to aggregate variables from different code constructs to prepare logging command 
	 * (variable dependencies if collected from final products)
	 * 
	 * @param dataRepresentationsConfiguration - the configuration with logging detail and optionally also target variables
	 * @param variabilityPointIdentifier - string identification of variation point representation
	 * @param loggerId - the identifier of particular logger - information about operation level under code is (should be) included during its generation 
	 * @param className - the name of processed class if any (optional)
	 * @param additionalObjectString - additional information used to identify variation point - such as function arguments,...
	 */
	public Logged(DataRepresentationsConfiguration dataRepresentationsConfiguration, String variabilityPointIdentifier, 
			String loggerId, String className, String additionalObjectString) {
		this(variabilityPointIdentifier, loggerId, className, additionalObjectString, dataRepresentationsConfiguration.shouldNotLogObjects(), 
			 dataRepresentationsConfiguration.shouldNotLogFunctions() || dataRepresentationsConfiguration.shouldNotLogClassConstructors() 
			 || dataRepresentationsConfiguration.shouldNotLogClassFunctions());
	}
	
	/**
	 * Instantiates functionality to aggregate variables from different code constructs to prepare logging command 
	 * (variable dependencies if collected from final products)
	 * 
	 * @param variabilityPointIdentifier - string identification of variation point representation
	 * @param loggerId - the identifier of particular logger - information about operation level under code is (should be) included during its generation 
	 * @param className - the name of processed class if any (optional) 
	 * @param additionalObjectString - additional information used to identify variation point - such as function arguments,...
	 * @param notLogObjects - sets to not log objects - objects will not be logged if true otherwise they will
	 * @param notLogFunctions - sets to not log any functions - any function will not be logged if true otherwise they will
	 */
	public Logged(String variabilityPointIdentifier, String loggerId, String className, 
			String additionalObjectString, boolean notLogObjects, boolean notLogFunctions) {
		this(variabilityPointIdentifier);
		this.loggerId = loggerId;
		this.notLogObjects = notLogObjects;
		this.notLogFunctions = notLogFunctions;
	}
	
	/**
	 * Instantiates functionality to aggregate variables from different code constructs to prepare logging command 
	 * (variable dependencies if collected from final products)
	 * 
	 * @param variabilityPointIdentifier - string identification of variation point representation
	 */
	public Logged(String variabilityPointIdentifier) {
		this.loggedVariables = new HashMap<String, String>();
		this.variationPointIdentifier = variabilityPointIdentifier;
	}
	
	/**
	 * Extracts variables from JSON and pushes functionality on the first position
	 * 
	 * @param additionalObjectString - additional information used to identify variation point - such as function arguments,...
	 * @param baseSplArray - the JSON array of statements where logger stub is going to be injected
	 * @param dataRepresentationsConfiguration - the configuration with logging detail and optionally also target variables
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void extractAndPushFunctionality(String additionalObjectString, JSONArray baseSplArray, 
			DataRepresentationsConfiguration dataRepresentationsConfiguration) throws IOException, InterruptedException {
		LoggedRepresentation.extractSamples(ASTLoader.loadASTFromString(" { " + additionalObjectString + " } "), this, dataRepresentationsConfiguration);
		this.appendLoggerStubOnAstFirstPosition(baseSplArray);
	}
	
	/**
	 * Merges aggregated variables for logging from another Logged object
	 * 
	 * @param newLogged - instantiates functionality to aggregate variables from different code constructs to prepare logging command 
	 */
	public void mergeLoggedVariables(Logged newLogged) {
		this.variationPointIdentifier = newLogged.variationPointIdentifier;
		this.loggedVariables.putAll(newLogged.loggedVariables);
	}
	
	/**
	 * Inserts variable name used to be logged after instantiation of derived product to the aggregation of logged variables
	 * 
	 * @param loggedName - the variable name that will be logged after product instantiation, the variable is stored also as type
	 */
	public void add(String loggedName) {
		this.loggedVariables.put(loggedName, loggedName);
	}
	
	/**
	 * Inserts variable name and type used to be logged after instantiation of derived product to the aggregation of logged variables
	 * 
	 * @param loggedName - the variable name that will be logged after product instantiation
	 * @param loggedValue - the type of provided variable name that will be logged after product instantiation 
	 */
	public void add(String loggedName, String loggedValue) {
		this.loggedVariables.put(loggedName, loggedValue);
	}
	
	/**
	 * Returns the code written in JavaScript for initialization of Logger
	 * 
	 * @return the code written in JavaScript for initialization of Logger
	 */
	public String constructLoggerInitialization() {
		return String.join("\n",
						   "var globalResult = {};",
						   "function logData(variabilityPointIdentifier, data, name) {",
						   (this.notLogObjects)? "if (typeof(data)===\"object\") { return; };" : "",
						   (this.notLogFunctions)? "if (typeof(data)===\"function\") { return; };" : "",
						   "    if (globalResult[variabilityPointIdentifier] === undefined) { globalResult[variabilityPointIdentifier] = {}; };",
						   " 	if (globalResult[variabilityPointIdentifier][name] === undefined) { globalResult[variabilityPointIdentifier][name] = []; };",
						   "	globalResult[variabilityPointIdentifier][name].push(data);",
						   "}");
	}
	
	/**
	 * Initializes the logger stub in the AST of final product script - this stub is injected behind import statements
	 * 
	 * @param astRoot - the root of AST script of final product
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void initializeLoggerStubOnAst(JSONObject astRoot) throws IOException, InterruptedException {
		JSONObject loggerStubAst = (JSONObject) ASTConverterClient.convertFromCodeToASTJSON(this.constructLoggerInitialization()).get("ast");
		JSONArray loggerStubStatements = (JSONArray) loggerStubAst.get("statements");
		
		int indexToPlace = 0;
		JSONArray baseSplStatements = (JSONArray) astRoot.get("statements");
		for (Object baseAstStatementObject: baseSplStatements) {
			if (!((JSONObject) baseAstStatementObject).containsKey("importClause")) {
				break;
			}
			indexToPlace++;
		}
		if (baseSplStatements.size() == indexToPlace) { indexToPlace--; }
		baseSplStatements.addAll(indexToPlace, loggerStubStatements);
	}

	/**
	 * Prepares functionality to extract function name and stringify all identifiers (logger id, log category, function name) into one variation point identifier of logger
	 * 
	 * @return the string representation of extracted function name and (unique) variation point identifier of logger
	 */
	private String findVariationPointIdentifier() {
		String functionNameMap = "\"fname\": arguments.callee.toString().substring(0, arguments.callee.toString().split(\"(\")[0].length) ";
		String classNameMap = "\"fname\": \"" + this.className + "\"";
		String parameterObject = "JSON.stringify({ " + functionNameMap + ", \"logCategory\": \"" + loggerId + "\", " + classNameMap + ", " + this.additionalObjectString + " })"; //to enable parse JavaScript call to be executed as JavaScript object
		return parameterObject;
	}
	
	/**
	 * Creates support for data representation of logger
	 * 
	 * @param className - the name of processed class if any (optional)
	 * @param additionalObjectString - additional information used to identify variation point - such as function arguments,...
	 * @param statementsArray - the JSON array of statements where logger stub is going to be injected
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void createReferencedInstance(String className, String additionalObjectString, JSONArray statementsArray) throws IOException, InterruptedException {
		this.className = className;
		this.additionalObjectString = additionalObjectString;
		this.appendLoggerStubOnAstFirstPosition(statementsArray);
	}
	
	/**
	 * Creates string command to log variable with the same type as name
	 * 
	 * @param variableName - the variable name that will be logged after product instantiation 
	 * @return string command to log group of variables or variable with their type
	 */
	public String constructLogger(String variableName) {
		String variationPointIdentifier = this.variationPointIdentifier;
		if (variationPointIdentifier == null) { variationPointIdentifier = this.findVariationPointIdentifier(); } else { variationPointIdentifier = "\"" + variationPointIdentifier + "\"";}
		return "logData(" + variationPointIdentifier + ", "+ variableName + ", \"" + variableName + "\");" + System.lineSeparator() ;
	}
	
	/**
	 * Creates string command to log variable with its type
	 * 
	 * @param variableName - the variable name that will be logged after product instantiation
	 * @param variableValue - the type of provided variable name that will be logged after product instantiation 
	 * @return string command to log group of variables or variable with their type
	 */
	public String constructLogger(String variableName, String variableValue) {
		String variationPointIdentifier = this.variationPointIdentifier;
		if (variationPointIdentifier == null) { variationPointIdentifier = this.findVariationPointIdentifier(); } else { variationPointIdentifier = "\"" + variationPointIdentifier + "\"";}
		return "logData(" + variationPointIdentifier + ", "+ variableValue + ", \"" + variableName + "\");" + System.lineSeparator() ;
	}
	
	/**
	 * Creates AST from command to log variable with the same type as name
	 *  
	 * @param variableName - the variable name that will be logged after product instantiation
	 * @return JSON AST of command to log variable with the same type as name
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public JSONObject constructLoggerAst(String variableName) throws IOException, InterruptedException {
		return ASTConverterClient.getFirstStatementFromASTFile(ASTConverterClient.convertFromCodeToASTJSON(this.constructLogger(variableName)));
	}
	
	/**
	 * Creates AST from command to log variable with its type
	 * 
	 * @param variableName - the variable name that will be logged after product instantiation
	 * @param variableValue - the type of provided variable name that will be logged after product instantiation 
	 * @return JSON AST of command to log variable with its type
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public JSONObject constructLoggerAst(String variableName, String variableValue) throws IOException, InterruptedException {
		//System.out.println(this.constructLogger(variableName, variableValue)); //CODE NOT TO JSON
		return ASTConverterClient.getFirstStatementFromASTFile((JSONObject) ASTConverterClient.convertFromCodeToASTJSON(this.constructLogger(variableName, variableValue)));
	}
	
	/**
	 * Returns the string consisting of all concatenated string commands to log all available logged variables with their types
	 * 
	 * @return the string consisting of all concatenated string commands to log all available logged variables with their types
	 */
	public String getLoggedPartsString() {
		Iterator<Entry<String, String>> logIterator = this.loggedVariables.entrySet().iterator();
		Entry<String, String> processedEntry;
		String variableName, variableValue;
		String result = "";
		
		while(logIterator.hasNext()) {
			processedEntry = logIterator.next();
			variableName = processedEntry.getKey();
			variableValue = processedEntry.getValue();
			result = result + this.constructLogger(variableName, variableValue);
		}
		return result;
	}
	
	/**
	 * Returns the JSON AST array consisting of all JSON AST commands to log all available logged variables with their types
	 * 
	 * @return the JSON AST array consisting of all JSON AST commands to log all available logged variables with their types
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public JSONArray getLoggedPartsAstArray() throws IOException, InterruptedException {
		Iterator<Entry<String, String>> logIterator = this.loggedVariables.entrySet().iterator();
		Entry<String, String> processedEntry;
		String variableName, variableValue;
		JSONArray loggingFunctionality = new JSONArray();
		
		JSONObject loggerAst;
		while(logIterator.hasNext()) {
			processedEntry = logIterator.next();
			variableName = processedEntry.getKey();
			variableValue = processedEntry.getValue();
			
			loggerAst = this.constructLoggerAst(variableName, variableValue);
			loggingFunctionality.add(loggerAst);
		}
		return loggingFunctionality;
	}
	
	/**
	 * Appends the logging command to the first position of the provided JSON array of statements where logger stub is going to be injected that is given by the name
	 * 
	 * @param astPart - the processed AST element with statements where logger stub is going to be injected (default statements)
	 * @param affectedAstArrayName - the name of JSON array of statements where logger stub is going to be injected (default statements)
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void appendLoggerStubOnAstFirstPosition(JSONObject astPart, String affectedAstArrayName) throws IOException, InterruptedException {
		JSONArray loggerStubStatements = this.getLoggedPartsAstArray();
		
		int indexToPlace = 0;
		JSONArray baseSplArray = (JSONArray) astPart.get(affectedAstArrayName);
		baseSplArray.addAll(indexToPlace, loggerStubStatements);
	}
	
	/**
	 * Appends the logging command to the first position of the provided JSON array of statements where logger stub is going to be injected
	 * 
	 * @param baseSplArray - the JSON array of statements where logger stub is going to be injected
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void appendLoggerStubOnAstFirstPosition(JSONArray baseSplArray) throws IOException, InterruptedException {
		JSONArray loggerStubStatements = this.getLoggedPartsAstArray();
		
		int indexToPlace = 0;
		baseSplArray.addAll(indexToPlace, loggerStubStatements);
	}
}
