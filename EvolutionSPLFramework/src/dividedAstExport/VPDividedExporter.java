package dividedAstExport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import astFileProcessor.ASTLoader;
import codeConstructsEvaluation.transformation.ASTConverterClient;
import codeContext.processors.ASTTextExtractorTools;
import codeContext.processors.AnnotationProcessor;
import dividedAstExport.recursionCycleFinder.RecursionCycleFinder;
import divisioner.VariationPointDivisionConfiguration;
import divisioner.variabilityASTAntagonistMapping.HierarchyEntityConstructor;
import splEvolutionCore.DebugInformation;
import splEvolutionCore.DefaultEvolutionCore;
import variationPointsVisualization.AnnotationExtensionMarker;
import variationPointsVisualization.NegativeVariabilityDecoratorConsistenceVerifier;


/**
 * Divisions specific code in form of ATS into variation points
 * -the negative variation points are annotated with system annotations
 * -the positive variation points are marked with system annotations
 * -user annotations are preserved
 * 
 * @author Jakub Perdek
 *
 */
public class VPDividedExporter {
	
	/**
	 * Associated instance of functionality to observe and mark recursions - consumes additional resources (not simple)
	 */
	private RecursionCycleFinder recursionCycleFinder = null;
	
	/**
	 * The root of processed AST - the root of AST from base code
	 */
	private JSONObject astRoot;
	
	
	/**
	 * Creates VPDividedExporter instance
	 */
	public VPDividedExporter() {}
	
	/**
	 * Collects, process and returns all variation points - associated information (positive and negative variability)
	 * @param highlightedAstRoot - the root of processed AST with inserted positive and negative variability markers and annotations
	 * @param originalAstRoot - the root of original AST
	 * @return the array of all variation points - associated information (positive and negative variability)
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 */
	public JSONArray collectAndProcessAllVariationPoints(JSONObject highlightedAstRoot, JSONObject originalAstRoot) throws IOException, InterruptedException, InvalidSystemVariationPointMarkerException {
		List<JSONObject> orderedVariationPoints = new ArrayList<JSONObject>();
		JSONObject originalAstRootFiltered;
		if (originalAstRoot.containsKey("ast")) { 
			originalAstRootFiltered = (JSONObject) originalAstRoot.get("ast"); 
		} else {
			originalAstRootFiltered = originalAstRoot;
		}
		if (DebugInformation.SHOW_POLLUTING_INFORMATION) {
			System.out.println("Original Filtered AST:");
			System.out.println(originalAstRootFiltered.toString());
			System.out.println("--------------------------------------->");
		}
		//JSONObject astRootCopy = ASTLoader.loadASTFromString(astRoot.toString());
		if (VariationPointDivisionConfiguration.PREFER_POSITION_UPDATES_BEFORE_PERSISTING_ILLEGAL_DECORATORS_INFORMATION) {
			highlightedAstRoot = (JSONObject) this.convertRepeatedlyToVerifyPositionNumbering(highlightedAstRoot).get("ast");
			this.recursionCycleFinder = new RecursionCycleFinder(highlightedAstRoot);
			this.harvestVariationPoints(highlightedAstRoot, highlightedAstRoot, highlightedAstRoot, "", "", orderedVariationPoints, originalAstRootFiltered);
		} else {
			JSONObject newAstRoot = (JSONObject) this.convertRepeatedlyToVerifyPositionNumbering(highlightedAstRoot).get("ast");
			this.recursionCycleFinder = new RecursionCycleFinder(newAstRoot);
			this.harvestVariationPoints(newAstRoot, newAstRoot, newAstRoot, "", "", orderedVariationPoints, originalAstRootFiltered);
		}
		
		JSONArray orderedVariationPointsJsonArray = new JSONArray();
		orderedVariationPointsJsonArray.addAll(orderedVariationPoints);
		return orderedVariationPointsJsonArray;
	}
	
	/**
	 * Returns JSON array of all decorator arguments
	 * 
	 * @param annotationAst - decorator AST from which decorator arguments are extracted
	 * @return JSON array of all decorator arguments
	 */
	private JSONArray getDecoratorArguments(JSONObject annotationAst) {
		JSONArray decoratorArguments;
		JSONObject annotationExpressionLower = (JSONObject) annotationAst.get("expression");
		JSONObject annotationExpression = (JSONObject) annotationExpressionLower.get("expression");
		String annotationName = ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(annotationExpression);
		if (annotationExpressionLower.containsKey("arguments")) {
			decoratorArguments = (JSONArray) annotationExpressionLower.get("arguments");
			return decoratorArguments;
		}
		decoratorArguments = (JSONArray) annotationExpression.get("arguments");
		return decoratorArguments;
	}
	
