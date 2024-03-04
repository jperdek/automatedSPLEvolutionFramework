package splEvolutionCore.candidateSelector;

import splEvolutionCore.candidateSelector.valueAssignment.AssignedValue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dividedAstExport.ExpressionConverter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Representation of negative variation point candidate that allows to manage its quality 
 * according to associated information, especially the code fragment
 * 
 * @author Jakub Perdek
 *
 */
public class NegativeVariationPointCandidate extends VariationPointCandidate {

	/**
	 * The mapping of measured metric names to measured score/value/quality
	 * -the metric name is mapped to information about measurement score/value/quality
	 * MAP[unique variable name, assigned value]
	 */
	private Map<String, AssignedValue> assignedValues; // MAP[unique variable name, assigned value]
	
	
	/**
	 * Creates the negative variation point candidate and initializes the mapping to manage quality
	 * 
	 * @param variationPointData - the associated information with variation point including code, positions in AST, user annotations and other attributes
	 */
	public NegativeVariationPointCandidate(JSONObject variationPointData) {
		super(variationPointData);
		this.assignedValues = new HashMap<String, AssignedValue>();
	}
	
	/**
	 * Assigns measured value to this negative variability variation point
	 * 
	 * @param metricName - the unique name of measured metric
	 * @param assignedValue - the measured value with all associated/related information
	 */
	public void putAssignedValue(String metricName, AssignedValue assignedValue) {
		this.assignedValues.put(metricName, assignedValue);
	}
	
	/**
	 * Evaluates the overall value across all used metrics
	 * 
	 * @return the overall value across all used metrics
	 */
	public double evaluateCandidateValue() {
		double candidateValue = 0;
		for(AssignedValue processedAssignedValue: assignedValues.values()) {
			candidateValue = candidateValue + processedAssignedValue.calculateOverallValue();
		}
		return candidateValue;
	}
	
	/**
	 * Returns the first found configuration expression from variation point data that is
	 *  associated with this negative variability variation point
	 * 
	 * @return the first found configuration expression from variation point data that is 
	 * associated with this negative variability variation point
	 */
	private JSONObject getFirstConfigurationExpression() {
		JSONArray variabilitySelections = (JSONArray) this.variationPointData.get("variabilitySelections");
		JSONObject variabilitySelection;
		JSONObject configurationExpression;
		
		if (variabilitySelections == null) { return new JSONObject(); }
		for (Object variabilitySelectionObject: variabilitySelections) {
			variabilitySelection = (JSONObject) variabilitySelectionObject;
			configurationExpression = (JSONObject) variabilitySelection.get("configurationExpression");
			
			//expression is found and not empty
			if (configurationExpression != null && configurationExpression.entrySet().size() > 0) {
				return configurationExpression;
			}
		}
		return new JSONObject();
	}
	
	/**
	 * Injects configuration expression to the first argument of given annotation/decorator
	 * 
	 * @param negativeVariabilityAnnotation - representation of negative variability variation point annotation
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws AlreadyProvidedArgumentInConfigurationExpressionPlace
	 */
	public void injectConfigurationExpressionAsFirstArgument(JSONObject negativeVariabilityAnnotation) throws IOException, InterruptedException, AlreadyProvidedArgumentInConfigurationExpressionPlace {
		JSONObject firstConfigurationExpressionJSON = this.getFirstConfigurationExpression(); //loads configuration expression
		if (firstConfigurationExpressionJSON == null) { firstConfigurationExpressionJSON = new JSONObject(); }
		
		JSONArray annotationParameters = (JSONArray) negativeVariabilityAnnotation.get("parameters");
		if (annotationParameters == null) { 
			annotationParameters = new JSONArray(); 
			negativeVariabilityAnnotation.put("parameters", annotationParameters);
		} else if (annotationParameters.size() > 0) {
			throw new AlreadyProvidedArgumentInConfigurationExpressionPlace(
					"Multiple parameters exists. Annotation is probably incompatible!");
		}
		
		JSONObject configurationExpressionAsParameter = ExpressionConverter.getJSONObjectFromConfigurationExpressionAst(
				firstConfigurationExpressionJSON);
		annotationParameters.add(0, configurationExpressionAsParameter); // inserts/injects annotation as the first parameter
	}
	
	/**
	 * Returns set of configuration expression (only unique ones) from associated variation point data to this negative 
	 * variability variation point converted to AST
	 * [-allows to analyze and compare configuration expressions amongs each other]
	 * 
	 * @return the set of configuration expressions from associated variation point data - only unique ones 
	 */
	private Set<JSONObject> getConfigurationExpressionsInJSON() {
		Map<String, JSONObject> configurationExpressionsMap = new HashMap<String, JSONObject>(); 
		JSONArray variabilitySelections = (JSONArray) this.variationPointData.get("variabilitySelections");
		JSONObject variabilitySelection;
		JSONObject configurationExpression;
		
		if (variabilitySelections == null) { return new HashSet<JSONObject>(); }
		for (Object variabilitySelectionObject: variabilitySelections) {
			variabilitySelection = (JSONObject) variabilitySelectionObject;
			configurationExpression = (JSONObject) variabilitySelection.get("configurationExpression");
			
			//expression is found and not empty
			if (configurationExpression != null) {
				configurationExpressionsMap.put(configurationExpression.toString(), configurationExpression);
			}
		}
		return new HashSet<JSONObject>(configurationExpressionsMap.values());
	}
	
