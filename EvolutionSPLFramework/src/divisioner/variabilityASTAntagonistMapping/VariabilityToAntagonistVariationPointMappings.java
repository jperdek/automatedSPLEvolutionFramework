package divisioner.variabilityASTAntagonistMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;

import codeContext.ClassContext;
import codeContext.FunctionContext;
import codeContext.InnerContext;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Transforms coordinate boundaries from variability enhanced AST into antagonist representation which is enhanced wihout any system variability markings or annotations
 * 
 * @author Jakub Perdek
 *
 */
public class VariabilityToAntagonistVariationPointMappings {

	/**
	 * Mapping of hierarchic identifiers into their object representation which implements transformation functionality
	 */
	private Map<String, Set<VariationPointTransformationBetweenAsts>> relationshipMappings;
	
	/**
	 * Initializes the trasformation class
	 */
	public VariabilityToAntagonistVariationPointMappings() {
		this.relationshipMappings = new HashMap<String, Set<VariationPointTransformationBetweenAsts>>();
	}
	
	public void loadAntagonistBoundaries(List<PositiveVariationPointCandidateTemplates> positiveVariationPointCandidatesTemplates, InnerContext innerContextRoot) {
		Long endSearchPosition, startSearchPosition;
		JSONObject actuallyProcessedVariationPointData;
		String variationPointIDName;
		String hierarchicIdentifier;
		VariationPointTransformationBetweenAsts variationPointTransformationBetweenAsts;
		Set<VariationPointTransformationBetweenAsts> variationPointTransformationBetweenAstsSet;
		
		for (PositiveVariationPointCandidateTemplates positiveVariationPointCandidateTemplate: positiveVariationPointCandidatesTemplates) {
			actuallyProcessedVariationPointData = positiveVariationPointCandidateTemplate.getVariationPointData();
			variationPointIDName = (String) actuallyProcessedVariationPointData.get("variationPointName");
		
			startSearchPosition = (long) actuallyProcessedVariationPointData.get("originalASTStartPosition");
			endSearchPosition = (long) actuallyProcessedVariationPointData.get("originalASTEndPosition");
			hierarchicIdentifier = (String) actuallyProcessedVariationPointData.get("hierarchicIdentifier");
			
			System.out.println(hierarchicIdentifier);
			variationPointTransformationBetweenAsts = new VariationPointTransformationBetweenAsts(variationPointIDName, startSearchPosition, endSearchPosition);
			if (this.relationshipMappings.containsKey(hierarchicIdentifier)) {
				variationPointTransformationBetweenAstsSet = this.relationshipMappings.get(hierarchicIdentifier);
			} else {
				variationPointTransformationBetweenAstsSet = new HashSet<VariationPointTransformationBetweenAsts>();
			}
			variationPointTransformationBetweenAstsSet.add(variationPointTransformationBetweenAsts);
			this.relationshipMappings.put(hierarchicIdentifier, variationPointTransformationBetweenAstsSet);
		}
		
		this.loadAndTransformVariationPointsData(innerContextRoot, "");
	}
	
	private void loadAndTransformVariationPointsData(InnerContext innerContext, String hierarchicIdentifier) {
		long processedAntagonistContextStartPosition = innerContext.getActualStartPosition();
		long processedAntagonistContextEndPosition = innerContext.getActualEndPosition();
		String childHierarchicIdentifier = hierarchicIdentifier;
		String entityName;
		
		System.out.println(hierarchicIdentifier);
		if (hierarchicIdentifier.contains("[F|constructor]")) {
			System.out.println("Skipping class constructor....");
			return;
		}
		
		if (!hierarchicIdentifier.equals("")) {
			Set<VariationPointTransformationBetweenAsts> variationPointTransformationBetweenAstsSet = 
					this.relationshipMappings.get(hierarchicIdentifier);
			for (VariationPointTransformationBetweenAsts variationPointTransformationBetweenAsts:variationPointTransformationBetweenAstsSet) {
				variationPointTransformationBetweenAsts.doTransformation(
						processedAntagonistContextStartPosition, processedAntagonistContextEndPosition);
			}
		}
		
		for(InnerContext childContext: innerContext.getAllInnerChildContext()) {
			if (childContext instanceof FunctionContext) {
				entityName = ((FunctionContext) childContext).getFunctionName();
				childHierarchicIdentifier = hierarchicIdentifier + HierarchyEntityConstructor.createFunctionLabel(entityName);
			} else if (childContext instanceof ClassContext) {
				entityName = ((ClassContext) childContext).getClassName();
				childHierarchicIdentifier = hierarchicIdentifier + HierarchyEntityConstructor.createClassLabel(entityName);
			}
			this.loadAndTransformVariationPointsData(childContext, childHierarchicIdentifier);
		}
	}
	
	public List<VariationPointTransformationBetweenAsts> getVariationPointTransformationBetweenAsts() { 
		List<VariationPointTransformationBetweenAsts> variationPointTransformationBetweenAstsEntities = new ArrayList<VariationPointTransformationBetweenAsts>();
		for (Set<VariationPointTransformationBetweenAsts> variationPointTransformationBetweenAstsSet: this.relationshipMappings.values()) {
			variationPointTransformationBetweenAstsEntities.addAll(variationPointTransformationBetweenAstsSet);
		}
		return variationPointTransformationBetweenAstsEntities;
	}
}
