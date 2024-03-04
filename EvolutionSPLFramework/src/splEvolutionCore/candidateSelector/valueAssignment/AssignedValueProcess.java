package splEvolutionCore.candidateSelector.valueAssignment;


import java.util.ArrayList;
import java.util.List;


/**
 * Manages strategies that are used to represent outcome of given measurement
 *  -serves as interface for various associated strategies
 * 
 * @author Jakub Perdek
 *
 * @param <T> type of given strategies
 */
public class AssignedValueProcess<T> {

	/**
	 * Strategies that are used to represent outcome of given measurement/process
	 */
	protected List<T> chosenValueAssignmentStrategies;
	
	
	/**
	 * Creates manager to manage various strategies and stores first assigned strategy
	 * 
	 * @param chosenValueAssignmentStrategy - given chosen value assignment strategy
	 */
	public AssignedValueProcess(T chosenValueAssignmentStrategy) {
		this.chosenValueAssignmentStrategies = new ArrayList<T>();
		this.chosenValueAssignmentStrategies.add(chosenValueAssignmentStrategy);
	}
	
	/**
	 * Creates manager to manage various strategies and stores the list of strategies that are used to represent given outcome
	 * 
	 * @param chosenValueAssignmentStrategies - the list of chosen value assignment strategies
	 */
	public AssignedValueProcess(List<T> chosenValueAssignmentStrategies) {
		this.chosenValueAssignmentStrategies = new ArrayList<T>(chosenValueAssignmentStrategies);
	}
	
	/**
	 * Creates manager to manage various strategies and stores the array of strategies that are used to represent given outcome
	 * 
	 * @param chosenValueAssignmentStrategies - the array of chosen value assignment strategies
	 */
	public AssignedValueProcess(T chosenValueAssignmentStrategies[]) {
		this.chosenValueAssignmentStrategies = new ArrayList<T>();
		for (T chosenStrategy: chosenValueAssignmentStrategies) {
			this.chosenValueAssignmentStrategies.add(chosenStrategy);
		}
	}
}
