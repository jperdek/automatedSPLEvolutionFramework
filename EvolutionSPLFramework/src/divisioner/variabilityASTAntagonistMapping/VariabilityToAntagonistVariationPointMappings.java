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
	
	public void loadAntagonistBoundaries(List<PositiveVariationPointCandidateTemplates> 
	positiveVariationPointCandidatesTemplates, InnerContext innerContextRoot, boolean onlyToBlockTransformation) {
		Long endSearchPosition, startSearchPosition;
		JSONObject actuallyProcessedVariationPointData;
		String variationPointIDName;
		String hierarchicIdentifier;
		VariationPointTransformationBetweenAsts variationPointTransformationBetweenAsts;
		Set<VariationPointTransformationBetweenAsts> variationPointTransformationBetweenAstsSet;
		
		for (PositiveVariationPointCandidateTemplates positiveVariationPointCandidateTemplate: positiveVariationPointCandidatesTemplates) {
			actuallyProcessedVariationPointData = positiveVariationPointCandidateTemplate.getVariationPointData();
			variationPointIDName = (String) actuallyProcessedVariationPointData.get("variationPointName");
		
			startSearchPosition = (long) actuallyProcessedVariationPointData.get("startPosition");
			endSearchPosition = (long) actuallyProcessedVariationPointData.get("endPosition");
			hierarchicIdentifier = (String) actuallyProcessedVariationPointData.get("hierarchicIdentifier");
			
			variationPointTransformationBetweenAsts = new VariationPointTransformationBetweenAsts(variationPointIDName, startSearchPosition, endSearchPosition);
			if (this.relationshipMappings.containsKey(hierarchicIdentifier)) {
				variationPointTransformationBetweenAstsSet = this.relationshipMappings.get(hierarchicIdentifier);
			} else {
				variationPointTransformationBetweenAstsSet = new HashSet<VariationPointTransformationBetweenAsts>();
			}
			variationPointTransformationBetweenAstsSet.add(variationPointTransformationBetweenAsts);
			this.relationshipMappings.put(hierarchicIdentifier, variationPointTransformationBetweenAstsSet);
		}
		
		this.loadAndTransformVariationPointsData(innerContextRoot, "", onlyToBlockTransformation);
	}
	
	private void loadAndTransformVariationPointsData(InnerContext innerContext, String hierarchicIdentifier, boolean onlyToBlockTransformation) {
		long processedAntagonistContextStartPosition = innerContext.getActualStartPosition();
		long processedAntagonistContextEndPosition = innerContext.getActualEndPosition();
		String childHierarchicIdentifier = hierarchicIdentifier;
		String entityName;
		long startMinimum, endMaximum, currentStartPosition, currentEndPosition;
		
		if (hierarchicIdentifier.contains("[F|constructor]")) {
			System.out.println("Skipping class constructor....");
			return;
		}
		
		if (!hierarchicIdentifier.equals("")) {
			Set<VariationPointTransformationBetweenAsts> variationPointTransformationBetweenAstsSet = 
					this.relationshipMappings.get(hierarchicIdentifier);
			startMinimum = -1;
			endMaximum = -1;
			if (!onlyToBlockTransformation) {
				for (VariationPointTransformationBetweenAsts variationPointTransformationBetweenAsts: variationPointTransformationBetweenAstsSet) {
					currentStartPosition = variationPointTransformationBetweenAsts.getTransformedStartPosition();
					if (startMinimum == -1 || startMinimum > currentStartPosition) { startMinimum = currentStartPosition; }
					currentEndPosition = variationPointTransformationBetweenAsts.getTransformedEndPosition();
					if (endMaximum == -1 || endMaximum < currentEndPosition) { endMaximum = currentEndPosition; }
				}
			}

			for (VariationPointTransformationBetweenAsts variationPointTransformationBetweenAsts:variationPointTransformationBetweenAstsSet) {
				// length of whole AST is intended to consist from the 0 to endMaximum, where length of block is ranging from endMinimum to endMaximum 
				if (onlyToBlockTransformation) {
					variationPointTransformationBetweenAsts.doTransformationOnBlocks(
							processedAntagonistContextStartPosition, processedAntagonistContextEndPosition);
				} else {
					variationPointTransformationBetweenAsts.doTransformation(
						processedAntagonistContextStartPosition, processedAntagonistContextEndPosition, endMaximum - startMinimum);
				}
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
			this.loadAndTransformVariationPointsData(childContext, childHierarchicIdentifier, onlyToBlockTransformation);
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
