package codeContext.processors;

import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import codeContext.GlobalContext;
import codeContext.InnerContext;
import divisioner.VariationPointDivisionConfiguration;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;
import variationPointsVisualization.ParameterExtensionMarker;
import variationPointsVisualization.VariationPointsWithVariablesMarker;
import variationPointsVisualization.VariationPointsWithVariablesMarker.DeclarationTypes;


/**
 * Manages context injection into AST
 *  
 * @author Jakub Perdek
 *
 */
public class ASTContextInjector {

	/**
	 * Injects the context for statements field including both variability types - the positive and negative variability
	 * -the negative variability can be processed only if annotations can be put on object thus object newVersionProcessedJSONObject is not null
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param usedInnerContext - actual/processed inner context in the hierarchy of inner contexts
	 * @param parentArrayObject - the parent JSON array where the positive variability marker (variable/parameter) is going to be inserted on the given position 
	 * @param position - the position where context should be injected in statements array
	 * @param newVersionProcessedJSONObject - new version of processed JSON object
	 * @return 1 if VariationPointDivisionConfiguration.ALLOW_POSITIVE_VARIABILITY is set to true otherwise 0
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public static int injectContextForStatements(GlobalContext globalContext, InnerContext usedInnerContext, JSONArray parentArrayObject, 
			int position, JSONObject newVersionProcessedJSONObject) throws IOException, InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		int added = ASTContextInjector.injectPositiveVariabilityContextForStatements(globalContext, usedInnerContext, parentArrayObject, position);
		if (newVersionProcessedJSONObject != null) { // when space between last element is managed
			ASTContextInjector.injectNegativeVariabilityContextForStatements(globalContext, usedInnerContext, newVersionProcessedJSONObject);
		}
		return added;
	}
	
	/**
	 * Injects the positive variability variation point context for statements field
	 * -generates all possible calls of available/reachable functionality from processed script
	 * -generates more descriptive inner representation of data on variation point from actual inner context if VariationPointDivisionConfiguration.LONG_CONTEXT_PRINT is set to true
	 * -generates descriptive representation from global context
	 * -generates positive variation point marker with all relevant/associated data in JSON format and inserts it into AST
	 * 
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param usedInnerContext - actual/processed inner context in the hierarchy of inner contexts
	 * @param parentArrayObject - the parent JSON array where the positive variability marker (non-class variable) is going to be inserted on the given position 
	 * @param position - the position in JSON array where the positive variability marker (non-class variable) should be inserted
	 * @return 1 if VariationPointDivisionConfiguration.ALLOW_POSITIVE_VARIABILITY is set to true otherwise 0
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static int injectPositiveVariabilityContextForStatements(GlobalContext globalContext, 
			InnerContext usedInnerContext, JSONArray parentArrayObject, int position) throws IOException, InterruptedException {
		JSONObject descriptiveJSON = new JSONObject();
		descriptiveJSON.put("global", globalContext.createDescriptiveJSON());
		if (VariationPointDivisionConfiguration.LONG_CONTEXT_PRINT) {
			descriptiveJSON.put("inner", usedInnerContext.createDescriptiveJSON(globalContext));
		}
		JSONArray allAvailableCalls = HierarchyContextProcessor.constructAllAvailableCalls(globalContext, usedInnerContext.getBaseContext(), usedInnerContext);
		if (!allAvailableCalls.isEmpty()) {
			descriptiveJSON.put("allAvailableCalls", allAvailableCalls);
		}
		if (VariationPointDivisionConfiguration.ALLOW_POSITIVE_VARIABILITY) {
			JSONObject astMarkerPart = VariationPointsWithVariablesMarker.generateMarkerFromVariableInAST(descriptiveJSON, DeclarationTypes.VAR, false); //non class variable
			parentArrayObject.add(position, astMarkerPart);
			return 1;
		}
		return 0;
	}
	
	/**
	 * Injects the negative variability context for statements field
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param usedInnerContext - actual/processed inner context in the hierarchy of inner contexts
	 * @param newVersionProcessedJSONObject - new version of processed JSON object
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public static void injectNegativeVariabilityContextForStatements(GlobalContext globalContext, InnerContext usedInnerContext, 
			JSONObject newVersionProcessedJSONObject) throws IOException, InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		AnnotationInjector.processAnnotationForVariablesInAstPart(globalContext, usedInnerContext, newVersionProcessedJSONObject);
	}
	
	/**
	 * Injects the negative variability context for members field
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param usedInnerContext - actual/processed inner context in the hierarchy of inner contexts
	 * @param newVersionProcessedJSONObject - new version of processed JSON object
	 * @param parentAstObject - the parent of actually processed AST part/object
	 * @param insertedVPElement - inserted variation point element
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public static void injectNegativeVariabilityContextForMembersBetweenParentAndArrayMember(GlobalContext globalContext, InnerContext usedInnerContext, 
			JSONObject newVersionProcessedJSONObject, JSONObject parentAstObject, JSONObject insertedVPElement) throws IOException, InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		if (insertedVPElement != null) {
			AnnotationInjector.processAnnotationForClassVariablesInAstPart(globalContext, usedInnerContext, insertedVPElement, parentAstObject);
		}
		if (newVersionProcessedJSONObject != null) {
			AnnotationInjector.processAnnotationForClassVariablesInAstPart(globalContext, usedInnerContext, newVersionProcessedJSONObject, parentAstObject);
		}
	}
	
	/**
	 * Injects the negative variability context for members
	 * -processes annotations for class
	 * -processes annotations for class functions
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param usedInnerContext - actual/processed inner context in the hierarchy of inner contexts
	 * @param newVersionProcessedJSONObject - new version of processed JSON object
	 * @param parentAstObject - the parent of actually processed AST part/object
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public static void injectNegativeVariabilityContextForMembers(GlobalContext globalContext, InnerContext usedInnerContext, 
			JSONObject newVersionProcessedJSONObject, 
			JSONObject parentAstObject) throws IOException, InterruptedException, 
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		AnnotationInjector.processAnnotationForClassInAstPart(globalContext, usedInnerContext, newVersionProcessedJSONObject);
		AnnotationInjector.processAnnotationForClassFunctionsInAstPart(globalContext, usedInnerContext, newVersionProcessedJSONObject, parentAstObject);
	}

	/**
	 * Injects positive variability context for members
	 * -injects positive variation point marker in the place between elements of members array or on margins
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param usedInnerContext - actual/processed inner context in the hierarchy of inner contexts
	 * @param parentArrayObject - the parent JSON array where the positive variability marker (class variable) is going to be inserted on the given position 
	 * @param position - the position in JSON array where the positive variability marker (class variable) should be inserted
	 * @return created marker appended to members array if VariationPointDivisionConfiguration.ALLOW_POSITIVE_VARIABILITY is set to true otherwise null
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static JSONObject injectContextForMembers(GlobalContext globalContext, InnerContext usedInnerContext, 
			JSONArray parentArrayObject, int position) throws IOException, InterruptedException {
		return ASTContextInjector.injectPositiveVariabilityContextForMembers(globalContext, usedInnerContext, parentArrayObject, position);
	}
	
	/**
	 * Injects the positive variability context for members
	 * -generates all possible calls of available/reachable functionality from processed script
	 * -generates more descriptive inner representation of data on variation point from actual inner context if VariationPointDivisionConfiguration.LONG_CONTEXT_PRINT is set to true
	 * -generates descriptive representation from global context
	 * -generates positive variation point marker with all relevant/associated data in JSON format and inserts it into AST
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param usedInnerContext - actual/processed inner context in the hierarchy of inner contexts
	 * @param parentArrayObject - the parent JSON array where the positive variability marker (class variable) is going to be inserted on the given position
	 * @param position - the position in JSON array where the positive variability marker (class variable) should be inserted
	 * @return created marker appended to members array if VariationPointDivisionConfiguration.ALLOW_POSITIVE_VARIABILITY is set to true otherwise null
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static JSONObject injectPositiveVariabilityContextForMembers(GlobalContext globalContext, InnerContext usedInnerContext, 
			JSONArray parentArrayObject, int position) throws IOException, InterruptedException {
		JSONObject descriptiveJSON = new JSONObject();
		descriptiveJSON.put("global", globalContext.createDescriptiveJSON());
		if (VariationPointDivisionConfiguration.LONG_CONTEXT_PRINT) {
			descriptiveJSON.put("inner", usedInnerContext.createDescriptiveJSON(globalContext));
		}
		// generates all available calls
		JSONArray allAvailableCalls = HierarchyContextProcessor.constructAllAvailableCalls(globalContext, usedInnerContext.getBaseContext(), usedInnerContext);
		if (!allAvailableCalls.isEmpty()) {
			descriptiveJSON.put("allAvailableCalls", allAvailableCalls);
		}
		// inserts marker if positive variability is allowed
		if (VariationPointDivisionConfiguration.ALLOW_POSITIVE_VARIABILITY) {
			JSONObject astMarkerPart = VariationPointsWithVariablesMarker.generateMarkerFromVariableInAST(descriptiveJSON, DeclarationTypes.WITHOUT, true); // creation of class marker
			parentArrayObject.add(position, astMarkerPart);
			return astMarkerPart;
		}
		return null;
	}
	
	/**
	 * Injects the positive variability context for parameters
	 * -generates positive variability function/constructor parameter in the position between function/constructor parameters or also on margins
	 * 
	 * @param parentArrayObject - the parent JSON array where the positive variability parameter is going to be inserted on the given position
	 * @param position - the position where positive variability function/constructor parameter is going to be inserted
	 * @return 1 if VariationPointDivisionConfiguration.ALLOW_POSITIVE_VARIABILITY and VariationPointDivisionConfiguration.GENERATE_ALL_FUNCTION_PARAMETERS otherwise 0
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static int injectPositiviveVariabilityContextForParameters(JSONArray parentArrayObject, int position) throws IOException, InterruptedException {
		if (VariationPointDivisionConfiguration.ALLOW_POSITIVE_VARIABILITY && VariationPointDivisionConfiguration.GENERATE_ALL_FUNCTION_PARAMETERS) {
			JSONObject astMarkerPart = ParameterExtensionMarker.generateMarkerFromParameterInAST();
			parentArrayObject.add(position, astMarkerPart);
			return 1;
		}
		return 0;
	}
	
	/**
	 * Injects negative variability context for parameter - injects the decorator/annotation for parameter
	 * -generates annotation/decorates function/constructor parameter on given position
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param usedInnerContext - actual/processed inner context in the hierarchy of inner contexts
	 * @param parentArrayObject - the parent JSON array where the actual parameter (negative variability) is going to be annotated on the given position
	 * @param position - the position where negative variability annotation for parameter is going to be injected
	 * @param newVersionProcessedJSONObject - new version of processed JSON object
	 * @param parentAstObject - the parent of actually processed AST part/object
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public static void injectNegativeVariabilityContextForParameters(GlobalContext globalContext, InnerContext usedInnerContext, 
			JSONArray parentArrayObject, int position, JSONObject newVersionProcessedJSONObject, JSONObject parentAstObject) throws IOException, InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		AnnotationInjector.processAnnotationForParameterInAstPart(globalContext, usedInnerContext, newVersionProcessedJSONObject, parentAstObject);
	}

	/**
	 * Injects the context for parameter
	 * -both variability types are supported
	 * -positive variability
	 * -negative variability
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param usedInnerContext - actual/processed inner context in the hierarchy of inner contexts
	 * @param parentArrayObject - the parent JSON array where the positive variability marker (function/constructor parameter) is going to be inserted on the given position
	 * @param position - the position in JSON array where the positive variability marker (function/constructor parameter) should be inserted
	 * @param newVersionProcessedJSONObject - new version of processed JSON object
	 * @param parentAstObject - the parent of actually processed AST part/object
	 * @return  1 if VariationPointDivisionConfiguration.ALLOW_POSITIVE_VARIABILITY and VariationPointDivisionConfiguration.GENERATE_ALL_FUNCTION_PARAMETERS otherwise 0
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public static int injectContextForParameters(GlobalContext globalContext, InnerContext usedInnerContext, JSONArray parentArrayObject, 
			int position, JSONObject newVersionProcessedJSONObject, JSONObject parentAstObject) throws IOException, 
	InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		int added = ASTContextInjector.injectPositiviveVariabilityContextForParameters(parentArrayObject, position);
		if (newVersionProcessedJSONObject != null) {
			ASTContextInjector.injectNegativeVariabilityContextForParameters(globalContext, usedInnerContext, 
					parentArrayObject, position, newVersionProcessedJSONObject, parentAstObject);
		}
		return added;
	}
}
