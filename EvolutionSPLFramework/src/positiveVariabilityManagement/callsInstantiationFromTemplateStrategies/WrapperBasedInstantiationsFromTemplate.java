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
import positiveVariabilityManagement.entities.CallableConstructTemplate;
import splEvolutionCore.DebugInformation;
import splEvolutionCore.candidateSelector.PositiveVariationPointCandidateTemplates;


/**
 * Performs instantiations where the name of one variable is as substring inside another variable (wrapper-based) 
 * from selected callable constructs which parameters are substituted with given imported or available variables
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class WrapperBasedInstantiationsFromTemplate implements CallsInstantiationFromTemplate {

	/**
	 * Set of messages propagated to the output to prevent duplicates
	 */
	private static HashSet<String> messageOnOutput = new HashSet<String>();
	
	@Override
	/**
	 * Prescribe the function to perform instantiations where the name of one variable is as substring inside another variable (wrapper-based) 
	 * from selected callable constructs which parameters are substituted with given imported or available variables
	 * 
	 * @param variationPointCandidateTemplate - the template with information about particular/processed positive variability variation point (where functionality can be injected)
	 * @param allVariablesMapper - the mapping of variable/parameter types to related information 
	 * @return the queue with instantiated callable constructs
	 * 
	 * @throws UnmappedContextException
	 */
	public Queue<CallableConstruct> instantiateCallsFromTemplate(
			PositiveVariationPointCandidateTemplates variationPointCandidateTemplate,
			AllVariablesMapper allVariablesMapper) throws UnmappedContextException {
		String callableConstructName, callableConstructNameWhole;
		Queue<CallableConstruct> callableConstructs = null;
		Queue<CallableConstruct> allCallableConstructs = new LinkedList<CallableConstruct>();

		if (DebugInformation.SHOW_POLLUTING_INFORMATION && variationPointCandidateTemplate.getCallableConstructTemplates().size() == 0) { 
			System.out.println("No callable construct templates are available!"); 
		}
		Map<String, ParsedTypeOfVariableData> parameterInformation; //type, associatedData
		for (CallableConstructTemplate callableConstructTemplate: variationPointCandidateTemplate.getCallableConstructTemplates()) {
			callableConstructNameWhole = callableConstructTemplate.getCallableTemplateForm();

			callableConstructName = callableConstructNameWhole.substring(0, callableConstructNameWhole.indexOf('('));
			parameterInformation = allVariablesMapper.findParameterInformation(callableConstructTemplate);
			callableConstructs = this.assignParametersForNewVariable(callableConstructName,
						callableConstructNameWhole, parameterInformation);
			if (!callableConstructs.isEmpty()) { allCallableConstructs.addAll(callableConstructs); }
		}
		if (variationPointCandidateTemplate.getCallableConstructTemplates().size() == 0) {
			if (DebugInformation.SHOW_POLLUTING_INFORMATION) { System.out.println("No callable constructa are instantiated!"); }
			return new LinkedList<CallableConstruct>(); 
		} 
		return allCallableConstructs;
	}

	/**
	 * Assigns the parameters for a new variable and restricts candidates according to wrapper-based condition 
	 * (the check if the name of one variable is as substring inside another variable)
	 * 
	 * @param callableConstructName - the callable functionality name without parameters or arguments
	 * @param callableConstructNameWhole - the template of callable functionality with parameters or arguments (the call with unsubstituted parameters)
	 * @param variableNameToExportedContextMapping - the mapping of variable names to exported contexts
	 * @return the queue with instantiated callable constructs
	 */
	private Queue<CallableConstruct> assignParametersForNewVariable(String callableConstructName, String callableConstructNameWhole, 
			Map<String, ParsedTypeOfVariableData> parameterInformation) {
		Queue<CallableConstruct> callableConstructsOld = new LinkedList<CallableConstruct>();
		callableConstructsOld.add(new CallableConstruct(callableConstructName));

		Queue<CallableConstruct> preCallableConstructsNew = new LinkedList<CallableConstruct>();
		CallableConstruct callableConstructNew;
		String matchedVariableName;
		ExportedContext matchedExportedContext;
		Set<Entry<String, String>> parameterNamesToTypeMapping;
		ParsedTypeOfVariableData parsedTypeOfVariableData;

		Map<String, ExportedContext> variableNameToExportedContextMapping;
		String variablePart, variableType, variableBase, parameterType;
		String originalParameters[] = callableConstructNameWhole.split(
				VariationPointDivisionConfiguration.PARAMETERIZED_FORM_START.replace("[", "\\["));
		for (int index = 1; index < originalParameters.length; index++) {
			variablePart = originalParameters[index];
			variableBase = variablePart.split(VariationPointDivisionConfiguration.PARAMETERIZED_FORM_END)[0];
			variablePart = variableBase.split(":")[0];	
			
			variableType =  variableBase.split(":")[1];
			parsedTypeOfVariableData = parameterInformation.get(variableType);
			variableNameToExportedContextMapping = parsedTypeOfVariableData.getNameToContextMapping();

			// SIMILARITY MATCHING
			preCallableConstructsNew = new LinkedList<CallableConstruct>();
			for (Entry<String, ExportedContext> variableNameToExportedContext: variableNameToExportedContextMapping.entrySet()) {
				matchedVariableName = variableNameToExportedContext.getKey();
				matchedExportedContext = variableNameToExportedContext.getValue();
				if (matchedVariableName.contains(variablePart) || variablePart.contains(matchedVariableName)) {
					for (CallableConstruct callableConstructOld: callableConstructsOld) {
						callableConstructNew = new CallableConstruct(callableConstructOld);
						callableConstructNew.addParameter(matchedVariableName, matchedExportedContext);
						preCallableConstructsNew.add(callableConstructNew);
					}
					
				} 
			}
			
			callableConstructsOld.clear();
			if (preCallableConstructsNew.isEmpty()) { //one of parameters is not matched, skipping template matches
				break;
			}
			if (!preCallableConstructsNew.isEmpty()) {
				callableConstructsOld.addAll(preCallableConstructsNew);
			}
		}
		if (callableConstructsOld.isEmpty()) {
			if (DebugInformation.SHOW_MISSING_EVOLUTION_ENHANCEMENTS && !WrapperBasedInstantiationsFromTemplate.messageOnOutput.contains(callableConstructNameWhole)) {
				WrapperBasedInstantiationsFromTemplate.messageOnOutput.add(callableConstructNameWhole);
				System.out.println("Wrapper based instantiation: Cannot find parameters for construct: " + callableConstructNameWhole + " Add them if possible...");
			}
		}

		return callableConstructsOld;
	}
}
