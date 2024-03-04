package variationPointsVisualization;

import org.json.simple.JSONObject;
import codeConstructsEvaluation.transformation.JSONResponseReader;
import divisioner.VariationPointDivisionConfiguration;


/**
 * Manages declaration of variable extensions
 * 
 * @author Jakub Perdek
 *
 */
public class ParameterExtensionMarker {
	
	/**
	 * Index of declared positive variation points to avoid conflicts/ to preserve unique parameter names
	 */
	private static int usedIndexInner = 1;
	
	
	/**
	 * Generates marker from parameter for functions/constructors in AST
	 * -the name starts with VariationPointDivisionConfiguration.PARAM_VP
	 * 
	 * @return AST of generated marker for parameter
	 */
	public static JSONObject generateMarkerFromParameterInAST() {
		String parameterName = VariationPointDivisionConfiguration.PARAM_VP + ParameterExtensionMarker.usedIndexInner;
		ParameterExtensionMarker.usedIndexInner = ParameterExtensionMarker.usedIndexInner + 1;
		String newParameter = "{\"pos\":41,\"end\":47,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":166,\"name\":{\"pos\":41,\"end\":47,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":79,\"escapedText\":\"" + parameterName + "\"}}";
		return JSONResponseReader.loadJSONConfig(newParameter);
	}
	
	/**
	 * Generates marker from parameter for functions/constructors in AST
	 * 
	 * @param parameterName - name of parameter for substitution purposes
	 * @return AST of generated marker for parameter
	 */
	public static JSONObject generateMarkerFromParameterInAST(String parameterName) {
		String newParameter = "{\"pos\":41,\"end\":47,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":166,\"name\":{\"pos\":41,\"end\":47,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":79,\"escapedText\":\"" + parameterName + "\"}}";
		return JSONResponseReader.loadJSONConfig(newParameter);
	}
}
