package divisioner;

import java.util.HashSet;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * Functionality to cut unimportant leaves during parsing of AST
 * 
 * @author Jakub Perdek
 *
 */
public class AstLeavesCutter {
	
	/**
	 * Set of AST keys to cut
	 */
	private Set<String> astKeysToCut;
	

	/**
	 * Creates instance AST leaves cutter
	 */
	public AstLeavesCutter() {
		this.astKeysToCut = new HashSet<String>();
		this.initializationForJavaScriptAst();
	}
	
	/**
	 * Initialized AST cutter for JavaScript/TypeScript AST
	 */
	private void initializationForJavaScriptAst() {
		this.astKeysToCut.add("name");
	}
	
	/**
	 * Decides if given AST part can be cut
	 * 
	 * @param keyName - the name of the key
	 * @param value - the processed AST part
	 * @return true if given instance can be cut otherwise false
	 */
	public boolean shouldCut(String keyName, Object value) {
		if (this.astKeysToCut.contains(keyName)) { return true; } 			// if it inside then its skipped
		if (!(value instanceof JSONArray || value instanceof JSONObject)) { // if its not JSON object or array it can be skipped
			return true;
		}
		return false;
	}
}
