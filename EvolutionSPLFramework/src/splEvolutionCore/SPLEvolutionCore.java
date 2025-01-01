package splEvolutionCore;

import java.util.List;


/**
 * All configuration variables related to evolution core
 * -to configure base evolution configuration entity with constants
 * -to configure optional data extensions inclusions into final derivations
 * 
 * @author Jakub Perdek
 *
 */
public interface SPLEvolutionCore {
	
	public static final boolean SHOW_ALL_SYSTEM_ANNOTATIONS_ON_DEBUG = true;
	
	/**
	 * Is evolution should be applied in form of HTML template //false is not directly supported
	 */
	public static final boolean APPLY_TO_TEMPLATE = true;
	
	/**
	 * Strategies to manage the divisions into positive and negative variability in the whole evolution
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum VariationPointDivisionConfigurationStrategies {
		RECALL_DIVISIONING, 
		PERFORMANCE_OPTIMIZED, CUSTOM_USER //not implemented
	}
	
	/**
	 * Selected strategy to manage divisions into positive and negative variability, the number of perceived variation points during search phase (for them)
	 *
	 */
	public static VariationPointDivisionConfigurationStrategies SELECTED_VARIATION_POINT_DICISION_CONFIGURATION_STRATEGY = 
			VariationPointDivisionConfigurationStrategies.RECALL_DIVISIONING;
	

	/**
	 * Strategies to extract calls from variation points
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum ExtractedCallsFromPositiveVariationPointCreatorStrategies {
		EXTRACT_ALL_CALLS, EXTRACT_ALL_EXCEPT_RECURSION_CALLS, PRIORITY_BASED_EXTRACTION, 
		TYPE_BASED_EXTRACTION, NONE_ENTITY_CREATION, EXTRACT_RECURSION_CALLS_ONLY
	}
	
	/**
	 * Strategies to extract calls from template 
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum ExtractedCallsInstantiationFromTemplateStrategies {
		ALL_SUBSTITUTIONS, UNIQUE_PARAMETER_SUBSTITUTION, SIMILARITY_BASED_SUBSTITUTION, WRAPPER_BASED_SUBSTITUTION
	}
	
	/**
	 * Strategies to manage matching/substitution/insertion of variables based on parameters
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum ParameterMatchingStrategyStrategies {
		MATCH_ALL, SIMILARITY_BASED_MATCHING
	}
	
	/**
	 * Strategies that can be applied to manage value assignment during use of various measurements related to negative variability
	 *  
	 * @author Jakub Perdek
	 *
	 */
	public static enum NEGATIVE_VARIABILITY_VALUE_ASSIGNMENT_STRATEGIES { COMPLEXITY_VALUE_ASSIGNMENT_NEGATIVE_VARIABILITY }
	
	/**
	 * Strategies that can be applied to manage value assignment during use of various measurements related to positive variability
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum POSITIVE_VARIABILITY_VALUE_ASSIGNMENT_STRATEGIES { COMPLEXITY_VALUE_ASSIGNMENT_POSITIVE_VARIABILITY }
	
	/**
	 *	 Strategies that are applied to manage value assignment during use of various measurements related to negative variability
	 */
	public static List<NEGATIVE_VARIABILITY_VALUE_ASSIGNMENT_STRATEGIES> SELECTED_NEGATIVE_VARIABILITY_VALUE_STRATEGIES = 
			List.of(NEGATIVE_VARIABILITY_VALUE_ASSIGNMENT_STRATEGIES.COMPLEXITY_VALUE_ASSIGNMENT_NEGATIVE_VARIABILITY);
	
	/**
	 *	 Strategies that are applied to manage value assignment during use of various measurements related to positive variability
	 */
	public static List<POSITIVE_VARIABILITY_VALUE_ASSIGNMENT_STRATEGIES> SELECTED_POSITIVE_VARIABILITY_VALUE_STRATEGIES = 
			List.of(POSITIVE_VARIABILITY_VALUE_ASSIGNMENT_STRATEGIES.COMPLEXITY_VALUE_ASSIGNMENT_POSITIVE_VARIABILITY);

	/**
	 *  Selected strategy to manage selection of positive variability callable templates
	 */
	public static ExtractedCallsFromPositiveVariationPointCreatorStrategies SELECTED_POSITIVE_VARIABILITY_CALLS_CREATOR_STRATEGY =
			ExtractedCallsFromPositiveVariationPointCreatorStrategies.EXTRACT_RECURSION_CALLS_ONLY; //ExtractedCallsFromPositiveVariationPointCreatorStrategies.NONE_ENTITY_CREATION;
	
