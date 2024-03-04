package splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import codeConstructsEvaluation.transformation.ASTConverterClient;
import codeConstructsEvaluation.transformation.TyphonTypeScriptComplexityAnalysis;
import splEvolutionCore.candidateSelector.NegativeVariationPointCandidate;
import splEvolutionCore.candidateSelector.valueAssignment.AssignedValue;
import splEvolutionCore.candidateSelector.valueAssignment.ChosenValueAssignmentStrategyForNegativeVariability;
import splEvolutionCore.candidateSelector.valueAssignment.cleaning.AllMarkerRemover;
import variationPointsVisualization.AnnotationExtensionMarker;


/**
 * Evaluates code complexity metrics for negative variability  
 * -uses the external service
 * 
 * @author Jakub Perdek
 *
 */
public class ComplexityValueAssignmentNegativeVariability implements ChosenValueAssignmentStrategyForNegativeVariability {

	/**
	 * Stores available measurements, used metrics for negative variability
	 * -name
	 * -importance
	 * -the orientation of the scale referring what is better
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum ComplexityMeasureNamesNegativeVariability {
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
		 * Creates the name of code complexity measurement with associated scales refering to its impact
		 * -intended for negative variability
		 * 
		 * @param label - the name of used metric
		 * @param importance - the importance of given metric
		 * @param higherBetter - refers to orientation of the scale to compare two measurements
		 */
	    private ComplexityMeasureNamesNegativeVariability(String label, double importance, boolean higherBetter) {
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
	 * Creates the ComplexityValueAssignmentNegativeVariability instance for
	 *  measuring complexity of negative variability variation point
	 */
	public ComplexityValueAssignmentNegativeVariability() {
	}
	
	//VPDividedExporter 
	/**
	 * Prepares affected code and applies optional transformations to measure its complexity
	 * -inserts class variable into class
	 * -extracts function from the class
	 * -extracts entire code associated with processed variation point - part of variation point data
	 * 
	 * @param variationPointData - the information associated with given variation point, the code is also included
	 * @return extracted code from given variation point
	 */
	private String prepareAffectedCode(JSONObject variationPointData) {
		String affectedCode = (String) variationPointData.get("affectedCode");
		String annotationVPType = (String) variationPointData.get("annotationVPType");
		boolean isClassRelated = (Boolean) variationPointData.get("classRelated");

		if (annotationVPType.equals(AnnotationExtensionMarker.SystemAnnotationType.CLASS.label)) {
			return (String) variationPointData.get("affectedCode");
		} else if (annotationVPType.equals(AnnotationExtensionMarker.SystemAnnotationType.CLASS_VARIABLE.label) || isClassRelated) {
			affectedCode = (String) variationPointData.get("affectedCode");
			affectedCode = "class BB { " + affectedCode + " }";
			return affectedCode;
		} else if(annotationVPType.equals(AnnotationExtensionMarker.SystemAnnotationType.CLASS_FUNCTION.label)) {
			affectedCode = (String) variationPointData.get("affectedCodeParent"); //analyze parent instead and get analysis for function
			return affectedCode;
		} 
		return (String) variationPointData.get("affectedCode");
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
						ComplexityMeasureNamesNegativeVariability.CYCLOMATIC_NUMBER);
		assignedValues.put(ComplexityMeasureNamesNegativeVariability.CYCLOMATIC_NUMBER.label, cyclomaticNumber);
		AssignedValue cyclomaticDensity = new AssignedValue(
				(double) this.loadDobleOrLongAsDouble(complexityMeasures.get("cyclomaticDensity")),
				ComplexityMeasureNamesNegativeVariability.CYCLOMATIC_DENSITY);
		assignedValues.put(ComplexityMeasureNamesNegativeVariability.CYCLOMATIC_DENSITY.label, cyclomaticDensity);
		
		AssignedValue halsteadBugs = new AssignedValue(
				(double) this.loadDobleOrLongAsDouble(halsteadMeasures.get("bugs")), ComplexityMeasureNamesNegativeVariability.HALSTEAD_BUGS);
		assignedValues.put(ComplexityMeasureNamesNegativeVariability.HALSTEAD_BUGS.label, halsteadBugs);
		AssignedValue halsteadDifficulty = new AssignedValue(
				(double) this.loadDobleOrLongAsDouble(halsteadMeasures.get("difficulty")), ComplexityMeasureNamesNegativeVariability.HALSTEAD_DIFFICULTY);
		assignedValues.put(ComplexityMeasureNamesNegativeVariability.HALSTEAD_DIFFICULTY.label, halsteadDifficulty);
		AssignedValue halsteadEffort = new AssignedValue(
				(double) this.loadDobleOrLongAsDouble(halsteadMeasures.get("effort")), ComplexityMeasureNamesNegativeVariability.HALSTEAD_EFFORT);
		assignedValues.put(ComplexityMeasureNamesNegativeVariability.HALSTEAD_EFFORT.label, halsteadEffort);
		AssignedValue halsteadTime = new AssignedValue(
				(double) this.loadDobleOrLongAsDouble(halsteadMeasures.get("time")), ComplexityMeasureNamesNegativeVariability.HALSTEAD_TIME);
		assignedValues.put(ComplexityMeasureNamesNegativeVariability.HALSTEAD_TIME.label, halsteadTime);
		AssignedValue halsteadVocabulary = new AssignedValue(
				(double) this.loadDobleOrLongAsDouble(halsteadMeasures.get("vocabulary")), ComplexityMeasureNamesNegativeVariability.HALSTEAD_VOCABULARY);
		assignedValues.put(ComplexityMeasureNamesNegativeVariability.HALSTEAD_VOCABULARY.label, halsteadVocabulary);
		AssignedValue halsteadVolume = new AssignedValue(
				(double) this.loadDobleOrLongAsDouble(halsteadMeasures.get("volume")), ComplexityMeasureNamesNegativeVariability.HALSTEAD_VOLUME);
		assignedValues.put(ComplexityMeasureNamesNegativeVariability.HALSTEAD_VOLUME.label, halsteadVolume);
		
		AssignedValue slocLogical = new AssignedValue(
				(double) this.loadDobleOrLongAsDouble(slocMeasures.get("logical")), ComplexityMeasureNamesNegativeVariability.LOC_LOGICAL);
		assignedValues.put(ComplexityMeasureNamesNegativeVariability.LOC_LOGICAL.label, slocLogical);
		AssignedValue slocPhysical = new AssignedValue(
				(double) this.loadDobleOrLongAsDouble(slocMeasures.get("physical")), ComplexityMeasureNamesNegativeVariability.LOC_PHYSICAL);
		assignedValues.put(ComplexityMeasureNamesNegativeVariability.LOC_PHYSICAL.label, slocPhysical);
		return assignedValues;
	}

