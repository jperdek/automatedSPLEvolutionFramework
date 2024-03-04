
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
function drawLine(context: CanvasRenderingContext2D, x1: number, y1: number, x2: number, y2: number, thickness: number) {
	context.beginPath();
	context.moveTo(x1, y1);
	context.lineTo(x2, y2);
	context.lineWidth = thickness;
	context.strokeStyle = "#cfc";
	context.stroke();
}


/*
	DRAWS CIRCLE ON CANVAS
	
	@param {Context} context - context from Canvas to perform paint operations
	@param {number} x - center x coordinate of circle
	@param {number} y - center y coordinate of circle
	@param {number} radius - radius of circle
*/
function drawCircle(context: CanvasRenderingContext2D, cordX: number, cordY: number, radius: number): void {
	context.fillStyle = "rgba(200, 200, 100, .9)";
	context.beginPath();
	context.arc(cordX, cordY, radius, 0, Math.PI*2, true);
	context.closePath();
	context.fill();
}


/*
	DRAWS CIRCUMCIRCLE ON CANVAS
	
	@param {number} x - center x coordinate of circle
	@param {number} y - center y coordinate of circle
	@param {AnkletMod} ankletMod - anklet data structure (class)
	@param {Context} context - context from Canvas to perform paint operations
*/
function createCircle(x: number, y: number, ankletMod: AnkletMod, context: CanvasRenderingContext2D) {
	//context.fillStyle = "rgba(200, 200, 100, .9)";
	context.beginPath();
	context.arc(x, y, ankletMod.circleRadius, 0, Math.PI*2, true);
	context.closePath();
	context.strokeStyle = "#cfc";
	context.stroke();
}

/*
		ORIENTATION_ENUM
		
		-conatins given extension options from Krishna Anklet
	*/
export enum Orientation {
	TOP = 0,
	LEFT = 1,
	RIGHT = 2,
	BOTTOM = 3,
	ALL = 4,
}
	
export class AnkletMod {
	
  lineLength: number;
  thickness: number;
  moveToBase: number;
  circleRadius: number;
  squareSideLength: number;
  hypotenuseSquare: number;
  hypotenuseSquareHalf: number;
  lineLengthFrac: any = [];
	/*
		KRISHNA ANKLET FRACTAL REPRESENTATION
		
		@param {number} lineLength - length of used lines
		@param {number} circleRadius - radius of Krishna anklet circles
		@param {number} thickness - line thickness/width
		@param {number} squareSideLength - side length of square in Krishna anklet fractal
	*/
	constructor(lineLength: number, circleRadius: number, thickness: number, squareSideLength: number) {
		this.lineLength = Math.round(lineLength);
		this.thickness = thickness;
		//this.lineLengthFrac;
		this.circleRadius = circleRadius;
		this.squareSideLength = Math.round(squareSideLength);
		this.hypotenuseSquare = Math.round(Math.sqrt(2*Math.pow(this.squareSideLength, 2)));
		this.hypotenuseSquareHalf = Math.round(this.hypotenuseSquare / 2.0);
		this.moveToBase = this.lineLength + this.hypotenuseSquare;
	}
	
	
	/*
		EVALUATES LINE LENGTH FROM GIVEN NUMBER OF ITERATIONS
		
		@param {number} numberIterations - actual processed iteration
	*/
	countLineLengthFrac(numberIterations: number): void {
		let arrayToHelp = [];
		arrayToHelp.push(Math.round(this.lineLength / 2.0));
		for(let i=1; i<=numberIterations; i++){
			arrayToHelp.push(Math.round(arrayToHelp[i-1] / 2.0));
		}
		
		this.lineLengthFrac = [];
		for(let i=numberIterations; i>=0; i--){
			this.lineLengthFrac.push(arrayToHelp[i]);
		}
	}
	
	

