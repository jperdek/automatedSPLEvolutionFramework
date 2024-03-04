package positiveVariabilityManagement.parameterVaribleMatchingStrategies;


/**
 * Strategies to apply and customize parameter matching
 * SHOULD BE ADAPTED TO USE DECORATORS
 * 
 * @author Jakub Perdek
 *
 */
public interface ParameterMatchingStrategy {
	
	/**
	 * Evaluates the similarity between two variable names
	 * 
	 * @param variableName1 - the first compared variable name/parameter
	 * @param variableName2 - the second compared variable name
	 * @return the evaluated value from the comparison process
	 */
	public double evaluateSimilarity(String variableName1, String variableName2);
}
