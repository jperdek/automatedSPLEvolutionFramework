package codeConstructsEvaluation;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import astFileProcessor.astObjects.ASTGenericDecorator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import codeConstructsEvaluation.entities.FileDependency;


/**
 * Representation of the file complexity record
 * 
 * @author Jakub Perdek
 *
 */
public class FileComplexityRecord extends ComplexityRecord implements EntityComplexityDifference  {
	
	/**
	 * Evaluated method code complexities that are associated with file represented with this entity
	 */
	private Map<String, MethodComplexityRecord> methodsMap; //also functions in JavaScript
	
	/**
	 * Evaluated class code complexities that are associated with file represented with this entity
	 */
	private Map<String, ClassComplexityRecord> classesMap;
	
	/**
	 * Associated imports with file represented with this entity
	 */
	private Map<String, FileDependency> imports;
	
	/**
	 * Associated the name of file
	 */
	private String fileName;

	/**
	 * The time marks of analyzed file content after certain change is taken
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum AnalyzedFileContentType { AFTER, BEFORE, STANDALONE };
	
	/**
	 * The analyzed content stored as string, if not set then null
	 */
	private String analyzedFileContent = null;
	
	/**
	 * The analyzed content that is analyzed before certain operation is taken and is stored as string, if not set then null
	 */
	private String analyzedFileContentBefore = null;
	
	/**
	 * The analyzed content that is analyzed after certain operation is taken and is stored as string, if not set then null
	 */
	private String analyzedFileContentAfter = null;
	

	/**
	 * Creates code complexity record of the file
	 * 
	 * @param fileName - the name of analyzed file
	 */
	public FileComplexityRecord(String fileName) {
		super(ComplexityRecord.SourceType.FILE);
		this.fileName = fileName;
		this.methodsMap = new HashMap<String, MethodComplexityRecord>();
		this.classesMap = new HashMap<String, ClassComplexityRecord>();
		this.imports = new HashMap<String, FileDependency>();
	}
	
	/**
	 * Inserts analyzed content according to the provided time mark
	 * 
	 * @param analyzedFileContent - the analyzed content according to the provided time mark
	 * @param contentType - the time mark of an analyzed content
	 */
	public void putAnalyzedFileContent(String analyzedFileContent, AnalyzedFileContentType contentType) {
		this.analyzedFileContent = analyzedFileContent;
		if (contentType == AnalyzedFileContentType.AFTER) {
			this.analyzedFileContentAfter = analyzedFileContent;
		} else if (contentType == AnalyzedFileContentType.BEFORE) {
			this.analyzedFileContentBefore = analyzedFileContent;
		}
	}
	
	/**
	 * Returns the name of analyzed file
	 * 
	 * @return the name of analyzed file
	 */
	public String getFileName() { return this.fileName; }
	
	/**
	 * Returns information if analyzed content is comparatory instance - at least one from time marks should be set 
	 * 
	 * @return true if the instance serves for the comparatory analysis otherwise false
	 */
	public boolean isComparatoryInstance() { return this.analyzedFileContentBefore != null || this.analyzedFileContentAfter != null; }

	/**
	 * Returns the base (STANDALONE) analyzed file content
	 * 
	 * @return the base (STANDALONE) analyzed file content
	 */
	public String getAnalyzedFileContent() { return this.analyzedFileContent; }
	
	/**
	 * Returns the analyzed file content
	 * 
	 * @param contentType - the time mark of the analyzed content that should be returned
	 * @return the analyzed file content according to provided contentType argument to this function
	 */
	public String getAnalyzedFileContent(AnalyzedFileContentType contentType) {
		if (contentType == AnalyzedFileContentType.AFTER) {
			return this.analyzedFileContentAfter;
		} else if (contentType == AnalyzedFileContentType.BEFORE) {
			return this.analyzedFileContentBefore;
		}
		return this.analyzedFileContent; 
	}
	
	/**
	 * Associates/inserts method complexity record according to its unique name to all file method complexity records
	 * 
	 * @param methodName - the unique name of the method
	 * @param methodComplexityRecord - the representation of evaluated code complexity for method
	 */
	public void putAssociatedMethod(String methodName, MethodComplexityRecord methodComplexityRecord) {
		this.methodsMap.put(methodName, methodComplexityRecord);
	}
	
