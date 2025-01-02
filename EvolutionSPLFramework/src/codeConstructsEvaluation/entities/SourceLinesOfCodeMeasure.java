package codeConstructsEvaluation.entities;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Source Lines of code measurement - representation
 * 
 * @author Jakub Perdek
 *
 */
public class SourceLinesOfCodeMeasure implements ComplexityMeasure {

	/**
	 * Logger to track information about SLOC measure
	 */
	private static final Logger logger = LoggerFactory.getLogger(SourceLinesOfCodeMeasure.class);
	
	private double physicalLOC;
	private double logicalLOC;
	
	
	public SourceLinesOfCodeMeasure(double physical, double logical) {
		this.physicalLOC = physical;
		this.logicalLOC = logical;
	}
	
	public double getLogicalNumberLines() { return this.logicalLOC; }
	
	public double getPhysicalNumberLines() { return this.physicalLOC; }
	
	public static SourceLinesOfCodeMeasure getComplexityDifference(SourceLinesOfCodeMeasure complexity1, SourceLinesOfCodeMeasure complexity2) {
		double physicalLOCDifference = complexity1.getPhysicalNumberLines() - complexity2.getPhysicalNumberLines();
		double logicalLOCDifference = complexity1.getLogicalNumberLines() - complexity2.getLogicalNumberLines();
		return new SourceLinesOfCodeMeasure(physicalLOCDifference, logicalLOCDifference);
	}

	@Override
	public ComplexityMeasure getComplexityDifference(ComplexityMeasure complexityMeasure1,
			ComplexityMeasure complexityMeasure2) {
		return SourceLinesOfCodeMeasure.getComplexityDifference((SourceLinesOfCodeMeasure) complexityMeasure1,
				(SourceLinesOfCodeMeasure) complexityMeasure2);
	}

	@Override
	public void print() {
		logger.debug("____| Source Lines Of Code Measure |____");
		logger.debug("---| physicalLOC: " + this.physicalLOC);
		logger.debug("---| logicalLOC: " + this.logicalLOC);
		logger.debug("-----------------------------------------");
	}
	
	public static void putColumnNameStatic(List<String> columnNames) {
		columnNames.add("LOC Physical");
		columnNames.add("LOC Logical");
	}
	
	public static void writeToCSVStatic(StringBuilder content) {
		content.append("");
		content.append(';');
		content.append("");
		content.append(';');
	}
	
	@Override
	public void putColumnName(List<String> columnNames) {
		columnNames.add("LOC Physical");
		columnNames.add("LOC Logical");
	}
	
	@Override
	public void writeToCSV(StringBuilder content) {
		content.append(String.valueOf(this.physicalLOC).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.logicalLOC).replace(".", ","));
		content.append(';');
	}
}
