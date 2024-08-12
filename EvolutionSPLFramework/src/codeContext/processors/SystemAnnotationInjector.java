package codeContext.processors;

import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import codeContext.GlobalContext;
import codeContext.InnerContext;
import divisioner.VariationPointDivisionConfiguration;
import variationPointsVisualization.AnnotationExtensionMarker;
import variationPointsVisualization.AnnotationExtensionMarker.SystemAnnotationType;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;
import variationPointsVisualization.NegativeVariabilityDecoratorConsistenceVerifier;


/**
 * Injects annotation to available/supported code constructs
 * 
 * @author Jakub Perdek
 *
 */
public class SystemAnnotationInjector {

	/**
	 * Instantiates AnnotationInjector 
	 */
	public SystemAnnotationInjector() {}
	
	/**
	 * Generates annotation/decorates particular variables in AST part
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param innerContext- the actual inner context in the hierarchy of inner contexts
	 * @param processedBlock - actually processed block
	 * @throws IOException - the exception for various problems with file loading
	 * @throws InterruptedException - the exception thrown during interruption
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint - the exception capturing different annotation types on the same variation point
	 * @throws DuplicatedAnnotation - the exception capturing duplicated annotation that is found on AST
	 */
	public static void processAnnotationForVariablesInAstPart(GlobalContext globalContext, InnerContext innerContext, JSONObject processedBlock) throws IOException, InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		JSONObject declarationList = (JSONObject) processedBlock.get("declarationList");
		JSONArray alreadyProvidedVariabilityAnnotations;
		JSONObject variabilityAnnotationAst;
		JSONArray illegalDecoratorsList, modifiersList;
		if (declarationList != null) {
			alreadyProvidedVariabilityAnnotations = AnnotationProcessor.getAllAnnotationsFromProcessedBlockRoot(processedBlock);
			if (processedBlock.containsKey("illegalDecorators")) { //NO EFFECT ON AST TO CODE 
				illegalDecoratorsList = (JSONArray) processedBlock.get("illegalDecorators");
			} else {
				illegalDecoratorsList = new JSONArray();
				processedBlock.put("illegalDecorators", illegalDecoratorsList);
			}
			
			if (VariationPointDivisionConfiguration.INJECT_UNSUPPORTED_VP_MARKS) {
				if (processedBlock.containsKey("modifiers")) {
					modifiersList = (JSONArray) processedBlock.get("modifiers");
				} else {
					modifiersList = new JSONArray();
					processedBlock.put("modifiers", modifiersList);
				}
			}
			if (alreadyProvidedVariabilityAnnotations == null) {
				variabilityAnnotationAst = AnnotationExtensionMarker.generateMarkerForVariableInAst(SystemAnnotationType.VARIABLE);
				if (NegativeVariabilityDecoratorConsistenceVerifier.areDecoratorsConsistent(illegalDecoratorsList, true)) {
					illegalDecoratorsList.add(variabilityAnnotationAst);
				}
				if (VariationPointDivisionConfiguration.INJECT_UNSUPPORTED_VP_MARKS) {
					if (NegativeVariabilityDecoratorConsistenceVerifier.areDecoratorsConsistent(modifiersList, true)) {
						modifiersList.add(variabilityAnnotationAst);
					}
				}
			}
		}
	}
	
	/**
	 * Generates annotation/decorates particular class in AST part
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param innerContext - the actual inner context in the hierarchy of inner contexts
	 * @param processedBlock - actually processed block
	 * @throws IOException - the exception for various problems with file loading
	 * @throws InterruptedException - the exception thrown during interruption
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint - the exception capturing different annotation types on the same variation point
	 * @throws DuplicatedAnnotation - the exception capturing duplicated annotation that is found on AST
	 */
	public static void processAnnotationForClassInAstPart(GlobalContext globalContext, InnerContext innerContext, JSONObject processedBlock) throws IOException, InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		JSONArray membersList = (JSONArray) processedBlock.get("members");
		
		JSONObject variabilityAnnotationAst;
		JSONArray alreadyProvidedVariabilityAnnotations;
		JSONArray decoratorsList;
		if (membersList != null) {
			alreadyProvidedVariabilityAnnotations = AnnotationProcessor.getAllAnnotationsFromProcessedBlockRoot(processedBlock);

			if (processedBlock.containsKey("modifiers")) {
				decoratorsList = (JSONArray) processedBlock.get("modifiers");
			} else {
				decoratorsList = new JSONArray();
				processedBlock.put("modifiers", decoratorsList);
			}
			if (alreadyProvidedVariabilityAnnotations == null) {
				if (NegativeVariabilityDecoratorConsistenceVerifier.areDecoratorsConsistent(decoratorsList, true)) {
					variabilityAnnotationAst = AnnotationExtensionMarker.generateMarkerForVariableInAst(SystemAnnotationType.CLASS);
					decoratorsList.add(variabilityAnnotationAst);
				}
			} 
		} 
	}
	
	/**
	 * Generates annotation/decorates particular class variables in AST part
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param innerContext - the actual inner context in the hierarchy of inner contexts
	 * @param processedBlock - actually processed block
	 * @param processedBlockParent - the parent of actually processed block
	 * @throws IOException - the exception for various problems with file loading
	 * @throws InterruptedException - the exception thrown during interruption
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint - the exception capturing different annotation types on the same variation point
	 * @throws DuplicatedAnnotation - the exception capturing duplicated annotation that is found on AST
	 */
	public static void processAnnotationForClassVariablesInAstPart(GlobalContext globalContext, InnerContext innerContext, 
			JSONObject processedBlock, JSONObject processedBlockParent) throws IOException, InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		JSONArray membersList = (JSONArray) processedBlockParent.get("members");
		JSONObject variabilityAnnotationAst;
		JSONArray alreadyProvidedVariabilityAnnotations;
		JSONArray decoratorsList;
		if (membersList != null && !processedBlockParent.containsKey("body") && !processedBlock.containsKey("body")) {
			alreadyProvidedVariabilityAnnotations = AnnotationProcessor.getAllAnnotationsFromProcessedBlockRoot(processedBlock);
			if (processedBlock.containsKey("modifiers")) {
				decoratorsList = (JSONArray) processedBlock.get("modifiers");
			} else {
				decoratorsList = new JSONArray();
				processedBlock.put("modifiers", decoratorsList);
			}
			if (alreadyProvidedVariabilityAnnotations == null) {
				if (NegativeVariabilityDecoratorConsistenceVerifier.areDecoratorsConsistent(decoratorsList, true)) {
					
					
					variabilityAnnotationAst = AnnotationExtensionMarker.generateMarkerForVariableInAst(SystemAnnotationType.CLASS_VARIABLE);
					decoratorsList.add(variabilityAnnotationAst);
				}
			}
		}
	}
	
	/**
	 * Generates annotation/decorates particular non-class functions in AST part
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param innerContext - the actual inner context in the hierarchy of inner contexts
	 * @param processedBlock - actually processed block
	 * @param processedBlockParent - the parent of actually processed block
	 * @throws IOException - the exception for various problems with file loading
	 * @throws InterruptedException - the exception thrown during interruption
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint - the exception capturing different annotation types on the same variation point
	 * @throws DuplicatedAnnotation - the exception capturing duplicated annotation that is found on AST
	 */
	public static void processAnnotationForNotClassFunctionInAstPart(GlobalContext globalContext, InnerContext innerContext, 
			JSONObject processedBlock, JSONObject processedBlockParent) throws IOException, InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		JSONObject variabilityAnnotationAst;
		JSONArray alreadyProvidedVariabilityAnnotations;
		JSONArray illegalDecoratorsList, decoratorsList;
		if (!processedBlockParent.containsKey("members") && !processedBlock.containsKey("declarationList") && !processedBlockParent.containsKey("parameters")) {
			alreadyProvidedVariabilityAnnotations = AnnotationProcessor.getAllAnnotationsFromProcessedBlockRoot(processedBlock);
			if (processedBlock.containsKey("illegalDecorators")) { //NO EFFECT ON AST TO CODE 
				illegalDecoratorsList = (JSONArray) processedBlock.get("illegalDecorators");
			} else {
				illegalDecoratorsList = new JSONArray();
				processedBlock.put("illegalDecorators", illegalDecoratorsList);
			}
			
			if (VariationPointDivisionConfiguration.INJECT_UNSUPPORTED_VP_MARKS) {
				if (processedBlock.containsKey("modifiers")) {
					decoratorsList = (JSONArray) processedBlock.get("modifiers");
				} else {
					decoratorsList = new JSONArray();
					processedBlock.put("modifiers", decoratorsList);
				}
			}
			if (alreadyProvidedVariabilityAnnotations == null) {
				variabilityAnnotationAst = AnnotationExtensionMarker.generateMarkerForVariableInAst(SystemAnnotationType.FUNCTION);
				if (NegativeVariabilityDecoratorConsistenceVerifier.areDecoratorsConsistent(illegalDecoratorsList, true)) {
					illegalDecoratorsList.add(variabilityAnnotationAst);
				}
				if (VariationPointDivisionConfiguration.INJECT_UNSUPPORTED_VP_MARKS) {
					if (NegativeVariabilityDecoratorConsistenceVerifier.areDecoratorsConsistent(decoratorsList, true)) {
						decoratorsList.add(variabilityAnnotationAst);
					}
				}
			}
		}
	}
	
	/**
	 * Generates annotation/decorates particular functions in AST part
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param innerContext - the actual inner context in the hierarchy of inner contexts
	 * @param processedBlock - actually processed block
	 * @param processedBlockParent - the parent of actually processed block
	 * @throws IOException - the exception for various problems with file loading
	 * @throws InterruptedException - the exception thrown during interruption
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint - the exception capturing different annotation types on the same variation point
	 * @throws DuplicatedAnnotation - the exception capturing duplicated annotation that is found on AST
	 */
	public static void processAnnotationForClassFunctionsInAstPart(GlobalContext globalContext, InnerContext innerContext,
			JSONObject processedBlock, JSONObject processedBlockParent) throws IOException, InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		JSONArray membersList = (JSONArray) processedBlockParent.get("members");
		
		JSONObject variabilityAnnotationAst;
		JSONArray alreadyProvidedVariabilityAnnotations;
		String functionName;
		JSONArray decoratorsList, modifiersList;
		if (membersList != null && processedBlock.containsKey("body") && processedBlock.containsKey("name")) {
			JSONObject textObject = ((JSONObject) processedBlock.get("name"));
			if (textObject.containsKey("escapedText")) {
				functionName = (String) textObject.get("escapedText");
			} else {
				functionName = (String) textObject.get("text");
			}
			
			if (functionName.equals("constructor")) { //omits constructor - decorators cannot be applied
				return;
			}

			alreadyProvidedVariabilityAnnotations = AnnotationProcessor.getAllAnnotationsFromProcessedBlockRoot(processedBlock);
			if (processedBlock.containsKey("modifiers")) { 
				decoratorsList = (JSONArray) processedBlock.get("modifiers");
			} else {
				decoratorsList = new JSONArray();
				processedBlock.put("modifiers", decoratorsList);
			}
			
			if (alreadyProvidedVariabilityAnnotations == null) {
				if (NegativeVariabilityDecoratorConsistenceVerifier.areDecoratorsConsistent(decoratorsList, true)) {
					variabilityAnnotationAst = AnnotationExtensionMarker.generateMarkerForVariableInAst(SystemAnnotationType.CLASS_FUNCTION);
					decoratorsList.add(variabilityAnnotationAst);
				}
			}
		}
	}
	
	/**
	 * Generates annotation/decorates particular function/constructor parameter
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param innerContext - the actual inner context in the hierarchy of inner contexts
	 * @param processedBlock - actually processed block
	 * @param processedBlockParent - the parent of actually processed block
	 * @throws IOException - the exception for various problems with file loading
	 * @throws InterruptedException - the exception thrown during interruption
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint - the exception capturing different annotation types on the same variation point
	 * @throws DuplicatedAnnotation - the exception capturing duplicated annotation that is found on AST
	 */
	public static void processAnnotationForParameterInAstPart(GlobalContext globalContext, InnerContext innerContext, 
			JSONObject processedBlock, JSONObject processedBlockParent) throws IOException, InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		JSONArray parametersList = (JSONArray) processedBlockParent.get("parameters");
		
		JSONObject variabilityAnnotationAst;
		JSONArray alreadyProvidedVariabilityAnnotations;
		String functionName;
		JSONArray decoratorsList;
		if (parametersList != null) {
			functionName = (String) ((JSONObject) processedBlock.get("name")).get("escapedText");

			if (processedBlock.containsKey("modifiers")) {
				decoratorsList = (JSONArray) processedBlock.get("modifiers");
			} else {
				decoratorsList = new JSONArray();
				processedBlock.put("modifiers", decoratorsList);
			}
			alreadyProvidedVariabilityAnnotations = AnnotationProcessor.getAllAnnotationsFromProcessedBlockRoot(processedBlock);
			if (alreadyProvidedVariabilityAnnotations == null) {
				if (NegativeVariabilityDecoratorConsistenceVerifier.areDecoratorsConsistent(decoratorsList, true)) {
					if (functionName.equals("constructor")) { //omits constructor - decorators cannot be applied
						variabilityAnnotationAst = AnnotationExtensionMarker.generateMarkerForVariableInAst(SystemAnnotationType.CONSTRUCTOR_PARAMETER);
					} else {
						variabilityAnnotationAst = AnnotationExtensionMarker.generateMarkerForVariableInAst(SystemAnnotationType.PARAMETER);
					}
					decoratorsList.add(variabilityAnnotationAst);
				}
			}
		}
	}
}
