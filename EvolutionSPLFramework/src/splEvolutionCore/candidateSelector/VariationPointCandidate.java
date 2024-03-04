package splEvolutionCore.candidateSelector;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.json.simple.JSONObject;


/**
 * Base representation of variation point information
 * -not restricted to positive or negative variability
 * 
 * @author Jakub Perdek
 *
 */
public class VariationPointCandidate {

	/**
	 * Associated information with variation point including code, positions in AST, user annotations and other attributes
	 * -the structure can vary for positive and negative variation points
	 */
	protected JSONObject variationPointData;
	
	/**
	 * The name of variation point
	 */
	protected String variationPointName;


	/**
	 * Creates the base representation of variation point
	 * - the name is automatically observed from associated information if available
	 * - the name consists of identifier, hash from the whole data and the associated construct name
	 * 
	 * @param variationPointData - information from variation point
	 */
	public VariationPointCandidate(JSONObject variationPointData) {
		this.variationPointData = variationPointData;
		String affectedCode = (String) variationPointData.get("affectedCode");
		String affectedCodeHash = "";
		MessageDigest digest;
		
		if (affectedCode != null) {
			try {
				digest = MessageDigest.getInstance("SHA-256");
				affectedCodeHash = digest.digest(affectedCode.getBytes(StandardCharsets.UTF_8)).toString();
			} catch (NoSuchAlgorithmException e) {
			}
		}
		this.variationPointName = this.getIdentifier() + "_" + affectedCodeHash + "_" + (String) variationPointData.get("variationPointName");
	}
	
	/**
	 * Returns available information about variation point in JSON object
	 * 
	 * @return variation point data - associated information with variation point
	 */
	public JSONObject getVariationPointData() { return this.variationPointData; }

	/**
	 * The base form of variation point identifier given by its start and end positions in AST
	 * 
	 * @return the string identifier of the variation point is given by its start and end position
	 */
	public String getIdentifier() {
		Long candidateStartPosition = (Long) this.getLongAsInteger(variationPointData.get("startPosition"));
		Long candidateEndPosition = (Long) this.getLongAsInteger(variationPointData.get("endPosition"));
		return candidateStartPosition.toString() + "_" + candidateEndPosition.toString();
	}
	
	/**
	 * Loads integer value or long value and converts it to integer
	 * 
	 * @param numberObject - object (previously from JSON) that is integer or long and should be converted to integer
	 * @return integer value from provided numberObject
	 */
	public Long getLongAsInteger(Object numberObject) {
		if (numberObject instanceof Integer) { return ((Integer) numberObject).longValue(); }
		return (Long) numberObject;
	}
	
	/**
	 * Extracts start and end positions from variation point data (these are from AST)
	 * -extraction from AST can be applied
	 * 
	 * @param variationPointData - associated information with variation point including code, positions in AST, user annotations and other attributes
	 * @return the string identifier of the variation point is given by its start and end position
	 */
	public static String getIdentifierFromAst(JSONObject variationPointData) {
		Long candidateStartPosition = (Long) variationPointData.get("pos");
		Long candidateEndPosition = (Long) variationPointData.get("end");
		return candidateStartPosition.toString() + "_" + candidateEndPosition.toString();
	}
	
	/**
	 * Returns variation point name in the form as was previously generated
	 * 
	 * @return variation point name in the form as was previously generated
	 */
	public String getVariationPointName() { return this.variationPointName; }
}
