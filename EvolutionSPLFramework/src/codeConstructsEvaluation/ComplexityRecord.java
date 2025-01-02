package codeConstructsEvaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import astFileProcessor.astObjects.ASTGenericDecorator;
import codeConstructsEvaluation.entities.CyclomaticComplexity;
import codeConstructsEvaluation.entities.GeneralComplexity;
import codeConstructsEvaluation.entities.GeneralComplexityMeasure;
import codeConstructsEvaluation.entities.HalsteadMeasures;
import codeConstructsEvaluation.entities.SourceLinesOfCodeMeasure;


/**
 * Representation of the complexity record
 * 
 * @author Jakub Perdek
 *
 */
public class ComplexityRecord implements EntityComplexityDifference {

	/**
	 * Logger to track information from complexity record
	 */
	private static final Logger logger = LoggerFactory.getLogger(ComplexityRecord.class);
	
	/**
	 * All available source code entity types for which the complexity is evaluated
	 * 
	 * @author Jakub Perdek
	 *
	 */
	protected static enum SourceType {
		FILE,
		METHOD,
		CLASS_METHOD,
		CLASS,
		GENERAL,
		UNDEFINED
	};
	
	//private AnalyzedEntityType analyzedEntityType = null;
	/**
	 * The measurement of Cyclomatic complexity
	 */
	private CyclomaticComplexity cyclomaticComplexity = null;
	
	/**
	 * The measurement of general complexity
	 */
	private GeneralComplexity generalComplexity = null;
	
	/**
	 * The measurement of Halstead measures
	 */
	private HalsteadMeasures halsteadMeasures = null;
	
	/**
	 * The measurement of SLOC
	 */
	private SourceLinesOfCodeMeasure slocMeasure = null;
	
	/**
	 * The explicit specification whit what is this complexity record associated with
	 */
	private String associatedWith = null;
	
	/**
	 * Which type of record is used to measure the code complexity
	 */
	private SourceType sourceType = ComplexityRecord.SourceType.UNDEFINED;
	
	/**
	 * The list of associated decorators
	 */
	protected List<ASTGenericDecorator> associatedDecorators = null;
	
	
	/**
	 * Created the ComplexityRecord.SourceType.GENERAL complexity record
	 */
	public ComplexityRecord() {
		this.sourceType = ComplexityRecord.SourceType.GENERAL;
	}
	
	/**
	 * Creates the custom complexity record
	 * 
	 * @param sourceType - the type of analyzed record
	 */
	protected ComplexityRecord(SourceType sourceType) {
		this.sourceType = sourceType;
	}
	
	/**
	 * Associates the list of decorators by creating new list and replacing the previous stored list
	 * 
	 * @param frameworkDecorators - the list of used decorators
	 */
	public void associateDecorators(List<ASTGenericDecorator> frameworkDecorators) {
		this.associatedDecorators = new ArrayList<ASTGenericDecorator>(frameworkDecorators);
	}
	
	/**
	 * Returns the number of associated decorators - if no one is associated then it returns -1
	 * 
	 * @return the number of associated decorators - if no one is associated then it returns -1
	 */
	protected int getNumberAssociatedDecorators() { 
		if(this.associatedDecorators == null) {
			return -1; 
		}
		return this.associatedDecorators.size(); 
	}
	
	/**
	 * Sets associated with string to specify the association of the complexity record
	 * 
	 * @param associatedWith - string to specify the association of the complexity record
	 */
	public void setAssociatedWith(String associatedWith) { this.associatedWith = associatedWith; }
	
	/**
	 * Returns the string that specifies the association of the complexity record
	 * 
	 * @return string that specifies the association of the complexity record
	 */
	public String getAssociatedWith() { return this.associatedWith; }
	
	/**
	 * Inserts/associates Halstead code complexity measurement with this complexity record
	 * 
	 * @param halsteadMeasures - the inserts/associated Halstead code complexity measurements with this complexity record
	 */
	public void setHalsteadMeasure(HalsteadMeasures halsteadMeasures) {
		this.halsteadMeasures = halsteadMeasures;
	}
	
