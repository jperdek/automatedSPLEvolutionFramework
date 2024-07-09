package evolutionSimulation;

import java.util.ArrayList;
import java.util.List;

import dataRepresentationsExtensions.DataRepresentationsConfiguration;
import evolutionSimulation.iteration.EvolutionSamples;
import evolutionSimulation.iteration.EvolutionVariables;
import evolutionSimulation.productAssetsInitialization.Resource;
import splEvolutionCore.DebugInformation;


/**
 * The configuration for given evolution process
 * -related to chosen assets
 * 
 * @author Jakub Perdek
 *
 */
public class EvolutionConfiguration {

	/**
	 * The name of the concern
	 */
	private String concernName = null;
	
	/**
	 * The path to source project/sources that are extended
	 */
	private String inputFilePath;
	
	/**
	 * The path to destination directory where assets and extensions will be incorporated
	 */
	private String outputFilePath;
	
	/**
	 * The path to base script/source code that is managed/extended or updated
	 */
	private String pathToScriptInputFilePath;
	
	/**
	 * The iteration number - after each phase it is increased
	 */
	private int iteration = 1;
	
	/**
	 * The relative path to base/evolved script/source code that is managed/extended or updated
	 * -  the initial setting should cover setting this information to start evolution only on one (application SPL) project or 
	 * set pathToEvolvedSPLProjectDirectory to start SPL evolution on multiple (application SPLs) candidates
	 */
	private String currentEvolvedScriptRelativePath = null;
	
	/**
	 * Path to actually evolved SPL projects directory
	 * -  the initial setting should cover setting this information to start SPL evolution on multiple (application SPLs) candidates or 
	 * set pathToScriptInputFilePath to start evolution only on one (application SPL) project
	 *  
	 */
	private String pathToEvolvedSPLProjectDirectory = null;

	/**
	 * OPTIONAL/ TEST BASED
	 * The path to template HTML file, especially to run tests of evolved functionality
	 */
	private String templateConfigurationPath = null;
	
	/**
	 * The configuration to inject and optionally drive functionality for the creation of various data representations
	 */
	private DataRepresentationsConfiguration dataRepresentationsConfiguration;
	
	/**
	 * OPTIONAL/ TEST BASED
	 * The list of used resources that are inserted to template to run tests of evolved functionality
	 * -project imports
	 * -project templates
	 */
	private List<Resource> initialCodeResources;
	
	/**
	 * OPTIONAL/ TEST BASED
	 * Initial code that is inserted to template to run tests of evolved functionality
	 */
	private String initialCode;
	
	/**
	 * Instance managing conditions to terminate the SPL evolution process
	 */
	private EvolutionTerminateConditions evolutionTerminateConditions;
	
	/**
	 * Number of evolved members in population (for previous evolution iterations)
	 */
	private int numberOfEvolvedMembersInPopulation = 3;
	
	/**
	 * Strategies to direct the evolution process
	 * 
	 * @author Jakub Perdek
	 *
	 */
	private static enum EvolutionConfigurationDirectionStrategies {
		QUALITY_BASED("qualityBased"),
		FUNCTIONALITY_BASED("functionalityBased"),
		CUSTOM("custom");

		/**
		 * String identifier of given strategy
		 */
		public final String label;

		/**
		 * Creates the given Evolution configuration strategy with specified name
		 * 
		 * @param label - string identifier of given strategy
		 */
	    private EvolutionConfigurationDirectionStrategies(String label) {
	        this.label = label;
	    }
	}
	