	/**
	 *  Selected strategy to manage selection of callable constructs instantiated from templates
	 */
	public static ExtractedCallsInstantiationFromTemplateStrategies SELECTED_EXTRACTOR_CALLS_INSTANTION_FROM_TEMPLATE_STRATEGY = 
			ExtractedCallsInstantiationFromTemplateStrategies.WRAPPER_BASED_SUBSTITUTION;
	
	/**
	 *  Selected strategy to manage selection of parameters
	 */
	public static ParameterMatchingStrategyStrategies SELECTED_PARAMETER_MATCHING_STRATEGY = 
			ParameterMatchingStrategyStrategies.SIMILARITY_BASED_MATCHING;
	
	
	/**
	 * Strategies to manage variation points selection
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum FeatureSelectionStrategies { //constant==fixed number
		CONSTANT_FEATURES, SIMILARITY_BASED_FEATURES, TOPOLOGICALLY_RELATED_FEATURES
	}
	
	/**
	 * Strategies to manage callable constructs 
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum FeatureConstructsSelectionStrategies {
		CONSTANT_CALLABLE_CONSTRUCTS, PERFORMANCE_OPTIMIZED, TOPOLLOGICALLY_BASED
	}
	
	/**
	 * Default number of features to select - the number of VP that should be processed
	 */
	public static int DEFAULT_NUMBER_OF_FEATURES_TO_SELECT = 1; //3
	
	/**
	 * Default number of callable constructs to select for each feature - number of constructs in each VP to select to select code constructs from various VP
	 */
	public static int DEFAULT_NUMBER_OF_FEATURE_CONSTRUCTS_TO_SELECT = 1; //2
	
	/**
	 * Default number of aggregated constructs to select - number of constructs in aggregation
	 */
	public static int DEFAULT_NUMBER_OF_AGGREGATED_OVERALL_CONSTRUCTS_TO_SELECT = 2;
	
	/**
	 * Selected strategy to select positive variation points to insert given content 
	 */
	public static FeatureSelectionStrategies SELECTED_FEATURE_SELECTION_STRATEGY = 
			FeatureSelectionStrategies.CONSTANT_FEATURES;
	
	/**
	 * Selected strategy to select code callable constructs for each selected variation point 
	 */
	public static FeatureConstructsSelectionStrategies SELECTED_FEATURE_CONSTRUCTS_SELECTION_STRATEGY = 
			FeatureConstructsSelectionStrategies.CONSTANT_CALLABLE_CONSTRUCTS;
	
	
	/**
	 * Interface to distinct between selection based synthesis of constructs and other types
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public interface SyntetizeConstructsSelectionBased { //constant==fixed number
		boolean isSelectionBasedOnly();
	}

	/**
	 * Abstract strategies to synthesize code constructs
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum SyntetizeConstructsType {
		SELECTION_BASED,
		TOPOLOGICALLY_DIVERSE_SELECTED
	}
	
	/**
	 * Strategies to synthesize constructs
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum SyntetizeConstructsOptions { //constant==fixed number
		UP_TO_SELECTED_FEATURES(SyntetizeConstructsType.SELECTION_BASED), 		//creates more potential products
		SIMILARITY_SELECT(SyntetizeConstructsType.SELECTION_BASED),
		ALL_SELECTED(SyntetizeConstructsType.SELECTION_BASED), 					// suitable if similarity matched features previously well
		EXACTLY_ONE_SELECTED(SyntetizeConstructsType.SELECTION_BASED), 			//when only one construct should be included,
		TOPOLOGICALLY_DIVERSE_SELECTED(SyntetizeConstructsType.TOPOLOGICALLY_DIVERSE_SELECTED);
		
		/**
		 * Abstract type that is associated with given strategy
		 */
		public final SyntetizeConstructsType type;

		/**
		 * Create SynthesizeConstructsOptions object with associated abstract strategy type
		 * 
		 * @param type
		 */
	    private SyntetizeConstructsOptions(SyntetizeConstructsType type) {
	        this.type = type;
	    }
	    
