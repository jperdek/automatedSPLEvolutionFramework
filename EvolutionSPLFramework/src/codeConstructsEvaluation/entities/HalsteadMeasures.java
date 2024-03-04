package codeConstructsEvaluation.entities;

import java.util.List;


public class HalsteadMeasures implements ComplexityMeasure {

	private double bugs;
	private double difficulty;
	private double effort;
	private double length;
	private double time;
	private double vocabulary;
	private double volume;
	private IdentifiersRecord operators;
	private IdentifiersRecord operands;
	
	
	public HalsteadMeasures(double bugs, double difficulty, double effort, 
			double length, double time, double vocabulary, double volume, 
			IdentifiersRecord operators, IdentifiersRecord operands) {
		this.bugs = bugs;
		this.difficulty = difficulty;
		this.effort = effort;
		this.length = length;
		this.time = time;
		this.vocabulary = vocabulary;
		this.volume = volume;
		this.operators = operators;
		this.operands = operands;
	}
	
	public double getBugs() { return this.bugs; }
	
	public double getDifficulty() { return this.difficulty; }

	public double getEffort() { return this.effort; }
	
	public double getLength() { return this.length; }
	
	public double getTime() { return this.time; }
	
	public double getVocabulary() { return this.vocabulary; }
	
	public double getVolume() { return this.volume; }

	public IdentifiersRecord getOperators() { return this.operators; }
	
	public IdentifiersRecord getOperands() { return this.operands; }
	
	public static HalsteadMeasures getComplexityDifference(HalsteadMeasures complexity1, HalsteadMeasures complexity2) {
		double bugsDifference = complexity1.getBugs() - complexity2.getBugs();
		double difficultyDifference = complexity1.getDifficulty() - complexity2.getDifficulty();
		double effortDifference = complexity1.getEffort() - complexity2.getEffort();
		double lengthDifference = complexity1.getLength() - complexity2.getLength();
		double timeDifference = complexity1.getTime() - complexity2.getTime();
		double vocabularyDifference = complexity1.getVocabulary() - complexity2.getVocabulary();
		double volumeDifference = complexity1.getVolume() - complexity2.getVolume();
		IdentifiersRecord operatorsDifference = IdentifiersRecord.getComplexityDifference(
				complexity1.getOperators(), complexity2.getOperators());
		IdentifiersRecord operandsDifference = IdentifiersRecord.getComplexityDifference(
				complexity1.getOperands(), complexity2.getOperands());
		return new HalsteadMeasures(bugsDifference, difficultyDifference, effortDifference,
				lengthDifference, timeDifference, vocabularyDifference, volumeDifference, 
				operatorsDifference, operandsDifference);
	}
	
	@Override
	public ComplexityMeasure getComplexityDifference(ComplexityMeasure complexityMeasure1,
			ComplexityMeasure complexityMeasure2) {
		return HalsteadMeasures.getComplexityDifference((HalsteadMeasures) complexityMeasure1, (HalsteadMeasures) complexityMeasure2);
	}

	@Override
	public void print() {
		System.out.println("____| Halstead complexity measures |____");
		System.out.println("----| bugs: " + this.bugs);
		System.out.println("----| difficulty: " + this.difficulty);
		System.out.println("----| effort: " + this.effort);
		System.out.println("----| length: " + this.length);
		System.out.println("----| time: " + this.time);
		System.out.println("----| vocabulary: " + this.vocabulary);
		System.out.println("----| volume: " + this.volume);
		System.out.println("----| operands: ");
		operands.print();
		System.out.println("----| operators: ");
		operators.print();
		System.out.println("-----------------------------------------");
	}
	
	public static void putColumnNameStatic(List<String> columnNames) {
		columnNames.add("Halstead Bugs");
		columnNames.add("Halstead Difficulty");
		columnNames.add("Halstead Effort");
		columnNames.add("Halstead Length");
		columnNames.add("Halstead Time");
		columnNames.add("Halstead Vocabulary");
		columnNames.add("Halstead Volume");
		IdentifiersRecord.putColumnNameStatic(columnNames);
		IdentifiersRecord.putColumnNameStatic(columnNames);
	}
	
	public static void writeToCSVStatic(StringBuilder content) {
		content.append("");
		content.append(';');
		content.append("");
		content.append(';');
		content.append("");
		content.append(';');
		content.append("");
		content.append(';');
		content.append("");
		content.append(';');
		content.append("");
		content.append(';');
		content.append("");
		content.append(';');
		IdentifiersRecord.writeToCSVStatic(content);
		IdentifiersRecord.writeToCSVStatic(content);	
	}
	
	@Override
	public void putColumnName(List<String> columnNames) {
		columnNames.add("Halstead Bugs");
		columnNames.add("Halstead Difficulty");
		columnNames.add("Halstead Effort");
		columnNames.add("Halstead Length");
		columnNames.add("Halstead Time");
		columnNames.add("Halstead Vocabulary");
		columnNames.add("Halstead Volume");
		operands.putColumnName(columnNames);
		operators.putColumnName(columnNames);
	}
	
	@Override
	public void writeToCSV(StringBuilder content) {
		content.append(String.valueOf(this.bugs).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.difficulty).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.effort).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.length).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.time).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.vocabulary).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.volume).replace(".", ","));
		content.append(';');
		operands.writeToCSV(content);
		operators.writeToCSV(content);	
	}
}
