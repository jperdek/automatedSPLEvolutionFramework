package evolutionSimulation.productAssetsInitialization;


/**
 * Representation of the application resource including
 * -paths to imported scripts
 * -path to used template
 * -paths to imported variables
 * 
 * @author Jakub Perdek
 *
 */
public class Resource {

	/**
	 * The relative path of the resource from project/SPL
	 */
	protected String relativePathFromProject;
	
	/**
	 * Information if the resource represents currently evolved base script, 
	 * true if the resource is currently evolved base script otherwise false
	 */
	protected boolean isBase = false;

	
	/**
	 * Creates the resource with its location (the path to resource)
	 * 
	 * @param relativePathFromProject - the relative path of the resource (some imports can use absolute paths)
	 */
	public Resource(String relativePathFromProject) {
		this.relativePathFromProject = relativePathFromProject;
	}
	
	/**
	 * Creates the resource with its location (the path to resource) and allows to mark it as base/evolved
	 * 
	 * @param relativePathFromProject  - the relative path of the resource from project/SPL(some imports can use absolute paths)
	 * @param isBase - information if the resource represents currently evolved base script, 
	 * true if the resource is currently evolved base script otherwise false
	 */
	public Resource(String relativePathFromProject, boolean isBase) {
		this(relativePathFromProject);
		this.isBase = isBase;
	}
	
	/**
	 * Returns the relative path of the resource from project/SPL
	 * 
	 * @return the relative path of the resource from project/SPL
	 */
	public String getRelativePathFromProject() { return this.relativePathFromProject; }
	
	/**
	 * Returns information if the resource represents currently evolved base script, 
	 * true if the resource is currently evolved base script otherwise false
	 * 
	 * @return information if the resource represents currently evolved base script, 
	 * true if the resource is currently evolved base script otherwise false
	 */
	public boolean isBase() { return this.isBase; }
}
