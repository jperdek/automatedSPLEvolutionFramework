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
import codeContext.processors.export.ExportedClassContext;
import codeContext.processors.export.ExportedObjectInterface;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.ActualScriptVariablesToSubstituteConfiguration;


/**
 * Representation of the class code context
 * 
 * @author Jakub Perdek
 *
 */
public class ClassContext extends InnerContext {
	
	/**
	 * Logger to track information about class context
	 */
	private static final Logger logger = LoggerFactory.getLogger(ClassContext.class);
	
	/**
	 * The name of the class
	 */
	private String className;
	
	/**
	 * The class variables - class members
	 */
	private UsedVariables members;
	
	/**
	 * The constructor parameters, declared variables are contained into inner context instance
	 */
	private UsedVariables constructorParameters;
	
	/**
	 * Creates the class code context
	 * 
	 * @param baseFirstInnerContext - the actually processed/parent inner context
	 * @param originalStartPosition - the start position of the class in the application AST
	 * @param originalEndPosition - the end position of the class in the application AST
	 * @param className - the name of the class
	 * @param useTypes - information if the types are used - true if types are used otherwise not
	 * @param isExported - information if whole class is exported, true if whole class is exported otherwise false
	 */
	public ClassContext(InnerContext baseFirstInnerContext, long originalStartPosition, long originalEndPosition, 
			String className, boolean useTypes, boolean isExported) {
		super(baseFirstInnerContext, originalStartPosition, originalEndPosition, useTypes, isExported);
		this.className = className;
		this.members = new UsedVariables();
		this.constructorParameters = new UsedVariables();
	}
	
	/**
	 * Creates the class code context
	 * 
	 * @param baseFirstInnerContext - the actually processed/parent inner context
	 * @param originalStartPosition - the start position of the class in the application AST
	 * @param originalEndPosition - the end position of the class in the application AST
	 * @param upperInnerContextParameters - the parameters accessible from the upper contexts
	 * @param upperInnerContextVariables - the variables declared in upper contexts
	 * @param className - the name of the class
	 * @param upperInnerContextMembers - the class variables - members
	 * @param useTypes - information if the types are used - true if types are used otherwise not
	 * @param isExported - information if whole class is exported, true if whole class is exported otherwise false
	 */
	public ClassContext(InnerContext baseFirstInnerContext, long originalStartPosition, long originalEndPosition, 
			UsedVariables upperInnerContextParameters, UsedVariables upperInnerContextVariables, 
			String className, UsedVariables upperInnerContextMembers, boolean useTypes, boolean isExported) {
		super(baseFirstInnerContext, originalStartPosition, originalEndPosition, upperInnerContextVariables, useTypes, isExported);
		this.className = className;
		this.members = new UsedVariables(upperInnerContextMembers.getUsedVariableObjects());
		this.constructorParameters = new UsedVariables(upperInnerContextParameters.getUsedVariableObjects());
	}
	
	/**
	 * Inserts/adds the parameter to used parameters in this context
	 * 
	 * @param partOfVariable - the part AST that contains the variable/parameter
	 * @param astRoot - the root of application AST
	 * @param variableName - the name of the variable/parameter
	 */
	public void addConstructorParameter(JSONObject partOfVariable, JSONObject astRoot, String variableName) {
		this.constructorParameters.addVariable(variableName, (long) partOfVariable.get(ASTContextProcessor.SearchPositions.END.label), 
				astRoot, partOfVariable, false, false, false);
	}
	
	/**
	 * Inserts all instantiations of calls to the actual state of the inner context
	 * 
	 * @param baseExecutable - the base executable expression - callable form, if none then callable form will be created
	 * @param actualContext - the actual processed context
	 */
	public ContextOptions insertAllInstantiations(String baseExecutable, InnerContext actualContext) {
		ContextOptions contextOptions = new ContextOptions();
		if (baseExecutable == null || baseExecutable.equals("")) {
			baseExecutable = this.constructCallableForm();
		}
		// set new context option for classes - only two depths are allowed 1. class - 2. class members
		// CLASS - CLASS_FUNCTIONS
		this.insertAllNestedFunctionContexts(baseExecutable, contextOptions, actualContext);
		// extend context about variables
		// CLASS - CLASS_VARIABLES
		contextOptions.extendAndAddExecutableFormAll(baseExecutable, ".", this.usedVariables.getUsedVariableObjectsStrings());
		// CLASS NEEDS TO LOAD ALL MEMBERS FIRST TO PROCESS IT AS WHOLE
		if (!baseExecutable.equals("")) { contextOptions.addExecutableForm(baseExecutable); }
		return contextOptions;
	}