	/**
	 * Returns the associated method with its representation of code complexity record
	 * 
	 * @param methodName - the unique name of the method that is used to find the method code complexity record
	 * @return the associated method with its representation of code complexity record
	 */
	public MethodComplexityRecord getAssociatedMethod(String methodName) {
		return this.methodsMap.get(methodName);
	}
	
	/**
	 * Returns iterator to iterate through method code complexity records
	 * 
	 * @return the iterator to iterate through method code complexity records
	 */
	public Iterator<MethodComplexityRecord> getMethodsRecordsComplexityIterator() {
		return this.methodsMap.values().iterator();
	}
	
	/**
	 * Associates/inserts class complexity record according to its unique name to all file class complexity records
	 * 
	 * @param className - the unique name of the class
	 * @param classComplexityRecord - the representation of evaluated code complexity for class
	 */
	public void putAssociatedClass(String className, ClassComplexityRecord classComplexityRecord) {
		this.classesMap.put(className, classComplexityRecord);
	}
	
	/**
	 * Returns the associated class with its representation of code complexity record
	 * 
	 * @param className - the unique name of the class that is used to find the class code complexity record
	 * @return the associated class with its representation of code complexity record
	 */
	public ClassComplexityRecord getAssociatedClass(String className) {
		return this.classesMap.get(className);
	}
	
	/**
	 * Returns iterator to iterate through class code complexity records
	 * 
	 * @return the iterator to iterate through class code complexity records
	 */
	public Iterator<ClassComplexityRecord> getClassRecordsComplexityIterator() {
		return this.classesMap.values().iterator();
	}
	
	/**
	 * Adds or replaces previous import with the new one
	 * 
	 * @param path - the used path to referenced source files
	 * @param type - the type of import
	 * @param importOccurence - the ocurrence of the import
	 */
	public void addOrUpdateImport(String path, String type, int importOccurence) {
		FileDependency fileDependency = new FileDependency(path, type, importOccurence);
		this.imports.put(path, fileDependency);
	}
	
	/**
	 * Adds or replaces previous import with the new one
	 * 
	 * @param path - the used path to referenced source files
	 * @param type - the type of import
	 */
	public void addOrUpdateImport(String path, String type) {
		FileDependency fileDependency = new FileDependency(path, type);
		this.imports.put(path, fileDependency);
	}
	
	/**
	 * Associates decorators with the file complexity record
	 * 
	 * @param frameworkDecorators - decorators that should be associated with the file
	 */
	public void associateDecorators(List<ASTGenericDecorator> frameworkDecorators) {
		this.associatedDecorators = new ArrayList<ASTGenericDecorator>(frameworkDecorators);
		for (ComplexityRecord complexityRecord: this.methodsMap.values()) {
			complexityRecord.associateDecorators(this.associatedDecorators);
		}
		for (ComplexityRecord complexityRecord: this.classesMap.values()) {
			complexityRecord.associateDecorators(this.associatedDecorators);
		}
	}
	
	@Override
	/**
	 * Calculates differences between the complexity measurements of two file complexity records
	 * 
	 * @param compareWith - another file complexity record to compare this instance with 
	 */
	public EntityComplexityDifference makeDifference(EntityComplexityDifference compareWith) {
		ComplexityRecord baseDifference = (ComplexityRecord) super.makeDifference(compareWith);
		FileComplexityRecord compareWithFileComplexityRecord = (FileComplexityRecord) compareWith;
		FileComplexityRecord complexityFileRecordDifference = new FileComplexityRecord(this.fileName);
		complexityFileRecordDifference.updateAccordingTo(baseDifference);
		
		if (this.associatedDecorators != null) {
			complexityFileRecordDifference.associateDecorators(associatedDecorators);
		}
		
		String recordMethodName;
		MethodComplexityRecord recordMethodDifference, 
		baseComplexityMethodRecord, comparedWithComplexityMethodRecord;
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.methodsMap.entrySet()) {
			recordMethodName = complexityMethodRecord.getKey();
			baseComplexityMethodRecord = complexityMethodRecord.getValue();
			comparedWithComplexityMethodRecord = compareWithFileComplexityRecord.getAssociatedMethod(recordMethodName);
			if (comparedWithComplexityMethodRecord == null) {
				complexityFileRecordDifference.putAssociatedMethod(recordMethodName, baseComplexityMethodRecord);
			} else {
				recordMethodDifference = (MethodComplexityRecord) baseComplexityMethodRecord.makeDifference(
						comparedWithComplexityMethodRecord);
				complexityFileRecordDifference.putAssociatedMethod(recordMethodName, recordMethodDifference);
			}	
		}
		
