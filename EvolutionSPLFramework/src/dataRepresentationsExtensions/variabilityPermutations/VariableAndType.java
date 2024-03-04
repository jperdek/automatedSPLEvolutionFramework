package dataRepresentationsExtensions.variabilityPermutations;


/**
 * Carrier for variable and its type
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class VariableAndType {
	
	/**
	 * The name of the variable (that is logged and possibly combined)
	 */
	private String variable;
	
	/**
	 * The type of used variable
	 */
	private String type;
	
	
	/**
	 * Instantiates the variable with associated type
	 * 
	 * @param variable - the name of the variable (that is logged and possibly combined)
	 * @param type - the type of used variable
	 */
	public VariableAndType(String variable, String type) {
		this.variable = variable;
		this.type = type;
	}
	
	/**
	 * Return the variable name
	 * 
	 * @return the variable name
	 */
	public String getVariable() {
		return this.variable;
	}
	
	/**
	 * Returns the variable type
	 * 
	 * @return the variable type
	 */
	public String getType() {
		return this.type;
	}
}
