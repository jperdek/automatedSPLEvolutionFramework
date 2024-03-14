package positiveVariabilityManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import codeContext.processors.ASTTextExtractorTools;
import codeContext.processors.export.ExportLocationAggregation;
import dataRepresentationsExtensions.DataRepresentationPerspective;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import divisioner.VariationPointDivisionConfiguration;
import evolutionSimulation.iteration.AlreadyMappedVariationPointContentsInjection;
import evolutionSimulation.productAssetsInitialization.UnknownResourceToProcessException;
import positiveVariabilityManagement.fragmentManagement.CodeIncrementGranularityManagementStrategy;
import positiveVariabilityManagement.fragmentManagement.model.CodeFragment;
import splEvolutionCore.DebugInformation;
import splEvolutionCore.SPLEvolutionCore;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidate;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;
import splEvolutionCore.candidateSelector.valueAssignment.AssignedValue;
import splEvolutionCore.candidateSelector.valueAssignment.featureSelectionStrategies.constructSelection.FeatureConstructsSelectionStrategy;
import splEvolutionCore.candidateSelector.valueAssignment.featureSelectionStrategies.templateSelection.FeatureSelectionStrategy;
import splEvolutionCore.derivation.DerivationResourcesManager;
import splEvolutionCore.derivation.VariationPointConjunctor;


/**
 * Functionality to synthesize the product with new functionality incorporated into existing one
 * The core of synthesis process of functionality/features related to positive variability
 * 
 * @author Jakub Perdek
 *
 */
public class NewContextsSynthesizer {

	/**
	 * The strategy to select positive variability variation points to be updated
	 */
	private FeatureSelectionStrategy featureSelectionStrategy;
	
	/**
	 * The strategy to select code constructs in place of variation points
	 */
	private FeatureConstructsSelectionStrategy featureConstructsSelectionStrategy;
	
	/**
	 * The strategy to manage code synthesis into more modular units
	 */
	private CodeIncrementGranularityManagementStrategy codeIncrementGranularityManagementStrategy;
	
	/**
	 * The strategy to select constructs across various variation points
	 */
	private SelectionOfConstructsAcrossSelectedVariationPointsStrategies selectionOfConstructsAcrossSelectedVariationPointsStrategy;
	
	/**
	 * The resource manager holding resources for derivation process
	 */
	private DerivationResourcesManager derivationResourcesManager;
	
	/**
	 * The name of synthesized content
	 */
	private String syntetizedContentName;
	
	/**
	 * 
	 */
	private DataRepresentationPerspective dataRepresentationPerspective;
	
	
	/**
	 * Instantiates the functionality for the synthesis of the product with new functionality
	 * 
	 * @param featureSelectionStrategy - the strategy to select positive variability variation points to be updated
	 * @param featureConstructsSelectionStrategy - the strategy to select code constructs in place of variation points
	 * @param codeIncrementGranularityManagementStrategy - the strategy to manage code synthesis into more modular units
	 * @param selectionOfConstructsSelectionStrategies - the strategy to select constructs across various variation points
	 * @param derivationResourcesManager - the resource manager holding resources for derivation process
	 * @param syntetizedContentName - the name of synthesized content
	 */
	public NewContextsSynthesizer(FeatureSelectionStrategy featureSelectionStrategy, 
			FeatureConstructsSelectionStrategy featureConstructsSelectionStrategy,
			CodeIncrementGranularityManagementStrategy codeIncrementGranularityManagementStrategy,
			SelectionOfConstructsAcrossSelectedVariationPointsStrategies selectionOfConstructsSelectionStrategies,
			DerivationResourcesManager derivationResourcesManager, String syntetizedContentName) {
		this.derivationResourcesManager = derivationResourcesManager;
		this.syntetizedContentName = syntetizedContentName;
		this.featureSelectionStrategy = featureSelectionStrategy;
		this.featureConstructsSelectionStrategy = featureConstructsSelectionStrategy;
		this.selectionOfConstructsAcrossSelectedVariationPointsStrategy = selectionOfConstructsSelectionStrategies;
		this.codeIncrementGranularityManagementStrategy = codeIncrementGranularityManagementStrategy;
		
		this.dataRepresentationPerspective = new DataRepresentationPerspective();
	}
	
