package codeContext.processors.export.exportedFileUnits;

import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;

import codeConstructsEvaluation.transformation.PostRequester;
import codeContext.CodeContext;
import codeContext.processors.NotFoundVariableDeclaration;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import divisioner.Divisioner;
import divisioner.VariationPointDivisionConfiguration;
import evolutionSimulation.tests.WrappedTypeScriptContentInVariable;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;


/**
 * Manages merging of file exports
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class FileExportUnitsToMerge {

	/**
	 * Performs all operation with harvesting and associating the exports from given file
	 * 
	 * @param inputCodePathForFile - the path to input code with exports (file which content can be used/imported in base script)
	 * @param extensionBeforeName - the extension before file name that is optionally removed
	 * @param fileExportUnits - the manager of exports across multiple files
	 * 
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	private static void prepareAndAddFileExportUnitsToMerge(String inputCodePathForFile, 
			String extensionBeforeName, FileExportsUnits fileExportUnits) throws NotFoundVariableDeclaration, 
				IOException, InterruptedException, InvalidSystemVariationPointMarkerException, 
				DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		CodeContext codeContextForFile;
		String fileContent = PostRequester.loadFileContent(inputCodePathForFile);
		if (fileContent.equals("") || fileContent == null) { return; }
		WrappedTypeScriptContentInVariable wrappedTypeScriptContentInVariable = new WrappedTypeScriptContentInVariable(fileContent);
		Divisioner divisionerForFileToMerge = new Divisioner();
		VariationPointDivisionConfiguration divisionStrategyForFile = divisionerForFileToMerge.getDivisionStrategy();
		
		String fileName = inputCodePathForFile.replace(extensionBeforeName, "");
		fileName = fileName.substring(fileName.lastIndexOf('/') + 1);

		JSONObject astRoot = divisionStrategyForFile.divisionAndGetHighlightedAst(
				fileName, wrappedTypeScriptContentInVariable.getScript(), divisionerForFileToMerge);

		FileExportUnit fileExportUnitForFile = FileExportUnit.loadFileExportUnit(divisionerForFileToMerge);

		codeContextForFile = divisionerForFileToMerge.getCodeContextFromDivision(); 
		fileExportUnitForFile.harvestExports(astRoot, codeContextForFile);
		fileExportUnits.addFileContextMapping(codeContextForFile.getFileName(), fileExportUnitForFile);
	}
	
	/**
	 * Performs all operation with harvesting and associating the exports from given file
	 * 
	 * @param fileContent - the string content of processed file
	 * @param fileName - the name of processed file
	 * @param extensionBeforeName - the extension before file name that is optionally removed
	 * @param fileExportUnits - the manager of exports across multiple files
	 * 
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public static void prepareAndAddFileExportUnitsToMergeFileContent(String fileContent, String fileName,
			String extensionBeforeName, FileExportsUnits fileExportUnits) throws NotFoundVariableDeclaration, 
				IOException, InterruptedException, InvalidSystemVariationPointMarkerException, 
				DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		CodeContext codeContextForFile;
		Divisioner divisionerForFileToMerge = new Divisioner();
		VariationPointDivisionConfiguration divisionStrategyForFile = divisionerForFileToMerge.getDivisionStrategy();
		
		fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
		JSONObject astRoot = divisionStrategyForFile.divisionAndGetHighlightedAst(
				fileName, fileContent, divisionerForFileToMerge);

		FileExportUnit fileExportUnitForFile = FileExportUnit.loadFileExportUnit(divisionerForFileToMerge);

		codeContextForFile = divisionerForFileToMerge.getCodeContextFromDivision(); 
		fileExportUnitForFile.harvestExports(astRoot, codeContextForFile);
		fileExportUnits.addFileContextMapping(codeContextForFile.getFileName(), fileExportUnitForFile);
	}
	
	/**
	 * Creates and returns the manager for exports across various/processed files
	 * 
	 * @param inputCodePathsForFile - the path to input code with exports (file which content can be used/imported in base script)
	 * @param extensionBeforeName - the extension before file name that is optionally removed
	 * @return the manager for exports across various/processed files
	 * 
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public static FileExportsUnits prepareDefaultFileExportUnitsToMerge(
					List<String> inputCodePathsForFile, String extensionBeforeName) throws NotFoundVariableDeclaration, 
			IOException, InterruptedException, InvalidSystemVariationPointMarkerException, 
			DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation {
		FileExportsUnits fileExportUnits = new FileExportsUnits();
		for (String inputCodePathForFile: inputCodePathsForFile) {
			FileExportUnitsToMerge.prepareAndAddFileExportUnitsToMerge(inputCodePathForFile, extensionBeforeName, fileExportUnits);
		}
		return fileExportUnits;
	}
}
