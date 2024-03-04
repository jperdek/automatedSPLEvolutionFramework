package dataRepresentationsExtensions;

import java.io.IOException;
import org.json.simple.JSONObject;
import evolutionSimulation.tests.EvolutionConfiguration;


/**
 * Perspective for injection of functionality to create various data representations from final products
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class DataRepresentationPerspective {

	/**
	 * Searches AST and injects functionality according to available AST objects
	 */
	private DataRepresentationInnerWrappers dataRepresentationInnerWrappers;
	
	/**
	 * Initializes the perspective to incorporate functionality to extract and harvest various data representations from final products
	 */
	public DataRepresentationPerspective() {
		this.dataRepresentationInnerWrappers = new DataRepresentationInnerWrappers();
	}
	
	/**
	 * Optionally injects data representation perspective
	 * 
	 * @param astRoot - the root of the base updated AST of final product script
	 * @param evolutionConfiguration - 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void optionallyOpenDataRepresentationPerspective(EvolutionConfiguration evolutionConfiguration, JSONObject astRoot) throws IOException, InterruptedException {
		DataRepresentationsConfiguration dataRepresentationsConfiguration = evolutionConfiguration.getDataRepresentationsConfiguration();
		if ((dataRepresentationsConfiguration == null && DefaultDataRepresentations.SIMULATE_STACK) || 
				dataRepresentationsConfiguration.shouldInjectStackFunctionality() || dataRepresentationsConfiguration.shouldInjectLogFunctionality()) {
			this.dataRepresentationInnerWrappers.additionalDataRepresentationsCreatorsInjector(evolutionConfiguration, dataRepresentationsConfiguration, astRoot);
		}	
	}
}
