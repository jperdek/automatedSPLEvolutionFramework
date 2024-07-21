package codeContext.processors.export;

import codeContext.InnerContext;


/**
 * Carries information and functionality connected with currently evolved script
 * -allows to restrict generation of final imports
 *  
 * 
 * @author Jakub Perdek
 *
 */
public class ExportedContextFromCurrentlyEvolvedScript extends ExportedContext {

	/**
	 * Creates the instance of exported context with information and functionality connected with currently evolved script
	 * 
	 * @param exportedObject - the processed object that is exported
	 * @param fileName - the name of the file where script is exported/harvested
	 * @param baseInnerContext - the associated base/root inner context with the exported object - pointing to hierarchy of inner contexts
	 */
	public ExportedContextFromCurrentlyEvolvedScript(ExportedObjectInterface exportedObject, String fileName,
			InnerContext baseInnerContext) {
		super(exportedObject, fileName, baseInnerContext);
	}

}