	/**
	 * Creates/models code contexts for further synthesis 
	 * -selects variation points which should be updated and particular construct/constructs for each of them
	 * -the feature selection strategy is used for this purpose
	 * 
	 * 
	 * @param positiveVariationPointCandidatesTemplates - the list of features/positive variation points representation that will be selected
	 * @return the map of variation point identifier to functionality that can be injected into this particular variation points
	 * 
	 * @throws AlreadyMappedVariationPointContentsInjection
	 */
	private Map<String, VariationPointContentsInjection> selectContexts(
			List<PositiveVariationPointCandidateTemplates> positiveVariationPointCandidatesTemplates) throws AlreadyMappedVariationPointContentsInjection {
		Map<String, VariationPointContentsInjection> 
			functionalityOnVarPointsToPossibilitiesMap = new HashMap<String, VariationPointContentsInjection>();
		List<CodeFragment> functionalityOnVarPoints;
		List<PositiveVariationPointCandidateTemplates> selectedTemplates = 
				this.featureSelectionStrategy.selectFeatures(positiveVariationPointCandidatesTemplates);
		PositiveVariationPointCandidate positiveVariationPointConstructCandidate;
		VariationPointContentsInjection variationPointContentInjection;
		JSONObject actuallyProcessedVariationPointData;
		String variationPointIDName;
	
		for (PositiveVariationPointCandidateTemplates selectedTemplate: selectedTemplates) {
			positiveVariationPointConstructCandidate = selectedTemplate.getAssociatedPositiveVariabilityConstructsCandidate();
			
			functionalityOnVarPoints = this.selectConstructs(positiveVariationPointConstructCandidate, selectedTemplate);
			if (DebugInformation.PROCESS_STEP_INFORMATION) { System.out.println("Constructs selected: " + functionalityOnVarPoints.size()); }
			variationPointContentInjection = new VariationPointContentsInjection(selectedTemplate, functionalityOnVarPoints);
			if (variationPointContentInjection.getCodeFragments().size() == 0) { continue; }
			actuallyProcessedVariationPointData = selectedTemplate.getVariationPointData();
			variationPointIDName = (String) actuallyProcessedVariationPointData.get("variationPointName");

			if (variationPointIDName != null && !functionalityOnVarPointsToPossibilitiesMap.containsKey(variationPointIDName)) {
				functionalityOnVarPointsToPossibilitiesMap.put(variationPointIDName, variationPointContentInjection);
			} else {
				throw new AlreadyMappedVariationPointContentsInjection ("Variation point id: " + variationPointIDName + " is used.");
			}
		}
		return functionalityOnVarPointsToPossibilitiesMap;
	}
	
	/**
	 * Selects constructs that can be substituted for processed/actually focused variation point
	 * 
	 * @param positiveVariationPointConstructCandidate - the entity containing code construct candidates that can enhance existing functionality in place of actually focused variation point
	 * @param selectedTemplate - features/positive variation points representation from which available constructs are extracted/obtained
	 * @return the list of code fragments for further selections and synthesis, which holding all decisions (selections, structural information)
	 */
	private List<CodeFragment> selectConstructs(PositiveVariationPointCandidate positiveVariationPointConstructCandidate, 
			PositiveVariationPointCandidateTemplates selectedTemplate) {
		List<CodeFragment> associatedGranularityShapedCodeFragments = new ArrayList<CodeFragment>();
		CodeFragment granularityShapedCodeFragment;
		if (positiveVariationPointConstructCandidate.getPositiveVariationPointConstructs().size() == 0) { 
			System.out.println("No positive variation point constructs are available!"); 
		}
		List<List<Entry<String, Map<String, AssignedValue>>>> positiveVariationPointConstructs = this.featureConstructsSelectionStrategy.selectFeaturesAllCases(
				positiveVariationPointConstructCandidate.getPositiveVariationPointConstructs());
		if (DebugInformation.PROCESS_STEP_INFORMATION && positiveVariationPointConstructs.size() == 0) { 
			System.out.println("No constructs selected!"); 
		}
		
		// coe granularity is handled in place of one variable point/positive variation point candidate template
		for(List<Entry<String, Map<String, AssignedValue>>> selectedFeatureConstructs: positiveVariationPointConstructs) {
			granularityShapedCodeFragment = this.codeIncrementGranularityManagementStrategy.associateConstructsTogether(
					selectedFeatureConstructs, selectedTemplate);
			if (DebugInformation.SHOW_SELECTED_SYNTESIZED_CODE_CONSTRUCTS) { this.showPrintedCode(granularityShapedCodeFragment); }
			associatedGranularityShapedCodeFragments.add(granularityShapedCodeFragment);
		}
		return associatedGranularityShapedCodeFragments;
	}
	
	/**
	 * Prints/outputs the code as string from the code fragment - CodeFragmet instance
	 * 
	 * @param granularityShapedCodeFragment - the instance with information about particular code fragment
	 */
	private void showPrintedCode(CodeFragment granularityShapedCodeFragment) {
		System.out.println(granularityShapedCodeFragment.getCode());
	}
	
