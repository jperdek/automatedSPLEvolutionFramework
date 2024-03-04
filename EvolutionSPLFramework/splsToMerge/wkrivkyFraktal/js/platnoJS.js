
var aaa1 = `
/*
	DRAWS LINE from (x1, y1) to (x2, y2)
	
	1. moves to initial point
	2. draws line to end point
	3. sets stroke style to #cfc and line width to thickness
	
	@param {Context} context - context from Canvas to perform paint operations
	@param {number} x1 - coordinate x of start point
	@param {number} y1 - coordinate y of start point
	@param {number} x2 - coordinate x of target point
	@param {number} y2 - coordinate y of target point
	@param {number} thickness - line thickness/width
	
*/
function drawLine(context: CanvasRenderingContext2D, x1: number, y1: number, x2: number, y2: number, thickness: number): void {
	context.beginPath();
	context.moveTo(x1, y1);
	context.lineTo(x2, y2);
	context.lineWidth = thickness;
	context.strokeStyle = "#cfc";
	context.stroke();
}

/*
		DIRECTION_ENUM
		
		-conatins given extension options from Krishna Anklet
	*/
	enum Direction {
		LEFT_UP = 0,
		LEFT_DOWN = 1,
		RIGHT_UP = 2,
		RIGHT_DOWN = 3,
		ALL = 4
	};
/*
	W-CURVE FRACTAL REPRESENTATION
	
	@param {number} size - size of whole shape (size of canvas in many cases)
	@param {number} lineLength - length of used lines
	@param {number} thickness - line thickness/width
	@param {number} distanceWidthRadius - distance radius between doubled lines
*/	
export class Wcurve {
    thickness: number;
    size: number;
    lineLength: number;
    diagonalLength: number | null;
    moveRatio: number = 8;
    distanceWidthRadius: number;
    lineLengthHalf: number = 0;

  constructor(size: number, lineLength: number, thickness: number, distanceWidthRadius: number){
    this.thickness = thickness;
    this.size = size;
    this.lineLength = lineLength;
    this.diagonalLength = null;
    this.moveRatio = 8;
    this.distanceWidthRadius = distanceWidthRadius;
  }
	
	/*
		EVALUATES LINE LENGTH FROM GIVEN NUMBER OF ITERATIONS AND INITIALIZES OTHER PARAMETERS
		
		@param {number} iterations - number of iterations to draw fractal
	*/
	initialize(iterations: number): void {
		this.lineLength = this.size;
		for(let i=0; i<iterations; i++){
			this.lineLength = Math.round(this.lineLength / 2.0);
		}
		//this.lineLength = this.size >> iterations;
		this.diagonalLength = this.lineLength;//Math.round(this.lineLength * Math.sqrt(2.0));
		this.lineLengthHalf = this.lineLength / 2;//Math.round(this.diagonalLength / 2.0);
		this.moveRatio = 1 << iterations;
	}


  drawCrossedLine(context: CanvasRenderingContext2D, point1xShared: number, point1yShared: number, point2x: number, point2y: number, 
        point3x: number, point3y: number, thickness: number): void {
      drawLine(context, point1xShared, point1yShared, point2x, point2y, thickness);
      drawLine(context, point1xShared, point1yShared, point3x, point3y, thickness);	
  }

  /*
    DRAWS DOUBLED MODIFIED W-CURVE
    
    @param {number} iteration - actual performed iteration
    @param {number} moveRatioIteration - how much should be fractal line extended in the next iteration
    @param {number} centerX - center x coordinate given fractal part (center of anmbulance cross)
    @param {number} centerY - center y coordinate given fractal part (center of anmbulance cross)
    @param {WCurve} wcurve - wcurve data structure
    @param {Context} context - context from Canvas to perform paint operations
    @param {DIRECTION_ENUM} direction - direction which should be processed in the next iteration
    @param {boolean} inheritedOperation - condition which should be applied in given direction of drawing w-curve part in the next iteration
  */
  drawWCurve(iteration: number, moveRatioIteration: number, centerX: number, centerY: number, 
        wcurve: Wcurve, context: CanvasRenderingContext2D, direction: Direction, inheritedOperation: boolean): void {
    let square1x: number, square1y: number, square2x: number, square2y: number;
    let square3x: number, square3y: number, square4x: number, square4y: number;
    let verticalToSquare1y: number, horizontalToSquare1x: number;
    let verticalToSquare2y: number, horizontalToSquare2x: number;
    let verticalToSquare3y: number, horizontalToSquare3x: number;
    let verticalToSquare4y: number, horizontalToSquare4x: number;
    
    let newLeftUpX: number, newLeftUpY: number;
    let newRightUpX: number, newRightUpY: number;
    let newLeftBottomX: number, newLeftBottomY: number;
    let newRightBottomX: number, newRightBottomY: number;
    
    // EVALUATES DISTANCE TO NEW CENTER OF GIVEN FRACTAL PART
    let distanceOfNewOnes: number = moveRatioIteration * wcurve.lineLength;
    
    let condOfDirRightUp: boolean, condOfDirRightDown: boolean;
    let condOfDirLeftDown: boolean, condOfDirLeftUp: boolean;
    
    // STOPPING CONDITION
    if (iteration > 0) {
      // EVALUATES FRACTAL POINTS FROM GIVEN CENTER
        //THE FIRST TOP LEFT POINT
      square1x = centerX - wcurve.lineLengthHalf;
      square1y = centerY - wcurve.lineLengthHalf;
        //THE SECOND TOP RIGHT POINT
      square2x = centerX + wcurve.lineLengthHalf;
      square2y = centerY - wcurve.lineLengthHalf;
        //THE THIRD BOTTOM LEFT POINT
      square3x = centerX - wcurve.lineLengthHalf;
      square3y = centerY + wcurve.lineLengthHalf;
        //THE FOURTH BOTTOM RIGHT POINT
      square4x = centerX + wcurve.lineLengthHalf;
      square4y = centerY + wcurve.lineLengthHalf;
      

      // EVALUATES SOME FRACTAL COORDINATES
      verticalToSquare1y = square1y - wcurve.lineLength;   //the most top point for given w-curve part (verticalToSquare2y === verticalToSquare1y [are same])
      horizontalToSquare1x = square1x - wcurve.lineLength; //the most left point for given w-curve part (horizontalToSquare1x == horizontalToSquare3x [are same])
      
      verticalToSquare2y = square2y - wcurve.lineLength;   //the most top point for given w-curve part (verticalToSquare2y === verticalToSquare1y [are same]) //CAN BE COMMENTED
      horizontalToSquare2x = square2x + wcurve.lineLength; //the most right point for given w-curve part (horizontalToSquare2x === horizontalToSquare4x [are same])
      
      verticalToSquare3y = square3y + wcurve.lineLength;	 //the most bottom point for given w-curve part (verticalToSquare3y === verticalToSquare4y [are same])
      horizontalToSquare3x = square3x - wcurve.lineLength; //the most left point for given w-curve part (horizontalToSquare1x === horizontalToSquare3x [are same])
      
      verticalToSquare4y = square4y + wcurve.lineLength;   //the most bottom point for given w-curve part (verticalToSquare3y === verticalToSquare4y [are same]) //CAN BE COMMENTED
      horizontalToSquare4x = square4x + wcurve.lineLength; //the most right point for given w-curve part (horizontalToSquare2x === horizontalToSquare4x [are same]) //CAN BE COMMENTED
      
      // MAKING SIMPLER:
      horizontalToSquare1x = horizontalToSquare1x;
      verticalToSquare2y = verticalToSquare1y;
      verticalToSquare4y = verticalToSquare3y;
      horizontalToSquare4x = horizontalToSquare2x;
      
      // EVALUATES NEW CENTER POINTS TO EXTEND RECURSION
        //THE FIRST NEW TOP LEFT CENTER
      newLeftUpX = centerX - distanceOfNewOnes;
      newLeftUpY = centerY - distanceOfNewOnes;
        //THE SECOND NEW TOP RIGHT CENTER
      newRightUpX = centerX + distanceOfNewOnes;
      newRightUpY = centerY - distanceOfNewOnes;
        //THE THIRD NEW BOTTOM LEFT CENTER
      newLeftBottomX = centerX - distanceOfNewOnes;
      newLeftBottomY = centerY + distanceOfNewOnes;
        //THE FOURTH NEW BOTTOM RIGHT CENTER
      newRightBottomX = centerX + distanceOfNewOnes;
      newRightBottomY = centerY + distanceOfNewOnes;
      
      
      // SETTING CONDITIONS TO COVER HOLES IN The LAST ITERATION TO FORM THE W-CURVE
      // RIGHT UP CONDITION
        // IF COMES FROM THE LEFT DOWN
      if(direction === Direction.LEFT_DOWN){
        condOfDirRightUp = true;
        
        // IF COMES FROM RIGHT_DOWN OR LEFT_UP)
      } else if(direction !== Direction.RIGHT_UP){
        condOfDirRightUp = false; // allows to put side
        
        // IF COMES FROM THE RIGHT UP
      } else{
        condOfDirRightUp = inheritedOperation;
      }
      
      // RIGHT DOWN CONDITION
        // IF COMES FROM THE LEFT UP
      if(direction === Direction.LEFT_UP){
        condOfDirRightDown = true;
        
        // IF COMES FROM LEFT_DOWN OR RIGHT_UP)
      } else if(direction !== Direction.RIGHT_DOWN){
        condOfDirRightDown = false; // allows to put side
        
        // IF COMES FROM THE BOTTOM RIGHT (DOWN)
      } else{
        condOfDirRightDown = inheritedOperation; // use previous decision
      }
      
      // LEFT DOWN CONDITION
        // IF COMES FROM THE RIGHT UP
      if(direction === Direction.RIGHT_UP){
        condOfDirLeftDown = true;
        
        // IF COMES FROM RIGHT_DOWN OR LEFT_UP)
      } else if(direction !== Direction.LEFT_DOWN){
        condOfDirLeftDown = false; // allows to put side
        
        // IF COMES FROM THE BOTTOM LEFT (DOWN) 
      } else{
        condOfDirLeftDown = inheritedOperation; // use previous decision
      }
      
      // LEFT UP CONDITION
        // IF COMES FROM THE RIGHT DOWN
      if(direction === Direction.RIGHT_DOWN){
        condOfDirLeftUp = true;
        
        // IF COMES FROM RIGHT_TOP OR LEFT_DOWN)
      } else if(direction !== Direction.LEFT_UP){
        condOfDirLeftUp = false; // allows to put side
        
        // IF COMES FROM THE TOP LEFT
      } else{
        condOfDirLeftUp = inheritedOperation; // use previous decision
      }
      
      // IN THE LAST ITERATION
      if(iteration == 1){
        // IF PART OF THE W-CURVE FRACTAL IS NOT CREATED FROM RIGHT DOWN AND ALSO NOT FROM TOP LEFT OR FROM PREVIOUS (INHERITED) TOP LEFT DIRECTION
        if(direction !== Direction.RIGHT_DOWN && (direction !== Direction.LEFT_UP || condOfDirLeftUp === false))
        {
          drawLine(context, square1x, square1y, square1x, verticalToSquare1y, wcurve.thickness); //vertical (top left)
          drawLine(context, square1x, square1y, horizontalToSquare1x, square1y, wcurve.thickness); //horizontal (top left)
        }
        
        // IF PART OF THE W-CURVE FRACTAL IS NOT CREATED FROM LEFT DOWN AND ALSO NOT FROM TOP RIGHT OR FROM PREVIOUS (INHERITED) TOP RIGHT DIRECTION
        if(direction !== Direction.LEFT_DOWN && (direction !== Direction.RIGHT_UP || condOfDirRightUp === false))
        {
          drawLine(context, square2x, square2y, square2x, verticalToSquare2y, wcurve.thickness); //vertical (top right)
          drawLine(context, square2x, square2y, horizontalToSquare2x, square2y, wcurve.thickness); //horizontal (top right)
        }
        
        // IF PART OF THE W-CURVE FRACTAL IS NOT CREATED FROM RIGHT UP AND ALSO NOT FROM LEFT DOWN OR FROM PREVIOUS (INHERITED) LEFT DOWN DIRECTION
        if(direction !== Direction.RIGHT_UP && (direction !== Direction.LEFT_DOWN || condOfDirLeftDown === false))
        {
          drawLine(context, square3x, square3y, square3x, verticalToSquare3y, wcurve.thickness); //vertical (left down)
          drawLine(context, square3x, square3y, horizontalToSquare3x, square3y, wcurve.thickness); //horizontal (left down)
        }
        
        // IF PART OF THE W-CURVE FRACTAL IS NOT CREATED FROM LEFT UP AND ALSO NOT FROM BOTTOM RIGHT (DOWN) OR FROM PREVIOUS (INHERITED) BOTTOM RIGHT (DOWN) DIRECTION
        if(direction !== Direction.LEFT_UP && (direction !== Direction.RIGHT_DOWN || condOfDirRightDown === false))
        {
          drawLine(context, square4x, square4y, square4x, verticalToSquare4y, wcurve.thickness); //vertical (right down)
          drawLine(context, square4x, square4y, horizontalToSquare4x, square4y, wcurve.thickness); //horizontal (right down)
        }
      } 
      
      // DRAWS BOUNDARY LINES (AS IN AMBULANCE CROSS)
        // DRAWS TOP LINE
      drawLine(context, square1x, verticalToSquare1y, square2x, verticalToSquare2y, wcurve.thickness);
        // DRAWS BOTTOM LINE
      drawLine(context, square3x, verticalToSquare3y, square4x, verticalToSquare4y, wcurve.thickness);
        // DRAWS LEFT LINE
      drawLine(context, horizontalToSquare1x, square1y, horizontalToSquare3x, square3y, wcurve.thickness);
        // DRAWS RIGHT LINE
      drawLine(context, horizontalToSquare2x, square2y, horizontalToSquare4x, square4y, wcurve.thickness);
      
      // EXTENDS RECURSION
        // DRAWS W-CURVE PART ON THE TOP LEFT
      this.drawWCurve(iteration - 1, moveRatioIteration / 2.0, newLeftUpX, newLeftUpY, wcurve, context, Direction.LEFT_UP, condOfDirLeftUp);
        // DRAWS W-CURVE PART ON THE TOP RIGHT
      this.drawWCurve(iteration - 1, moveRatioIteration / 2.0, newRightUpX, newRightUpY, wcurve, context, Direction.RIGHT_UP, condOfDirRightUp);
        // DRAWS W-CURVE PART ON THE LEFT DOWN
      this.drawWCurve(iteration - 1, moveRatioIteration / 2.0, newLeftBottomX, newLeftBottomY, wcurve, context, Direction.LEFT_DOWN, condOfDirLeftDown);
        // DRAWS W-CURVE PART ON THE RIGHT DOWN
      this.drawWCurve(iteration - 1, moveRatioIteration / 2.0, newRightBottomX, newRightBottomY, wcurve, context, Direction.RIGHT_DOWN,condOfDirRightDown);	
    }
  }

}


export function drawWCurve(canvas: HTMLCanvasElement, context: CanvasRenderingContext2D,  
   numberIterations: number, lineLength: number, thickness: number, distanceWidthRadius: number): void {
    const wcurve = new Wcurve(canvas.width/4, lineLength, thickness, distanceWidthRadius);
	
    wcurve.initialize(numberIterations);
    wcurve.drawWCurve(numberIterations, wcurve.moveRatio / 2.0, canvas.width/2.0, canvas.width/2.0,
        wcurve, context, Direction.ALL, false);
}
`;