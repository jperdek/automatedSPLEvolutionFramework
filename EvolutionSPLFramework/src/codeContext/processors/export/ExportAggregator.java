package codeContext.processors.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.ExportAssetPlanner;


/**
 * Manages aggregation of exports
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class ExportAggregator {

	/**
	 * Logger to track information about export aggregation
	 */
	private static final Logger logger = LoggerFactory.getLogger(ExportAggregator.class);
	
	/**
	 * The map of export type (string identifier) to export context
	 */
	private Map<String, ExportedContext> associatedExports;
	
	/**
	 * The map of export type (string identifier) to export any exported object
	 */
	private Map<String, ExportedObjectInterface> contextToExportMap;
	
	/**
	 * Functionality to plan use of exported objects during evolution or its subsequence
	 */
	private ExportAssetPlanner exportPlanner;
	
	/**
	 * Creates export aggregator to manage exports
	 */
	public ExportAggregator(ExportAssetPlanner exportPlanner) {
		this.exportPlanner = exportPlanner;
		this.associatedExports = new HashMap<String, ExportedContext>();
		this.contextToExportMap = new HashMap<String, ExportedObjectInterface>();
	}
	
	/**
	 * Associates export object with the export type (string identifier)
	 * 
	 * @param exportType - usually variable name as unique identifier
	 * @param exportedContext - the exported context object with information about export
	 */
	public void associateExport(String exportType, ExportedContext exportedContext) {
		this.exportPlanner.putAssetToPlanner(exportType, (ExportedInterface) exportedContext);
		this.associatedExports.put(exportType, exportedContext);
	}
	
	/**
	 * Associates export object with the export type (string identifier)
	 * 
	 * @param exportType - usually variable name as unique identifier
	 * @param context - the exported context object with information about export
	 */
	public void associateExport(String exportType, ExportedObjectInterface context) {
		this.exportPlanner.putAssetToPlanner(exportType, (ExportedInterface) context);
		this.contextToExportMap.put(exportType, context);
	}
	
	/**
	 * Returns the list of all exported contexts
	 * 
	 * @return the list of all exported contexts
	 */
	public List<ExportedContext> getAllExportedContexts() {
		return new ArrayList<ExportedContext>(this.associatedExports.values());
	}
	
	/**
	 * Prints aggregated exports
	 */
	public void printAggregatedExports() {
		String exportName;
		ExportedContext exportedContext;
		for (Entry<String, ExportedContext> exportedContextEntry: this.associatedExports.entrySet()) {
			exportName = exportedContextEntry.getKey();
			exportedContext = exportedContextEntry.getValue();
			
			logger.debug("<....IMPORT " + exportName + ": ");
			exportedContext.printContent();
		}
	}
}
