package evolutionSimulation.tests;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository of custom/project specific fractal rendering scripts (fractals)
 * -available functionality should be selectively marked as exported to efficiently tune and direct the evolution process
 * 
 * @author Jakub Perdek
 *
 */
public class EvolutionSamples {

	/**
	 * The relative path to script in source project that is responsible to render fractal in given evolution processs/cenario
	 */
	public static final String PATH_TO_SCRIPT = "js/platnoJS.js";
	
	/**
	 * The paths to various projects that render Krishna anklet fractal with the similar structure
	 */
	public static final List<String> KRISHNA_ANKLET_INPUT_PATHS = List.of(
			"E://aspects/spaProductLine/VariationPointDivisioner/splsToMerge/ankletFractal4",
			"E://aspects/spaProductLine/VariationPointDivisioner/splsToMerge/ankletFractal5"
	);
	
	/**
	 * The paths to various projects that render Koch snowflake fractal with the similar structure
	 */
	public static final List<String> KOCH_INPUT_PATHS = List.of(
			"E://aspects/spaProductLine/VariationPointDivisioner/splsToMerge/koch"
	);
	
	/**
	 * The paths to various projects that render Five edge fractal with the similar structure
	 */
	public static final List<String> FIVE_EDGE_INPUT_PATHS = List.of(
			"E://aspects/spaProductLine/VariationPointDivisioner/splsToMerge/fiveEdge"
	);
	
	/**
	 * The paths to various projects that render N side shape with the similar structure
	 */
	public static final List<String> N_SIDE_INPUT_PATHS = List.of(
			"E://aspects/spaProductLine/VariationPointDivisioner/splsToMerge/nSideHashtag"
	);
	
	/**
	 * The paths to various projects that render Sierpinski shape with the similar structure
	 */
	public static final List<String> SIERPINSKI_INPUT_PATHS = List.of(
			"E://aspects/spaProductLine/VariationPointDivisioner/splsToMerge/sierpinskyInOneStroke",
			"E://aspects/spaProductLine/VariationPointDivisioner/splsToMerge/sierpinskyRecursion"
	);
	
	/**
	 * The paths to various projects that render W-Curves shape with the similar structure
	 */
	public static final List<String> WCURVES_INPUT_PATHS = List.of(
			"E://aspects/spaProductLine/VariationPointDivisioner/splsToMerge/wkrivkyDoubled",
			"E://aspects/spaProductLine/VariationPointDivisioner/splsToMerge/wkrivkyFractal4Recu",
			"E://aspects/spaProductLine/VariationPointDivisioner/splsToMerge/wkrivkyFraktal"
	);
	
	
	/**
	 * Creates the instance to manage evolution script extensions
	 */
	public EvolutionSamples() {	}
	
	/**
	 * Returns all evolution paths with scripts containing exported modules that can be imported to project
	 * 
	 * @param pathsToRemove - the list of paths that are removed from prepared paths 
	 * @return the list of selected all evolution script files containing exported modules that can be imported 
	 * to project except the removed paths that are provided as argument
	 */
	public static List<String> getAllEvolutionSamples(List<String> pathsToRemove) {
		List<String> evolutionSamples = new ArrayList<String>();
		evolutionSamples.addAll(EvolutionSamples.KRISHNA_ANKLET_INPUT_PATHS);
		//evolutionSamples.addAll(EvolutionSamples.KOCH_INPUT_PATHS);
		//evolutionSamples.addAll(EvolutionSamples.FIVE_EDGE_INPUT_PATHS);
		//evolutionSamples.addAll(EvolutionSamples.N_SIDE_INPUT_PATHS);
		//evolutionSamples.addAll(EvolutionSamples.SIERPINSKI_INPUT_PATHS);
		//evolutionSamples.addAll(EvolutionSamples.WCURVES_INPUT_PATHS);

		if (pathsToRemove != null) { evolutionSamples.removeAll(pathsToRemove); }
		return evolutionSamples;
	}
	
	/**
	 * Concatenates the script path with script names to get scripts containing exported modules that can be imported to the project
	 * 
	 * @param evolutionSamplePathsImmutable - the list of script names
	 * @param scriptPathPart - the part of the script path that will be concatenated with the script name
	 * @return the list of paths to project related scripts (fractals) with scripts containing exported modules that can be imported to project
	 */
	public static List<String> concatenateScriptPath(List<String> evolutionSamplePaths, String scriptPathPart) {
		for (int index = 0; index < evolutionSamplePaths.size(); index++) {
			evolutionSamplePaths.set(index, evolutionSamplePaths.get(index) + scriptPathPart);
		}
		return evolutionSamplePaths;
	}
}
