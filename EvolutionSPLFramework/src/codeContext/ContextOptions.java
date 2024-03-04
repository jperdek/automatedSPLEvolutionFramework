package codeContext;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;


/**
 * Representation of context options to customize/streamline search under contexts and harvest executable callable code fragments
 * 
 * @author Jakub Perdek
 *
 */
public class ContextOptions {
	
	/**
	 * Information if actual instance is found during the search
	 */
	private boolean foundActualInstance;
	
	/**
	 * The list of executable forms/callable code fragments (in original form)
	 */
	private List<String> executableForms;
	

	/**
	 * Creates the context option instance to customize/streamline search under contexts
	 */
	public ContextOptions() {
		this.foundActualInstance = false;
		this.executableForms = new ArrayList<String>();
	}
	
	/**
	 * Initialized context option to start search for given function context
	 * 
	 * @param actualContext - the actual context from which the search begins
	 * @param functionContext - the target context that should be found during the search and all intermediade calls harvested
	 */
	public ContextOptions(InnerContext actualContext, FunctionContext functionContext) {
		if (actualContext == functionContext) {this.foundActualInstance = true;} else {this.foundActualInstance = false;}
		this.executableForms = new ArrayList<String>();
		this.executableForms.add(functionContext.constructCallableForm());
	}
	
	/**
	 * Extends and add executable form from inner code construct and replaces previous executable form carried in this object
	 * -base form is merged with previous executable form
	 * -the extension operator (usually/in JavaScript, Java it is ".") is used to merge base form with previous executable form
	 * -example: "new A().callMe()"
	 * -the constructor is omitted
	 * 
	 * @param baseForm - the base form to call inner objects such as class methods under instantiated class
	 * @param extensionOperator - used to merge base form with previous executable form (usually/in JavaScript, Java it is ".")
	 * @param executableForm - the executable form from previous code context that should be extended
	 */
	public void extendAndAddExecutableForm(String baseForm, String extensionOperator, String executableForm) {
		if (baseForm == null) {baseForm = "";}
		String newForm = baseForm + extensionOperator + executableForm;
		this.executableForms.add(newForm);
	}
	
	/**
	 * Merges base context form with this one
	 * -all executable forms/callable code fragments (in original form) from base context are added to this context
	 * -if contexts are found (foundActualInstance of base context option instance is true) then foundActualInstance is set to true
	 * 		=> the search can stop then
	 * 
	 * @param contextOptionsToBeMerged - the base context form that is going to be merged with this one
	 */
	public void mergeBase(ContextOptions contextOptionsToBeMerged) {
		if (this.foundActualInstance == false) {
			this.foundActualInstance = contextOptionsToBeMerged.isFound();
		}
		this.executableForms.addAll(contextOptionsToBeMerged.getExecutableForms());
	}
	
	/**
	 * Exports all executable forms/callable code constructs (in original form) from the context options as JSON array
	 * 
	 * @return all executable forms/callable code constructs (in original form) exported from the context options exported as JSON array
	 */
	public JSONArray exportAsJSONArray() {
		JSONArray exportedArray = new JSONArray();
		for (String executableForm: this.executableForms) {
			exportedArray.add(executableForm);
		}
		return exportedArray;
	}
	
	/**
	 * Returns the list of executable forms/code constructs (in original form)
	 * 
	 * @return the list of executable forms/code constructs (in original form)
	 */
	public List<String> getExecutableForms() { return this.executableForms; }
	
	/**
	 * Inserts executable form/callable code construct (in original form) into associated callable constructs with this options
	 * 
	 * @param executableForm - the executable form/callable construct (in original form) that is going to be inserted into associated callable constructs with this options
	 */
	public void addExecutableForm(String executableForm) { this.executableForms.add(executableForm); }
	
	/**
	 * Extends and add executable forms from inner code construct and replaces previous executable forms carried in this object
	 * -base form is merged with previous executable form
	 * -the extension operator (usually/in JavaScript, Java it is ".") is used to merge base form with previous executable form
	 * -example: "new A().callMe()"
	 * -the constructor is omitted
	 * 
	 * @param baseForm - the base form to call inner objects such as class methods under instantiated class
	 * @param extensionOperator - used to merge base form with previous executable form (usually/in JavaScript, Java it is ".")
	 * @param executableForm - the list of executable forms from previous code context that should be extended
	 */
	public void extendAndAddExecutableFormAllReplacement(String baseForm, String extensionOperator, List<String> executableForm) {
		if (baseForm == null) {baseForm = "";}
		List<String> newExecutableForms = new ArrayList<String>();
		String newForm;
		
		for (String previousExecutableForm: executableForm) {
			newForm = baseForm + extensionOperator + previousExecutableForm;
			if (previousExecutableForm.startsWith("constructor(")) { continue; }
			newExecutableForms.add(newForm);
		}
		this.executableForms = newExecutableForms;
	}
	
	/**
	 * Extends and add executable forms from inner code construct
	 * -base form is merged with previous executable form
	 * -the extension operator (usually/in JavaScript, Java it is ".") is used to merge base form with previous executable form
	 * -example: "new A().callMe()"
	 * -the constructor is omitted
	 * 
	 * @param baseForm - the base form to call inner objects such as class methods under instantiated class
	 * @param extensionOperator - used to merge base form with previous executable form (usually/in JavaScript, Java it is ".")
	 * @param executableForm - the list of executable forms from previous code context that should be extended
	 */
	public void extendAndAddExecutableFormAll(String baseForm, String extensionOperator, List<String> executableForm) {
		if (baseForm == null) {baseForm = "";}
		String newForm;
		
		for (String previousExecutableForm: executableForm) {
			newForm = baseForm + extensionOperator + previousExecutableForm;
			if (previousExecutableForm.startsWith("constructor(")) { continue; }
			this.executableForms.add(newForm);
		}
	}
	
	/**
	 * Extends and add executable forms according to inner code construct form from this instance (this.executableForms)
	 *  and replaces previous executable forms carried in this object
	 * -base form is merged with previous executable form
	 * -the extension operator (usually/in JavaScript, Java it is ".") is used to merge base form with previous executable form
	 * -example: "new A().callMe()"
	 * -the constructor is omitted
	 * 
	 * @param previousBaseForm - the previously used base form to call inner objects such as class methods under instantiated class
	 * @param extensionOperator - used to merge base form with previous executable form (usually/in JavaScript, Java it is ".")
	 */
	public void extendAccordingPreviousForm(String previousBaseForm, String extensionOperator) {
		if (previousBaseForm == null) {previousBaseForm = "";}
		List<String> newExecutableForms = new ArrayList<String>();
		String newForm;

		for (String previousExecutableForm: this.executableForms) {
			newForm = previousBaseForm + extensionOperator + previousExecutableForm;
			if (previousExecutableForm.startsWith("constructor(")) { continue; }
			newExecutableForms.add(newForm);
		}
		this.executableForms = newExecutableForms;
	}
	
	/**
	 * Returns the information if the requested context is found
	 * 
	 * @return true if the code context is found otherwise false
	 */
	public boolean isFound() { return this.foundActualInstance; }
	
	/**
	 * Sets the code context as found - the search as prepared to terminate
	 */
	public void setAsFound() { this.foundActualInstance = true; }
}