	/**
	 * Inserts all nested function calls to the inner context
	 * 
	 * @param classExecutable - instantiation of the class, reference to the class object of this representation
	 * @param contextOptions - the context options to manage context
	 * @param actualContext - the actual processed context
	 */
	private void insertAllNestedFunctionContexts(String classExecutable, ContextOptions contextOptions, InnerContext actualContext) {
		FunctionContext functionContext;
		String functionCallableForm;
		for (InnerContext innerContext: this.orderedContexts.values()) {
			if (innerContext == actualContext) {
				contextOptions.setAsFound();
				//return;
			}
			if (innerContext instanceof FunctionContext) {
				functionContext = (FunctionContext) innerContext;
				functionCallableForm = functionContext.constructCallableForm();
				if (functionCallableForm.startsWith("constructor(")) { continue; }
				contextOptions.extendAndAddExecutableForm(classExecutable, ".", functionContext.constructCallableForm());
			} else if (innerContext instanceof ClassContext) {
				logger.warn("Classes cannot be nested!!! Not implemented!");
			} else {
				logger.warn("New context! Not supported!!!");
			}
		}
	}
	
	/**
	 * Returns the name of the class
	 *  
	 * @return the name of the class
	 */
	public String getClassName() { return this.className; }
	
	/**
	 * Returns the class constructor parameters representation
	 * 
	 * @return  the class constructor parameters representation
	 */
	public UsedVariables getConstructorParameters() { return this.constructorParameters; }
	
	/**
	 * Adds class member with priority to class variables/members
	 * 
	 * @param partOfVariable - the part AST that contains the variable
	 * @param astRoot - the root of application AST
	 * @param variableName - the name of the variable
	 * @param isDirectlyExported - information if member is directly exported, true if member is directly exported otherwise false
	 */
	public void addMemberWithPrioritization(JSONObject partOfVariable, JSONObject astRoot, String variableName, boolean isDirectlyExported) {
		this.members.addVariable(variableName, this.originalStartPosition + 1, astRoot, 
				partOfVariable, true, false, isDirectlyExported);
	}
	
	/**
	 * Adds class member to class variables/members
	 * 
	 * @param partOfVariable - the part AST that contains the variable
	 * @param astRoot - the root of application AST
	 * @param variableName - the name of the variable
	 * @param isDirectlyExported - information if member is directly exported, true if member is directly exported otherwise false
	 */
	public void addMember(JSONObject partOfVariable, JSONObject astRoot, String variableName, boolean isDirectlyExported) {
		this.members.addVariable(variableName, (long) partOfVariable.get(ASTContextProcessor.SearchPositions.END.label), 
				astRoot, partOfVariable, true, false, isDirectlyExported);
	}
	
	/**
	 * Returns the class constructor parameters that belongs to current class according to currentPosition that is provided as function parameter if are allowed in configuration otherwise empty list
	 * 
	 * @param currentPosition - the position in application AST (script) which is used to decide if given variables are available/are already declared
	 * @param actualScriptVariablesToSubstituteConfiguration
	 * @return all actual (before or at currentPosition that is provided as function parameter) parameters if are allowed in configuration otherwise empty list
	 */
	public List<VariableObject> getConstructorParameters(long currentPosition, 
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration) {
		if (actualScriptVariablesToSubstituteConfiguration.useParameters()) {
			return this.constructorParameters.getAllActualVariableObject(currentPosition, actualScriptVariablesToSubstituteConfiguration);
		}
		return new ArrayList<VariableObject>();
	}
	
