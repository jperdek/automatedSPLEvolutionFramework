package codeConstructsEvaluation;


/**
 * The configuration of the complexity construct
 * 
 * @author Jakub Perdek
 *
 */
public interface ComplexityConstructEvaluationConfiguration {

	/**
	 * Complexity service server url
	 */
	public final static String SERVER_URL = "http://localhost:5001/";
	
	/**
	 * Analyzed entity types
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum AnalyzedEntityType {
		  SERVICE,
		  COMPONENT,
		  DIRECTIVE
	};
	
	/**
	 * The service to evaluate TypeScript code complexity
	 */
	public final static String TYPHON_SERVICE_URL = "analyzeTyphonJS";
	
	/**
	 * The service to evaluate JavaScript code complexity
	 */
	public final static String JAVASCRIPT_SERVICE_URL = "analyzeESLintCC";
	
	/**
	 * The service to evaluate JavaScript code complexity 2
	 */
	public final static String ECMASCRIPT_SERVICE_URL = "analyzeEScomplex";
	
	/**
	 * Service url part to convert code into AST
	 */
	public final static String FROM_CODE_TO_AST_SERVICE_URL = "convert";
	
	/**
	 * Service url part to convert AST into code
	 */
	public final static String FROM_AST_TO_CODE_SERVICE_URL = "convertBack";
	
	/**
	 * Service url part to convert large code fragment into AST
	 */
	public final static String FROM_CODE_TO_LARGE_AST_SERVICE_URL = "convertLarge";
	
	/**
	 * Service url part to convert extensive AST into code
	 */
	public final static String FROM_AST_TO_LARGE_CODE_SERVICE_URL = "convertLargeBack";
	
	/**
	 * Service url part to clear comments from (TypeScript) code
	 */
	public final static String CLEAR_COMMENTS_IN_CODE_SERVICE_URL = "cleanComments";

}