	/*
		CREATES ANKLET BASE - ROTATED SQUARE
		
		1. evaluates side points of rotated square
		2. connects these side points to anklet base square
		
		@param {number} centerX - center x coordinate (in the middle of given rotated base square)
		@param {number} centerY - center y coordinate (in the middle of given rotated base square)
		@param {AnkletMod} ankletMod - anklet data structure (class)
		@param {Context} context - context from Canvas to perform paint operations
	*/
	createBase(centerX: number, centerY: number, ankletMod: AnkletMod, context: CanvasRenderingContext2D): void {	
		const centerXMinusHypo: number = centerX - ankletMod.hypotenuseSquareHalf;
		const centerYMinusHypo: number = centerY - ankletMod.hypotenuseSquareHalf;
		const centerXPlusHypo: number = centerX + ankletMod.hypotenuseSquareHalf;
		const centerYPlusHypo: number = centerY + ankletMod.hypotenuseSquareHalf;

		drawLine(context, centerX, centerYMinusHypo, centerXPlusHypo, centerY, ankletMod.thickness);
		drawLine(context, centerXPlusHypo, centerY, centerX, centerYPlusHypo, ankletMod.thickness);
		drawLine(context, centerX, centerYPlusHypo, centerXMinusHypo, centerY, ankletMod.thickness);
		drawLine(context, centerXMinusHypo, centerY, centerX, centerYMinusHypo, ankletMod.thickness);
	}


	/*
		PROCESSES CREATION OF ANKLET CENTRAL BASE - ROTATED CENTRAL SQUARE
		
		@param {number} centerX - center x coordinate (in the middle of given rotated base square)
		@param {number} centerY - center y coordinate (in the middle of given rotated base square)
		@param {AnkletMod} ankletMod - anklet data structure (class)
		@param {Context} context - context from Canvas to perform paint operations
	*/
	createCentralBase(centerX: number, centerY: number, ankletMod: AnkletMod, context: CanvasRenderingContext2D): void {
		this.createBase(centerX, centerY, ankletMod, context);
	}


	/*
		CREATE CIRCLES ON THE ANKLET
		
		TOP POINT 		[centerX, centerYMinusHypoHalf]
		LEFT POINT 		[centerXMinusHypoHalf, centerY]
		RIGHT POINT 	[centerXPlusHypoHalf,  centerY]
		BOTTOM POINT 	[centerX,  centerYPlusHypoHalf]
		
		@param {number} iteration - actual processed iteration
		@param {number} centerX - center x coordinate (in the middle of rotated base square)
		@param {number} centerY - center y coordinate (in the middle of rotated base square)
		@param {number} centerXMinusHypoHalf - coordinate x for left point of base rotated square 
		@param {number} centerYMinusHypoHalf - coordinate y for top point of base rotated square 
		@param {number} centerXPlusHypoHalf - coordinate x for right point of base rotated square 
		@param {number} centerYPlusHypoHalf - coordinate y for bottom point of base rotated square 
		@param {AnkletMod} ankletMod - anklet data structure (class)
		@param {Context} context - context from Canvas to perform paint operations
		@param {ORIENTATION_ENUM} orientation - orientation to identify on which side is fractal drawn
		@param {ORIENTATION_ENUM} inheritedOrientationLeft - inherited left orientation indication from previous iterations to identify on which side is fractal drawn
		@param {ORIENTATION_ENUM} inheritedOrientationRight - inherited right orientation indication from previous iterations to identify on which side is fractal drawn
		@param {ORIENTATION_ENUM} inheritedOrientationTop - inherited top orientation indication from previous iterations to identify on which side is fractal drawn
		@param {ORIENTATION_ENUM} inheritedOrientationBottom - inherited bottom orientation indication from previous iterations to identify on which side is fractal drawn
	*/
	createCircles(centerX: number, centerY: number, centerXMinusHypoHalf: number, centerYMinusHypoHalf: number, centerXPlusHypoHalf: number, centerYPlusHypoHalf: number, 
			ankletMod: AnkletMod, context: CanvasRenderingContext2D, orientation: Orientation, inheritedOrientationLeft: boolean, 
			inheritedOrientationRight: boolean, inheritedOrientationTop: boolean, inheritedOrientationBottom: boolean) {
		// NOT EXTENDS SHAPE TO BOTTOM IF COMES FROM THE BOTTOM
		if(orientation != Orientation.BOTTOM) {
			// IF COMES FROM LEFT OR RIGHT THEN CAN EXTEND SHAPE TO THE TOP
			if(orientation == Orientation.LEFT ||  orientation == Orientation.RIGHT) {
				createCircle(centerX, centerYMinusHypoHalf - ankletMod.circleRadius, ankletMod, context);
				
			// OHTERWISE IF PREVIOUSLY DOES NOT COME FROM THE TOP THEN EXTENDS TO THE TOP
			} else if(inheritedOrientationTop == false){
				createCircle(centerX, centerYMinusHypoHalf - ankletMod.circleRadius, ankletMod, context);
			}
		}
		
		// NOT EXTENDS SHAPE TO RIGHT IF COMES FROM THE RIGHT
		if(orientation != Orientation.RIGHT) {
			// IF COMES FROM TOP OR BOTTOM THEN CAN EXTEND SHAPE TO THE LEFT
			if(orientation == Orientation.TOP ||  orientation == Orientation.BOTTOM) {
				createCircle(centerXMinusHypoHalf - ankletMod.circleRadius, centerY, ankletMod, context);
				
			// OHTERWISE IF PREVIOUSLY DOES NOT COME FROM THE LEFT THEN EXTENDS TO THE LEFT
			} else if(inheritedOrientationLeft == false){
				createCircle(centerXMinusHypoHalf - ankletMod.circleRadius, centerY, ankletMod, context);
			}
		}
		
		// NOT EXTENDS SHAPE TO LEFT IF COMES FROM THE LEFT
		if(orientation != Orientation.LEFT) {	
			// IF COMES FROM TOP OR BOTTOM THEN CAN EXTEND SHAPE TO THE RIGTH
			if(orientation == Orientation.TOP ||  orientation == Orientation.BOTTOM) {
				createCircle(centerXPlusHypoHalf + ankletMod.circleRadius, centerY, ankletMod, context);
				
			// OHTERWISE IF PREVIOUSLY DOES NOT COME FROM THE RIGTH THEN EXTENDS TO THE RIGHT
			} else if(inheritedOrientationRight == false){
				createCircle(centerXPlusHypoHalf + ankletMod.circleRadius, centerY, ankletMod, context);
			}
		}
		
		// NOT EXTENDS SHAPE TO TOP IF COMES FROM THE TOP
		if(orientation != Orientation.TOP) {
			// IF COMES FROM RIGHT OR LEFT THEN CAN EXTEND SHAPE TO THE BOTTOM
			if(orientation == Orientation.LEFT ||  orientation == Orientation.RIGHT) {
				createCircle(centerX, centerYPlusHypoHalf + ankletMod.circleRadius, ankletMod, context);
		
			// OHTERWISE IF PREVIOUSLY DOES NOT COME FROM THE BOTTOM THEN EXTENDS TO THE BOTTOM 
			} else if(inheritedOrientationBottom == false){
				createCircle(centerX, centerYPlusHypoHalf + ankletMod.circleRadius, ankletMod, context);
			}
		}
	}


