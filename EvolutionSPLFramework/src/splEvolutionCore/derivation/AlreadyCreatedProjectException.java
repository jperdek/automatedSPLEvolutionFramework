package splEvolutionCore.derivation;

import java.util.HashSet;
import java.util.Set;


/**
 * Functionality to prevent creation of already created projects/SPL according to their hash
 * 
 * @author Jakub Perdek
 *
 */
public class AlreadyCreatedProjectException extends Exception {

	/**
	 * Stored hashes
	 */
	private static Set<String> existingProjectHashes = new HashSet<String>();
	
	/**
	 * Verifies if project/SPL is already created
	 * -usually it is verified across many evolution stages
	 * 
	 * @param projectHash - hash of given project/SPL
	 * @return true if project is already created otherwise false
	 */
	public static boolean alreadyExists(String projectHash) { return AlreadyCreatedProjectException.existingProjectHashes.contains(projectHash); }
	
	/**
	 * Adds a hash of given project/SPL to set of hashes
	 * 
	 * @param projectHash - hash of given project/SPL
	 */
	public static void addHash(String projectHash) { AlreadyCreatedProjectException.existingProjectHashes.add(projectHash); }
	
	/**
	 * Serial Version UID for AlreadyCreatedProjectException exception
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Exception thrown when project/SPL is already created
	 * 
	 * @param message - associated message with already created project/SPL
	 */
	public AlreadyCreatedProjectException(String message) {
		super(message);
	}
}