	/**
	 * The instance of evolution configuration to drive evolution and store relevant information 
	 * 
	 * @param initialCode - the initial code that is inserted to template to run tests of evolved functionality
	 * @param concernName - the name of evolved concern to properly name entities - rnadom string can be used
	 * @param inputFilePath - the path to target project/sources that are extended
	 * @param outputFilePath - the path to destination directory where assets and extensions will be incorporated
	 * @param pathToScriptInputFilePath - the path to base script/source code that is managed/extended or updated
	 * @param currentEvolvedScriptRelativePath - the relative path to base/evolved script/source code that is managed/extended or updated
	 */
	public EvolutionConfiguration(String initialCode, String concernName, String inputFilePath,
			String outputFilePath, String pathToScriptInputFilePath, 
			String currentEvolvedScriptRelativePath, DataRepresentationsConfiguration dataRepresentationsConfiguration) {
		this(initialCode, concernName, inputFilePath, outputFilePath, pathToScriptInputFilePath, 
				currentEvolvedScriptRelativePath, dataRepresentationsConfiguration, new EvolutionTerminateConditions());
	}
	
	/**
	 * The instance of evolution configuration to drive evolution and store relevant information 
	 * 
	 * @param initialCode - the initial code that is inserted to template to run tests of evolved functionality
	 * @param concernName - the name of evolved concern to properly name entities - rnadom string can be used
	 * @param inputFilePath - the path to target project/sources that are extended
	 * @param outputFilePath - the path to destination directory where assets and extensions will be incorporated
	 * @param pathToScriptInputFilePath - the path to base script/source code that is managed/extended or updated
	 * @param currentEvolvedScriptRelativePath - the relative path to base/evolved script/source code that is managed/extended or updated
	 * @param evolutionTerminateConditions - the conditions to terminate evolution process
	 */
	public EvolutionConfiguration(String initialCode, String concernName, String inputFilePath,
			String outputFilePath, String pathToScriptInputFilePath, String currentEvolvedScriptRelativePath,
			DataRepresentationsConfiguration dataRepresentationsConfiguration, EvolutionTerminateConditions evolutionTerminateConditions) {
		this.initialCode = initialCode;
		this.inputFilePath = inputFilePath;
		this.outputFilePath = outputFilePath;
		this.pathToScriptInputFilePath = pathToScriptInputFilePath;
		this.currentEvolvedScriptRelativePath = currentEvolvedScriptRelativePath;
		this.initialCodeResources = new ArrayList<Resource>();
		this.dataRepresentationsConfiguration = dataRepresentationsConfiguration;
		this.evolutionTerminateConditions = evolutionTerminateConditions;
	}

	/**
	 * Returns the number of evolved members in population (for previous evolution iterations)
	 * 
	 * @return the number of evolved members in population (for previous evolution iterations)
	 */
	public int getNumberOfEvolvedMembersInPopulation() { return this.numberOfEvolvedMembersInPopulation; }
	
	/**
	 * Sets the number of evolved members in population (for previous evolution iterations)
	 * 
	 * @param numberOfEvolvedMembersInPopulation - the number of evolved members in population (for previous evolution iterations)
	 */
	public void setNumberOfEvolvedMembersInPopulation(int numberOfEvolvedMembersInPopulation) { 
		this.numberOfEvolvedMembersInPopulation = numberOfEvolvedMembersInPopulation;
	} 
	
	/**
	 * Checks if condition for terminating the evolution process are met
	 * 
	 * @return true if (sub-)evolution process should be terminated otherwise not
	 */
	public boolean shouldTerminateEvolution() { return this.evolutionTerminateConditions.shouldTerminate(this.iteration); }
	
	/**
	 * Returns the configuration to inject and optionally drive functionality for the creation of various data representations
	 * 
	 * @return the configuration to inject and optionally drive functionality for the creation of various data representations
	 */
	public DataRepresentationsConfiguration getDataRepresentationsConfiguration() { return this.dataRepresentationsConfiguration; }
	
	/**
	 * Returns the initial code that is inserted to template to run tests of evolved functionality
	 * 
	 * @return the initial code that is inserted to template to run tests of evolved functionality
	 */
	public String getInitialCode() { return this.initialCode; }
	
	/**
	 * Returns the list of used resources that are inserted to template to run tests of evolved functionality
	 * 
	 * @return the list of used resources that are inserted to template to run tests of evolved functionality
	 */
	public List<Resource> getInitialResources() { return this.initialCodeResources; }
	
