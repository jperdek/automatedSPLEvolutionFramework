
var aaa1 = `
/*
	PREVIOUS COORDINATES REPRESENTATION
	
	@param {number} x - previous coordinate x
	@param {number} y - previous coordinate y
*/
class PreviousCoordinates {
  x: number;
  y: number;

  constructor(x: number, y: number) {
	  this.x = x;
	  this.y = y;
  }
}


/*
	DRAWS TOP RIGHT PART OF W-CURVE - WITH EXTENDING RECURSION
	
	@param {number} iteration - actual performed iteration
	@param {Context} context - context from Canvas to perform paint operations
	@param {number} move - length of line/step
	@param {number} previousX - value of previous x coordinate (previously used to draw)
	@param {number} previousY - value of previous y coordinate (previously used to draw)
*/
export function shapeUpRight(iteration: number, context: CanvasRenderingContext2D, 
      move: number, previousX: number, previousY: number): PreviousCoordinates {
	let previousCoordinates: PreviousCoordinates;
	// STOPPING CONDITION
	if(iteration > 1) {
		// DRAWS TOP RIGHT SIDE
		// PERFORMS TOP RIGHT MOVEMENT (IN THE LAST ITERATION ONLY TO THE BOTTOM)
		previousCoordinates = shapeUpRight(iteration - 1, context, move, previousX, previousY);
		previousX = previousCoordinates.x;
		previousY = previousCoordinates.y;
		
		// PERFORMS LINE TO THE RIGHT + SAVES POSITION
		context.lineTo(move + previousX, previousY)
		previousX = previousX + move;
		// PERFORMS LINE TO THE BOTTOM + SAVES POSITION
		context.lineTo(previousX, move + previousY);
		previousY = previousY + move;
		
		
		// DRAWS BOTTOM RIGHT SIDE
		// PERFORMS BOTTOM RIGHT MOVEMENT (IN THE LAST ITERATION ONLY TO THE RIGHT)
		previousCoordinates = shapeBottomRight(iteration - 1, context, move, previousX, previousY);
		previousX = previousCoordinates.x;
		previousY = previousCoordinates.y;
		// PERFORMS LINE TO THE BOTTOM + SAVES POSITION
		context.lineTo(previousX, move + previousY);
		previousY = previousY + move;
		
		
		// DRAWS TOP LEFT SIDE
		// PERFORMS TOP LEFT MOVEMENT (IN THE LAST ITERATION ONLY TO THE LEFT)
		previousCoordinates = shapeUpLeft(iteration - 1, context, move, previousX, previousY);
		previousX = previousCoordinates.x;
		previousY = previousCoordinates.y;
		
		// PERFORMS LINE TO THE BOTTOM + SAVES POSITION
		context.lineTo(previousX, move + previousY);
		previousY = previousY + move;
		// PERFORMS LINE TO THE LEFT + SAVES POSITION
		context.lineTo(previousX - move, previousY);
		previousX = previousX - move;
		
		
		// DRAWS TOP RIGHT SIDE
		// PERFORMS TOP RIGHT MOVEMENT (IN THE LAST ITERATION ONLY TO THE BOTTOM)
		return shapeUpRight(iteration - 1, context, move, previousX, previousY);
		
	// DRAWS LINE TO THE BOTTOM IN THE LAST ITERATION + SAVES THIS MOVEMENT
	} else {
		context.lineTo(previousX, move + previousY);
		previousY = previousY + move;
	}
	
	return new PreviousCoordinates(previousX, previousY);
}


/*
	DRAWS TOP LEFT PART OF W-CURVE - WITH EXTENDING RECURSION
	
	@param {number} iteration - actual performed iteration
	@param {Context} context - context from Canvas to perform paint operations
	@param {number} move - length of line/step
	@param {number} previousX - value of previous x coordinate (previously used to draw)
	@param {number} previousY - value of previous y coordinate (previously used to draw)
*/
export function shapeUpLeft(iteration: number, context: CanvasRenderingContext2D, 
      move: number, previousX: number, previousY: number): PreviousCoordinates {
	let previousCoordinates: PreviousCoordinates;
	
	// STOPPING CONDITION
	if(iteration > 1) {
		// DRAWS TOP LEFT SIDE
		// PERFORMS TOP LEFT MOVEMENT (IN THE LAST ITERATION ONLY TO THE LEFT)
		previousCoordinates = shapeUpLeft(iteration - 1, context, move, previousX, previousY);
		previousX = previousCoordinates.x;
		previousY = previousCoordinates.y;
		// PERFORMS LINE TO THE BOTTOM + SAVES POSITION
		context.lineTo(previousX, previousY + move);
		previousY = previousY + move;
		// PERFORMS LINE TO THE LEFT + SAVES POSITION
		context.lineTo(previousX - move, previousY);
		previousX = previousX - move;
		
		
		// DRAWS TOP RIGHT SIDE
		// PERFORMS TOP RIGHT MOVEMENT (IN THE LAST ITERATION ONLY TO THE BOTTOM)
		previousCoordinates = shapeUpRight(iteration - 1, context, move, previousX, previousY);
		previousX = previousCoordinates.x;
		previousY = previousCoordinates.y;
		// PERFORMS LINE TO THE LEFT + SAVES POSITION
		context.lineTo(previousX - move, previousY);
		previousX = previousX - move;
		
		
		// DRAWS BOTTOM LEFT SIDE
		// PERFORMS BOTTOM LEFT MOVEMENT (IN THE LAST ITERATION ONLY TO THE BOTTOM)
		previousCoordinates = shapeBottomLeft(iteration - 1, context, move, previousX, previousY);
		previousX = previousCoordinates.x;
		previousY = previousCoordinates.y;
		// PERFORMS LINE TO THE LEFT + SAVES POSITION
		context.lineTo(previousX - move, previousY);
		previousX = previousX - move;
		// PERFORMS LINE TO THE TOP + SAVES POSITION
		context.lineTo(previousX, previousY - move);
		previousY = previousY - move;
		
		
		// DRAWS TOP LEFT SIDE
		// PERFORMS TOP LEFT MOVEMENT (IN THE LAST ITERATION ONLY TO THE LEFT)
		return shapeUpLeft(iteration - 1, context, move, previousX, previousY);
		
	// DRAWS LINE TO THE LEFT IN THE LAST ITERATION + SAVES THIS MOVEMENT
	} else {
		context.lineTo(previousX - move, previousY);
		previousX = previousX - move;
	}
	
	return new PreviousCoordinates(previousX, previousY);
}


/*
	DRAWS BOTTOM LEFT PART OF W-CURVE - WITH EXTENDING RECURSION
	
	@param {number} iteration - actual performed iteration
	@param {Context} context - context from Canvas to perform paint operations
	@param {number} move - length of line/step
	@param {number} previousX - value of previous x coordinate (previously used to draw)
	@param {number} previousY - value of previous y coordinate (previously used to draw)
*/
export function shapeBottomLeft(iteration: number, context: CanvasRenderingContext2D, 
      move: number, previousX: number, previousY: number): PreviousCoordinates {
	let previousCoordinates: PreviousCoordinates;

	// STOPPING CONDITION
	if(iteration > 1){
		// DRAWS BOTTOM LEFT SIDE
		// PERFORMS BOTTOM LEFT MOVEMENT (IN THE LAST ITERATION ONLY TO THE BOTTOM)
		previousCoordinates = shapeBottomLeft(iteration - 1, context, move, previousX, previousY);
		previousX = previousCoordinates.x;
		previousY = previousCoordinates.y;
		// PERFORMS LINE TO THE LEFT + SAVES POSITION
		context.lineTo(previousX - move, previousY);
		previousX = previousX - move;
		// PERFORMS LINE TO THE TOP + SAVES POSITION
		context.lineTo(previousX, previousY - move);
		previousY = previousY - move;
		

		// DRAWS TOP LEFT SIDE
		// PERFORMS TOP LEFT MOVEMENT (IN THE LAST ITERATION ONLY TO THE LEFT)
		previousCoordinates = shapeUpLeft(iteration - 1, context, move, previousX, previousY);
		previousX = previousCoordinates.x;
		previousY = previousCoordinates.y;
		// PERFORMS LINE TO THE TOP + SAVES POSITION
		context.lineTo(previousX, previousY - move);
		previousY = previousY - move;
		
		
		// DRAWS BOTTOM RIGHT SIDE
		// PERFORMS BOTTOM RIGHT MOVEMENT (IN THE LAST ITERATION ONLY TO THE RIGHT)
		previousCoordinates = shapeBottomRight(iteration - 1, context, move, previousX, previousY);
		previousX = previousCoordinates.x;
		previousY = previousCoordinates.y;
		// PERFORMS LINE TO THE TOP + SAVES POSITION
		context.lineTo(previousX, previousY - move);
		previousY = previousY - move;
		// PERFORMS LINE TO THE RIGHT + SAVES POSITION
		context.lineTo(previousX + move, previousY);
		previousX = previousX + move;
		
		// DRAWS BOTTOM LEFT SIDE
		// PERFORMS BOTTOM LEFT MOVEMENT (IN THE LAST ITERATION ONLY TO THE BOTTOM)
		return shapeBottomLeft(iteration - 1, context, move, previousX, previousY);
		
	// DRAWS LINE TO THE BOTTOM IN THE LAST ITERATION + SAVES THIS MOVEMENT
	} else {
		context.lineTo(previousX, previousY - move);
		previousY = previousY - move;
	}
	
	return new PreviousCoordinates(previousX, previousY);
}


/*
	DRAWS BOTTOM RIGHT PART OF W-CURVE - WITH EXTENDING RECURSION
	
	@param {number} iteration - actual performed iteration
	@param {Context} context - context from Canvas to perform paint operations
	@param {number} move - length of line/step
	@param {number} previousX - value of previous x coordinate (previously used to draw)
	@param {number} previousY - value of previous y coordinate (previously used to draw)
*/
export function shapeBottomRight(iteration: number, context: CanvasRenderingContext2D, 
      move: number, previousX: number, previousY: number): PreviousCoordinates {
	let previousCoordinates: PreviousCoordinates;
	
	// STOPPING CONDITION
	if(iteration > 1){
		// DRAWS BOTTOM RIGHT SIDE
		// PERFORMS BOTTOM RIGHT MOVEMENT (IN THE LAST ITERATION ONLY TO THE RIGHT)
		previousCoordinates = shapeBottomRight(iteration -1, context, move, previousX, previousY);
		previousX = previousCoordinates.x;
		previousY = previousCoordinates.y;
		
		// PERFORMS LINE TO THE TOP + SAVES POSITION
		context.lineTo(previousX, previousY - move);
		previousY = previousY - move;
		// PERFORMS LINE TO THE RIGHT + SAVES POSITION
		context.lineTo(previousX + move, previousY);
		previousX = previousX + move;
		

		// DRAWS BOTTOM LEFT SIDE
		// PERFORMS BOTTOM LEFT MOVEMENT (IN THE LAST ITERATION ONLY TO THE BOTTOM)
		previousCoordinates = shapeBottomLeft(iteration - 1, context, move, previousX, previousY);
		previousX = previousCoordinates.x;
		previousY = previousCoordinates.y;
		// PERFORMS LINE TO THE RIGHT + SAVES POSITION
		context.lineTo(previousX + move, previousY);
		previousX = previousX + move;
		

		// DRAWS TOP RIGHT SIDE
		// PERFORMS TOP RIGHT MOVEMENT (IN THE LAST ITERATION ONLY TO THE BOTTOM)
		previousCoordinates = shapeUpRight(iteration - 1, context, move, previousX, previousY);
		previousX = previousCoordinates.x;
		previousY = previousCoordinates.y;
		// PERFORMS LINE TO THE RIGHT + SAVES POSITION
		context.lineTo(previousX + move, previousY);
		previousX = previousX + move;
		// PERFORMS LINE TO THE BOTTOM + SAVES POSITION
		context.lineTo(previousX, previousY + move);
		previousY = previousY + move;
		
		// DRAWS BOTTOM RIGHT SIDE
		// PERFORMS BOTTOM RIGHT MOVEMENT (IN THE LAST ITERATION ONLY TO THE RIGHT)
		return shapeBottomRight(iteration - 1, context, move, previousX, previousY);
		
	// DRAWS LINE TO RIGHT IN THE LAST ITERATION + SAVES THIS MOVEMENT
	} else {
		context.lineTo(previousX + move, previousY);
		previousX = previousX + move;
	}
	
	return new PreviousCoordinates(previousX, previousY);
}

/*
	DRAWS W-CURVE - WITH EXTENDING RECURSION
	
	@param {WCurve} wcurve - w-curve data structure
	@param {number} numberIterations - number of all iterations
	@param {Context} context - context from Canvas to perform paint operations
*/
export function drawWCurve(wcurve: Wcurve, numberIterations: number, context: CanvasRenderingContext2D): void {
	let startX: number, startY: number, previousX: number, previousY: number;
	let previousCoordinates: PreviousCoordinates;
	
	// INITIALIZE DRAWING
	context.beginPath();
	startX = startY = wcurve.center;
	context.moveTo(startX, startY);
	
	
	// DRAWS BOTTOM RIGHT SIDE
	// PERFORMS BOTTOM RIGHT MOVEMENT (IN THE LAST ITERATION ONLY TO THE RIGHT)
	previousCoordinates = shapeBottomRight(numberIterations - 1, context, wcurve.lineLength, startX, startY);
	previousX = previousCoordinates.x;
	previousY = previousCoordinates.y;
	
	// PERFORMS LINE TO TOP + SAVES POSITION
	context.lineTo(previousX, previousY - wcurve.lineLength);
	previousY = previousY - wcurve.lineLength;
	// PERFORMS LINE TO RIGHT + SAVES POSITION
	context.lineTo(previousX + wcurve.lineLength, previousY);
	previousX = previousX + wcurve.lineLength;
	
	wcurve.lineLength = wcurve.lineLength / 2;
	
	// DRAWS BOTTOM LEFT SIDE
	// PERFORMS BOTTOM LEFT MOVEMENT (IN THE LAST ITERATION ONLY TO THE BOTTOM)
	previousCoordinates = shapeBottomLeft(numberIterations - 1, context, wcurve.lineLength, previousX, previousY);
	previousX = previousCoordinates.x;
	previousY = previousCoordinates.y;

	// PERFORMS LINE TO THE LEFT + SAVES POSITION
	context.lineTo(previousX - wcurve.lineLength, previousY);
	previousX = previousX - wcurve.lineLength;
	// PERFORMS LINE TO THE TOP + SAVES POSITION
	context.lineTo(previousX, previousY - wcurve.lineLength);
	previousY = previousY - wcurve.lineLength;
	
	
	wcurve.lineLength = wcurve.lineLength / 2;
	
	
	// DRAWS TOP LEFT SIDE
	// PERFORMS TOP LEFT MOVEMENT (IN THE LAST ITERATION ONLY TO THE LEFT)
	previousCoordinates = shapeUpLeft(numberIterations - 1, context, wcurve.lineLength, previousX, previousY);
	previousX = previousCoordinates.x;
	previousY = previousCoordinates.y;
	
	// PERFORMS LINE TO THE BOTTOM + SAVES POSITION
	context.lineTo(previousX, previousY + wcurve.lineLength);
	previousY = previousY + wcurve.lineLength;
	// PERFORMS LINE TO THE LEFT + SAVES POSITION
	context.lineTo(previousX - wcurve.lineLength, previousY);
	previousX = previousX - wcurve.lineLength;
	
	wcurve.lineLength = wcurve.lineLength / 2;
	
	
	// DRAWS TOP RIGHT SIDE
	// PERFORMS TOP RIGHT MOVEMENT (IN THE LAST ITERATION ONLY TO THE BOTTOM)
	previousCoordinates = shapeUpRight(numberIterations - 1, context, wcurve.lineLength, previousX, previousY);
	previousX = previousCoordinates.x;
	previousY = previousCoordinates.y;
	
	// PERFORMS LINE TO THE RIGHT + SAVES POSITION
	context.lineTo(previousX + wcurve.lineLength, previousY);
	previousX = previousX + wcurve.lineLength;
	// PERFORMS LINE TO THE TOP + SAVES POSITION
	context.lineTo(previousX, previousY + wcurve.lineLength);
	previousY = previousY + wcurve.lineLength;
	

	// FINALIZES DRAWING - APPLY DRAWING SETTINGS
	context.lineWidth = wcurve.thickness;
	context.strokeStyle = "#cfc";
	context.stroke();
}


/*
	W-CURVE FRACTAL REPRESENTATION
	
	@param {number} size - size of whole shape (size of canvas in many cases)
	@param {number} lineLength - length of used lines
	@param {number} thickness - line thickness/width
*/
export class Wcurve {
  size: number;
  center: number;
  block: number;
  lineLength: number;
  thickness: number;

  constructor(size: number, lineLength: number, thickness: number) {
    this.thickness = thickness;
    this.size = size;
    this.center = size / 2;
    this.block = size / 2;
    this.lineLength = lineLength;
  }
}

	
export function drawWCurveFrom4Rec(context: CanvasRenderingContext2D,  initSize: number, 
      lineLength: number, numberIterations: number, thickness: number): void {
  const wcurve = new Wcurve(initSize, lineLength, thickness);
  
  drawWCurve(wcurve, numberIterations, context);
}
`