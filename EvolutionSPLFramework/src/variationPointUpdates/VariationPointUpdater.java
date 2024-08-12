package variationPointUpdates;

import java.io.IOException;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import codeContext.processors.ASTTextExtractorTools;
import codeContext.processors.NotFoundVariableDeclaration;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import divisioner.VariationPointDivisionConfiguration;
import splEvolutionCore.SPLEvolutionCore;
import splEvolutionCore.candidateSelector.AlreadyProvidedArgumentInConfigurationExpressionPlace;
import splEvolutionCore.candidateSelector.NegativeVariationPointCandidate;
import splEvolutionCore.candidateSelector.valueAssignment.cleaning.AnnotationsFromCodeRemover;
import variationPointsVisualization.AnnotationExtensionMarker;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;


/**
 * Updates user negative variability annotation according to selected/deselected variation points 
 * 
 * @author Jakub Perdek
 *
 */
public class VariationPointUpdater {

	/**
	 * Creates variationPointUpdater instance
	 */
	public VariationPointUpdater() {}

	
	/**
	 * Checks type of given annotation and created given one
	 * 
	 * @param astRoot - the root of AST
	 * @param astPart - actually processed AST element
	 * @param astParent - the parent of actually processed AST element
	 * @param foundNegativeVariationPointCandidate - selected variation point candidate who is annotated
	 * @param associatedType - type of candidate to select appropriate user negative variability annotation
	 * @param isUserAnnotation - to decide which type of annotations should be inserted - if true user annotations helps to preserve content further for next iterations otherwise system annotations allows to mark variable places that are found and treated
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 * @throws AlreadyProvidedArgumentInConfigurationExpressionPlace
	 * @throws NotFoundVariableDeclaration
	 */
	private void processAnnotationAccordingType(JSONObject astRoot, JSONObject astPart, JSONObject astParent,
			NegativeVariationPointCandidate foundNegativeVariationPointCandidate, 
			AnnotationExtensionMarker.SystemAnnotationType associatedType, boolean isUserAnnotation) throws IOException, InterruptedException, 
				DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation, 
				AlreadyProvidedArgumentInConfigurationExpressionPlace, NotFoundVariableDeclaration {
		if (associatedType == AnnotationExtensionMarker.SystemAnnotationType.FUNCTION) {
			AnnotationInjector.processAnnotationForClassFunctionsInAstPart(astPart, astParent, isUserAnnotation);
		} else if (associatedType == AnnotationExtensionMarker.SystemAnnotationType.CLASS_VARIABLE) {
			AnnotationInjector.processAnnotationForClassVariablesInAstPart(astPart, astParent, isUserAnnotation);
		} else if (associatedType == AnnotationExtensionMarker.SystemAnnotationType.CLASS_FUNCTION) {
			AnnotationInjector.processAnnotationForClassFunctionsInAstPart(astPart, astParent, isUserAnnotation);
		} else if (associatedType ==  AnnotationExtensionMarker.SystemAnnotationType.VARIABLE) {
			AnnotationInjector.processAnnotationForVariablesInAstPart(astRoot, astPart, isUserAnnotation);
		} else if (associatedType == AnnotationExtensionMarker.SystemAnnotationType.PARAMETER) {
			AnnotationInjector.processAnnotationForParameterInAstPart(astPart, astParent, isUserAnnotation);
		} else if (associatedType == AnnotationExtensionMarker.SystemAnnotationType.CLASS) {
			AnnotationInjector.processAnnotationForClassInAstPart(astPart, foundNegativeVariationPointCandidate, isUserAnnotation);
		}
	}
	
