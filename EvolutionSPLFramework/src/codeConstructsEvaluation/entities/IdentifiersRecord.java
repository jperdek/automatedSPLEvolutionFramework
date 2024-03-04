package codeConstructsEvaluation.entities;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


public class IdentifiersRecord implements ComplexityMeasure {

	private double distinct;
	private double total;
	private Set<String> identifiers;
	private boolean hasAssociatedIdentifiers = true;
	
	
	public IdentifiersRecord(double distinct, double total) {
		this.distinct = distinct;
		this.total = total;
		this.identifiers = new HashSet<String>();
	}
	
	public double getDistinctIdentifiers() { return this.distinct; }
	
	public double getTotalIdentifiers() { return this.total; }
	
	public void setNoAssociatedIdentifiers() { this.hasAssociatedIdentifiers = false; }
	
	public boolean hasAssociatedIdentifiers() { return this.hasAssociatedIdentifiers; }
	
	public void addIdentifier(String identifier) {
		this.identifiers.add(identifier);
	}
	
	public boolean isInSet(String identifier) {
		return this.identifiers.contains(identifier);
	}
	
	public Iterator<String> getIdentifiers() {
		return this.identifiers.iterator();
	}
	
	public Set<String> getIdentifiersSet() {
		return this.identifiers;
	}
	
	public void addAllIdentifiers(Set<String> identifiers) {
		this.identifiers = identifiers;
	}
	
	public static IdentifiersRecord getComplexityDifference(IdentifiersRecord complexity1, IdentifiersRecord complexity2) {
		Set<String> identifiers = new HashSet<String>(complexity1.getIdentifiersSet());
		identifiers.removeAll(complexity2.getIdentifiersSet());
		int distinct = identifiers.size();
		double total = complexity1.getTotalIdentifiers() - complexity2.getTotalIdentifiers();
		IdentifiersRecord identifiersRecord = new IdentifiersRecord(distinct, total);
		identifiersRecord.addAllIdentifiers(identifiers);
		return identifiersRecord;
	}

	@Override
	public ComplexityMeasure getComplexityDifference(ComplexityMeasure complexityMeasure1,
			ComplexityMeasure complexityMeasure2) {
		return IdentifiersRecord.getComplexityDifference(
				(IdentifiersRecord) complexityMeasure1, (IdentifiersRecord) complexityMeasure2);
	}

	@Override
	public void print() {
		System.out.println("----| distant: " + this.distinct);
		System.out.println("----| total: " + this.total);
		
		System.out.print  ("----|");
		String identifier;
		Iterator<String> identifiersIterator = this.identifiers.iterator();
		while(identifiersIterator.hasNext()) {
			identifier = identifiersIterator.next();
			System.out.print(identifier);
			if (identifiersIterator.hasNext()) {
				System.out.print(", ");
			} else {
				System.out.print("\n");
			}
		}
	}
	
	public static void putColumnNameStatic(List<String> columnNames) {
		
		columnNames.add("Halstead Identifiers Distinct");
		columnNames.add("Halstead Identifiers Total");
		columnNames.add("Halstead Identifiers Names");
	}
	
	public static void writeToCSVStatic(StringBuilder content) {

		content.append("");
		content.append(';');
		content.append("");
		content.append(';');
		content.append("");
		content.append(';');
	}
	
	@Override
	public void putColumnName(List<String> columnNames) {
		
		columnNames.add("Halstead Identifiers Distinct");
		columnNames.add("Halstead Identifiers Total");
		columnNames.add("Halstead Identifiers Names");
	}
	
	@Override
	public void writeToCSV(StringBuilder content) {

		content.append(String.valueOf(this.distinct).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.total).replace(".", ","));
		content.append(';');
		content.append(String.join(",", this.identifiers.toArray(new String[0])).replaceAll(";", ","));
		content.append(';');
	}
}
