package codeContext.persistence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;


/**
 * Persists content in file - creates file
 * 
 * @author Jakub Perdek
 *
 */
public class UpdatedTreePersistence {

	/**
	 * Persists string/text in file
	 * 
	 * @param pathToFile - path to file that will be created, where content is going to be persisted
	 * @param textToPersist - text that is going to be persisted
	 */
	public static void persistsAstInFile(String pathToFile, String textToPersist) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try {
			fw = new FileWriter(pathToFile);
			bw = new BufferedWriter(fw);
			bw.append(textToPersist);
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) { bw.close(); }
				if (fw != null) { fw.close(); }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Persists AST in file
	 * 
	 * @param pathToFile - path to file that will be created, where content is going to be persisted
	 * @param astTree - JSON object containing AST
	 */
	public static void persistsAstInFile(String pathToFile, JSONObject astTree) {
		UpdatedTreePersistence.persistsAstInFile(pathToFile, astTree.toString());
	}
}
