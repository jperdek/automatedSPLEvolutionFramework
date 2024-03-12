package splEvolutionCore.candidateSelector.valueAssignment;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.simple.JSONObject;
import splEvolutionCore.candidateSelector.NegativeVariationPointCandidate;
import splEvolutionCore.candidateSelector.valueAssignment.cleaning.AllMarkerRemover;
import splEvolutionCore.candidateSelector.valueAssignment.cleaning.AnnotationsFromCodeRemover;
import splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment.MethodToEvaluateComplexityNotFoundException;


/**
 * Manages value assignment from various measured metrics to manage negative variability
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class AssignedValueProcessForNegativeVariability extends AssignedValueProcess<ChosenValueAssignmentStrategyForNegativeVariability> {
	
	/**
	 * Creates manager to manage various strategies and stores first assigned strategy
	 * 
	 * @param chosenValueAssignmentStrategyForNegativeVariability - given chosen value assignment strategy
	 */
	public AssignedValueProcessForNegativeVariability(ChosenValueAssignmentStrategyForNegativeVariability chosenValueAssignmentStrategyForNegativeVariability) {
		super(chosenValueAssignmentStrategyForNegativeVariability);
	}
	
	/**
	 * Creates manager to manage various strategies and stores the list of strategies that are used to represent given outcome
	 * 
	 * @param chosenValueAssignmentStrategyForNegativeVariabilities - the list of chosen value assignment strategies
	 */
	public AssignedValueProcessForNegativeVariability(List<ChosenValueAssignmentStrategyForNegativeVariability> chosenValueAssignmentStrategyForNegativeVariabilities) {
		super(chosenValueAssignmentStrategyForNegativeVariabilities);
	}
	
	/**
	 * Creates manager to manage various strategies and stores the array of strategies that are used to represent given outcome
	 * 
	 * @param chosenValueAssignmentStrategyForNegativeVariabilities - the array of chosen value assignment strategies
	 */
	public AssignedValueProcessForNegativeVariability(ChosenValueAssignmentStrategyForNegativeVariability chosenValueAssignmentStrategyForNegativeVariabilities[]) {
		super(chosenValueAssignmentStrategyForNegativeVariabilities);
	}
	
	/**
	 * Assigns value for each negative variation point candidate
	 * 
	 * @param negativeVariationPointCandidates - the list of negative variation point candidates
	 * @throws MethodToEvaluateComplexityNotFoundException - the exception thrown when method which complexity is evaluated is not found in results
	 * @throws IOException - the exception for various problems with file loading
	 * @throws InterruptedException - the exception thrown during interruption
	 */
	public void assignValuesProcess(List<NegativeVariationPointCandidate> negativeVariationPointCandidates) throws MethodToEvaluateComplexityNotFoundException, IOException, InterruptedException {
		for (NegativeVariationPointCandidate negativeVariationPointCandidate: negativeVariationPointCandidates) {
			this.assignValuesProcess(negativeVariationPointCandidate);
		}
	}

	/**
	 * Process of value assignment for specific/selected negative variation point - observing its quality/score/value
	 * -any constructs from associated code to any variation point are removed - business code remains
	 * -all available/assigned strategies with their measurements are executed and their values are stored
	 * 
	 * @param negativeVariationPointCandidate - the negative variation point candidate that is processed - metrics are associated with him
	 * @throws MethodToEvaluateComplexityNotFoundException - the exception thrown when method which complexity is evaluated is not found in results
	 * @throws IOException - the exception for various problems with file loading
	 * @throws InterruptedException - the exception thrown during interruption
	 */
	public void assignValuesProcess(NegativeVariationPointCandidate negativeVariationPointCandidate) throws MethodToEvaluateComplexityNotFoundException, IOException, InterruptedException {
		//group variation points according expressions
		//mix possible positive variability candidates - only additionally
		this.cleanVariabilityConstructs(negativeVariationPointCandidate.getVariationPointData());
		this.assignValues(negativeVariationPointCandidate);
	}
	
	/**
	 * Assigns value to given negative variability constructs - to existing business functionality/code on variation points
	 * -iterates through all available value assignment strategies
	 * -performs given measurement under evaluated variation point
	 * -inserts value from performed measurements
	 * 
	 * @param negativeVariationPointCandidate - representation of negative variation point with associated information including code
	 * @throws MethodToEvaluateComplexityNotFoundException - the exception thrown when method which complexity is evaluated is not found in results
	 * @throws IOException - the exception for various problems with file loading
	 * @throws InterruptedException - the exception thrown during interruption
	 */
	private void assignValues(NegativeVariationPointCandidate negativeVariationPointCandidate) throws MethodToEvaluateComplexityNotFoundException, IOException, InterruptedException {
		this.cleanVariabilityConstructs(negativeVariationPointCandidate.getVariationPointData()); 		// obtains variation point data
		
		Map<String, AssignedValue> assignedValues;
		for (ChosenValueAssignmentStrategyForNegativeVariability chosenStrategy: this.chosenValueAssignmentStrategies) {
			assignedValues = chosenStrategy.getStrategyAssignedValues(negativeVariationPointCandidate); // measures outcome - usually the quality metric (for negat. variability)
			for (Entry<String, AssignedValue> assignedValueEntry: assignedValues.entrySet()) {
				negativeVariationPointCandidate.putAssignedValue(assignedValueEntry.getKey(), assignedValueEntry.getValue());
			}
		}
	}
	
	/**
	 * Removes all variability constructs from given AST
	 * -in this context it operates on variation point data where is necessary to evaluate only their business quality and not variability management
	 * 
	 * @param codeAst - AST from which variability constructs are going to be removed 
	 */
	private void cleanVariabilityConstructs(JSONObject codeAst) {
		AnnotationsFromCodeRemover.removeAllVariabilityAnnotations(codeAst, codeAst); // annotations that belongs to negative variability
		AllMarkerRemover.removeAllPositiveVariabilityMarkers(codeAst, codeAst);		  // markers that belongs to positive variability
	}
}
