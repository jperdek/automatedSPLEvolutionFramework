package positiveVariabilityManagement.fragmentManagement;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import codeContext.processors.export.ExportLocationAggregation;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.InjectionIntoVariationPointValidator;
import positiveVariabilityManagement.fragmentManagement.model.AggregatedCodeFragment;
import positiveVariabilityManagement.fragmentManagement.model.CodeFragment;
import positiveVariabilityManagement.fragmentManagement.model.LineOfCode;
import positiveVariabilityManagement.fragmentManagement.model.annotable.MethodContextCodeFragment;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidate;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;
import splEvolutionCore.candidateSelector.valueAssignment.AssignedValue;


/**
 * Management of quantity-based code aggregations that belongs to positive variability
 * 
 * @author Jakub Perdek
 *
 */
public class QuantityBasedGranularityManagementStrategy implements CodeIncrementGranularityManagementStrategy {

	/**
	 * Identifier to generat new method names
	 */
	private static long methodId = 1;
	
	/**
	 * Threshold to introduce method
	 */
	private int methodThreshold = 5; //class method or outside
	
	/**
	 * Threshold for variable declarations to introduce class
	 */
	private int variableDeclarationForClassThreshold = 3; //create class as create/carrier
	

	/**
	 * Creates the quantity based granularity management strategy
	 * 
	 * @param methodThreshold - the minimal number of methods to introduce method
	 * @param variableDeclarationForClassThreshold - the minimal number of variables to introduce class
	 */
	public QuantityBasedGranularityManagementStrategy(
			int methodThreshold, int variableDeclarationForClassThreshold) {
		this.methodThreshold = methodThreshold;
		this.variableDeclarationForClassThreshold = variableDeclarationForClassThreshold;
	}
	
	@Override
	/**
	 * Associates/synthesizes the code constructs together under given code structures and according to granularity of aggregated constructs
	 * 
	 * @param variationPointConstructs - the list of associated code constructs with selected variation point that can be aggregated under new code construct 
	 * @param selectedTemplate - to find used positive variability variation point where code should be aggregated and
	 *  get relevant export locations of associated code constructs
	 * @param selectedVariationPointToInjectContent - the selected variation point where content is intended to be injected (for injection dependency checks)
	 * @return the code fragment that is created from code constructs in place of selected positive variability variation point
	 */
	public CodeFragment associateConstructsTogether(
			List<Entry<String, Map<String, AssignedValue>>> variationPointConstructs,
			PositiveVariationPointCandidateTemplates selectedTemplate, String selectedVariationPointToInjectContent) {
		LineOfCode variationPointConstruct;
		AggregatedCodeFragment variationPointConstructAggregation;
		MethodContextCodeFragment methodContextCodeFragment;
		ExportLocationAggregation associatedAggregatedLocationExports;
		PositiveVariationPointCandidate positiveVariationPointCandidate = selectedTemplate.getAssociatedPositiveVariabilityConstructsCandidate();
		String methodName, instantiatedCodeForm;

		if (variationPointConstructs.size() == 1) {
			Entry<String, Map<String, AssignedValue>> featureConstruct = variationPointConstructs.get(0);
			instantiatedCodeForm = featureConstruct.getKey();

			associatedAggregatedLocationExports = positiveVariationPointCandidate.
					getExportLocationAggregationsAccordingToInstantiatedCallableForm(instantiatedCodeForm);
			if (!InjectionIntoVariationPointValidator.canBeInjectedIntoVariationPoint(selectedVariationPointToInjectContent, associatedAggregatedLocationExports)) {
				System.out.println("Code fragment: " + instantiatedCodeForm + " is violating dependency injection issue! Returning null!");
				return null;
			}
			variationPointConstruct = new LineOfCode(featureConstruct.getKey(), featureConstruct.getValue(), associatedAggregatedLocationExports);
			return variationPointConstruct;
		} else if (variationPointConstructs.size() < methodThreshold || true) { //CREATE METHOD IN ALL CASES - THIS HAS TO BE UPDATED
			// TO DO - observe number of variables and propose class as carier
			variationPointConstructAggregation = new AggregatedCodeFragment(); 
			int insertedLinesOfCode = 0;
			for (Entry<String, Map<String, AssignedValue>> featureConstruct: variationPointConstructs) {
				instantiatedCodeForm = featureConstruct.getKey();
				associatedAggregatedLocationExports = positiveVariationPointCandidate.
						getExportLocationAggregationsAccordingToInstantiatedCallableForm(instantiatedCodeForm);
				if (!InjectionIntoVariationPointValidator.canBeInjectedIntoVariationPoint(selectedVariationPointToInjectContent, associatedAggregatedLocationExports)) {
					System.out.println("Omitting code fragment: " + instantiatedCodeForm + " from aggregated code fragment due to dependency injection issue!");
					continue;
				}
				insertedLinesOfCode = insertedLinesOfCode + 1;
				variationPointConstruct = new LineOfCode(instantiatedCodeForm, featureConstruct.getValue(), 
						associatedAggregatedLocationExports);
				variationPointConstructAggregation.addCodeFragment(variationPointConstruct);
			}
			if (insertedLinesOfCode == 0) { return null; }
			return variationPointConstructAggregation;
		} //else if (variationPointConstructs.size() > methodTreshold) {
		
		//TO BE UPDATED LATER
		methodName = "introducedMethod" + QuantityBasedGranularityManagementStrategy.methodId;//generate method name from context
		QuantityBasedGranularityManagementStrategy.methodId += 1;
		methodContextCodeFragment = new MethodContextCodeFragment(methodName, false, selectedTemplate);
	
		//many methods can be created here and aggregated inside class - their order can be relevant
		return methodContextCodeFragment;
	}

}