	/**
	 * The selection and synthesis of particular contexts according to the configured strategies
	 *  
	 * @param astRoot - the root of AST
	 * @param positiveVariationPointCandidatesTemplates - the list of features/positive variation points representation that will be selected
	 * @param processDirectly - if true the contexts are synthesized immediately after their selection and all preparations
	 * @return the list of synthesized contexts, but always the null is returned
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws VariationPointPlaceInArrayNotFound
	 * @throws UnknownResourceToProcessException
	 * @throws AlreadyMappedVariationPointContentsInjection
	 */
	public List<SynthesizedContent> selectAndSynthetizeContexts(JSONObject astRoot, 
			List<PositiveVariationPointCandidateTemplates> positiveVariationPointCandidatesTemplates,
			boolean processDirectly) throws IOException, InterruptedException, InvalidSystemVariationPointMarkerException,
			VariationPointPlaceInArrayNotFound, UnknownResourceToProcessException, AlreadyMappedVariationPointContentsInjection {
		Map<String, VariationPointContentsInjection> functionalityOnVarPointsToPossibilitiesMap 
				= this.selectContexts(positiveVariationPointCandidatesTemplates);
		VariationPointContentsInjection variationPointContentInjection;
		JSONObject actuallyProcessedVariationPointData;
		String variationPointIDName;
	
		//content preparation (into map transformation) - extracts selected constructs from each variation point and organizes them in map
		for (PositiveVariationPointCandidateTemplates positiveVariationPointCandidateTemplate: positiveVariationPointCandidatesTemplates) {
			actuallyProcessedVariationPointData = positiveVariationPointCandidateTemplate.getVariationPointData();
			variationPointIDName = (String) actuallyProcessedVariationPointData.get("actuallyProcessedVariationPointData");
			
			if (variationPointIDName != null && !functionalityOnVarPointsToPossibilitiesMap.containsKey(variationPointIDName)) {
				variationPointContentInjection = new VariationPointContentsInjection(positiveVariationPointCandidateTemplate, new ArrayList<CodeFragment>());
				functionalityOnVarPointsToPossibilitiesMap.put(variationPointIDName, variationPointContentInjection);
			}
		}

		this.synthesizeContexts(astRoot, functionalityOnVarPointsToPossibilitiesMap, processDirectly);
		return null;
	}

	/**
	 * Returns the name of the positive variability marker if found otherwise null
	 * 
	 * @param entryJSONObject - the AST with declared variables which are checked for the presence of positive variability marker
	 * @return the name of the marker if found otherwise null
	 */
	private String getMarkerName(JSONObject entryJSONObject) {
		String potentialMarkerName;
		JSONArray declarationsList;
		JSONObject declaration;
		if (entryJSONObject.containsKey("declarationList")) {
			declarationsList = (JSONArray) ((JSONObject) entryJSONObject.get("declarationList")).get("declarations");
			for (Object declarationObject: declarationsList) {
				declaration = (JSONObject) declarationObject;
				potentialMarkerName = ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(declaration);
				if (potentialMarkerName.startsWith(VariationPointDivisionConfiguration.MARKER_VP_NAME)) {
					return potentialMarkerName;
				}
			}
		}
		return null;
	}
	
	/**
	 * Processes particular variation point and injects the selected code constructs
	 * 
	 * @param astTemplatePart - the AST part of the template/original code (is not modified) possibly with the particular positive variation point marker
	 * @param contextIdentifier - the unique context identifier
	 * @param contextArray - the JSON array of particular contexts
	 * @param variationPointsContentInjection - the all related information for injection into specified variation point
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws VariationPointPlaceInArrayNotFound
	 */
	private void processPossiblePositiveVariationPointPlace(JSONObject astTemplatePart, String contextIdentifier, 
			JSONArray contextArray, VariationPointsContentInjection variationPointsContentInjection) 
					throws IOException, InterruptedException, VariationPointPlaceInArrayNotFound {
		String markerName = this.getMarkerName(astTemplatePart);
		int foundPositionIndex = -1;
	
		//contextArray.remove(contextArray.get(foundPositionIndex));
		CodeFragment codeFragment;
		JSONArray codePartsArray;
		if (markerName != null) {
			for (int positionIndex = 0; positionIndex < contextArray.size(); positionIndex++) {
				if (astTemplatePart.toString().equals(contextArray.get(positionIndex).toString())) {
					foundPositionIndex = positionIndex;
				}
			}
			if (foundPositionIndex == -1) {
				throw new VariationPointPlaceInArrayNotFound("Template element is not matched!");
			}
			
			codeFragment = variationPointsContentInjection.getContentAccordingToMarkerName(markerName);
			if (codeFragment != null) {
				codePartsArray = codeFragment.getCodeAst();
				
				if(DebugInformation.SHOW_POSITIVE_VARIABILITY_INCREMENT_CODE_FRAGMENT) {
					System.out.println("Inserted code at marker: " + markerName);
					int index = 0;
					for (Object codePart: codePartsArray) {
						System.out.println(index + ": " + codePart.toString());
						index++;
					}
				}
				
				contextArray.addAll(foundPositionIndex, codePartsArray);
			}
		}
	}
	
