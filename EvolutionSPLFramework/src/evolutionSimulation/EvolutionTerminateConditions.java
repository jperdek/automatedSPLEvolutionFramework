package evolutionSimulation;

public class EvolutionTerminateConditions {

	private int maxNumberOfIterations = 10;
	
	
	public EvolutionTerminateConditions() {
		
	}
	
	public void setMaximalNumberOfIterations(int maxNumberOfIterations) {
		this.maxNumberOfIterations = maxNumberOfIterations;
	}
	
	public int getMaximalNumberOfIterations() { return this.maxNumberOfIterations; }
}
