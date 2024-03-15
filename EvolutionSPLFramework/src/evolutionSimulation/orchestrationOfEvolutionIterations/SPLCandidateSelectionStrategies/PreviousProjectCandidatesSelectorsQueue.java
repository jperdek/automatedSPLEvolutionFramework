package evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Entity to adapt queue to collect recently used SPL project candidate selectors from previous SPL iterations
 * 
 * @author Jakub Perdek
 *
 */
public class PreviousProjectCandidatesSelectorsQueue {

	/**
	 * The maximum size of the queue
	 */
	private int maxQueueSize = 10;
	
	/**
	 * Queue to recently collect/with recently collected (used) SPL project candidate selectors from previous SPL iterations
	 */
	private Queue<SPLProjectCandidateToPopulationOfEvolIterationSelector> previousProjectCandidatesSelector;
	
	/**
	 * Initializes the instance with queue to collect recently used SPL project candidate selectors from previous SPL iterations
	 * 
	 * @param initialInstance - the initial and recently used SPL project candidate selector instance or null if not available
	 */
	public PreviousProjectCandidatesSelectorsQueue(
			SPLProjectCandidateToPopulationOfEvolIterationSelector initialInstance) {
		this.previousProjectCandidatesSelector = new LinkedList<SPLProjectCandidateToPopulationOfEvolIterationSelector>();
		if (initialInstance != null) { this.previousProjectCandidatesSelector.add(initialInstance); }
	}
	
	/**
	 * Changes the maximal size of the queue with recently used SPL project candidate selector instances
	 * 
	 * @param maxQueueSize - the new maximal size of the queue with recently used SPL project candidate selector instances
	 */
	public void changeMaxQueueSize(int maxQueueSize) { this.maxQueueSize = maxQueueSize; }
	
	/**
	 * Returns the maximal size of the queue with recently used SPL project candidate selector instances
	 * 
	 * @return the maximal size of the queue with recently used SPL project candidate selector instances
	 */
	public int getMaxQueueSize() { return this.maxQueueSize; }
	
	/**
	 * Inserts the recently used SPL project candidate selector instance
	 * -if queue is full, then the oldest instance of SPL project candidate selector is removed
	 * 
	 * @param previousProjectCandidate - the recently used SPL project candidate selector instance to be inserted
	 */
	public void addProjectToQueueOfPreviousProjects(
			SPLProjectCandidateToPopulationOfEvolIterationSelector previousProjectCandidate) {
		if (this.previousProjectCandidatesSelector.size() + 1 > this.maxQueueSize) {
			this.previousProjectCandidatesSelector.poll();
		}
		this.previousProjectCandidatesSelector.add(previousProjectCandidate);
	}
	
	/**
	 * Returns the last processed/the most recently used SPL project candidate selector instance
	 * 
	 * @return the last processed/the most recently used SPL project candidate selector instance
	 */
	public SPLProjectCandidateToPopulationOfEvolIterationSelector getLastProcessedProjectCandidateSelector() {
		return this.previousProjectCandidatesSelector.peek();
	}
	
	/**
	 * Returns the iterator to iterate over recently used SPL project candidate selector instances
	 * 
	 * @return the iterator to iterate over recently used SPL project candidate selector instances
	 */
	public Iterator<SPLProjectCandidateToPopulationOfEvolIterationSelector> 
			getPreviousNProjectCandidatesSelectors() {
		return this.previousProjectCandidatesSelector.iterator();
	}
}
