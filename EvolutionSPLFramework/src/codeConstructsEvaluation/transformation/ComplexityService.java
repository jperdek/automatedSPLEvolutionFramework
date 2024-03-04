package codeConstructsEvaluation.transformation;

import java.io.IOException;

import codeConstructsEvaluation.ComplexityMeasurement;


/**
 * Interface to unite the way how code complexity is measured with complexity services
 * 
 * @author Jakub Perdek
 *
 */
public interface ComplexityService {
	
	/**
	 * Returns the code complexity service name
	 * 
	 * @return the code complexity service name
	 */
	String getName();
	
	/**
	 * Cleans the file/script content to perform code complexity analysis with given service
	 * 
	 * @param content - the file/script content that is cleaned
	 * @return the cleaned file/script content to perform code complexity analysis
	 */
	String doCleaning(String content);
	
	/**
	 * Prepares/evaluates the complexity measurement 
	 * 
	 * @param fileName - the name of the file
	 * @param fileContent - the file/script content that is evaluated 
	 * @param storeFileContent - if file content should be stored
	 * @return the prepared complexity measurement
	 * @throws IOException
	 * @throws InterruptedException
	 */
	ComplexityMeasurement prepareComplexityMeasurement(String fileName, 
			String fileContent, boolean storeFileContent) throws IOException, InterruptedException;
}
