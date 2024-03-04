package evolutionSimulation.productAssetsInitialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Initializes the custom product assets
 * 
 * @author Jakub Perdek
 *
 */
public class CustomProductAssetsInitiator {

	/**
	 * The list of base resource directories paths
	 */
	protected List<String> baseResourceDirectoriesPaths;
	
	/**
	 * Mapped of resource paths (unique identifiers) to resources 
	 */
	protected Map<String, Set<Resource>> usedResourceToDependenciesMap = new HashMap<String, Set<Resource>>();
	

	/**
	 * Creates custom initiator for used resources/assets in product
	 */
	public CustomProductAssetsInitiator() {
		this.baseResourceDirectoriesPaths = new ArrayList<String>();
	}
	
	/**
	 * Creates custom initiator for used resources/assets in product with base path
	 * 
	 * @param baseResourceDirectoryPath - the path to associated resources 
	 */
	public CustomProductAssetsInitiator(String baseResourceDirectoryPath) {
		this();
		this.baseResourceDirectoriesPaths.add(baseResourceDirectoryPath);
	}
	
	/**
	 * Creates custom initiator for used resources/assets in product with the list of base paths
	 * 
	 * @param baseResourceDirectoryPaths - the list of associated resources
	 */
	public CustomProductAssetsInitiator(List<String> baseResourceDirectoryPaths) {
		this();
		this.baseResourceDirectoriesPaths.addAll(baseResourceDirectoryPaths);
	}
	
	/**
	 * Creates child resource and stores it under head parent resource
	 * 
	 * @param headResource - the parent resource that will be stored
	 * @param childResourcePath - the path to child resource that will be created
	 */
	public void addResource(Resource headResource, String childResourcePath) {
		Resource childResource = new Resource(childResourcePath);
		this.addResource(headResource, childResource);
	}
	
	/**
	 * Associates child resource under provided parent resource
	 * - if head resource has not associated structure to store the child resources then this structure will be created
	 * 
	 * @param headResource - the parent resource is going to be associate with child resource
	 * @param childResource - the child resource that is going to be associated with headResource
	 */
	public void addResource(Resource headResource, Resource childResource) {
		Set<Resource> resources;
		if (this.usedResourceToDependenciesMap.containsKey(headResource.getRelativePathFromProject())) {
			resources = this.usedResourceToDependenciesMap.get(headResource.getRelativePathFromProject());
		} else {
			resources = new HashSet<Resource>();
		}
		resources.add(childResource);
		this.usedResourceToDependenciesMap.put(headResource.getRelativePathFromProject(), resources);
	}
}
