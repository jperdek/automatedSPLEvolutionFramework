package codeConstructsEvaluation.entities;

import java.util.List;

public class GeneralComplexityMeasure implements ComplexityMeasure {

	private double value;
	private String label;
	private double rank;
	private String measureType;
	
	
	public GeneralComplexityMeasure(String measureType, double value, String label, double rank) {
		this.measureType = measureType;
		this.value = value;
		this.label = label;
		this.rank = rank;
	}
	
	public String getMeasureType() { return this.measureType; }
	
	public double getValue() { return this.value; }
	
	public String getLabel() { return this.label; }
	
	public double getRank() { return this.rank; }

	public static GeneralComplexityMeasure getComplexityDifference(GeneralComplexityMeasure complexity1, GeneralComplexityMeasure complexity2) throws IncompatibleComplexityMeasures {
		if (!complexity1.getMeasureType().equals(complexity2.getMeasureType())) {
			throw new IncompatibleComplexityMeasures("Incompatible measure types of General Complexity: " + complexity1.getMeasureType() + " <---> " + complexity2.getMeasureType());
		}
		double valueDifference = complexity1.getValue() - complexity2.getValue();
		double rankDifference = complexity1.getRank() - complexity2.getRank();
		String rankChange = complexity1.getLabel() + " --> " + complexity2.getLabel();
		return new GeneralComplexityMeasure(complexity1.getMeasureType(), valueDifference, rankChange, rankDifference);
	}
	
	@Override
	public ComplexityMeasure getComplexityDifference(ComplexityMeasure complexityMeasure1,
			ComplexityMeasure complexityMeasure2) {
		try {
			return GeneralComplexityMeasure.getComplexityDifference((GeneralComplexityMeasure) complexityMeasure1, (GeneralComplexityMeasure) complexityMeasure2);
		} catch (IncompatibleComplexityMeasures e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void print() {
	}
	
	@Override
	public void putColumnName(List<String> columnNames) {
	}
	
	@Override
	public void writeToCSV(StringBuilder content) {

	}
}
