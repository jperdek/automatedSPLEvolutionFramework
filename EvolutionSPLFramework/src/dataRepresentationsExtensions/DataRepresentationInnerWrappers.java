package dataRepresentationsExtensions;


import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codeContext.processors.ASTTextExtractorTools;
import dataRepresentationsExtensions.logs.Logged;
import dataRepresentationsExtensions.stack.FunctionStackLogger;
import dividedAstExport.recursionCycleFinder.RecursionCycleFinder;
import evolutionSimulation.EvolutionConfiguration;


/**
 * Searches AST and injects functionality according to available AST objects
 * 
 * @author Jakub Perdek
 *
 */
public class DataRepresentationInnerWrappers {

	/**
	 * Logger to track data wrapper representations 
	 */
	private static final Logger logger = LoggerFactory.getLogger(DataRepresentationInnerWrappers.class);
	
	private FunctionStackLogger functionStackLogger;
	private Logged defaultGlobalLogged = null;
	
	/**
	 * Information if stack functionality is already pushed into derived SPLs
	 */
	private boolean availableStackStatus = DefaultDataRepresentations.SIMULATE_STACK;
	
	public DataRepresentationInnerWrappers() {
		this.functionStackLogger = new FunctionStackLogger();
	}

	public void additionalDataRepresentationsCreatorsInjector(EvolutionConfiguration evolutionConfiguration, DataRepresentationsConfiguration dataRepresentationsConfiguration, JSONObject astRoot) throws IOException, InterruptedException {
		this.additionalDataRepresentationsCreatorsInjector(astRoot, astRoot, astRoot, dataRepresentationsConfiguration);
		if (dataRepresentationsConfiguration.shouldInjectStackFunctionality()) {
			logger.info("Injecting stacks!");
			this.availableStackStatus = this.functionStackLogger.verifyIfStackIsAlreadyPushed(astRoot);
			if (!this.availableStackStatus) {
				this.functionStackLogger.initializeStackStubOnAst(astRoot); //base functions are added after all functionality is prepared
			}
		}
		if (dataRepresentationsConfiguration.shouldInjectLogFunctionality()) {
			logger.info("Injecting logs!");
			this.defaultGlobalLogged = new Logged(dataRepresentationsConfiguration, "root", evolutionConfiguration.getEvolvedContentName(), "", "");
			this.defaultGlobalLogged.initializeLoggerStubOnAst(astRoot);
		}
	}
	
	private void injectAdditionalDataRepresentationsCreators(JSONObject astPart, JSONObject astParent, 
			DataRepresentationsConfiguration dataRepresentationsConfiguration) throws IOException, InterruptedException  {
		if (astParent.containsKey("members") && astPart.containsKey("parameters") && 
				astPart.containsKey("name") && !astPart.containsKey("expression")) {
			//isClassRelated = true;
			this.processClassFunction(astPart, astParent, dataRepresentationsConfiguration);
		} else if (!astParent.containsKey("members") && astPart.containsKey("parameters") && astPart.containsKey("body") && !astPart.containsKey("expression")) {
			if (astPart.containsKey("name")) {
				//isClassRelated = false;
				this.processFunction(astPart, dataRepresentationsConfiguration);
			}
		} else if (astParent.containsKey("members") && astPart.containsKey("parameters") && 
				astPart.containsKey("body") && !astPart.containsKey("expression")) {
			if (!astPart.containsKey("name")) { // CLASS CONSTRUCTOR
				//isClassRelated = true;
				this.processClassConstructorFunction(astPart, astParent, dataRepresentationsConfiguration); //harvest class constructor
			}
		} else {
			// OTHER OBJECTS SUCH AS PARAMETERS, DECORATORS, VARIABLES... - UNUSABLE IN STRUCTURE OF CALLS
		}
	}

	private void additionalDataRepresentationsCreatorsInjector(JSONObject astRoot, JSONObject astPart, 
			JSONObject astParent, DataRepresentationsConfiguration dataRepresentationsConfiguration) throws IOException, InterruptedException {
		String key;
		if (astPart == null) { return; }
		
		this.injectAdditionalDataRepresentationsCreators(astPart, astParent, dataRepresentationsConfiguration);
		Object entryValue;
		JSONObject entryJSONObject;
		for(Object entryKey: astPart.keySet()) {
			key = (String) entryKey;
			entryValue = astPart.get(key);
			//if (key.equals("illegalDecorators")) { continue; }
			if (entryValue instanceof JSONObject) {
				entryJSONObject = (JSONObject) entryValue;
				this.additionalDataRepresentationsCreatorsInjector(astRoot, entryJSONObject, astPart, dataRepresentationsConfiguration);
			} else if(entryValue instanceof JSONArray) {
				for (Object arrayPart: ((JSONArray) entryValue)) {
					entryJSONObject = (JSONObject) arrayPart;
					this.additionalDataRepresentationsCreatorsInjector(astRoot, entryJSONObject, astPart, dataRepresentationsConfiguration);
				}
			}
		}
	}
	
