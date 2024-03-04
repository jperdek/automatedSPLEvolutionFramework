package splEvolutionCore.candidateSelector.valueAssignment;

import java.io.IOException;
import java.util.Map;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;
import splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment.MethodToEvaluateComplexityNotFoundException;


/**
 * Manages how the preference amongst variation points that belongs to positive variability is evaluated
 * -all strategies use different keys and metrics/or metrics under different keys
 * 
 * @author Jakub Perdek
 *
 */
public interface ChosenValueAssignmentStrategyForPositiveVariability {
	
	/**
	 * Evaluates the outcomes of configured measurement of content stored in the string in form of mapping of metric name to associated information
	 * 
	 * @param positiveVariationPointCandidateTemplates - representation of positive variation point
	 * @param functionalityCall - its content represented as string, usually the code, that is evaluated
	 * @return outcome of measurements in form of the mapping of given metric with measured/representative information, especially value
	 * @throws MethodToEvaluateComplexityNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public Map<String, AssignedValue> getStrategyAssignedValues(PositiveVariationPointCandidateTemplates 
			positiveVariationPointCandidateTemplates, String functionalityCall) 
					throws MethodToEvaluateComplexityNotFoundException, IOException, InterruptedException;
}