	/**
	 * Inserts/associates Cyclomatic code complexity measurement with this complexity record
	 * 
	 * @param cyclomaticComplexity - the inserts/associated Cyclomatic code complexity measurement with this complexity record
	 */
	public void setCyclomaticComplexity(CyclomaticComplexity cyclomaticComplexity) {
		this.cyclomaticComplexity = cyclomaticComplexity;
	}
	
	/**
	 * Inserts/associates general code complexity measurement with this complexity record
	 * 
	 * @param generalComplexity - the inserted/associated general code complexity measurement with this complexity record
	 */
	public void setGeneralComplexity(GeneralComplexity generalComplexity) {
		this.generalComplexity = generalComplexity;
	}
	
	/**
	 * Inserts/associates SLOC (source lines of code) code complexity measurement with this complexity record
	 * 
	 * @param slocMeasure - the inserted/associated SLOC (source lines of code) code complexity measurements with this complexity record
	 */
	public void setSourceLinesOfCodeMeasure(SourceLinesOfCodeMeasure slocMeasure) {
		this.slocMeasure = slocMeasure;
	}
	
	/**
	 * Inserts/associates general code complexity measurement with this complexity record
	 * 
	 * @param generalComplexityMeasure - the representation/measurement of general code complexity measurement
	 * @return true if general code complexity measurement is successfully added otherwise false
	 */
	public boolean insertGeneralComplexityMeasure(GeneralComplexityMeasure generalComplexityMeasure) {
		if (this.generalComplexity != null) {
			this.generalComplexity.addGeneralComplexityMeasure(generalComplexityMeasure);
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the measured Halstead code complexity metrics associated with this complexity record
	 * 
	 * @return the measured Halstead code complexity metrics associated with this complexity record
	 */
	public HalsteadMeasures getHalsteadMeasure() { return this.halsteadMeasures; }
	
	/**
	 * Returns the measured Cyclomatic code complexity metrics associated with this complexity record
	 * 
	 * @return the measured Cyclomatic code complexity metrics associated with this complexity record
	 */
	public CyclomaticComplexity getCyclomaticComplexity() { return this.cyclomaticComplexity; }
	
	/**
	 * Returns the measured General code complexity metrics associated with this complexity record
	 * 
	 * @return the measured General code complexity metrics associated with this complexity record
	 */
	public GeneralComplexity getGeneralComplexity() { return this.generalComplexity; }
	
	/**
	 * Returns the SLOC (source lines of code) complexity metrics associated with this complexity record
	 * -physical
	 * -logical
	 * 
	 * @return the SLOC (source lines of code) complexity metrics associated with this complexity record
	 */
	public SourceLinesOfCodeMeasure getSourceLinesOfCodeMeasure() { return this.slocMeasure; }
	
	/**
	 * Returns the source type of the complexity record
	 * 
	 * @return the source type of the complexity record
	 */
	public SourceType getSource() { return this.sourceType; }

	@Override
	/**
	 * Calculates differences between the complexity measurements of two complexity records
	 * 
	 * @param compareWith - another complexity record to compare this instance with 
	 */
	public EntityComplexityDifference makeDifference(EntityComplexityDifference compareWith) {
		ComplexityRecord compareWithComplexityRecord = (ComplexityRecord) compareWith;
		ComplexityRecord complexityRecordDifference = new ComplexityRecord();
		complexityRecordDifference.setAssociatedWith(this.associatedWith);
		if (this.associatedDecorators != null) {
			complexityRecordDifference.associateDecorators(this.associatedDecorators);
		}
		
		CyclomaticComplexity cyclomaticComplexityDifference, compareWithCyclomaticComplexity;
		HalsteadMeasures halsteadMeasuresDifference, compareWithHalsteadMeasures;
		SourceLinesOfCodeMeasure slocMeasuresDifference, compareWithSlocMeasures;

		if(this.cyclomaticComplexity != null) {
			compareWithCyclomaticComplexity = compareWithComplexityRecord.getCyclomaticComplexity();
			if (compareWithCyclomaticComplexity != null) {
				cyclomaticComplexityDifference = CyclomaticComplexity.getComplexityDifference(this.cyclomaticComplexity, compareWithCyclomaticComplexity);
				complexityRecordDifference.setCyclomaticComplexity(cyclomaticComplexityDifference);
			} else {
				complexityRecordDifference.setCyclomaticComplexity(this.cyclomaticComplexity);
			}
		}
		
		if(this.halsteadMeasures != null) {
			compareWithHalsteadMeasures = compareWithComplexityRecord.getHalsteadMeasure();
			if (compareWithHalsteadMeasures != null) {
				halsteadMeasuresDifference = HalsteadMeasures.getComplexityDifference(this.halsteadMeasures, compareWithHalsteadMeasures);
				complexityRecordDifference.setHalsteadMeasure(halsteadMeasuresDifference);
			} else {
				complexityRecordDifference.setHalsteadMeasure(this.halsteadMeasures);
			}
		}
		
		if(this.slocMeasure != null) {
			compareWithSlocMeasures = compareWithComplexityRecord.getSourceLinesOfCodeMeasure();
			if (compareWithSlocMeasures != null) {
				slocMeasuresDifference = SourceLinesOfCodeMeasure.getComplexityDifference(this.slocMeasure, compareWithSlocMeasures);
				complexityRecordDifference.setSourceLinesOfCodeMeasure(slocMeasuresDifference);
			} else {
				complexityRecordDifference.setSourceLinesOfCodeMeasure(this.slocMeasure);
			}
		}
		
		return complexityRecordDifference;
	}

	/**
	 * Updates this code complexity record with the measured complexities in another one code complexity record
	 * 
	 * @param sourceRecord - another one code complexity record that is used to update this one
	 */
	public void updateAccordingTo(ComplexityRecord sourceRecord) {
		this.cyclomaticComplexity = sourceRecord.getCyclomaticComplexity();
		this.halsteadMeasures = sourceRecord.getHalsteadMeasure();
		this.slocMeasure = sourceRecord.getSourceLinesOfCodeMeasure();
		this.generalComplexity = sourceRecord.getGeneralComplexity();
		this.associatedWith = sourceRecord.getAssociatedWith();
	}
	
	@Override
	/**
	 * Prints the data stored in this complexity record
	 */
	public void print() {
		logger.debug("XXXXXXXXX___| COMPLEXITY RECORD " + this.sourceType + " |___XXXXXXXXX");
		if (this.cyclomaticComplexity != null) {
			this.cyclomaticComplexity.print();
		}
		if (this.halsteadMeasures != null) {
			this.halsteadMeasures.print();
		}
		if (this.slocMeasure != null) {
			this.slocMeasure.print();
		}
		logger.debug("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
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
		//if (this.sourceType.equals(SourceType.GENERAL)) {
			columnNames.add("Associated with");
			if (this.associatedDecorators != null) {
				columnNames.add("Overall file decorastors");
			}
		//}
		if (this.cyclomaticComplexity != null) {
			this.cyclomaticComplexity.putColumnName(columnNames);
		} else {
			CyclomaticComplexity.putColumnNameStatic(columnNames);
		}
		if (this.halsteadMeasures != null) {
			this.halsteadMeasures.putColumnName(columnNames);
		} else {
			HalsteadMeasures.putColumnNameStatic(columnNames);
		}
		if (this.slocMeasure != null) {
			this.slocMeasure.putColumnName(columnNames);
		} else {
			SourceLinesOfCodeMeasure.putColumnNameStatic(columnNames);
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
		//if (this.sourceType.equals(SourceType.GENERAL)) {
			content.append(this.associatedWith);
			content.append(";");
			if (this.associatedDecorators != null) {
				content.append(this.associatedDecorators.size());
				content.append(";");
			}
		//}
		if (this.cyclomaticComplexity != null) {
			this.cyclomaticComplexity.writeToCSV(content);
		} else {
			CyclomaticComplexity.writeToCSVStatic(content);
		}
		if (this.halsteadMeasures != null) {
			this.halsteadMeasures.writeToCSV(content);
		} else {
			HalsteadMeasures.writeToCSVStatic(content);
		}
		if (this.slocMeasure != null) {
			this.slocMeasure.writeToCSV(content);
		} else {
			SourceLinesOfCodeMeasure.writeToCSVStatic(content);
		}
	}

	@Override
	/**
	 * Returns null
	 * 
	 * @return null
	 */
	public Set<Entry<String, ? extends ComplexityRecord>> getComplexityRecordsEntrySet() {
		return null;
	}
}