	/**
	 * Identifies if candidates match with selected variation point and then inserts annotation
	 * -optionally removes negative variability annotations
	 *  
	 * @param astRoot - the root of AST
	 * @param astPart - actually processed AST element
	 * @param astParent - the parent of actually processed AST element
	 * @param chosenVariationPointsMap - selected variation points that should be updated (their names are mapped to their representation) 
	 * @param associatedType - type of candidate to select appropriate user negative variability annotation
	 * @param isUserAnnotation - to decide which type of annotations should be inserted - if true user annotations helps to preserve content further for next iterations otherwise system annotations allows to mark variable places that are found and treated
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 * @throws AlreadyProvidedArgumentInConfigurationExpressionPlace
	 * @throws NotFoundVariableDeclaration
	 */
	private void identifyCandidateAndInsertUserAnnotation(JSONObject astRoot, JSONObject astPart, JSONObject astParent,
			Map<String, NegativeVariationPointCandidate> chosenVariationPointsMap, 
			AnnotationExtensionMarker.SystemAnnotationType associatedType, boolean isUserAnnotation) throws IOException, InterruptedException, 
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation, 
			AlreadyProvidedArgumentInConfigurationExpressionPlace, NotFoundVariableDeclaration {
		String actualAstIdentifier = NegativeVariationPointCandidate.getIdentifierFromAst(astPart);
		NegativeVariationPointCandidate foundNegativeVariationPointCandidate = chosenVariationPointsMap.get(actualAstIdentifier);
		if (foundNegativeVariationPointCandidate != null) {
			//clear annotations
			if (!VariationPointDivisionConfiguration.REMOVE_ALL_VARIABILITY_ANNOTATIOS_BEFORE_UPDATE) {
				AnnotationsFromCodeRemover.removeActualLayerVariabilityAnnotations(astPart);
			}
			
			this.processAnnotationAccordingType(astRoot, astPart, astParent, foundNegativeVariationPointCandidate, associatedType, isUserAnnotation);
		} else {
			//clear annotations - especially removed ones
			if (!VariationPointDivisionConfiguration.REMOVE_ALL_VARIABILITY_ANNOTATIOS_BEFORE_UPDATE) {
				AnnotationsFromCodeRemover.removeActualLayerVariabilityAnnotations(astPart);
			}
		}
	}
	
	/**
	 * Updates the variation point with user annotation on proper place of AST
	 * 
	 * @param astPart - actually processed AST element
	 * @param astParent - the parent of actually processed AST element
	 * @param contextStringIdentifier - identifier that reflects position of given entity inside other entities
	 * @param astRoot - the root of AST
	 * @param chosenVariationPointsMap - selected variation points that should be updated (their names are mapped to their representation) 
	 * @param isUserAnnotation - to decide which type of annotations should be inserted - if true user annotations helps to preserve content further for next iterations otherwise system annotations allows to mark variable places that are found and treated
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 * @throws AlreadyProvidedArgumentInConfigurationExpressionPlace
	 * @throws NotFoundVariableDeclaration
	 */
	private void updateVariationPoint(JSONObject astPart, JSONObject astParent, 
			String contextStringIdentifier, JSONObject astRoot, 
			Map<String, NegativeVariationPointCandidate> chosenVariationPointsMap, boolean isUserAnnotation) throws IOException, 
			InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation, 
			AlreadyProvidedArgumentInConfigurationExpressionPlace, NotFoundVariableDeclaration {
		if (astParent.containsKey("members") && astPart.containsKey("parameters") && astPart.containsKey("name")) { //GET ANNOTATIONS FOR CLASS FUNCTION DECORATORS
			this.identifyCandidateAndInsertUserAnnotation(astRoot, astPart, astParent, chosenVariationPointsMap, AnnotationExtensionMarker.SystemAnnotationType.CLASS_FUNCTION, isUserAnnotation);
		} else if (astParent.containsKey("members") && !astPart.containsKey("parameters") && astPart.containsKey("name")) { //GET ANNOTATIONS FOR CLASS VARIABLES
			this.identifyCandidateAndInsertUserAnnotation(astRoot,astPart, astParent, chosenVariationPointsMap, AnnotationExtensionMarker.SystemAnnotationType.CLASS_VARIABLE, isUserAnnotation);
		} else if (!astPart.containsKey("members") && astPart.containsKey("parameters") && astPart.containsKey("body")) { //GET ANNOTATIONS FOR FUNCTION DECORATORS //directly from part
			if (!astPart.containsKey("name")) { // CLASS CONSTRUCTOR
				this.identifyCandidateAndInsertUserAnnotation(astRoot, astPart, astParent, chosenVariationPointsMap, AnnotationExtensionMarker.SystemAnnotationType.CLASS_FUNCTION, isUserAnnotation);
			} else {
				this.identifyCandidateAndInsertUserAnnotation(astRoot, astPart, astParent, chosenVariationPointsMap, AnnotationExtensionMarker.SystemAnnotationType.FUNCTION, isUserAnnotation);
			}
		}  else if (astParent.containsKey("declarationList") && astPart.containsKey("declarations")){	//GET ANNOTATION FOR GLOBAL CONTEXT VARIABLES
			this.identifyCandidateAndInsertUserAnnotation(astRoot, astPart, astParent, chosenVariationPointsMap, AnnotationExtensionMarker.SystemAnnotationType.VARIABLE, isUserAnnotation);
		} else if (astPart.containsKey("members") && astPart.containsKey("name")){	//CLASS ANNOTATION - processed directly
			this.identifyCandidateAndInsertUserAnnotation(astRoot, astPart, astParent, chosenVariationPointsMap, AnnotationExtensionMarker.SystemAnnotationType.CLASS, isUserAnnotation);
		} else if (astPart.containsKey("parameters")){	//PARAMETER ANNOTATION - processed directly
			if (VariationPointDivisionConfiguration.ALLOW_NEGATIVE_VARIABILITY_PARAMETERS) {
				this.identifyCandidateAndInsertUserAnnotation(astRoot, astPart, astParent, chosenVariationPointsMap, AnnotationExtensionMarker.SystemAnnotationType.PARAMETER, isUserAnnotation);
			} 
		} else {
			return; 
		}
	}
	
