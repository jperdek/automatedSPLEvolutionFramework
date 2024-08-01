package codeContext;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.Map.Entry;
import codeContext.objects.CodeContextObject;
import codeContext.objects.ImportObject;
import codeContext.objects.SortByCodeContext;
import codeContext.objects.VariableObject;
import codeContext.processors.export.ExportAggregator;
import codeContext.processors.export.ExportedContextInterface;
import codeContext.processors.export.ExportedInterface;
import codeContext.processors.export.ExportedObjectInterface;
import divisioner.VariationPointDivisionConfiguration;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.ActualScriptVariablesToSubstituteConfiguration;
import splEvolutionCore.DebugInformation;

/**
 * Used variables with given code context
 * 
 * @author Jakub Perdek
 *
 */
public class UsedVariables implements ExportedContextInterface, ExportedInterface {
	
	/**
	 * The list of used variables by given code context
	 */
	private List<VariableObject> usedVariableObjects;
	
	/**
	 * Set to filter duplicate variable names
	 */
	private Set<String> duplicatesFilter;

	
	/**
	 * Creates the used variables instance
	 */
	public UsedVariables() {
		this.usedVariableObjects = new ArrayList<VariableObject>();
		this.duplicatesFilter = new HashSet<String>();
	}
	
	/**
	 * Creates the used variables instance and updates variables according upper ones
	 * 
	 * @param upperContextVariables - the used variables from upper context
	 */
	public UsedVariables(List<VariableObject> upperContextVariables) {
		this.usedVariableObjects = new ArrayList<VariableObject>(upperContextVariables);

		this.duplicatesFilter = new HashSet<String>();
		for (VariableObject vo: upperContextVariables) {
			this.duplicatesFilter.add(vo.getVariableName() + vo.getVariableType() + vo.getPosition());
		}
	}
	
	/**
	 * Returns usable variables with their type in actual context
	 * 
	 * @param availableVariablesFromActualContext - usable variables to be substituted from actual context
	 * @param actualScriptVariablesToSubstituteConfiguration - configuration for getting actually available functionality that can be substituted in code
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 */
	public void getUsableVariablesInActualContext(
			Set<Entry<String, String>> availableVariablesFromActualContext,
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration, GlobalContext globalContext) {
		for (VariableObject processedObject: this.usedVariableObjects) {
			availableVariablesFromActualContext.add(
					new AbstractMap.SimpleEntry<String, String>(processedObject.getVariableName(), processedObject.getVariableType()));
		}
	}
	
	/**
	 * Returns the list of used variable names by given code context
	 * 
	 * @return the list of used variable names by given code context
	 */
	public List<String> getUsedVariableObjectsStrings() { 
		List<String> variableStrings = new ArrayList<String>();
		for (VariableObject processedObject: this.usedVariableObjects) {
			variableStrings.add(processedObject.getVariableName());
		}
		return variableStrings;
	}
	
	/**
	 * Returns the list of used variables by given code context
	 * 
	 * @return the list of used variables by given code context
	 */
	public List<VariableObject> getUsedVariableObjects() { return this.usedVariableObjects; }
	