		String recordClassName;
		ClassComplexityRecord recordClassDifference, 
		baseComplexityClassRecord, comparedWithComplexityClassRecord;
		for(Entry<String, ClassComplexityRecord> complexityClassRecord: this.classesMap.entrySet()) {
			recordClassName = complexityClassRecord.getKey();
			baseComplexityClassRecord = complexityClassRecord.getValue();
			comparedWithComplexityClassRecord = compareWithFileComplexityRecord.getAssociatedClass(recordClassName);
			if (comparedWithComplexityClassRecord == null) {
				complexityFileRecordDifference.putAssociatedClass(recordClassName, baseComplexityClassRecord);
			} else {
				recordClassDifference = (ClassComplexityRecord) baseComplexityClassRecord.makeDifference(
						comparedWithComplexityClassRecord);
				complexityFileRecordDifference.putAssociatedClass(recordClassName, recordClassDifference);
			}	
		}

		complexityFileRecordDifference.putAnalyzedFileContent(this.analyzedFileContent, AnalyzedFileContentType.BEFORE);
		complexityFileRecordDifference.putAnalyzedFileContent(
					compareWithFileComplexityRecord.getAnalyzedFileContent(), AnalyzedFileContentType.AFTER);
		
		return complexityFileRecordDifference;
	}
	
	@Override
	/**
	 * Prints the data stored in this file complexity record
	 */
	public void print() {
		System.out.println();
		System.out.println("XXXXXXXX| FILE |XXXXXXX");
		super.print();
		String methodName, className;
		System.out.println("XXX| File methods: (" + this.methodsMap.size() + ")");
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.methodsMap.entrySet()) {
			methodName = complexityMethodRecord.getKey();
			complexityMethodRecord.getValue().print();
			System.out.println("XXX __METHOD_END__ " + methodName + " ___XXXX");
		}

		System.out.println("XXX| File classes: (" + this.classesMap.size() + ")");
		for(Entry<String, ClassComplexityRecord> complexityClassRecord: this.classesMap.entrySet()) {
			className = complexityClassRecord.getKey();
			complexityClassRecord.getValue().print();
			System.out.println("XXX __CLASS_END___ " + className + " ___XXXX");
		}
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		System.out.println();
		System.out.println();
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
		
		//columnNames.add(suffixName + " File name");
		columnNames.add("File name");
		columnNames.add("File methods");
		columnNames.add("File classes");
		columnNames.add("File framework decorators");
		
		if (!aggregated) {
			for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.methodsMap.entrySet()) {
				//methodName = complexityMethodRecord.getKey();
				complexityMethodRecord.getValue().putColumnName(columnNames, aggregated);
			}
	
			for(Entry<String, ClassComplexityRecord> complexityClassRecord: this.classesMap.entrySet()) {
				//className = complexityClassRecord.getKey();
				complexityClassRecord.getValue().putColumnName(columnNames, aggregated);
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
		
		content.append("FILE: " + this.fileName);
		content.append(';');
		content.append(String.valueOf(this.methodsMap.size()).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.classesMap.size()).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.getNumberAssociatedDecorators()).replace(".", ","));
		content.append(';');
		
		if (!aggregated) {
			for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.methodsMap.entrySet()) {
				complexityMethodRecord.getValue().writeToCSV(content, aggregated);
			}
	
			for(Entry<String, ClassComplexityRecord> complexityClassRecord: this.classesMap.entrySet()) {
				complexityClassRecord.getValue().writeToCSV(content, aggregated);
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
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.methodsMap.entrySet()) {
			usedSet.add(complexityMethodRecord);
		}
		for(Entry<String, ClassComplexityRecord> complexityMethodRecord: this.classesMap.entrySet()) {
			usedSet.add(complexityMethodRecord);
		}
		return usedSet;
	}
}
