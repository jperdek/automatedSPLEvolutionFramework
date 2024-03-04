package dividedAstExport.recursionCycleFinder;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import codeConstructsEvaluation.transformation.ASTConverterClient;
import codeConstructsEvaluation.transformation.PostRequester;
import evolutionSimulation.productAssetsInitialization.SharedConfiguration;


/**
 * Tests for functionality to find recursively called code fragments
 * 
 * @author Jakub Perdek
 *
 */
class RecursionCycleFinderTest {

	/**
	 * Test 1: recursively called functions (cycle with length 3) + recursively called classes
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	void test() throws IOException, InterruptedException {
		String filePath = SharedConfiguration.PROJECT_PATH + "/src/dividedAstExport/recursionCycleFinder/recursionTest.txt";
		String fileContent = PostRequester.loadFileContent(filePath);
		JSONObject astRoot = ASTConverterClient.convertFromCodeToASTJSON(fileContent);
		RecursionCycleFinder recursionFinder = new RecursionCycleFinder(astRoot);
		
		assertEquals(recursionFinder.isCycle("m"), true);
		assertEquals(recursionFinder.isCycle("t"), true);
		assertEquals(recursionFinder.isCycle("tt"), true);
		assertEquals(recursionFinder.isCycle("ttt"), true);
		assertEquals(recursionFinder.isCycle("b"), false);
		assertEquals(recursionFinder.isCycle("CC"), true);
		assertEquals(recursionFinder.isCycle("BB"), true);
	}

	/**
	 * Test 2: test on real scenario - fractal-based SPL
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	void test2() throws IOException, InterruptedException {
		String filePath = SharedConfiguration.PROJECT_PATH + "/src/dividedAstExport/recursionCycleFinder/recursionTest2.txt";
		String fileContent = PostRequester.loadFileContent(filePath);
		JSONObject astRoot = ASTConverterClient.convertFromCodeToASTJSON(fileContent);
		RecursionCycleFinder recursionFinder = new RecursionCycleFinder(astRoot);
		//drawAnkletModMain
		assertEquals(recursionFinder.isCycle("getFiveSideShapes"), true);
		assertEquals(recursionFinder.isCycle("drawAnkletModMain"), false);
	}
}
