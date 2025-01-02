package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import splEvolutionCore.DebugInformation;


/**
 * Dependency introduced using particular callable construct
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class CallableConstructDependency {
	
	/**
	 * Logger to track dependencies bound to callable constructs
	 */
	private static final Logger logger = LoggerFactory.getLogger(CallableConstructDependency.class);

	/**
	 * The set of identifier of variation point where validity of extracted parameters/local variables holds
	 */
	private Set<String> variationPointIdentifierNames;
	
	/**
	 * Instantiates dependency for specific group of parameters/local variables which validity holds for specific variation point
	 * 
	 * @param variationPointIdentifierName
	 */
	public CallableConstructDependency(String variationPointIdentifierName) {
		this.variationPointIdentifierNames = new HashSet<String>(); 
		this.variationPointIdentifierNames.add(variationPointIdentifierName);
	}
	
	/**
	 * Instantiates dependency for specific group of parameters/local variables which validity holds for specific variation point
	 * 
	 * @param variationPointIdentifierNames - the collection of identifier associated with variation point where validity of extracted parameters/local variables holds
	 */
	public CallableConstructDependency(Collection<String> variationPointIdentifierNames) {
		this.variationPointIdentifierNames = new HashSet<String>(variationPointIdentifierNames); 
	}
	
	/**
	 * Checks if all dependencies fits requested requirements
	 * 
	 * @param requestedVariationPointName - the name of variation point that is evaluated if validity of used parameters holds
	 * @return true if all dependencies fits requested requirements otherwise false
	 */
	public boolean fitsAllDependencies(String requestedVariationPointName) {
		if (DebugInformation.SHOW_POLLUTING_INFORMATION) {
			logger.debug("MATCHING >" + requestedVariationPointName + "< IN: ");
			for (String param: this.variationPointIdentifierNames) {
				logger.debug(param);
			}
		}
		return this.variationPointIdentifierNames.contains(requestedVariationPointName);
	}
}
