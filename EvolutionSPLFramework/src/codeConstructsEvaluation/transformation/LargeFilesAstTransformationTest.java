package codeConstructsEvaluation.transformation;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

/**
 * Testing sending large files by saving them on the shared space
 * 
 * @author Jakub Perdek
 *
 */
class LargeFilesAstTransformationTest {

	@Test
	void test() throws IOException, InterruptedException {
		String fileContent = new String(Files.readAllBytes(Paths.get("./src/codeConstructsEvaluation/transformation/testAST.txt")));
		String code = ASTConverterClient.convertFromASTToCode(fileContent, true);
		JSONObject resultingObject = ASTConverterClient.convertFromCodeToASTJSON(code, true);
	}

}
