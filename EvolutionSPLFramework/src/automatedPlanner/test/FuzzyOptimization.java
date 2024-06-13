package automatedPlanner.test;

import java.security.Policy.Parameters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import evolutionSimulation.productAssetsInitialization.SharedConfiguration;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.optimization.OptimizationDeltaJump;
import net.sourceforge.jFuzzyLogic.optimization.Parameter;
import net.sourceforge.jFuzzyLogic.rule.Rule;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;


/**
 * Optimization according to https://github.com/pcingola/jFuzzyLogic/blob/master/fcl/qualify.fcl
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class FuzzyOptimization {

	public static void main(String[] args) {
		//String fuzzyFileName = SharedConfiguration.PROJECT_PATH + "/fuzzyPlannerConfiguration/test/qualify.fcl";
		String fuzzyFileName = SharedConfiguration.PROJECT_PATH + "/fuzzyPlannerConfiguration/test/fuzzyTest.fcl";
		String functionBlockName = "fuzzyTestMain"; //"qualify";
		String ruleBlockName = "No1";
				
		FIS fis = FIS.load(fuzzyFileName);
		Map<String, RuleBlock> ruleBlockMap = fis.getFunctionBlock(functionBlockName).getRuleBlocks();

		
		fis.setVariable("service", 3);
        fis.setVariable("food", 7);
        // Evaluate
        //fis.evaluate();
        System.out.println(fis.getVariable("service"));
        System.out.println(fis.getVariable("service").getDefaultValue()); //NaN
        
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		// Add variables. 
		// Note: Fuzzy sets' parameters for these 
		// variables will be optimized
		//List<Parameter> parameters1 = Parameter.parametersMembershipFunction(fis.getVariable("scoring"));
		//List<Parameter> parameters2 = Parameter.parametersMembershipFunction(fis.getVariable("credLimMul"));
		
		List<Parameter> parameters1 = Parameter.parametersMembershipFunction(fis.getVariable("service"));
		List<Parameter> parameters2 = Parameter.parametersMembershipFunction(fis.getVariable("food"));
		parameters.addAll(parameters1);
		parameters.addAll(parameters2);
		
		RuleBlock ruleBlock = ruleBlockMap.get(ruleBlockName);
		// Add every rule's weight
		for( Rule rule: ruleBlock ) {
			parameters.addAll(Parameter.parametersRuleWeight(rule));
		}
		//---
		// Create an error function to be 
		// optimzed (i.e. minimized)
		//---
		ErrorFunctionQualify errorFunction = new ErrorFunctionQualify();

		//---
		// Optimize (using 'Delta jump optimization')
		//---
		OptimizationDeltaJump optimizationDeltaJump = 
				new OptimizationDeltaJump(ruleBlock, errorFunction, parameters);

		// Number optimization of iterations
		optimizationDeltaJump.setMaxIterations(20);
		optimizationDeltaJump.finishCondition(19);
		optimizationDeltaJump.optimize();
		System.out.println(optimizationDeltaJump.getShowEvery());
		System.out.println(optimizationDeltaJump.stats(5));
		System.out.println(optimizationDeltaJump.stats(10));
		System.out.println(fis.getVariable("tip").getValue());
	}
}