	/**
	 * Adds the resource to the initial code resources
	 * 
	 * @param resource - the resource usually as external script used to extend original functionality with exported parts
	 */
	public void addInitialResource(Resource resource) { this.initialCodeResources.add(resource); }
	
	/**
	 * Sets the path of current relative path of evolved script
	 * 
	 * @param currentEvolvedScriptRelativePath - the path of current relative path of evolved script
	 */
	public void setCurrentEvolvedScriptRelativePath(String currentEvolvedScriptRelativePath) {
		this.currentEvolvedScriptRelativePath = currentEvolvedScriptRelativePath;
	}
	
	/**
	 * Sets the path of template configuration path of evolved script
	 * 
	 * @param templateRelativePath
	 */
	public void setTemplateConfigurationPath(String templateRelativePath) { this.templateConfigurationPath = templateRelativePath; }
	
	/**
	 * Information string covering evolution identification used in evolved project name
	 */
	private final static String NAME_EVOLUTION_IDENTIFICATION_NOTE = "evolNum";
	
	/**
	 * Information string covering evolution identification used in evolved project path
	 */
	private final static String PATH_EVOLUTION_IDENTIFICATION_NOTE = "/evolNum";
	
	/**
	 * Information string covering concern identification used in evolved project path
	 */
	private final static String PATH_CONCERN_IDENTIFICATION_NOTE = "/conc";
	
	/**
	 * Information string covering application identification used in evolved project path
	 */
	private final static String PATH_APPLICATION_IDENTIFICATION_NOTE = "/app";
	
	/**
	 * Returns the relative configuration path to template HTML file
	 * -this file is usually used for test purposes
	 * -the file is named "index.html" by default
	 * 
	 * @return the relative configuration path to template HTML file
	 */
	public String getRelativeTemplateConfigurationPath() {
		if (this.templateConfigurationPath == null) {
			return "/index.html"; //return root of project - relativePath
		}
		return this.templateConfigurationPath; 
	}
	
	/**
	 * Returns the path to template configuration HTML file
	 * 
	 * @param targetDestinationPath - destination path to resulting directory if is modified otherwise use null
	 * @param applicationID - the unique identifier of application/evolved SPL
	 * @return the path to template configuration HTML file
	 */
	public String getTemplateConfigurationPath(String targetDestinationPath, String applicationID) {
		if (targetDestinationPath == null) { targetDestinationPath = this.getOutputFilePath(applicationID); }
		if (this.templateConfigurationPath == null) {
			return targetDestinationPath + "/index.html"; //return root of project - relativePath
		}
		return targetDestinationPath + this.templateConfigurationPath; 
	}
	
	/**
	 * Returns the current relative path to evolved script
	 * -the base script can changed during evolution to make evolution more complex
	 * 
	 * @return the current relative path to evolved script
	 */
	public String getCurrentEvolvedScriptRelativePath() { return this.currentEvolvedScriptRelativePath; }
	
	/**
	 * Returns the list of selected exported content paths (paths to variables and scripts) 
	 * that are going to extend the base script in evolution process
	 * in future:
	 * -inject variables to arguments of functions/constructors
	 * -insert the calls with the exported functionality
	 * actually exclude:
	 * -the paths to selected scripts with functionality
	 * -the paths to selected scripts with variables
	 * 
	 * @param extensionBeforeName - the extension before script name that will be appended to the exported content path
	 * @param contentPathsToOmit - the list of content paths that should be omitted
	 * @param variablesPathsOmit - the list of paths with configuration of various variables
	 * @return the list of selected variables and configuration scripts that are candidates/going to extend the base script in evolution process
	 */
	public List<String> getSelectedExportedContentPaths(String extensionBeforeName, 
			List<String> contentPathsToOmit, List<String> variablesPathsOmit) {
		//load external dependencies
		List<String> exportUnitsToMerge = EvolutionSamples.concatenateScriptPath(EvolutionSamples.getAllEvolutionSamples(
				contentPathsToOmit), extensionBeforeName);
		List<String> exportVariablesToMerge = EvolutionVariables.getAllEvolutionSamples(variablesPathsOmit);
		exportUnitsToMerge.addAll(exportVariablesToMerge);
		return exportUnitsToMerge;
	}
	
