package splEvolutionCore.candidateSelector.valueAssignment;

import java.io.IOException;
import java.util.Map;
import splEvolutionCore.candidateSelector.NegativeVariationPointCandidate;
import splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment.MethodToEvaluateComplexityNotFoundException;


/**
 * Manages how the preference amongst variation points that belongs to negative variability is evaluated
 * -all strategies use different keys and metrics/or metrics under different keys
 * 
 * @author Jakub Perdek
 *
 */
public interface ChosenValueAssignmentStrategyForNegativeVariability {
	
	/**
	 * Evaluates the outcomes of existing data from provided negative variability variation point in the 
	 * form of mapping of metric name to associated information
	 * 
	 * @param negativeVariationPointCandidate - representation of negative variation point, usually the code of existing module is stored tehre
	 * @return outcome of measurements in form of the mapping of given metric with measured/representative information, especially value
	 * @throws MethodToEvaluateComplexityNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public Map<String, AssignedValue> getStrategyAssignedValues(NegativeVariationPointCandidate 
			negativeVariationPointCandidate) throws MethodToEvaluateComplexityNotFoundException, IOException, InterruptedException;
}
