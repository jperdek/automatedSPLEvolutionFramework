package automatedPlanner;

import evolutionSimulation.productAssetsInitialization.SharedConfiguration;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class VariabilityModelConfiguration {

	public void testLayer1() {
        // Load from 'FCL' file
        String fileName = SharedConfiguration.PROJECT_PATH + "/fuzzyPlannerConfiguration/step1VariationPointsDivisionStrategy.fcl";
        FIS fis = FIS.load(fileName,true);
        // Error while loading?
        if( fis == null ) { 
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }

        // Show 
        //fis.chart();
        JFuzzyChart.get().chart(fis);
        
        // Set inputs
        fis.setVariable("featureDispersion", 50);
        fis.setVariable("iterationComplexity", 70);
        fis.setVariable("featurePosition", 50);
        
        // Evaluate
        fis.evaluate();

        // Show output variable's chart
        Variable tip = fis.getVariable("variationPointsProportion");
        Double result = tip.defuzzify();
        //fis.getVariable("tip").chartDefuzzifier(true);
        JFuzzyChart.get().chart(tip, true);
        
        // Print ruleSet
        System.out.println(fis);
        System.out.println(result);    
	}
	
	public void testLayer2() {
        // Load from 'FCL' file
        String fileName = SharedConfiguration.PROJECT_PATH + "/fuzzyPlannerConfiguration/step2CommonalityVariabilityStrategy.fcl";
        FIS fis = FIS.load(fileName, true);
        // Error while loading?
        if( fis == null ) { 
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }

        // Show 
        //fis.chart();
        JFuzzyChart.get().chart(fis);
        
        // Set inputs
        fis.setVariable("iterationComplexity", 70);
        fis.setVariable("structuralVariability", 50);
        fis.setVariable("reuseOrientation", 50);
        
        // Evaluate
        fis.evaluate();

        // Show output variable's chart
        Variable tip = fis.getVariable("variationPointsProportion");
        Double result = tip.defuzzify();
        //fis.getVariable("tip").chartDefuzzifier(true);
        JFuzzyChart.get().chart(tip, true);
        
        // Print ruleSet
        System.out.println(fis);
        System.out.println(result);    
	}

	public static void main(String[] args) throws Exception {

    }
}
