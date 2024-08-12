package divisioner;

import java.io.IOException;
import java.io.PrintWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import astFileProcessor.ASTLoader;
import codeConstructsEvaluation.transformation.ASTConverterClient;
import codeContext.ClassContext;
import codeContext.CodeContext;
import codeContext.FunctionContext;
import codeContext.GlobalContext;
import codeContext.InnerContext;
import codeContext.processors.ASTContextInjector;
import codeContext.processors.ASTContextProcessor;
import codeContext.processors.SystemAnnotationInjector;
import codeContext.processors.FunctionProcessor;
import codeContext.processors.HierarchyContextProcessor;
import codeContext.processors.NotFoundVariableDeclaration;
import divisioner.divisionStrategies.RecallStrategy;
import splEvolutionCore.DebugInformation;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;


/**
 * Functionality to division AST into positive and negative variability variation points
 * 
 * @author Jakub Perdek
 *
 */
public class Divisioner implements DivisioningInterface {

	/**
	 * The configuration and support to division variation points into negative and positive variability accoring to particular strategy
	 */
	private VariationPointsDivisioningStrategy variationPointsDivisioningStrategy;
	
	/**
	 * The code context used for divisioning
	 */
	private CodeContext divisionerContext = null;
	
	/**
	 * The functionality to optimize processing by cutting the leaves
	 */
	private AstLeavesCutter astLeavesCutter;
	
	
	/**
	 * Creates the instance of functionality to division AST into positive and negative variability variation points
	 * -as default strategy is used RecallStrategy()
	 */
	public Divisioner() {
		this(new RecallStrategy());
	}
	
	/**
	 * Creates the instance of functionality to division AST into positive and negative variability variation points
	 * 
	 * @param variationPointDivisionConfiguration - implementation of strategy to division variation points
	 */
	public Divisioner(VariationPointsDivisioningStrategy variationPointsDivisioningStrategy) {
		this.variationPointsDivisioningStrategy = variationPointsDivisioningStrategy;
		this.astLeavesCutter = new AstLeavesCutter();
	}
	
	/**
	 * Returns code context with hierarchical information obtained from divisioning
	 * 
	 * @return the code context with hierarchical information obtained from divisioning
	 */
	public CodeContext getCodeContextFromDivision() { return this.divisionerContext; }
	
	/**
	 * Returns used strategy for divisioning into variation points
	 * 
	 * @return used strategy for divisioning into variation points
	 */
	public VariationPointsDivisioningStrategy getDivisionStrategy() { return this.variationPointsDivisioningStrategy; }

