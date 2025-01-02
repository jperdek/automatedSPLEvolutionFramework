package codeConstructsEvaluation.entities;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Representation of Cyclomatic complexity
 * 
 * @author Jakub Perdek
 *
 */
public class CyclomaticComplexity implements ComplexityMeasure {

	/**
	 * Logger to track information about cyclomatic complexity
	 */
	private static final Logger logger = LoggerFactory.getLogger(CyclomaticComplexity.class);
	
	/**
	 * Cyclomatic number
	 */
	private double cyclomaticNumber;
	
	/**
	 * Cyclomatic density
	 */
	private double cyclomaticDensity;
	
	
	/**
	 * Creates representation of Cyclomatic code complexity record
	 * 
	 * @param cyclomaticNumber - the evaluated Cyclomatic number
	 * @param cyclomaticDensity - the evaluated Cyclomatic density
	 */
	public CyclomaticComplexity(double cyclomaticNumber, double cyclomaticDensity) {
		this.cyclomaticNumber = cyclomaticNumber;
		this.cyclomaticDensity = cyclomaticDensity;	
	}
	
	/**
	 * Returns the evaluated Cyclomatic number
	 * 
	 * @return the evaluated Cyclomatic number
	 */
	public double getCyclomaticNumber() { return this.cyclomaticNumber; }
	
	/**
	 * Returns the evaluated Cyclomatic density
	 * 
	 * @return the evaluated Cyclomatic density
	 */
	public double getCyclomaticDensity() { return this.cyclomaticDensity; }
	
	/**
	 * Evaluates the difference between two Cyclomatic complexity measures that implement this interface
	 * 
	 * @param complexityMeasure1 - the first Cyclomatic complexity measures that implements this interface
	 * @param complexityMeasure2 - the second Cyclomatic complexity measures that implements this interface
	 * @return the difference between two Cyclomatic complexity measures that implements this interface
	 */
	public static CyclomaticComplexity getComplexityDifference(
			CyclomaticComplexity complexityMeasure1, CyclomaticComplexity complexityMeasure2) {
		double cyclomaticNumberDifference = complexityMeasure1.getCyclomaticNumber() - complexityMeasure2.getCyclomaticNumber();
		double cyclomaticDensityDifference = complexityMeasure1.getCyclomaticDensity() - complexityMeasure2.getCyclomaticDensity();
		return new CyclomaticComplexity(cyclomaticNumberDifference, cyclomaticDensityDifference);
	}

	@Override
	/**
	 * Evaluates the difference between two Cyclomatic complexity measures that implement this interface
	 * 
	 * @param complexityMeasure1 - the first Cyclomatic complexity measures that implements this interface
	 * @param complexityMeasure2 - the second Cyclomatic complexity measures that implements this interface
	 * @return the difference between two Cyclomatic complexity measures that implements this interface
	 */
	public ComplexityMeasure getComplexityDifference(ComplexityMeasure complexityMeasure1, ComplexityMeasure complexityMeasure2) {
		return CyclomaticComplexity.getComplexityDifference((CyclomaticComplexity) complexityMeasure1, (CyclomaticComplexity) complexityMeasure2);
	}
	
	@Override
	/**
	 * Prints information about this Cyclomatic code complexity measure 
	 */
	public void print() {
		logger.debug("____| Cyclomatic Complexity |____");
		logger.debug("---| cyclomatic number: " + this.cyclomaticNumber);
		logger.debug("---| cyclomatic density: " + this.cyclomaticDensity);
		logger.debug("-----------------------------------------");
	}

	/**
	 * Static methods to put column names for this representation of Cyclomatic code complexity record
	 * 
	 * @param columnNames - the column names that are going to be added to available columns
	 */
	public static void putColumnNameStatic(List<String> columnNames) {
		columnNames.add("Cycl. Number");
		columnNames.add("Cycl. Density");
	}
	
	/**
	 * Persists information in the CSV file - the static method
	 * 
	 * @param content
	 */
	public static void writeToCSVStatic(StringBuilder content) {
		content.append("");
		content.append(';');
		content.append("");
		content.append(';');
	}
	
	@Override
	/**
	 * Inserts the column names with Cyclomatic complexity information to available columns
	 * 
	 * @param columnNames - the column names that are going to be added to available columns
	 */
	public void putColumnName(List<String> columnNames) {
		columnNames.add("Cycl. Number");
		columnNames.add("Cycl. Density");
	}
	
	@Override
	/**
	 * Persists information in the CSV file
	 * 
	 * @param content - the Cyclomatic code complexity content that is going to be extended and persisted
	 */
	public void writeToCSV(StringBuilder content) {
		content.append(String.valueOf(this.cyclomaticNumber).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.cyclomaticDensity).replace(".", ","));
		content.append(';');
	}
}
