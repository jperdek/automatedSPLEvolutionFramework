package codeConstructsEvaluation.entities;

import java.util.List;


/**
 * Interface to process code complexity results
 * -to make difference
 * -to persist or output code complexity information in various ways
 * 
 * @author Jakub Perdek
 *
 */
public interface ComplexityMeasure {

	/**
	 * Evaluates the difference between two complexity measures that implement this interface
	 * 
	 * @param complexityMeasure1 - the first complexity measures that implements this interface
	 * @param complexityMeasure2 - the second complexity measures that implements this interface
	 * @return the difference between two complexity measures that implements this interface
	 */
	public ComplexityMeasure getComplexityDifference(ComplexityMeasure complexityMeasure1, ComplexityMeasure complexityMeasure2);
	
	/**
	 * Inserts the column names to available columns
	 * 
	 * @param columnNames - the column names that are going to be added to available columns
	 */
	public void putColumnName(List<String> columnNames);
	
	/**
	 * Persists information in the CSV file
	 * 
	 * @param content - the content that is going to be extended and persisted
	 */
	public void writeToCSV(StringBuilder content);
	
	/**
	 * Prints information about given code complexity measure 
	 */
	public void print();
}
