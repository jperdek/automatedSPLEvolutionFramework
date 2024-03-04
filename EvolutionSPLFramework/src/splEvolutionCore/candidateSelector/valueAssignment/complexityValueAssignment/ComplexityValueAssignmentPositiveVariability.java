package splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import codeConstructsEvaluation.transformation.TyphonTypeScriptComplexityAnalysis;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;
import splEvolutionCore.candidateSelector.valueAssignment.AssignedValue;
import splEvolutionCore.candidateSelector.valueAssignment.ChosenValueAssignmentStrategyForPositiveVariability;


/**
 * Evaluates code complexity metrics for positive variability  
 * -uses the external service
 * 
 * @author Jakub Perdek
 *
 */
public class ComplexityValueAssignmentPositiveVariability implements ChosenValueAssignmentStrategyForPositiveVariability {

	/**
	 * Stores available measurements, used metrics for positive variability
	 * -name
	 * -importance
	 * -the orientation of the scale referring what is better
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum ComplexityMeasureNamesPositiveVariability {
		LOC_PHYSICAL("locPhysical", 0.2, false),
		LOC_LOGICAL("locLogical", 0.2, false),
		HALSTEAD_BUGS("halsteadBugs", 0.5, false),
		HALSTEAD_DIFFICULTY("halsteadDifficulty", 0.5, false),
		HALSTEAD_EFFORT("halsteadEffort", 0.5, false),
		HALSTEAD_LENGTH("halsteadLength", 0.5, false),
		HALSTEAD_TIME("halsteadTime", 0.5, false),
		HALSTEAD_VOCABULARY("halsteadVocabulary", 0.5, false),
		HALSTEAD_VOLUME("halsteadVolume", 0.5, false),
		CYCLOMATIC_NUMBER("cyclomaticNumber", 1, false),
		CYCLOMATIC_DENSITY("cyclomaticDensity", 1, false);
		
		/**
		 * The name of used metric
		 */
		public final String label;
		/**
		 * The importance associated with given metric, he importance of given metric
		 */
		public final double importance;
		/**
		 * Refers to orientation of the scale to compare two measurements
		 */
		public final boolean higherBetter;
		
		/**
		 * Creates the name of code complexity measurement with associated scales referring to its impact
		 * -intended for positive variability
		 * 
		 * @param label - the name of used metric
		 * @param importance - the importance of given metric
		 * @param higherBetter - refers to orientation of the scale to compare two measurements
		 */
	    private ComplexityMeasureNamesPositiveVariability(String label, double importance, boolean higherBetter) {
	        this.label = label;
	        this.importance = importance;
	        this.higherBetter = higherBetter;
	    }
	    
	    /**
	     * Returns the name of used metric
	     * 
	     * @return the name of given metric
	     */
	    public String getName() { return this.label; }
	    
