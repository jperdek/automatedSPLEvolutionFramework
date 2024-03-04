package codeConstructsEvaluation;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;


/**
 * Entity complexity difference interface
 * -interface to evaluate differences between complexities
 * 
 * @author Jakub Perdek
 *
 */
public interface EntityComplexityDifference {

	/**
	 * Calculates differences between the complexity measurements of two instances that implements this interface
	 * 
	 * @param compareWith - another instance that implements this interface to compare this instance with 
	 */
	public EntityComplexityDifference makeDifference(EntityComplexityDifference compareWith);
	
	/**
	 * Put column names into complexity record
	 * -adds base columns to the complexity record
	 * 
	 * @param columnNames - the list of column names 
	 * @param aggregated - if true then column names will be added to complexity record otherwise not
	 */
	public void putColumnName(List<String> columnNames, boolean aggregated);
	
	/**
	 * Returns the complexity record entry set
	 * 
	 * @return the complexity record entry set
	 */
	public Set<Entry<String,? extends ComplexityRecord>> getComplexityRecordsEntrySet();
	
	/**
	 * Put column names into CSV
	 * -adds base columns to the CSV
	 * 
	 * @param content - the list of column names 
	 * @param aggregated - if true then column names will be added to complexity record otherwise not
	 */
	public void writeToCSV(StringBuilder content, boolean aggregated);
	
	/**
	 * Prints the data stored in given entity that implements this interface
	 */
	public void print();
}
