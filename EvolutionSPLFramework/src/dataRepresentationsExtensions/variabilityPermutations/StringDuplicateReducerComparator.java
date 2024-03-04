package dataRepresentationsExtensions.variabilityPermutations;

import java.util.Comparator;

/**
 * Comparator of strings according to the matches in the array
 * -strings are delimited according to "|" character and this value is searched as the substring of the string
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class StringDuplicateReducerComparator implements Comparator<String> {
	    
	@Override
    /**
     * Compares the strings according to the matches in the array
     * -strings are delimited according to "|" character and this value is searched as the substring of the string
     * -1 if substring is found otherwise 0
     * 
     * @param s1 - the list of values delimited by "|" that are used as substrings for the search
     * @param s2 - a list of values ​​to search for the substring
     */
    public int compare(String s1, String s2) {
        String array[] = s1.split("|");
        for (int i = 0; i < array.length; i++) {
        	if (s2.indexOf(array[i]) != -1) {
        		return 1;
        	}
        }
        return 0;
    }
}
