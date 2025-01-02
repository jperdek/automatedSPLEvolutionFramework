package codeContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codeContext.objects.VariableObject;
import codeContext.processors.ASTContextProcessor;
import codeContext.processors.HierarchyContextProcessor;
import codeContext.processors.export.ExportAggregator;
import codeContext.processors.export.ExportedFunctionContext;
import codeContext.processors.export.ExportedObjectInterface;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.ActualScriptVariablesToSubstituteConfiguration;


/**
 * Representation of the function code context
 * 
 * @author Jakub Perdek
 *
 */
public class FunctionContext extends InnerContext {
	
	/**
	 * Logger to track information about function context
	 */
	private static final Logger logger = LoggerFactory.getLogger(FunctionContext.class);
	
	/**
	 * The name of the function
	 */
	private String functionName;
	
	/**
	 * The function parameters, declared variables are contained into inner context instance
	 */
	private UsedVariables functionParameters;
	
	/**
	 * Return type of the function, which object is returned to extend it further
	 */
	private String returnType = "any";
	

	/**
	 * Creates the function code context
	 * 
	 * @param baseFirstInnerContext - the actually processed/parent inner context
	 * @param originalStartPosition - the start position of the function in the application AST
	 * @param originalEndPosition - the end position of the function in the application AST
	 * @param functionName - the name of the function
	 * @param useTypes - information if the types are used - true if types are used otherwise not
	 * @param returnType - the return type of the function, which object is returned to extend it further
	 * @param isExported - information if whole class is exported, true if whole class is exported otherwise false
	 */
	public FunctionContext(InnerContext baseFirstInnerContext, long originalStartPosition, long originalEndPosition, 
			String functionName, boolean useTypes, String returnType, boolean isExported) {
		super(baseFirstInnerContext, originalStartPosition, originalEndPosition, useTypes, isExported);
		this.functionName = functionName;
		this.functionParameters = new UsedVariables();
		this.returnType = returnType;
	}
	
	/**
	 * Creates the function code context
	 * 
	 * @param baseFirstInnerContext - the actually processed/parent inner context
	 * @param originalStartPosition - the start position of the function in the application AST
	 * @param originalEndPosition - the end position of the function in the application AST
	 * @param upperInnerContextParameters - the parameters accessible from the upper contexts
	 * @param upperInnerContextVariables - the variables declared in upper contexts
	 * @param functionName - the name of the function
	 * @param upperInnerContextMembers - the local function variables
	 * @param useTypes - information if the types are used - true if types are used otherwise not
	 * @param returnType - the return type of the function, which object is returned to extend it further
	 * @param isExported - information if whole class is exported, true if whole class is exported otherwise false
	 */
	public FunctionContext(InnerContext baseFirstInnerContext, long originalStartPosition, long originalEndPosition, 
			UsedVariables upperInnerContextParameters, UsedVariables upperInnerContextVariables, 
			String functionName, UsedVariables upperInnerContextMembers, boolean useTypes, String returnType, boolean isExported) {
		super(baseFirstInnerContext, originalStartPosition, originalEndPosition, upperInnerContextVariables, useTypes, isExported);
		this.functionName = functionName;
		this.functionParameters = new UsedVariables(upperInnerContextMembers.getUsedVariableObjects());
		this.returnType = returnType;
	}
	
	/**
	 * Adds function parameter to function parameters
	 * 
	 * @param partOfVariable - the part AST that contains the variable
	 * @param astRoot - the root of application AST
	 * @param variableName - the name of the variable
	 * @param isDirectlyExported - information if member is directly exported, true if variable is directly exported otherwise false
	 */
	public void addFunctionParameter(JSONObject partOfVariable, JSONObject astRoot, String variableName, boolean isDirectlyExported) {
		this.functionParameters.addVariable(variableName, (long) partOfVariable.get(ASTContextProcessor.SearchPositions.END.label), 
				astRoot, partOfVariable, true, false, isDirectlyExported);
	}
	
	/**
	 * Inserts/adds the parameter to used parameters in this context
	 * 
	 * @param partOfVariable - the part AST that contains the variable/parameter
	 * @param astRoot - the root of application AST
	 * @param variableName - the name of the variable/parameter
	 */
	public void addFunctionParameter(JSONObject partOfVariable, JSONObject astRoot, String variableName) {
		this.functionParameters.addVariable(variableName, (long) partOfVariable.get(ASTContextProcessor.SearchPositions.END.label), 
				astRoot, partOfVariable, false, false, false);
	}
	
	/**
	 * Returns the function name
	 * 
	 * @return the name of the function
	 */
	public String getFunctionName() { return this.functionName; }
	
	/**
	 * Returns context options based only this function context
	 * 
	 * @param baseExecutable - the base executable expression - callable form, if none then callable form will be created
	 * @param actualContext - the actual processed context
	 */
	public ContextOptions insertAllInstantiations(String baseExecutable, InnerContext actualContext) {
		return new ContextOptions(actualContext, this);
	}

	/**
	 * Returns the used parameters representation
	 * 
	 * @return the used parameters representation
	 */
	public UsedVariables getFunctionParameters() { logger.debug("Getting function parameters from" + this.functionName); return this.functionParameters; }

