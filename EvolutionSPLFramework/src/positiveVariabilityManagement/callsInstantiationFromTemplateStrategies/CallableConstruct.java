package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies;

import java.util.ArrayList;
import java.util.List;
import codeContext.processors.export.ExportLocationAggregation;
import codeContext.processors.export.ExportedContext;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.ExportedObjectOrAvailableVariable;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.InjectionCandidateVariationPoint;
import splEvolutionCore.SPLEvolutionCore;


/**
 * Representation of callable construct
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class CallableConstruct {

	/**
	 * The base part of the call such as function or class name without parameters
	 */
	private String base;
	
	/**
	 * The list of substituted parameters of callable functionality (to make functionality callable)
	 */
	private List<String> substitutedParameters;
	
	/**
	 * The list of exported contexts where each is used to extract imports/dependencies
	 * -null if inner structure is used - to generate imports
	 */
	private List<ExportedContext> exportsMapping;
	
	/**
	 * information if callable constructs must be necessarily injected into specific variation point if true otherwise false
	 */
	private boolean belongToParticularVariationPoint = false;
	
	/**
	 * Identifier of variation point that should be used to inject this callable construct
	 */
	private String variationPointToInjectCondIdentifier = null;
	
	/**
	 * Instantiates the callable construct representation with related information
	 * -the base part of the call such as function or class name without parameters
	 * -substituted parameters (construct is already callable)
	 * -associated exports/dependencies
	 * 
	 * @param base - the base part of the call such as function or class name without parameters
	 */
	public CallableConstruct(String base) {
		this.base = base;
		this.substitutedParameters = new ArrayList<String>();
		this.exportsMapping = new ArrayList<ExportedContext>();
	}
	
	/**
	 * Instantiates the callable construct representation that is initialized with related information extracted from another callable construct
	 * -the base part of the call such as function or class name without parameters
	 * -substituted parameters (construct is already callable)
	 * -associated exports/dependencies
	 * 
	 * @param callableConstruct - another callable construct which is going to initialize this instance
	 */
	public CallableConstruct(CallableConstruct callableConstruct) {
		this.base = callableConstruct.getBase();
		this.substitutedParameters = new ArrayList<String>(callableConstruct.getSubstitutedParameters());
		this.exportsMapping = new ArrayList<ExportedContext>(callableConstruct.getExportsMapping());
	}
	
	
	/**
	 * Checks if this callable construct belongs to particular variation point
	 * 
	 * @return true if this callable construct belongs to particular variation point
	 */
	public boolean belongsToParticularVariationPoint() { return this.belongToParticularVariationPoint; }
	
	/**
	 * Returns the variation point identifier if the callable construct has prescribed it as dependency otherwise null
	 * - can be checked with belongsToParticularVariationPoint()
	 * 
	 * @return the variation point identifier if the callable construct has prescribed it as dependency otherwise null
	 */
	public String getVariationPointToInjectIdentifier() { return this.variationPointToInjectCondIdentifier; }
	
	/**
	 * Adds parameter into the list of callable construct parameters and associated exported context related to the parameter type to the mapping of exports
	 * -function is repeatedly called in the order of parameter chain
	 * 
	 * @param parameterToSubstitute - parameter name
	 * @param parameterExportedContext - associated exported context/dependency related to the parameter type
	 */
	public void addParameter(String parameterToSubstitute, ExportedContext parameterExportedContext) {
		if (SPLEvolutionCore.DISABLE_EXTERNAL_VARIABLE_INJECTIONS) { return; }
		this.substitutedParameters.add(parameterToSubstitute);
		this.exportsMapping.add(parameterExportedContext);
	}

	/**
	 * Adds parameter into the list of callable construct parameters and checks with existing injection dependencies
	 * -function is repeatedly called in the order of parameter chain
	 * 
	 * @param parameterToSubstitute - parameter name
	 * @param parameterExportedContext - associated exported context/dependency related to the parameter type
	 * @throws AlreadyChosenVariationPointForInjectionException - exception informing about violating dependency of previously chosen another variation point where content must be injected
	 */
	public void addParameter(String parameterToSubstitute, InjectionCandidateVariationPoint parameterExportedContext) throws AlreadyChosenVariationPointForInjectionException {
		if (SPLEvolutionCore.DISABLE_INTERNAL_VARIABLE_INJECTIONS) { throw new AlreadyChosenVariationPointForInjectionException("Injection of inner variables is disabled!"); }
		this.substitutedParameters.add(parameterToSubstitute);
		if (this.belongToParticularVariationPoint) {
			if (!this.variationPointToInjectCondIdentifier.equals(parameterExportedContext.getVariationPointIdentifier())) {
				throw new AlreadyChosenVariationPointForInjectionException("Another variation point: " + 
			this.variationPointToInjectCondIdentifier + " has already been chosen for injection");
			}
		} else {
			this.belongToParticularVariationPoint = true;
			this.variationPointToInjectCondIdentifier = parameterExportedContext.getVariationPointIdentifier();
		}
		this.exportsMapping.add(null);
	}
	
	/**
	 * Adds parameter into the list of callable construct parameters and associated exported context related to the parameter type to the mapping of exports
	 * -function is repeatedly called in the order of parameter chain
	 * 
	 * @param parameterToSubstitute - parameter name
	 * @param parameterExportedContext - associated exported context/dependency related to the parameter type
	 * @return true if parameter is successfully applied/inserted otherwise false
	 * @throws AlreadyChosenVariationPointForInjectionException  - exception informing about violating dependency of previously chosen another variation point where content must be injected
	 */
	public boolean addParameterWithChecking(String parameterToSubstitute, ExportedObjectOrAvailableVariable parameterExportedContext) throws AlreadyChosenVariationPointForInjectionException {
		if (parameterExportedContext instanceof  InjectionCandidateVariationPoint) {
			if (SPLEvolutionCore.DISABLE_INTERNAL_VARIABLE_INJECTIONS) { return false; }
			if (!this.variationPointToInjectCondIdentifier.equals(
					((InjectionCandidateVariationPoint) parameterExportedContext).getVariationPointIdentifier())) {
				return false;
			}
			this.addParameter(parameterToSubstitute, (InjectionCandidateVariationPoint) parameterExportedContext);
		} else {
			if (SPLEvolutionCore.DISABLE_EXTERNAL_VARIABLE_INJECTIONS) { return false; }
			this.addParameter(parameterToSubstitute, (ExportedContext) parameterExportedContext);
		}
		return true;
	}
	
	/**
	 * Returns the base part of the call such as function or class name without parameters
	 * 
	 * @return the base part of the call such as function or class name without parameters
	 */
	public String getBase() { return this.base; }
	
	/**
	 * Returns the list of substituted parameters of callable functionality (to make functionality callable)
	 * 
	 * @return The list of substituted parameters of callable functionality (to make functionality callable)
	 */
	public List<String> getSubstitutedParameters() { return this.substitutedParameters; }
	
	/**
	 * Prepares/instantiates the callable construct from the available/associated resources (in JavaScript/TypeScript)
	 * 
	 * @return the instantiated callable construct from the available/associated resources (in JavaScript/TypeScript)
	 */
	public String instantiateCallableConstruct() { 
		String callableConstruct = null;
		for (String parameterName: this.substitutedParameters) {
			if (callableConstruct == null) {
				callableConstruct = this.base + "(" + parameterName;
			} else {
				callableConstruct = callableConstruct + ", " + parameterName;
			}
		}
		if (callableConstruct == null) { return this.base + "()"; }
		return callableConstruct + ")";
	}
	
	/**
	 * Returns the list of exported contexts where each is used to extract imports/dependencies
	 * 
	 * @return the list of exported contexts where each is used to extract imports/dependencies
	 */
	public List<ExportedContext> getExportsMapping() { return this.exportsMapping; }
	
	/**
	 * Aggregates the imports/exports from exported contexts/dependencies
	 * 
	 * @return the aggregation of dependencies from exported contexts/dependencies
	 */
	public ExportLocationAggregation getExportedDependenciesOfFutureImports() {
		ExportLocationAggregation pathsOfExportDependencies = new ExportLocationAggregation(this.variationPointToInjectCondIdentifier);
		for (ExportedContext exportedContext: this.exportsMapping) {
			if (exportedContext != null) {
				pathsOfExportDependencies.aggregateLocation(exportedContext.getExportedLocation());
			}
		}
		return pathsOfExportDependencies;
	}
}
