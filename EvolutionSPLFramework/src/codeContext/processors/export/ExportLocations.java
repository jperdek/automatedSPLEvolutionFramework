package codeContext.processors.export;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codeConstructsEvaluation.transformation.ASTConverterClient;
import splEvolutionCore.DebugInformation;


/**
 * The collection of exported objects from one particular file location (name)
 * -more export locations can be stored inside under one file location (name)
 * 
 * @author Jakub Perdek
 *
 */
public class ExportLocations extends ExportLocation {
	
	/**
	 * Logger to track information from export locations
	 */
	private static final Logger logger = LoggerFactory.getLogger(ExportLocations.class);
	
	/**
	 * The set of exported constructs from one particular file or location
	 */
	private Set<String> constructNames;
	
	/**
	 * Instantiates the export location from already existing export location
	 * -all information will be propagated also to this object
	 * 
	 * @param exportLocation - already existing export location
	 */
	public ExportLocations(ExportLocation exportLocation) {
		super(exportLocation);
		
		if (this.fileName.equals(exportLocation.getFileName()) && this.importPath.equals(exportLocation.getImportPath())) {
			
		}
		if ((this.importPath == null || this.importPath.equals("")) && exportLocation.getFileName().replace("\\", "/").indexOf('/') > -1) {
			this.importPath = exportLocation.getFileName();
		}
		this.constructNames = new HashSet<String>();
		this.constructNames.add(exportLocation.getConstructName());	
	}
	
	/**
	 * Adds export location into the export locations
	 * -the export name (path) and import path must match 
	 * 
	 * @param exportLocation - the export location that is going to be merged
	 * @return true if the export location is merged otherwise false
	 */
	public boolean addLocation(ExportLocation exportLocation) {
		if (this.fileName.equals(exportLocation.getFileName()) && this.importPath.equals(exportLocation.getImportPath())) {
			this.constructNames.add(exportLocation.getConstructName());
			return true;
		} else if (this.fileName.equals(exportLocation.getFileName())) { // in case of missing imports during addition
			this.constructNames.add(exportLocation.getConstructName());
			return true;
		}
		return false;
	}
	
	/**
	 * Creates string containing the import line of code (in TypeScript)
	 * 
	 * @return the string containing the import line of code (in TypeScript)
	 */
	public String createImportStr() {
		String importStr = "import {";
		for (String constructName: this.constructNames) {
			if (importStr.endsWith("{")) {
				importStr = importStr + constructName;
			} else {
				importStr = importStr + ", " + constructName;
			}
		}
		return importStr + "} from '" + this.importPath + "';";
	}
	
	/**
	 * Creates import statement from all exported locations and converts it into AST statement
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public JSONObject createImportToAST() throws IOException, InterruptedException {
		String importStr = this.createImportStr();
		if(DebugInformation.SHOW_POLLUTING_INFORMATION) { logger.debug("Created import: " + importStr); }
		
		return ASTConverterClient.getFirstStatementFromASTFile(ASTConverterClient.convertFromCodeToASTJSON(importStr));
	}
	
	/**
	 * Checks if exported construct/object is inside/belongs to used/imported constructs/objects
	 *  
	 * @param constructName - the name of the construct that should be checked if is inside/belongs imported constructs/objects
	 * @return true if exported construct/object is inside/belongs to used/imported constructs/objects otherwise false
	 */
	public boolean hasName(String constructName) { return this.constructNames.contains(constructName); }
	
	/**
	 * Returns the set containing the name of each exported construct
	 * 
	 * @return the set containing the name of each exported construct
	 */
	public Set<String> getExportedConstructNames() { return this.constructNames; }
}
