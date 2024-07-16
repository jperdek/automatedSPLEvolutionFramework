package divisioner;

import java.util.HashSet;
import java.util.Set;
import java.util.List;


/**
 * Configuration of divisions into variation points in processed SPL
 * 
 * @author Jakub Perdek
 *
 */
public interface VariationPointDivisionConfiguration {

	public static final boolean LONG_CONTEXT_PRINT = true;
	public static final boolean ALLOW_POSITIVE_VARIABILITY = true;  // allows to manage concerns related to positive variability
	public static final boolean ALLOW_NEGATIVE_VARIABILITY = true;	// allows to manage concerns related to negative variability
	public static final boolean GENERATE_ALL_FUNCTION_PARAMETERS = false;
	public static final String MARKER_VP_NAME = "markerVP"; 		// positive variation point markers
	public static final String ANNOTATION_OF_VP = "AnnotationVP"; 	// negative variation point system annotation
	public static final String PARAM_VP = "paramVP";				// positive variation point parameter marker
	public static final boolean INJECT_UNSUPPORTED_VP_MARKS = true;
	public static final boolean ALLOW_NEGATIVE_VARIABILITY_PARAMETERS = false;
	
	public static final boolean USE_PARAMETERIZED_FORM = true;
	public static final String PARAMETERIZED_FORM_START = "[%[";		//start marker for function/template parameters
	public static final String PARAMETERIZED_FORM_DELIMITER = "|%|";	//delimiter marker for function/template parameters
	public static final String PARAMETERIZED_FORM_END = "]%]";			//end marker for function/template parameters
	public static final boolean USE_TYPES = true;
	public static final boolean PRESERVE_MULTIPLE_USER_ANNOTATIONS = false;
	public static final boolean REMOVE_ALL_VARIABILITY_ANNOTATIOS_BEFORE_UPDATE = true; //otherwise removal will be applied locally
	public static final boolean PREFER_POSITION_UPDATES_BEFORE_PERSISTING_ILLEGAL_DECORATORS_INFORMATION = false; //will convert Ast to code and back
	public static final boolean OPTIONALLY_MERGE_ILLEGAL_DECORATORS_DURING_SELECTION = true; // uses illegal decorators if are available - are merged with legal, but marked as illegal
	public static final boolean PRESERVE_USER_ANNOTATION_IF_EXISTS_ONLY = true;
	public static final boolean PRESERVE_AUTOMATICALLY_ANNOTATED_NEGATIVE_VARIABILITY_PLACES = false; //preserves all negative variability annotations
	//DEFINE OWN ANNOTATIONS
	final static Set<String>  RESERVED_ANNOTATIONS = Set.of("wholeEntity", 
			"wholeInitlialization", "variableDeclaration", "wholeBlock", "wholeBlock2", "skipLine");
	final static Set<String> RESERVED_ANGULAR_ANNOTATIONS = Set.of(
			"wholeBlockFile", "wholeBlockMethod", "skipLineParameter", "skipLineVariableDeclaration", 
			"skipLineClassVariableDeclaration", "skipLineFile");
	
	public static final String USER_GLOBAL_VARIABLE_DECLARATION_ANNOTATION_NAME = "variableDeclarationGlobal";
	public static final String USER_LOCAL_VARIABLE_DECLARATION_ANNOTATION_NAME = "variableDeclarationLocal";
	public static final String USER_CLASS_VARIABLE_DECLARATION_ANNOTATION_NAME = "variableDeclarationClass";
	public static final String USER_CLASS_DECLARATION_ANNOTATION_NAME = "wholeClass";
	public static final String USER_METHOD_ANNOTATION_NAME = "wholeBlockMethod";
	public static final String USER_CLASS_METHOD_ANNOTATION_NAME = "wholeClassMethod";
	public static final String USER_VARIABLE_PARAMETER = "variableParameter";
	public static final String USER_VARIABLE_CONSTRUCTOR_PARAMETER = "variableParameter";
	final static Set<String> RESERVED_SEMANTIC_ANNOTATIONS = Set.of(List.of(USER_GLOBAL_VARIABLE_DECLARATION_ANNOTATION_NAME,
			USER_LOCAL_VARIABLE_DECLARATION_ANNOTATION_NAME, USER_CLASS_VARIABLE_DECLARATION_ANNOTATION_NAME,
			USER_CLASS_DECLARATION_ANNOTATION_NAME, USER_METHOD_ANNOTATION_NAME, USER_CLASS_METHOD_ANNOTATION_NAME, 
			USER_VARIABLE_PARAMETER, USER_VARIABLE_CONSTRUCTOR_PARAMETER, "skipLine").stream().distinct().toArray(String[] :: new)); 
	//"skipLine" - in code decorators cannot be used directly for this except declaration of supporting 
	//variable or using constructs from positive variability - RECURSIVE DERIVATOR ANNOTATIONS SHOULD BE USED FOR THIS PURPOSE
	
	//PUT DEFINED VALUES HERE IN PLACE OF RESERVED_SEMANTIC_ANNOTATIONS
	public final static Set<String> RESERVED_SELECTED_ANNOTATIONS = new HashSet<String>(RESERVED_SEMANTIC_ANNOTATIONS);
}
