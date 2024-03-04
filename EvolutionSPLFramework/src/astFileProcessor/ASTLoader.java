package astFileProcessor;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * Loads AST from string or from file
 * 
 * @author Jakub Perdek
 *
 */
public class ASTLoader {
	
	/**
	 * Root of the AST
	 */
	private JSONObject astTreeRoot = null;
	

	/**
	 * Loads AST from the file
	 * 
	 * @param fileName - the name of file
	 */
	public ASTLoader(String fileName) {
		this.astTreeRoot = ASTLoader.loadAST(fileName);
	}
	
	/**
	 * Loads AST from the file
	 * 
	 * @param astPath - path to AST
	 * @return JSON object with AST
	 */
	public static JSONObject loadAST(String astPath) {
		try {
	        JSONParser parser = new JSONParser();
	        JSONObject configurationObject = (JSONObject) parser.parse(new FileReader(astPath));
	        return configurationObject;
	    } catch (IOException | ParseException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	/**
	 * Loads AST from string
	 * 
	 * @param astString - the string representation that contains AST
	 * @return JSON object with AST
	 */
	public static JSONObject loadASTFromString(String astString) {
		try {
	        JSONParser parser = new JSONParser();
	        JSONObject configurationObject = (JSONObject) parser.parse(astString);
	        return configurationObject;
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
		return null;
	}
}
