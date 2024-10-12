package codeContext;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import codeContext.objects.VariableObject;
import codeContext.processors.export.ExportAggregator;
import codeContext.processors.export.ExportedContextInterface;
import codeContext.processors.export.ExportedObjectInterface;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.ActualScriptVariablesToSubstituteConfiguration;


/**
 * Representation of global context
 * 
 * @author Jakub Perdek
 *
 */
public class GlobalContext implements ExportedContextInterface {
	
	/**
	 * The representation of global imports
	 */
	private GlobalImports globalImports;
	
	/**
	 * The representation of global variables
	 */
	private UsedVariables globalVariables;
	

	/**
	 * Creates/instantiates the global context
	 */
	public GlobalContext() {
		this.globalImports = new GlobalImports();
		this.globalVariables = new UsedVariables();
	}
	
	/**
	 * Returns the representation of global imports
	 * 
	 * @return the representation of global imports
	 */
	public GlobalImports getGlobalImports() { return this.globalImports; }
	
	/**
	 * Returns the representation of global variables
	 * 
	 * @return the representation of global variables
	 */
	public UsedVariables getGlobalVariables() { return this.globalVariables; }
	
	/**
	 * Returns all actually declared global variables according to currentPosition that is provided as function parameter
	 * 
	 * @param currentPosition - the position in application ASt (script) which is used to decide if given variables are available/are already declared
	 * @return all actually declared (before or at currentPosition that is provided as function parameter) global variables
	 */
	public List<VariableObject> getActualGlobalVariables(long currentPosition) {
		return this.globalVariables.getAllActualVariableObject(currentPosition);
	}
	
	/**
	 * Inserts/adds the global variable to global variables
	 * 
	 * @param partOfVariable - the part AST that contains the variable
	 * @param astRoot - the root of application AST
	 * @param variableName - the name of the variable
	 * @param isDirectlyExported - information if member is directly exported, true if variable is directly exported otherwise false
	 */
	public void addGlobalVariable(JSONObject partOfVariable, JSONObject astRoot, String variableName, boolean isDirectlyExported) {
		this.globalVariables.addVariable(variableName, (long) partOfVariable.get("end"), astRoot, 
				partOfVariable, true, true, isDirectlyExported);
	}

	/**
	 * Inserts all instantiations of calls to the actual state of the inner context
	 * -on the actual context all global variables are tried to be extended on all possible executable forms)
	 * 
	 * @param baseExecutable - the base executable expression - callable form, if none then callable form will be created
	 * @param actualContext - the actual processed context (inner)
	 * @return the new context option object (default)
	 */
	public ContextOptions insertAllInstantiations(String baseExecutable, InnerContext actualContext) {
		ContextOptions contextOptions = new ContextOptions(); //default context option
		String extensionOperator = ".";
		if (baseExecutable == null || baseExecutable.equals("")) {
			extensionOperator = "";
		} 
		//extends all global variables
		contextOptions.extendAndAddExecutableFormAll(baseExecutable, extensionOperator, this.globalVariables.getUsedVariableObjectsStrings());
		return contextOptions;
	}
	
	/**
	 * Loads global imports from AST import part
	 * 
	 * @param partOfImport - the AST import part
	 */
	public void addGlobalImport(JSONObject partOfImport) {
		String importName;
		String importPath = (String) ((JSONObject) partOfImport.get("moduleSpecifier")).get("text");
		JSONArray importNames = (JSONArray) ((JSONObject) ((JSONObject) partOfImport.get("importClause")).get("namedBindings")).get("elements");
		for (Object importNameObject: importNames) {
			importName = (String) ((JSONObject) ((JSONObject) importNameObject).get("name")).get("text");
			this.globalImports.addGlobalImport(importName, importPath, (Integer) partOfImport.get("end"));
		}
	}
	
	/**
	 * Returns and creates the descriptive JSON represented output from information about this global context
	 * 
	 * @return the descriptive JSON represented output from information about this global context
	 */
	public JSONObject createDescriptiveJSON() {
		JSONObject descriptiveJSON = new JSONObject();
		JSONArray globalVariables = this.globalVariables.createDescriptiveJSON();
		if (!globalVariables.isEmpty()) {
			descriptiveJSON.put("globalVariables", globalVariables);
		}
		return descriptiveJSON;
	}

	/**
	 * Returns usable variables in actual context
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param variables that can be used in actual context
	 */
	public void getUsableVariablesInActualContext(
			Set<Entry<String, String>> availableVariablesFromActualContext,
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration, GlobalContext globalContext) {
		this.globalVariables.getUsableVariablesInActualContext(availableVariablesFromActualContext, actualScriptVariablesToSubstituteConfiguration, globalContext);
	}
	
	@Override
	public ExportedObjectInterface findDefaultExport(Long startPosition, Long endPosition) {
		return this.globalVariables.findDefaultExport(startPosition, endPosition);
	}

	@Override
	public void findContextToExportMapping(List<String> exportNames, ExportAggregator exportAggregator) {
		this.globalVariables.findContextToExportMapping(exportNames, exportAggregator);
	}

	@Override
	public void markDirrectExportMapping(ExportAggregator exportAggregator, String fileName, InnerContext baseInnerContext) {
		this.globalVariables.markDirrectExportMapping(exportAggregator, fileName, baseInnerContext);
	}

	@Override
	public ExportedObjectInterface getExtendableInnerObjectAccordingToType(String innerObjectType) {
		return null; //only complex data structures should be associated to type - thus null
	}

	@Override
	public String getCallableStr() { return this.createDescriptiveJSON().toJSONString(); }
	
	public String getIdentificationAST() { return this.createDescriptiveJSON().toJSONString(); };
}