	/**
	 * Updates identifier that reflects position of given entity inside other entities separated with "." notation
	 * 
	 * @param contextStringIdentifier - context identifier from previous iterations
	 * @param astPart - part of AST from which the name should be/is extracted and updated
	 * @return string representation of updated context identifier
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
	 * Iterates over whole AST tree and performs updates related to negative variability with user annotations
	 * 
	 * @param astRoot - the root of AST
	 * @param astPart - actually processed AST element
	 * @param astParent - the parent of actually processed AST element
	 * @param previousParent - the grand parent of actually processed AST element
	 * @param contextStringIdentifier - identifier that reflects position of given entity inside other entities
	 * @param chosenVariationPoints - selected variation points that should be updated (their names are mapped to their representation) 
	 * @param isUserAnnotation - to decide which type of annotations should be inserted - if true user annotations helps to preserve content further for next iterations otherwise system annotations allows to mark variable places that are found and treated
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 * @throws AlreadyProvidedArgumentInConfigurationExpressionPlace
	 * @throws NotFoundVariableDeclaration
	 */
	private void updateAstAccordingToNegativeVariabilitySelections(
			JSONObject astRoot, JSONObject astPart, 
			JSONObject astParent, JSONObject previousParent, 
			String contextStringIdentifier, 
			Map<String, NegativeVariationPointCandidate> chosenVariationPoints, boolean isUserAnnotation) throws IOException, 
				InterruptedException, InvalidSystemVariationPointMarkerException, 
				DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation, 
				AlreadyProvidedArgumentInConfigurationExpressionPlace, NotFoundVariableDeclaration {
		String key;
		if (astPart == null) { return; }
		contextStringIdentifier = this.updateContextStringIdentifier(contextStringIdentifier, astPart);
		
		this.updateVariationPoint(astPart, astParent, contextStringIdentifier, astRoot, chosenVariationPoints, isUserAnnotation);
		Object entryValue;
		JSONObject entryJSONObject;
		for(Object entryKey: astPart.keySet()) {
			key = (String) entryKey;
			entryValue = astPart.get(key);
			//if (key.equals("illegalDecorators")) { continue; }
			if (entryValue instanceof JSONObject) {
				entryJSONObject = (JSONObject) entryValue;
				this.updateAstAccordingToNegativeVariabilitySelections(
						astRoot, entryJSONObject, astPart, astParent, contextStringIdentifier, chosenVariationPoints, isUserAnnotation);
			} else if(entryValue instanceof JSONArray) {
				for (Object arrayPart: ((JSONArray) entryValue)) {
					entryJSONObject = (JSONObject) arrayPart;
					this.updateAstAccordingToNegativeVariabilitySelections(
							astRoot, entryJSONObject, astPart, astParent, contextStringIdentifier, chosenVariationPoints, isUserAnnotation);
				}
			}
		}
	}
	
	/**
	 * Performs updates according to negative variability selections
	 * -inserts user annotations into selected places
	 * 
	 * @param splAstTree - the root of processed AST
	 * @param chosenVariationPoints - selected variation points that serve should be updated
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation - application is duplicated
	 * @throws AlreadyProvidedArgumentInConfigurationExpressionPlace
	 * @throws NotFoundVariableDeclaration
	 */
	public void updateAstAccordingToNegativeVariabilitySelections(
			JSONObject splAstTree, Map<String, NegativeVariationPointCandidate> chosenVariationPoints) throws IOException, 
			InterruptedException, InvalidSystemVariationPointMarkerException, DifferentAnnotationTypesOnTheSameVariationPoint, 
			DuplicatedAnnotation, AlreadyProvidedArgumentInConfigurationExpressionPlace, NotFoundVariableDeclaration {

		this.updateAstAccordingToNegativeVariabilitySelections(splAstTree, splAstTree, 
				splAstTree, splAstTree, "", chosenVariationPoints, true);
	}
}
