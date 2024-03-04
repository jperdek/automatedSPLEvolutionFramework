package codeContext.processors.export.exportedFileUnits;

import java.io.IOException;

import org.json.simple.JSONObject;

import codeContext.CodeContext;
import codeContext.InnerContext;
import codeContext.processors.export.ExportAggregator;
import dividedAstExport.InvalidSystemVariationPointMarkerException;
import divisioner.Divisioner;
import positiveVariabilityCodeInjector.ExportHarvester;


/**
 * Manages exported objects from particular file
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class FileExportUnit {

	/**
	 * The name of file with exports
	 */
	private String fileName;
	
	/**
	 * The aggregator of exports from particular file/script
	 */
	private ExportAggregator exportAggregator;
	
	/**
	 * Associated base/root inner context - pointing to hierarchy of inner contexts
	 */
	private InnerContext baseInnerContext;
	
	
	/**
	 * Creates file export unit and associates related information
	 * -the associated base/root inner context - pointing to hierarchy of inner contexts
	 * -the name of file with exports
	 * 
	 * @param fileName - the name of file with exports
	 * @param baseInnerContext - the associated base/root inner context - pointing to hierarchy of inner contexts
	 */
	public FileExportUnit(String fileName, InnerContext baseInnerContext) {
		this.fileName = fileName;
		this.baseInnerContext = baseInnerContext;
		this.exportAggregator = new ExportAggregator();
	}
	
	/**
	 * Returns the name of file with exports
	 * 
	 * @return the name of file with exports
	 */
	public String getFileName() { return this.fileName; }
	
	/**
	 * Returns the instance of aggregator with all exports across particular file (associated with this exported unit)
	 * 
	 * @return the instance of aggregator with all exports across particular file (associated with this exported unit)
	 */
	public ExportAggregator getAssociatedExportAggregator() { return this.exportAggregator; }
	
	/**
	 * Returns the associated base/root inner context - pointing to hierarchy of inner contexts
	 * 
	 * @return the associated base/root inner context - pointing to hierarchy of inner contexts
	 */
	public InnerContext getAssociatedBaseContext() { return this.baseInnerContext; }
	
	/**
	 * Creates ExportHarvester instance and uses it to harvest exports from given AST (script)
	 * -the exports are harvested into aggregator object instance
	 *  
	 * @param astRoot - the root of processed AST
	 * @param codeContext - the base inner code context
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 */
	public void harvestExports(JSONObject astRoot, CodeContext codeContext) throws IOException, InterruptedException, InvalidSystemVariationPointMarkerException {
		ExportHarvester exportHarvester = new ExportHarvester(astRoot, this.exportAggregator);
		exportHarvester.harvestExportToContextMappings(astRoot, codeContext);
	}

	/**
	 * Returns the entity that is responsible for divisioning the functionality into variation points
	 * 
	 * @param divisioner - the entity responsible for divisioning the functionality into variation points
	 * @return the entity that is responsible for divisioning the functionality into variation points
	 */
	public static FileExportUnit loadFileExportUnit(Divisioner divisioner) {
		CodeContext extractedCodeContext = divisioner.getCodeContextFromDivision();
		String fileName = extractedCodeContext.getFileName();
		InnerContext baseInnerContext = extractedCodeContext.getInnerContext().getBaseContext();
		FileExportUnit fileExportUnit = new FileExportUnit(fileName, baseInnerContext);
		return fileExportUnit;
	}
	
	/**
	 * Prints the aggregated context associated with particular file (associated with this exported unit)
	 */
	public void printContent() {
		this.exportAggregator.printAggregatedExports();
	}
}
