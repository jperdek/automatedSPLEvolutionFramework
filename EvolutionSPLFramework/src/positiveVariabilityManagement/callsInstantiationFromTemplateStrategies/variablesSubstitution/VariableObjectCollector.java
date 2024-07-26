package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import java.util.ArrayList;
import java.util.List;

import codeContext.objects.VariableObject;


/**
 * Collects and organizes the variable objects such as parameters and variables that are extracted from hierarchies
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class VariableObjectCollector {

	/**
	 * Collected previously declared parameters (according to configuration to suit particular programming language)
	 */
	private List<VariableObject> collectedParameters;
	
	/**
	 * Collected previously declared local variables (according to configuration to suit particular programming language)
	 */
	private List<VariableObject> collectedLocalVariables;
	
	/**
	 * Collected previously declared global parameters (according to configuration to suit particular programming language)
	 */
	private List<VariableObject> collectedGlobalVariables;
	
	/**
	 * Initializes the entities to collect and organize variable objects from hierarchies
	 */
	public VariableObjectCollector() {
		this.collectedParameters = new ArrayList<VariableObject>();
		this.collectedLocalVariables = new ArrayList<VariableObject>();
		this.collectedGlobalVariables = new ArrayList<VariableObject>();
	}
	
	/**
	 * Inserts parameters to all collected parameters (for their possible substitution)
	 * 
	 * @param parameters - parameters that will be inserted to all collected parameters (for their possible substitution) 
	 */
	public void addParameters(List<VariableObject> parameters) {
		this.collectedParameters.addAll(parameters);
	}
	
	/**
	 * Inserts local variables to all collected local variables (for their possible substitution)
	 * 
	 * @param localVariables - local variables that will be inserted to all collected local variables
	 */
	public void addLocalVariables(List<VariableObject> localVariables) {
		this.collectedLocalVariables.addAll(localVariables);
	}
	
	/**
	 * Inserts global variables to all collected global variables (for their possible substitution)
	 * 
	 * @param globalVariables - global variables that will be inserted to all collected global variables
	 */
	public void addGlobalVariables(List<VariableObject> globalVariables) {
		this.collectedGlobalVariables.addAll(globalVariables);
	}
	
	/**
	 * Returns the list of collected parameters
	 * 
	 * @return the list of collected parameters
	 */
	public List<VariableObject> getCollectedParameters() { return this.collectedParameters; }
	
	/**
	 * Returns the list of collected globally declared variables
	 * 
	 * @return the list of collected globally declared variables
	 */
	public List<VariableObject> getCollectedGlobalVariables() { return this.collectedGlobalVariables; }
	
	/**
	 * Returns the list of collected locally declared variables
	 * 
	 * @return the list of collected locally declared variables
	 */
	public List<VariableObject> getCollectedLocalVariables() { return this.collectedLocalVariables; }
	
	/**
	 * Returns merged collected variables in one list
	 * 
	 * @return merged collected variables in one list
	 */
	public List<VariableObject> getMergedCollectedVariables() {
		List<VariableObject> collectedVariables = new ArrayList<VariableObject>();
		collectedVariables.addAll(this.collectedParameters);
		collectedVariables.addAll(this.collectedLocalVariables);
		collectedVariables.addAll(this.collectedGlobalVariables);
		return collectedVariables;
	}
}
