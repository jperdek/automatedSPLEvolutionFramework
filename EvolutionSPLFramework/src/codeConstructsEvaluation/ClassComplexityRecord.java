package codeConstructsEvaluation;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import astFileProcessor.astObjects.ASTGenericDecorator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


/**
 * Representation of the class complexity record
 * 
 * @author Jakub Perdek
 *
 */
public class ClassComplexityRecord extends ComplexityRecord implements EntityComplexityDifference  {
	/**
	 * Associated complexity records
	 */
	private Map<String, ComplexityRecord> associatedRecords;
	
	/**
	 * Associated class methods
	 */
	private Map<String, MethodComplexityRecord> classMethods;
	
	/**
	 * Evaluated class maintainability
	 */
	private double maintainability;
	
	/**
	 * Name of the class
	 */
	private String className;
	
	
	/**
	 * Creation of class complexity record
	 * 
	 * @param className - the name of class
	 * @param maintainability - measured maintainability
	 */
	public ClassComplexityRecord(String className, double maintainability) {
		super(ComplexityRecord.SourceType.CLASS);
		this.associatedRecords = new HashMap<String, ComplexityRecord>();
		this.classMethods = new HashMap<String, MethodComplexityRecord>();
		this.className = className;
		this.maintainability = maintainability;
	}
	
	/**
	 * Returns the class name
	 * 
	 * @return the name of the class
	 */
	public String getClassName() { return this.className; }
	
	/**
	 * Returns the measured maintainability
	 * 
	 * @return the measured maintainability
	 */
	public double getMaintainability() { return this.maintainability; }
	
	/**
	 * Inserts/assigns method with the class
	 * 
	 * @param methodName - the name of the method
	 * @param methodComplexityRecord - the complexity record that is associated to the method
	 */
	public void putClassMethod(String methodName, MethodComplexityRecord methodComplexityRecord) {
		this.classMethods.put(methodName, methodComplexityRecord);
	}
	
	/**
	 * Obtains class method according to the name
	 * 
	 * @param methodName - the name of the class method
	 * @return the class method with the name provided as function argument
	 */
	public MethodComplexityRecord getMethodComplexityRecord(String methodName) {
		return this.classMethods.get(methodName);
	}
	
	/**
	 * Inserts complexity measurement record with the class under unique identifier
	 * 
	 * @param recordIdentifier - the unique identifier of the record
	 * @param complexityRecord - the complexity measurement that is going to be associated with the class
	 */
	public void putAssociatedRecord(String recordIdentifier, ComplexityRecord complexityRecord) {
		this.associatedRecords.put(recordIdentifier, complexityRecord);
	}
	
	/**
	 * Obtains associated record according to the recordIdentifier string
	 * 
	 * @param recordIdentifier - unique identifier of the record (complexity measurement)
	 * @return the associated complexity record with the class stored under recordIdentifier string that is provided as parameter 
	 */
	public ComplexityRecord getAssociatedRecord(String recordIdentifier) {
		return this.associatedRecords.get(recordIdentifier);
	}
	
	/**
	 * Calculates differences between the complexity measurements of two classes and instantiates new class record to store differences
	 *  
	 * @param compareWith - another class to compare this instance with
	 * @return new measurement as subtraction of each complexity measurement in the most cases
	 */
	private ClassComplexityRecord instantiateClassDifference(ClassComplexityRecord compareWith) {
		String className = this.className;
		if (!className.equals(compareWith.getClassName())) {
			className = className + " != " + compareWith.getClassName();
		}
		double maintainabilityDifference = this.maintainability - compareWith.getMaintainability();
		return new ClassComplexityRecord(className, maintainabilityDifference);
	}
	
	/**
	 * Associates decorators with the complexity record
	 * 
	 * @param frameworkDecorators - decorators that should be associated with the class
	 */
	public void associateDecorators(List<ASTGenericDecorator> frameworkDecorators) {
		this.associatedDecorators = new ArrayList<ASTGenericDecorator>(frameworkDecorators);
		for (ComplexityRecord complexityRecord: this.classMethods.values()) {
			complexityRecord.associateDecorators(this.associatedDecorators);
		}
		for (ComplexityRecord complexityRecord: this.associatedRecords.values()) {
			complexityRecord.associateDecorators(this.associatedDecorators);
		}
	}
	
