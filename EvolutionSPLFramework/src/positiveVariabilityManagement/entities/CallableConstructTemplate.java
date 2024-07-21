package positiveVariabilityManagement.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import codeContext.UsedVariables;
import codeContext.objects.VariableObject;
import divisioner.VariationPointDivisionConfiguration;


/**
 * Representation of the callable construct template
 * 
 * @author Jakub Perdek
 *
 */
public class CallableConstructTemplate {
	
	/**
	 * Associated callable template form
	 */
	private String callableTemplateForm = null;
	
	/**
	 * The mapping of exported name to the type of associated/processed exported object from UsedVariables object
	 */
	private Map<String, String> parameterNamesToTypeMapping;
	
	/**
	 * Creates and stores callable code construct template along with extraction of inner arguments/parameters
	 * 
	 * @param callableTemplateForm - the callable parameterized code construct template
	 */
	public CallableConstructTemplate(String callableTemplateForm) {
		this();
		this.parseVariableNames(callableTemplateForm);
		this.callableTemplateForm = callableTemplateForm;
	}
	
	/**
	 * Creates and stores callable code construct template without the name along with extraction of inner arguments/parameters
	 * 
	 * @param usedParameters - the function/constructor parameters used in template
	 */
	public CallableConstructTemplate(UsedVariables usedParameters) {
		this();
		this.parseVariableNames(usedParameters);
	}
	
	/**
	 * Creates empty callable code construct template
	 */
	public CallableConstructTemplate() {
		this.parameterNamesToTypeMapping = new HashMap<String, String>();
	}

	/**
	 * Returns the callable template/parameterized form
	 * 
	 * @return the callable template/parameterized form
	 */
	public String getCallableTemplateForm() { return this.callableTemplateForm; }
	
	/**
	 * Returns the mapping of template (previously of function/constructor) parameter names to their type
	 * 
	 * @return the mapping of template (previously of function/constructor) parameter names to their type
	 */
	public Set<Entry<String, String>> getParameterNamesToTypeMappingEntries() { return this.parameterNamesToTypeMapping.entrySet(); }
	
	/**
	 * Returns the set of entries from the mapping of template (previously of function/constructor) parameter names to their type
	 * 
	 * @return the set of entries from mapping of template (previously of function/constructor) parameter names to their type
	 */
	public Map<String, String> getParameterNamesToTypeMapping() { return this.parameterNamesToTypeMapping; }
	
	/**
	 * Parses the variable names with their type and stores them into the mapping of parameter names to their type
	 * 
	 * @param callableTemplateForm - the callable parameterized code construct template
	 */
	private void parseVariableNames(String callableTemplateForm) {
		String[] callableFormParameters = callableTemplateForm.split(
				VariationPointDivisionConfiguration.PARAMETERIZED_FORM_START.replace("[", "\\["));
		String callableFormParameter, parameterName, parameterType;
		String nameTypeMappings[];
		String callableFormParameterRaw;
		for (int index = 1; index < callableFormParameters.length; index++) {
			callableFormParameterRaw = callableFormParameters[index].strip();
			callableFormParameter = callableFormParameterRaw.split(VariationPointDivisionConfiguration.PARAMETERIZED_FORM_END)[0];
			nameTypeMappings = callableFormParameter.split(":");
			parameterName = nameTypeMappings[0];
			parameterType = nameTypeMappings[1];
			this.parameterNamesToTypeMapping.put(parameterName, parameterType);
		}
	}
	
	/**
	 * Creates mapping of exported name to the type of associated/processed exported object from UsedVariables object
	 * -the mapping is stored in this.parameterNamesToTypeMapping
	 * 
	 * @param usedParameters - object from which the mapping of exported name to the type of associated/processed exported object is updated
	 */
	private void parseVariableNames(UsedVariables usedParameters) {
		String parameterName, parameterType;
		for (VariableObject variableObject: usedParameters.getUsedVariableObjects()) {
			parameterName = variableObject.getExportName();
			parameterType = variableObject.getExportType();
			this.parameterNamesToTypeMapping.put(parameterName, parameterType);
		}
	}
}
