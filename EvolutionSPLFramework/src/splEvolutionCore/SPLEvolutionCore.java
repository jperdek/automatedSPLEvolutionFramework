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
	
	public static boolean APPLY_TO_TEMPLATE = true;
	
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
	public static int DEFAULT_NUMBER_OF_FEATURES_TO_SELECT = 3;
	
	/**
	 * Default number of callable constructs to select for each feature - number of constructs in each VP to select to select code constructs from various VP
	 */
	public static int DEFAULT_NUMBER_OF_FEATURE_CONSTRUCTS_TO_SELECT = 2;
	
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
	public static final Integer NUMBER_LINES_TO_INTRODUCE_METHOD = 5;
	/**
	 * class can be introduced if number of declarations exceeds this constant
	 */
	public static final Integer NUMBER_DECLARATIONS_TO_INTRODUCE_CLASS = 3;
	/**
	 * class can be introduced if number of declared methods exceeds this constant
	 */
	public static final Integer NUMBER_METHODS_TO_INTRODUCE_CLASS = 8;
	/**
	 * class can be introduced if number of lines exceeds this constant
	 */
	public static final Integer NUMBER_LINES_TO_CONVERT_METHOD_INTO_CLASS = 8;
	

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
	public final static boolean SERIALIZE_APPLICATION_AST = true;
	
	/**
	 * if true then the resulting variation points sequence of SPL will be persisted otherwise not 
	 */
	public final static boolean SERIALIZE_VARIATION_POINTS = true;
}
