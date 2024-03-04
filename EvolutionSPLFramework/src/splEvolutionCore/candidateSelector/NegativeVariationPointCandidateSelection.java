package splEvolutionCore.candidateSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import splEvolutionCore.candidateSelector.valueAssignment.selectionAndUnselectionStrategies.SelectionAndUnselectionStrategy;


/**
 * Manages selection of negative variability variation points from the list of all managed variation points
 * -manages which code fragments will be assigned/remain under variability to provide more 
 * heterogeneous derivations or will be removed and assigned to commonality with increased reuse
 * 
 * @author Jakub Perdek
 *
 */
public class NegativeVariationPointCandidateSelection {

	/**
	 * Key in JSON that represents information belongs to negative variability (if given variation point is already available as an existing code fragment)
	 */
	private final static String NEW_VARIATION_POINT = "newVariationPoint";
	
	/**
	 * Implementation of given strategy that is used to decide if given variation points should be chosen as variable or deselected/not chosen to represent commonalities
	 */
	private SelectionAndUnselectionStrategy chosenSelectionAndUnselectionStrategy;

	
	/**
	 * Creates the instance of NegativeVariationPointCandidateSelection
	 * 
	 * @param selectionAndUnselectionStrategy - the implementation of the strategy to select and deselect variation points assigned to variability of SPL 
	 */
	public NegativeVariationPointCandidateSelection(SelectionAndUnselectionStrategy selectionAndUnselectionStrategy) {
		this.chosenSelectionAndUnselectionStrategy = selectionAndUnselectionStrategy;
	}
	
	/**
	 * Extracts the variation point and associates all available data to it if the given variation point is negative
	 *  
	 * @param variationPointArray - the managed JSON array of variation points
	 * @return the list of negative variability variation point candidates
	 */
	public static List<NegativeVariationPointCandidate> createNegativeVariabilityCandidates(JSONArray variationPointArray) {
		List<NegativeVariationPointCandidate> negativeVariationCandidates = new ArrayList<NegativeVariationPointCandidate>();
		NegativeVariationPointCandidate createdCandidate;
		
		JSONObject variationPointData;
		for (Object variationPointObject: variationPointArray) {
			variationPointData = (JSONObject) variationPointObject;
			// the test if variation point belongs to negative variability
			if ((boolean) variationPointData.get(NegativeVariationPointCandidateSelection.NEW_VARIATION_POINT) == false) {
				createdCandidate = new NegativeVariationPointCandidate(variationPointData);
				negativeVariationCandidates.add(createdCandidate); // candidate is appended
			}
		}
		return negativeVariationCandidates;
	}
	
	/**
	 * Extracts the variation point and associates all available data to it if the given variation point is negative
	 *  
	 * @param variationPointArray - the managed JSON array of variation points
	 * @return the mapping of the negative variability variation point names with their representations
	 */
	public static Map<String, NegativeVariationPointCandidate> createNegativeVariabilityCandidatesInMap(JSONArray variationPointArray) {
		Map<String, NegativeVariationPointCandidate> negativeVariationCandidates = new HashMap<String, NegativeVariationPointCandidate>();
		NegativeVariationPointCandidate createdCandidate;
		
		JSONObject variationPointData;
		String candidateIdentifier;
		for (Object variationPointObject: variationPointArray) {
			variationPointData = (JSONObject) variationPointObject;
			// the test if variation point belongs to negative variability
			if ((boolean) variationPointData.get(NegativeVariationPointCandidateSelection.NEW_VARIATION_POINT) == false) {
				createdCandidate = new NegativeVariationPointCandidate(variationPointData);
				candidateIdentifier = createdCandidate.getIdentifier();
				negativeVariationCandidates.put(candidateIdentifier, createdCandidate); //candidate is associated with its identifier
			}
		}
		return negativeVariationCandidates;
	}
	
	/**
	 * Checks if user annotation is inside of provided variation point data
	 * 
	 * @param variationPointData - the associated information with variation point including code, 
	 * positions in AST, user annotations and other attributes
	 * @return true if variation point data contains information about user annotation otherwise false
	 */
	private boolean checkIfUserAnnotationIsInside(JSONObject variationPointData) {
		JSONArray variationPointAnnotations = (JSONArray) variationPointData.get("variabilitySelections");
		if (variationPointAnnotations == null) { return false; }
		
		JSONObject variationPointAnnotation;
		String variationPointAnnotationType;
		for (Object variationPointAnnotationObject: variationPointAnnotations) {
			variationPointAnnotation = (JSONObject) variationPointAnnotationObject;
			variationPointAnnotationType = (String) variationPointAnnotation.get("type");
			if (variationPointAnnotationType == null) { continue; }
			if (variationPointAnnotationType.equals("user")) { return true; }
		}
		return true;
	}
	
