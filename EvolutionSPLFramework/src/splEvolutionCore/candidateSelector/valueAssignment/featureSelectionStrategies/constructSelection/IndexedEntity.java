package splEvolutionCore.candidateSelector.valueAssignment.featureSelectionStrategies.constructSelection;


/**
 * Carrier entity to carry given object with associated index
 * 
 * @author Jakub Perdek
 *
 * @param <T> carried entity, entity that needs to be associated with index
 */
public class IndexedEntity<T> {

	/**
	 * Associated index with mapped object
	 */
	private int index;
	
	/**
	 * Carried object
	 */
	private T mappedObject;
	
	
	/**
	 * Associates index with given carried (parameterized) entity
	 * 
	 * @param mappedObject - object that needs to be associated with index
	 * @param index - index or position to orchestrate carried object
	 */
	public IndexedEntity(T mappedObject, int index) {
		this.mappedObject = mappedObject;
		this.index = index;
	}
	
	/**
	 * Changes index of given indexed entity
	 * 
	 * @param index - index or position to orchestrate carried object
	 */
	public void changeIndex(int index) { this.index = index; }
	
	/**
	 * Returns associated index with carried object
	 * 
	 * @return index or position to orchestrate carried object
	 */
	public int getIndex() { return this.index; }
	
	/**
	 * Returns carried object that is associated with index
	 * 
	 * @return carried object
	 */
	public T getMappedObject() { return this.mappedObject; }
}
