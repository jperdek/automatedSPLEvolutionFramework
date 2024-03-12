package codeConstructsEvaluation;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import astFileProcessor.astObjects.ASTGenericDecorator;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


/**
 * Representation of the method complexity record
 * 
 * @author Jakub Perdek
 *
 */
public class MethodComplexityRecord extends ComplexityRecord implements EntityComplexityDifference {

	/**
	 * The nested methods associated with the method that is represented with this instance
	 */
	private Map<String, MethodComplexityRecord> nestedMethods;
	
	/**
	 * The name of the method
	 */
	private String methodName;
	
	/**
	 * The maximal depth of nested methods
	 */
	private int maxNestedMethodDepth;
	
	/**
	 * The names of method parameters
	 */
	private List<String> parameterNames;
	
	/**
	 * Optional association with the class
	 */
	private String associatedClassName = "Not class";

	
	/**
	 * Creates the code complexity record for method
	 * 
	 * @param methodName - the name of the method
	 * @param maxNestedMethodDepth - the maximal depth of nested methods
	 */
	public MethodComplexityRecord(String methodName, int maxNestedMethodDepth) {
		super(ComplexityRecord.SourceType.CLASS_METHOD);
		this.methodName = methodName;
		this.maxNestedMethodDepth = maxNestedMethodDepth;
		this.parameterNames = new ArrayList<String>();
		this.nestedMethods = new HashMap<String, MethodComplexityRecord>();
	}
	
	/**
	 * Sets associated class name, if method belongs to the class
	 * 
	 * @param associatedClassName - the ssociated class name, if method belongs to the class otherwise there should be "Not class"
	 */
	public void setAssociatedClassName(String associatedClassName) { this.associatedClassName = associatedClassName; }
	
	/**
	 * Returns the associated class name, if method belongs to the class otherwise "Not class"
	 * 
	 * @return the associated class name, if method belongs to the class otherwise "Not class"
	 */
	public String getAssociatedClassName() { return this.associatedClassName; }
	
	/**
	 * Returns the maximal depth of nested methods
	 * 
	 * @return the maximal depth of nested methods
	 */
	public int getMaxNestedMethodDepth() { return this.maxNestedMethodDepth; }
	
	/**
	 * Inserts the parameter name to the list of method parameters
	 * 
	 * @param parameterName - the parameter name that is going to be added to the list of method parameters
	 */
	public void addParameterName(String parameterName) { this.parameterNames.add(parameterName); }
	
	/**
	 * Checks if method has given parameter
	 * 
	 * @param parameterName - the observed name across method parameters
	 * @return true if method has given parameter otherwise false
	 */
	public boolean hasParameter(String parameterName) { return this.parameterNames.contains(parameterName); }
	
	/**
	 * Returns the array of method parameter names
	 * 
	 * @return the array of method parameter names
	 */
	public String[] getParameterNames() { return this.parameterNames.toArray(new String[0]); }

	/**
	 * Inserts/assigns the nested method according its unique name under nested methods of method that is represented by this instance
	 * 
	 * @param nestedMethodName - the unique name of nested method 
	 * @param nestedmethodComplexityRecord - the representation of evaluated method code complexity record
	 */
	public void putNestedMethod(String nestedMethodName, MethodComplexityRecord nestedmethodComplexityRecord) {
		this.nestedMethods.put(nestedMethodName, nestedmethodComplexityRecord);
	}
	
	/**
	 * Returns the nested method code complexity record according to the name of nested method
	 * 
	 * @param nestedMethodName - the name of nested method
	 * @return the nested method code complexity record according to the name of nested method
	 */
	public MethodComplexityRecord getNestedRecord(String nestedMethodName) {
		return this.nestedMethods.get(nestedMethodName);
	}
	
	/**
	 * Returns the method name
	 * 
	 * @return the method name
	 */
	public String getMethodName() { return this.methodName; }
	
	/**
	 * Sets the method name
	 * 
	 * @param methodName the method name
	 */
	public void setMethodName(String methodName) { this.methodName = methodName; }
	
	/**
	 * Calculates differences between the complexity measurements of two methods and instantiates new method record to store differences
	 *  
	 * @param comparedWith - another method to compare this instance with
	 * @return new measurement as subtraction of each complexity measurement in the most cases
	 */
	private MethodComplexityRecord instantiateMethodDifference(MethodComplexityRecord comparedWith) {
		String methodName = this.methodName;
		if (!methodName.equals(comparedWith.getMethodName())) {
			methodName = methodName + " != " + comparedWith.getMethodName();
		}
		return new MethodComplexityRecord(methodName, this.maxNestedMethodDepth);
	}
	
	/**
	 * Associates decorators with the method complexity record
	 * 
	 * @param frameworkDecorators - decorators that should be associated with the method
	 */
	public void associateDecorators(List<ASTGenericDecorator> frameworkDecorators) {
		this.associatedDecorators = new ArrayList<ASTGenericDecorator>(frameworkDecorators);
		for (ComplexityRecord complexityRecord: this.nestedMethods.values()) {
			complexityRecord.associateDecorators(this.associatedDecorators);
		}
	}
	