	@Override
	/**
	 * Calculates differences between the complexity measurements of two classes
	 * 
	 * @param compareWith - another class to compare this instance with 
	 */
	public EntityComplexityDifference makeDifference(EntityComplexityDifference compareWith) {
		ComplexityRecord baseDifference = (ComplexityRecord) super.makeDifference(compareWith);
		ClassComplexityRecord compareWithClassComplexityRecord = (ClassComplexityRecord) compareWith;
		ClassComplexityRecord complexityClassRecordDifference = this.instantiateClassDifference(
				compareWithClassComplexityRecord);
		complexityClassRecordDifference.updateAccordingTo(baseDifference);

		if (this.associatedDecorators != null) {
			complexityClassRecordDifference.associateDecorators(this.associatedDecorators);
		}
	
		String recordMethodName;
		MethodComplexityRecord recordMethodDifference, 
		baseComplexityMethodRecord, comparedWithComplexityMethodRecord;
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.classMethods.entrySet()) {
			recordMethodName = complexityMethodRecord.getKey();
			baseComplexityMethodRecord = complexityMethodRecord.getValue();
			comparedWithComplexityMethodRecord = compareWithClassComplexityRecord.getMethodComplexityRecord(recordMethodName);
			if (comparedWithComplexityMethodRecord == null) {
				complexityClassRecordDifference.putClassMethod(recordMethodName, baseComplexityMethodRecord);
			} else {
				recordMethodDifference = (MethodComplexityRecord) baseComplexityMethodRecord.makeDifference(
						comparedWithComplexityMethodRecord);
				complexityClassRecordDifference.putClassMethod(recordMethodName, recordMethodDifference);
			}	
		}
		
		ComplexityRecord recordDifference,
		baseComplexityAssociatedRecord, comparedWithComplexityAssociatedRecord;
		for(Entry<String, ComplexityRecord> complexityAssociatedRecord: this.associatedRecords.entrySet()) {
			recordMethodName = complexityAssociatedRecord.getKey();
			baseComplexityAssociatedRecord = complexityAssociatedRecord.getValue();
			comparedWithComplexityAssociatedRecord = compareWithClassComplexityRecord.getAssociatedRecord(recordMethodName);
			if (comparedWithComplexityAssociatedRecord == null) {
				complexityClassRecordDifference.putAssociatedRecord(recordMethodName, baseComplexityAssociatedRecord);
			} else {
				recordDifference = (ComplexityRecord) baseComplexityAssociatedRecord.makeDifference(
						comparedWithComplexityAssociatedRecord);
				complexityClassRecordDifference.putAssociatedRecord(recordMethodName, recordDifference);
			}	
		}
		
		return complexityClassRecordDifference;
	}
	
	/**
	 * Prints output from information that is stored inside class
	 */
	@Override
	public void print() {	
		System.out.println("+++| CLASS: " + this.className + " (maintainability: " + this.maintainability + ")");
		super.print();
		String methodName, recordName;
		
		System.out.println("+++| File methods: (" + this.classMethods.size() + ")");
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.classMethods.entrySet()) {
			methodName = complexityMethodRecord.getKey();
			complexityMethodRecord.getValue().print();
			System.out.println("+++ __METHOD_END__ " + methodName + " ___+++");
		}

		System.out.println("+++| Associated record: (" + this.associatedRecords.size() + ")");
		for(Entry<String, ComplexityRecord> complexityAssociatedRecord: this.associatedRecords.entrySet()) {
			recordName = complexityAssociatedRecord.getKey();
			System.out.println("+++| Associated record: (" + recordName + ")");
			complexityAssociatedRecord.getValue().print();
		}

		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
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
		columnNames.add("Class Name");
		columnNames.add("Class Maintainability");
		columnNames.add("Class number methods");
		
		if (!aggregated) {
			for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.classMethods.entrySet()) {
				complexityMethodRecord.getValue().putColumnName(columnNames, aggregated);
			}
	
			for(Entry<String, ComplexityRecord> complexityAssociatedRecord: this.associatedRecords.entrySet()) {
				complexityAssociatedRecord.getValue().putColumnName(columnNames, aggregated);
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
		if (aggregated) { // WRITES AGGREGATED RESULTS FOR THE CLASS (for class methods, aggregatedAverage remains
			if (this.associatedRecords.size() >= 1 && this.associatedRecords.containsKey("AGGREGATE_CLASS")) {
				this.associatedRecords.get("AGGREGATE_CLASS").writeToCSV(content, aggregated);
			}
		} else {
			super.writeToCSV(content, aggregated);
		}
		content.append(this.className);
		content.append(';');
		content.append(String.valueOf(this.maintainability).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.classMethods.size()).replace(".", ","));
		content.append(';');
		
		if (!aggregated) {
			for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.classMethods.entrySet()) {
				complexityMethodRecord.getValue().writeToCSV(content, aggregated);
			}
	
			for(Entry<String, ComplexityRecord> complexityAssociatedRecord: this.associatedRecords.entrySet()) {
				complexityAssociatedRecord.getValue().writeToCSV(content, aggregated);
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
		for(Entry<String, ComplexityRecord> complexityRecord: this.associatedRecords.entrySet()) {
			usedSet.add(complexityRecord);
		}
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.classMethods.entrySet()) {
			usedSet.add(complexityMethodRecord);
		}
		return usedSet;
	}
}
