package divisioner.astObjects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.Map;
import java.util.HashMap;


/**
 * Helper functionality to capture children to parent mapping
 * -mapping is used to access parent from children (does not work properly if AST is changed)
 * 
 *  
 * @author Jakub Perdek
 *
 */
public class ASTObjectMatcher {

	/**
	 * Mapping from children to parent
	 */
	private Map<JSONObject, JSONObject> objectParent = new HashMap<JSONObject, JSONObject>();
	

	/**
	 * Creates the ASTObject mapper instance to capture mapping for astPart AST
	 * 
	 * @param astRoot - the root of AST
	 */
	public ASTObjectMatcher(JSONObject astRoot) {
		this.getParents(astRoot, astRoot);
	}
	

	/**
	 * Iterates through AST and harvests children to parent mapping 
	 * 
	 * @param astPart - actually processed AST element  
	 * @param astParent - the parent of actually processed AST element
	 */
	private void getParents(JSONObject astPart, JSONObject astParent) {
		String key;
		this.objectParent.put(astPart, astParent);

		Object entryValue;
		JSONObject entryJSONObject;
		for(Object entryKey: astPart.keySet()) {
			key = (String) entryKey;
			entryValue = astPart.get(key);
			if (entryValue instanceof JSONObject) {
				entryJSONObject = (JSONObject) entryValue;
				this.getParents(entryJSONObject, astPart);
			} else if(entryValue instanceof JSONArray) {
				for (Object arrayPart: ((JSONArray) entryValue)) {
					entryJSONObject = (JSONObject) arrayPart;
					this.getParents(entryJSONObject, astPart);
				}
			}
		}
	}

	
	/**
	 * Finds parent according to the child or returns null in case of no match
	 * 
	 * @param child - child element to find associated parent   
	 * @return the parent object associated with a given child element otherwise null
	 */
	public JSONObject findParent(JSONObject child) { return this.objectParent.get(child); }
	
	//public List<DivisionerObject> getPossibleDivisionerObjects() { return null; }
}
