package codeContext.processors;

import org.json.simple.JSONObject;
import codeContext.ClassContext;
import codeContext.CodeContext;
import codeContext.FunctionContext;
import codeContext.GlobalContext;
import codeContext.InnerContext;


/**
 * Initialization, configuration, and processing of contexts, including harvesting their information
 * 
 * @author Jakub Perdek
 *
 */
public class ASTContextProcessor {
	
	/**
	 * Enumeration to specify the search direction to optimize/adapt search in the hierarchy of contexts
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum SearchPositions {
		BEGIN("pos"),
		END("end");
		
		/**
		 * The AST identifier (field/key) of used position
		 */
		public final String label;

		/**
		 * Creates the search position with the AST identifier (field/key)
		 * 
		 * @param label - the AST identifier (field/key) of used position
		 */
	    private SearchPositions(String label) {
	        this.label = label;
	    }
	}
	
	/**
	 * Chosen search position - the end position fits the all current tasks and START may not work
	 */
	public final static String SEARCH_POSITION = SearchPositions.END.label; 
	

	/**
	 * Creates the instance of AST processor to process contexts
	 */
	public ASTContextProcessor() {}

	/**
	 * Initializes the code contexts under CodeContext entity including GlobalContext and hierarchy of inner contexts
	 * 
	 * @param astRoot - the root of application AST
	 * @param analyzedAstPlace - the place in AST where imports are stored, usually its the root of AST (the root is also set - astRoot)
	 * @param fileName - the name of processed script name to initialize initial CodeContext as aggregation of global and inner contexts
	 * @param useTypes - information if the types are used - true if types are used otherwise not
	 * @return aggregation of global and inner contexts under CodeContext instance
	 */
	public static CodeContext initializeCodeContext(JSONObject astRoot, JSONObject analyzedAstPlace, 
			String fileName, boolean useTypes) {
		GlobalContext globalContext = new GlobalContext();
		InnerContext innerContext = new InnerContext(null, 0, (Long) ((JSONObject) astRoot.get("endOfFileToken")).get("end"), useTypes, false);
		CodeContext initialCodeContext = new CodeContext(globalContext, innerContext, fileName);
		ImportProcessor.processImportsInAstPart(astRoot, analyzedAstPlace, initialCodeContext);
		return initialCodeContext;
	}
	
	/**
	 * Processes the variables and parameters of/in given method
	 * 
	 * @param globalContext - global context - accessible in all places (such as variables declared as var in JavaScript)
	 * @param innerContext - the actual inner context in the hierarchy of inner contexts
	 * @param astRoot - the root of application AST
	 * @param analyzedAstPlace - the actual observed place in AST under inner context
	 * @throws NotFoundVariableDeclaration
	 */
	public static void evaluateTreeStep(GlobalContext globalContext, InnerContext innerContext, JSONObject astRoot, JSONObject analyzedAstPlace) throws NotFoundVariableDeclaration {
		VariableProcessor.processVariablesInAstPart(globalContext, innerContext, astRoot, analyzedAstPlace);		// variables global/local
		ParameterProcessor.processVariablesInAstPart(innerContext, analyzedAstPlace, astRoot);						// method parameters
	}
	
	/**
	 * Processes the AST member - related to class in AST
	 * -loads/associates class variables with the class (inner context)
	 * -does not load/associates functions with the class (inner context) - each function is separed entity in the hierarchy of inner contexts!!!
	 * 
	 * @param innerContext - the actual inner context in the hierarchy of inner contexts
	 * @param analyzedAstMemberPlace - the actual observed place in AST under class that was observed as type of newly created inner content
	 * @param astRoot - the root of application AST
	 * @param notFunctions true if functions should be omitted otherwise not
	 */
	public static void evaluateTreeMember(InnerContext innerContext, JSONObject analyzedAstMemberPlace, JSONObject astRoot, boolean notFunctions) {
		if (innerContext instanceof ClassContext) {
			MemberProcessor.processActualMemberInAstPart((ClassContext) innerContext, analyzedAstMemberPlace, astRoot, notFunctions); 		// class members/attributes
		}
	}
}
