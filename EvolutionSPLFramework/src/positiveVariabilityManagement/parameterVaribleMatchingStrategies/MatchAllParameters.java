package positiveVariabilityManagement.parameterVaribleMatchingStrategies;


/**
 * Strategies to match all parameters
 * 
 * @author Jakub Perdek
 *
 */
public class MatchAllParameters implements ParameterMatchingStrategy {

	@Override
	public double evaluateSimilarity(String variableName1, String variableName2) {
		return 1; //if -1, element will not be included 
	}
}
