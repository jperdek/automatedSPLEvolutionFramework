package codeContext.objects;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import codeContext.InnerContext;
import codeContext.processors.export.ExportAggregator;
import codeContext.processors.export.ExportedContext;
import codeContext.processors.export.ExportedContextInterface;
import codeContext.processors.export.ExportedInterface;
import codeContext.processors.export.ExportedObjectInterface;


/**
 * Representation of code variable
 * 
 * @author Jakub Perdek
 *
 */
public class VariableObject extends CodeContextObject implements Comparable<String>, ExportedContextInterface, ExportedObjectInterface, ExportedInterface {
	
	/**
	 * The name of the variable
	 */
	private String variableName;
	
	/**
	 * The type of variable
	 */
	private String variableType;
	
	/**
	 * Informing if variable is declared as global
	 * - true if variable is declared as global otherwise false
	 */
	private boolean isGlobal;
	
	/**
	 * Informing if variable is exported
	 * -true if variable is declared as global otherwise false
	 */
	private boolean isExported;
	
	
	/**
	 * Creates representation of variable from the code
	 * 
	 * @param variableName - the name of the variable
	 * @param variableType - The type of variable
	 * @param position - the position in the AST whre variable is declared/used
	 * @param isGlobal - informing if variable is declared as global - true if variable is declared as global otherwise false
	 * @param isExported - informing if variable is exported - true if variable is exported otherwise false
	 */
	public VariableObject(String variableName, String variableType, long position, boolean isGlobal, boolean isExported) {
		super(position);
		this.variableName = variableName;
		this.variableType = variableType;
		this.isGlobal = isGlobal;
		this.isExported = isExported;
	}
	
	/**
	 * Returns the name of the variable
	 * 
	 * @return the name of the variable 
	 */
	public String getVariableName() { return this.variableName; }

	/**
	 * Returns the type of the variable
	 * 
	 * @return the type of the variable
	 */
	public String getVariableType() { return this.variableType; }

	/**
	 * Returns information if variable is exported - true if variable is exported otherwise false
	 * 
	 * @return information if variable is exported - true if variable is exported otherwise false
	 */
	public boolean isExported() { return isExported; }
	
	/**
	 * Returns information if variable is declared as global - true if variable is declared as global otherwise false
	 * 
	 * @return information if variable is declared as global - true if variable is declared as global otherwise false
	 */
	public boolean isGlobal() { return this.isGlobal; }

	/**
	 * This representation/code variable is marked as exported
	 */
	public void setAsExported() { this.isExported=true; }

	@Override
	/**
	 * Compares variable name and compared variable name provided in function argument
	 * 
	 * @param variableName - the compared variable name
	 * @return 1 if variable name and compared variable name match otherwise 0
	 */
	public int compareTo(String variableName) {
		if (this.variableName.equals(variableName)) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * Returns string representation of the variable (type is optionally used)
	 *  
	 * @param useTypes - if true then it allows to specify types in the output otherwise not
	 * @return string representation of the code variable (type is optionally used)
	 */
	public String getStringRepresentation(boolean useTypes) {
		if (useTypes && this.variableType != null) {
			return this.variableName + ": " + this.variableType;
		} 
		return this.getVariableName();
	}
	
	/**
	 * Returns and creates the descriptive JSON represented output from information about code variable
	 * 
	 * @return the descriptive JSON represented output from information about code variable
	 */
	public JSONObject createDescriptiveJSON() {
		JSONObject descriptiveVariable = new JSONObject();
		if (variableName == null) {
			return null;
		}
		descriptiveVariable.put("name", variableName);
		if (variableType != null) {
			descriptiveVariable.put("type", variableType);
		}
		return descriptiveVariable;
	}

	@Override
	public void findContextToExportMapping(List<String> exportNames, ExportAggregator exportAggregator) {
		List<String> exportNamesToRemove = new ArrayList<String>();
		for (String exportName: exportNames) {
			if (this.variableName.equals(exportName)) {
				exportAggregator.associateExport(this.variableName, this);
				exportNamesToRemove.add(exportName);	
			}
		}
		for (String exportName: exportNamesToRemove) {
			exportNames.remove(exportName);
		}
	}

	@Override
	public ExportedObjectInterface findDefaultExport(Long initialPosition, Long terminatingPosition) {
		if (terminatingPosition == this.position) { return this; }
		return null;
	}

	@Override
	public void markDirrectExportMapping(ExportAggregator exportAggregator, String fileName, InnerContext baseInnerContext) {
		if (this.isExported) {
			exportAggregator.associateExport(this.variableName, this);
			exportAggregator.associateExport(this.variableName, new ExportedContext(this, fileName, baseInnerContext));
		}
	}

	@Override
	public String getExportName() { return this.variableName; }
	
	@Override
	public String getExportType() { return this.variableType; }

	@Override
	public String constructCallableForm() { return this.variableName; }
	
	@Override
	public JSONArray constructAllAvailableCallsUnderType() {
		return this.constructAllAvailableCallsUnderType("");
	}
	
	@Override
	public JSONArray constructAllAvailableCallsUnderType(String initialVariableName) {
		return null; //all available calls should be constructed according to type
	}

	@Override
	public ExportedObjectInterface getExtendableInnerObjectAccordingToType(String innerObjectType) {
		return null;
	}

	@Override
	public String getCallableStr() { return this.createDescriptiveJSON().toJSONString(); }

	@Override
	public String getIdentificationAST() { return this.createDescriptiveJSON().toJSONString(); }
}