	/**
	 * Returns the list of selected exported content path (FIVE_EDGE script and all available scripts with variables) 
	 * that are going to extend the base script in evolution process
	 * in future:
	 * -inject variables to arguments of functions/constructors
	 * -insert the calls with the exported functionality
	 * 
	 * DEFAULT METHOD
	 * @return the list of selected exported content path (FIVE_EDGE script and all available scripts with variables) 
	 * that are going to extend the base script in evolution process
	 */
	public List<String> getSelectedExportedContentPaths() {
		//load external dependencies
		String extensionBeforeName = "/" + EvolutionSamples.PATH_TO_SCRIPT; //path to script
		List<String> exportUnitsToMerge = EvolutionSamples.concatenateScriptPath(EvolutionSamples.getAllEvolutionSamples(
				EvolutionSamples.FIVE_EDGE_INPUT_PATHS.subList(0, 1)), extensionBeforeName);
		List<String> exportVariablesToMerge = EvolutionVariables.getAllEvolutionSamples(null);
		exportUnitsToMerge.addAll(exportVariablesToMerge);
		return exportUnitsToMerge;
	}
	
	/**
	 * Returns the path extension directing to the source script in project folder
	 * -in default configuration the "/" is put before EvolutionSamples.PATH_TO_SCRIPT - "/js/platnoJS.js"
	 * 
	 * @return the path extension directing to the source script in project folder
	 */
	public String getPathExtensionToGetScript() { return "/" + EvolutionSamples.PATH_TO_SCRIPT; }
	
	/**
	 * Returns the unique name of the evolved content referencing the evolved concern (focus) and iteration
	 * 
	 * @return the unique name of the evolved content referencing the evolved concern (focus) and iteration
	 */
	public String getEvolvedContentName() {
		if (this.concernName == null || this.concernName.equals("")) {
			this.concernName = EvolutionConfigurationDirectionStrategies.CUSTOM.label;
		}
		return this.concernName + EvolutionConfiguration.NAME_EVOLUTION_IDENTIFICATION_NOTE + this.iteration; 
	}
	
	/**
	 * Returns the unique name of the evolved content referencing the evolved concern (focus) and iteration
	 * 
	 * @return the unique name of the evolved content referencing the evolved concern (focus) and iteration
	 */
	private String getEvolvedContentName(int evolutionIteration, String concernName) {
		if (concernName == null || concernName.equals("")) {
			concernName = EvolutionConfigurationDirectionStrategies.CUSTOM.label;
		}
		return concernName + EvolutionConfiguration.NAME_EVOLUTION_IDENTIFICATION_NOTE + evolutionIteration; 
	}
	
	/**
	 * Returns the name of currently evolved concern
	 * 
	 * @return the name of currently evolved concern
	 */
	public String getConcernName() { 
		if (this.concernName == null || this.concernName.equals("")) {
			this.concernName = EvolutionConfigurationDirectionStrategies.CUSTOM.label;
		}
		return this.concernName;
	}
	
	/**
	 * Returns the currently evolved iteration number
	 * 
	 * @return the currently evolved iteration number
	 */
	public int getIteration() { return this.iteration; }
	
	/**
	 * Increases evolution iteration by one
	 * 
	 */
	public void incrementIteration() { this.iteration = this.iteration + 1; }
	
	/**
	 * Updates evolution iteration according to another evolution configuration
	 * 
	 * * @param evolutionConfiguration - another configuration for evolution iteration
	 */
	public void updateIteration(EvolutionConfiguration evolutionConfiguration) { this.iteration = evolutionConfiguration.getIteration(); }
	
	/**
	 * Returns the path to source project/sources that are extended
	 *  
	 * @return the path to source project/sources that are extended
	 */
	public String getInputFilePath() { return this.inputFilePath; }
	
