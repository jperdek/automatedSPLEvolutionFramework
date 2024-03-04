package positiveVariabilityManagement;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;
import splEvolutionCore.DebugInformation;


/**
 * Responsible to copy the whole project assets to the specified target location
 * -under given directory to the specified target location
 * 
 * @author Jakub Perdek
 *
 */
public class ProjectCopier {

	/**
	 * Copies the whole project assets to the specified target location
	 * 
	 * @param pathToProjectTree - source location from where project assets are copied
	 * @param pathToNewProject - destination location where project assets are copied
	 * @param allowOverwriting - information if overwriting is allowed, if true then assets will be overwritten otherwise exception is thrown
	 */
	public static void copyExistingProject(String pathToProjectTree, String pathToNewProject, boolean allowOverwriting) {
		Stream<Path> s = null;
		if (!pathToProjectTree.contains("file:///")) {
			pathToProjectTree = "file:///" + pathToProjectTree.replace("://", ":/");
		}
		if (!pathToNewProject.contains("file:///")) {
			pathToNewProject = "file:///" + pathToNewProject.replace("://", ":/");
		}
		if (!pathToNewProject.endsWith("/")) {
			pathToNewProject = pathToNewProject + "/";
		}
		
		try {
			Path inputPath1 = Path.of(URI.create(pathToProjectTree));
			int baseLength = inputPath1.toUri().getRawPath().length();
			s = Files.walk(inputPath1);
			Iterator<Path> paths = s.iterator();
			while(paths.hasNext()) {
				Path actualPath = paths.next();
				String newDirectoryOrFileString = actualPath.toUri().getRawPath().substring(baseLength);
				String outputDirectoryOrFileString = pathToNewProject + newDirectoryOrFileString;
				if(Files.isDirectory(actualPath)) {
					Path outputDirectoryPath = Path.of(URI.create(outputDirectoryOrFileString));
					try {
						Files.createDirectory(outputDirectoryPath);
					} catch(FileAlreadyExistsException e) {
						if (!allowOverwriting) { throw e; }
					}
				} else {
					Path outputDirectoryPath = Path.of(URI.create(outputDirectoryOrFileString));
					//String baseInputPath = pathToProjectTree.substring(pathToProjectTree.indexOf("C:/"));
					//String baseOutputPath = pathToNewProject.substring(pathToProjectTree.indexOf("C:/"));
					try {
						if (DebugInformation.SHOW_INITIAL_COPIED_INFORMATION) {
							System.out.println("FROM: " + actualPath);
							System.out.println("TO  : " + outputDirectoryPath);
						}
						Files.copy(actualPath, outputDirectoryPath);
					} catch(FileAlreadyExistsException e) {
						if (!allowOverwriting) { throw e; }
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(s != null) { s.close(); }
		}
	}
}