	/**
	 * Injects configuration expressions into negative variability variation point annotation parameters
	 *  
	 * @param negativeVariabilityAnnotation - information about negative variability variation point annotation
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws AlreadyProvidedArgumentInConfigurationExpressionPlace
	 */
	public void injectConfigurationExpressions(JSONObject negativeVariabilityAnnotation) throws IOException, InterruptedException, AlreadyProvidedArgumentInConfigurationExpressionPlace {
		JSONObject firstConfigurationExpressionJSON;
		Set<JSONObject> configurationExpressionJSONSet = this.getConfigurationExpressionsInJSON();
		if (configurationExpressionJSONSet.size() > 0) {
			firstConfigurationExpressionJSON = configurationExpressionJSONSet.iterator().next();
		} else {
			firstConfigurationExpressionJSON = new JSONObject();
		}
		
		JSONArray annotationParameters = (JSONArray) negativeVariabilityAnnotation.get("parameters");
		if (annotationParameters == null) { 
			annotationParameters = new JSONArray(); 
			negativeVariabilityAnnotation.put("parameters", annotationParameters);
		} else if (annotationParameters.size() > 0) {
			throw new AlreadyProvidedArgumentInConfigurationExpressionPlace(
					"Multiple parameters exists. Annotation is probably incompatible!");
		}
		
		JSONObject configurationExpressionAsParameter;
		configurationExpressionAsParameter = ExpressionConverter.getJSONObjectFromConfigurationExpressionAst(
				firstConfigurationExpressionJSON);
		annotationParameters.add(0, configurationExpressionAsParameter);
		
		JSONArray allConfigurationExpressionsInAst = new JSONArray();
		for (JSONObject processedConfigurationExpressionJSON: configurationExpressionJSONSet) {
			configurationExpressionAsParameter = ExpressionConverter.getJSONObjectFromConfigurationExpressionAst(
					processedConfigurationExpressionJSON);
			allConfigurationExpressionsInAst.add(configurationExpressionAsParameter);
		}
		annotationParameters.add(1, allConfigurationExpressionsInAst);
	}
	
	/**
	 * Loads JSON from string
	 * 
	 * @param astString - the string which contains coded JSON object
	 * @return JSON object loaded from string
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
	 * Loads JSON array from string
	 * 
	 * @param astString - the string which contains coded JSON array
	 * @return JSON array loaded from string
	 */
	private static JSONArray loadJSONArrayFromString(String astString) {
		try {
	        JSONParser parser = new JSONParser();
	        JSONArray configurationObject = (JSONArray) parser.parse(astString);
	        return configurationObject;
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	/**
	 * Injects configuration expressions into negative variability variation point annotation parameters from all annotation-decorators
	 * 
	 * @param negativeVariabilityAnnotation - information about negative variability variation point annotation 
	 * @param decoratorsList - the list of decorators with annotations with configuration expression
	 * @throws AlreadyProvidedArgumentInConfigurationExpressionPlace
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void injectAndInsertAllConfigurationExpressions(JSONObject negativeVariabilityAnnotation, 
			JSONArray decoratorsList) throws AlreadyProvidedArgumentInConfigurationExpressionPlace, IOException, InterruptedException {
		JSONObject firstConfigurationExpressionJSON, negativeVariabilityAnnotationDuplicate;
		Set<JSONObject> configurationExpressionJSONSet = this.getConfigurationExpressionsInJSON();
		if (configurationExpressionJSONSet.size() > 0) {
			firstConfigurationExpressionJSON = configurationExpressionJSONSet.iterator().next();
		} else {
			firstConfigurationExpressionJSON = new JSONObject();
		}
		
		JSONArray annotationParameters = (JSONArray) negativeVariabilityAnnotation.get("parameters");
		JSONArray annotationParametersDuplicate;
		if (annotationParameters == null) { 
			annotationParameters = new JSONArray(); 
			negativeVariabilityAnnotation.put("parameters", annotationParameters);
		} else if (annotationParameters.size() > 0) {
			throw new AlreadyProvidedArgumentInConfigurationExpressionPlace(
					"Multiple parameters exists. Annotation is probably incompatible!");
		}
		
		JSONObject configurationExpressionAsParameter;
		if (configurationExpressionJSONSet.size() == 0) {
			configurationExpressionAsParameter = ExpressionConverter.getJSONObjectFromConfigurationExpressionAst(
				firstConfigurationExpressionJSON);
			annotationParameters.add(0, configurationExpressionAsParameter);
			decoratorsList.add(negativeVariabilityAnnotation);
		} else {
			for (JSONObject processedConfigurationExpressionJSON: configurationExpressionJSONSet) {
				negativeVariabilityAnnotationDuplicate = this.loadASTFromString(negativeVariabilityAnnotation.toString());
				annotationParametersDuplicate = this.loadJSONArrayFromString(annotationParameters.toString());
				configurationExpressionAsParameter = ExpressionConverter.getJSONObjectFromConfigurationExpressionAst(
						processedConfigurationExpressionJSON);
				annotationParametersDuplicate.add(0, configurationExpressionAsParameter);
				decoratorsList.add(negativeVariabilityAnnotationDuplicate);
			}
		}
	}
}