	/**
	 * Analyzes the type of the variable in TypeScript
	 * -type any or not found type result in Warning message - use types to make SPL evolution more effective!!!
	 * 
	 * @param astRoot - the root of application AST
	 * @param astVariable - the processed variable in AST form
	 * @param isDeclaration - information if variable is provided inform of declaration, true if variable is provided inform of declaration otherwise false
	 * @return the string representation of found type
	 */
	public static String analyzeType(JSONObject astRoot, JSONObject astVariable, boolean isDeclaration) {
		JSONObject typeObject, initializerJSONObject, typeNameObject;
		Long startInitializerPosition, endInitializerPosition;
		Pattern pattern; Matcher matcher;
		String typeName;
		long kind;
		if (astVariable.containsKey("type")) {
			typeObject = (JSONObject) astVariable.get("type");
			if (!typeObject.containsKey("typeName")) {
				kind = (long) typeObject.get("kind");
					   if (kind == 152) { return "string";
				} else if (kind == 148) { return "number";
				} else if (kind == 110) { return "boolean";
				} else if (kind == 157) { return "unknown";
				} else if (kind == 131) { return "any";
				} else if(kind == 114)  { return "void"; }
				return "unknown";
			}
			typeName = (String) ((JSONObject) typeObject.get("typeName")).get("escapedText");
			if (Character.isUpperCase(typeName.charAt(0))) { return typeName; } //"class " 
			return "function " + typeName;
		}	
		
		String wholeCodeText = (String) astRoot.get("text");
		String valueCharacteristics;
		if (astVariable.containsKey("initializer")) {
			if (DebugInformation.SHOW_MISSING_EVOLUTION_ENHANCEMENTS) {
				System.out.println("Automatically guessing type. Can be error prone!");
			}
			initializerJSONObject = (JSONObject) astVariable.get("initializer");
			if (initializerJSONObject.containsKey("arguments")) {	
				typeNameObject = (JSONObject) initializerJSONObject.get("expression");
				if (typeNameObject.containsKey("name")) {
					typeName = (String) ((JSONObject) typeNameObject.get("name")).get("escapedText");
				} else {
					typeName = (String) typeNameObject.get("escapedText");
				}
				if (Character.isUpperCase(typeName.charAt(0))) { return typeName; } //"class "
				return typeName; //"function "
			}
			
			startInitializerPosition = (long) initializerJSONObject.get("pos");
			endInitializerPosition = (long) initializerJSONObject.get("end");
			valueCharacteristics = wholeCodeText.substring(startInitializerPosition.intValue(), endInitializerPosition.intValue()).strip();
			if (valueCharacteristics.startsWith("new")) {
				return "class";
			}
			
			if (valueCharacteristics.startsWith("\"") || valueCharacteristics.startsWith("'")) {
				return "string";
			}
			pattern = Pattern.compile("[A-Za-z]+[A-Za-z]+", Pattern.CASE_INSENSITIVE);
		    matcher = pattern.matcher(valueCharacteristics);
		    if(matcher.find()) { return "any"; }

			try {
				double loadedValue = Double.parseDouble(valueCharacteristics);
				return "number";
			} catch(Exception e) { 
				if (DebugInformation.SHOW_MISSING_EVOLUTION_ENHANCEMENTS) {
					System.out.println("Unparsed value: " + valueCharacteristics);
				}
				if (DebugInformation.SHOW_POLLUTING_INFORMATION) { e.printStackTrace(); } 
			}
			
			return "any";
		} 
		
		if (isDeclaration) { return "undefined"; }
		return "any";
	}
	
	/**
	 * Adds variable to used variables and sorts results by context position
	 * 
	 * @param variableName - the name of processed variable
	 * @param position - the position of variable in application AST (script)
	 * @param astRoot - the root of application AST
	 * @param variableAst - the processed variable in AST form
	 * @param isDeclaration - information if variable is provided inform of declaration, true if variable is provided inform of declaration otherwise false
	 * @param isboolean - if variable has type of boolean
	 * @param isExported - information if whole class is exported, true if whole class is exported otherwise false
	 */
	public void addVariable(String variableName, long position, JSONObject astRoot, JSONObject variableAst, 
			boolean isDeclaration, boolean isboolean, boolean isExported) {
		String variableType = UsedVariables.analyzeType(astRoot, variableAst, isDeclaration);
		String identifier = variableName + variableType + position;
		if (!this.duplicatesFilter.contains(identifier)) {
			this.duplicatesFilter.add(identifier);
			VariableObject newVariableObject = new VariableObject(variableName, variableType, position, isboolean, isExported);
			this.usedVariableObjects.add(newVariableObject);
			Collections.sort(this.usedVariableObjects, new SortByCodeContext());
		}
	}

	/**
	 * Returns all actually declared variables according to currentPosition that is provided as function parameter
	 * 
	 * @param currentPosition - the position in application ASt (script) which is used to decide if given variables are available/are already declared
	 * @return all actually declared (before or at currentPosition that is provided as function parameter) variables
	 */
	public List<VariableObject> getAllActualVariableObject(long currentPosition) {
		if (DebugInformation.SHOW_POLLUTING_INFORMATION) {
			System.out.println("Current position: " + currentPosition);
			for (VariableObject vo: this.usedVariableObjects) {
				System.out.println(vo.getExportName() + "  " + vo.getCallableStr());
			}
		}
		CodeContextObject[] cco = new CodeContextObject[this.usedVariableObjects.size()];
		cco = (CodeContextObject[]) this.usedVariableObjects.toArray(cco);
		int finalLength = Arrays.binarySearch(cco, new CodeContextObject(currentPosition));
		if (finalLength == -1) { finalLength = this.usedVariableObjects.size(); }
		if (Math.abs(finalLength) >= this.usedVariableObjects.size() - 1 && this.usedVariableObjects.size() != 0) { finalLength = Math.abs(finalLength) - 1; }
		return new ArrayList<VariableObject>(this.usedVariableObjects.subList(0, Math.abs(finalLength)));
	}

	/**
	 * Returns all actually declared variables according to currentPosition that is provided as function parameter
	 * 
	 * @param currentPosition - the position in application ASt (script) which is used to decide if given variables are available/are already declared
	 * @param actualScriptVariablesToSubstituteConfiguration
	 * @return all actually declared (before or at currentPosition that is provided as function parameter) variables
	 */
	public List<VariableObject> getAllActualVariableObject(long currentPosition,
			ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration) {
		CodeContextObject[] cco = new CodeContextObject[this.usedVariableObjects.size()];
		cco = (CodeContextObject[]) this.usedVariableObjects.toArray(cco);
		int finalLength = Arrays.binarySearch(cco, new CodeContextObject(currentPosition));
		if (finalLength == -1) { finalLength = this.usedVariableObjects.size(); }
		if (Math.abs(finalLength) >= this.usedVariableObjects.size() - 1 && this.usedVariableObjects.size() != 0) { finalLength = Math.abs(finalLength) - 1; }
		return new ArrayList<VariableObject>(this.usedVariableObjects.subList(0, Math.abs(finalLength)));
	}
	
