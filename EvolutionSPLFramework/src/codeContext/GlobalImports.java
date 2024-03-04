package codeContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import codeContext.objects.CodeContextObject;
import codeContext.objects.ImportObject;
import codeContext.objects.SortByCodeContext;


/**
 * Representation of the global import
 * 
 * @author Jakub Perdek
 *
 */
public class GlobalImports {
	
	/**
	 * The list of global imports
	 */
	private List<ImportObject> globalImports; 
	

	/**
	 * Creates the instance to manage global imports
	 */
	public GlobalImports() {
		this.globalImports = new ArrayList<ImportObject>();
	}
	
	/**
	 * Inserts the global import and sorts global imports according to their name
	 * 
	 * @param importName - the name of the global import
	 * @param importPath - the path of the global import
	 * @param position - the position of the import in the AST
	 */
	public void addGlobalImport(String importName, String importPath, long position) {
		ImportObject newGlobalImportObject = new ImportObject(importName, importPath, position);
		this.globalImports.add(newGlobalImportObject);
		Collections.sort(this.globalImports, new SortByCodeContext());
	}
	
	/**
	 * Returns the list of current global imports 
	 * 
	 * @param currentPosition - the current position in the application AST
	 * @return the list of all import before current position
	 */
	public List<ImportObject> getAllActualImport(long currentPosition) {
		int finalLength = Arrays.binarySearch((ImportObject[]) this.globalImports.toArray(new ImportObject[0]), new CodeContextObject(currentPosition));
		return this.globalImports.subList(0, finalLength);
	}
}
