package codeConstructsEvaluation.entities;
import java.util.ArrayList;
import java.util.List;


public class GeneralComplexity implements ComplexityMeasure {

	private String analyzedConstruct;
	private String name;
	private List<GeneralComplexityMeasure> generalComplexityMeasures;
	private GeneralComplexityMeasure averageComplexityMeasure;
	
	
	public GeneralComplexity(String name, String analyzedConstruct, GeneralComplexityMeasure averageComplexityMeasure) {
		this.name = name;
		this.analyzedConstruct = analyzedConstruct;
		this.generalComplexityMeasures = new ArrayList<GeneralComplexityMeasure>();
		this.averageComplexityMeasure = averageComplexityMeasure;
	}
	
	public void addGeneralComplexityMeasure(GeneralComplexityMeasure generalComplexityMeasure) {
		this.generalComplexityMeasures.add(generalComplexityMeasure);
	}
	
	public String getAnalyzedConstruct() { return this.analyzedConstruct; }
	
	public GeneralComplexityMeasure getAverageComplexityMeasure() { return this.averageComplexityMeasure; }

	@Override
	public ComplexityMeasure getComplexityDifference(ComplexityMeasure complexityMeasure1,
			ComplexityMeasure complexityMeasure2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putColumnName(List<String> columnNames) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeToCSV(StringBuilder content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		
	}
}
