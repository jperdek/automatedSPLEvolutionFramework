package splEvolutionCore.candidateSelector.valueAssignment.cleaning;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codeContext.processors.ASTTextExtractorTools;
import divisioner.VariationPointDivisionConfiguration;
import splEvolutionCore.candidateSelector.valueAssignment.AssignedValueProcessForPositiveVariability;


/**
 * Removes all positive variability markers from AST
 * 
 * @author Jakub Perdek
 *
 */
public class AllMarkerRemover {
	
	/**
	 * Logger to track removal of positive variability markers 
	 */
	private static final Logger logger = LoggerFactory.getLogger(AllMarkerRemover.class);
	
	/**
	 * Instantiates AllMarkerRemover
	 */
	public AllMarkerRemover() {}
	
	/**
	 * Recursive search for positive variability markers in application AST
	 * 
	 * @param astRoot - the root of processed AST to remove all positive variability markers from it
	 * @param astPart - actually processed part of AST where positive variability markers are found and removed
	 */
	public static void removeAllPositiveVariabilityMarkers(JSONObject astRoot, JSONObject astPart) {
		String key;
		if (astPart == null) { return; }
		
		Object entryValue;
		JSONObject entryJSONObject, statementPart;
		JSONObject declarationList;
		JSONArray entryArray, declarationsArray;
		List<JSONObject> positiveVariabilityStatementsToRemove;

		for(Object entryKey: astPart.keySet()) {
			key = (String) entryKey;
			entryValue = astPart.get(key);
			if (entryValue instanceof JSONObject) {
				entryJSONObject = (JSONObject) entryValue;
				AllMarkerRemover.removeAllPositiveVariabilityMarkers(astRoot, entryJSONObject);
			} else if(entryValue instanceof JSONArray) {
				entryArray = (JSONArray) entryValue;
				
				positiveVariabilityStatementsToRemove = new ArrayList<JSONObject>();
				if (key.equals("statements")) {
					for (Object statementObject: entryArray) {
						statementPart = (JSONObject) statementObject;
						
						if (statementPart.containsKey("declarationList")) {
							declarationList = (JSONObject) statementPart.get("declarationList");
							declarationsArray = (JSONArray) declarationList.get("declarations");
							for (int index = declarationsArray.size() - 1; index >= 0 ; index--) {
								entryJSONObject = (JSONObject) declarationsArray.get(index);
								if (AllMarkerRemover.checkIfIsPositiveVariabilityMarker(entryJSONObject)) {
									positiveVariabilityStatementsToRemove.add(statementPart);
									break;
								}
							}
						}
 					}
				} else if (key.equals("members"))  {
					for (Object memberPart: entryArray) {
						entryJSONObject = (JSONObject) memberPart;
						if (AllMarkerRemover.checkIfIsPositiveVariabilityMarker(entryJSONObject)) {
							positiveVariabilityStatementsToRemove.add(entryJSONObject);
						}
					}
				} 
				
				for (Object entryObj: entryArray) {
					entryJSONObject = (JSONObject) entryObj;
					AllMarkerRemover.removeAllPositiveVariabilityMarkers(astRoot, entryJSONObject);
				}
				
				for (JSONObject statementToRemove: positiveVariabilityStatementsToRemove) {
					if (!entryArray.remove(statementToRemove)) {
						logger.debug("Posistive variation point NOT REMOVED!!!!");
					}
				}
			}
		}
	}
	
	/**
	 * Verifies if given marker belongs to positive variability according to the name of the marker
	 *  
	 * @param markerAst - actually processed marker in form of AST
	 * @return true if marker starts with VariationPointDivisionConfiguration.MARKER_VP_NAME otherwise false
	 */
	private static boolean checkIfIsPositiveVariabilityMarker(JSONObject markerAst) {
		String markerName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(markerAst);
		if (markerName == null) { markerName = ""; }
		return markerName.startsWith(VariationPointDivisionConfiguration.MARKER_VP_NAME);
	}
}
