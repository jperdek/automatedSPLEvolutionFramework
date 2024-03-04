package codeConstructsEvaluation;

import java.util.List;
import java.util.ArrayList;


/**
 * Collects all complexity measurements to efficiently and coherently operate on them
 * 
 * @author Jakub Perdek
 *
 */
public class ComplexityRecordsCollector {

	/**
	 * The list of associated code complexity measurements
	 */
	private List<ComplexityMeasurement> complexityMeasurements;
	
	
	/**
	 * Creates the collector to aggregate selected complexity measurements to efficiently and coherently operate on them
	 */
	public ComplexityRecordsCollector() {
		this.complexityMeasurements = new ArrayList<ComplexityMeasurement>();
	}
	
	/**
	 * Inserts code complexity measurement into this collector
	 *  
	 * @param complexityMeasurement - the complexity measurement that should be aggregated in this colector
	 */
	public void addMeasurement(ComplexityMeasurement complexityMeasurement) {
		this.complexityMeasurements.add(complexityMeasurement);
	}
	
	/**
	 * Returns the code complexity measurement that is stored on given index
	 * 
	 * @param index - the index of given code complexity measurement
	 * @return the code complexity measurement that is stored on given index
	 */
	public ComplexityMeasurement getMeasurement(int index) {
		return this.complexityMeasurements.get(index);
	}
	
	/**
	 * Returns all aggregated measurements
	 * 
	 * @return all aggregated measurements
	 */
	public List<ComplexityMeasurement> getMeasurements() { return this.complexityMeasurements; }
}
