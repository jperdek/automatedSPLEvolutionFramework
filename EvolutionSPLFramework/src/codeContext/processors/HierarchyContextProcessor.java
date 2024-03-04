package codeContext.processors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import codeContext.ClassContext;
import codeContext.ContextOptions;
import codeContext.FunctionContext;
import codeContext.GlobalContext;
import codeContext.InnerContext;
import codeContext.UsedVariables;
import codeContext.processors.export.ExportsProcessor;


/**
 * Functionality to create hierarchies by introducing the inner contexts as children of the previous contexts
 * 
 * @author Jakub Perdek
 *
 */
public class HierarchyContextProcessor {

	/**
	 * Creates context according to found complex code entity
	 * 
	 * @param astPart - actually/currently processed part of AST
	 * @param defaultInnerContext - the default context as the root of inner context that is propagated to each new context
	 * @param useTypes - information if the types are used - true if types are used otherwise not
	 * @return the new created/introduced inner context if new complex code structure is found otherwise null
	 */
	public static InnerContext createInnerContextIfNecessary(JSONObject astRoot, JSONObject astPart, InnerContext defaultInnerContext, boolean useTypes) {
		String className, functionName;
		boolean isDirectlyExported;
		if (astPart.containsKey("members")) {
			className = (String) ((JSONObject) astPart.get("name")).get("escapedText");
			isDirectlyExported = ExportsProcessor.isConstructMarkedAsDirectExport(astPart);
			return new ClassContext(defaultInnerContext, (long) astPart.get("pos"), (long) astPart.get(ASTContextProcessor.SEARCH_POSITION), 
					className, useTypes, isDirectlyExported);
		} else if (astPart.containsKey("parameters")) {
			if (astPart.containsKey("name")) {
				functionName = (String) ((JSONObject) astPart.get("name")).get("escapedText");
			} else {
				functionName = "constructor";
			}
			isDirectlyExported = ExportsProcessor.isConstructMarkedAsDirectExport(astPart);
			String functionReturnType = UsedVariables.analyzeType(astRoot, astPart, false);
			return new FunctionContext(defaultInnerContext, (long) astPart.get("pos"), (long) astPart.get(ASTContextProcessor.SEARCH_POSITION),
					functionName, useTypes, functionReturnType, isDirectlyExported);
		}
		return null;
	}
	
	/**
	 * Constructs all available calls available to the occurrence of the certain context
	 * - the design of recursively evaluated version of constructAllAvailableCalls, but unusable in JavaScript/TypeScript with two layer references
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param baseFirstInnerContext - the default context as the root of inner context that is propagated to each new context 
	 * @param actualContext - the actually processed code context
	 * @param overallContextOptions - code options to aggregate and extends possible calls of available functionality up to given context 
	 */
	@SuppressWarnings("Design version only for languages based on hierarchies") 
	private static void constructAllAvailableCallsRecursive(GlobalContext globalContext, InnerContext baseFirstInnerContext, InnerContext actualContext, ContextOptions overallContextOptions) {
		ContextOptions extractedContextOption;
		for (InnerContext childFirstLayerContext: baseFirstInnerContext.getAllInnerChildContext()) {
			if (childFirstLayerContext == actualContext && childFirstLayerContext.getActualEndPosition() > actualContext.getActualEndPosition()) {
				return;
			}
			HierarchyContextProcessor.constructAllAvailableCallsRecursive(globalContext, childFirstLayerContext, actualContext, overallContextOptions);
			extractedContextOption = childFirstLayerContext.insertAllInstantiations("", actualContext);
			overallContextOptions.mergeBase(extractedContextOption);
		}
		if (baseFirstInnerContext == actualContext) {
			extractedContextOption =  actualContext.insertAllInstantiations("", actualContext);
			overallContextOptions.mergeBase(extractedContextOption);
			return;
		}
	}
	
	/**
	 * Constructs all available calls available to the occurrence of the certain context
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param baseFirstInnerContext - the default context as the root of inner context that is propagated to each new context
	 * @param actualContext - the actually processed code context
	 * @param overallContextOptions - code options to aggregate and extends possible calls of available functionality up to given context 
	 */
	public static JSONArray constructAllAvailableCalls(GlobalContext globalContext, InnerContext baseFirstInnerContext, InnerContext actualContext) {
		ContextOptions extractedContextOption;
		ContextOptions overallContextOptions = new ContextOptions(); //globalContext.insertAllInstantiations("", actualContext);
		for (InnerContext childFirstLayerContext: baseFirstInnerContext.getAllInnerChildContext()) {
			if (childFirstLayerContext == actualContext && childFirstLayerContext.getActualEndPosition() > actualContext.getActualEndPosition()) {
				return overallContextOptions.exportAsJSONArray();
			}
			//HierarchyContextProcessor.constructAllAvailableCallsRecursive(globalContext, childFirstLayerContext, actualContext, overallContextOptions);
			extractedContextOption = childFirstLayerContext.insertAllInstantiations("", actualContext);
			overallContextOptions.mergeBase(extractedContextOption);
		}
		if (baseFirstInnerContext == actualContext) {
			extractedContextOption =  actualContext.insertAllInstantiations("", actualContext);
			overallContextOptions.mergeBase(extractedContextOption);
			return overallContextOptions.exportAsJSONArray();
		}
		return overallContextOptions.exportAsJSONArray();
	}
}