	/**
	 * Returns the relative output file path including the evolution 
	 * iteration directory, evolved concern directory and application id on path
	 * 
	 * @param applicationID - the unique identifier of application/evolved SPL
	 * @return the relative output file path including the evolution 
	 * iteration directory, evolved concern directory and application id on path
	 */
	public String getRelativeOutputFilePath(String applicationID) {
		if (this.concernName == null || this.concernName.equals("")) {
			this.concernName = EvolutionConfigurationDirectionStrategies.CUSTOM.label;
		}
		return EvolutionConfiguration.PATH_EVOLUTION_IDENTIFICATION_NOTE + Integer.toString(this.iteration) + 
				EvolutionConfiguration.PATH_CONCERN_IDENTIFICATION_NOTE + this.concernName + 
				EvolutionConfiguration.PATH_APPLICATION_IDENTIFICATION_NOTE + applicationID;
	}
	
	/**
	 * Returns the relative output file path including only the evolution 
	 * iteration directory and evolved concern directory

	 * @return the relative output file path including only the evolution iteration directory and evolved concern directory
	 */
	public String getRelativeOutputFilePathToEvolvedContent() {
		if (this.concernName == null || this.concernName.equals("")) {
			this.concernName = EvolutionConfigurationDirectionStrategies.CUSTOM.label;
		}
		return EvolutionConfiguration.PATH_EVOLUTION_IDENTIFICATION_NOTE + Integer.toString(this.iteration) + 
				EvolutionConfiguration.PATH_CONCERN_IDENTIFICATION_NOTE + this.concernName;
	}
	
