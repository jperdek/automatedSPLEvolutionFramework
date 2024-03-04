package positiveVariabilityManagement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * The carrier for all related information to synthesize the final product
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class SynthesizedContent {
	
	/**
	 * The resulting AST with the synthesized content
	 */
	private JSONObject resultingAST;
	
	/**
	 * The JSON array of variation points representations with all related information (about these points) 
	 */
	private JSONArray variationPointsData;
	
	/**
	 * The name of processed base file that should be updated/changed/evolved
	 */
	private String fileName;
	
	
	/**
	 * Instantiates the carrier for all required information to synthesize content in the final product
	 * 
	 * @param templateAST - the base AST of unmodified script from which the copy of resulting AST with synthesized content will be initialized (copied and later updated with the positive variability)
	 * @param fileName - the name of processed base file that should be updated/changed/evolved
	 * @param variationPointsData - the JSON array of variation points representations with all related information (about these points)
	 */
	public SynthesizedContent(JSONObject templateAST, String fileName, JSONArray variationPointsData) {
		this.fileName = fileName;
		this.variationPointsData = variationPointsData;
		this.resultingAST = this.loadASTFromString(templateAST.toString());
	}
	
	/**
	 * Creates the copy of the (template) AST of base script
	 * -the positive variability content (new functionality/feature) will be later synthesized with this copy
	 * 
	 * @param astString - the converted (template) AST of base script into String
	 * @return the copy of the (template) AST of base script (the positive variability content (new functionality/feature) will be later synthesized with this AST)
	 */
	private static JSONObject loadASTFromString(String astString) {
		try {
	        JSONParser parser = new JSONParser();
	        JSONObject configurationObject = (JSONObject) parser.parse(astString);
	        return configurationObject;
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	/**
	 * Returns the name of processed base file that should be updated/changed/evolved
	 * 
	 * @return the name of processed base file that should be updated/changed/evolved
	 */
	public String getFileName() { return this.fileName; }

	/**
	 * Returns the reference to resulting AST with the synthesized content
	 * 
	 * @return the reference to resulting AST with the synthesized content
	 */
	public JSONObject getReferenceToProcessedAST() { return this.resultingAST; }
	
	/**
	 * Returns the JSON array of variation points representations with all related information (about these points) 
	 * 
	 * @return the JSON array of variation points representations with all related information (about these points) 
	 */
	public JSONArray getVariationPointsDataReferenceAST() { return this.variationPointsData; }
}