	/**
	 * Returns and creates the descriptive JSON represented output from information about the class context
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @return the descriptive JSON represented output from information about the class context
	 */
	public JSONObject createDescriptiveJSON(GlobalContext globalContext) {
		JSONObject descriptiveJSON = super.createDescriptiveJSON(globalContext);
		descriptiveJSON.put("contextType", "Class");
		descriptiveJSON.put("className", this.className);
		JSONArray membersArray = this.members.createDescriptiveJSON();
		if (!membersArray.isEmpty()) {
			descriptiveJSON.put("classMembers", membersArray);
		}
		JSONArray allAvailableCalls = HierarchyContextProcessor.constructAllAvailableCalls(globalContext, this.baseFirstInnerContext, this);
		if (!allAvailableCalls.isEmpty()) {
			descriptiveJSON.put("allAvailableCalls", allAvailableCalls);
		}
		descriptiveJSON.put("functionality", this.getAllClassFunctionsWithoutConstructorInJSON());
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
			this.members.getUsableVariablesInActualContext(
					availableVariablesFromActualContext, actualScriptVariablesToSubstituteConfiguration, globalContext);
		}
	}
	
	@Override
	public String constructCallableForm() {
		FunctionContext constructor = this.findFunctionContext("constructor");
		if (constructor == null) { return ""; }
		return "new " + this.className + "(" + this.constructorParameters.concatenate(this.useTypes) + ")";
	}
	
	/**
	 * Returns the list of all callable class functions without constructor
	 * 
	 * @return the list of all callable class functions without constructor
	 */
	public List<String> getAllClassFunctionsWithoutConstructor() {
		String functionText;
		List<String> functionsWithoutConstructor = new ArrayList<String>();
		for (FunctionContext functionContext: this.getAllFunctions()) {
			if (!functionContext.getFunctionName().equals("constructor")) {
				functionText = functionContext.constructCallableForm(); //instantiates the callable function call
				functionsWithoutConstructor.add(functionText);
			}
		}
		return functionsWithoutConstructor;
	}
	
	/**
	 * Returns the JSON array of all callable class functions without constructor
	 * 
	 * @return the JSON array of all callable class functions without constructor
	 */
	public JSONArray getAllClassFunctionsWithoutConstructorInJSON() {
		String functionText;
		JSONArray functionsWithoutConstructor = new JSONArray();
		for (FunctionContext functionContext: this.getAllFunctions()) {
			if (!functionContext.getFunctionName().equals("constructor")) {
				functionText = functionContext.constructCallableForm();
				functionsWithoutConstructor.add(functionText);
			}
		}
		return functionsWithoutConstructor;
	}

	@Override
	public void findContextToExportMapping(List<String> exportNames, ExportAggregator exportAggregator) {
		for (String exportName: exportNames) {
			if (this.className.equals(exportName)) {
				exportAggregator.associateExport(exportName, this);
				exportNames.remove(exportName);
			}
		}
		for (InnerContext childContexts: this.orderedContexts.values()) {
			childContexts.findContextToExportMapping(exportNames, exportAggregator);
		}
		this.usedVariables.findContextToExportMapping(exportNames, exportAggregator);
	}
	
	@Override
	public void markDirrectExportMapping(ExportAggregator exportAggregator, String fileName, InnerContext baseContext) {
		if (this.isExported) {
			exportAggregator.associateExport(this.className, this);
			exportAggregator.associateExport(this.className, new ExportedClassContext(this, fileName, this.baseFirstInnerContext, this.className));
		}
		for (InnerContext childContexts: this.orderedContexts.values()) {
			childContexts.markDirrectExportMapping(exportAggregator, fileName, this.baseFirstInnerContext);
		}
		this.usedVariables.markDirrectExportMapping(exportAggregator, fileName, this.baseFirstInnerContext);
	}

	@Override
	public String getExportName() { return this.className; }
	
	@Override
	public String getExportType() { return this.className; }
	
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
		if (this.className.equals(innerObjectType)) { return this; }
		return super.getExtendableInnerObjectAccordingToType(innerObjectType);
	}
	
	/**
	 * Harvests variables, parameters including global ones to "actual"/specified position in this inner code context 
	 * 
	 * @param currentPosition - the current position in the application AST to decide about what is "actual"
	 * @param actualScriptVariablesToSubstituteConfiguration
	 * @return the list of actual variables, parameters of previously accessible from this inner context to actual position and global variables
	 */
	public List<VariableObject> getVariables(long currentPosition, 
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration) {
		List<VariableObject> actualVariables = super.getVariables(currentPosition);
		actualVariables.addAll(this.members.getAllActualVariableObject(currentPosition, actualScriptVariablesToSubstituteConfiguration));
		return actualVariables;
	}
	
	public List<VariableObject> getClassVariables(long currentPosition, 
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration) {
		return this.usedVariables.getAllActualVariableObject(currentPosition, actualScriptVariablesToSubstituteConfiguration);
	}
	
	public void printContextSpecifics() {
		logger.debug("-->===>---> CLASS VARIABLES: ");
		for (String extractedVariableString: this.members.getUsedVariableObjectsStrings()) {
			logger.debug("-->===> " + extractedVariableString);
		}
		logger.debug("\n");
	}
}