	@Override
	/**
	 * Calculates differences between the complexity measurements of two method complexity records
	 * 
	 * @param compareWith - another method complexity record to compare this instance with 
	 */
	public EntityComplexityDifference makeDifference(EntityComplexityDifference compareWith) {
		ComplexityRecord baseDifference = (ComplexityRecord) super.makeDifference(compareWith);
		MethodComplexityRecord compareWithMethodComplexityRecord = (MethodComplexityRecord) compareWith;
		MethodComplexityRecord complexityMethodRecordDifference = this.instantiateMethodDifference(
				compareWithMethodComplexityRecord);
		complexityMethodRecordDifference.setAssociatedClassName(this.associatedClassName);
		complexityMethodRecordDifference.updateAccordingTo(baseDifference);

		if (this.associatedDecorators != null) {
			complexityMethodRecordDifference.associateDecorators(this.associatedDecorators);
		}
	
		String recordMethodName;
		MethodComplexityRecord recordMethodDifference, 
		baseComplexityMethodRecord, comparedWithComplexityMethodRecord;
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.nestedMethods.entrySet()) {
			recordMethodName = complexityMethodRecord.getKey();
			baseComplexityMethodRecord = complexityMethodRecord.getValue();
			comparedWithComplexityMethodRecord = compareWithMethodComplexityRecord.getNestedRecord(recordMethodName);
			if (comparedWithComplexityMethodRecord == null) {
				complexityMethodRecordDifference.putNestedMethod(recordMethodName, baseComplexityMethodRecord);
			} else {
				recordMethodDifference = (MethodComplexityRecord) baseComplexityMethodRecord.makeDifference(
						comparedWithComplexityMethodRecord);
				complexityMethodRecordDifference.putNestedMethod(recordMethodName, recordMethodDifference);
			}	
		}
		
		for(String parameterName: this.parameterNames) {
			if (!this.hasParameter(parameterName)) {
				complexityMethodRecordDifference.addParameterName(parameterName);
			}
		}
		
		return complexityMethodRecordDifference;
	}

	@Override
	/**
	 * Prints the data stored in this method complexity record
	 */
	public void print() {	
		System.out.println("***| METHOD: " + this.methodName + " (max nested depth: " + this.maxNestedMethodDepth + ") |**************");
		super.print();
		String methodName;
		
		if (this.parameterNames.size() > 0) {
			System.out.print  ("***| Method Parameter Names |****");
			String identifier;
			Iterator<String> identifiersIterator = this.parameterNames.iterator();
			while(identifiersIterator.hasNext()) {
				identifier = identifiersIterator.next();
				System.out.print(identifier);
				if (identifiersIterator.hasNext()) {
					System.out.print(", ");
				} else {
					System.out.println();
				}
			}
		}
		
		System.out.println("***| Nested methods: (" + this.nestedMethods.size() + ")");
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.nestedMethods.entrySet()) {
			methodName = complexityMethodRecord.getKey();
			complexityMethodRecord.getValue().print();
			System.out.println("*** __METHOD_END__ " + methodName + " ___***");
		}
		
		System.out.println("************************************************");
	}
	
	@Override
	/**
	 * Put column names into complexity record
	 * -adds base columns to the complexity record
	 * 
	 * @param columnNames - the list of column names 
	 * @param aggregated - if true then column names will be added to complexity record otherwise not
	 */
	public void putColumnName(List<String> columnNames, boolean aggregated) {
		super.putColumnName(columnNames, aggregated);
		
		columnNames.add("Method Name");
		columnNames.add("Method Max Depth");
		columnNames.add("Method Number Nested Methods");
		columnNames.add("Method Parameter Names");
		columnNames.add("Associated Class Name");
		if (!aggregated) {
			for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.nestedMethods.entrySet()) {
				complexityMethodRecord.getValue().putColumnName(columnNames, aggregated);
			}
		}
	}
	
	@Override
	/**
	 * Put column names into CSV
	 * -adds base columns to the CSV
	 * 
	 * @param content - the list of column names 
	 * @param aggregated - if true then column names will be added to complexity record otherwise not
	 */
	public void writeToCSV(StringBuilder content, boolean aggregated) {
		super.writeToCSV(content, aggregated);
		content.append(this.methodName);
		content.append(';');
		content.append(String.valueOf(this.maxNestedMethodDepth).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.nestedMethods.size()).replace(".", ","));
		content.append(';');
		content.append(String.join(",", this.parameterNames.toArray(new String[0])));
		content.append(';');
		content.append(this.associatedClassName);
		content.append(';');
		if (!aggregated) {
			for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.nestedMethods.entrySet()) {
				complexityMethodRecord.getValue().writeToCSV(content, aggregated);
			}
		}
	}
	
	@Override
	/**
	 * Returns the complexity record entry set
	 * 
	 * @return the complexity record entry set
	 */
	public Set<Entry<String, ? extends ComplexityRecord>> getComplexityRecordsEntrySet() {
		Set<Entry<String, ? extends ComplexityRecord>> usedSet = new HashSet<Entry<String, ? extends ComplexityRecord>>();
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.nestedMethods.entrySet()) {
			usedSet.add(complexityMethodRecord);
		}
		return usedSet;
	}
}
