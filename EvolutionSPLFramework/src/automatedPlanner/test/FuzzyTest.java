package automatedPlanner.test;

import evolutionSimulation.productAssetsInitialization.SharedConfiguration;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;


public class FuzzyTest {

	
	public static void main(String[] args) throws Exception {
        // Load from 'FCL' file
        String fileName = SharedConfiguration.PROJECT_PATH + "/fuzzyPlannerConfiguration/test/fuzzyTest.fcl";
        FIS fis = FIS.load(fileName,true);
        // Error while loading?
        if( fis == null ) { 
            System.err.println("Can't load file: '" 
                                   + fileName + "'");
            return;
        }

        // Show 
        //fis.chart();
        JFuzzyChart.get().chart(fis);
        
        // Set inputs
        fis.setVariable("service", 3);
        fis.setVariable("food", 5);

        // Evaluate
        fis.evaluate();

        // Show output variable's chart
        Variable tip = fis.getVariable("tip");
        Double result = tip.defuzzify();
        //fis.getVariable("tip").chartDefuzzifier(true);
        JFuzzyChart.get().chart(tip, true);
        
        // Print ruleSet
        System.out.println(fis);
        System.out.println(result);
    }
}