	/**
	 * Iterates over the template AST (from base code) and injects the functionality (code constructs) on selected places (variation points)
	 * 
	 * @param astTemplatePart - the AST part of the template/original code (is not modified) possibly with the particular positive variation point marker
	 * @param astPart - the AST part equivalent to the original AST part for the code synthesis (recursion is not dependent on it)
	 * @param variationPointsContentInjection - the all related information for injection into specified variation point
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws VariationPointPlaceInArrayNotFound
	 */
	private void searchForPositiveVariabilityVariationPointMarkers(JSONObject astTemplatePart, JSONObject astPart,
			VariationPointsContentInjection variationPointsContentInjection) throws IOException, 
			InterruptedException, InvalidSystemVariationPointMarkerException, VariationPointPlaceInArrayNotFound {
		String key;
		if (astTemplatePart == null || astPart == null) { return; }

		Object entryValue;
		JSONObject entryTemplateJSONObject, entryJSONObject;
		JSONArray contextTemplateArray, contextArray;
		for(Object entryKey: astTemplatePart.keySet()) {
			key = (String) entryKey;
			entryValue = astTemplatePart.get(key);
			//if (key.equals("illegalDecorators")) {	continue; }
			if (entryValue instanceof JSONObject) {
				entryTemplateJSONObject = (JSONObject) entryValue;
				entryJSONObject = (JSONObject) astPart.get(key);
				this.searchForPositiveVariabilityVariationPointMarkers(entryTemplateJSONObject, entryJSONObject, variationPointsContentInjection);
			} else if(entryValue instanceof JSONArray) {	
				contextTemplateArray = (JSONArray) entryValue;
				contextArray = (JSONArray) astPart.get(key); //context to insert functionality
				JSONArray contextArrayPrevious = new JSONArray();
				for (Object previousObject: contextArray) {
					contextArrayPrevious.add((JSONObject) previousObject);
				}
				for (int index = 0; index < contextTemplateArray.size(); index++) {
					entryTemplateJSONObject = (JSONObject) contextTemplateArray.get(index);
					entryJSONObject = (JSONObject) contextArrayPrevious.get(index);
					this.processPossiblePositiveVariationPointPlace(entryTemplateJSONObject, key,
							contextArray, variationPointsContentInjection);
	
					this.searchForPositiveVariabilityVariationPointMarkers(
							entryTemplateJSONObject, entryJSONObject, variationPointsContentInjection);
				}
			}
		}
	}
	
	/**
	 * Integrates the imports into synthesized base code/script
	 * -dependencies in for of imports are extracted from each code fragment and used to update the imports in existing synthesized script/application
	 * -the imports are optionally integrated into the base script as imports (for testing using HTML template the other mechanism is used - dependencies are imported as script)
	 * 
	 * @param newApplicationAst - the root of synthesized AST to insert/integrate all related imports 
	 * @param variationPointsContentInjection - the all related information for injection into specified variation point
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void integrateImports(JSONObject newApplicationAst,
			VariationPointsContentInjection variationPointsContentInjection) throws IOException, InterruptedException {
		for (CodeFragment usedCodeFragment: variationPointsContentInjection.getCodeFragments()) {
			usedCodeFragment.integrateImports(newApplicationAst);
		}
	}

	/**
	 * Searches for selected places and inserts selected constructs into synthesized AST
	 * 
	 * @param templateAstRoot - the AST part of the template/original code (is not modified) possibly with the particular positive variation point marker
	 * @param newApplicationAst - the root of synthesized AST to insert/integrate all related imports
	 * @param variationPointsContentInjection - the all related information for injection into specified variation point
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws VariationPointPlaceInArrayNotFound
	 */
	private void updateAstTreeAccordingInformationFromVariationPoints(JSONObject templateAstRoot, JSONObject newApplicationAst, 
			VariationPointsContentInjection variationPointsContentInjection) throws IOException, 
			InterruptedException, InvalidSystemVariationPointMarkerException, VariationPointPlaceInArrayNotFound {		
		this.searchForPositiveVariabilityVariationPointMarkers(templateAstRoot, newApplicationAst, variationPointsContentInjection);
		if (!SPLEvolutionCore.APPLY_TO_TEMPLATE) { 			// imports are not inserted into template
			this.integrateImports(newApplicationAst, variationPointsContentInjection);
		}
	}
	
