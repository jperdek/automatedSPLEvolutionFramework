package codeConstructsEvaluation.entities;

import java.util.List;

public class SourceLinesOfCodeMeasure implements ComplexityMeasure {

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
		System.out.println("____| Source Lines Of Code Measure |____");
		System.out.println("---| physicalLOC: " + this.physicalLOC);
		System.out.println("---| logicalLOC: " + this.logicalLOC);
		System.out.println("-----------------------------------------");
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
