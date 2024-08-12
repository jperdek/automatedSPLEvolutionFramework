package variationPointUpdates;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import codeContext.processors.ASTTextExtractorTools;
import codeContext.processors.NotFoundVariableDeclaration;
import divisioner.VariationPointDivisionConfiguration;
import splEvolutionCore.candidateSelector.AlreadyProvidedArgumentInConfigurationExpressionPlace;
import splEvolutionCore.candidateSelector.NegativeVariationPointCandidate;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;
import variationPointsVisualization.NegativeVariabilityDecoratorConsistenceVerifier;


/**
 * Inserts selected user/system annotations for variable code fragments
 * 
 * @author Jakub Perdek
 *
 */
public class AnnotationInjector {
	
	/**
	 * Creation of annotation injector
	 */
	public AnnotationInjector() {}
	
	/**
	 * Determines how is variable declared and returns appropriate annotation
	 * 
	 * @param astRoot - the root of processed AST
	 * @param astVariable - processed AST part with declared variable 
	 * @param isUserAnnotation - if created annotation should belong/be marked and treated as user otherwise will be treated as system
	 * @return associated annotation to given annotation type
	 * 
	 * @throws NotFoundVariableDeclaration
	 */
	private static JSONObject determineAndGetUsedVariableTypeAst(JSONObject astRoot, 
			JSONObject astVariable, boolean isUserAnnotation) throws NotFoundVariableDeclaration {
		String contentText = (String) astRoot.get("text");
		String declarationName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(astVariable);
		Pattern pattern = Pattern.compile("(var|let|const)(\\s+|\\s+[^;]+,\\s*)" + declarationName, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(contentText);
	    
	    String matchedResult;
	    boolean matchFound = matcher.find();
	    if(matchFound) {
	      matchedResult = matcher.group();
	      if(matchedResult.toLowerCase().startsWith("var")) {
	    	  return AnnotationStubs.getGlobalVariableAnnotationAst(isUserAnnotation);
	      } 
	      return AnnotationStubs.getLocalVariableAnnotationAst(isUserAnnotation);
	    } else {
	    	throw new NotFoundVariableDeclaration("Variable declaration not found in AST text: " + declarationName);
	    }
	}
	
	/**
	 * Process annotations for variable in processed AST part
	 * -adds appropriate user annotation to processed AST variable - annotates variable as negative variation point
	 * 
	 * @param astRoot - the root of processed AST
	 * @param processedBlock - the AST block that is actually processed 
	 * @param isUserAnnotation - if created annotation should belong/be marked and treated as user otherwise will be treated as system
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 * @throws NotFoundVariableDeclaration
	 */
	public static void processAnnotationForVariablesInAstPart(JSONObject astRoot, 
			JSONObject processedBlock, boolean isUserAnnotation) throws IOException, InterruptedException, 
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation, NotFoundVariableDeclaration {
		JSONObject declarationList = (JSONObject) processedBlock.get("declarationList");
		JSONObject variableAnnotationAst;
		JSONArray illegalDecoratorsList, modifiersList;
		if (declarationList != null) {
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

			variableAnnotationAst = AnnotationInjector.determineAndGetUsedVariableTypeAst(astRoot, processedBlock, isUserAnnotation);
			illegalDecoratorsList.add(variableAnnotationAst); // all variability annotations should be removed - no checking is necessary
			if (VariationPointDivisionConfiguration.INJECT_UNSUPPORTED_VP_MARKS) {
				if (NegativeVariabilityDecoratorConsistenceVerifier.areDecoratorsConsistent(modifiersList, true)) {
					modifiersList.add(variableAnnotationAst);
				}
			}
		}
	}
	
	/**
	 * Process annotations for class in processed AST part
	 * -adds appropriate user annotation to processed AST class - annotates class as negative variation point
	 * 
	 * @param processedBlock - the AST block that is actually processed 
	 * @param negativeVariationPointCandidate
	 * @param isUserAnnotation - if created annotation should belong/be marked and treated as user otherwise will be treated as system
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 * @throws AlreadyProvidedArgumentInConfigurationExpressionPlace
	 */
	public static void processAnnotationForClassInAstPart(JSONObject processedBlock, 
			NegativeVariationPointCandidate negativeVariationPointCandidate, boolean isUserAnnotation) throws IOException, 
			InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, 
			DuplicatedAnnotation, AlreadyProvidedArgumentInConfigurationExpressionPlace {
		JSONArray membersList = (JSONArray) processedBlock.get("members");
		
		JSONObject classAnnotationAst;
		JSONArray decoratorsList;
		if (membersList != null) {
			classAnnotationAst = AnnotationStubs.getClassAnnotationAst(isUserAnnotation);
			if (processedBlock.containsKey("modifiers")) {
				decoratorsList = (JSONArray) processedBlock.get("modifiers");
			} else {
				decoratorsList = new JSONArray();
				processedBlock.put("modifiers", decoratorsList);
			}
			classAnnotationAst = AnnotationStubs.getClassAnnotationAst(isUserAnnotation);
			if (VariationPointDivisionConfiguration.PRESERVE_MULTIPLE_USER_ANNOTATIONS) {
				negativeVariationPointCandidate.injectAndInsertAllConfigurationExpressions(classAnnotationAst, decoratorsList);
			} else {
				negativeVariationPointCandidate.injectConfigurationExpressionAsFirstArgument(classAnnotationAst);
				decoratorsList.add(classAnnotationAst);
			}
		}
	}
	
	/**
	 * Process annotations for class variable in processed AST part
	 * -adds appropriate user annotation to processed AST class variable - annotates class variable as negative variation point
	 * 
	 * @param processedBlock  - the AST block that is actually processed 
	 * @param processedBlockParent  - the parent of AST block that is actually processed 
	 * @param isUserAnnotation - if created annotation should belong/be marked and treated as user otherwise will be treated as system
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public static void processAnnotationForClassVariablesInAstPart(JSONObject processedBlock, 
			JSONObject processedBlockParent, boolean isUserAnnotation) throws IOException, InterruptedException, 
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		JSONArray membersList = (JSONArray) processedBlockParent.get("members");
		JSONObject variabilityAnnotationAst;
		JSONArray decoratorsList;
		if (membersList != null && !processedBlockParent.containsKey("body") && !processedBlock.containsKey("body")) {
			if (processedBlock.containsKey("modifiers")) {
				decoratorsList = (JSONArray) processedBlock.get("modifiers");
			} else {
				decoratorsList = new JSONArray();
				processedBlock.put("modifiers", decoratorsList);
			}
			//all variability annotations should be removed - no checking is necessary
			variabilityAnnotationAst = AnnotationStubs.getClassVariableAnnotationAst(isUserAnnotation);
			decoratorsList.add(variabilityAnnotationAst);
		}
	}
	
	/**
	 * Process annotations for non-class function in processed AST part
	 * -adds appropriate user annotation to processed AST non-class function - annotates non-class function as negative variation point
	 * 
	 * @param processedBlock - the AST block that is actually processed 
	 * @param processedBlockParent - the parent of AST block that is actually processed 
	 * @param isUser - if created annotation should belong/be marked and treated as user otherwise will be treated as system
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public static void processAnnotationForNotClassFunctionInAstPart(JSONObject processedBlock, 
			JSONObject processedBlockParent, boolean isUserAnnotation) throws IOException, InterruptedException, 
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		JSONObject nonClassFunctionAnnotationAst;
		JSONArray illegalDecoratorsList, decoratorsList;
		if (!processedBlockParent.containsKey("members") && !processedBlock.containsKey("declarationList") && !processedBlockParent.containsKey("parameters")) {
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
			//all variability annotations should be removed - no checking is necessary
			nonClassFunctionAnnotationAst = AnnotationStubs.getMethodAnnotationAst(isUserAnnotation);
			illegalDecoratorsList.add(nonClassFunctionAnnotationAst);
			if (VariationPointDivisionConfiguration.INJECT_UNSUPPORTED_VP_MARKS) {
				if (NegativeVariabilityDecoratorConsistenceVerifier.areDecoratorsConsistent(decoratorsList, true)) {
					decoratorsList.add(nonClassFunctionAnnotationAst);
				}
			}
		}
	}
	
	/**
	 * Process annotations for class functions in processed AST part
	 * -adds appropriate user annotation to processed AST class function - annotates class function as negative variation point
	 * 
	 * @param processedBlock - the AST block that is actually processed 
	 * @param processedBlockParent - the parent of AST block that is actually processed
	 * @param isUserAnnotation - if created annotation should belong/be marked and treated as user otherwise will be treated as system
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public static void processAnnotationForClassFunctionsInAstPart(JSONObject processedBlock, 
			JSONObject processedBlockParent, boolean isUserAnnotation) throws IOException, InterruptedException, 
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		JSONArray membersList = (JSONArray) processedBlockParent.get("members");
		
		JSONObject variabilityAnnotationAst;
		String functionName;
		JSONArray decoratorsList;
		if (membersList != null && processedBlock.containsKey("body") && processedBlock.containsKey("name")) {
			functionName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(processedBlock);
			
			if (functionName.equals("constructor")) { return; } //omits constructor - decorators cannot be applied
			if (processedBlock.containsKey("modifiers")) { 
				decoratorsList = (JSONArray) processedBlock.get("modifiers");
			} else {
				decoratorsList = new JSONArray();
				processedBlock.put("modifiers", decoratorsList);
			}
			
			variabilityAnnotationAst = AnnotationStubs.getClassMethodAnnotationAst(isUserAnnotation);
			decoratorsList.add(variabilityAnnotationAst);
		}
	}
	
	/**
	 * Process annotations for parameter in processed AST part
	 * -adds appropriate user annotation to processed AST fucntion/constructor parameter - annotates function/constructor parameter as negative variation points
	 * 
	 * @param processedBlock - the AST block that is actually processed
	 * @param processedBlockParent - the parent of AST block that is actually processed
	 * @param isUserAnnotation - if created annotation should belong/be marked and treated as user otherwise will be treated as system
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public static void processAnnotationForParameterInAstPart(JSONObject processedBlock, 
			JSONObject processedBlockParent, boolean isUserAnnotation) throws IOException, InterruptedException, 
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		JSONArray parametersList = (JSONArray) processedBlockParent.get("parameters");
		
		JSONObject variabilityAnnotationAst;
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

			if (functionName.equals("constructor")) { //omits constructor - decorators cannot be applied
				variabilityAnnotationAst = AnnotationStubs.getConstructorParameterAnnotationAst(isUserAnnotation);
			} else {
				variabilityAnnotationAst = AnnotationStubs.getParameterAnnotationAst(isUserAnnotation);
			}
			decoratorsList.add(variabilityAnnotationAst);
		}
	}
}
