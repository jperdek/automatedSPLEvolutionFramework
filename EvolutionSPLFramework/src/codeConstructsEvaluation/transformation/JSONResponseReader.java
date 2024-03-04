package codeConstructsEvaluation.transformation;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * Reads the JSON response from the string
 * 
 * @author Jakub Perdek
 *
 */
public class JSONResponseReader {

	/**
	 * Creates the JSON reader object
	 */
	public JSONResponseReader() {}
	
	/**
	 * Reads the JSON response from the string
	 * -parses the string and  loads the JSON content
	 * 
	 * @param jsonContent - string with stored JSON content
	 * @return
	 */
	public static JSONObject loadJSONConfig(String jsonContent) {
		try {
	        JSONParser parser = new JSONParser();
	        JSONObject configurationObject = (JSONObject) parser.parse(jsonContent);
	        return configurationObject;
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
		return null;
	}
}
