package evolutionSimulation.iteration;

import splEvolutionCore.CanvasBasedApplicationConfiguration;


/**
 * Helper class with language-dependent fractal initialziation code fragments
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class FractalIterationInitializationCodeFragments {

	/**
	 * Initializes FractalIterationInitializationCodeFragments
	 */
	public FractalIterationInitializationCodeFragments() {
	}
	
	/**
	 * The initialization code of HTML canvas with possibility to wrap it under Canto JS
	 *  
	 * @return the initialization code of HTML canvas with possibility to wrap it under Canto JS
	 */
	private static String getCanvasCode() {
		String canvasInitialization = "var canvas = document.getElementById(\"game\");\r\n"
				+   "		var context = canvas.getContext('2d');\r\n";
		if (CanvasBasedApplicationConfiguration.WRAP_WITH_CANTO_JS) {
			return CanvasBasedApplicationConfiguration.wrapWithCantoJS(canvasInitialization);
		}
		return canvasInitialization;
	}

	/**
	 * The initialization code to call anklet fractal rendering
	 * -includes the initialization of canvas functionality
	 * 
	 * @return the initialization code to call anklet fractal rendering
	 */
	public static String getInitialCodeAnklet() {
		return getCanvasCode()
				+ "		const circleRadius = 5;\r\n"
				+ "		const numberIterations = 5;\r\n"
				+ "		const lineLength = 500;\r\n"
				+ "		const thickness = 2;\r\n"
				+ "		const squareSideLength = lineLength / Math.pow(2, numberIterations);\r\n"
				+ "		const ankletInfo = new AnkletInfo(lineLength, circleRadius, thickness, 2*squareSideLength);\r\n"
				+ "		drawAnkletMain(canvas, context, circleRadius, numberIterations, lineLength, thickness, squareSideLength, ankletInfo);";
	}

	/**
	 * The initialization code to call Five side fractal rendering
	 * -includes the initialization of canvas functionality
	 * 
	 * @return the initialization code to call Five side fractal rendering
	 * -includes the initialization of canvas functionality
	 */
	public static String getInitialCodeFiveSide() {
		return getCanvasCode()
				+ "		const circleRadius = 5;\r\n"
				+ "		const numberIterations = 5;\r\n"
				+ "		const lineLength = 500;\r\n"
				+ "		const thickness = 2;\r\n"
				+ "		const squareSideLength = lineLength / Math.pow(2, numberIterations);\r\n"
				+ "		drawAnkletModMain(context, circleRadius, numberIterations, thickness);\r\n";
	}
}