	/**
	 * Returns and creates the descriptive JSON represented output from information about this used variables
	 * 
	 * @return the descriptive JSON represented output from information about used variables
	 */
	public JSONArray createDescriptiveJSON() {
		JSONArray descriptiveJSONArray = new JSONArray();
		JSONObject storedObject;
		for (VariableObject variableObject: this.usedVariableObjects) {
			storedObject = variableObject.createDescriptiveJSON();
			if (storedObject != null) {
				descriptiveJSONArray.add(storedObject);
			}
		}
		return descriptiveJSONArray;
	}
	
	/**
	 * Concatenates parameters into function/constructor call with optional creation of parameterized form to create templates
	 * -parameterized form is created if VariationPointDivisionConfiguration.USE_PARAMETERIZED_FORM is set to true otherwise not
	 * 
	 * @param useTypes - information if the types are used - true if types are used otherwise not
	 * @return concatenated parameters into function/constructor call with optional creation of parameterized form to create templates
	 */
	public String concatenate(boolean useTypes) {
		String concatenatedParameters = "", variable;
		for (VariableObject vo: this.usedVariableObjects) {
			variable = vo.getStringRepresentation(useTypes);
			if (concatenatedParameters.equals("")) {
				if (VariationPointDivisionConfiguration.USE_PARAMETERIZED_FORM) {
					variable = VariationPointDivisionConfiguration.PARAMETERIZED_FORM_START + variable;
				}
				concatenatedParameters = variable;
				if(useTypes) {
					//concatenatedParameters = concatenatedParameters + VariationPointDivisionConfiguration.PARAMETERIZED_FORM_DELIMITER + vo.getVariableType();
				}
				if (VariationPointDivisionConfiguration.USE_PARAMETERIZED_FORM) {
					concatenatedParameters = concatenatedParameters + VariationPointDivisionConfiguration.PARAMETERIZED_FORM_END;
				} 
				
			} else {
				if (VariationPointDivisionConfiguration.USE_PARAMETERIZED_FORM) {
					variable =  VariationPointDivisionConfiguration.PARAMETERIZED_FORM_START + variable;
				} 
				concatenatedParameters = concatenatedParameters + ", " + variable;
				if(useTypes) {
					//concatenatedParameters = concatenatedParameters + VariationPointDivisionConfiguration.PARAMETERIZED_FORM_DELIMITER + vo.getVariableType();
				}
				if (VariationPointDivisionConfiguration.USE_PARAMETERIZED_FORM) {
					concatenatedParameters = concatenatedParameters + VariationPointDivisionConfiguration.PARAMETERIZED_FORM_END;
				} 
			}
		}
		return concatenatedParameters;
	}

	@Override
	public ExportedObjectInterface findDefaultExport(Long initialPosition, Long terminatingPosition) {
		ExportedObjectInterface defaultExport = null;
		for (VariableObject variableObject: usedVariableObjects) {
			defaultExport = variableObject.findDefaultExport(initialPosition, terminatingPosition);
			if (defaultExport != null) { return defaultExport; }
		}
		return defaultExport;
	}

	@Override
	public void findContextToExportMapping(List<String> exportNames, ExportAggregator exportAggregator) {
		for (VariableObject variableObject: usedVariableObjects) {
			variableObject.findContextToExportMapping(exportNames, exportAggregator);
		}
	}

	@Override
	public void markDirrectExportMapping(ExportAggregator exportAggregator, String fileName, InnerContext baseInnerContext) {
		for(VariableObject variableObject: usedVariableObjects) {
			variableObject.markDirrectExportMapping(exportAggregator, fileName, baseInnerContext);
		}
	}

	@Override
	public ExportedObjectInterface getExtendableInnerObjectAccordingToType(String innerObjectType) {
		ExportedObjectInterface objectWithAssociatedType = null;
		for (VariableObject variableObject: usedVariableObjects) {
			objectWithAssociatedType = variableObject.getExtendableInnerObjectAccordingToType(innerObjectType);
			if (objectWithAssociatedType != null) { return objectWithAssociatedType; }
		}
		return objectWithAssociatedType;
	}
	
	@Override
	public String getCallableStr() { return this.createDescriptiveJSON().toJSONString(); }
	
	public String getIdentificationAST() { return this.createDescriptiveJSON().toJSONString(); };
}
