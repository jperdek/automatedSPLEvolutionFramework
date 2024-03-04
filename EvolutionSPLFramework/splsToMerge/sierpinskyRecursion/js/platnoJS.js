
var aaa1 = `
/*
	POINT OBJECT - definition
	
	@param {number} x - center x coordinate of point
	@param {number} y - center y coordinate of point
	@param {number} radius - radius of point
*/
class Point {
  x: number;
  y: number;

  constructor(x: number, y: number) {
    this.x = x;
    this.y = y;
  }
}


/*
	DRAWS LINE from (x1, y1) to (x2, y2)
	
	@param {Context} context - context from Canvas to perform paint operations
	@param {number} x1 - coordinate x of start point
	@param {number} y1 - coordinate y of start point
	@param {number} x2 - coordinate x of target point
	@param {number} y2 - coordinate y of target point
	@param {number} thickness - line thickness/width
	
*/
function drawLine(context: CanvasRenderingContext2D, x1: number, 
        y1: number, x2: number, y2: number, thickness: number): void {
  context.beginPath();
  context.moveTo(x1, y1);
  context.lineTo(x2, y2);
  context.lineWidth = thickness;
  context.strokeStyle = "#cfc";
  context.stroke();
}


/*
	Object where data for Sierpinski shape are stored - similar to class representation
	
	@param {number} initialX - initial x coordinate where drawing starts
	@param {number} initialY - initial y coordinate where drawing starts
	@param {number} numberIterations - number iteration of fractal generation (number extensions/levels of original shape)
	@param {number} initialSideLength - initial length of side of koch shape which should be recalculated according number of iteration later
*/
class SierpinskyMapInfo {
  numberIterations: number;
  sideLength: number;
  sierpinskyMapString: string = "";
  thickness: number;

  constructor(numberIterations: number, initialSideLength: number, thickness: number) {
    this.numberIterations = numberIterations;
    this.sideLength = initialSideLength;
    this.sierpinskyMapString;
    this.thickness = thickness;
  }

  setFinalSierpinskyMapString(sierpinskyMapString: string) { this.sierpinskyMapString = sierpinskyMapString; }
  setFinalSideLength(sideLength: number) { this.sideLength = sideLength; }
}

export class SierpinskiFractalRecursive {
  /*
    INITIAL FUNCTION FROM WHICH SIERPINSKI SHAPE IS DRAWN
    
    - draws base triangle and starts recursion to draw rest

    @param {number} numberIterations - number iteration of fractal generation (number extensions/levels of original shape)
    @param {Context} context - context from Canvas to perform paint operations
    @param {number} point1x - x coordinate of first point in triangle
    @param {number} point1y - y coordinate of first point in triangle
    @param {number} point2x - x coordinate of second point in triangle
    @param {number} point2y - y coordinate of second point in triangle
    @param {number} point3x - x coordinate of third point in triangle
    @param {number} point3y - y coordinate of third point in triangle
    @param {SierpinskiMapInfo} sierpinskyMapInfo - Sierpinski mapping representation
  */
  drawSierpinsky(numberIterations: number, context: CanvasRenderingContext2D, point1x: number, point1y: number, 
        point2x: number, point2y: number, point3x: number, point3y: number, sierpinskyMapInfo: SierpinskyMapInfo): void {
    
    drawLine(context, point1x, point1y, point2x, point2y, sierpinskyMapInfo.thickness);
    drawLine(context, point2x, point2y, point3x, point3y, sierpinskyMapInfo.thickness);
    drawLine(context, point1x, point1y, point3x, point3y, sierpinskyMapInfo.thickness);
    
    this.drawSierpinskyRec(numberIterations, context, point1x, point1y, point2x, point2y, point3x, point3y, sierpinskyMapInfo);
  }


  /*
    SIERPINSKI RECURSION
    
    1. checks condition if (extending) recursion should be stopped
    2. evaluates points in the middle of triangle sides (for lower levels of recursion)
    3. connects these middle points to form 4 triangles
    4. for three side triangles it starts new iteration by calling itself

    @param {number} iteration - actual processed iteration
    @param {Context} context - context from Canvas to perform paint operations
    @param {number} point1x - x coordinate of first point in triangle
    @param {number} point1y - y coordinate of first point in triangle
    @param {number} point2x - x coordinate of second point in triangle
    @param {number} point2y - y coordinate of second point in triangle
    @param {number} point3x - x coordinate of third point in triangle
    @param {number} point3y - y coordinate of third point in triangle
    @param {SierpinskiMapInfo} sierpinskyMapInfo - Sierpinski mapping representation
  */
  drawSierpinskyRec(iteration: number, context: CanvasRenderingContext2D, point1x: number, point1y: number, 
        point2x: number, point2y: number, point3x: number, point3y: number, sierpinskyMapInfo: SierpinskyMapInfo): void {
    let middle12x: number, middle12y: number, middle23x: number;
    let middle23y: number, middle13x: number, middle13y: number;
    
    if (iteration > 0){
      middle12x = point1x + (point2x - point1x) / 2.0;
      middle12y = point1y + (point2y - point1y) / 2.0;
      
      middle23x = point2x + (point3x - point2x) / 2.0;
      middle23y = point2y + (point3y - point2y) / 2.0;
      
      middle13x = point1x + (point3x - point1x) / 2.0;
      middle13y = point1y + (point3y - point1y) / 2.0;
      
      
      drawLine(context, middle12x, middle12y, middle23x, middle23y, sierpinskyMapInfo.thickness);
      drawLine(context, middle23x, middle23y, middle13x, middle13y, sierpinskyMapInfo.thickness);
      drawLine(context, middle12x, middle12y, middle13x, middle13y, sierpinskyMapInfo.thickness);
      
      
      //drawSierpinskyRec(iteration - 1, context, middle12x, middle12y, middle23x, middle23y, middle13x, middle13y, sierpinskyMapInfo);
      this.drawSierpinskyRec(iteration - 1, context, point1x, point1y, middle12x, middle12y, middle13x, middle13y, sierpinskyMapInfo);
      this.drawSierpinskyRec(iteration - 1, context, point2x, point2y, middle12x, middle12y, middle23x, middle23y, sierpinskyMapInfo);
      this.drawSierpinskyRec(iteration - 1, context, point3x, point3y, middle23x, middle23y, middle13x, middle13y, sierpinskyMapInfo);
    }
  }
}


export function drawSierpinskiRecursive(point1: Point, point2: Point, point3: Point, 
        context: CanvasRenderingContext2D, numberIterations: number, initialSideLength: number, thickness: number): void {
    const sierpinskyMapInfo = new SierpinskyMapInfo(numberIterations, initialSideLength, thickness);
    const sierpinskiFractal = new SierpinskiFractalRecursive();
    sierpinskiFractal.drawSierpinsky(numberIterations, context, point1.x, point1.y, point2.x, point2.y, point3.x, point3.y, sierpinskyMapInfo);
}
`;