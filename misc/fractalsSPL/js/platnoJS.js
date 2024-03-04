

/*
	CIRCLE OBJECT - definition
	
	@param {number} x - center x coordinate of circle
	@param {number} y - center y coordinate of circle
	@param {number} radius - radius of circle
*/
function Circle(x, y, radius) {
	this.x = x;
	this.y = y;
	this.radius = radius;
}


/*
	LINE OBJECT - definition
	
	@param {Circle} startPoint - start point of line
	@param {Circle} endPoint - end point of line
	@param {number} thickness - thickness/width of line
*/
function Line(startPoint, endPoint, thickness) {
	this.startPoint = startPoint;
	this.endPoint = endPoint;
	this.thickness = thickness;
}


/*
	GLOBAL OBJECT - data can be stored there - but its not recommend to use it in this form
*/
var gameInfo = {
	circles: [],
	thinLineThickness: 1,
	lines: []
};


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
function drawLine(context, x1, y1, x2, y2, thickness) {
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
function drawCircle(context, x, y, radius) {
	context.fillStyle = "rgba(200, 200, 100, .9)";
	context.beginPath();
	context.arc(x, y, radius, 0, Math.PI*2, true);
	context.closePath();
	context.fill();
}


/*
	PREVIOUS COORDINATES REPRESENTATION
	
	@param {number} x - previous coordinate x
	@param {number} y - previous coordinate y
*/
function PreviousCoordinates(x, y){
	this.x = x;
	this.y = y;
}


/*
	W-CURVE FRACTAL REPRESENTATION
	
	@param {number} size - size of whole shape (size of canvas in many cases)
	@param {number} lineLength - length of used lines
	@param {number} thickness - line thickness/width
	@param {number} distanceWidthRadius - distance radius between doubled lines
*/		
function Wcurve(size, lineLength, thickness, distanceWidthRadius){
	this.thickness = thickness;
	this.size = size;
	this.lineLength = null;
	this.diagonalLength = null;
	this.moveRatio = null;
	this.distanceWidthRadius = distanceWidthRadius;
	
	/*
		DIRECTION_ENUM
		
		-conatins given extension options from Krishna Anklet
	*/
	this.direction = {
		LEFT_UP: 0,
		LEFT_DOWN: 1,
		RIGHT_UP: 2,
		RIGHT_DOWN: 3,
		ALL: 4
	};
	
	/*
		EVALUATES LINE LENGTH FROM GIVEN NUMBER OF ITERATIONS AND INITIALIZES OTHER PARAMETERS
		
		@param {number} iterations - number of iterations to draw fractal
	*/
	this.initialize = function(iterations) {
		this.lineLength = this.size;
		for(let i=0; i<iterations; i++){
			this.lineLength = Math.round(this.lineLength / 2.0);
		}
		//this.lineLength = this.size >> iterations;
		this.diagonalLength = this.lineLength;//Math.round(this.lineLength * Math.sqrt(2.0));
		this.lineLengthHalf = this.lineLength / 2;//Math.round(this.diagonalLength / 2.0);
		this.moveRatio = 1 << iterations;
	}
}

//~{}
	if(true) {
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
	function drawWCurve(iteration, moveRatioIteration, centerX, centerY, wcurve, context, direction, inheritedOperation){
		var square1x, square1y, square2x, square2y, square3x, square3y, square4x, square4y;
		var verticalToSquare1y, horizontalToSquare1x;
		var verticalToSquare2y, horizontalToSquare2x;
		var verticalToSquare3y, horizontalToSquare3x;
		var verticalToSquare4y, horizontalToSquare4x;
		
		var newLeftUpX, newLeftUpY, newRightUpX, newRightUpY;
		var newLeftBottomX, newLeftBottomY, newRightBottomX, newRightBottomY;
		var distanceOfNewOnes = moveRatioIteration * wcurve.lineLength;
		
		var condOfDirRightUp, condOfDirRightDown, condOfDirLeftDown, condOfDirLeftUp;
		
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
			
			
			if(direction == wcurve.direction.LEFT_DOWN){
				condOfDirRightUp = true;
			} else if(direction != wcurve.direction.RIGHT_UP){
				condOfDirRightUp = false;
			} else{
				condOfDirRightUp = inheritedOperation;
			}
			
			if(direction == wcurve.direction.LEFT_UP){
				condOfDirRightDown = true;
			} else if(direction != wcurve.direction.RIGHT_DOWN){
				condOfDirRightDown = false;
			} else{
				condOfDirRightDown = inheritedOperation;
			}
			
			if(direction == wcurve.direction.RIGHT_UP){
				condOfDirLeftDown = true;
			} else if(direction != wcurve.direction.LEFT_DOWN){
				condOfDirLeftDown = false;
			} else{
				condOfDirLeftDown = inheritedOperation;
			}
			
			if(direction == wcurve.direction.RIGHT_DOWN){
				condOfDirLeftUp = true;
			} else if(direction != wcurve.direction.LEFT_UP){
				condOfDirLeftUp = false;
			} else{
				condOfDirLeftUp = inheritedOperation;
			}
			
			if(iteration == 1){
				if(direction != wcurve.direction.RIGHT_DOWN && (direction != wcurve.direction.LEFT_UP || condOfDirLeftUp == false)){
					drawLine(context, square1x - wcurve.distanceWidthRadius, square1y - wcurve.distanceWidthRadius, square1x - wcurve.distanceWidthRadius, verticalToSquare1y - wcurve.distanceWidthRadius, wcurve.thickness);
					drawLine(context, square1x + wcurve.distanceWidthRadius, square1y + wcurve.distanceWidthRadius, square1x + wcurve.distanceWidthRadius, verticalToSquare1y + wcurve.distanceWidthRadius, wcurve.thickness);
										
					drawLine(context, square1x - wcurve.distanceWidthRadius, square1y - wcurve.distanceWidthRadius, horizontalToSquare1x - wcurve.distanceWidthRadius, square1y - wcurve.distanceWidthRadius, wcurve.thickness);
					drawLine(context, square1x + wcurve.distanceWidthRadius, square1y + wcurve.distanceWidthRadius, horizontalToSquare1x + wcurve.distanceWidthRadius, square1y + wcurve.distanceWidthRadius, wcurve.thickness);			
				}
			
				
				if(direction != wcurve.direction.LEFT_DOWN && (direction != wcurve.direction.RIGHT_UP || condOfDirRightUp == false)){
					drawLine(context, square2x + wcurve.distanceWidthRadius, square2y - wcurve.distanceWidthRadius, square2x + wcurve.distanceWidthRadius, verticalToSquare2y - wcurve.distanceWidthRadius, wcurve.thickness);
					drawLine(context, square2x - wcurve.distanceWidthRadius, square2y + wcurve.distanceWidthRadius, square2x - wcurve.distanceWidthRadius, verticalToSquare2y + wcurve.distanceWidthRadius, wcurve.thickness);
					
					
					drawLine(context, square2x + wcurve.distanceWidthRadius, square2y - wcurve.distanceWidthRadius, horizontalToSquare2x + wcurve.distanceWidthRadius, square2y - wcurve.distanceWidthRadius, wcurve.thickness);
					drawLine(context, square2x - wcurve.distanceWidthRadius, square2y + wcurve.distanceWidthRadius, horizontalToSquare2x - wcurve.distanceWidthRadius, square2y + wcurve.distanceWidthRadius, wcurve.thickness);
				}
				
				
				if(direction != wcurve.direction.RIGHT_UP && (direction != wcurve.direction.LEFT_DOWN || condOfDirLeftDown == false)){
					drawLine(context, square3x - wcurve.distanceWidthRadius, square3y + wcurve.distanceWidthRadius, square3x - wcurve.distanceWidthRadius, verticalToSquare3y + wcurve.distanceWidthRadius, wcurve.thickness);
					drawLine(context, square3x + wcurve.distanceWidthRadius, square3y - wcurve.distanceWidthRadius, square3x + wcurve.distanceWidthRadius, verticalToSquare3y - wcurve.distanceWidthRadius, wcurve.thickness);
					
					
					drawLine(context, square3x + wcurve.distanceWidthRadius, square3y - wcurve.distanceWidthRadius, horizontalToSquare3x + wcurve.distanceWidthRadius, square3y - wcurve.distanceWidthRadius, wcurve.thickness);
					drawLine(context, square3x - wcurve.distanceWidthRadius, square3y + wcurve.distanceWidthRadius, horizontalToSquare3x - wcurve.distanceWidthRadius, square3y + wcurve.distanceWidthRadius, wcurve.thickness);
					
				}  
				
				//~{}
				if(direction != wcurve.direction.LEFT_UP && (direction != wcurve.direction.RIGHT_DOWN || condOfDirRightDown == false)){
					drawLine(context, square4x + wcurve.distanceWidthRadius, square4y + wcurve.distanceWidthRadius, square4x + wcurve.distanceWidthRadius, verticalToSquare4y + wcurve.distanceWidthRadius, wcurve.thickness);
					drawLine(context, square4x - wcurve.distanceWidthRadius, square4y - wcurve.distanceWidthRadius, square4x - wcurve.distanceWidthRadius, verticalToSquare4y - wcurve.distanceWidthRadius, wcurve.thickness);
					
					
					drawLine(context, square4x - wcurve.distanceWidthRadius, square4y - wcurve.distanceWidthRadius, horizontalToSquare4x - wcurve.distanceWidthRadius, square4y - wcurve.distanceWidthRadius, wcurve.thickness);
					drawLine(context, square4x + wcurve.distanceWidthRadius, square4y + wcurve.distanceWidthRadius, horizontalToSquare4x + wcurve.distanceWidthRadius, square4y + wcurve.distanceWidthRadius, wcurve.thickness);
				} 
				
				
				if(direction == wcurve.direction.RIGHT_DOWN) {
					drawLine(context, square1x - wcurve.distanceWidthRadius, verticalToSquare1y + wcurve.distanceWidthRadius, square2x - wcurve.distanceWidthRadius, verticalToSquare2y + wcurve.distanceWidthRadius, wcurve.thickness);
					drawLine(context, square1x + wcurve.distanceWidthRadius, verticalToSquare1y - wcurve.distanceWidthRadius, square2x + wcurve.distanceWidthRadius, verticalToSquare2y - wcurve.distanceWidthRadius, wcurve.thickness);
					
					drawLine(context, horizontalToSquare1x + wcurve.distanceWidthRadius, square1y - wcurve.distanceWidthRadius, horizontalToSquare3x + wcurve.distanceWidthRadius, square3y - wcurve.distanceWidthRadius, wcurve.thickness);
					drawLine(context, horizontalToSquare1x - wcurve.distanceWidthRadius, square1y + wcurve.distanceWidthRadius, horizontalToSquare3x - wcurve.distanceWidthRadius, square3y + wcurve.distanceWidthRadius, wcurve.thickness);
				
				} 
				
				
				if(direction == wcurve.direction.RIGHT_UP){
					drawLine(context, square3x + wcurve.distanceWidthRadius, verticalToSquare3y + wcurve.distanceWidthRadius, square4x + wcurve.distanceWidthRadius, verticalToSquare4y + wcurve.distanceWidthRadius, wcurve.thickness);
					drawLine(context, square3x - wcurve.distanceWidthRadius, verticalToSquare3y - wcurve.distanceWidthRadius, square4x - wcurve.distanceWidthRadius, verticalToSquare4y - wcurve.distanceWidthRadius, wcurve.thickness);
					
					drawLine(context, horizontalToSquare1x + wcurve.distanceWidthRadius, square1y + wcurve.distanceWidthRadius, horizontalToSquare3x + wcurve.distanceWidthRadius, square3y + wcurve.distanceWidthRadius, wcurve.thickness);
					drawLine(context, horizontalToSquare1x - wcurve.distanceWidthRadius, square1y - wcurve.distanceWidthRadius, horizontalToSquare3x - wcurve.distanceWidthRadius, square3y - wcurve.distanceWidthRadius, wcurve.thickness);
				}
				
				if(direction == wcurve.direction.LEFT_UP ) {		
					drawLine(context, square3x + wcurve.distanceWidthRadius, verticalToSquare3y - wcurve.distanceWidthRadius, square4x + wcurve.distanceWidthRadius, verticalToSquare4y - wcurve.distanceWidthRadius, wcurve.thickness);
					drawLine(context, square3x - wcurve.distanceWidthRadius, verticalToSquare3y + wcurve.distanceWidthRadius, square4x - wcurve.distanceWidthRadius, verticalToSquare4y + wcurve.distanceWidthRadius, wcurve.thickness);
					
					drawLine(context, horizontalToSquare2x - wcurve.distanceWidthRadius, square2y + wcurve.distanceWidthRadius, horizontalToSquare4x - wcurve.distanceWidthRadius, square4y + wcurve.distanceWidthRadius, wcurve.thickness);
					drawLine(context, horizontalToSquare2x + wcurve.distanceWidthRadius, square2y - wcurve.distanceWidthRadius, horizontalToSquare4x + wcurve.distanceWidthRadius, square4y - wcurve.distanceWidthRadius, wcurve.thickness);
				}
				
				
				if(direction == wcurve.direction.LEFT_DOWN){
					drawLine(context, square1x - wcurve.distanceWidthRadius, verticalToSquare1y - wcurve.distanceWidthRadius, square2x - wcurve.distanceWidthRadius, verticalToSquare2y - wcurve.distanceWidthRadius, wcurve.thickness);
					drawLine(context, square1x + wcurve.distanceWidthRadius, verticalToSquare1y + wcurve.distanceWidthRadius, square2x + wcurve.distanceWidthRadius, verticalToSquare2y + wcurve.distanceWidthRadius, wcurve.thickness);
					
					drawLine(context, horizontalToSquare2x - wcurve.distanceWidthRadius, square2y - wcurve.distanceWidthRadius, horizontalToSquare4x - wcurve.distanceWidthRadius, square4y - wcurve.distanceWidthRadius, wcurve.thickness);
					drawLine(context, horizontalToSquare2x + wcurve.distanceWidthRadius, square2y + wcurve.distanceWidthRadius, horizontalToSquare4x + wcurve.distanceWidthRadius, square4y + wcurve.distanceWidthRadius, wcurve.thickness);
				}
				
				
				if(direction == wcurve.direction.LEFT_UP){
					if(condOfDirLeftUp == false){
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
				
				if(direction == wcurve.direction.RIGHT_UP){
					if(condOfDirRightUp == false){
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
				
				//~{}
				if(direction == wcurve.direction.LEFT_DOWN){
					
					//~{}
					if(condOfDirLeftDown == false){
						drawLine(context, square3x - wcurve.distanceWidthRadius, verticalToSquare3y + wcurve.distanceWidthRadius, square4x + wcurve.distanceWidthRadius, verticalToSquare4y + wcurve.distanceWidthRadius, wcurve.thickness);
						drawLine(context, square3x + wcurve.distanceWidthRadius, verticalToSquare3y - wcurve.distanceWidthRadius, square4x - wcurve.distanceWidthRadius, verticalToSquare4y - wcurve.distanceWidthRadius, wcurve.thickness);
						
						drawLine(context, horizontalToSquare1x + wcurve.distanceWidthRadius, square1y + wcurve.distanceWidthRadius, horizontalToSquare3x + wcurve.distanceWidthRadius, square3y - wcurve.distanceWidthRadius, wcurve.thickness);
						drawLine(context, horizontalToSquare1x - wcurve.distanceWidthRadius, square1y - wcurve.distanceWidthRadius, horizontalToSquare3x - wcurve.distanceWidthRadius, square3y + wcurve.distanceWidthRadius, wcurve.thickness);
					} 

					if(condOfDirLeftDown != false) {
						drawLine(context, square3x + wcurve.distanceWidthRadius, verticalToSquare3y + wcurve.distanceWidthRadius, square4x + wcurve.distanceWidthRadius, verticalToSquare4y + wcurve.distanceWidthRadius, wcurve.thickness);
						drawLine(context, square3x - wcurve.distanceWidthRadius, verticalToSquare3y - wcurve.distanceWidthRadius, square4x - wcurve.distanceWidthRadius, verticalToSquare4y - wcurve.distanceWidthRadius, wcurve.thickness);
						
						drawLine(context, horizontalToSquare1x + wcurve.distanceWidthRadius, square1y + wcurve.distanceWidthRadius, horizontalToSquare3x + wcurve.distanceWidthRadius, square3y + wcurve.distanceWidthRadius, wcurve.thickness);
						drawLine(context, horizontalToSquare1x - wcurve.distanceWidthRadius, square1y - wcurve.distanceWidthRadius, horizontalToSquare3x - wcurve.distanceWidthRadius, square3y - wcurve.distanceWidthRadius, wcurve.thickness);
					}
				}

				//~{}
				if(direction == wcurve.direction.RIGHT_DOWN){
					if(condOfDirRightDown == false){
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
			
			
			// EXTENDS RECURSION
				// DRAWS W-CURVE PART ON THE TOP LEFT

			drawWCurve(iteration - 1, moveRatioIteration / 2.0, newLeftUpX, newLeftUpY, wcurve, context, wcurve.direction.LEFT_UP, condOfDirLeftUp);
				// DRAWS W-CURVE PART ON THE TOP RIGHT
			drawWCurve(iteration - 1, moveRatioIteration / 2.0, newRightUpX, newRightUpY, wcurve, context, wcurve.direction.RIGHT_UP, condOfDirRightUp);
				// DRAWS W-CURVE PART ON THE LEFT DOWN
			drawWCurve(iteration - 1, moveRatioIteration / 2.0, newLeftBottomX, newLeftBottomY, wcurve, context, wcurve.direction.LEFT_DOWN, condOfDirLeftDown);
				// DRAWS W-CURVE PART ON THE RIGHT DOWN
			drawWCurve(iteration - 1, moveRatioIteration / 2.0, newRightBottomX, newRightBottomY, wcurve, context, wcurve.direction.RIGHT_DOWN,condOfDirRightDown);	
			
			//drawWCurve(iteration - 2, moveRatioIteration / 2.0, newRightUpX, newRightBottomY, wcurve, context, wcurve.direction.RIGHT_UP, condOfDirRightUp);
		}
	}


	/*
		INITIAL FUNCTION
		
		1. Obtains context from HTML element
		2. Prepares/initializes W-curve fractal structure
		3. Draws W-curve fractal
	*/
	function init() {
		const canvas = document.getElementById("game");
		const context = canvas.getContext('2d');
		const circleRadius = 5;
		let numberIterations = 2;
		const lineLength = 10;
		const thickness = 1;
		const distanceWidthRadius = 2;
		const wcurve = new Wcurve(200, lineLength, thickness, distanceWidthRadius);
		let divisionStrength = 4;
		
		wcurve.initialize(numberIterations);
		//~{}
		if(true) {
			drawWCurve(numberIterations, wcurve.moveRatio / 2, 200, 200, wcurve, context, wcurve.direction.ALL, false, false, false, false);
		}
		//~{"__define": {"divisionValue": "wcurve.moveRatio / divisionStrength" }, "__change": {"numberIterations": "j"}, "__wrap_for": [{"variable": "k", "step": "1", "range": "2;5", "start": "for(var j=0; j<k; j++) { ", "end": "}"}], "__log": ["numberIterations", "wcurve.moveRatio"]}
		if (true) {
			drawWCurve(numberIterations, wcurve.moveRatio / divisionStrength, 200, 200, wcurve, context, wcurve.direction.ALL, false, false, false, false);
		}
		drawWCurve(numberIterations, wcurve.moveRatio / 16, 200, 200, wcurve, context, wcurve.direction.ALL, false, false, false, false);
	}

	init();
}