package divisioner;

import java.io.IOException;

import org.json.simple.JSONObject;
import codeContext.CodeContext;
import codeContext.processors.NotFoundVariableDeclaration;
import variationPointsVisualization.DifferentAnnotationTypesOnTheSameVariationPoint;
import variationPointsVisualization.DuplicatedAnnotation;

/**
 * Interface to prescribe functionality required for divisioning
 * 
 * 
 * @author Jakub Perdek
 *
 */
public interface DivisioningInterface {

	/**
	 * Returns code context with hierarchical information obtained from divisioning
	 * 
	 * @return the code context with hierarchical information obtained from divisioning
	 */
	public CodeContext getCodeContextFromDivision();
	
	/**
	 * Returns used strategy for divisioning into variation points
	 * 
	 * @return used strategy for divisioning into variation points
	 */
	public VariationPointsDivisioningStrategy getDivisionStrategy();
	
	/**
	 * Hierarchically divisions variation points according provided AST (TypeScript)  
	 * -initializes root inner context and global context
	 * -launches variation points divisioning - positive and negative variability
	 * -outputs ordered structure
	 * 
	 * @param astTreeRoot - the root of processed AST
	 * @param fileName - the name of parsed AST
	 * @param useTypes - if types should be used if found during divisioning, especially context/contexts preparation
	 * @return divisioned AST (particular code) into variation points
	 * 
	 * @throws NotFoundVariableDeclaration
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DifferentAnnotationTypesOnTheSameVariationPoint
	 * @throws DuplicatedAnnotation
	 */
	public JSONObject divisionToVPHierarchic(JSONObject astTreeRoot, String fileName, boolean useTypes) throws NotFoundVariableDeclaration, 
				IOException, InterruptedException, DifferentAnnotationTypesOnTheSameVariationPoint, DuplicatedAnnotation;
}
