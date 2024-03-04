package codeContext.processors.export.exportedFileUnits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import codeContext.processors.export.ExportedContext;
import splEvolutionCore.DebugInformation;


/**
 * Manages exports from/of particular files
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class FileExportsUnits {
	
	/**
	 * The map of file names to associated exports
	 */
	private Map<String, FileExportUnit> fileExportsMap;
	

	/**
	 * Initializes export units for particular (SPL project) files
	 */
	public FileExportsUnits() {
		this.fileExportsMap = new HashMap<String, FileExportUnit>();
	}
	
	/**
	 * Adds export to particular file mapping
	 * 
	 * @param fileName - the name of the file with association of exports (also imports if used)
	 * @param fileExportUnit - the file export unit associated with provided file
	 */
	public void addFileContextMapping(String fileName, FileExportUnit fileExportUnit) {
		this.fileExportsMap.put(fileName, fileExportUnit);
	}
	
	/**
	 * Returns the export unit associated with particular file
	 * 
	 * @param fileName - the name of the file with association of exports (also imports if used)
	 * @return the export unit associated with particular file
	 */
	public FileExportUnit getFileExportUnit(String fileName) {
		return this.fileExportsMap.get(fileName);
	}
	
	/**
	 * Returns the list of all exported contexts/units
	 * 
	 * @return the list of all exported contexts/units
	 */
	public List<ExportedContext> getAllExportedContexts() {
		List<ExportedContext> exportedContexts = new ArrayList<ExportedContext>();
		for (FileExportUnit usedFileExportUnit: this.fileExportsMap.values()) {
			exportedContexts.addAll(usedFileExportUnit.getAssociatedExportAggregator().getAllExportedContexts());
		}
		return exportedContexts;
	}

	/**
	 * Prints content of export units
	 */
	public void printContentOfExportUnits() {
		String fileName;
		FileExportUnit fileExportUnit;
		if (DebugInformation.SHOW_INFORMATION_ABOUT_EXPORTS) {
			for (Entry<String, FileExportUnit> fileToExportUnitEntry: this.fileExportsMap.entrySet()) {
				fileName = fileToExportUnitEntry.getKey();
				fileExportUnit = fileToExportUnitEntry.getValue();
				System.out.println("|&------ File name: " + fileName + "------&>");
				fileExportUnit.printContent();
				System.out.println("|&-------------------------------------------&|");
				System.out.println();
			}
		}
	}
}