	/**
	 * Assigns code complexity values that are measured on negative variability to their type, importance and scale orientation
	 * -decomposes classes into methods and finds associated method with evaluated measurement
	 * -otherwise takes general/aggregated code complexity results consisting of related metrics
	 * 
	 * @param variationPointData - the data with information about (negative - existing business functionality) variation points
	 * @param complexityMeasures - JSON object with measurements, measured code complexity metrics
	 * @return measurements represented as the mapping of their names to associated measured values
	 * @throws MethodToEvaluateComplexityNotFoundException
	 */
	private Map<String, AssignedValue> assignComplexityValuesAccordingToType(
			JSONObject variationPointData, JSONObject complexityMeasures) throws MethodToEvaluateComplexityNotFoundException {
		JSONObject analyzedComplexityMeasuresPart;
		JSONArray analyzedComplexityMethodsMeasures;
		String methodName, analyzedMethodName;
		String annotationVPType = (String) variationPointData.get("annotationVPType");
		//boolean isClassRelated = (Boolean) variationPointData.get("classRelated");
		if (annotationVPType.equals(AnnotationExtensionMarker.SystemAnnotationType.CLASS_FUNCTION.label) 
				&& annotationVPType.equals(AnnotationExtensionMarker.SystemAnnotationType.FUNCTION.label)){
			analyzedComplexityMethodsMeasures = (JSONArray) complexityMeasures.get("methods");
			analyzedMethodName = (String) variationPointData.get("variationPointName");

			for (Object methodMeasurement: analyzedComplexityMethodsMeasures) {
				analyzedComplexityMeasuresPart = (JSONObject) methodMeasurement;
				methodName = (String) analyzedComplexityMeasuresPart.get("name");
				if (methodName.equals(analyzedMethodName)) {
					return this.assignComplexityValues(analyzedComplexityMeasuresPart);
				}
			}
			
			throw new MethodToEvaluateComplexityNotFoundException("Method with name " + analyzedMethodName + " has not been found.");
		}
		analyzedComplexityMeasuresPart = (JSONObject) complexityMeasures.get("aggregate");
		return this.assignComplexityValues(analyzedComplexityMeasuresPart);
	}
	
	/**
	 * Assigns values/measurements to this code complexity (quality value) assignment strategy 
	 * -instantiation of the service to perform complexity measurements
	 * -converts AST to code
	 * -evaluates the code complexities - of each code fragment or as the whole variation point
	 * -migrates complexity values to the map of the complexity name and represented assigned value
	 * 
	 * @param negativeVariationPointCandidate - negative variation point candidate which contains information about variation point 
	 * @return measurements represented as the mapping of their names to associated measured values
	 */
	public Map<String, AssignedValue> getStrategyAssignedValues(NegativeVariationPointCandidate negativeVariationPointCandidate) throws MethodToEvaluateComplexityNotFoundException, IOException, InterruptedException {
		JSONObject variationPointData = negativeVariationPointCandidate.getVariationPointData();
		String affectedCode = this.prepareAffectedCode(variationPointData);
		JSONObject affectedCodeAst;
		String fileName = "";

		affectedCode = affectedCode.replace("\\n", "").replace("\\t", "").replace("\\r", "");
		affectedCodeAst = ASTConverterClient.convertFromCodeToASTJSON(affectedCode);
		AllMarkerRemover.removeAllPositiveVariabilityMarkers(affectedCodeAst, affectedCodeAst);
		affectedCode = ASTConverterClient.convertFromASTToCode(affectedCodeAst.get("ast").toString());
		TyphonTypeScriptComplexityAnalysis typhoneTypeScriptComplexityAnalysis = new TyphonTypeScriptComplexityAnalysis();
		
		JSONObject evaluatedComplexities = typhoneTypeScriptComplexityAnalysis.getJSONComplexityReport(fileName, affectedCode);
		Map<String, AssignedValue> assignedValues = this.assignComplexityValuesAccordingToType(variationPointData, evaluatedComplexities);
		
		return assignedValues;
	}
}