	/**
	 * Checks if particular string is possible project name
	 * 
	 * @param projectName - particular string that should be checked
	 * @return true if particular string is possible project name otherwise false
	 */
	public boolean isProjectName(String projectName) {
		if (projectName.contains(EvolutionConfiguration.NAME_EVOLUTION_IDENTIFICATION_NOTE)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if particular string is possible last evolution project name
	 * 
	 * @param projectName - particular string that should be checked
	 * @return true if particular string is possible project name otherwise false
	 */
	public boolean isEvolutionLastIterationProjectName(String projectName) {
		String mainProjectNamePart = getEvolvedContentName(this.iteration - 1, this.concernName);
		if (projectName.contains(mainProjectNamePart)) { return true; }
		return false;
	}
	
	
	/**
	 * Returns the path to destination directory where assets and extensions will be incorporated
	 * -output path is concatenated with relative output path with use of application id
	 * 
	 * @param applicationID - the unique identifier of application/evolved SPL
	 * @return the path to destination directory where assets and extensions will be incorporated
	 */
	public String getOutputFilePath(String applicationID) { 
		return this.outputFilePath  + this.getRelativeOutputFilePath(applicationID);
	}
	
	/**
	 * Returns the path to destination directory where new currently evolved SPLs are stored/inserted
	 * 
	 * @return the path to destination directory where new currently evolved SPLs are stored/inserted
	 */
	public String getOutputFilePathToDirectoryUsedInCurrentEvolution() { 
		return this.outputFilePath + this.getRelativeOutputFilePathToEvolvedContent();
	}
	
	/**
	 * Returns the path to base script/source code that is managed/extended or updated
	 *  
	 * @return the path to base script/source code that is managed/extended or updated
	 */
	public String getPathToScriptInputFile() { return this.pathToScriptInputFilePath; }
	
	/**
	 * Sets the path to actually evolved script
	 * 
	 * @param pathToScriptInputFilePath - the path to actually evolved script
	 */
	public void setPathToScriptInputFile(String pathToScriptInputFilePath) {
		this.pathToScriptInputFilePath = pathToScriptInputFilePath;
	}
	
	/**
	 * Returns the path to actually evolved SPL projects directory
	 * 
	 */
	public void updatePathToEvolvedSPLProjectDirectory() {
		this.pathToEvolvedSPLProjectDirectory = this.getOutputFilePathToDirectoryUsedInCurrentEvolution(); 
	}
	
	/**
	 * Returns the path to actually evolved SPL projects directory
	 * 
	 * @return the path to actually evolved SPL projects directory
	 */
	public String getPathToEvolvedSPLProjectDirectory() { return this.pathToEvolvedSPLProjectDirectory; }
	
	/**
	 * Sets the path to actually evolved SPL projects directory
	 * 
	 * @param pathToEvolvedSPLProjectDirectory - the path to actually evolved SPL projects directory
	 */
	public void setPathToEvolvedSPLProjectDirectory(String pathToEvolvedSPLProjectDirectory) {
		this.pathToEvolvedSPLProjectDirectory = pathToEvolvedSPLProjectDirectory;
	}
	
	/**
	 * Returns path to destination directory where assets and extensions will be incorporated 
	 * concatenated with the AST file name ("/markedVariationPoints.json")
	 * 
	 * @param targetDestinationPath - destination path to resulting directory if is modified otherwise use null
	 * @param applicationID - the unique identifier of application/evolved SPL
	 * @return path to destination directory where assets and extensions will be incorporated 
	 * concatenated with the AST file name ("/markedVariationPoints.json")
	 */
	public String getFileOutputAstPath(String targetDestinationPath, String applicationID) { 
		if (targetDestinationPath == null) { targetDestinationPath = this.getOutputFilePath(applicationID); }
		return targetDestinationPath + "/markedVariationPoints.json"; 
	}
	
	/**
	 * Returns path to destination directory where assets and extensions will be incorporated 
	 * concatenated with the name of file where information about variation points is stored ("/harvestedVariationPoints.json")
	 * 
	 * @param targetDestinationPath - destination path to resulting directory if is modified otherwise use null
	 * @param applicationID - the unique identifier of application/evolved SPL
	 * @return path to destination directory where assets and extensions will be incorporated 
	 * concatenated with the name of file where information about variation points is stored ("/harvestedVariationPoints.json")
	 */
	public String getFileOutputVariationPoints(String targetDestinationPath, String applicationID) { 
		if (targetDestinationPath == null) { targetDestinationPath = this.getOutputFilePath(applicationID); }
		return targetDestinationPath + "/harvestedVariationPoints.json";
	}
	
	/**
	 * Sets the path to the evolved SPL project directory from latest evolution
	 * 
	 * @param globalEvolutionConfiguration - evolution configuration from previous iterations 
	 */
	public void setPathToEvolvedSPLProjectDirectoryFromLatestEvolution(EvolutionConfiguration globalEvolutionConfiguration) {
		System.out.println(globalEvolutionConfiguration.getConcernName());
		System.out.println(this.concernName);
		if (this.concernName != null) {
			this.concernName = globalEvolutionConfiguration.getConcernName();
		}
		if (DebugInformation.PROCESS_STEP_INFORMATION) { 
			System.out.println("Setting path to actually evolved SPL/applications for next evolution iteration: " + 
							globalEvolutionConfiguration.getPathToEvolvedSPLProjectDirectory()); 
		}
		if (globalEvolutionConfiguration.getPathToEvolvedSPLProjectDirectory() != null) {
			this.inputFilePath = this.pathToEvolvedSPLProjectDirectory = globalEvolutionConfiguration.getPathToEvolvedSPLProjectDirectory();
		} else {
			System.out.println("Path to directory from previous evolution iteration is not set!");
		}
	}
	
	/**
	 * Prints information about currently set configuration
	 */
	public void printCurrentConfiguration() {
		System.out.println();
		System.out.println("------------------------ CONFIGURATION ---------------------------");
		System.out.println("Processed concern name: " + this.concernName);
		System.out.println("Relative path to current evolved script: " + this.currentEvolvedScriptRelativePath);
		System.out.println("Input file path: " + this.inputFilePath);
		System.out.println("Iteration: " + this.iteration);
		System.out.println("Path to currently evolved SPL directory: " + this.pathToEvolvedSPLProjectDirectory);
		System.out.println("Output file path: " + this.outputFilePath);
		System.out.println("Template path: " + this.templateConfigurationPath);
		System.out.println("Path to script input path: " + this.pathToScriptInputFilePath);
		System.out.println("------------------------ CONFIGURATION ---------------------------");
		System.out.println();
	}
}
