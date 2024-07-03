package codeContext.processors.export;

import java.util.List;

import codeContext.InnerContext;


/**
 * Prescribes contract for the exported context
 * 
 * @author Jakub Perdek
 *
 */
public interface ExportedContextInterface {

	/**
	 * Searches for variables, entities such as classes and methods and associates the exported dependencies if found
	 * 
	 * @param exportName - the list of export names
	 * @param exportAggregator - the used aggregator to aggregate used exports
	 */
	public void findContextToExportMapping(List<String> exportName, ExportAggregator exportAggregator);
			
	/**
	 * Creates proper export context for entities marked as exported during creation of hierarchy from entities
	 * 
	 * @param exportAggregator - the used aggregator to aggregate used exports
	 * @param fileName - the name of processed file
	 * @param baseInnerContext - the associated base/root inner context with the exported object - pointing to hierarchy of inner contexts
	 */
	public void markDirrectExportMapping(ExportAggregator exportAggregator, String fileName, InnerContext baseInnerContext);
	
	/**
	 * Searches for default export according to provided boundaries
	 * 
	 * @param initialPosition - the initial/starting position of default export in AST
	 * @param terminatingPosition - the terminal/ending position of default export in AST
	 * @return the exported object with type of ExportedObjectInterface if found otherwise null
	 */
	public ExportedObjectInterface findDefaultExport(Long initialPosition, Long terminatingPosition);
	
	/**
	 * Returns the exported object according to its type
	 * 
	 * @param innerObjectType - the type of object used to select object
	 * @return the exported object according to its type 
	 */
	public ExportedObjectInterface getExtendableInnerObjectAccordingToType(String innerObjectType);
	
	/**
	 * Returns the export represented as string
	 * 
	 * @return the export represented as string
	 */
	public String getCallableStr();
}
