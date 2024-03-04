package dataRepresentationsExtensions.variabilityPermutations;

import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Functionality to create distinct aggregation of provided names
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class VariabilityMerger {

	/**
	 * Prepares the list of variable cases/aggregations of provided names
	 * 
	 * @param size - the number of elements that are necessary to cover
	 * @return the list of prepared variable cases/aggregations of provided names
	 */
	public static List<VariabilityPart<Integer>> prepareVariabilityCases(int size, int maxSize) {
		//if (size < maxSize) { maxSize = size; }
		List<VariabilityPart<Integer>> results = new ArrayList<VariabilityPart<Integer>>();
		Queue<NumericCouple<VariabilityPart<Integer>>> queue1 = new LinkedList<NumericCouple<VariabilityPart<Integer>>>();
		Queue<NumericCouple<VariabilityPart<Integer>>> queue2 = new LinkedList<NumericCouple<VariabilityPart<Integer>>>();
		Queue<NumericCouple<VariabilityPart<Integer>>> helperQueue;
		VariabilityPart<Integer> combinedElement;
		
		for (int i=0; i<size; i++) {
			combinedElement = new VariabilityPart<Integer>(i);
			results.add(combinedElement);
			queue1.add(new NumericCouple<VariabilityPart<Integer>>(new VariabilityPart<Integer>(i), i));
		}

		int lastElement1Number;
		int nextElement1Number;
		VariabilityPart<Integer> previousElementPart;
		NumericCouple<VariabilityPart<Integer>> processedNumericCouple;
		NumericCouple<VariabilityPart<Integer>> newNumericCouple;
	
		int iteration = 1;
		do {
			while(!queue1.isEmpty()) {
				processedNumericCouple = queue1.poll();
				previousElementPart = (VariabilityPart<Integer>) processedNumericCouple.getIdentifier();
				lastElement1Number = processedNumericCouple.getLastNumber();
				for (nextElement1Number=lastElement1Number + 1; nextElement1Number<size; nextElement1Number++) {
					combinedElement = new VariabilityPart<Integer>(previousElementPart);
					combinedElement.insertComponentIdentifier(nextElement1Number);
					
					newNumericCouple = new NumericCouple<VariabilityPart<Integer>>(combinedElement, nextElement1Number);
					queue2.add(newNumericCouple);
					results.add(combinedElement);
				}
			}
			helperQueue = queue1;
			queue1 = queue2;
			queue2 = helperQueue;
			iteration++;
			if (iteration >= maxSize) { break; }
		} while(!queue1.isEmpty());

		return results;
	}
	
	/**
	 * Main method for observing applicability of the algorithm
	 * 
	 * @param args - arguments of the function - not used
	 */
	public static void main(String args[]) {
		//List<VariabilityPart<Integer>> results = VariabilityMerger.prepareVariabilityCases(4);
		List<VariabilityPart<Integer>> results = VariabilityMerger.prepareVariabilityCases(5, 4);
		//List<VariabilityPart<Integer>> results = VariabilityMerger.prepareVariabilityCases(2);
		Iterator<VariabilityPart<Integer>> resultIterator = results.iterator();
		while(resultIterator.hasNext()) {
			resultIterator.next().printInteger();
		}
	}
}