	/**
	 * Returns and creates the descriptive JSON represented output from information about the function context
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @return the descriptive JSON represented output from information about the function context
	 */
	public JSONObject createDescriptiveJSON(GlobalContext globalContext) {
		JSONObject descriptiveJSON = super.createDescriptiveJSON(globalContext);
		descriptiveJSON.put("contextType", "Function");
		descriptiveJSON.put("functionName", this.functionName);
		descriptiveJSON.put("returnType", this.returnType);
		JSONArray membersArray = this.functionParameters.createDescriptiveJSON();
		if (!membersArray.isEmpty()) {
			descriptiveJSON.put("functionMembers", membersArray);
		}
		JSONArray allAvailableCalls = HierarchyContextProcessor.constructAllAvailableCalls(globalContext, this.baseFirstInnerContext, this);
		if (!allAvailableCalls.isEmpty()) {
			descriptiveJSON.put("allAvailableCalls", allAvailableCalls);
		}
		descriptiveJSON.put("callable", this.constructCallableForm());
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
		super.getUsableVariablesInActualContext(availableVariablesFromActualContext, actualScriptVariablesToSubstituteConfiguration, globalContext);
		if (actualScriptVariablesToSubstituteConfiguration.useParameters()) {
			//CHECK POSITION!!!!!
			this.functionParameters.getUsableVariablesInActualContext(
					availableVariablesFromActualContext, actualScriptVariablesToSubstituteConfiguration, globalContext);
		}
	}

	/**
	 * Returns the function parameters that belongs to current function according to currentPosition that is provided as function parameter if are allowed in configuration otherwise empty list
	 * 
	 * @param currentPosition - the position in application AST (script) which is used to decide if given variables are available/are already declared
	 * @param actualScriptVariablesToSubstituteConfiguration
	 * @return all actual (before or at currentPosition that is provided as function parameter) parameters if are allowed in configuration otherwise empty list
	 */
	public List<VariableObject> getFunctionParameters(long currentPosition, 
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration) {
		if (actualScriptVariablesToSubstituteConfiguration.useParameters()) {
			return this.functionParameters.getAllActualVariableObject(currentPosition, actualScriptVariablesToSubstituteConfiguration);
		}
		return new ArrayList<VariableObject>();
	}
	
	@Override
	public String constructCallableForm() {
		return this.functionName + "(" + this.functionParameters.concatenate(this.useTypes) + ")";
	}

	@Override
	public void findContextToExportMapping(List<String> exportNames, ExportAggregator exportAggregator) {
		List<String> exportNamesToRemove = new ArrayList<String>();
		for (String exportName: exportNames) {
			if (this.functionName.equals(exportName)) {
				exportAggregator.associateExport(exportName, this);
				exportNamesToRemove.add(exportName);
			}
		}
		for (String exportName: exportNamesToRemove) {
			exportNames.remove(exportName);
		}
		for (InnerContext childContexts: this.orderedContexts.values()) {
			childContexts.findContextToExportMapping(exportNames, exportAggregator);
		}
		this.usedVariables.findContextToExportMapping(exportNames, exportAggregator);
	}
	
	@Override
	public void markDirrectExportMapping(ExportAggregator exportAggregator, String fileName, InnerContext baseInnerContext) {
		if (this.isExported) {
			exportAggregator.associateExport(this.functionName, this);
			exportAggregator.associateExport(this.functionName, 
					new ExportedFunctionContext(this, fileName, baseInnerContext, this.functionName));
		}
		for (InnerContext childContexts: this.orderedContexts.values()) {
			childContexts.markDirrectExportMapping(exportAggregator, fileName, baseInnerContext);
		}
		this.usedVariables.markDirrectExportMapping(exportAggregator, fileName, baseInnerContext);
	}
	
	@Override
	public String getExportName() { return this.functionName; }
	
	@Override
	public String getExportType() { return this.functionName; }
	
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
		if (this.functionName.equals(innerObjectType)) { return this; }
		return super.getExtendableInnerObjectAccordingToType(innerObjectType);
	}
	
	/**
	 * Harvests variables, parameters including global ones to "actual"/specified position in this inner code context 
	 * 
	 * @param currentPosition - the current position in the application AST to decide about what is "actual"
	 * @param actualScriptVariablesToSubstituteConfiguration
	 * @return the list of actual variables, parameters of previously accessible from this inner context to actual position and global variables
	 */
	public List<VariableObject> getActualVariables(long currentPosition, 
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration) {
		List<VariableObject> actualVariables = super.getVariables(currentPosition);
		List<VariableObject> functionParametersAndVariables = this.functionParameters.getAllActualVariableObject(currentPosition, actualScriptVariablesToSubstituteConfiguration);
		actualVariables.addAll(functionParametersAndVariables);
		logger.debug("----------Printing function parameters and declared variables and data structures: ");
		for (VariableObject vo: functionParametersAndVariables) {
			logger.debug(vo.getExportName() +  " --- called as --> " + vo.getCallableStr());
		}
		logger.debug("-----------------------------------------------------------------------------------");
		return actualVariables;
	}
	
	public void printContextSpecifics() {
		logger.debug("-->===>---> FUNCTION PARAMETERS: ");
		for (String extractedVariableString: this.functionParameters.getUsedVariableObjectsStrings()) {
			logger.debug("-->===> " + extractedVariableString);
		}
		logger.debug("\n");
		logger.debug("RETURN TYPE: " + this.returnType);
		logger.debug("\n");
	}
}
