package variationPointsVisualization;

import java.io.IOException;
import org.json.simple.JSONObject;
import codeContext.processors.ASTTextExtractorTools;
import dividedAstExport.ExpressionConverter;
import variationPointsVisualization.AnnotationExtensionMarker.SystemAnnotationType;


/**
 * Negative variation point annotation representation
 * 
 * @author Jakub Perdek
 *
 */
public class NegativeVariabilityAnnotationElement {

	/**
	 * true if annotation belongs to user otherwise false
	 */
	private boolean isUserAnnotation = true;
	
	/**
	 * The type of system annotation if annotation belongs to system annotations
	 */
	private SystemAnnotationType systemAnnotationType = null;
	
	/**
	 * The name of annotation
	 */
	private String annotatioName;
	
	/**
	 * The configuration expression converted into string in JSON
	 */
	private String expressionStr = null;
	
	/**
	 * The configuration expression converted from AST in original/native JSON form
	 */
	private JSONObject expressionInJSON;
	
	/**
	 * The configuration expression represented as AST (also stored as JSON)
	 */
	private JSONObject expressionAst;
	
	
	/**
	 * Creates negative variability annotation element and converts it into JSON
	 * 
	 * @param annotationName - the name of annotation
	 * @param expressionAst - configuration expression converted into AST 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public NegativeVariabilityAnnotationElement(String annotationName, JSONObject expressionAst) throws IOException, InterruptedException {
		this.annotatioName = annotationName;
		this.expressionAst = expressionAst;
		this.convertExpressionAstToJSON();
	}
	
	/**
	 * Creates negative variability annotation element with associated type information and converts it into JSON
	 * 
	 * @param annotationName - the name of annotation
	 * @param expressionAst - configuration expression converted into AST 
	 * @param systemAnnotationType - the type of system annotation
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public NegativeVariabilityAnnotationElement(String annotationName, JSONObject expressionAst, 
			SystemAnnotationType systemAnnotationType) throws IOException, InterruptedException {
		this(annotationName, expressionAst);
		this.systemAnnotationType = systemAnnotationType;
		this.isUserAnnotation = false;
	}
	
	/**
	 * Converts AST configuration expression into JSON
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void convertExpressionAstToJSON() throws IOException, InterruptedException {
		String annotationName = ASTTextExtractorTools.getTextFromAstIncludingNameAndExpressions(this.expressionAst);
		this.expressionInJSON = ExpressionConverter.extractExpressionFromDecoratorAst(this.expressionAst);
		if (this.expressionInJSON == null && AnnotationExtensionMarker.isSystemAnnotation(annotationName)) {
			//logger.debug("Automatically generated exception does not have configuration expression! Skip.");
		} else {
			this.expressionStr = this.expressionInJSON.toString();
		}
	}
	
	/**
	 * Returns type of system annotation
	 * 
	 * @return returns system annotation type of used annotation
	 */
	public SystemAnnotationType getSystemAnnotationType() { return this.systemAnnotationType; }
	
	/**
	 * Returns converted string representation of AST
	 * 
	 * @return string representation of expression in AST
	 */
	public String getExpressionInString() { return this.expressionStr; }
	
	/**
	 * Returns annotation name
	 * 
	 * @return the name of annotation
	 */
	public String getAnnotationName() { return this.annotatioName; }
	
	/**
	 * Returns information if annotation is user annotation
	 * 
	 * @return true if annotation is user annotation otherwise false
	 */
	public boolean isUserAnnotation() { return this.isUserAnnotation; }
}