	/**
	 * Synthesizes the selected configuration among code constructs in place of selected variation points (modeling also crosscutting features)
	 * -optionally the resulting projects/products are directly generated if processDirectly parameter is set to true
	 *   
	 * @param templateAstRoot - the AST part of the template/original code (is not modified) possibly with the particular positive variation point marker
	 * @param functionalityOnVarPointsToPossibilitiesMap - the mapping of variation points names to selected constructs on their places
	 * @param processDirectly - if true the contexts are synthesized immediately after their selection and all preparations
	 * @return the list of synthesized content for their further serialization if processDirectly is set to true
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws VariationPointPlaceInArrayNotFound
	 * @throws UnknownResourceToProcessException
	 */
	public List<SynthesizedContent> synthesizeContexts(JSONObject templateAstRoot,
			Map<String, VariationPointContentsInjection> functionalityOnVarPointsToPossibilitiesMap,
			boolean processDirectly) throws IOException, 
			InterruptedException, InvalidSystemVariationPointMarkerException, VariationPointPlaceInArrayNotFound, UnknownResourceToProcessException {
		
		// synthesis of selected constructs across selected variation points
		List<VariationPointsContentInjection> variationPointsContentInjections = this.selectionOfConstructsAcrossSelectedVariationPointsStrategy.
				aggregateAllPossibleInjections(functionalityOnVarPointsToPossibilitiesMap);
		VariationPointConjunctor variationPointConjunctor;
		ExportLocationAggregation associatedAggregatedLocationExports;
		SynthesizedContent synthesizedContent;
		JSONObject newApplicationAst;
		String projectId;
		List<ExportLocationAggregation> usedExportedAggregations;

		List<SynthesizedContent> synthesizedContents = new ArrayList<SynthesizedContent>();
 		for (VariationPointsContentInjection variationPointsContentInjection: variationPointsContentInjections) {
			synthesizedContent = new SynthesizedContent(templateAstRoot, this.syntetizedContentName, derivationResourcesManager.getVariationPointData());
			newApplicationAst = synthesizedContent.getReferenceToProcessedAST();
			this.updateAstTreeAccordingInformationFromVariationPoints(
					templateAstRoot, newApplicationAst, variationPointsContentInjection);
	
			/* try {
				System.out.println(ASTConverterClient.convertFromASTToCode(newApplicationAst.toString()));
			} catch(Exception e) {
				
			} */
			// possibilities to serialized all information - inside synthesized content
			if (processDirectly) {
				if(DebugInformation.PROCESS_STEP_INFORMATION) { System.out.println("GENERATING..............");}
				usedExportedAggregations = new ArrayList<ExportLocationAggregation>();
				for (CodeFragment usedCodeFragment: variationPointsContentInjection.getCodeFragments()) {
					associatedAggregatedLocationExports = usedCodeFragment.getExportLocationAggregation();
					usedExportedAggregations.add(associatedAggregatedLocationExports);
				}
				variationPointConjunctor = new VariationPointConjunctor(this.derivationResourcesManager);

				// APPLY DATA REPRESENTATIONS - applied after conjuction to wrap inner content correctly
				this.dataRepresentationPerspective.optionallyOpenDataRepresentationPerspective(
						this.derivationResourcesManager.getEvolutionConfigurationReference(), synthesizedContent.getReferenceToProcessedAST());
				
				if (SPLEvolutionCore.APPLY_TO_TEMPLATE) {
					projectId = variationPointConjunctor.deriveProductWithConjunctingPartsAsTestTemplate(synthesizedContent, usedExportedAggregations);
				} else {
					projectId = variationPointConjunctor.deriveProductWithConjunctingParts(synthesizedContent, usedExportedAggregations);
				}
				
				if (SPLEvolutionCore.SERIALIZE_APPLICATION_AST || SPLEvolutionCore.SERIALIZE_VARIATION_POINTS) {
					variationPointConjunctor.serializeModifiedVariationPointConfiguration(synthesizedContent, projectId);
				}
			} else {
				synthesizedContents.add(synthesizedContent);
			}
		}
 		return synthesizedContents;
	}
}