	//public static int counter = 1;
	/**
	 * Recursive search of variation points
	 * 
	 * @param astRoot - the root of processed AST
	 * @param astPart - actually processed AST part
	 * @param astParent - the parent of actually processed AST part
	 * @param newAstPart - the actually processed AST part from new divisioned AST
	 * @param newAstParent - the parent of actually processed AST part from new divisioned AST
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param actualContext - the actually processed code context (method or class)
	 * @param parentContext - the parent of actually processed code context (method or class)
	 * @param useTypes - if true then it allows to specify types in the output otherwise not
	 * 
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	private void searchVariationPointPositionsHierarchic(JSONObject astRoot, JSONObject astPart, JSONObject astParent,
			JSONObject newAstPart, JSONObject newAstParent, GlobalContext globalContext, InnerContext actualContext, 
			InnerContext parentContext, boolean useTypes) throws NotFoundVariableDeclaration, IOException, InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		String key;
		if (!astPart.containsKey(ASTContextProcessor.SearchPositions.BEGIN.label)) {
			return;
		}
		int createdMarkers = 0;
		long actualEndPosition = (long) astPart.get(ASTContextProcessor.SearchPositions.END.label);
		InnerContext newActualContext = HierarchyContextProcessor.createInnerContextIfNecessary(astRoot, astPart, actualContext.getBaseContext(), useTypes);
		InnerContext usedContext;
		if (newActualContext != null) { //new context is created/observed
			if (actualContext instanceof ClassContext) {
				//PRIORITIZE CLASS FUNCTIONS
				//previousStartPosition = actualContext.getActualStartPosition();
				//actualContext.addContext(previousStartPosition + counter, newActualContext);
				//counter = counter + 1;
				actualContext.addContext(actualEndPosition, newActualContext);
			} else {
				actualContext.addContext(actualEndPosition, newActualContext);
			}
		
			parentContext = actualContext;
			usedContext = newActualContext;
		} else {
			usedContext = actualContext;
		}
		ASTContextProcessor.evaluateTreeStep(globalContext, usedContext, astRoot, astPart);

		JSONObject injectedMember;
		Object entryValue, entryValueNewAst;
		JSONObject entryJSONObject, entryJSONObjectNewAst;
		JSONArray duplicatedNewAstArray, originalArray;
		ASTContextInjector.injectNegativeVariabilityContextForMembers(globalContext, usedContext, newAstPart, newAstParent);
		if (usedContext instanceof FunctionContext) {
			FunctionProcessor.processFunctionASTContext(astPart, astRoot, globalContext,(FunctionContext) usedContext, newAstPart, newAstParent);
		}
		for(Object entryKey: astPart.keySet()) {
			key = (String) entryKey;
			entryValue = astPart.get(key);
			if (this.astLeavesCutter.shouldCut(key, entryValue)) {
				continue;
			}
			entryValueNewAst = newAstPart.get(key);
			// CHANGING VALUES ONLY
			if (entryValue instanceof JSONObject) {
				entryJSONObject = (JSONObject) entryValue;
				entryJSONObjectNewAst = (JSONObject) entryValueNewAst;
				this.searchVariationPointPositionsHierarchic(astRoot, entryJSONObject, astPart, 
						entryJSONObjectNewAst, newAstPart, globalContext, usedContext, parentContext, useTypes);
				
			//POSSIBLY COMBINING FUNCTIONALITY
			} else if(entryValue instanceof JSONArray) {

				duplicatedNewAstArray = new JSONArray();
				for (Object newArrayPart: ((JSONArray) entryValueNewAst)) {
					entryJSONObjectNewAst = (JSONObject) newArrayPart;
					duplicatedNewAstArray.add(entryJSONObjectNewAst);
				}
				
				originalArray = ((JSONArray) entryValue);
				createdMarkers = 0;
				for (int index = 0; index < originalArray.size(); index++) {
					entryJSONObject = (JSONObject) originalArray.get(index);
					entryJSONObjectNewAst = (JSONObject) duplicatedNewAstArray.get(createdMarkers + index);
					
					//FUNCTIONS CAN BE ACCESSED FROM EVERYWHERE INSIDE PARENT EXCEPT DIRECTED IN THEMSELVES
					if ((usedContext instanceof ClassContext || usedContext instanceof FunctionContext) && entryJSONObject.containsKey("body")) { 
						this.searchVariationPointPositionsHierarchic(astRoot, entryJSONObject, astPart, 
								entryJSONObjectNewAst, newAstPart, globalContext, usedContext, parentContext, useTypes);
					}
				}
				
				for (int index = 0; index < originalArray.size(); index++) {
					entryJSONObject = (JSONObject) originalArray.get(index);
					entryJSONObjectNewAst = (JSONObject) duplicatedNewAstArray.get(createdMarkers + index);
					if (key.equals("statements")) { // BETWEEN EACH PAIR OF NON-CLASS VARIABLES, FUNCTIONS AND CLASSES
						if (ASTContextInjector.injectContextForStatements(globalContext, usedContext, duplicatedNewAstArray, 
								createdMarkers + index, entryJSONObjectNewAst) == 1) {
							createdMarkers = createdMarkers + 1;
						}
					} else if (key.equals("members")) { // BETWEEN EACH PAIR OF CLASS VARIABLES, FUNCTIONS
						injectedMember = ASTContextInjector.injectContextForMembers(globalContext, usedContext, duplicatedNewAstArray, 
								createdMarkers + index);
						createdMarkers = createdMarkers + 1;
						ASTContextProcessor.evaluateTreeMember(usedContext, entryJSONObject, astRoot, true);
						ASTContextInjector.injectNegativeVariabilityContextForMembersBetweenParentAndArrayMember(
								globalContext, usedContext, entryJSONObjectNewAst, newAstPart, injectedMember);
					} else if (key.equals("parameters")) { // BETWEEN EACH PAIR OF FUNCTION
						if (ASTContextInjector.injectContextForParameters(globalContext, usedContext, duplicatedNewAstArray, 
								createdMarkers + index, entryJSONObjectNewAst, newAstParent) == 1) {
							createdMarkers = createdMarkers + 1;
						}
					}
					if ((usedContext instanceof ClassContext || usedContext instanceof FunctionContext) && entryJSONObject.containsKey("body")) { continue; }
					this.searchVariationPointPositionsHierarchic(astRoot, entryJSONObject, astPart, 
							entryJSONObjectNewAst, newAstPart, globalContext, usedContext, parentContext, useTypes);
					actualEndPosition = (long) entryJSONObject.get(ASTContextProcessor.SearchPositions.END.label);
				}
				
				if (key.equals("statements")) {
					ASTContextInjector.injectContextForStatements(
							globalContext, usedContext, duplicatedNewAstArray, createdMarkers + originalArray.size(), null);
					newAstPart.put(key, duplicatedNewAstArray);
				} else if (key.equals("members")) {
					injectedMember = ASTContextInjector.injectContextForMembers(globalContext, usedContext, duplicatedNewAstArray, 
							createdMarkers + originalArray.size());
					ASTContextInjector.injectNegativeVariabilityContextForMembersBetweenParentAndArrayMember(
							globalContext, usedContext, null, newAstPart, injectedMember);
					newAstPart.put(key, duplicatedNewAstArray);
				} else if (key.equals("parameters")) {
					ASTContextInjector.injectContextForParameters(globalContext, usedContext, duplicatedNewAstArray, 
							createdMarkers + originalArray.size(), null, null);
					newAstPart.put(key, duplicatedNewAstArray);
				}
			}
		}
	}

	/**
	 * Hierarchically divisions variation points according provided AST (TypeScript)  
	 * -initializes root inner context and global context
	 * -launches variation points divisioning - positive and negative variability
	 * -outputs ordered structure
	 * 
	 * @param astTreeRoot - the root of processed AST
	 * @param fileName - the name of parsed AST
	 * @param useTypes - if types should be used if found during divisioning, especially context/contexts preparation
	 * @return divisioned AST (particular code) into variation points
	 * 
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public JSONObject divisionToVPHierarchic(JSONObject astTreeRoot, String fileName, boolean useTypes) throws NotFoundVariableDeclaration, 
				IOException, InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		JSONObject astRoot = (JSONObject) astTreeRoot.get("ast");
		JSONObject dividedAst = ASTLoader.loadASTFromString(astRoot.toString());
		this.divisionerContext = ASTContextProcessor.initializeCodeContext(astRoot, astRoot, fileName, useTypes);
		InnerContext innerContext = this.divisionerContext.getInnerContext();
		GlobalContext globalContext = this.divisionerContext.getGlobalContext();
		this.searchVariationPointPositionsHierarchic(astRoot, astRoot, astRoot, dividedAst, dividedAst, 
				globalContext, innerContext, innerContext, useTypes);
		
		if (DebugInformation.OUTPUT_DEBUG_FILES) {
			System.out.println("Debug file on output: labeledVariationPoints.txt");
			try (PrintWriter out = new PrintWriter("labeledVariationPointsAst.txt")) { out.println(dividedAst.toString()); }
			String modifiedContent = ASTConverterClient.convertFromASTToCode(dividedAst.toString());
			try (PrintWriter out = new PrintWriter("labeledVariationPoints.txt")) { out.println(modifiedContent); }
		}
		
		innerContext.printOrderedStructure(globalContext);
		return dividedAst;
	}
}
