package positiveVariabilityCodeInjector;

import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import codeContext.CodeContext;
import codeContext.GlobalContext;
import codeContext.InnerContext;
import codeContext.processors.export.ExportAggregator;
import codeContext.processors.export.ExportsProcessor;
import dividedAstExport.InvalidSystemVariationPointMarkerException;


/**
 * Harvests the exports from the global and inner contexts
 *  
 * @author Jakub Perdek
 *
 */
public class ExportHarvester {

	/**
	 * Aggregator instance to aggregate exports
	 */
	private ExportAggregator exportAggregator;

	
	/**
	 * Creates/instantiates the export harvester
	 * -exports have empty modifier in AST
	 * 
	 * @param astRoot - the root of application AST
	 * @param exportAggregator - aggregator instance to aggregate exports
	 */
	public ExportHarvester(JSONObject astRoot, ExportAggregator exportAggregator) {
		this.exportAggregator = exportAggregator;
	}

	/**
	 * Harvests and aggregates the exports
	 * -from global code context
	 * -from inner context hierarchy - where exports are marked in code
	 * 
	 * @param astRoot - the root of application AST
	 * @param codeContext - the code context that aggregates the global and the root of inner context hierarchy 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 */
	public void harvestExportToContextMappings(JSONObject astRoot, 
			CodeContext codeContext) throws IOException, 
			InterruptedException, InvalidSystemVariationPointMarkerException {
		InnerContext initialContext = codeContext.getInnerContext();
		InnerContext baseContext = initialContext.getBaseContext();
		GlobalContext globalContext = codeContext.getGlobalContext();
		String fileName = codeContext.getFileName();

		initialContext.markDirrectExportMapping(this.exportAggregator, fileName, baseContext); //marked as export before
		globalContext.markDirrectExportMapping(this.exportAggregator, fileName, baseContext);  //marked as export before
		this.harvestExportsOfGivenFile(astRoot, astRoot, initialContext, globalContext);
	}
	
	/**
	 * Recursively walks through application AST and search for exported contexts
	 * 
	 * @param astRoot - the root of application AST
	 * @param astPart - the actually processed AST part
	 * @param initialContext - the initial/base/root inner context of inner context hierarchy
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript) 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidSystemVariationPointMarkerException
	 */
	private void harvestExportsOfGivenFile(JSONObject astRoot, JSONObject astPart, 
			InnerContext initialContext, GlobalContext globalContext) throws IOException, 
						InterruptedException, InvalidSystemVariationPointMarkerException {
		String key;
		if (astPart == null) { return; }
		//tries to find mapping/exports
		ExportsProcessor.exportNamesAndFindMapping(astPart, this.exportAggregator, initialContext, globalContext);
	
		Object entryValue;
		JSONObject entryJSONObject;
		for(Object entryKey: astPart.keySet()) {
			key = (String) entryKey;
			entryValue = astPart.get(key);
			//if (key.equals("illegalDecorators")) {	continue; }
			if (entryValue instanceof JSONObject) {
				entryJSONObject = (JSONObject) entryValue;
				this.harvestExportsOfGivenFile(astRoot, entryJSONObject, initialContext, globalContext);
			} else if(entryValue instanceof JSONArray) {
				for (Object arrayPart: ((JSONArray) entryValue)) {
					entryJSONObject = (JSONObject) arrayPart;
					this.harvestExportsOfGivenFile(astRoot, entryJSONObject, initialContext, globalContext);
				}
			}
		}
	}
}