	    /**
	     * Returns the importance of given metric
	     * 
	     * @return the importance of given metric
	     */
	    public double getImportance() { return this.importance; }
	}
	
	/**
	 * Creates the ComplexityValueAssignmentPositiveVariability instance for
	 *  measuring complexity of positive variability variation point
	 */
	public ComplexityValueAssignmentPositiveVariability() {
	}
	
	/**
	 * Loads number from measurement that is stored as JSON object
	 * -it can be stored as long or double
	 * 
	 * @param recordPart - JSON object from which measured value as double should be extracted
	 * @return measured value loaded as a value
	 */
	private double loadDobleOrLongAsDouble(Object recordPart) {
		if (recordPart == null) { return 0.0; }

		if (recordPart instanceof Long) {
			return ((Long) recordPart).doubleValue();
		} else {
			return (double) recordPart;
		}
	}
	
	/**
	 * Assigns the code complexity values from measured metrics
	 * 
	 * @param complexityMeasures - JSON object with measurements, measured code complexity metrics
	 * @return measurements represented as the mapping of their names to associated measured values
	 */
	private Map<String, AssignedValue> assignComplexityValues(JSONObject complexityMeasures) {
		Map<String, AssignedValue> assignedValues = new HashMap<String, AssignedValue>();
		JSONObject halsteadMeasures = (JSONObject) complexityMeasures.get("halstead");
		JSONObject slocMeasures = (JSONObject) complexityMeasures.get("sloc");
		AssignedValue cyclomaticNumber = new AssignedValue(
				(double) this.loadDobleOrLongAsDouble(complexityMeasures.get("cyclomatic")), 
						ComplexityMeasureNamesPositiveVariability.CYCLOMATIC_NUMBER);
		assignedValues.put(ComplexityMeasureNamesPositiveVariability.CYCLOMATIC_NUMBER.label, cyclomaticNumber);
		AssignedValue cyclomaticDensity = new AssignedValue(
				(double) loadDobleOrLongAsDouble(complexityMeasures.get("cyclomaticDensity")), 
				ComplexityMeasureNamesPositiveVariability.CYCLOMATIC_DENSITY);
		assignedValues.put(ComplexityMeasureNamesPositiveVariability.CYCLOMATIC_DENSITY.label, cyclomaticDensity);
		
		AssignedValue halsteadBugs = new AssignedValue(
				(double) loadDobleOrLongAsDouble(halsteadMeasures.get("bugs")), 
				ComplexityMeasureNamesPositiveVariability.HALSTEAD_BUGS);
		assignedValues.put(ComplexityMeasureNamesPositiveVariability.HALSTEAD_BUGS.label, halsteadBugs);
		AssignedValue halsteadDifficulty = new AssignedValue(
				(double) loadDobleOrLongAsDouble(complexityMeasures.get("difficulty")),
				ComplexityMeasureNamesPositiveVariability.HALSTEAD_DIFFICULTY);
		assignedValues.put(ComplexityMeasureNamesPositiveVariability.HALSTEAD_DIFFICULTY.label, halsteadDifficulty);
		AssignedValue halsteadEffort = new AssignedValue(
				(double) loadDobleOrLongAsDouble(complexityMeasures.get("effort")),
				ComplexityMeasureNamesPositiveVariability.HALSTEAD_EFFORT);
		assignedValues.put(ComplexityMeasureNamesPositiveVariability.HALSTEAD_EFFORT.label, halsteadEffort);
		AssignedValue halsteadTime = new AssignedValue(
				(double) loadDobleOrLongAsDouble(complexityMeasures.get("time")),
				ComplexityMeasureNamesPositiveVariability.HALSTEAD_TIME);
		assignedValues.put(ComplexityMeasureNamesPositiveVariability.HALSTEAD_TIME.label, halsteadTime);
		AssignedValue halsteadVocabulary = new AssignedValue(
				(double) loadDobleOrLongAsDouble(complexityMeasures.get("vocabulary")),
				ComplexityMeasureNamesPositiveVariability.HALSTEAD_VOCABULARY);
		assignedValues.put(ComplexityMeasureNamesPositiveVariability.HALSTEAD_VOCABULARY.label, halsteadVocabulary);
		AssignedValue halsteadVolume = new AssignedValue(
				(double) loadDobleOrLongAsDouble(complexityMeasures.get("volume")),
				ComplexityMeasureNamesPositiveVariability.HALSTEAD_VOLUME);
		assignedValues.put(ComplexityMeasureNamesPositiveVariability.HALSTEAD_VOLUME.label, halsteadVolume);
		
		AssignedValue slocLogical = new AssignedValue(
				(double) loadDobleOrLongAsDouble(slocMeasures.get("logical")),
				ComplexityMeasureNamesPositiveVariability.LOC_LOGICAL);
		assignedValues.put(ComplexityMeasureNamesPositiveVariability.LOC_LOGICAL.label, slocLogical);
		AssignedValue slocPhysical = new AssignedValue(
				(double) loadDobleOrLongAsDouble(slocMeasures.get("physical")),
				ComplexityMeasureNamesPositiveVariability.LOC_PHYSICAL);
		assignedValues.put(ComplexityMeasureNamesPositiveVariability.LOC_PHYSICAL.label, slocPhysical);
		return assignedValues;
	}

	@Override
	/**
	 * Assigns code complexity values that are measured on positive variability of callable constructs or templates that will be merged with base code
	 * to their type, importance and scale orientation
	 * -decomposes classes into methods and finds associated method with evaluated measurement
	 * -otherwise takes general/aggregated code complexity results consisting of related metrics
	 * 
	 * @param variationPointData - the data with information about (negative - existing business functionality) variation points
	 * @param complexityMeasures - JSON object with measurements, measured code complexity metrics
	 * @return measurements represented as the mapping of their names to associated measured values
	 * @throws MethodToEvaluateComplexityNotFoundException
	 */
	public Map<String, AssignedValue> getStrategyAssignedValues(
			PositiveVariationPointCandidateTemplates positiveVariationPointCandidateTemplates,
			String functionalityCall) throws MethodToEvaluateComplexityNotFoundException, IOException, InterruptedException {
		TyphonTypeScriptComplexityAnalysis typhoneTypeScriptComplexityAnalysis =  new TyphonTypeScriptComplexityAnalysis();
		
		if (functionalityCall.strip().startsWith(".")) {
			functionalityCall = functionalityCall.strip().substring(1); 
		}
		JSONObject evaluatedComplexities = typhoneTypeScriptComplexityAnalysis.getJSONComplexityReport("", functionalityCall);
		Map<String, AssignedValue> assignedValues = this.assignComplexityValues((JSONObject) evaluatedComplexities.get("aggregate"));
		
		JSONArray arrayOfClasses = (JSONArray) evaluatedComplexities.get("classes");
		JSONObject classResult, methodResult;
		for (Object classResultObject: arrayOfClasses) {
			classResult = (JSONObject) classResultObject;
			for (Object classMethodObject: (JSONArray) classResult.get("methods")) {
				methodResult = (JSONObject) classMethodObject;
				assignedValues.putAll(this.assignComplexityValues((JSONObject) methodResult.get("aggregate")));
			}
		}
		return assignedValues;
	}
}