	/**
	 * Separates already selected variability variation point candidates from available to select negative variability variation point candidates
	 * 
	 * @param availableAndChoiceCandidates - the list of negative variability variation point candidates
	 * @param alreadyChosenCandidates - the list of already chosen candidates to represent variability in SPL
	 * @param availableToChooseCandidates - the list of candidates from which candidates can be selected to represent variability in SPL
	 */
	private void separarateAlreadySelectedAndAvailableToSelectCandidates(
			List<NegativeVariationPointCandidate> availableAndChoiceCandidates,
			List<NegativeVariationPointCandidate> alreadyChosenCandidates,
			List<NegativeVariationPointCandidate> availableToChooseCandidates) {
		JSONObject variationPointData;
		for (NegativeVariationPointCandidate availableAndChoiceCandidate: availableAndChoiceCandidates) {
			variationPointData = availableAndChoiceCandidate.getVariationPointData();
			
			if (this.checkIfUserAnnotationIsInside(variationPointData)) {
				availableToChooseCandidates.add(availableAndChoiceCandidate);
			} else {
				alreadyChosenCandidates.add(availableAndChoiceCandidate);
			}
		}
	}
	
	/**
	 * Creates map from the chosen negative variability variation point candidates
	 * 
	 * @param chosenCandidates - the list of chosen/selected negative variability variation points to represent variability in SPL
	 * @return the mapping of the negative variability variation point names to their full representations
	 * @throws DuplicateCandidateIdentifier - refers to duplicate identifier of candidate amongst all used candidates
	 */
	private Map<String, NegativeVariationPointCandidate> chosenCandidatesToMap(
			List<NegativeVariationPointCandidate> chosenCandidates) throws DuplicateCandidateIdentifier {
		String variationPointCandidateIdentifier;
		Map<String, NegativeVariationPointCandidate> chosenCandidatesMap = new HashMap<String, NegativeVariationPointCandidate>();
		for (NegativeVariationPointCandidate chosenCandidate: chosenCandidates) {
			variationPointCandidateIdentifier = chosenCandidate.getVariationPointName();
			if (variationPointCandidateIdentifier != null && !chosenCandidatesMap.containsKey(variationPointCandidateIdentifier)) {
				chosenCandidatesMap.put(variationPointCandidateIdentifier, chosenCandidate);
			} else {
				throw new DuplicateCandidateIdentifier("Identifier is a duplicate: " + variationPointCandidateIdentifier);
			}
		}
		return chosenCandidatesMap;
	}
	
	/**
	 * Manages the both selection and deselections of negative variability variation points to represent variability in SPL
	 * 
	 * @param availableAndChoiceCandidates - the list of negative variability variation points
	 * @return the mapping of the negative variability variation point names to their full representations
	 * @throws DuplicateCandidateIdentifier - refers to duplicate identifier of candidate amongst all used candidates
	 */
	public Map<String, NegativeVariationPointCandidate> manageCandidateSelectionAndUnselections(
			List<NegativeVariationPointCandidate> availableAndChoiceCandidates) throws DuplicateCandidateIdentifier {
		List<NegativeVariationPointCandidate> alreadyChosenCandidates = new LinkedList<NegativeVariationPointCandidate>();
		List<NegativeVariationPointCandidate> availableToChooseCandidates = new LinkedList<NegativeVariationPointCandidate>();
		this.separarateAlreadySelectedAndAvailableToSelectCandidates(
				availableAndChoiceCandidates, alreadyChosenCandidates, availableToChooseCandidates);
		this.chosenSelectionAndUnselectionStrategy.unselectCandidates(alreadyChosenCandidates);
		this.chosenSelectionAndUnselectionStrategy.selectCandidates(availableToChooseCandidates, alreadyChosenCandidates);
		
		return this.chosenCandidatesToMap(availableAndChoiceCandidates);
	}
}
