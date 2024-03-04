
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
	CONVERTS DEGREES TO RADIANS
	
	@param {number} angle - angle which should be converted to radians
*/
function degreeToRadians(angle: number): number {
	return Math.PI * angle / 180.0;
}



export class AnkletInfo {

	/*
		KRISHNA ANKLET FRACTAL REPRESENTATION
		
		@param {number} lineLength - length of used lines
		@param {number} circleRadius - radius of Krishna anklet circles
		@param {number} thickness - line thickness/width
		@param {number} squareSideLength - side length of square in Krishna anklet fractal
	*/
	constructor (lineLength: number, circleRadius: number, thickness: number, squareSideLength: number): void {
		this.lineLength = Math.round(lineLength);
		this.thickness = thickness;
		this.circleRadius = circleRadius;
		this.squareSideLength = Math.round(squareSideLength);
		this.hypotenuseSquare = Math.round(Math.sqrt(2*Math.pow(this.squareSideLength, 2)));
		this.hypotenuseSquareHalf = Math.round(this.hypotenuseSquare / 2.0);
		this.moveToBase = this.lineLength + this.hypotenuseSquare;
	}	
	

	/*
		CREATES ANKLET BASE - ROTATED SQUARE
		
		1. evaluates side points of rotated square
		2. connects these side points to anklet base square
		
		@param {number} centerX - center x coordinate (in the middle of given rotated base square)
		@param {number} centerY - center y coordinate (in the middle of given rotated base square)
		@param {AnkletInfo} ankletInfo - anklet data structure (class)
		@param {Context} context - context from Canvas to perform paint operations
	*/
	createBase(centerX: number, centerY: number, thickness: number, hypotenuseSquareHalf: number, context: CanvasRenderingContext2D): void {
		const centerXMinusHypo = centerX - hypotenuseSquareHalf;
		const centerYMinusHypo = centerY - hypotenuseSquareHalf;
		const centerXPlusHypo = centerX + hypotenuseSquareHalf;
		const centerYPlusHypo = centerY + hypotenuseSquareHalf;

		drawLine(context, centerX, centerYMinusHypo, centerXPlusHypo, centerY, thickness);
		drawLine(context, centerXPlusHypo, centerY, centerX, centerYPlusHypo, thickness);
		drawLine(context, centerX, centerYPlusHypo, centerXMinusHypo, centerY, thickness);
		drawLine(context, centerXMinusHypo, centerY, centerX, centerYMinusHypo, thickness);
	}


	/*
		PROCESSES CREATION OF ANKLET CENTRAL BASE - ROTATED CENTRAL SQUARE
		
		@param {number} centerX - center x coordinate (in the middle of given rotated base square)
		@param {number} centerY - center y coordinate (in the middle of given rotated base square)
		@param {AnkletInfo} ankletInfo - anklet data structure (class)
		@param {Context} context - context from Canvas to perform paint operations
	*/
	createCentralBase(centerX: number, centerY: number, ankletInfo: AnkletInfo, context: CanvasRenderingContext2D): void {
		this.createBase(centerX, centerY, ankletInfo, context);
	}


	/*
		PROCESSES CREATION OF ANKLET SIDE BASE - ROTATED SQUARE
		
		@param {number} centerX - center x coordinate (in the middle of given rotated base square)
		@param {number} centerY - center y coordinate (in the middle of given rotated base square)
		@param {AnkletInfo} ankletInfo - anklet data structure (class)
		@param {Context} context - context from Canvas to perform paint operations
	*/
	createSideBase(centerX: number, centerY: number, ankletInfo: AnkletInfo, context: CanvasRenderingContext2D): void {
		this.createBase(centerX, centerY, ankletInfo, context);
	}


	/*
		DRAWS ANKLET - WITH EXTENDING RECURSION
		
		@param {number} iteration - actual processed iteration
		@param {number} sideWidth - length of fractal side
		@param {number} centerX - center x coordinate (in the middle of rotated base square)
		@param {number} centerY - center y coordinate (in the middle of rotated base square)
		@param {AnkletInfo} ankletInfo - anklet data structure (class)
		@param {Context} context - context from Canvas to perform paint operations
	*/
	drawAnklet(iteration: number, sideWidth: number, centerX: number, centerY: number, ankletInfo: AnkletInfo, context: CanvasRenderingContext2D): void {
		let sideWidthHalf: number;
		if (iteration > 0) {
			// CREATES CENTRAL ROTATED SQUARE FROM PREVIOUSLY EVALUATED POINTS
			this.createCentralBase(centerX , centerY, ankletInfo, context);
			
			// EVALUATES HALF FROM THE SIDE
			sideWidthHalf = Math.round(sideWidth / 2.0);
			
			// ENTENDS RECURSION TO EACH SIDE
				// EXTENDING TO THE TOP
			this.drawAnklet(iteration - 1, sideWidthHalf, centerX, centerY - sideWidthHalf, ankletInfo, context);
				// EXTENDING TO THE LEFT
			this.drawAnklet(iteration - 1, sideWidthHalf, centerX - sideWidthHalf, centerY, ankletInfo, context);
				// EXTENDING TO THE RIGHT
			this.drawAnklet(iteration - 1, sideWidthHalf, centerX + sideWidthHalf, centerY, ankletInfo, context);
				// EXTENDING TO THE BOTTOM
			this.drawAnklet(iteration - 1, sideWidthHalf, centerX, centerY + sideWidthHalf, ankletInfo, context);
		}
	}
}

function drawAnkletMain(canvas: HTMLCanvasElement, context: CanvasRenderingContext2D, circleRadius: number, 
			numberIterations: number, lineLength: number, thickness: number, squareSideLength: number, ankletInfo: AnkletInfo): void {
	ankletInfo.drawAnklet(numberIterations, lineLength, Math.round(canvas.width/2.0), Math.round(canvas.height / 2.0),  ankletInfo, context);
}
`
