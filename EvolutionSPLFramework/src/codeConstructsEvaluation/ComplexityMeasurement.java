package codeConstructsEvaluation;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import astFileProcessor.astObjects.ASTGenericDecorator;
import astFileProcessor.astObjects.ASTGenericDecorator.DecoratorAssociatedWith;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


/**
 * Stores information about code complexity measurement
 * -customized for the most complex Typhone service - TypeScript data
 * 
 * @author Jakub Perdek
 *
 */
public class ComplexityMeasurement implements EntityComplexityDifference {

	/**
	 * Logger to track information from complexity measurement
	 */
	private static final Logger logger = LoggerFactory.getLogger(ComplexityMeasurement.class);
	
	/**
	 * TYpe of measurement data
	 */
	private String measurementType = "Single";
	
	/**
	 * Maintainability of the code if available
	 */
	private double maintainability = -1;
	
	/**
	 * Mapping of complexity measurements - the measurement name is mapped to the complexity record
	 */
	private Map<String, ComplexityRecord> complexityMeasurement;
	
	/**
	 * Mapping of associated complexity measurements - the measurement name is mapped to the complexity record
	 */
	private Map<String, ComplexityRecord> uniqueComplexityMeasurements = null;
	
	/**
	 * Mapping of associated complexity measurements
	 */
	private Map<String, ComplexityMeasurement> associatedMeasurements = null;

	
	/**
	 * Creates the instance to handle complexity measurement
	 * -initializes associated mappings and other data structures
	 */
	public ComplexityMeasurement() {
		this.complexityMeasurement = new HashMap<String, ComplexityRecord>();
		this.maintainability = -1;
	}
	
	/**
	 * Creates the instance to handle complexity measurement with additional information
	 * -initializes associated mappings and other data structures
	 * 
	 * @param measurementType - the type of applied code complexity measurement
	 * @param maintainability - maintainability of the code if available
	 */
	public ComplexityMeasurement(String measurementType, double maintainability) {
		this();
		this.maintainability = maintainability;
		this.measurementType = measurementType;
	}
	
	/**
	 * Creates the instance to handle complexity measurement with additional information
	 * -initializes associated mappings and other data structures
	 * 
	 * @param measurementType - the type of applied code complexity measurement
	 */
	public ComplexityMeasurement(String measurementType) {
		this();
		this.measurementType = measurementType;
	}
	
	/**
	 * Associates decorators with the file unit
	 * 
	 * @param associatedDecorators - the list of decorators that should be associated with the complexity record
	 */
	public void associateDecoratorsWithFileUnit(List<ASTGenericDecorator> associatedDecorators) {
		for (ComplexityRecord complexityRecord: this.complexityMeasurement.values()) {
			if (complexityRecord instanceof FileComplexityRecord) {
				complexityRecord.associateDecorators(associatedDecorators);
			}
		}
		if (this.uniqueComplexityMeasurements != null) {
			for (ComplexityRecord complexityRecord: this.uniqueComplexityMeasurements.values()) {
				if (complexityRecord instanceof FileComplexityRecord) {
					complexityRecord.associateDecorators(associatedDecorators);
				}
			}
		}
		if(this.associatedMeasurements != null) {
			for (ComplexityMeasurement associatedMeasurement: this.associatedMeasurements.values()) {
				associatedMeasurement.associateDecoratorsWithFileUnit(associatedDecorators);
			}
		}
	}
	
	/**
	 * Associates decorators with the complexity record and also with associated measurements if are available
	 * 
	 * @param associatedDecorators - the list of decorators that should be associated with the complexity record
	 */
	public void associateDecoratorsWithEverything(List<ASTGenericDecorator> associatedDecorators) {
		for (ComplexityRecord complexityRecord: this.complexityMeasurement.values()) {
			complexityRecord.associateDecorators(associatedDecorators);
		}
		if(this.associatedMeasurements != null) {
			for (ComplexityMeasurement associatedMeasurement: this.associatedMeasurements.values()) {
				associatedMeasurement.associateDecoratorsWithEverything(associatedDecorators);
			}
		}
	}
	
