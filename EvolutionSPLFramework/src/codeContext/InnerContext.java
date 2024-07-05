package codeContext;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import codeContext.objects.VariableObject;
import codeContext.processors.ASTContextProcessor;
import codeContext.processors.export.ExportAggregator;
import codeContext.processors.export.ExportedContextInterface;
import codeContext.processors.export.ExportedInterface;
import codeContext.processors.export.ExportedObjectInterface;
import splEvolutionCore.DebugInformation;


/**
 * Representation of inner context that si organized into hierarchy
 * 
 * @author Jakub Perdek
 *
 */
public class InnerContext implements ExportedContextInterface, ExportedObjectInterface, ExportedInterface {
	
	/**
	 * Used parameters of inner context
	 */
	protected UsedVariables usedParameters;
	
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

		this.usedParameters = new UsedVariables();
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
	 * @param upperInnerContextParameters - the parameters accessible from the upper contexts (are propagated to this context)
	 * @param upperInnerContextVariables - the variables declared in upper contexts (are propagated to this context)
	 * @param useTypes - information if the types are used - true if types are used otherwise not
	 * @param isExported - information if this inner context is exported, true if this inner context is exported otherwise false
	 */
	public InnerContext(InnerContext baseFirstInnerContext, long originalStartPosition, long originalEndPosition,
			UsedVariables upperInnerContextParameters, UsedVariables upperInnerContextVariables, 
			boolean useTypes, boolean isExported) {
		this.useTypes = useTypes;
		this.originalStartPosition = originalStartPosition;
		this.originalEndPosition = originalEndPosition;
		this.orderedContexts = new TreeMap<Long, InnerContext>();
		this.baseFirstInnerContext = baseFirstInnerContext;
		this.isExported = isExported;

		this.usedParameters = new UsedVariables(upperInnerContextParameters.getUsedVariableObjects());
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
	 * Inserts/adds the parameter to used parameters in this context
	 * 
	 * @param partOfVariable - the part AST that contains the variable/parameter
	 * @param astRoot - the root of application AST
	 * @param variableName - the name of the variable/parameter
	 */
	public void addParameter(JSONObject partOfVariable, JSONObject astRoot, String variableName) {
		this.usedParameters.addVariable(variableName, (long) partOfVariable.get(ASTContextProcessor.SearchPositions.END.label), 
				astRoot, partOfVariable, false, false, false);
	}

	/**
	 * Returns the used parameters representation
	 * 
	 * @return the used parameters representation
	 */
	public UsedVariables getUsedParameters() { return this.usedParameters; }
	
	/**
	 * Returns the used variables representation
	 * 
	 * @return the used variables representation
	 */
	public UsedVariables getUsedVariables() { return this.usedVariables; }
	
	/**
	 * Returns all actual parameters according to currentPosition that is provided as function parameter
	 * 
	 * @param currentPosition - the position in application AST (script) which is used to decide if given variables are available/are already declared
	 * @return all actual (before or at currentPosition that is provided as function parameter) parameters
	 */
	public List<VariableObject> getActualParameters(long currentPosition) { 
		return this.usedParameters.getAllActualVariableObject(currentPosition); 
	}
	
	/**
	 * Returns all actually declared variables according to currentPosition that is provided as function parameter
	 * 
	 * @param currentPosition - the position in application AST (script) which is used to decide if given variables are available/are already declared
	 * @return all actually declared (before or at currentPosition that is provided as function parameter) variables
	 */
	public List<VariableObject> getActualVariables(long currentPosition) { 
		return this.usedVariables.getAllActualVariableObject(currentPosition); 
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
	 */
	public JSONObject createDescriptiveJSON(GlobalContext globalContext) {
		JSONObject descriptiveJSON = new JSONObject();
		JSONArray availableVariablesJSON = this.usedVariables.createDescriptiveJSON();
		JSONArray availableParametersJSON = this.usedParameters.createDescriptiveJSON();
		descriptiveJSON.put("p", this.originalEndPosition);
		if (!availableVariablesJSON.isEmpty()) {
			descriptiveJSON.put("availableVariables", availableVariablesJSON);
			//descriptiveJSON.put("p", this.originalStartPosition);
		}
		if (!availableParametersJSON.isEmpty()) {
			descriptiveJSON.put("availableParameters", availableParametersJSON);
			//descriptiveJSON.put("p", this.originalStartPosition);
		}
		return descriptiveJSON;
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
			System.out.println("<---------------------- START ---------------------------->");
			for (Entry<Long, InnerContext> orderedContextEntry: this.orderedContexts.entrySet()) {
				position = orderedContextEntry.getKey();
				innerContext = orderedContextEntry.getValue();
				System.out.println(position + " --> " + innerContext.createDescriptiveJSON(globalContext).toString());
				parentContexts.add(innerContext);
			}
			System.out.println("<---------------------- END ---------------------------->");
			
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
}
