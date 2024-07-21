package codeContext.processors.export;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import astFileProcessor.ASTLoader;
import positiveVariabilityManagement.callsInstantiationFromTemplateStrategies.AlreadyChosenVariationPointForInjectionException;


/**
 * Manages aggregation of export locations with associated exported objects under one path for each file
 * - export aggregation K-- export locations K-- export location K-- exports
 * 
 * @author Jakub Perdek
 *
 */
public class ExportLocationAggregation {
	
	/**
	 * The map of file names (paths) to exported locations
	 */
	private Map<String, ExportLocations> fileBasedLocations;
	
	/**
	 * information if callable constructs must be necessarily injected into specific variation point if true otherwise false
	 */
	private boolean belongToParticularVariationPoint = false;
	
	/**
	 * Identifier of variation point that should be used to inject the functionality
	 */
	private String variationPointToInjectCondIdentifier = null;

	/**
	 * Initializes the aggregation of export locations
	 */
	public ExportLocationAggregation() { this(null); }
	
	/**
	 * Initializes the aggregation of export locations with possible dependency on particular variation point
	 * 
	 * @param variationPointToInjectCondIdentifier - identifier of variation point that should be used to inject the functionality
	 */
	public ExportLocationAggregation(String variationPointToInjectCondIdentifier) {
		this.fileBasedLocations = new HashMap<String, ExportLocations>();
		this.variationPointToInjectCondIdentifier = variationPointToInjectCondIdentifier;
		if (this.variationPointToInjectCondIdentifier == null) {
			this.belongToParticularVariationPoint = true;
		}
	}
	
	/**
	 * Checks if this callable construct belongs to particular variation point
	 * 
	 * @return true if this callable construct belongs to particular variation point
	 */
	public boolean belongsToParticularVariationPoint() { return this.belongToParticularVariationPoint; }
	
	/**
	 * Returns the variation point identifier if the callable construct has prescribed it as dependency otherwise null
	 * - can be checked with belongsToParticularVariationPoint()
	 * 
	 * @return the variation point identifier if the callable construct has prescribed it as dependency otherwise null
	 */
	public String getVariationPointToInjectIdentifier() { return this.variationPointToInjectCondIdentifier; }

	/**
	 * Merges samples from export location aggregation into this one 
	 * 
	 * @param exportLocationAggregation - the another export location aggregation that should be merged
	 */
	public void merge(ExportLocationAggregation exportLocationAggregation) {
		this.fileBasedLocations.putAll(exportLocationAggregation.getFileBasedLocations());
	}
	
	/**
	 * Returns the mapping of export locations to associated file names (paths)
	 * 
	 * @return the mapping of export locations to associated file names (paths)
	 */
	public Map<String, ExportLocations> getFileBasedLocations() { return this.fileBasedLocations; }
	
	/**
	 * Aggregates the export location under export locations or creates new one
	 * 
	 * @param exportLocation
	 */
	public void aggregateLocation(ExportLocation exportLocation) {
		String fileName = exportLocation.getFileName();
		ExportLocations exportedLocations;

		if (this.fileBasedLocations.containsKey(fileName)) {
			exportedLocations = this.fileBasedLocations.get(fileName);
			exportedLocations.addLocation(exportLocation);
		} else {
			exportedLocations = new ExportLocations(exportLocation);
			this.fileBasedLocations.put(fileName, exportedLocations);
		}
	}

	/**
	 * Builds import string if exports (export locations with exported objects) exist
	 * 
	 * @param aggregatedFileName - the file name (path) where particular exports were harvested
	 * @return the line with the import statement in string
	 */
	public String buildImportIfExist(String aggregatedFileName) {
		ExportLocations usedVariablesFromLocation = this.fileBasedLocations.get(aggregatedFileName);
		return usedVariablesFromLocation.createImportStr();
	}
	
	/**
	 * Compares the aggregated exports with another exports aggregated in another export location aggregation
	 * 
	 * @param exportLocationAggregation - another aggregation of exports that should be compared
	 * @return not matched exports from this base aggregation in comparison with the one provided in argument
	 */
	public Set<String> compareImports(ExportLocationAggregation exportLocationAggregation) { 
		Set<String> resultingSet = new HashSet<String>(exportLocationAggregation.getFileBasedLocations().keySet());
		resultingSet.removeAll(this.fileBasedLocations.keySet());
		return resultingSet;
	}
	
	/**
	 * Builds all imports from this base aggregation of exports
	 * 
	 * @return builds all imports from this base aggregation of exports
	 */
	public String buildAllImports() {
		String allImports = "";
		if (this.fileBasedLocations.size() == 0) { return null; }
		for(ExportLocations usedVariablesFromLocation: this.fileBasedLocations.values()) {
			allImports = allImports + usedVariablesFromLocation.createImportStr() + "\n";
		}
		return allImports;
	}
	
	/**
	 * Creates the AST representation of the import construct/object
	 * 
	 * @param constructName - the name of the imported construct/object name
	 * @return the AST representation of the import construct
	 */
	private JSONObject createImportConstructAST(String constructName) {
		String importConfigurationStr = "{\"pos\":8,\"end\":18,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":273,\"isTypeOnly\":false,\"name\":{\"pos\":8,\"end\":18,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":79,\"escapedText\":\"" + constructName + "\"}}";
		return ASTLoader.loadASTFromString(importConfigurationStr);
	}
	
	/**
	 * Integrates the imports under initial statements in the script
	 * -appends new used imported objects into the already provided imports
	 * -appends new imports  
	 * 
	 * @param originalAst - the original AST of the base application/script that is updated
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void integrateImports(JSONObject originalAst) throws IOException, InterruptedException {
		JSONObject statementAst, constructAstToInclude, wholeImportAst;
		String importPath;
		String originalImportName;
		Set<String> actualImportConstructs;
		Set<ExportLocations> usedLocations = new HashSet<ExportLocations>();
		JSONArray originalAstStatements = (JSONArray) originalAst.get("statements");
		
		for (Object statementObjectAst: originalAstStatements) {
			statementAst = (JSONObject) statementObjectAst;
			if (statementAst.containsKey("importClause")) {
				importPath = (String) ((JSONObject) statementAst.get("moduleSpecifier")).get("text");
				
				actualImportConstructs = new HashSet<String>();
				for (Object importElementObjectAst: ((JSONArray) ((JSONObject) statementAst.get("namedBindings")).get("elements"))) {
					originalImportName = (String) ((JSONObject) ((JSONObject) importElementObjectAst).get("name")).get("text");
					actualImportConstructs.add(originalImportName);
				}
				
				for (ExportLocations mappedLocations: this.fileBasedLocations.values()) {
					if (importPath.indexOf(mappedLocations.getImportPath()) >= 0) {
						
						for (String constructToInclude: mappedLocations.getExportedConstructNames()) {
							if (!actualImportConstructs.contains(constructToInclude)) {
								//name is not in existing import
								constructAstToInclude = this.createImportConstructAST(constructToInclude);
								((JSONArray) ((JSONObject) statementAst.get("namedBindings")).get("elements")).add(constructAstToInclude);
								usedLocations.add(mappedLocations);
							}
						}
					}
				}				
			}
		}
		JSONArray futureStatements = new JSONArray();
		for (ExportLocations mappedLocations: this.fileBasedLocations.values()) {
			if (!usedLocations.contains(mappedLocations)) {
				wholeImportAst = mappedLocations.createImportToAST();
				futureStatements.addAll(originalAstStatements);
			}
		}
		futureStatements.addAll(originalAstStatements);
		originalAst.put("statements", futureStatements);//ERRORRRR
	}
}