	/**
	 * Associates decorators with given complexity records in accordance with their type
	 * 
	 * @param associatedDecorators - the list of decorators that should be associated with the complexity record
	 */
	public void associateDecorators(List<ASTGenericDecorator> associatedDecorators) {
		 Predicate<ASTGenericDecorator> byClass = associatedDecorator -> associatedDecorator.isAssociatedWith(DecoratorAssociatedWith.CLASS);
		 Predicate<ASTGenericDecorator> byMethod = associatedDecorator -> associatedDecorator.isAssociatedWith(DecoratorAssociatedWith.METHOD);
		for (ComplexityRecord complexityRecord: this.complexityMeasurement.values()) {
			if (complexityRecord instanceof FileComplexityRecord) {
				complexityRecord.associateDecorators(associatedDecorators);
			} else if (complexityRecord instanceof ClassComplexityRecord) {
				complexityRecord.associateDecorators(associatedDecorators.stream().filter(byClass).collect(Collectors.toList()));
			} else if (complexityRecord instanceof MethodComplexityRecord) {
				complexityRecord.associateDecorators(associatedDecorators.stream().filter(byMethod).collect(Collectors.toList()));
			}
		}
	}
	
	/**
	 * Associates measurement with other measurements - the measurement is inserted into associated measurements
	 *   
	 * @param measurementType - the type of measurement
	 * @param parentMeasurement - the parent measurement that is going to take measurement under its associated measurements
	 */
	public void putAssociatedMeasurement(String measurementType, ComplexityMeasurement parentMeasurement) {
		if (this.associatedMeasurements == null) {
			this.associatedMeasurements = new HashMap<String, ComplexityMeasurement>();
		}
		this.associatedMeasurements.put(measurementType, parentMeasurement);
	}
	
	/**
	 * Associates measurement with other unique measurements - the measurement is inserted into unique measurements
	 * 
	 * @param measurementType - the type of measurement
	 * @param uniqueComplexityRecord - the measurement that is going to take measurement under its unique measurements
	 */
	public void putUniqueComplexityMeasurement(String measurementType, ComplexityRecord uniqueComplexityRecord) {
		if (this.uniqueComplexityMeasurements == null) {
			this.uniqueComplexityMeasurements = new HashMap<String, ComplexityRecord>();
		}
		this.uniqueComplexityMeasurements.put(measurementType, uniqueComplexityRecord);
	}

	/**
	 * Returns maintainability of analyzed code fragment if available, otherwise -1
	 * 
	 * @return the maintainability of analyzed code fragment if available, otherwise -1
	 */
	public double getMaintainability() { return this.maintainability; }
	
	/**
	 * Returns the associated measurement according to the string that is provided as parameter otherwise null
	 * 
	 * @param associatedMeasurementIdentifier - the string identifier that is mapped to given representation of associated measurement
	 * @return the associated measurement according to the string that is provided as parameter otherwise null
	 */
	public ComplexityMeasurement getAssociatedMeasurement(String associatedMeasurementIdentifier) {
		if (this.associatedMeasurements != null) {
			return this.associatedMeasurements.get(associatedMeasurementIdentifier);
		}
		return null;
	}
	
	/**
	 * Changes the type of measurement
	 * 
	 * @param measurementType - the new name of this measurement
	 */
	public void changeMeasurementType(String measurementType) {
		this.measurementType = measurementType;
	}
	
	/**
	 * Returns the complexity record according to the provided name
	 * 
	 * @param recordName - the provided name of the record
	 * @return the complexity record associated with/mapped to given record name
	 */
	public ComplexityRecord getComplexityRecord(String recordName) {
		return this.complexityMeasurement.get(recordName);
	}
	
