
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
export class DoubledWcurve {
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
  drawWCurve(iteration: number, moveRatioIteration: number, centerX: number, centerY: number, wcurve: Wcurve, 
        context: CanvasRenderingContext2D , direction: Direction, inheritedOperation: boolean): void {
    let square1x, square1y, square2x, square2y, square3x, square3y, square4x, square4y;
    let verticalToSquare1y, horizontalToSquare1x;
    let verticalToSquare2y, horizontalToSquare2x;
    let verticalToSquare3y, horizontalToSquare3x;
    let verticalToSquare4y, horizontalToSquare4x;
    
    let newLeftUpX, newLeftUpY, newRightUpX, newRightUpY;
    let newLeftBottomX, newLeftBottomY, newRightBottomX, newRightBottomY;
    let distanceOfNewOnes = moveRatioIteration * wcurve.lineLength;
    
    let condOfDirRightUp, condOfDirRightDown, condOfDirLeftDown, condOfDirLeftUp;
    
    if(iteration > 0){
      square1x = centerX - wcurve.lineLengthHalf;
      square1y = centerY - wcurve.lineLengthHalf;
      
      square2x = centerX + wcurve.lineLengthHalf;
      square2y = centerY - wcurve.lineLengthHalf;
      
      square3x = centerX - wcurve.lineLengthHalf;
      square3y = centerY + wcurve.lineLengthHalf;
      
      square4x = centerX + wcurve.lineLengthHalf;
      square4y = centerY + wcurve.lineLengthHalf;
      
      
      verticalToSquare1y = square1y - wcurve.lineLength;
      horizontalToSquare1x = square1x - wcurve.lineLength;
      
      verticalToSquare2y = square2y - wcurve.lineLength;
      horizontalToSquare2x = square2x + wcurve.lineLength;
      
      verticalToSquare3y = square3y + wcurve.lineLength;
      horizontalToSquare3x = square3x - wcurve.lineLength;
      
      verticalToSquare4y = square4y + wcurve.lineLength;
      horizontalToSquare4x = square4x + wcurve.lineLength;
      
      
      newLeftUpX = centerX - distanceOfNewOnes;
      newLeftUpY = centerY - distanceOfNewOnes;
      
      newRightUpX = centerX + distanceOfNewOnes;
      newRightUpY = centerY - distanceOfNewOnes;
      
      newLeftBottomX = centerX - distanceOfNewOnes;
      newLeftBottomY = centerY + distanceOfNewOnes;
      
      newRightBottomX = centerX + distanceOfNewOnes;
      newRightBottomY = centerY + distanceOfNewOnes;
      
      
      if(direction === Direction.LEFT_DOWN){
        condOfDirRightUp = true;
      } else if(direction !== Direction.RIGHT_UP){
        condOfDirRightUp = false;
      } else{
        condOfDirRightUp = inheritedOperation;
      }
      
      if(direction === Direction.LEFT_UP){
        condOfDirRightDown = true;
      } else if(direction != Direction.RIGHT_DOWN){
        condOfDirRightDown = false;
      } else{
        condOfDirRightDown = inheritedOperation;
      }
      
      if(direction === Direction.RIGHT_UP){
        condOfDirLeftDown = true;
      } else if(direction !== Direction.LEFT_DOWN){
        condOfDirLeftDown = false;
      } else{
        condOfDirLeftDown = inheritedOperation;
      }
      
      if(direction === Direction.RIGHT_DOWN){
        condOfDirLeftUp = true;
      } else if(direction !== Direction.LEFT_UP){
        condOfDirLeftUp = false;
      } else{
        condOfDirLeftUp = inheritedOperation;
      }
      
      if(iteration === 1){
        if(direction !== Direction.RIGHT_DOWN && (direction !== Direction.LEFT_UP || condOfDirLeftUp === false)){
          this.drawCrossedLine(context, square1x - wcurve.distanceWidthRadius, square1y - wcurve.distanceWidthRadius, 
                               square1x - wcurve.distanceWidthRadius, verticalToSquare1y - wcurve.distanceWidthRadius, 
                               horizontalToSquare1x - wcurve.distanceWidthRadius, square1y - wcurve.distanceWidthRadius, 
                               wcurve.thickness);
          this.drawCrossedLine(context, square1x + wcurve.distanceWidthRadius, square1y + wcurve.distanceWidthRadius,
                               square1x + wcurve.distanceWidthRadius, verticalToSquare1y + wcurve.distanceWidthRadius, 
                               horizontalToSquare1x + wcurve.distanceWidthRadius, square1y + wcurve.distanceWidthRadius, 
                               wcurve.thickness);
        }
      
        
        if(direction !== Direction.LEFT_DOWN && (direction !== Direction.RIGHT_UP || condOfDirRightUp === false)){
          this.drawCrossedLine(context, square2x + wcurve.distanceWidthRadius, square2y - wcurve.distanceWidthRadius, 
                               square2x + wcurve.distanceWidthRadius, verticalToSquare2y - wcurve.distanceWidthRadius,
                               horizontalToSquare2x + wcurve.distanceWidthRadius, square2y - wcurve.distanceWidthRadius, 
                               wcurve.thickness);
          this.drawCrossedLine(context, square2x - wcurve.distanceWidthRadius, square2y + wcurve.distanceWidthRadius, 
                               square2x - wcurve.distanceWidthRadius, verticalToSquare2y + wcurve.distanceWidthRadius, 
                               horizontalToSquare2x - wcurve.distanceWidthRadius, square2y + wcurve.distanceWidthRadius,                     wcurve.thickness);
        }
        
        
        if(direction !== Direction.RIGHT_UP && (direction !== Direction.LEFT_DOWN || condOfDirLeftDown === false)) {
          this.drawCrossedLine(context, square3x - wcurve.distanceWidthRadius, square3y + wcurve.distanceWidthRadius, 
                               square3x - wcurve.distanceWidthRadius, verticalToSquare3y + wcurve.distanceWidthRadius,
                               horizontalToSquare3x - wcurve.distanceWidthRadius, square3y + wcurve.distanceWidthRadius, 
                               wcurve.thickness);
          this.drawCrossedLine(context, square3x + wcurve.distanceWidthRadius, square3y - wcurve.distanceWidthRadius, 
                               square3x + wcurve.distanceWidthRadius, verticalToSquare3y - wcurve.distanceWidthRadius, 
                               horizontalToSquare3x + wcurve.distanceWidthRadius, square3y - wcurve.distanceWidthRadius, 
                               wcurve.thickness);
        }  
        
        
        if(direction !== Direction.LEFT_UP && (direction !== Direction.RIGHT_DOWN || condOfDirRightDown === false)) {
          this.drawCrossedLine(context, square4x + wcurve.distanceWidthRadius, square4y + wcurve.distanceWidthRadius,
                               square4x + wcurve.distanceWidthRadius, verticalToSquare4y + wcurve.distanceWidthRadius,
                               horizontalToSquare4x + wcurve.distanceWidthRadius, square4y + wcurve.distanceWidthRadius, 
                               wcurve.thickness);
          this.drawCrossedLine(context, square4x - wcurve.distanceWidthRadius, square4y - wcurve.distanceWidthRadius, 
                               square4x - wcurve.distanceWidthRadius, verticalToSquare4y - wcurve.distanceWidthRadius, 
                               horizontalToSquare4x - wcurve.distanceWidthRadius, square4y - wcurve.distanceWidthRadius,   
                               wcurve.thickness);
        } 
        
        
        if(direction === Direction.RIGHT_DOWN) {
          drawLine(context, square1x - wcurve.distanceWidthRadius, verticalToSquare1y + wcurve.distanceWidthRadius, square2x - wcurve.distanceWidthRadius, verticalToSquare2y + wcurve.distanceWidthRadius, wcurve.thickness);
          drawLine(context, square1x + wcurve.distanceWidthRadius, verticalToSquare1y - wcurve.distanceWidthRadius, square2x + wcurve.distanceWidthRadius, verticalToSquare2y - wcurve.distanceWidthRadius, wcurve.thickness);
          
          drawLine(context, horizontalToSquare1x + wcurve.distanceWidthRadius, square1y - wcurve.distanceWidthRadius, horizontalToSquare3x + wcurve.distanceWidthRadius, square3y - wcurve.distanceWidthRadius, wcurve.thickness);
          drawLine(context, horizontalToSquare1x - wcurve.distanceWidthRadius, square1y + wcurve.distanceWidthRadius, horizontalToSquare3x - wcurve.distanceWidthRadius, square3y + wcurve.distanceWidthRadius, wcurve.thickness);
        
        } 
        
        
        if(direction === Direction.RIGHT_UP){
          drawLine(context, square3x + wcurve.distanceWidthRadius, verticalToSquare3y + wcurve.distanceWidthRadius, square4x + wcurve.distanceWidthRadius, verticalToSquare4y + wcurve.distanceWidthRadius, wcurve.thickness);
          drawLine(context, square3x - wcurve.distanceWidthRadius, verticalToSquare3y - wcurve.distanceWidthRadius, square4x - wcurve.distanceWidthRadius, verticalToSquare4y - wcurve.distanceWidthRadius, wcurve.thickness);
          
          drawLine(context, horizontalToSquare1x + wcurve.distanceWidthRadius, square1y + wcurve.distanceWidthRadius, horizontalToSquare3x + wcurve.distanceWidthRadius, square3y + wcurve.distanceWidthRadius, wcurve.thickness);
          drawLine(context, horizontalToSquare1x - wcurve.distanceWidthRadius, square1y - wcurve.distanceWidthRadius, horizontalToSquare3x - wcurve.distanceWidthRadius, square3y - wcurve.distanceWidthRadius, wcurve.thickness);
        }
        
        if(direction === Direction.LEFT_UP ) {		
          drawLine(context, square3x + wcurve.distanceWidthRadius, verticalToSquare3y - wcurve.distanceWidthRadius, square4x + wcurve.distanceWidthRadius, verticalToSquare4y - wcurve.distanceWidthRadius, wcurve.thickness);
          drawLine(context, square3x - wcurve.distanceWidthRadius, verticalToSquare3y + wcurve.distanceWidthRadius, square4x - wcurve.distanceWidthRadius, verticalToSquare4y + wcurve.distanceWidthRadius, wcurve.thickness);
          
          drawLine(context, horizontalToSquare2x - wcurve.distanceWidthRadius, square2y + wcurve.distanceWidthRadius, horizontalToSquare4x - wcurve.distanceWidthRadius, square4y + wcurve.distanceWidthRadius, wcurve.thickness);
          drawLine(context, horizontalToSquare2x + wcurve.distanceWidthRadius, square2y - wcurve.distanceWidthRadius, horizontalToSquare4x + wcurve.distanceWidthRadius, square4y - wcurve.distanceWidthRadius, wcurve.thickness);
        }
        
        
        if(direction === Direction.LEFT_DOWN){
          drawLine(context, square1x - wcurve.distanceWidthRadius, verticalToSquare1y - wcurve.distanceWidthRadius, square2x - wcurve.distanceWidthRadius, verticalToSquare2y - wcurve.distanceWidthRadius, wcurve.thickness);
          drawLine(context, square1x + wcurve.distanceWidthRadius, verticalToSquare1y + wcurve.distanceWidthRadius, square2x + wcurve.distanceWidthRadius, verticalToSquare2y + wcurve.distanceWidthRadius, wcurve.thickness);
          
          drawLine(context, horizontalToSquare2x - wcurve.distanceWidthRadius, square2y - wcurve.distanceWidthRadius, horizontalToSquare4x - wcurve.distanceWidthRadius, square4y - wcurve.distanceWidthRadius, wcurve.thickness);
          drawLine(context, horizontalToSquare2x + wcurve.distanceWidthRadius, square2y + wcurve.distanceWidthRadius, horizontalToSquare4x + wcurve.distanceWidthRadius, square4y + wcurve.distanceWidthRadius, wcurve.thickness);
        }
        
        
        if(direction === Direction.LEFT_UP){
          if(condOfDirLeftUp === false){
            drawLine(context, square1x - wcurve.distanceWidthRadius, verticalToSquare1y - wcurve.distanceWidthRadius, square2x + wcurve.distanceWidthRadius, verticalToSquare2y - wcurve.distanceWidthRadius, wcurve.thickness);
            drawLine(context, square1x + wcurve.distanceWidthRadius, verticalToSquare1y + wcurve.distanceWidthRadius, square2x - wcurve.distanceWidthRadius, verticalToSquare2y + wcurve.distanceWidthRadius, wcurve.thickness);
            
            drawLine(context, horizontalToSquare1x + wcurve.distanceWidthRadius, square1y + wcurve.distanceWidthRadius, horizontalToSquare3x + wcurve.distanceWidthRadius, square3y - wcurve.distanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare1x - wcurve.distanceWidthRadius, square1y - wcurve.distanceWidthRadius, horizontalToSquare3x - wcurve.distanceWidthRadius, square3y + wcurve.distanceWidthRadius, wcurve.thickness);
          } else {
            drawLine(context, square1x - wcurve.distanceWidthRadius, verticalToSquare1y + wcurve.distanceWidthRadius, square2x - wcurve.distanceWidthRadius, verticalToSquare2y + wcurve.distanceWidthRadius, wcurve.thickness);
            drawLine(context, square1x + wcurve.distanceWidthRadius, verticalToSquare1y - wcurve.distanceWidthRadius, square2x + wcurve.distanceWidthRadius, verticalToSquare2y - wcurve.distanceWidthRadius, wcurve.thickness);
            
            drawLine(context, horizontalToSquare1x - wcurve.distanceWidthRadius, square1y + wcurve.distanceWidthRadius, horizontalToSquare3x - wcurve.distanceWidthRadius, square3y + wcurve.distanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare1x + wcurve.distanceWidthRadius, square1y - wcurve.distanceWidthRadius, horizontalToSquare3x + wcurve.distanceWidthRadius, square3y - wcurve.distanceWidthRadius, wcurve.thickness);
          }
        }
        
        if(direction === Direction.RIGHT_UP){
          if(condOfDirRightUp === false){
            drawLine(context, square1x - wcurve.distanceWidthRadius, verticalToSquare1y - wcurve.distanceWidthRadius, square2x + wcurve.distanceWidthRadius, verticalToSquare2y - wcurve.distanceWidthRadius, wcurve.thickness);
            drawLine(context, square1x + wcurve.distanceWidthRadius, verticalToSquare1y + wcurve.distanceWidthRadius, square2x - wcurve.distanceWidthRadius, verticalToSquare2y + wcurve.distanceWidthRadius, wcurve.thickness);
            
            drawLine(context, horizontalToSquare2x - wcurve.distanceWidthRadius, square2y + wcurve.distanceWidthRadius, horizontalToSquare4x - wcurve.distanceWidthRadius, square4y - wcurve.distanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare2x + wcurve.distanceWidthRadius, square2y - wcurve.distanceWidthRadius, horizontalToSquare4x + wcurve.distanceWidthRadius, square4y + wcurve.distanceWidthRadius, wcurve.thickness);
          } else {
            drawLine(context, square1x - wcurve.distanceWidthRadius, verticalToSquare1y - wcurve.distanceWidthRadius, square2x - wcurve.distanceWidthRadius, verticalToSquare2y - wcurve.distanceWidthRadius, wcurve.thickness);
            drawLine(context, square1x + wcurve.distanceWidthRadius, verticalToSquare1y + wcurve.distanceWidthRadius, square2x + wcurve.distanceWidthRadius, verticalToSquare2y + wcurve.distanceWidthRadius, wcurve.thickness);
              
            drawLine(context, horizontalToSquare2x + wcurve.distanceWidthRadius, square2y + wcurve.distanceWidthRadius, horizontalToSquare4x + wcurve.distanceWidthRadius, square4y + wcurve.distanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare2x - wcurve.distanceWidthRadius, square2y - wcurve.distanceWidthRadius, horizontalToSquare4x - wcurve.distanceWidthRadius, square4y - wcurve.distanceWidthRadius, wcurve.thickness);
          }
        }
        
        if(direction === Direction.LEFT_DOWN){
          if(condOfDirLeftDown === false){
            drawLine(context, square3x - wcurve.distanceWidthRadius, verticalToSquare3y + wcurve.distanceWidthRadius, square4x + wcurve.distanceWidthRadius, verticalToSquare4y + wcurve.distanceWidthRadius, wcurve.thickness);
            drawLine(context, square3x + wcurve.distanceWidthRadius, verticalToSquare3y - wcurve.distanceWidthRadius, square4x - wcurve.distanceWidthRadius, verticalToSquare4y - wcurve.distanceWidthRadius, wcurve.thickness);
            
            drawLine(context, horizontalToSquare1x + wcurve.distanceWidthRadius, square1y + wcurve.distanceWidthRadius, horizontalToSquare3x + wcurve.distanceWidthRadius, square3y - wcurve.distanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare1x - wcurve.distanceWidthRadius, square1y - wcurve.distanceWidthRadius, horizontalToSquare3x - wcurve.distanceWidthRadius, square3y + wcurve.distanceWidthRadius, wcurve.thickness);
          } else {
            drawLine(context, square3x + wcurve.distanceWidthRadius, verticalToSquare3y + wcurve.distanceWidthRadius, square4x + wcurve.distanceWidthRadius, verticalToSquare4y + wcurve.distanceWidthRadius, wcurve.thickness);
            drawLine(context, square3x - wcurve.distanceWidthRadius, verticalToSquare3y - wcurve.distanceWidthRadius, square4x - wcurve.distanceWidthRadius, verticalToSquare4y - wcurve.distanceWidthRadius, wcurve.thickness);
            
            drawLine(context, horizontalToSquare1x + wcurve.distanceWidthRadius, square1y + wcurve.distanceWidthRadius, horizontalToSquare3x + wcurve.distanceWidthRadius, square3y + wcurve.distanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare1x - wcurve.distanceWidthRadius, square1y - wcurve.distanceWidthRadius, horizontalToSquare3x - wcurve.distanceWidthRadius, square3y - wcurve.distanceWidthRadius, wcurve.thickness);
          }
        }
        
        
        if(direction === Direction.RIGHT_DOWN){
          if(condOfDirRightDown === false){
            drawLine(context, square3x - wcurve.distanceWidthRadius, verticalToSquare3y + wcurve.distanceWidthRadius, square4x + wcurve.distanceWidthRadius, verticalToSquare4y + wcurve.distanceWidthRadius, wcurve.thickness);
            drawLine(context, square3x + wcurve.distanceWidthRadius, verticalToSquare3y - wcurve.distanceWidthRadius, square4x - wcurve.distanceWidthRadius, verticalToSquare4y - wcurve.distanceWidthRadius, wcurve.thickness);
            
            drawLine(context, horizontalToSquare2x + wcurve.distanceWidthRadius, square2y - wcurve.distanceWidthRadius, horizontalToSquare4x + wcurve.distanceWidthRadius, square4y + wcurve.distanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare2x - wcurve.distanceWidthRadius, square2y + wcurve.distanceWidthRadius, horizontalToSquare4x - wcurve.distanceWidthRadius, square4y - wcurve.distanceWidthRadius, wcurve.thickness);
          } else {
            drawLine(context, square3x + wcurve.distanceWidthRadius, verticalToSquare3y - wcurve.distanceWidthRadius, square4x + wcurve.distanceWidthRadius, verticalToSquare4y - wcurve.distanceWidthRadius, wcurve.thickness);
            drawLine(context, square3x - wcurve.distanceWidthRadius, verticalToSquare3y + wcurve.distanceWidthRadius, square4x - wcurve.distanceWidthRadius, verticalToSquare4y + wcurve.distanceWidthRadius, wcurve.thickness);
            
            drawLine(context, horizontalToSquare2x + wcurve.distanceWidthRadius, square2y - wcurve.distanceWidthRadius, horizontalToSquare4x + wcurve.distanceWidthRadius, square4y - wcurve.distanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare2x - wcurve.distanceWidthRadius, square2y + wcurve.distanceWidthRadius, horizontalToSquare4x - wcurve.distanceWidthRadius, square4y + wcurve.distanceWidthRadius, wcurve.thickness);
          }
        }
        
      } else {			
        drawLine(context, square1x - wcurve.distanceWidthRadius, verticalToSquare1y + wcurve.distanceWidthRadius, square2x + wcurve.distanceWidthRadius, verticalToSquare2y + wcurve.distanceWidthRadius, wcurve.thickness);
        drawLine(context, square1x + wcurve.distanceWidthRadius, verticalToSquare1y - wcurve.distanceWidthRadius, square2x - wcurve.distanceWidthRadius, verticalToSquare2y - wcurve.distanceWidthRadius, wcurve.thickness);
      
      
        drawLine(context, square3x - wcurve.distanceWidthRadius, verticalToSquare3y - wcurve.distanceWidthRadius, square4x + wcurve.distanceWidthRadius, verticalToSquare4y - wcurve.distanceWidthRadius, wcurve.thickness);
        drawLine(context, square3x + wcurve.distanceWidthRadius, verticalToSquare3y + wcurve.distanceWidthRadius, square4x - wcurve.distanceWidthRadius, verticalToSquare4y + wcurve.distanceWidthRadius, wcurve.thickness);
    
    
        drawLine(context, horizontalToSquare1x + wcurve.distanceWidthRadius, square1y - wcurve.distanceWidthRadius, horizontalToSquare3x + wcurve.distanceWidthRadius, square3y + wcurve.distanceWidthRadius, wcurve.thickness);
        drawLine(context, horizontalToSquare1x - wcurve.distanceWidthRadius, square1y + wcurve.distanceWidthRadius, horizontalToSquare3x - wcurve.distanceWidthRadius, square3y - wcurve.distanceWidthRadius, wcurve.thickness);
      
      
        drawLine(context, horizontalToSquare2x - wcurve.distanceWidthRadius, square2y - wcurve.distanceWidthRadius, horizontalToSquare4x - wcurve.distanceWidthRadius, square4y + wcurve.distanceWidthRadius, wcurve.thickness);
        drawLine(context, horizontalToSquare2x + wcurve.distanceWidthRadius, square2y + wcurve.distanceWidthRadius, horizontalToSquare4x + wcurve.distanceWidthRadius, square4y - wcurve.distanceWidthRadius, wcurve.thickness);
      }
      
      
      this.drawWCurve(iteration - 1, moveRatioIteration / 2.0, newLeftUpX, newLeftUpY, wcurve, context, Direction.LEFT_UP, condOfDirLeftUp);
      this.drawWCurve(iteration - 1, moveRatioIteration / 2.0, newRightUpX, newRightUpY, wcurve, context, Direction.RIGHT_UP, condOfDirRightUp);
      this.drawWCurve(iteration - 1, moveRatioIteration / 2.0, newLeftBottomX, newLeftBottomY, wcurve, context, Direction.LEFT_DOWN,  condOfDirLeftDown);
      this.drawWCurve(iteration - 1, moveRatioIteration / 2.0, newRightBottomX, newRightBottomY, wcurve, context, Direction.RIGHT_DOWN,condOfDirRightDown);	
    }
  }
}

export function drawDoubledWcurve(canvas: HTMLCanvasElement, context: CanvasRenderingContext2D,  
   numberIterations: number, lineLength: number, thickness: number, distanceWidthRadius: number): void {
    const wcurve = new DoubledWcurve(canvas.width/4, lineLength, thickness, distanceWidthRadius);
	
    wcurve.initialize(numberIterations);
    wcurve.drawWCurve(numberIterations, wcurve.moveRatio / 2.0, canvas.width/2.0, canvas.width/2.0,
        wcurve, context, Direction.ALL, false);
}
`;