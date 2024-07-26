package codeContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONObject;

import codeContext.InnerContext.Direction;
import codeContext.objects.VariableObject;
import codeContext.processors.NotFoundVariableDeclaration;
import codeContext.processors.export.ExportedObjectInterface;
import codeContext.processors.export.ExportsProcessor;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.ActualScriptVariablesToSubstituteConfiguration;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.VariableObjectCollector;


/**
 * General representation of code context
 * -aggregation of both global context and top inner context
 * 
 * @author Jakub Perdek
 *
 */
public class CodeContext {
	
	/**
	 * Global context - accessible in all places (such as variables declared as var in JavaScript)
	 */
	protected GlobalContext globalContext;
	
	/**
	 * Inner context - the context known as local context, context inside functions, classes
	 */
	protected InnerContext innerContext;
	
	/**
	 * Default top inner context
	 */
	protected ExportedObjectInterface defaultContext;
	
	/**
	 * The name of processed file from which context are aggregated
	 */
	protected String fileName;

	
	/**
	 * Creates the base code context and stores global, actually processed/top inner context and file name
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param innerContext - inner context - the context known as local context, context inside functions, classes
	 * @param fileName - the name of processed file from which context are aggregated
	 */
	public CodeContext(GlobalContext globalContext, InnerContext innerContext, String fileName) {
		this.globalContext = globalContext;
		this.innerContext = innerContext;
		this.fileName = fileName;
	}
	
	/**
	 * Returns the file name
	 * 
	 * @return the file name
	 */
	public String getFileName() { return this.fileName; }

	/**
	 * Finds exported context from all contexts in given application AST and sets it as default context
	 * 
	 * @param astRoot - given application AST from which the contexts are harvested
	 */
	public void findExportedContext(JSONObject astRoot) {
		this.defaultContext = ExportsProcessor.getDefaultExportFromAst(astRoot, this.globalContext, this.innerContext);
	}

	/**
	 * Adds import to the global imports if import contains "importClause"
	 * 
	 * @param astImport - the import that should be added to the global imports if contains "importClause"
	 */
	public void addImport(JSONObject astImport) {
		if (astImport.containsKey("importClause")) { this.globalContext.addGlobalImport(astImport); }
	}
	
	/**
	 * Assigns the processed variable to local (let, const in JavaScript) or global context (var in JavaScript) according to its declaration
	 *  
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param innerContext - inner context - the context known as local context, context inside functions, classes
	 * @param astRoot - the root of application AST
	 * @param astVariable - the AST of actually processed variable
	 * @param declarationName - the name of declaration
	 * @param contentText - information about the type of declaration (var, let or const in JavaScript) extracted from the text in application AST on root layer
	 * @param isDirectlyExported - information if the code context is exported, true if ode context is exported otherwise false
	 * @throws NotFoundVariableDeclaration
	 */
	public static void addVariable(GlobalContext globalContext, InnerContext innerContext, JSONObject astRoot, 
			JSONObject astVariable, String declarationName, String contentText, 
			boolean isDirectlyExported) throws NotFoundVariableDeclaration {
		Pattern pattern = Pattern.compile("(var|let|const)(\\s+|\\s+[^;]+,\\s*)" + declarationName, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(contentText);
	    
	    String matchedResult;
	    boolean matchFound = matcher.find();
	    if(matchFound) {
	      matchedResult = matcher.group();
	      if(matchedResult.toLowerCase().startsWith("var")) { //decides if global variable should be added
	    	  globalContext.addGlobalVariable(astVariable, astRoot, declarationName, isDirectlyExported);
	      } else { //otherwise local
	    	  innerContext.addVariable(astVariable, astRoot, declarationName, true, isDirectlyExported);
	      }
	    } else {
	    	throw new NotFoundVariableDeclaration("Variable declaration not found in AST text: " + declarationName);
	    }
	}
	
	/**
	 * Harvests variables, parameters including global ones to "actual"/specified position in this inner code context 
	 * 
	 * @param currentPosition - the current position in the aplication AST to decide about what is "actual"
	 * @param actualScriptVariablesToSubstituteConfiguration
	 * @return the list of actual variables, parameters of previously accessible from this inner context to actual position and global variables
	 */
	public List<VariableObject> getActualVariables(InnerContext searchedContext, long currentPosition, long startSearchPosition, long endSearchPosition, Direction direction, 
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration) {
		VariableObjectCollector variableObjectCollector = this.innerContext.collectVariableObjects(
				searchedContext, currentPosition, startSearchPosition, endSearchPosition, direction, actualScriptVariablesToSubstituteConfiguration);
		
		if (actualScriptVariablesToSubstituteConfiguration.useGlobalVariables()) {
			variableObjectCollector.addGlobalVariables(this.globalContext.getActualGlobalVariables(currentPosition));
		}
		return variableObjectCollector.getMergedCollectedVariables();
	}
	
	/**
	 * Returns the global context (with global variable, accessible on various places in the script)
	 * 
	 * @return the global context (with global variable, accessible on various places in the script)
	 */
	public GlobalContext getGlobalContext() { return this.globalContext; }
	
	/**
	 * Returns the representation of inner context from this instance
	 * 
	 * @return the representation of inner context from this instance
	 */
	public InnerContext getInnerContext() { return this.innerContext; }
	
	/**
	 * Returns and creates the descriptive JSON represented output from information about the code context
	 * 
	 * @param reachedPosition - the "actual" position reached in the script to get available (declared) variables and calls
	 * @return the descriptive JSON represented output from information about the code context
	 */
	public JSONObject createDescriptiveJSON(int reachedPosition) {
		JSONObject descriptiveJSON = new JSONObject();
		JSONObject globalContext = this.globalContext.createDescriptiveJSON();
		JSONObject actualContext = this.innerContext.createDescriptiveJSON(this.globalContext);
		if (globalContext != null && !globalContext.isEmpty()) {
			descriptiveJSON.put("globalContext", globalContext);
		}
		if (actualContext != null && !actualContext.isEmpty()) {
			descriptiveJSON.put("actualContext", actualContext);
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
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration) {
		if (actualScriptVariablesToSubstituteConfiguration.useGlobalVariables()) {
			this.globalContext.getUsableVariablesInActualContext(
					availableVariablesFromActualContext, actualScriptVariablesToSubstituteConfiguration, this.globalContext);
		}
		this.innerContext.getUsableVariablesInActualContext(
				availableVariablesFromActualContext, actualScriptVariablesToSubstituteConfiguration, this.globalContext);
	}

}
