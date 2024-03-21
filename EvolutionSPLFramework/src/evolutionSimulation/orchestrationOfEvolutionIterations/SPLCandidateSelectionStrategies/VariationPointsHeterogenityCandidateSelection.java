package evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.imageContentQualityAssignment.VariationPointView;
import evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.imageContentQualityAssignment.VariationPointsDataAggregations;
import evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.imageContentQualityAssignment.VariationPointsDataOrganizer;
import evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.imageContentQualityAssignment.VariationPointsDataOrganizer.CapturedRelations;


/**
 * Selecting candidates according to the heterogeneous relations to various code structures
 * 
 * @author Jakub Perdek
 *
 */
public class VariationPointsHeterogenityCandidateSelection implements SPLNextEvolutionIterationCandidateSelectionStrategy {

	/**
	 * Data aggregations of categorized variation point according to their relation to the code
	 */
	private VariationPointsDataAggregations variationPointsDataAggregations;
	
	/**
	 * 
	 */
	private List<CapturedRelations> evaluatedRelations = List.of(CapturedRelations.INSIDE_CLASS, 
			CapturedRelations.INSIDE_RECURSION, CapturedRelations.IS_CONSTRUCTOR, CapturedRelations.IS_FUNCTION, 
			CapturedRelations.IS_VARIABLE, CapturedRelations.IS_PARAMETER, CapturedRelations.POSITIVE_VARIABILITY);
	

	/**
	 * Instantiates the entity to select candidates according to the heterogeneous relations to various code structures
	 * 
	 * @param variationPointsDataAggregations - the adapter with implemented functionality to select candidates according to the heterogeneous relations to various code structures
	 */
	public VariationPointsHeterogenityCandidateSelection(VariationPointsDataAggregations variationPointsDataAggregations) {
		this.variationPointsDataAggregations = variationPointsDataAggregations;
		
	}
	
	/**
	 * Returns associated candidate selector assessing adapters
	 * - the helper entities to collect variation point/SPL/product related data and use it for selection making under current candidates
	 * 
	 * @return the associated candidate selector assessing adapters - the helper entities to collect variation point/SPL/product 
	 * related data and use it for selection making under current candidates
	 */
	@Override
	public List<CandidateSelectorAssessingAdapter> getAssociatedCandidateSelectorAssessingAdapters() {
		return new ArrayList<CandidateSelectorAssessingAdapter>();
	}

	/**
	 * Selects the candidates to form the heterogeneous population for the next evolution iteration phase from the previous evolution phase/or initial samples
	 * 
	 * @param numberOfNextEvolutionCandidates - the number of the next evolution candidates
	 * @param evolutionDirectory - the path to the SPL projects from the previous evolution iteration/or their initial versions
	 * @param listOfCandidateSPLFileNamesFromEachPopulationMember - the list of candidate SPL file names from each previous evolution iteration population member
	 * @param candidateSelectorAssessingAdapters - helper entities to collect variation point/SPL/product 
	 * related data and use it for selection making under current candidates
	 * @return the list of selected next evolution iteration candidates according to implemented strategy
	 */
	@Override
	public List<String> selectNextEvolutionIterationCandidates(int numberOfNextEvolutionCandidates, String evolutionDirectory,
			List<String> listOfCandidateSPLFileNamesFromEachPopulationMember,
			CandidateSelectorAssessingAdapter... candidateSelectorAssessingAdapters) {
		VariationPointsDataOrganizer variationPointsOrganizer;
		Map<String, Set<VariationPointView>> nameToVariationPointViewSetMap;

		String uniqueVariationPointIdentifier;
		Set<String> helperSet;
		Map<String, Set<String>> uniqueContentGroupsMapping = new HashMap<String, Set<String>>(); 
		for (String candidateFileName: listOfCandidateSPLFileNamesFromEachPopulationMember) {
			variationPointsOrganizer = this.variationPointsDataAggregations.getVariationPointOraganizerForParticularSPL(candidateFileName);
			
			uniqueVariationPointIdentifier = "";
			for (CapturedRelations actualRelation: this.evaluatedRelations) {
				uniqueVariationPointIdentifier = uniqueVariationPointIdentifier + "|" + String.valueOf(
						variationPointsOrganizer.getVariationPointViewsInSetAccordingToRelation(actualRelation).size());
			}
			
			helperSet = uniqueContentGroupsMapping.get(uniqueVariationPointIdentifier);
			if (helperSet == null) {
				helperSet = new HashSet<String>();
				uniqueContentGroupsMapping.put(uniqueVariationPointIdentifier, helperSet);
			}
			helperSet.add(candidateFileName);
			
			//nameToVariationPointViewSetMap = this.variationPointsDataAggregations.getSetsOfRelatedVariationPointsAccordingToAssociatedGroup();
			//nameToVariationPointViewSetMap.get(nameToVariationPointViewSetMap);
			//nameToVariationPointViewSetMap.
		}
		if (numberOfNextEvolutionCandidates < listOfCandidateSPLFileNamesFromEachPopulationMember.size() 
				&& numberOfNextEvolutionCandidates < uniqueContentGroupsMapping.size()) {
			System.out.println("Not all candidates are proportionally unique. Unique are only: " + 
				uniqueContentGroupsMapping.size() + " from all of: " + numberOfNextEvolutionCandidates);
		}
		
		List<String> selectedCandidateSPLs = new ArrayList<String>();
		while (selectedCandidateSPLs.size() < numberOfNextEvolutionCandidates) {
			for (Set<String> candidateFileNameSet: uniqueContentGroupsMapping.values()) {
				for (String candidateFileName: candidateFileNameSet) {			
					selectedCandidateSPLs.add(candidateFileName);
					break;
				}
	
			}
		}
		return selectedCandidateSPLs;
	}
}