	/**
	 * Optionally creates negative variation point representation from decorator/annotation
	 * -otherwise returns null 
	 * 
	 * @param annotationAst - decorator AST from which data about decorator/annotation is optionally extracted 
	 * @return the representation of negative variation point extracted from decorator
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private JSONObject extractVariabilityAnnotation(JSONObject annotationAst) throws IOException, InterruptedException {
		String annotationName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(annotationAst);
		if (annotationName == null) {
			System.out.println("Cannot find annotation name in " + annotationAst.toString() + " Skipping...");
			return null;
		}
		String annotationFullName = ASTTextExtractorTools.getFullTextFromAstExpressions(annotationAst);
		JSONArray decoratorArguments = (JSONArray) ((JSONObject) annotationAst.get("expression")).get("arguments");
		JSONObject configurationExpressionJSON = null, configurationExpressionAst;
		String configurationExpressionJSONStr = null;
		
		if (decoratorArguments != null && decoratorArguments.size() > 0) {
			configurationExpressionJSON = ExpressionConverter.getJSONObjectFromVariable((JSONObject) (decoratorArguments).get(0));
			configurationExpressionJSONStr = configurationExpressionJSON.toString();
		}
		configurationExpressionAst = new JSONObject();
		configurationExpressionAst.put("configurationExpression", configurationExpressionJSON);
		configurationExpressionAst.put("configurationExpressionStr", configurationExpressionJSONStr);
		configurationExpressionAst.put("name", annotationName);
		configurationExpressionAst.put("fullName", annotationFullName);
		
		AnnotationExtensionMarker.SystemAnnotationType annotationType 
			= NegativeVariabilityDecoratorConsistenceVerifier.classifyNegativeVariabilitySystemAnnotation(annotationName);
		if (annotationType == null) {
			configurationExpressionAst.put("type", "user"); //user negative variability annotation
		} else {
			configurationExpressionAst.put("type", annotationType.label); //system (potential variation point) negative variability annotation
		}
		return configurationExpressionAst;
	}
	
	/**
	 * Extracts and returns initialized value from class variable represented in AST object
	 * -helps to extract configuration expression (in JSON) from class variable
	 * 
	 * @param classMember - class variable in AST object
	 * @return class variable initialization (stub part)
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static JSONObject getVariableStubPart(JSONObject classMember) throws IOException, InterruptedException {
		String initializedExpressionCodeConstruct= "class DefaultClass { }";
		JSONObject defaultClassAst = (JSONObject) ASTConverterClient.convertFromCodeToASTJSON(initializedExpressionCodeConstruct).get("ast");
		((JSONArray) defaultClassAst.get("statements")).clear();
		((JSONArray) defaultClassAst.get("statements")).add(classMember);
		JSONArray statementsArray = (JSONArray) defaultClassAst.get("statements");
		((JSONObject) ((JSONArray) defaultClassAst.get("statements")).get(0)).remove("modifiers");
		String codeWithVariableExpression = ASTConverterClient.convertFromASTToCode(defaultClassAst.toString());
		codeWithVariableExpression = codeWithVariableExpression.strip();
		if (codeWithVariableExpression.endsWith(";")) { codeWithVariableExpression = codeWithVariableExpression.substring(0, codeWithVariableExpression.length() - 1); }
		return ASTLoader.loadASTFromString(codeWithVariableExpression.substring(codeWithVariableExpression.indexOf('=') + 1));
	}
	
	/**
	 * Harvests positive variation point in class
	 * - must start with content of VariationPointDivisionConfiguration.MARKER_VP_NAME string
	 * 
	 * @param astPart - actually processed AST part
	 * @param astParent - the parent of actually processed AST part
	 * @return harvested system positive variation point from class if found otherwise null
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private JSONObject harvestVariationPointDirectlyInsideClass(JSONObject astPart, JSONObject astParent) throws IOException, InterruptedException {
		JSONObject harvestedVariationPoint;
		if (!astPart.containsKey("body") && !astPart.containsKey("parameters")) {
			String variableAnnotationName = ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(astPart);
			if (variableAnnotationName == null) { return null; } //probably decorator is evaluated
			if (variableAnnotationName.startsWith(VariationPointDivisionConfiguration.MARKER_VP_NAME)) {
				//ASTConverterClient.getFirstMemberFromASTFileFistStatement(astParent);
				harvestedVariationPoint = VPDividedExporter.getVariableStubPart(astPart);
				harvestedVariationPoint.put("variationPointName", variableAnnotationName);
				return harvestedVariationPoint;
			}
		}
		return null;
	}

	/**
	 * Returns text from AST expressions
	 * 
	 * @param astPart - actually processed AST part
	 * @return text from AST expression
	 */
	private String getTextFromAstExpressions(JSONObject astPart) {
		JSONObject annotationExpression = (JSONObject) ((JSONObject) astPart.get("expression")).get("expression");
		return  ASTTextExtractorTools.getTextFromAst(annotationExpression);
	}
	