	private String getParameters(JSONArray parametersArray) {
		boolean storeWholeObjects = false;
		String parameterObjectString = null;
		JSONObject parameterAst;
		String parameterName;
		for (Object parameterObject: parametersArray) {
			parameterAst = (JSONObject) parameterObject;
			parameterName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(parameterAst);
			if (storeWholeObjects) {
				if (parameterObjectString == null) {
					parameterObjectString = "\"" + parameterName + "\": " + parameterName;
				} else {
					parameterObjectString = parameterObjectString + ",\n\"" + parameterName + "\": " + parameterName;
				}
			} else {
				if (parameterObjectString == null) {
					parameterObjectString = "\"" + parameterName + "\": (typeof(" + parameterName + ")===\"object\" || typeof(\" + parameterName + \")===\"function\")? \"[Object]\" : " + parameterName;
				} else {
					parameterObjectString = parameterObjectString + ",\n\"" + parameterName + "\": (typeof(" + parameterName + ")===\"object\" || typeof(\" + parameterName + \")===\"function\")? \"[Object]\" : " + parameterName;
				}
			}
		}
		return parameterObjectString;
	}
	
	private String getParametersWithTypesAsString(JSONArray parametersArray) {
		String parameterObjectString = null;
		JSONObject parameterAst;
		String parameterName;
		for (Object parameterObject: parametersArray) {
			parameterAst = (JSONObject) parameterObject;
			parameterName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(parameterAst);
			if (parameterObjectString == null) {
				parameterObjectString = "\"" + parameterName + "\": \"" + parameterName + "\"";
			} else {
				parameterObjectString = parameterObjectString + ",\n\"" + parameterName + "\": \"" + parameterName + "\"";
			}
		}
		return parameterObjectString;
	}
	
	private void processFunction(JSONObject processedFunctionAst, 
			DataRepresentationsConfiguration dataRepresentationsConfiguration) throws IOException, InterruptedException {
		JSONObject bodyObject = (JSONObject) processedFunctionAst.get("body");
		JSONArray statementsArray = (JSONArray) bodyObject.get("statements");
		JSONArray parametersArray = (JSONArray) processedFunctionAst.get("parameters");
		String additionalObjectString;
		
		// TO-DO extract modifiers
		if (dataRepresentationsConfiguration.shouldInjectStackFunctionality() && !this.availableStackStatus) {
			additionalObjectString = this.getParameters(parametersArray);
			this.functionStackLogger.createReferencedInstance("", additionalObjectString, statementsArray);
		}
		if (dataRepresentationsConfiguration.shouldInjectLogFunctionality() && !dataRepresentationsConfiguration.shouldNotLogFunctions()) {
			additionalObjectString = this.getParametersWithTypesAsString(parametersArray);
			Logged localFunctionLogged = new Logged(dataRepresentationsConfiguration, null, "functionLogger", "", "");
			localFunctionLogged.extractAndPushFunctionality(additionalObjectString, statementsArray, dataRepresentationsConfiguration);	
		}
	}

	private void processClassFunction(JSONObject processedClassFunctionAst, JSONObject processedClassAst, 
			DataRepresentationsConfiguration dataRepresentationsConfiguration) throws IOException, InterruptedException {
		JSONObject bodyObject = (JSONObject) processedClassFunctionAst.get("body");
		JSONArray statementsArray = (JSONArray) bodyObject.get("statements");
		JSONArray parametersArray = (JSONArray) processedClassFunctionAst.get("parameters");
		String additionalObjectString;
		
		// TO-DO extract modifiers
		String className = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(processedClassAst);
		if (dataRepresentationsConfiguration.shouldInjectStackFunctionality() && !this.availableStackStatus) {
			additionalObjectString = this.getParameters(parametersArray);
			this.functionStackLogger.createReferencedInstance(className, additionalObjectString, statementsArray);
		}
		if (dataRepresentationsConfiguration.shouldInjectLogFunctionality() && !dataRepresentationsConfiguration.shouldNotLogClassFunctions()) {
			additionalObjectString = this.getParametersWithTypesAsString(parametersArray);
			Logged localFunctionLogged = new Logged(dataRepresentationsConfiguration, null, "classFunctionLogger", className, "");
			localFunctionLogged.extractAndPushFunctionality(additionalObjectString, statementsArray, dataRepresentationsConfiguration);
		}
	}
	
	//INCOMPLETE
	private void processClassConstructorFunction(JSONObject processedClassAst, JSONObject processClassAst, 
			DataRepresentationsConfiguration dataRepresentationsConfiguration) throws IOException, InterruptedException {
		JSONObject bodyObject = (JSONObject) processedClassAst.get("body");
		JSONArray statementsArray = (JSONArray) bodyObject.get("statements");
		JSONArray parametersArray = (JSONArray) processedClassAst.get("parameters");
		String additionalObjectString;
		
		// TO-DO extract modifiers
		String className = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(processedClassAst);
		String callOfclassName = "new " + className;
		if (dataRepresentationsConfiguration.shouldInjectStackFunctionality() && !this.availableStackStatus) {
			additionalObjectString = this.getParameters(parametersArray);
			this.functionStackLogger.createReferencedInstance(callOfclassName, additionalObjectString, statementsArray);
		}
		if (dataRepresentationsConfiguration.shouldInjectLogFunctionality() && !dataRepresentationsConfiguration.shouldNotLogClassConstructors()) {
			additionalObjectString = this.getParametersWithTypesAsString(parametersArray);
			Logged localFunctionLogged = new Logged(dataRepresentationsConfiguration, null, "classConstructorsLogger", className, "");
			localFunctionLogged.extractAndPushFunctionality(additionalObjectString, statementsArray, dataRepresentationsConfiguration);
		}
	}	
}
