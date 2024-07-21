package positiveVariabilityManagement.callsInstantiationFromTemplateStrategies;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import codeContext.processors.export.ExportedContext;
import divisioner.VariationPointDivisionConfiguration;

import java.util.Map.Entry;
import java.util.Queue;

import positiveVariabilityManagement.UnmappedContextException;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.ActualScriptVariablesToSubstituteConfiguration;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.AllVariablesMapper;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.ExportedObjectOrAvailableVariable;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.ParsedTypeOfVariableData;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.variablesSubstitution.ParsedTypeOfVariableImportedData;
import positiveVariabilityManagement.entities.CallableConstructTemplate;
import splEvolutionCore.DebugInformation;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Performs all instantiations from selected callable constructs which parameters are substituted with given imported or available variables
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class AllInstantiationsFromTemplate implements CallsInstantiationFromTemplate {

	/**
	 * Set of messages propagated to the output to prevent duplicates
	 */
	private static HashSet<String> messageOnOutput = new HashSet<String>();
	
	
	/**
	 * Initialization of AllInstantiationsFromTemplate
	 */
	public AllInstantiationsFromTemplate() {
	}
	
	@Override
	/**
	 * Prescribe the function to perform all instantiations from selected callable constructs which parameters are substituted with given imported or available variables
	 * 
	 * @param variationPointCandidateTemplate - the template with information about particular/processed positive variability variation point (where functionality can be injected)
	 * @param allVariablesMapper - the mapping of variable/parameter types to related information 
	 * @return the queue with instantiated callable constructs
	 * 
	 * @throws UnmappedContextException
	 */
	public Queue<CallableConstruct> instantiateCallsFromTemplate(
			PositiveVariationPointCandidateTemplates variationPointCandidateTemplate,
			AllVariablesMapper allVariablesMapper) throws UnmappedContextException, AlreadyChosenVariationPointForInjectionException {
		ActualScriptVariablesToSubstituteConfiguration actualScriptVariablesToSubstituteConfiguration = 
				allVariablesMapper.getActualScriptVariablesToSubstituteConfiguration();
		String callableConstructName, callableConstructNameWhole;
		Queue<CallableConstruct> callableConstructs = null;
		Queue<CallableConstruct> allCallableConstructs = new LinkedList<CallableConstruct>();
		String parameterType;
		Set<Entry<String, String>> parameterNamesToTypeMapping;
		ParsedTypeOfVariableData parsedTypeOfVariableData;

		Map<String, ? extends ExportedObjectOrAvailableVariable> variableNameToContextMapping;
		Map<String, ParsedTypeOfVariableData> parameterInformation; //type, associatedData
		for (CallableConstructTemplate callableConstructTemplate: variationPointCandidateTemplate.getCallableConstructTemplates()) {
			callableConstructNameWhole = callableConstructTemplate.getCallableTemplateForm();
			
			callableConstructName = callableConstructNameWhole.substring(0, callableConstructNameWhole.indexOf('('));
			parameterInformation = allVariablesMapper.findExternalScriptsParameterInformation(callableConstructTemplate);
			parameterNamesToTypeMapping = callableConstructTemplate.getParameterNamesToTypeMappingEntries();
			callableConstructs = null;

			for (Entry<String, String> parameterEntry: parameterNamesToTypeMapping) { //for each [variable name, variable type]
				parameterType = parameterEntry.getValue();
				parsedTypeOfVariableData = parameterInformation.get(parameterType);
				variableNameToContextMapping = parsedTypeOfVariableData.getNameToContextMapping();
				callableConstructs = this.assignParametersForNewVariable(callableConstructName,
						callableConstructNameWhole, variableNameToContextMapping);
			}
			if (!callableConstructs.isEmpty()) {
				allCallableConstructs.addAll(callableConstructs); 
			}
		}
		if (variationPointCandidateTemplate.getCallableConstructTemplates().size() == 0) {
			if (true || DebugInformation.SHOW_POLLUTING_INFORMATION) { System.out.println("No callable constructa are selected!"); }
			return new LinkedList<CallableConstruct>(); 
		} else if (true || DebugInformation.SHOW_POLLUTING_INFORMATION) { 
			System.out.println("Callable constructa are selected: " + callableConstructs.size()); 
		}
		return callableConstructs;
	}

	/**
	 * Assigns the parameters for new variable  
	 * 
	 * @param callableConstructName - the callable functionality name without parameters or arguments
	 * @param callableConstructNameWhole - the template of callable functionality with parameters or arguments (the call with unsubstituted parameters)
	 * @param variableNameToExportedContextMapping - the mapping of variable names to exported contexts
	 * @return the queue with instantiated callable constructs
	 * @throws AlreadyChosenVariationPointForInjectionException 
	 */
	private Queue<CallableConstruct> assignParametersForNewVariable(String callableConstructName, String callableConstructNameWhole, 
			Map<String, ? extends ExportedObjectOrAvailableVariable> variableNameToExportedContextMapping) throws AlreadyChosenVariationPointForInjectionException {
		Queue<CallableConstruct> callableConstructsOld = new LinkedList<CallableConstruct>();
		callableConstructsOld.add(new CallableConstruct(callableConstructName));

		Queue<CallableConstruct> preCallableConstructsNew = new LinkedList<CallableConstruct>();
		CallableConstruct callableConstructNew;
		String matchedVariableName;
		ExportedObjectOrAvailableVariable matchedExportedContextOrVariable;
		
		String variablePart;
		String originalParameters[] = callableConstructNameWhole.split(
				VariationPointDivisionConfiguration.PARAMETERIZED_FORM_START.replace("[", "\\["));
		for (int index = 1; index < originalParameters.length; index++) {
			variablePart = originalParameters[index];
			variablePart = variablePart.split(VariationPointDivisionConfiguration.PARAMETERIZED_FORM_END)[0].split(":")[0];	
		
			// SIMILARITY MATCHING
			preCallableConstructsNew = new LinkedList<CallableConstruct>();
			for (Entry<String, ? extends ExportedObjectOrAvailableVariable> variableNameToExportedContext: variableNameToExportedContextMapping.entrySet()) {
				matchedVariableName = variableNameToExportedContext.getKey();
				matchedExportedContextOrVariable = variableNameToExportedContext.getValue();

				for (CallableConstruct callableConstructOld: callableConstructsOld) {
					callableConstructNew = new CallableConstruct(callableConstructOld);
					if (callableConstructNew.addParameterWithChecking(variablePart, matchedExportedContextOrVariable)) {
						preCallableConstructsNew.add(callableConstructNew);
					}
				}
			}
			callableConstructsOld.clear();
			if (!preCallableConstructsNew.isEmpty()) {
				callableConstructsOld.addAll(preCallableConstructsNew);
			}
		}
		if (callableConstructsOld.isEmpty()) {
			if (DebugInformation.SHOW_MISSING_EVOLUTION_ENHANCEMENTS && 
					!AllInstantiationsFromTemplate.messageOnOutput.contains(callableConstructNameWhole)) {
				AllInstantiationsFromTemplate.messageOnOutput.add(callableConstructNameWhole);
				System.out.println("Cannot find parameters for construct: " + callableConstructNameWhole + " Add them if possible...");
			}
		}
		return callableConstructsOld;
	}
}