	    /**
	     * Returns information of given startegy has abstract type selection based
	     * 
	     * @return true if given strategy is selection base otherwise false
	     */
	    public boolean isSelectionBasedOnly() { 
	    	if(this.type.equals(SyntetizeConstructsType.SELECTION_BASED)) {
	    		return true;
	    	} else {
	    		return false;
	    	}
	    }
	}
	
	/**
	 * Selected strategy to synthesize feature constructs
	 */
	public static SyntetizeConstructsOptions SYNTETIZE_FEATURE_CONSTRUCTS_OPTION = 
			SyntetizeConstructsOptions.TOPOLOGICALLY_DIVERSE_SELECTED;
	
	/**
	 * Used separator/delimiter to separate various code fragments, especially one line ones
	 */
	public static final String CODE_FRAGMENT_SEPARATOR = "\n";
	
	/**
	 * method can be introduced if number of appended lines exceeds this constant
	 */
	public static final Integer NUMBER_LINES_TO_INTRODUCE_METHOD = Integer.parseInt(
			System.getenv().getOrDefault("NUMBER_LINES_TO_INTRODUCE_METHOD", "5"));
	
	/**
	 * class can be introduced if number of declarations exceeds this constant
	 */
	public static final Integer NUMBER_DECLARATIONS_TO_INTRODUCE_CLASS = Integer.parseInt(
			System.getenv().getOrDefault("NUMBER_DECLARATIONS_TO_INTRODUCE_CLASS", "3"));
	
	/**
	 * class can be introduced if number of declared methods exceeds this constant
	 */
	public static final Integer NUMBER_METHODS_TO_INTRODUCE_CLASS = Integer.parseInt(
			System.getenv().getOrDefault("NUMBER_METHODS_TO_INTRODUCE_CLASS", "8"));
	
	/**
	 * class can be introduced if number of lines exceeds this constant
	 */
	public static final Integer NUMBER_LINES_TO_CONVERT_METHOD_INTO_CLASS = Integer.parseInt(
			System.getenv().getOrDefault("NUMBER_LINES_TO_CONVERT_METHOD_INTO_CLASS", "8"));
	

	/**
	 * Strategies used to manage granularity of synthesized content that is represented as various data structures
	 * -similarity based - code fragments are merged according to similarity between various types
	 * -quantity based - code fragments are merged according to cardinality of their types
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum CodeIncrementGranularityManagementStrategies {
		QUANTITY_BASED_GRANULARITY, SIMILARITY_BASED_GRANULARITY
	}
	
	/**
	 * Selected strategy for synthesis of introduced content
	 */
	public static final CodeIncrementGranularityManagementStrategies SELECTED_CODE_INCREMENT_GRANULARITY_MANAGEMENT_STRATEGY = 
			CodeIncrementGranularityManagementStrategies.QUANTITY_BASED_GRANULARITY;
	

	/**
	 * Strategies to manage given evolution iteration
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum EvolutionStrategies { DEFAULT }
	
	/**
	 * Selected strategy to manage the evolution iteration
	 */
	public static EvolutionStrategies SELECTED_EVOLUTION_STRATEGY = EvolutionStrategies.DEFAULT;
	
	/**
	 * Allows or disallows creation of duplicated product across evolutions
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum DuplicateProductPreventionOptions { //constant==fixed number
		ALLOW, DISALLOW
	}
	
	/**
	 * Selected duplicate prevention option
	 */
	public static DuplicateProductPreventionOptions DUPLICATE_PREVENTION_OPTION = DuplicateProductPreventionOptions.ALLOW;
	
	/**
	 * if true then the resulting AST of derived SPL will be persisted otherwise not
	 */
	public final static boolean SERIALIZE_APPLICATION_AST = Boolean.parseBoolean(
			System.getenv().getOrDefault("SERIALIZE_APPLICATION_AST", "true"));
	
	/**
	 * if true then the resulting variation points sequence of SPL will be persisted otherwise not 
	 */
	public final static boolean SERIALIZE_VARIATION_POINTS = Boolean.parseBoolean(
			System.getenv().getOrDefault("SERIALIZE_VARIATION_POINTS", "true"));
	
	/**
	 * String identifier identifying data from variation points - representation
	 */
	public final static String VARIATION_POINTS_DATA_NAME_ID_ENDING = System.getenv().getOrDefault(
			"VARIATION_POINTS_DATA_NAME_ID_ENDING", "_VariationPointData.json");
	
	/**
	 * String identifier identifying AST of processed application
	 */
	public final static String PROCESSED_AST_NAME_ID_ENDING = System.getenv().getOrDefault(
			"PROCESSED_AST_NAME_ID_ENDING", "_AST.json");
	
	/**
	 * Clears comments from resulting SPL derivation
	 */
	public final static boolean CLEAR_COMMENTS_FROM_RESULTING_SPL_DERIVATION = Boolean.parseBoolean(
			System.getenv().getOrDefault("CLEAR_COMMENTS_FROM_RESULTING_SPL_DERIVATION", "true"));
	
	/**
	 * Clears comments from resulting SPL during evolution iteration
	 */
	public final static boolean CLEAR_COMMENTS_DURING_SPL_EVOLUTION = Boolean.parseBoolean(
			System.getenv().getOrDefault("CLEAR_COMMENTS_DURING_SPL_EVOLUTION", "true"));
	
	/**
	 * Copy resources from shared library into each SPL derivation
	 */
	public final static boolean INCLUDE_SHARED_LIBRARY = Boolean.parseBoolean(
			System.getenv().getOrDefault("INCLUDE_SHARED_LIBRARY", "true"));
	
	/**
	 * Copy scripts with global configuration variables from shared library into each SPL derivation
	 */
	public final static boolean INCLUDE_SHARED_VARIABLES_IN_LIBRARY = Boolean.parseBoolean(
			System.getenv().getOrDefault("INCLUDE_SHARED_VARIABLES_IN_LIBRARY", "true"));
	
	/**
	 * Path to stored libraries for each evolved SPL/product/application
	 */
	public final static String SHARED_LIBRARY_LOCATION = "lib";
	
	/**
	 * Path to stored global variables for each evolved SPL/product/application
	 */
	public final static String SHARED_GLOBAL_VARIABLES_LOCATION = "lib/vars";
	
	/**
	 * Restriction to maximal derived SPLs during evolution - obeyed only in special cases
	 */
	public final static int MAX_SPL_INSTANCES_TO_DERIVE = Integer.parseInt(
			System.getenv().getOrDefault("MAX_SPL_INSTANCES_TO_DERIVE", "1"));

	/**
	 * The list of functions that cannot be instantiated into code fragment
	 */
	public final static List<String> FORBIDDEN_FUNCTIONS_IN_PROCESSED_SPLS = List.of("popData()", "harvestStackData()");
	
	/**
	 * Allows to use actual/currently available script variables, parameters and directly created particular types if true otherwise not
	 */
	public final static boolean USE_ACTUAL_SCRIPT_VARIABLES = Boolean.parseBoolean(
			System.getenv().getOrDefault("USE_ACTUAL_SCRIPT_VARIABLES", "true"));
	
	/**
	 * Allows to harvest method or class parameters and optionally to use them for substitution if true otherwise not if true otherwise not
	 */
	public final static boolean USE_PARAMETERS = Boolean.parseBoolean(
			System.getenv().getOrDefault("USE_PARAMETERS", "true"));
	
	/**
	 * Allows to harvest global variables and to use them for substitution if true otherwise not
	 */
	public final static boolean USE_GLOBAL_VARIABLES = Boolean.parseBoolean(
			System.getenv().getOrDefault("USE_GLOBAL_VARIABLES", "true"));
	
	/**
	 * Restricts the harvesting only the variables on current hierarchy level if true otherwise not
	 */
	public final static boolean USE_CURRENT_LEVEL_VARIABLES_ONLY = Boolean.parseBoolean(
			System.getenv().getOrDefault("USE_CURRENT_LEVEL_VARIABLES_ONLY", "true"));
	
	/**
	 * Allows to instantiate new entities according to type if true otherwise not
	 */
	public final static boolean INSTANTIATE_NEW_ENTITIES_ACCORDING_TO_TYPE = Boolean.parseBoolean(
			System.getenv().getOrDefault("INSTANTIATE_NEW_ENTITIES_ACCORDING_TO_TYPE", "true"));
	
	/**
	 * Separates the currently available variable from the name/identifier of variation point to manage both
	 */
	public final static String CURRENTLY_AVAILABLE_VARIABLE_DELIMITER = "_YYY_";
	
	/**
	 * Disables inject and use external variables during the evolution (has impact on available code fragments used during positive variability handling)
	 */
	public final static boolean DISABLE_EXTERNAL_VARIABLE_INJECTIONS = Boolean.parseBoolean(
			System.getenv().getOrDefault("DISABLE_EXTERNAL_VARIABLE_INJECTIONS", "false"));
	
	/**
	 * Disables inject and use internally available variables during the evolution (has impact on available code fragments used during positive variability handling)
	 */
	public final static boolean DISABLE_INTERNAL_VARIABLE_INJECTIONS = Boolean.parseBoolean(
			System.getenv().getOrDefault("DISABLE_INTERNAL_VARIABLE_INJECTIONS", "false"));
	
	/**
	 * The name of file with configuration of custom annotations used to manage variability in SPL with their declaration
	 */
	public final static String DECORATOR_CONFIGURATION_ANNOTATIONS_FILE_NAME = "decoratorsSPL.js";
	
	/**
	 * Path to file with configuration of custom annotations used to manage variability in SPL with their declaration
	 */
	public final static String DECORATOR_CONFIGURATION_ANNOTATIONS_FILE_PATH = 
			"js/" + SPLEvolutionCore.DECORATOR_CONFIGURATION_ANNOTATIONS_FILE_NAME;
	
	/**
	 * Allows to connect to RabbitMQ and store produced messages about each evolved SPL
	 */
	public final static boolean PRODUCE_MESSAGES_INTO_MQ_AFTER_DERIVATION = true;
}
