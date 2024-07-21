package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution;

import static org.junit.Assert.assertNotNull;

import java.util.Map;
import codeContext.processors.export.ExportedContext;


/**
 * The counter for the occurrences of particular parameter types (of callable code templates)
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class ParsedTypeOfVariableData {
	
	/**
	 * The counter of the parameters of the same type for particular template (of callable construct) or code construct
	 */
	protected int numberParametersOfSameType = 1;
	
	
	/**
	 * Instantiates and initializes the entity to manage parsed type of variable data with the mapping 
	 * of each variable/parameter name to exported context (import/export/dependency)
	 * 
	 * @param nameToContextMapping - the mapping of each variable/parameter name to exported context (import/export/dependency)
	 */
	public ParsedTypeOfVariableData() {
	}
	
	/**
	 * Increases the counter representing the number of parameter (each such instance only for one parameter type) with the same type
	 */
	public void increaseOccurence() { this.numberParametersOfSameType = this.numberParametersOfSameType + 1; }
	
	/**
	 * Returns the number of occurrences of parameter (each such instance only for one parameter type) with the same type
	 * 
	 * @return the number of occurrences of parameter (each such instance only for one parameter type) with the same type
	 */
	public int getNumberOccurences() { return this.numberParametersOfSameType; }
	
	/**
	 * Returns null or the mapping carrying exported objects or currently available variables in extensions of this simplyfied version of this class
	 * 
	 * @return null or the mapping carrying exported objects or currently available variables in extensions of this simplyfied version of this class
	 */
	public Map<String, ? extends ExportedObjectOrAvailableVariable> getNameToContextMapping() { return null;}
}