	/**
	 * Harvests positive variation point located not directly inside the class
	 * - must start with the content of VariationPointDivisionConfiguration.MARKER_VP_NAME string
	 * 
	 * @param astPart - actually processed AST part
	 * @return harvested system positive variation point located not directly inside class if found otherwise null
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private JSONObject harvestVariationPointOutsideFromTheMarker(JSONObject astPart) throws IOException, InterruptedException {
		JSONObject harvestedVariationPoint;
		String variableAnnotationName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(astPart);
		if (variableAnnotationName != null && variableAnnotationName.startsWith(VariationPointDivisionConfiguration.MARKER_VP_NAME)) {
			harvestedVariationPoint = VPDividedExporter.getVariableStubPart(astPart);
			harvestedVariationPoint.put("variationPointName", variableAnnotationName);
			return harvestedVariationPoint;
		}
		return null;
	}
	
	/**
	 * Search and optionally returns information about negative variability marker annotations
	 * 
	 * @param astPart - actually processed AST part with object preferably containing decorators/annotations
	 * @return the array of information about negative variability marker annotations if found otherwise null
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private JSONArray getVariabilityDecoratorSelectionsMarkups(JSONObject astPart) throws IOException, InterruptedException {
		JSONArray processedVariabilityDecorators = null;
		JSONArray annotationToBeProcessed, illegalDecorators = null;
		JSONObject variabilityAnnotation;
		String annotationName;
		boolean isIllegalDecoratorUsed = false;
		boolean isNegativeVariabilityUserAnnotation; // if user annotation is used then its negative variability annotation
		
		if (astPart.containsKey("modifiers") || astPart.containsKey("illegalDecorators")) {
			annotationToBeProcessed = (JSONArray) astPart.get("modifiers");
			illegalDecorators = (JSONArray) astPart.get("illegalDecorators");
			if (annotationToBeProcessed == null) {
				annotationToBeProcessed = (JSONArray) astPart.get("illegalDecorators");
				isIllegalDecoratorUsed = true;
			}  else if (VariationPointDivisionConfiguration.OPTIONALLY_MERGE_ILLEGAL_DECORATORS_DURING_SELECTION) {
				illegalDecorators = (JSONArray) astPart.get("illegalDecorators");
				if (illegalDecorators != null && illegalDecorators.size() != 0) {
					annotationToBeProcessed.addAll(illegalDecorators);
					isIllegalDecoratorUsed = true;
				}
			}

			for (Object decoratorObject: annotationToBeProcessed) {
				annotationName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions((JSONObject) decoratorObject);
				isNegativeVariabilityUserAnnotation = VPDividedExporter.isChosenNegativeVariabilityDecorator(annotationToBeProcessed);
				variabilityAnnotation = this.extractVariabilityAnnotation((JSONObject) decoratorObject);
				
				if (variabilityAnnotation != null) {
					variabilityAnnotation.put("isMarkedAsIllegal", isIllegalDecoratorUsed);
					variabilityAnnotation.put("isNegativeVariabilityUserAnnotation", isNegativeVariabilityUserAnnotation);
					if (processedVariabilityDecorators == null) {
						processedVariabilityDecorators = new JSONArray();
					} 
					processedVariabilityDecorators.add(variabilityAnnotation);
				} 
			}
		}
		return processedVariabilityDecorators;
	}
	
	/**
	 * Decides if at least one decorator belongs to negative variability
	 * 
	 * @param usedDecorators - all associated decorators that annotate particular code fragment
	 * @return true if at least one decorator belongs to negative variability otherwise false
	 */
	private static boolean isChosenNegativeVariabilityDecorator(JSONArray usedDecorators) {
		JSONObject decoratorObject;
		for (Object usedDecoratorObject: usedDecorators) {
			decoratorObject = (JSONObject) usedDecoratorObject;
			if (AnnotationProcessor.isVariabilityConfigurationAnnotation(decoratorObject)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Extracts and optionally returns information about negative variability variation point from decorator/annotation
	 * 
	 * @param astPart - actually processed AST part with object preferably containing decorators/annotations
	 * @return the information about existing negative variability decorators
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private JSONArray findExistingVariabilityDecorator(JSONObject astPart) throws IOException, InterruptedException {
		JSONArray processedVariabilityDecorators = null;
		JSONArray annotationToBeProcessed, illegalDecorators = null;;
		JSONObject variabilityAnnotation;
		String annotationName;
		boolean isIllegalDecoratorUsed = false;
		boolean isNegativeVariabilityUserAnnotation;
		
		if (astPart.containsKey("modifiers") || astPart.containsKey("illegalDecorators")) {
			annotationToBeProcessed = (JSONArray) astPart.get("modifiers");
			if (annotationToBeProcessed == null) {
				annotationToBeProcessed = (JSONArray) astPart.get("illegalDecorators");
				isIllegalDecoratorUsed = true;
			} else if (VariationPointDivisionConfiguration.OPTIONALLY_MERGE_ILLEGAL_DECORATORS_DURING_SELECTION) {
				illegalDecorators = (JSONArray) astPart.get("illegalDecorators");
				if (illegalDecorators != null && illegalDecorators.size() != 0) {
					annotationToBeProcessed.addAll(illegalDecorators);
					isIllegalDecoratorUsed = true;
				}
			}

			for (Object decoratorObject: annotationToBeProcessed) {
				annotationName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions((JSONObject) decoratorObject);
				if (annotationName == null) { continue; } // empty modifier marking export
				if (NegativeVariabilityDecoratorConsistenceVerifier.isNegativeVariabilityAnnotation(annotationName)) {
					isNegativeVariabilityUserAnnotation = VPDividedExporter.isChosenNegativeVariabilityDecorator(annotationToBeProcessed);
					variabilityAnnotation = this.extractVariabilityAnnotation((JSONObject) decoratorObject);
					
					if (variabilityAnnotation != null) {
						variabilityAnnotation.put("isMarkedAsIllegal", isIllegalDecoratorUsed);
						variabilityAnnotation.put("isNegativeVariabilityUserAnnotation", isNegativeVariabilityUserAnnotation);
						
						if (processedVariabilityDecorators == null) {
							processedVariabilityDecorators = new JSONArray();
						} 
						processedVariabilityDecorators.add(variabilityAnnotation);
					} 
				}
			}
		}
	
		return processedVariabilityDecorators;
	}
	
	/**
	 * Harvests and returns information about newly available positive variability variation point
	 * 
	 * @param astPart - actually processed AST part
	 * @param astParent - the parent of actually processed AST part
	 * @param contextStringIdentifier - string with sequence of inner wrapper objects such as classes (in case of class function) and methods
	 * @param originalAstPart - 
	 * @return harvested information about newly available positive variability variation point
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 */
	private JSONObject harvestNewlyAvailableVariationPoint(JSONObject astPart, JSONObject astParent, 
			String contextStringIdentifier, String hierarchicIdentifier, JSONObject originalAstPart) throws IOException, InterruptedException, InvalidSystemVariationPointMarkerException {
		JSONObject harvestedVariationPoint = null;
		JSONArray variabilityDecoratorSelectionMarkups;
		JSONArray markerDeclarationList;
		boolean isClassRelated = false;

		if (astParent.containsKey("members") && astPart.containsKey("parameters")) {
			return null; //CLASS FUNCTION IS NOT MARKER!!!
		} else if (!astParent.containsKey("members") && astParent.containsKey("parameters")) { 
			return null; //FUNCTION IS NOT MARKER!!!
		}  else if (astParent.containsKey("members") && !astParent.containsKey("parameters") && !astPart.containsKey("body")
				&& !astPart.containsKey("parameters") && ((JSONArray) astParent.get("members")).contains(astPart)){
			isClassRelated = true;
			harvestedVariationPoint = this.harvestVariationPointDirectlyInsideClass(astPart, astParent);
			variabilityDecoratorSelectionMarkups = this.getVariabilityDecoratorSelectionsMarkups(astPart);
			if (harvestedVariationPoint != null) {
				harvestedVariationPoint.put("newVPType", AnnotationExtensionMarker.SystemAnnotationType.CLASS_VARIABLE.label);
			} 
		} else if (astPart.containsKey("declarationList")){
			isClassRelated = false;
			markerDeclarationList = (JSONArray) ((JSONObject) astPart.get("declarationList")).get("declarations");
			if (markerDeclarationList == null) { return null; }
			harvestedVariationPoint = this.harvestVariationPointOutsideFromTheMarker((JSONObject) markerDeclarationList.get(0));
			if (harvestedVariationPoint == null) { return null; }
			if (markerDeclarationList.size() != 1) {
				System.out.println(harvestedVariationPoint.toString());
				System.out.println(((JSONObject) markerDeclarationList.get(0)).toString());
				throw new InvalidSystemVariationPointMarkerException(
						"Marker should contain only one argument/parameter! " + markerDeclarationList.toString());
			}
			variabilityDecoratorSelectionMarkups = this.getVariabilityDecoratorSelectionsMarkups(astParent);
			if (harvestedVariationPoint != null) {
				harvestedVariationPoint.put("newVPType", AnnotationExtensionMarker.SystemAnnotationType.VARIABLE.label);
			}
		} else if (astParent.containsKey("members")){
			return null; //CLASS IS NOT MARKER!!!
		} else if (astParent.containsKey("parameters") && astParent.containsKey("modifiers")){
			return null; //FUNCTION PARAMETER IS NOT MARKER!!!
		} else { return null; }
		
		if (harvestedVariationPoint == null) { return null; }
		
		//String variableAnnotationName = ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(astPart);
		
		Long startPosition = (Long) astPart.get("pos");
		Long endPosition = (Long) astPart.get("end");

		Long originalASTStartPosition = (Long) originalAstPart.get("pos");
		Long originalASTEndPosition = (Long) originalAstPart.get("end");
		
		if (this.recursionCycleFinder != null) {
			boolean isInsideRecursion = this.recursionCycleFinder.checkIfIsInsideRecursion(startPosition, endPosition, this.astRoot);
			if (DebugInformation.SHOW_POLLUTING_INFORMATION) { System.out.println("IS inside: " + isInsideRecursion+ ":" + contextStringIdentifier); }
			harvestedVariationPoint.put("isInsideRecursion", isInsideRecursion);
		}
		
		harvestedVariationPoint.put("newVariationPoint", true);
		harvestedVariationPoint.put("hierarchicIdentifier", hierarchicIdentifier);
		
		harvestedVariationPoint.put("startPosition", startPosition);
		harvestedVariationPoint.put("endPosition", endPosition);
		
		
		harvestedVariationPoint.put("originalASTStartPosition", originalASTStartPosition);
		harvestedVariationPoint.put("originalASTEndPosition", originalASTEndPosition);
		
		harvestedVariationPoint.put("classRelated", isClassRelated);
		harvestedVariationPoint.put("contextStringIdentifier", contextStringIdentifier);
		
		//variability-based information
		harvestedVariationPoint.put("variabilitySelections", variabilityDecoratorSelectionMarkups);
		return harvestedVariationPoint;
	}

	/**
	 * Checks for negative or for positive variation point and optionally returns associated data 
	 * - firstly the negative variation point is checked
	 * - if negative variation point is found then associated data are returned
	 * - secondly the positive variation point is checked
	 * - if positive variation point is found then associated data are returned otherwise returns null
	 * 
	 * @param astRoot - the root of processed AST
	 * @param astPart - actually processed AST part
	 * @param astParent - the parent of actually processed AST part
	 * @param contextStringIdentifier - string with sequence of inner wrapper objects such as classes (in case of class function) and methods
	 * @return the negative variation point data or positive variation point data if found otherwise null
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 */
	private JSONObject harvestVariationPoint(JSONObject astRoot, JSONObject astPart, JSONObject astParent, 
			String contextStringIdentifier, String hierarchicIdentifier, JSONObject originalAstPart) throws IOException, InterruptedException, InvalidSystemVariationPointMarkerException {
		JSONObject variationPoint = this.harvestNewlyAvailableVariationPoint(astPart, astParent, contextStringIdentifier, hierarchicIdentifier, originalAstPart);
		if (variationPoint != null) { return variationPoint; }
		variationPoint = this.harvestAlreadyAvailableVariationPoint(astPart, astParent, contextStringIdentifier, hierarchicIdentifier, astRoot, originalAstPart);
		return variationPoint;
	}
	
	/**
	 * Harvests and classify variation point to negative or positive variability if found
	 * -if found the result is harvested and stored to array according to associated variability 
	 * 
	 * @param astRoot - the root of processed AST
	 * @param astPart - actually processed AST part
	 * @param astParent - the parent of actually processed AST part
	 * @param contextStringIdentifier - string with sequence of inner wrapper objects such as classes (in case of class function) and methods
	 * @param positiveVariability - the array with information about positive variability variation points
	 * @param negativeVariability - the array with information about negative variability variation points
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 */
	private void harvestAndClasifyVariationPoint(JSONObject astRoot, JSONObject astPart, JSONObject astParent, 
			String contextStringIdentifier, String hierarchicIdentifier, JSONArray positiveVariability, 
			JSONArray negativeVariability, JSONObject originalAstPart) throws IOException, InterruptedException, InvalidSystemVariationPointMarkerException {
		JSONObject variationPoint = this.harvestNewlyAvailableVariationPoint(astPart, astParent, contextStringIdentifier, hierarchicIdentifier, originalAstPart);
		if (variationPoint != null) { 
			positiveVariability.add(variationPoint);
			return;
		}
		variationPoint = this.harvestAlreadyAvailableVariationPoint(astPart, astParent, contextStringIdentifier, hierarchicIdentifier, astRoot, originalAstPart);
		if (variationPoint != null) {
			negativeVariability.add(variationPoint);
		}
		return;
	}
	
	/**
	 * Extracts affected code according to its boundaries from the AST and associates it with variation point
	 * 
	 * @param harvestedVariationPoint - extracted/associated data from particular variation point
	 * @param astRoot - the root of processed AST
	 * @param astParent - the parent of actually processed AST part
	 */
	private void getAffectedCodeFromParent(JSONObject harvestedVariationPoint, JSONObject astRoot, JSONObject astParent) {
		Long parentStartInitializerPosition = (Long) astParent.get("pos");
		Long parentEndInitializerPosition = (Long) astParent.get("end");
		String affectedCodeParent = ((String) astRoot.get("text")).substring(
				parentStartInitializerPosition.intValue(), parentEndInitializerPosition.intValue()).strip();
		harvestedVariationPoint.put("affectedCodeParent", affectedCodeParent);
	}

	/**
	 * Harvests and returns the information associated with existing negative variability variation point 
	 * 
	 * @param astPart - actually processed AST part
	 * @param astParent - the parent of actually processed AST part
	 * @param contextStringIdentifier - string with sequence of inner wrapper objects such as classes (in case of class function) and methods
	 * @param astRoot - the root of processed AST
	 * @param originalAstPart - 
	 * @return the harvested information associated with existing negative variability variation point
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private JSONObject harvestAlreadyAvailableVariationPoint(JSONObject astPart, JSONObject astParent, 
			String contextStringIdentifier, String hierarchicIdentifier, JSONObject astRoot, JSONObject originalAstPart) throws IOException, InterruptedException {
		JSONObject harvestedVariationPoint = new JSONObject();
		JSONArray variabilityDecoratorSelectionMarkups;
		String variableAnnotationName, affectedCode;
		boolean isClassRelated = false;
		Long startInitializerPosition, endInitializerPosition;
		
		if (astParent.containsKey("members") && astPart.containsKey("parameters") && astPart.containsKey("name")) { //GET ANNOTATIONS FOR CLASS FUNCTION DECORATORS
			isClassRelated = true;
			variabilityDecoratorSelectionMarkups = this.findExistingVariabilityDecorator(astPart);
			harvestedVariationPoint.put("annotationVPType", AnnotationExtensionMarker.SystemAnnotationType.CLASS_FUNCTION.label);
			variableAnnotationName = ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(astPart);
			
			this.getAffectedCodeFromParent(harvestedVariationPoint, astRoot, astParent);
			startInitializerPosition = (Long) astPart.get("pos");
			endInitializerPosition = (Long) astPart.get("end");
		} else if (astParent.containsKey("members") && !astPart.containsKey("parameters") && astPart.containsKey("name")) { //GET ANNOTATIONS FOR CLASS VARIABLES
			isClassRelated = true;
			variabilityDecoratorSelectionMarkups = this.findExistingVariabilityDecorator(astPart);
			harvestedVariationPoint.put("annotationVPType", AnnotationExtensionMarker.SystemAnnotationType.CLASS_VARIABLE.label);
			variableAnnotationName = ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(astPart);
		
			startInitializerPosition = (Long) astPart.get("pos");
			endInitializerPosition = (Long) astPart.get("end");
		} else if (!astParent.containsKey("members") && astPart.containsKey("parameters") && astPart.containsKey("body")) { //GET ANNOTATIONS FOR FUNCTION DECORATORS //directly from part
			if (!astPart.containsKey("name")) { // CLASS CONSTRUCTOR
				isClassRelated = true;
				variabilityDecoratorSelectionMarkups = this.findExistingVariabilityDecorator(astPart);
				harvestedVariationPoint.put("annotationVPType", AnnotationExtensionMarker.SystemAnnotationType.CLASS_FUNCTION.label);
				variableAnnotationName = "constructor";
				
				this.getAffectedCodeFromParent(harvestedVariationPoint, astRoot, astParent);
			} else {
				isClassRelated = false;
				variabilityDecoratorSelectionMarkups = this.findExistingVariabilityDecorator(astPart);
				harvestedVariationPoint.put("annotationVPType", AnnotationExtensionMarker.SystemAnnotationType.FUNCTION.label);
				variableAnnotationName = (String) ((JSONObject) astPart.get("name")).get("escapedText");//ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(astParent);
				if (variableAnnotationName == null || variableAnnotationName.equals("")) {
					System.out.println("Error: negative variability function declaration wrongly processed!");
					System.exit(5);
				}
			}
			
			startInitializerPosition = (Long) astPart.get("pos");
			endInitializerPosition = (Long) astPart.get("end");
		}  else if (astParent.containsKey("declarationList") && astPart.containsKey("declarations")){	//GET ANNOTATION FOR GLOBAL CONTEXT VARIABLES
			isClassRelated = false;
			variabilityDecoratorSelectionMarkups = this.findExistingVariabilityDecorator(astParent); // HAVE TO BE PARENT - contains declaration List
			harvestedVariationPoint.put("annotationVPType", AnnotationExtensionMarker.SystemAnnotationType.VARIABLE.label);
			variableAnnotationName = ASTTextExtractorTools.getTextFromTheDeclarations(astPart);
			if (variableAnnotationName == null || variableAnnotationName.equals("")) {
				System.out.println("Error: negative variability variable declaration wrongly processed!");
				System.exit(5);
			}
			
			startInitializerPosition = (Long) astPart.get("pos");
			endInitializerPosition = (Long) astPart.get("end");
		} else if (astPart.containsKey("members") && astPart.containsKey("name")){	//CLASS ANNOTATION - processed directly
			isClassRelated = true;
			variabilityDecoratorSelectionMarkups = this.findExistingVariabilityDecorator(astPart);
			harvestedVariationPoint.put("annotationVPType", AnnotationExtensionMarker.SystemAnnotationType.CLASS.label);
			
			variableAnnotationName = ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(astPart);
			if (variableAnnotationName == null || variableAnnotationName.equals("")) {
				System.out.println("Error: negative variability class declaration wrongly processed!");
				System.exit(5);
			}
			
			startInitializerPosition = (Long) astPart.get("pos");
			endInitializerPosition = (Long) astPart.get("end");
		} else if (astPart.containsKey("parameters")){	//PARAMETER ANNOTATION - processed directly
			if (VariationPointDivisionConfiguration.ALLOW_NEGATIVE_VARIABILITY_PARAMETERS) {
				isClassRelated = false; //any parameter
				variabilityDecoratorSelectionMarkups = this.findExistingVariabilityDecorator(astPart);
				harvestedVariationPoint.put("annotationVPType", AnnotationExtensionMarker.SystemAnnotationType.PARAMETER.label);
				variableAnnotationName = ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(astPart);
				if (variableAnnotationName == null || variableAnnotationName.equals("")) {
					System.out.println("Error: negative variability parameter wrongly processed!");
					System.exit(5);
				}
			} else {
				variableAnnotationName = null;
				variabilityDecoratorSelectionMarkups = null;
			}
			
			startInitializerPosition = (Long) astPart.get("pos");
			endInitializerPosition = (Long) astPart.get("end");
		} else {
			//System.out.println("-------------------------------------------------");
			//System.out.println(astParent.toString());
			//System.out.println(astPart.toString());
			return null; 
		}

		//variability annotation should be added in previous step according requirements
		if (variabilityDecoratorSelectionMarkups == null) {  return null; }
		
		Integer startPosition = startInitializerPosition.intValue();
		Integer endPosition = endInitializerPosition.intValue();
		if (this.recursionCycleFinder != null) {
			boolean isInsideRecursion = this.recursionCycleFinder.checkIfIsInsideRecursion(startPosition, endPosition, this.astRoot);
			harvestedVariationPoint.put("isInsideRecursion", isInsideRecursion);
			if (DebugInformation.SHOW_POLLUTING_INFORMATION) { System.out.println("IS inside: " + isInsideRecursion+ ":" + contextStringIdentifier);}
		}
		
		Long originalASTStartPosition = (Long) originalAstPart.get("pos");
		Long originalASTEndPosition = (Long) originalAstPart.get("end");
		harvestedVariationPoint.put("hierarchicIdentifier", hierarchicIdentifier);
		harvestedVariationPoint.put("originalASTStartPosition", startPosition);
		harvestedVariationPoint.put("originalASTEndPosition", endPosition);
		
		harvestedVariationPoint.put("newVariationPoint", false);
		harvestedVariationPoint.put("variationPointName", variableAnnotationName);
		harvestedVariationPoint.put("startPosition", startPosition);
		harvestedVariationPoint.put("endPosition", endPosition);
		affectedCode = ((String) astRoot.get("text")).substring(startInitializerPosition.intValue(), endInitializerPosition.intValue()).strip();
		harvestedVariationPoint.put("affectedCode", affectedCode);
		harvestedVariationPoint.put("classRelated", isClassRelated);
		harvestedVariationPoint.put("contextStringIdentifier", contextStringIdentifier);
		
		//variability-based information
		harvestedVariationPoint.put("variabilitySelections", variabilityDecoratorSelectionMarkups);
		return harvestedVariationPoint;
	}
			
	/**
	 * Updates context identifier string if inner structure is encountered such as method or class
	 * - the dot notation is used for this purpose
	 * 
	 * @param contextStringIdentifier - string with sequence of inner wrapper objects such as classes (in case of class function) and methods
	 * @param astPart - actually processed AST part
	 * @return the optionally updated context identifier string according to encountered inner structures such as methods or classes
	 */
	private String updateContextStringIdentifier(String contextStringIdentifier, JSONObject astPart) {
		String capturedText =  ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(astPart);
		if (capturedText != null && !astPart.containsKey("fileName")) {
			if (contextStringIdentifier.equals("")) {
				return ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(astPart);
			}
			return contextStringIdentifier + "." + ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(astPart);
		}
		return contextStringIdentifier;
	}
	
	/**
	 * Checks positive variability marker as part of destination AST
	 * 
	 * @param entryJSONObject
	 * @return
	 */
	private boolean checkPositiveVariabilityMarker(JSONObject entryJSONObject) {
		JSONObject declarationList = (JSONObject) entryJSONObject.get("declarationList");
		JSONObject declaration;
		String declarationName;
		if (entryJSONObject.containsKey("declarationList")) {
			for (Object declarationObject: (JSONArray) declarationList.get("declarations")) {
				declaration = (JSONObject) declarationObject;
				JSONObject textObject = ((JSONObject) ((JSONObject) declaration).get("name"));
				if (textObject.containsKey("escapedText")) {
					declarationName = (String) textObject.get("escapedText");
				} else {
					declarationName = (String) textObject.get("text");
				}
				if (declarationName.startsWith(VariationPointDivisionConfiguration.MARKER_VP_NAME)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Recursively harvests positive and negative variation points from divisioned AST
	 * 
	 * @param astRoot - the root of processed AST
	 * @param astPart - actually processed AST part
	 * @param astParent - the parent of actually processed AST part
	 * @param contextStringIdentifier - string with sequence of inner wrapper objects such as classes (in case of class function) and methods
	 * @param harvestedVariationPoints - the list of representations where each has associated information about positive and negative variability variation points
	 * @param originalAstPart - 
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 */
	private void harvestVariationPoints(JSONObject astRoot, JSONObject astPart, JSONObject astParent,
			String contextStringIdentifier, String hierarchicIdentifier, List<JSONObject> harvestedVariationPoints, JSONObject originalAstPart) throws IOException, InterruptedException, InvalidSystemVariationPointMarkerException {
		String key;
		if (astPart == null) { return; }
		contextStringIdentifier = this.updateContextStringIdentifier(contextStringIdentifier, astPart);
		hierarchicIdentifier = HierarchyEntityConstructor.createLabelForParticularEntityInAST(hierarchicIdentifier, astPart);
		JSONObject variationPointData = this.harvestVariationPoint(
				astRoot, astPart, astParent, contextStringIdentifier, hierarchicIdentifier, originalAstPart);
		if (variationPointData != null) { harvestedVariationPoints.add(variationPointData); }
	
		Object entryValue, originalAstValue;
		JSONObject entryJSONObject, originalAstEntryJSONObject;
		int index;
		for(Object entryKey: astPart.keySet()) {
			key = (String) entryKey;
			entryValue = astPart.get(key);
			Set<String> resultingSet = new HashSet<String>(astPart.keySet());
			resultingSet.removeAll(originalAstPart.keySet());
			if (resultingSet.isEmpty() && originalAstPart.containsKey(key)) {
				if (entryValue instanceof JSONObject) {
					resultingSet = new HashSet<String>(((JSONObject) entryValue).keySet());
					resultingSet.removeAll(((JSONObject) originalAstPart.get(key)).keySet());
					if (resultingSet.isEmpty()) {
						originalAstValue = originalAstPart.get(key);
					} else {
						originalAstValue = originalAstPart;
					}
				} else {
					originalAstValue = originalAstPart.get(key);
				}
			} else {
				originalAstValue = originalAstPart;
			}
			//if (key.equals("illegalDecorators")) {	continue; }
			if (entryValue instanceof JSONObject) {
				entryJSONObject = (JSONObject) entryValue;
				this.harvestVariationPoints(astRoot, entryJSONObject, astPart, 
						contextStringIdentifier, hierarchicIdentifier, 
						harvestedVariationPoints, (JSONObject) originalAstValue);
			} else if(entryValue instanceof JSONArray) {
				index = 0;
				for (Object arrayPart: ((JSONArray) entryValue)) {
					entryJSONObject = (JSONObject) arrayPart;
				
					if (originalAstValue instanceof JSONArray && ((JSONArray) originalAstValue).size() != 0) {
						if (index >= ((JSONArray) originalAstValue).size()) { index = ((JSONArray) originalAstValue).size() - 1; }
						originalAstEntryJSONObject = (JSONObject) ((JSONArray) originalAstValue).get(index);
						if (this.checkPositiveVariabilityMarker(entryJSONObject)) { index = index + 1; }
					} else {
						originalAstEntryJSONObject = originalAstPart;
					}
					this.harvestVariationPoints(astRoot, entryJSONObject, astPart, 
							contextStringIdentifier, hierarchicIdentifier, 
							harvestedVariationPoints, originalAstEntryJSONObject);
				}
			}
		}
	}
	
	/**
	 * Repeatedly converts AST to code and back to update AST starting and ending positions
	 * 
	 * @param astRoot - the root of processed AST
	 * @return the root of AST converted to and back 
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private JSONObject convertRepeatedlyToVerifyPositionNumbering(JSONObject astRoot) throws IOException, InterruptedException {
		DefaultEvolutionCore.clearNegativeVariabilityAnnotationsAndMarkers(astRoot); // otherwise tree cannot be converted
		String convertedScript = ASTConverterClient.convertFromASTToCode(astRoot.toString());
		return ASTConverterClient.convertFromCodeToASTJSON(convertedScript);
	}	
}
