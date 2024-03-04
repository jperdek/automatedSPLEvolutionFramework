package evolutionSimulation.tests;

import java.io.IOException;
import java.util.UUID;

import codeConstructsEvaluation.transformation.ASTConverterClient;
import codeContext.persistence.UpdatedTreePersistence;
import positiveVariabilityManagement.SynthesizedContent;
import splEvolutionCore.DebugInformation;


/**
 * Functionality to manage TypeScript file in pure JavaScript environment that is stored in 
 * variable and transpiled on the fly for testing purposes
 * -manages also its persistence
 * 
 * @author Jakub Perdek
 *
 */
public class WrappedTypeScriptContentInVariable {

	/**
	 * The content of TypeScript file
	 */
	private String typeScriptContent;
	
	/**
	 * The name of the variable
	 */
	private String variableName;
	

	/**
	 * Creates the wrapped TypeScript content that is stored in variable and transpiled on the fly for testing purposes
	 * -it allows to manage TypeScript file in JavaScript environment
	 * 
	 * @param fileContent - the original content written in TypeScript
	 */
	public WrappedTypeScriptContentInVariable(String fileContent) {
		int splitPosition = fileContent.indexOf('=');
		this.typeScriptContent = fileContent.substring(splitPosition + 1);
		this.variableName = fileContent.substring(0, splitPosition);
		this.typeScriptContent = this.typeScriptContent.substring(
				this.typeScriptContent.indexOf('`') + 1, this.typeScriptContent.lastIndexOf('`'));
	}
	
	/**
	 * Returns the name of the variable
	 *  
	 * @return the name of the variable
	 */
	public String getVariableName() { return this.variableName; }
	
	/**
	 * Returns the original TypeScript content
	 * 
	 * @return the original TypeScript content
	 */
	public String getScript() { return this.typeScriptContent; }
	
	/**
	 * Forces the unique naming of variable mainly to prevent conflict during import and transpillation in the test HTML template 
	 * -variable is used to store the base imported fractal rendering script written in TypeScript in string form that should be transpilled before its application inside browser
	 * 
	 * @param scriptContent - the original TypeScript content 
	 * @param synthesizedContent - the sources necessary to manage synthesis of the evolved part, especially target paths, application/SPL and array of variation points
	 * @param isBaseContent - if true then based content is processed differently by converting updated AST into resulting code otherwise only variable name is replaced
	 * @return the final content of processed script
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String forceUniqueVariableNamesAndAdaptItToStringContent(String scriptContent, 
			SynthesizedContent synthesizedContent, boolean isBaseContent) throws IOException, InterruptedException {
		UUID uuid = UUID.randomUUID();
		String previousVariableName = this.variableName;
		String scriptContentFromAST, finalScriptContent;
		this.variableName = this.variableName.strip() + uuid.toString().substring(0, 5).strip();
		if (isBaseContent) {
			if (DebugInformation.PROCESS_STEP_INFORMATION) { System.out.println("Getting updated/evolved base script content."); }
			scriptContentFromAST = ASTConverterClient.convertFromASTToCode(
				synthesizedContent.getReferenceToProcessedAST().toString());
			finalScriptContent = scriptContent.split(previousVariableName)[0] + this.variableName + " = `" + scriptContentFromAST + "`";
			this.typeScriptContent = finalScriptContent;
			return finalScriptContent;
		}
		finalScriptContent = scriptContent.replaceFirst(previousVariableName, this.variableName);
		return finalScriptContent;
	}
	
	/**
	 * Persists the AST in the file
	 * 
	 * @param scriptCode - the code that is going to be persisted
	 * @param evolutionConfiguration - the configuration of the evolution process
	 * @param projectId - the unique identifier of final project/SPL
	 * @param synthesizedContent - the sources necessary to manage synthesis of the evolved part, especially target paths, application/SPL and array of variation points
	 * @param currentDestinationScriptPath - the destination path where resulting AST is going to be persisted
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void persistAstFromSynthesizedContent(String scriptCode, EvolutionConfiguration evolutionConfiguration, String projectId, 
			SynthesizedContent synthesizedContent, String currentDestinationScriptPath) throws IOException, InterruptedException {
		//String targetDestinationPath = evolutionConfiguration.getOutputFilePath(projectId);
		if (currentDestinationScriptPath == null || currentDestinationScriptPath.equals("")) {
			currentDestinationScriptPath = evolutionConfiguration.getCurrentEvolvedScriptRelativePath();
		}
		//if (currentScriptPath.contains("file:///")) { v = "file:///" + currentScriptPath.replace("://", ":/"); }
		if (DebugInformation.PROCESS_STEP_INFORMATION) { System.out.println("Persisting code in file: " + currentDestinationScriptPath); }
		UpdatedTreePersistence.persistsAstInFile(currentDestinationScriptPath, scriptCode);
	} 
}
