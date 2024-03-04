package splEvolutionCore.candidateSelector.valueAssignment;

import splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment.ComplexityValueAssignmentNegativeVariability.ComplexityMeasureNamesNegativeVariability;
import splEvolutionCore.candidateSelector.valueAssignment.complexityValueAssignment.ComplexityValueAssignmentPositiveVariability.ComplexityMeasureNamesPositiveVariability;

/**
 * Representation of quantitative measurement
 * -its value
 * -its importance
 * -the position of scales - if more is better
 * 
 * @author Jakub Perdek
 *
 */
public class AssignedValue {

	/**
	 * The measured value that is stored as decimal number
	 */
	private double measuredValue;
	
	/**
	 * The importance of measuremnt in comparison with other measurements
	 */
	private double importance;
	
	/**
	 * The position of scale - if more is better, or less
	 */
	private boolean higherBetter = true;
	

	/**
	 * Creates representation of given quantitative measurement
	 * 
	 * @param measuredValue - measured value that is stored as decimal number
	 * @param importance - the importance of measuremnt in comparison with other measurements
	 * @param higherBetter - the position of scale - if more is better, or less
	 */
	public AssignedValue(double measuredValue, double importance, boolean higherBetter) {
		this.measuredValue = measuredValue;
		this.importance = importance;
		this.higherBetter = higherBetter;
	}
	
	/**
	 * Creates representation of given quantitative measurement from negative code complexity measurement
	 * 
	 * @param measuredValue - measured value that is stored as decimal number
	 * @param locLogical - negative variability code complexity measurement that is used to extract information from the code complexity measurement
	 */
	public AssignedValue(double measuredValue, ComplexityMeasureNamesNegativeVariability locLogical) {
		this.measuredValue = measuredValue;
		this.importance = locLogical.importance;
		this.higherBetter = locLogical.higherBetter;
	}
	
	/**
	 * Creates representation of given quantitative measurement from positive code complexity measurement
	 * 
	 * @param measuredValue - measured value that is stored as decimal number 
	 * @param locLogical - positive variability code complexity measurement that is used to extract information from the code complexity measurement
	 */
	public AssignedValue(double measuredValue, ComplexityMeasureNamesPositiveVariability locLogical) {
		this.measuredValue = measuredValue;
		this.importance = locLogical.importance;
		this.higherBetter = locLogical.higherBetter;
	}

	/**
	 * Returns importance connected with assigned value from previously performed measurement
	 * 
	 * @return the importance of this measurement in comparison with other measurements
	 */
	public double getImportance() { return this.importance; }
	
	/**
	 * Returns decimal measured value (metric)
	 * 
	 * @return decimal value as outcome of performed measurement
	 */
	public double getMeasuredValue() { return this.measuredValue; }
	
	/**
	 * Overall value calculated with use of custom algorithm
	 * -this function can be overridden to customize given strategy 
	 * 
	 * @return calculated overall value from the measured value, its importance and preference given by orientation of scales
	 */
	public double calculateOverallValue() {
		if (this.higherBetter) {
			return this.importance * this.measuredValue;
		}
		return this.importance * -this.measuredValue;
	}
}
