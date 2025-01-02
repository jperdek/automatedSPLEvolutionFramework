package positiveVariabilityManagement;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * Logger to track copying of project files (deletion, moving, copying, ...) 
	 */
	private static final Logger logger = LoggerFactory.getLogger(ProjectCopier.class);
	
	/**
	 * Deletes empty directories when files from them are moved to another location
	 * 
	 * @param inputPath - the input path to the root of moved project directory
	 * @throws IOException
	 */
	private static void deleteEmptyDirectoriesDuringMoving(Path inputPath) throws IOException {
		String actualDirectoryOrFileString;
		Stream<Path> stream = Files.walk(inputPath);
		Iterator<Path> paths = stream.sorted(Collections.reverseOrder()).iterator();
		File directory;
		
		while(paths.hasNext()) { 
			Path actualPath = paths.next();
	
			if(Files.isDirectory(actualPath)) {
				actualDirectoryOrFileString = actualPath.toAbsolutePath().toString();
				directory = new File(actualDirectoryOrFileString);
				if (directory.listFiles() == null || directory.listFiles().length == 0) {
					logger.debug("Deleting directory: " + actualDirectoryOrFileString);
					Files.delete(actualPath); 
				} else { 
					logger.debug("Not empty directory " + actualDirectoryOrFileString + " cannot be deleted.");
				}
			}
		}
		stream.close();
	}
	/**
	 * Copies the whole project assets to the specified target location
	 * 
	 * @param pathToProjectTree - source location from where project assets are copied
	 * @param pathToNewProject - destination location where project assets are copied
	 * @param allowOverwriting - information if overwriting is allowed, if true then assets will be overwritten otherwise exception is thrown
	 * @param moveOnly - true if files are moved only otherwise false
	 */
	public static void copyExistingProject(String pathToProjectTree, String pathToNewProject, boolean allowOverwriting, boolean moveOnly) {
		Stream<Path> s = null;
		pathToProjectTree = pathToProjectTree.replace('\\', '/');
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
				outputDirectoryOrFileString = outputDirectoryOrFileString.replace('\\', '/');
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
							if (moveOnly) {
								logger.info("MOVING FROM: " + actualPath);
								logger.info("MOVING TO  : " + outputDirectoryPath);
							} else {
								logger.info("FROM: " + actualPath);
								logger.info("TO  : " + outputDirectoryPath);
							}
						}
						if (moveOnly) {
							Files.move(actualPath, outputDirectoryPath, StandardCopyOption.REPLACE_EXISTING);
						} else {
							Files.copy(actualPath, outputDirectoryPath);
						}
					} catch(FileAlreadyExistsException e) {
						if (!allowOverwriting) { throw e; }
					}
				}
			}
			if (moveOnly) { ProjectCopier.deleteEmptyDirectoriesDuringMoving(inputPath1); }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(s != null) { s.close(); }
		}
	}
}
