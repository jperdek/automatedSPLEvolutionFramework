package evolutionSimulation.productAssetsInitialization;

/**
 * Manages canvas element - the representation of HTML canvas resource
 * -its properties
 * -its injection across fractal-based SPL 
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class CanvasBasedResource extends Resource {

	/**
	 * Canvas element name, usually HTML id attribute (identifier)
	 */
	private String canvasElementName;
	
	/**
	 * Width of the canvas
	 */
	private int width = 1600;
	
	/**
	 * Height of the canvas
	 */
	private int height = 1600;
	
	
	/**
	 * Creates the instance of canvas based resource
	 * 
	 * @param canvasElementName - the name of canvas element, usually HTML id attribute (identifier)
	 * @param templatePath - path to processed HTML template (file) where canvas is used
	 */
	public CanvasBasedResource(String canvasElementName, String templatePath) {
		super(templatePath);
		this.canvasElementName = canvasElementName;
	}
	
	/**
	 * Creates the instance of canvas based resource
	 * 
	 * @param canvasElementName - the name of canvas element, usually HTML id attribute (identifier)
	 * @param templatePath - path to processed HTML template (file) where canvas is used
	 * @param width - canvas width
	 * @param height - canvas height
	 */
	public CanvasBasedResource(String canvasElementName, String templatePath, int width, int height) {
		this(canvasElementName, templatePath);
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Returns canvas width
	 * 
	 * @return canvas width
	 */
	public int getCanvasWidth() { return this.width; }
	
	/**
	 * Returns canvas height
	 * 
	 * @return canvas height
	 */
	public int getCanvasHeight() { return this.height; }
	
	/**
	 * Returns the canvas identifier, usually HTML id attribute (identifier)
	 * 
	 * @return the canvas identifier, usually HTML id attribute (identifier)
	 */
	public String getCanvasId() { return this.canvasElementName; }
}
