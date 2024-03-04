package positiveVariabilityManagement.parameterVaribleMatchingStrategies;

import java.util.HashMap;
import java.util.Map;


/**
 * Similarity matching strategy for parameters
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class SimilarityMatchingOfParameters implements ParameterMatchingStrategy {
	
	/**
	 * Mapping of parameter names to similarity value
	 */
	private Map<String, Double> similarityMatchingMap = null;
	
	/**
	 * Delimiter string that is put between two parameter names to assign similarity to them
	 */
	private static final String DELIMITER = "[!=!]";
	
	
	/**
	 * Creates instance/initializes to handle similarity matching of function parameters
	 */
	public SimilarityMatchingOfParameters() {
		this.similarityMatchingMap = new HashMap<String, Double>();
	}
	
	/**
	 * Creates/initializes instance to handle similarity matching of function parameters pairs
	 * 
	 * @param similarityMatching - the similarity matching between parameter pairs
	 */
	public SimilarityMatchingOfParameters(Map<String, Double> similarityMatching) {
		this.similarityMatchingMap = new HashMap<String, Double>(similarityMatching);
	}
	
	/**
	 * Assigns similarity between variable pair
	 * -the value is stored under variables in map delimited by SimilarityMatchingOfParameters.DELIMITER
	 * 
	 * @param variableName1 - the first variable/parameter name
	 * @param variableName2 - the second variable/parameter name
	 * @param similarityValue - the similarity for both values
	 */
	public void assignSimilarityBetweenVariables(String variableName1, String variableName2, double similarityValue) {
		String similarityPair = variableName1 + SimilarityMatchingOfParameters.DELIMITER + variableName2;
		this.similarityMatchingMap.put(similarityPair, similarityValue);
	}
	
	@Override
	/**
	 * Returns/evaluates the similarity between two variables
	 * 
	 * @param variableName1 - the first variable/parameter name
	 * @param variableName2 - the second variable/parameter name
	 * @param the similarity value between two variables
	 */
	public double evaluateSimilarity(String variableName1, String variableName2) {
		String similarityPair = variableName1 + SimilarityMatchingOfParameters.DELIMITER + variableName2;
		if(this.similarityMatchingMap.containsKey(similarityPair)) {
			return this.similarityMatchingMap.get(similarityPair);
		}
		return 1;
	}
}
