package evolutionSimulation.iteration;

import java.util.ArrayList;
import java.util.List;

import evolutionSimulation.productAssetsInitialization.SharedConfiguration;


/**
 * Repository of custom/project specific variables for various scripts (fractals)
 * -variables should be selectively marked as exported to efficiently tune and direct the evolution process
 * 
 * @author Jakub Perdek
 *
 */
public class EvolutionVariables {
	
	/**
	 * Path to configuration variables directory for specific project based scripts
	 */
	public static final String PATH_TO_SCRIPT = 
			SharedConfiguration.PROJECT_PATH + "\\splsToMerge\\configurationVariables\\";
	
	/**
	 * Path to general configuration variables directory
	 */
	public static final String PATH_TO_BASE_SCRIPT = 
			SharedConfiguration.PROJECT_PATH + "\\splsToMerge\\configurationVariablesGeneral\\";
	
	/**
	 * Script names with general variables
	 */
	public static final List<String> BASE_VARIABLES_PATH = List.of("generalVariables.js");
	
	/**
	 * Script names with (project specific) variables for Krishna anklet fractal script
	 */
	public static final List<String> KRISHNA_ANKLET_VARIABLES_PATH = List.of(
			"variablesAnkletFractal3.js", "variablesAnkletFractal4.js"
	);
	
	/**
	 * Script names with (project specific) variables for Koch fractal script
	 */
	public static final List<String> KOCH_INPUT_VARIABLES_PATH = List.of("variablesKoch.js");
	
	/**
	 * Script names with (project specific) variables for Five edge fractal script
	 */
	public static final List<String> FIVE_EDGE_INPUT_VARIABLES_PATH = List.of("variablesFiveEdge.js");
	
	/**
	 * Script names with (project specific) variables for N-side fractal script
	 */
	public static final List<String> N_SIDE_INPUT_VARIABLES_PATH = List.of("variablesNSideHashTag.js");
	
	/**
	 * Script names with (project specific) variables for Sierpinski triangle fractal script
	 */
	public static final List<String> SIERPINSKI_INPUT_VARIABLES_PATH = List.of(
			"variablesSierpinskyInOneStroke.js", "variablesSierpinskyRecursion.js"
	);
	
	/**
	 * Script names with (project specific) variables for Wcurves fractal script
	 */
	public static final List<String> WCURVES_INPUT_VARIABLES_PATH = List.of(
			"variablesWkrivkyDoubled.js", "variablesWkrivkyFractal.js", "variablesWkrivkyFractal4Recu.js"
	);
	
	
	/**
	 * Creates instance of evolution variables manager
	 */
	public EvolutionVariables() {	
	}
	
	/**
	 * Returns all evolution paths with variables
	 * 
	 * @param pathsToRemove - the list of paths that are removed from prepared paths 
	 * @return the list of selected all evolution variable files except the removed paths that are provided as argument
	 */
	public static List<String> getAllEvolutionSamples(List<String> pathsToRemove) {
		List<String> variablesSamples = new ArrayList<String>();
		variablesSamples.addAll(EvolutionVariables.concatenateScriptPath(
				EvolutionVariables.BASE_VARIABLES_PATH, EvolutionVariables.PATH_TO_BASE_SCRIPT));
		variablesSamples.addAll(EvolutionVariables.concatenateScriptPath(
				EvolutionVariables.KRISHNA_ANKLET_VARIABLES_PATH, EvolutionVariables.PATH_TO_SCRIPT));
		variablesSamples.addAll(EvolutionVariables.concatenateScriptPath(
				EvolutionVariables.KOCH_INPUT_VARIABLES_PATH, EvolutionVariables.PATH_TO_SCRIPT));
		variablesSamples.addAll(EvolutionVariables.concatenateScriptPath(
				EvolutionVariables.FIVE_EDGE_INPUT_VARIABLES_PATH, EvolutionVariables.PATH_TO_SCRIPT));
		variablesSamples.addAll(EvolutionVariables.concatenateScriptPath(
				EvolutionVariables.N_SIDE_INPUT_VARIABLES_PATH, EvolutionVariables.PATH_TO_SCRIPT));
		variablesSamples.addAll(EvolutionVariables.concatenateScriptPath(
				EvolutionVariables.SIERPINSKI_INPUT_VARIABLES_PATH, EvolutionVariables.PATH_TO_SCRIPT));
		variablesSamples.addAll(EvolutionVariables.concatenateScriptPath(
				EvolutionVariables.WCURVES_INPUT_VARIABLES_PATH, EvolutionVariables.PATH_TO_SCRIPT));

		if (pathsToRemove != null) { variablesSamples.removeAll(pathsToRemove); }
		return variablesSamples;
	}
	
	/**
	 * Concatenates the script path with script names to get configured variables that will be used inside code 
	 * 
	 * @param evolutionSamplePathsImmutable - the list of script names
	 * @param scriptPathPart - the part of the script path that will be concatenated with the script name
	 * @return the list of paths to project related scripts (fractals) with configured code variables
	 */
	private static List<String> concatenateScriptPath(List<String> evolutionSamplePathsImmutable, String scriptPathPart) {
		List<String> evolutionSamplePaths = new ArrayList<String>(evolutionSamplePathsImmutable);
		for (int index = 0; index < evolutionSamplePaths.size(); index++) {
			evolutionSamplePaths.set(index, scriptPathPart + evolutionSamplePaths.get(index));
		}
		return evolutionSamplePaths;
	}
}