	/**
	 * Returns the default complexity record - the record with name "default"
	 * 
	 * @return the default complexity record - the record with name "default"
	 */
	public ComplexityRecord getComplexityRecord() {
		return this.complexityMeasurement.get("default");
	}
	
	/**
	 * Associates the complexity record into this measurement - provided unique name is associated to given representation in the map
	 * 
	 * @param recordName - the unique code complexity record name
	 * @param complexityRecord - the code complexity record that should be stored under this measurement
	 */
	public void setComplexityRecord(String recordName, ComplexityRecord complexityRecord) {
		this.complexityMeasurement.put(recordName, complexityRecord);
	}
	
	/**
	 * Associates the complexity record into this measurement - as default name is used the name "default" is associated to given representation in the map
	 * -multiple calls will result with replacing previously added complexity record
	 * 
	 * @param complexityRecord - the code complexity record that should be stored under this measurement
	 */
	public void setComplexityRecord(ComplexityRecord complexityRecord) {
		this.complexityMeasurement.put("default", complexityRecord);
	}
	
	/**
	 * Returns the type of measurement
	 * 
	 * @return the type of measurement
	 */
	public String getMeasurementType() { return this.measurementType; }

	
	@Override
	/**
	 * Calculates differences between the two complexity measurements
	 * 
	 * @param compareWith - another complexity measurement to compare this instance with 
	 */
	public EntityComplexityDifference makeDifference(EntityComplexityDifference compareWith) {
		ComplexityMeasurement compareWithMeasurement = (ComplexityMeasurement) compareWith;
		ComplexityMeasurement complexityMeasurementDifference = new ComplexityMeasurement();
		String recordName;
		ComplexityRecord recordDifference, baseComplexityRecord, comparedWithComplexityRecord;
		for(Entry<String, ComplexityRecord> complexityRecord: this.complexityMeasurement.entrySet()) {
			recordName = complexityRecord.getKey();
			baseComplexityRecord = complexityRecord.getValue();
			comparedWithComplexityRecord = compareWithMeasurement.getComplexityRecord(recordName);
			if (comparedWithComplexityRecord == null) {
				complexityMeasurementDifference.putUniqueComplexityMeasurement(recordName, baseComplexityRecord);
			} else {
				recordDifference = (ComplexityRecord) baseComplexityRecord.makeDifference(comparedWithComplexityRecord);
				complexityMeasurementDifference.setComplexityRecord(recordName, recordDifference);
			}
			
		}
		return complexityMeasurementDifference;
	}

	@Override
	/**
	 * Prints the data stored in this complexity measurement along with all complexity record
	 */
	public void print() {
		String recordName;
		logger.debug("##########| COMPLEXITY MEASUREMENT (maintainability: " + this.maintainability + 
				", measurements: " + this.complexityMeasurement.size() + ") |#########");
		for(Entry<String, ComplexityRecord> complexityRecord: this.complexityMeasurement.entrySet()) {
			recordName = complexityRecord.getKey();
			logger.debug("######## ___ RECORD " + recordName + " ___ ##########");
			complexityRecord.getValue().print();
			logger.debug("########_____RECORD_END___ ##########");
		}
		logger.debug("################################################");
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
		columnNames.add("Maintainability");
		for(Entry<String, ComplexityRecord> complexityRecord: this.complexityMeasurement.entrySet()) {
			//recordName = complexityRecord.getKey();
			complexityRecord.getValue().putColumnName(columnNames, aggregated);
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
		content.append(String.valueOf(this.maintainability).replace(".", ","));
		content.append(';');
		for(Entry<String, ComplexityRecord> complexityRecord: this.complexityMeasurement.entrySet()) {
			complexityRecord.getValue().writeToCSV(content, aggregated);
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
		for(Entry<String, ComplexityRecord> complexityMethodRecord: this.complexityMeasurement.entrySet()) {
			usedSet.add(complexityMethodRecord);
		}

		return usedSet;
	}
}

