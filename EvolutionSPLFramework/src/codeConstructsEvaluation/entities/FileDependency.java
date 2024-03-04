package codeConstructsEvaluation.entities;

public class FileDependency {

	private String path;
	private int importOccurence;
	private String type;
	

	public FileDependency(String path, String type) {
		this.path = path;
		this.type = type;
	}
	
	public FileDependency(String path, String type, int importOccurence) {
		this(path, type);
		this.importOccurence = importOccurence;
	}
	
	public String getPath() { return this.path; }
	
	public String getType() { return this.type; }
	
	public int getLineOccurence() { return this.importOccurence; }
}
