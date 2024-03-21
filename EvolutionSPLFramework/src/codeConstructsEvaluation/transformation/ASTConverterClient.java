package codeConstructsEvaluation.transformation;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import astFileProcessor.ASTLoader;
import codeConstructsEvaluation.ComplexityConstructEvaluationConfiguration;

/**
 * The helper service to convert AST to Code and back
 * -additional tools to get certain elements from AST are provided
 * 
 * @author Jakub Perdek
 *
 */
public class ASTConverterClient {

	private final static String FROM_CODE_TO_AST_SERVICE_URL = ComplexityConstructEvaluationConfiguration.SERVER_URL 
			+ ComplexityConstructEvaluationConfiguration.FROM_CODE_TO_AST_SERVICE_URL;
	private final static String FROM_AST_TO_CODE_SERVICE_URL = ComplexityConstructEvaluationConfiguration.SERVER_URL 
			+ ComplexityConstructEvaluationConfiguration.FROM_AST_TO_CODE_SERVICE_URL;
	private final static String CLEAR_COMMENTS_FROM_CODE_SERVICE_URL = ComplexityConstructEvaluationConfiguration.SERVER_URL 
			+ ComplexityConstructEvaluationConfiguration.CLEAR_COMMENTS_IN_CODE_SERVICE_URL;
	
	/**
	 * 
	 */
	public ASTConverterClient() {}
	
	public static String clearComments(String codeWithComments) throws IOException, InterruptedException {
		String cleanedCodeFromComments = PostRequester.doPost(ASTConverterClient.CLEAR_COMMENTS_FROM_CODE_SERVICE_URL, codeWithComments);
		return cleanedCodeFromComments;
	}
	
	public static String cleanOneLineIgnoreComments(String fileString) {
		return fileString.replaceAll("//\s*@ts[^\n]+\n", "");
	}
	public static JSONArray getStatementsFromASTFile(JSONObject astFileRoot) {
		return (JSONArray) ((JSONObject) astFileRoot.get("ast")).get("statements");
	}
	
	public static JSONObject getFirstMemberFromASTFileFistStatement(JSONObject astFileRoot) {
		return (JSONObject) ((JSONArray) ((JSONObject) ((JSONArray) ((JSONObject) astFileRoot.get("ast")).get("statements")).get(0)).get("members")).get(0);
	}

	public static JSONArray getMembersFromASTFile(JSONObject astFileRoot, String searchedClassName) {
		JSONArray statements = (JSONArray) ((JSONArray) ((JSONObject) astFileRoot.get("ast")).get("statements"));
		JSONObject fileStatement;
		String statementName;
		for (Object statement: statements) {
			fileStatement = (JSONObject) statement;
			if (fileStatement.containsKey("name")) {
				statementName = (String) ((JSONObject) fileStatement.get("name")).get("escapedText");
				if (statementName.equals(searchedClassName)) {
					return (JSONArray) fileStatement.get("members");
				}
			}
		}
		return null;
	}
	
	public static JSONObject getFirstStatementFromASTFile(JSONObject astFileRoot) {
		return (JSONObject) ((JSONArray) ((JSONObject) astFileRoot.get("ast")).get("statements")).get(0);
	}
	
	public static JSONObject getIthStatementFromASTFile(JSONObject astFileRoot, int index) {
		return (JSONObject) ((JSONArray) ((JSONObject) astFileRoot.get("ast")).get("statements")).get(index);
	}
	
	public static JSONObject convertFromCodeToASTJSON(String fileContent) throws IOException, InterruptedException {
		fileContent = ASTConverterClient.cleanOneLineIgnoreComments(fileContent);
		String convertedToASTString = PostRequester.doPost(ASTConverterClient.FROM_CODE_TO_AST_SERVICE_URL, fileContent);
		return ASTLoader.loadASTFromString(convertedToASTString);
	}

	public static JSONObject convertFromASTToCodeJSON(String fileContent) throws IOException, InterruptedException {
		String convertedToASTString = PostRequester.doPost(ASTConverterClient.FROM_AST_TO_CODE_SERVICE_URL, fileContent);
		return ASTLoader.loadASTFromString(convertedToASTString);
	}
	
	public static String convertFromASTToCode(String fileContent) throws IOException, InterruptedException {
		String convertedToCodeString = PostRequester.doPost(ASTConverterClient.FROM_AST_TO_CODE_SERVICE_URL, fileContent);
		return convertedToCodeString;
	}
	
	public static String convertJSONFromAST(JSONObject expressionPart, boolean formatAnnotationsInLine) throws IOException, InterruptedException {
		String customScheme = "{\"pos\":0,\"end\":87,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":4457472,"
				+ "\"kind\":308,\"statements\":[{\"pos\":0,\"end\":85,\"flags\":0,\"modifierFlagsCache\":0,"
				+ "\"transformFlags\":4457472,\"kind\":240,\"declarationList\":{\"pos\":0,\"end\":84,\"flags\":2,"
				+ "\"modifierFlagsCache\":0,\"transformFlags\":4457472,\"kind\":258,\"declarations\":[{\"pos\":12,"
				+ "\"end\":84,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":257,\"name\":"
				+ "{\"pos\":12,\"end\":30,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":0,\"kind\":79,"
				+ "\"escapedText\":\"EXPRESSION_START0\"},\"initializer\":{\"pos\":32,\"end\":84,\"flags\":0,\"modifierFlagsCache\":0,"
				+ "\"transformFlags\":0,\"kind\":207,\"properties\":"
				+ expressionPart.get("properties").toString()
				+ "}}]}}],\"endOfFileToken\":{\"pos\":85,\"end\":87,\"flags\":0,\"modifierFlagsCache\":0,\"transformFlags\":0,"
				+ "\"kind\":1},\"fileName\":\"x.ts\",\"text\":\"\\r\\n     const EXPRESSION_START0 = {  \\\"pos\\\": 155, \\\"kind\\\": 207 "
				+ " };\\r\\n\",\"languageVersion\":99,\"languageVariant\":0,\"scriptKind\":3,\"isDeclarationFile\":false,\"hasNoDefaultLib\":false,"
				+ "\"bindDiagnostics\":[],\"pragmas\":{},\"referencedFiles\":[],\"typeReferenceDirectives\":[],\"libReferenceDirectives\":[],"
				+ "\"amdDependencies\":[],\"nodeCount\":16,\"identifierCount\":1,\"identifiers\":{},\"parseDiagnostics\":[]}";
		String convertedToASTString = PostRequester.doPost(ASTConverterClient.FROM_AST_TO_CODE_SERVICE_URL, customScheme);
		convertedToASTString = convertedToASTString.substring(convertedToASTString.indexOf("=") + 1);
		if (formatAnnotationsInLine) {
			convertedToASTString = convertedToASTString.replaceAll("\n", "");
		}
		return convertedToASTString;
	}
}
