package dataRepresentationsExtensions.variabilityPermutations;

import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;


/**
 * Representation of the result instance among variability options
 * 
 * @author Jakub Perdek
 *
 * @param <T> The type of selected components that should be aggregated
 */
public class VariabilityPart<T> {
	
	/**
	 * The set of selected components that form the aggregation
	 */
	private Set<T> includedComponents;
	
	
	/**
	 * Creates/initializes the variability part - one selected instance among variability options
	 * 
	 * @param args - the array/enumeration of selected components that form the aggregation
	 */
	public VariabilityPart(T ...args) {
		this.includedComponents = new HashSet<T>();
		this.insertComponentIdentifiers(args);
	}
	
	/**
	 * Creates/initializes the variability part - one selected instance among variability options
	 */
	public VariabilityPart() {
		this.includedComponents = new HashSet<T>();
	}
	
	/**
	 * Creates/initializes the variability part from existing instance - existing one selected instance among variability options
	 *  
	 * @param vp - the another existing instance among variability options
	 */
	public VariabilityPart(VariabilityPart<T> vp) {
		this.includedComponents = new HashSet<T>(vp.getComponentsCollection());
	}
	
	/**
	 * Inserts the component into existing associated/aggregated components
	 * 
	 * @param componentIdentifier - the component that is going to be added to associated components
	 */
	public void insertComponentIdentifier(T componentIdentifier) {
		this.includedComponents.add(componentIdentifier);
	}
	
	/**
     * Compares the strings according to the matches in the array
     * -strings are delimited according to "|" character and this value is searched as the substring of the string
     * -1 if substring is found otherwise 0
     * 
     * @param s1 - the list of values delimited by "|" that are used as substrings for the search
     * @param s2 - a list of values ​​to search for the substring
     */
	public int compareStrings(String s1, String s2) {
        String array[] = s1.split("|");
        for (int i =0; i < array.length; i++) {
        	if (s2.indexOf(array[i]) != -1) {
        		return 1;
        	}
        }
        return 0;
    }
	
	/**
	 * Inserts the identifiers of components that form the aggregation
	 * 
	 * @param args - the array/enumeration of selected components that form the aggregation
	 */
	public void insertComponentIdentifiers(T ...args) {
		for (T componentIdentifier: args) {
			this.includedComponents.add(componentIdentifier);
		}
	}
	
	/**
	 * Returns the component that form the aggregation according to identifier if available
	 * 
	 * @param componentIdentifier - the identifier/index of component to return it if is available
	 * @return the component to return if is available
	 */
	public boolean checkComponentIdentifier(Integer componentIdentifier) {
		return this.includedComponents.contains(componentIdentifier);
	}
	
	/**
	 * Returns the collection of components that form the aggregation
	 * 
	 * @return the collection of components that form the aggregation
	 */
	public Collection<? extends T> getComponentsCollection() { return this.includedComponents; }
	
	/**
	 * Returns the array of components that form the aggregation
	 * 
	 * @return the array of components that form the aggregation
	 */
	public T[] getComponents() { return (T[]) this.includedComponents.toArray(); }
	
	/**
	 * Check if all component identifiers are part of the component aggregation
	 * 
	 * @param args - the array/enumeration of components identifiers to check their availability
	 * @return true if all component identifiers are part of variability part
	 */
	public boolean checkComponentIdentifiers(T ...args) {
		boolean containsAll = true;
		for (T arg: args) {
			if (!this.includedComponents.contains(arg)) {
				containsAll = false; 
			}
		}
		return containsAll;
	}
	
	/**
	 * Merges component collections across variable parts
	 * -the components from the second instance are inserted into the set of this instance (result instance among variability options)
	 * 
	 *  @param vp - the another existing instance among variability options
	 */
	public void mergeCollections(VariabilityPart<T> vp) {
		for (T componentIdentifier: vp.getComponents()) {
			this.includedComponents.add(componentIdentifier);
		}
	}
	
	/**
	 * Print of values from the aggregation set if these values/components are numerical and represented as integers
	 */
	public void printInteger() {
		ArrayList<Integer> list = new ArrayList<Integer>((Collection<Integer>) this.includedComponents);
		Collections.sort(list);
		Iterator<Integer> identifierIterator = list.iterator();
		while(identifierIterator.hasNext()) {
			System.out.print(Integer.toString(identifierIterator.next()));
		}
		System.out.println();
	}
	
	/**
	 * Returns the identifier of this result instance among variability options
	 * -consisting of string representations of all contained components
	 * 
	 * @return the identifier of this result instance amongst variability options
	 */
	public String getIdentifier() {
		String identifier = "";
		for (Object componentIdentifier: includedComponents) {
			identifier = identifier + "|" + (String) componentIdentifier;
		}
		return identifier.substring(1); 
	}
}