	/*
		DRAWS ANKLET - WITH EXTENDING RECURSION
		
		@param {number} iteration - actual processed iteration
		@param {number} centerX - center x coordinate (in the middle of rotated base square)
		@param {number} centerY - center y coordinate (in the middle of rotated base square)
		@param {AnkletMod} ankletMod - anklet data structure (class)
		@param {Context} context - context from Canvas to perform paint operations
		@param {ORIENTATION_ENUM} orientation - orientation to identify on which side is fractal drawn
		@param {ORIENTATION_ENUM} inheritedOrientation - inherited orientation from previous iterations to identify on which side is fractal drawn
	*/
	drawAnklet(iteration: number, centerX: number, centerY: number, ankletMod: AnkletMod, context: CanvasRenderingContext2D, 
			orientation: Orientation, inheritedOrientation: boolean){
		let sideWidthHalf: number;
		let centerXMinusHypoHalf: number, centerXPlusHypoHalf: number, centerYMinusHypoHalf: number, centerYPlusHypoHalf: number;
		let newBaseX: number, newBaseY: number;
		let conditionTop: boolean, conditionBottom: boolean, conditionLeft: boolean, conditionRight: boolean;
		
		if(iteration > 0){
			// EVALUATES SIDE COORDINATES OF THE BASE ROTATED SQUARE
			centerXMinusHypoHalf = centerX - ankletMod.hypotenuseSquareHalf;
			centerXPlusHypoHalf = centerX + ankletMod.hypotenuseSquareHalf;
			centerYMinusHypoHalf =  centerY - ankletMod.hypotenuseSquareHalf;
			centerYPlusHypoHalf = centerY + ankletMod.hypotenuseSquareHalf;
			
			// CREATES CENTRAL ROTATED SQUARE FROM PREVIOUSLY EVALUATED POINTS
			this.createCentralBase(centerX , centerY, ankletMod, context);
			sideWidthHalf = ankletMod.lineLengthFrac[iteration];
			
			// LEFT CONDITION
				// IF CREATION OF THE SHAPE EXTENDS FROM THE LEFT
			if(orientation == Orientation.LEFT) {
				conditionRight = true;
				
				// IF CREATION OF THE SHAPE DOES NOT EXTEND FROM THE RIGHT
			} else if(orientation != Orientation.RIGHT){
				conditionRight = false;
				
				// OTHERWISE INHERITS FROM PREVIOUS STEPS
			} else {
				conditionRight = inheritedOrientation;
			}
			
			// RIGHT CONDITION
				// IF CREATION OF THE SHAPE EXTENDS FROM THE RIGHT
			if(orientation == Orientation.RIGHT) {
				conditionLeft = true;
				
				// IF CREATION OF THE SHAPE DOES NOT EXTEND FROM THE LEFT
			} else if(orientation != Orientation.LEFT){
				conditionLeft = false;
				
				// OTHERWISE INHERITS FROM PREVIOUS STEPS
			} else {
				conditionLeft = inheritedOrientation;
			}
			
			// TOP CONDITION
				// IF CREATION OF THE SHAPE EXTENDS FROM THE TOP
			if(orientation == Orientation.TOP) {
				conditionBottom = true;
				
				// IF CREATION OF THE SHAPE DOES NOT EXTEND FROM THE BOTTOM
			} else if(orientation != Orientation.BOTTOM){
				conditionBottom = false;
				
				// OTHERWISE INHERITS FROM PREVIOUS STEPS
			} else {
				conditionBottom = inheritedOrientation;
			}
			
			// BOTTOM CONDITION
				// IF CREATION OF THE SHAPE EXTENDS FROM THE BOTTOM
			if(orientation == Orientation.BOTTOM) {
				conditionTop = true;
				
				// IF CREATION OF THE SHAPE DOES NOT EXTEND FROM THE TOP
			} else if(orientation != Orientation.TOP){
				conditionTop = false;
				
				// OTHERWISE INHERITS FROM PREVIOUS STEPS
			} else {
				conditionTop = inheritedOrientation;
			}
			
			// DRAWS CIRCLES IN THE LAST ITERATION
			if(iteration == 1){
				this.createCircles(centerX , centerY, centerXMinusHypoHalf, centerYMinusHypoHalf, centerXPlusHypoHalf, centerYPlusHypoHalf, 
						ankletMod, context, orientation, conditionLeft, conditionRight, conditionTop, conditionBottom);
			
			// OTHERWISE DRAWS CONNECTIONS TO FOLLOWING PARTS
			} else if (iteration > 1){
				drawLine(context, centerX, centerYMinusHypoHalf, centerX, centerYMinusHypoHalf - ankletMod.lineLengthFrac[0], ankletMod.thickness);
				drawLine(context, centerX, centerYPlusHypoHalf, centerX, centerYPlusHypoHalf + ankletMod.lineLengthFrac[0], ankletMod.thickness);
				drawLine(context, centerXMinusHypoHalf, centerY, centerXMinusHypoHalf - ankletMod.lineLengthFrac[0], centerY, ankletMod.thickness);
				drawLine(context, centerXPlusHypoHalf, centerY, centerXPlusHypoHalf + ankletMod.lineLengthFrac[0], centerY, ankletMod.thickness);
			}
			
			
			// ENTENDS RECURSION TO EACH SIDE
				// EXTENDING TO THE TOP
			this.drawAnklet(iteration - 1, centerX, centerY - sideWidthHalf, ankletMod, context, Orientation.TOP, conditionTop);
				// EXTENDING TO THE LEFT
			this.drawAnklet(iteration - 1, centerX - sideWidthHalf, centerY, ankletMod, context, Orientation.LEFT, conditionLeft);
				// EXTENDING TO THE RIGHT
			this.drawAnklet(iteration - 1, centerX + sideWidthHalf, centerY, ankletMod, context, Orientation.RIGHT, conditionRight);
				// EXTENDING TO THE BOTTOM
			this.drawAnklet(iteration - 1, centerX, centerY + sideWidthHalf, ankletMod, context, Orientation.BOTTOM, conditionBottom);
		} 
	}
}	


function drawAnkletModMain(canvas: HTMLCanvasElement, context: CanvasRenderingContext2D, circleRadius: number, 
			numberIterations: number, lineLength: number, thickness: number, squareSideLength: number, ankletMod: AnkletMod): void {
        ankletMod.drawAnklet(numberIterations, Math.round(canvas.width/2.0), Math.round(canvas.height / 2.0),  ankletMod, context, Orientation.ALL, false);
}
`;