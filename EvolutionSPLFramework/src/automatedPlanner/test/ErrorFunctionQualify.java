package automatedPlanner.test;

import net.sourceforge.jFuzzyLogic.optimization.ErrorFunction;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;

public class ErrorFunctionQualify extends ErrorFunction {

	public double evaluate(RuleBlock ruleBlock) {
        double error = 1;
        System.out.println("HERE");
        return error;
    }
}
