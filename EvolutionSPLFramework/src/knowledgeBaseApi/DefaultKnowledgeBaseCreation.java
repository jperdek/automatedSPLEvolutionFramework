package knowledgeBaseApi;

import static org.junit.jupiter.api.DynamicTest.stream;

import java.io.IOException;

import org.apache.http.NoHttpResponseException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codeConstructsEvaluation.MethodComplexityRecord;
import codeConstructsEvaluation.transformation.PostRequester;
import evolutionSimulation.productAssetsInitialization.SharedConfiguration;


/**
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class DefaultKnowledgeBaseCreation {

	private static final String DEFAULT_KNOWLEDGE_BASE_SERVER = "http://" + System.getenv().getOrDefault("DOCKER_HOST", "localhost")+ ":" + 
			System.getenv().getOrDefault("KNOWLEDGE_EXTRACTOR_SERVER_PORT", "5000") + "/api/knowledge-base";
	private static final String INIT_DEFAULT_KNOWLEDGE_BASE = DefaultKnowledgeBaseCreation.DEFAULT_KNOWLEDGE_BASE_SERVER + "/init";
	private static final String CLEAR_DEFAULT_KNOWLEDGE_BASE = DefaultKnowledgeBaseCreation.DEFAULT_KNOWLEDGE_BASE_SERVER + "/clear";
	private static final String REGISTER_NEW_PRODUCT = DefaultKnowledgeBaseCreation.DEFAULT_KNOWLEDGE_BASE_SERVER + "/registerNewProduct";
	private static final String REGISTER_NEW_EVOLUTION = DefaultKnowledgeBaseCreation.DEFAULT_KNOWLEDGE_BASE_SERVER + "/registerNewEvolution";
	private static final String INSERT_TRIPLES = DefaultKnowledgeBaseCreation.DEFAULT_KNOWLEDGE_BASE_SERVER + "/addTriples";
	
	/**
	 * Logger to track information from method complexity record
	 */
	private static final Logger logger = LoggerFactory.getLogger(DefaultKnowledgeBaseCreation.class);
	

	public static void initializeDefaultKnowledgeBase() {
		if (SharedConfiguration.BUILD_DEFAULT_KNOWLEDGE_BASE) {
			try {
				logger.info("Initializing default knowledge base.");
				String response = PostRequester.doGetRequest(DefaultKnowledgeBaseCreation.INIT_DEFAULT_KNOWLEDGE_BASE);
				logger.info("Initialized with response: " + response);
			} catch (NoHttpResponseException e) {
				logger.error("Knowledge base is probably down.", e);
				if (!e.toString().contains("The graph is non-empty.") && System.getenv().getOrDefault("SHOUD_TERMINATE_IF_KNOWLEDGE_BASE_IS_NOT_INITIALIZED", "false").equals("true")) {
					System.exit(5);
				}
			} catch (IOException e) {
				logger.error("Failed to initialize default knowledge base.", e);
				if (!e.toString().contains("The graph is non-empty.") && System.getenv().getOrDefault("SHOUD_TERMINATE_IF_KNOWLEDGE_BASE_IS_NOT_INITIALIZED", "false").equals("true")) {
					System.exit(5);
				}
			} catch (InterruptedException e) {
				logger.error("Failed to initialize default knowledge base.", e);
				if (!e.toString().contains("The graph is non-empty.") && System.getenv().getOrDefault("SHOUD_TERMINATE_IF_KNOWLEDGE_BASE_IS_NOT_INITIALIZED", "false").equals("true")) {
					System.exit(5);
				}
			} 
		}
	}
	
	public static void clearDefaultKnowledgeBase() {
		if (SharedConfiguration.BUILD_DEFAULT_KNOWLEDGE_BASE) {
			try {
				logger.info("Clearing default knowledge base.");
				String response = PostRequester.doGetRequest(DefaultKnowledgeBaseCreation.CLEAR_DEFAULT_KNOWLEDGE_BASE);
				logger.info("Cleared with response: " + response);
			} catch (IOException e) {
				logger.error("Failed to initialize default knowledge base.", e);
			} catch (InterruptedException e) {
				logger.error("Failed to initialize default knowledge base.", e);
			}
		}
	}
	
	public static void registerNewEvolution(String evolutionId, String initialProductLineId, 
			String evolvedScriptPath, String previousProductLineId, String previousEvolutionId, String evolutionConfigurationPath) {
		if (SharedConfiguration.BUILD_DEFAULT_KNOWLEDGE_BASE) {
			JSONObject newEvolutionRequestData = new JSONObject();
			newEvolutionRequestData.put("evolution_id", evolutionId);
			newEvolutionRequestData.put("initial_product_line_id", initialProductLineId);
			newEvolutionRequestData.put("evolved_script_path", evolvedScriptPath);
			newEvolutionRequestData.put("previous_product_line_id", previousProductLineId);
			newEvolutionRequestData.put("previous_evolution_id", previousEvolutionId);
			newEvolutionRequestData.put("evolution_configuration_path", evolutionConfigurationPath);
			
			try {
				logger.info("Creating default knowledge of new evolution");
				String response = PostRequester.doPostRequest(DefaultKnowledgeBaseCreation.REGISTER_NEW_EVOLUTION, newEvolutionRequestData.toString());
				logger.info("Default knowledge of new evolution is created with response: " + response);
			} catch (IOException e) {
				logger.error("Failed to initialize default knowledge base.", e);
			} catch (InterruptedException e) {
				logger.error("Failed to initialize default knowledge base.", e);
			}
		}
	}
	
	public static void registerNewProduct(String evolvedProductLineId, String codePath, 
			String screenshotPath, String vectorPath, String jsonGraphPath) {
		if (SharedConfiguration.BUILD_DEFAULT_KNOWLEDGE_BASE) {
			JSONObject newEvolutionRequestData = new JSONObject();
			newEvolutionRequestData.put("evolved_product_line_id", evolvedProductLineId);
	
			newEvolutionRequestData.put("code_path", codePath);
			newEvolutionRequestData.put("screenshot_path", screenshotPath);
			newEvolutionRequestData.put("vector_path", vectorPath);
			newEvolutionRequestData.put("json_graph_path", jsonGraphPath);
			
			try {
				logger.info("Creating default knowledge about actually derived new product.");
				String response = PostRequester.doPostRequest(DefaultKnowledgeBaseCreation.REGISTER_NEW_PRODUCT, newEvolutionRequestData.toString());
				logger.info("Default knowledge about actually derived new product is created with response: " + response);
			} catch (IOException e) {
				logger.error("Failed to initialize default knowledge base.", e);
			} catch (InterruptedException e) {
				logger.error("Failed to initialize default knowledge base.", e);
			}
		}
	}
	
	public static String getDefaultHeaders(String baseHeader) {
		if (baseHeader == null) {
			baseHeader = "@base <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .";
		}
		return baseHeader + """
			@prefix faspls: <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .
            @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
            @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
            
		""";
	}
	
	public static void insertTriples(String triples) {
		if (SharedConfiguration.BUILD_DEFAULT_KNOWLEDGE_BASE) {
			try {
				logger.info("Inserting triples.");
				String response = PostRequester.doPostRequest(DefaultKnowledgeBaseCreation.INSERT_TRIPLES, triples);
				logger.info("Insertion of triples finished with response: " + response);
			} catch (IOException e) {
				logger.error("Failed to initialize default knowledge base.", e);
			} catch (InterruptedException e) {
				logger.error("Failed to initialize default knowledge base.", e);
			}
		}
	}
}
