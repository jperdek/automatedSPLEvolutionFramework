package codeContext.processors.export;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codeContext.GlobalContext;
import codeContext.InnerContext;
import codeContext.processors.ASTTextExtractorTools;
import codeContext.processors.FunctionProcessor;
import splEvolutionCore.DebugInformation;


/**
 * Functionality to extract and process exports
 * 
 * @author Jakub Perdek
 *
 */
public class ExportsProcessor {
	
	/**
	 * Logger to track information from exports processor
	 */
	private static final Logger logger = LoggerFactory.getLogger(ExportsProcessor.class);
	
	/**
	 * Get information if processed code construct is directly marked as exported
	 * -based on AST, empty record (without parameters, arguments, members or body)
	 * 
	 * @param codeConstructAst - particular/processed construct in AST 
	 * @return true if the processed code construct is directly marked as exported otherwise false
	 */
	public static boolean isConstructMarkedAsDirectExport(JSONObject codeConstructAst) {
		JSONArray astPartModifiers;
		JSONObject astModifier;
		if (!codeConstructAst.containsKey("modifiers")) { return false; }
		astPartModifiers = (JSONArray) codeConstructAst.get("modifiers");
		for (Object astModifierObject: astPartModifiers) {
			astModifier = (JSONObject) astModifierObject;
			if (!astModifier.containsKey("parameters") && !astModifier.containsKey("members") && 
					!astModifier.containsKey("arguments") && !astModifier.containsKey("body")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the default export from AST (from the root of AST)
	 * 
	 * @param astRoot - the root of application AST
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param localContext - inner context - the context known as local context, context inside functions, classes
	 * @return default export from AST (from the root of AST)
	 */
	public static ExportedObjectInterface getDefaultExportFromAst(JSONObject astRoot, GlobalContext globalContext, 
			InnerContext localContext) {
		JSONObject defaultExportAst = (JSONObject) astRoot.get("externalModuleIndicator");
		List<String> exportedParts = ExportsProcessor.getExportedParts(defaultExportAst);
		
		ExportedObjectInterface defaultExport;
		Long startPosition, endPosition;
		if (exportedParts.size() == 0 || exportedParts.size() == 1) {
			startPosition = (Long) defaultExportAst.get("pos");
			endPosition = (Long) defaultExportAst.get("end");
			defaultExport = localContext.findDefaultExport(startPosition, endPosition);
			if (defaultExport == null) {
				defaultExport = globalContext.findDefaultExport(startPosition, endPosition);
			}
			return defaultExport;
		}
		// this situation should not occur
		return null;
	}
	
	/**
	 * Extracts export code fragments
	 * -declared variables from exported declaration list
	 * -direct exports from export clause
	 * -export from expression
	 * 
	 * @param codeConstructAst - code construct in AST
	 * @return The list of exported code fragments/parts
	 */
	private static List<String> getExportedParts(JSONObject codeConstructAst) {
		List<String> exportedNames = new ArrayList<String>();
		
		String exportName;
		JSONArray innerParameters;
		JSONObject statementPart, exportPart, innerExpression;
		JSONArray declarations;
		JSONArray associatedStatements = (JSONArray) codeConstructAst.get("statements");
		for (Object statementObject: associatedStatements) {
			statementPart = (JSONObject) statementObject;
			if (statementPart.containsKey("exportClause")) {
				for (Object exportObject: (JSONArray) ((JSONObject) statementPart.get("exportClause")).get("elements")) {
					exportPart = (JSONObject) exportObject;
					exportName = ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(exportPart);
					exportedNames.add(exportName.strip());
				}
			} else if (statementPart.containsKey("declarationList") && statementPart.containsKey("modifiers")) {
				declarations = (JSONArray) ((JSONObject) statementPart.get("declarationList")).get("declarations");
				if (ExportsProcessor.isConstructMarkedAsDirectExport(codeConstructAst)) {
					for (Object declarationObject: declarations) {
						exportPart = (JSONObject) declarationObject;
						exportName = ASTTextExtractorTools.getTextFromAstIncludingOptionallyName(exportPart);
						exportedNames.add(exportName.strip());
					}
				}
			} else if (!statementPart.containsKey("members") && !statementPart.containsKey("body") && 
					!statementPart.containsKey("parameters") && !statementPart.containsKey("arguments") && !statementPart.containsKey("name")) {
				innerExpression = (JSONObject) statementPart.get("expression");
				if (innerExpression != null && innerExpression.containsKey("parameters")) {
					innerParameters = (JSONArray) innerExpression.get("parameters");
					exportName = "";
					for (Object parameterObject: innerParameters) {
						exportName = exportName + "|->|" + (String) ((JSONObject) parameterObject).get("escapedText");
					}
					exportName = exportName.substring(3);
				} else {
					exportName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(statementPart);
				}
				exportedNames.add(exportName);
			}
		}
		return exportedNames;
	}

	/**
	 * Analyzes and extracts the export names
	 * 
	 * @param codeConstructAst - the AST from which the export names are going to be harvested
	 * @return the list of extracted export names
	 */
	public static List<String> analyzeAndExtractsExportedNames(JSONObject codeConstructAst) {
		if (!codeConstructAst.containsKey("statements")) { return null; }
		return ExportsProcessor.getExportedParts(codeConstructAst);
	}
	
	/**
	 * Extracts the exported names from and harvests mapping between particular context and exported code fragment 
	 * 
	 * @param codeConstructAst - the AST from which the export names are going to be harvested
	 * @param exportAggregator - aggregator instance to aggregate exports
	 * @param initialContext - the initial/base/root inner context of inner context hierarchy
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 */
	public static void exportNamesAndFindMapping(JSONObject codeConstructAst, ExportAggregator exportAggregator, 
			InnerContext initialContext, GlobalContext globalContext) {
		List<String> exportedNames = ExportsProcessor.analyzeAndExtractsExportedNames(codeConstructAst);
		if (exportedNames != null) {
			if (DebugInformation.SHOW_POLLUTING_INFORMATION) {
				for (String exportedName: exportedNames) { logger.debug("Exported name: " + exportedName); }
			}
			initialContext.findContextToExportMapping(exportedNames, exportAggregator);
			globalContext.findContextToExportMapping(exportedNames, exportAggregator);
		}
	}
}
