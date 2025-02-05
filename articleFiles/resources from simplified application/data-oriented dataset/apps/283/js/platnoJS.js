var globalResult = {};
function logData(variabilityPointIdentifier, data, name) {
if (typeof(data)==="object") { return; };
if (typeof(data)==="function") { return; };
    if (globalResult[variabilityPointIdentifier] === undefined) { globalResult[variabilityPointIdentifier] = {}; };
 	if (globalResult[variabilityPointIdentifier][name] === undefined) { globalResult[variabilityPointIdentifier][name] = []; };
	globalResult[variabilityPointIdentifier][name].push(data);
}var stackStub = [];
var initialGraphRoot = {"pointsTo": [] }
function pushData(insertedObject) {
 	if (stackStub.length === 0) {  
		if (initialGraphRoot["pointsTo"] === undefined) { initialGraphRoot["pointsTo"] = []; } 
		initialGraphRoot["pointsTo"].push(insertedObject);
 	} else  { 
		if (stackStub[stackStub.length - 1]["pointsTo"] === undefined) { stackStub[stackStub.length - 1]["pointsTo"] = []; } 
    	stackStub[stackStub.length - 1]["pointsTo"].push(insertedObject); 
	} 
	stackStub.push(insertedObject);
}
function popData() {
	if( stackStub.length > 0) { 
    	stackStub.pop(); 
	}
}/*
	CIRCLE OBJECT - definition
	
	@param {number} x - center x coordinate of circle
	@param {number} y - center y coordinate of circle
	@param {number} radius - radius of circle
*/

function Circle(x, y, radius) {
  pushData({
  "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), 
  "x": x, 
  "y": y, 
  "radius": radius});
  this.x = x;
  this.y = y;
  this.radius = radius;
  popData();
}
/*
	LINE OBJECT - definition
	
	@param {Circle} startPoint - start point of line
	@param {Circle} endPoint - end point of line
	@param {number} thickness - thickness/width of line
*/

function Line(startPoint, endPoint, thickness) {
  pushData({
  "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), 
  "startPoint": startPoint, 
  "endPoint": endPoint, 
  "thickness": thickness});
  this.startPoint = startPoint;
  this.endPoint = endPoint;
  this.thickness = thickness;
  popData();
}
/*
	GLOBAL OBJECT - data can be stored there - but its not recommend to use it in this form
*/

var gameInfo = {
  circles: [], 
  thinLineThickness: 1, 
  lines: []};
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
  pushData({
  "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), 
  "x1": x1, 
  "y1": y1, 
  "x2": x2, 
  "y2": y2, 
  "thickness": thickness});
  context.beginPath();
  context.moveTo(x1, y1);
  context.lineTo(x2, y2);
  context.lineWidth = thickness;
  context.strokeStyle = "#cfc";
  context.stroke();
  popData();
}
/*
	DRAWS CIRCLE ON CANVAS
	
	@param {Context} context - context from Canvas to perform paint operations
	@param {number} x - center x coordinate of circle
	@param {number} y - center y coordinate of circle
	@param {number} radius - radius of circle
*/

function drawCircle(context, x, y, radius) {
  pushData({
  "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), 
  "x": x, 
  "y": y, 
  "radius": radius});
  context.fillStyle = "rgba(200, 200, 100, .9)";
  context.beginPath();
  context.arc(x, y, radius, 0, Math.PI * 2, true);
  context.closePath();
  context.fill();
  popData();
}
/*
	PREVIOUS COORDINATES REPRESENTATION
	
	@param {number} x - previous coordinate x
	@param {number} y - previous coordinate y
*/

function PreviousCoordinates(x, y) {
  pushData({
  "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), 
  "x": x, 
  "y": y});
  this.x = x;
  this.y = y;
  popData();
}
/*
	W-CURVE FRACTAL REPRESENTATION
	
	@param {number} size - size of whole shape (size of canvas in many cases)
	@param {number} lineLength - length of used lines
	@param {number} thickness - line thickness/width
	@param {number} distanceWidthRadius - distance radius between doubled lines
*/

function Wcurve(size, lineLength, thickness, distanceWidthRadius) {
  pushData({
  "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), 
  "size": size, 
  "lineLength": lineLength, 
  "thickness": thickness, 
  "distanceWidthRadius": distanceWidthRadius});
  this.thickness = thickness;
  this.size = size;
  this.lineLength = null;
  this.diagonalLength = null;
  this.moveRatio = null;
  this.distanceWidthRadius = distanceWidthRadius;
  /*
		DIRECTION_ENUM
		
		-conatins given extension options from Krishna Ankvar
	*/

  this.direction = {
  LEFT_UP: 0, 
  LEFT_DOWN: 1, 
  RIGHT_UP: 2, 
  RIGHT_DOWN: 3, 
  ALL: 4};
  /*
		EVALUATES LINE LENGTH FROM GIVEN NUMBER OF ITERATIONS AND INITIALIZES OTHER PARAMETERS
		
		@param {number} iterations - number of iterations to draw fractal
	*/

  this.initialize = function(iterations) {
  this.lineLength = this.size;
  for (var i = 0; i < iterations; i++) {
    this.lineLength = Math.round(this.lineLength / 2.0);
  }
  //this.lineLength = this.size >> iterations;
  this.diagonalLength = this.lineLength;  //Math.round(this.lineLength * Math.sqrt(2.0));
  this.lineLengthHalf = this.lineLength / 2;  //Math.round(this.diagonalLength / 2.0);
  this.moveRatio = 1 << iterations;
};
  popData();
}
;
if (true) {
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

  function drawWCurve(iteration, moveRatioIteration, centerX, centerY, wcurve, context, direction, inheritedOperation) {
    pushData({
  "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), 
  "iteration": iteration, 
  "moveRatioIteration": moveRatioIteration, 
  "centerX": centerX, 
  "centerY": centerY, 
  "wcurve": wcurve, 
  "direction": direction, 
  "inheritedOperation": inheritedOperation});
    var square1x, square1y, square2x, square2y, square3x, square3y, square4x, square4y;
    var verticalToSquare1y, horizontalToSquare1x;
    var verticalToSquare2y, horizontalToSquare2x;
    var verticalToSquare3y, horizontalToSquare3x;
    var verticalToSquare4y, horizontalToSquare4x;
    var newLeftUpX, newLeftUpY, newRightUpX, newRightUpY;
    var newLeftBottomX, newLeftBottomY, newRightBottomX, newRightBottomY;
    var distanceOfNewOnes = moveRatioIteration * wcurve.lineLength;
    let www = 300;
for(var w=0; w<1; w++) { 
centerY = centerY + 10 * Math.sin(www*(Math.PI/180));
centerX = centerX + 10 * Math.cos(www*(Math.PI/180));
    if (true) {
    }
}
    var condOfDirRightUp, condOfDirRightDown, condOfDirLeftDown, condOfDirLeftUp;
    var square1xMinuswcurvedistanceWidthRadius, square1xPluswcurvedistanceWidthRadius;
    var square1yMinuswcurvedistanceWidthRadius, square1yPluswcurvedistanceWidthRadius;
    var square2xPluswcurvedistanceWidthRadius, square2xMinuswcurvedistanceWidthRadius;
    var square2yMinuswcurvedistanceWidthRadius, square2yPluswcurvedistanceWidthRadius;
    var verticalToSquare1yMinusWcurveDistanceWidthRadius, verticalToSquare1yPlusWcurveDistanceWidthRadius;
    var verticalToSquare2yMinusWcurveDistanceWidthRadius, verticalToSquare2yPlusWcurveDistanceWidthRadius;
    var verticalToSquare3yMinusWcurveDistanceWidthRadius, verticalToSquare3yPlusWcurveDistanceWidthRadius;
    var verticalToSquare4yMinusWcurveDistanceWidthRadius, verticalToSquare4yPlusWcurveDistanceWidthRadius;
    var horizontalToSquare1xMinusWcurveDistanceWidthRadius, varhorizontalToSquare1xPlusWcurveDistanceWidthRadius;
    if (iteration > 0) {
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
      if (direction == wcurve.direction.LEFT_DOWN) {
        condOfDirRightUp = true;
      } else if (direction != wcurve.direction.RIGHT_UP) {
        condOfDirRightUp = false;
      } else {
        condOfDirRightUp = inheritedOperation;
      }
      if (direction == wcurve.direction.LEFT_UP) {
        condOfDirRightDown = true;
      } else if (direction != wcurve.direction.RIGHT_DOWN) {
        condOfDirRightDown = false;
      } else {
        condOfDirRightDown = inheritedOperation;
      }
      if (direction == wcurve.direction.RIGHT_UP) {
        condOfDirLeftDown = true;
      } else if (direction != wcurve.direction.LEFT_DOWN) {
        condOfDirLeftDown = false;
      } else {
        condOfDirLeftDown = inheritedOperation;
      }
      if (direction == wcurve.direction.RIGHT_DOWN) {
        condOfDirLeftUp = true;
      } else if (direction != wcurve.direction.LEFT_UP) {
        condOfDirLeftUp = false;
      } else {
        condOfDirLeftUp = inheritedOperation;
      }
      square1xMinuswcurvedistanceWidthRadius = square1x - wcurve.distanceWidthRadius;
      square1xPluswcurvedistanceWidthRadius = square1x + wcurve.distanceWidthRadius;
      square1yMinuswcurvedistanceWidthRadius = square1y - wcurve.distanceWidthRadius;
      square1yPluswcurvedistanceWidthRadius = square1y + wcurve.distanceWidthRadius;
      square2xPluswcurvedistanceWidthRadius = square2x + wcurve.distanceWidthRadius;
      square2xMinuswcurvedistanceWidthRadius = square2x - wcurve.distanceWidthRadius;
      square2yMinuswcurvedistanceWidthRadius = square2y - wcurve.distanceWidthRadius;
      square2yPluswcurvedistanceWidthRadius = square2y + wcurve.distanceWidthRadius;
      square3xPluswcurvedistanceWidthRadius = square3x + wcurve.distanceWidthRadius;
      square3xMinuswcurvedistanceWidthRadius = square3x - wcurve.distanceWidthRadius;
      square3yMinuswcurvedistanceWidthRadius = square3y - wcurve.distanceWidthRadius;
      square3yPluswcurvedistanceWidthRadius = square3y + wcurve.distanceWidthRadius;
      square4xPluswcurvedistanceWidthRadius = square4x + wcurve.distanceWidthRadius;
      square4xMinuswcurvedistanceWidthRadius = square4x - wcurve.distanceWidthRadius;
      square4yMinuswcurvedistanceWidthRadius = square4y - wcurve.distanceWidthRadius;
      square4yPluswcurvedistanceWidthRadius = square4y + wcurve.distanceWidthRadius;
      verticalToSquare1yPlusWcurveDistanceWidthRadius = verticalToSquare1y + wcurve.distanceWidthRadius;
      verticalToSquare1yMinusWcurveDistanceWidthRadius = verticalToSquare1y - wcurve.distanceWidthRadius;
      horizontalToSquare1xMinusWcurveDistanceWidthRadius = horizontalToSquare1x - wcurve.distanceWidthRadius;
      horizontalToSquare1xPlusWcurveDistanceWidthRadius = horizontalToSquare1x + wcurve.distanceWidthRadius;
      verticalToSquare2yPlusWcurveDistanceWidthRadius = verticalToSquare2y + wcurve.distanceWidthRadius;
      verticalToSquare2yMinusWcurveDistanceWidthRadius = verticalToSquare2y - wcurve.distanceWidthRadius;
      horizontalToSquare2xMinusWcurveDistanceWidthRadius = horizontalToSquare2x - wcurve.distanceWidthRadius;
      horizontalToSquare2xPlusWcurveDistanceWidthRadius = horizontalToSquare2x + wcurve.distanceWidthRadius;
      verticalToSquare3yPlusWcurveDistanceWidthRadius = verticalToSquare3y + wcurve.distanceWidthRadius;
      verticalToSquare3yMinusWcurveDistanceWidthRadius = verticalToSquare3y - wcurve.distanceWidthRadius;
      horizontalToSquare3xMinusWcurveDistanceWidthRadius = horizontalToSquare3x - wcurve.distanceWidthRadius;
      horizontalToSquare3xPlusWcurveDistanceWidthRadius = horizontalToSquare3x + wcurve.distanceWidthRadius;
      verticalToSquare4yPlusWcurveDistanceWidthRadius = verticalToSquare4y + wcurve.distanceWidthRadius;
      verticalToSquare4yMinusWcurveDistanceWidthRadius = verticalToSquare4y - wcurve.distanceWidthRadius;
      horizontalToSquare4xMinuswcurvedistanceWidthRadius = horizontalToSquare4x - wcurve.distanceWidthRadius;
      horizontalToSquare4xPlusWcurveDistanceWidthRadius = horizontalToSquare4x + wcurve.distanceWidthRadius;
      //{"__define": {"rr": "" }, "__change": {"square1xMinuswcurvedistanceWidthRadius": "square1xMinuswcurvedistanceWidthRadius * Math.cos(60*(Math.PI/180))", "square1yMinuswcurvedistanceWidthRadius": "square1yMinuswcurvedistanceWidthRadius* Math.sin(60*(Math.PI/180))" }, "__wrap_for": [ {"variable": "rr", "step": "60", "range": "0;360", "start": "for(var r=0; r<1; r++) { ", "end": "}"}]}		
      if (true) {
      }
      if (true) {
      }
      if (true) {
      }
      if (true) {
      }
      if (iteration == 1) {
        if (direction != wcurve.direction.RIGHT_DOWN && (direction != wcurve.direction.LEFT_UP || condOfDirLeftUp == false)) {
          //square1xMinuswcurvedistanceWidthRadius = square1xMinuswcurvedistanceWidthRadius * Math.cos(10*(Math.PI/180));
          //square1yMinuswcurvedistanceWidthRadius = square1yMinuswcurvedistanceWidthRadius* Math.sin(10*(Math.PI/180));
          drawLine(context, square1xMinuswcurvedistanceWidthRadius, square1yMinuswcurvedistanceWidthRadius, square1xMinuswcurvedistanceWidthRadius, verticalToSquare1yMinusWcurveDistanceWidthRadius, wcurve.thickness);
          drawLine(context, square1xPluswcurvedistanceWidthRadius, square1yPluswcurvedistanceWidthRadius, square1xPluswcurvedistanceWidthRadius, verticalToSquare1yPlusWcurveDistanceWidthRadius, wcurve.thickness);
          drawLine(context, square1xMinuswcurvedistanceWidthRadius, square1yMinuswcurvedistanceWidthRadius, horizontalToSquare1xMinusWcurveDistanceWidthRadius, square1yMinuswcurvedistanceWidthRadius, wcurve.thickness);
          drawLine(context, square1xPluswcurvedistanceWidthRadius, square1yPluswcurvedistanceWidthRadius, horizontalToSquare1xPlusWcurveDistanceWidthRadius, square1yPluswcurvedistanceWidthRadius, wcurve.thickness);
        }
        if (direction != wcurve.direction.LEFT_DOWN && (direction != wcurve.direction.RIGHT_UP || condOfDirRightUp == false)) {
          drawLine(context, square2xPluswcurvedistanceWidthRadius, square2yMinuswcurvedistanceWidthRadius, square2xPluswcurvedistanceWidthRadius, verticalToSquare2yMinusWcurveDistanceWidthRadius, wcurve.thickness);
          drawLine(context, square2xMinuswcurvedistanceWidthRadius, square2yPluswcurvedistanceWidthRadius, square2xMinuswcurvedistanceWidthRadius, verticalToSquare2yPlusWcurveDistanceWidthRadius, wcurve.thickness);
          drawLine(context, square2xPluswcurvedistanceWidthRadius, square2yMinuswcurvedistanceWidthRadius, horizontalToSquare2xPlusWcurveDistanceWidthRadius, square2yMinuswcurvedistanceWidthRadius, wcurve.thickness);
          drawLine(context, square2xMinuswcurvedistanceWidthRadius, square2yPluswcurvedistanceWidthRadius, horizontalToSquare2xMinusWcurveDistanceWidthRadius, square2yPluswcurvedistanceWidthRadius, wcurve.thickness);
        }
        if (direction != wcurve.direction.RIGHT_UP && (direction != wcurve.direction.LEFT_DOWN || condOfDirLeftDown == false)) {
          drawLine(context, square3xMinuswcurvedistanceWidthRadius, square3yPluswcurvedistanceWidthRadius, square3xMinuswcurvedistanceWidthRadius, verticalToSquare3yPlusWcurveDistanceWidthRadius, wcurve.thickness);
          drawLine(context, square3xPluswcurvedistanceWidthRadius, square3yMinuswcurvedistanceWidthRadius, square3xPluswcurvedistanceWidthRadius, verticalToSquare3yMinusWcurveDistanceWidthRadius, wcurve.thickness);
          drawLine(context, square3xPluswcurvedistanceWidthRadius, square3yMinuswcurvedistanceWidthRadius, horizontalToSquare3xPlusWcurveDistanceWidthRadius, square3yMinuswcurvedistanceWidthRadius, wcurve.thickness);
          drawLine(context, square3xMinuswcurvedistanceWidthRadius, square3yPluswcurvedistanceWidthRadius, horizontalToSquare3xMinusWcurveDistanceWidthRadius, square3yPluswcurvedistanceWidthRadius, wcurve.thickness);
        }
        ;
        
        if (direction == wcurve.direction.RIGHT_DOWN) {
          drawLine(context, square1xMinuswcurvedistanceWidthRadius, verticalToSquare1yPlusWcurveDistanceWidthRadius, square2xMinuswcurvedistanceWidthRadius, verticalToSquare2yPlusWcurveDistanceWidthRadius, wcurve.thickness);
          drawLine(context, square1xPluswcurvedistanceWidthRadius, verticalToSquare1yMinusWcurveDistanceWidthRadius, square2xPluswcurvedistanceWidthRadius, verticalToSquare2yMinusWcurveDistanceWidthRadius, wcurve.thickness);
          drawLine(context, horizontalToSquare1xPlusWcurveDistanceWidthRadius, square1yMinuswcurvedistanceWidthRadius, horizontalToSquare3xPlusWcurveDistanceWidthRadius, square3yMinuswcurvedistanceWidthRadius, wcurve.thickness);
          drawLine(context, horizontalToSquare1xMinusWcurveDistanceWidthRadius, square1yPluswcurvedistanceWidthRadius, horizontalToSquare3xMinusWcurveDistanceWidthRadius, square3yPluswcurvedistanceWidthRadius, wcurve.thickness);
        }
        if (direction == wcurve.direction.RIGHT_UP) {
          drawLine(context, square3xPluswcurvedistanceWidthRadius, verticalToSquare3yPlusWcurveDistanceWidthRadius, square4xPluswcurvedistanceWidthRadius, verticalToSquare4yPlusWcurveDistanceWidthRadius, wcurve.thickness);
          drawLine(context, square3xMinuswcurvedistanceWidthRadius, verticalToSquare3yMinusWcurveDistanceWidthRadius, square4xMinuswcurvedistanceWidthRadius, verticalToSquare4yMinusWcurveDistanceWidthRadius, wcurve.thickness);
          drawLine(context, horizontalToSquare1xPlusWcurveDistanceWidthRadius, square1yPluswcurvedistanceWidthRadius, horizontalToSquare3xPlusWcurveDistanceWidthRadius, square3yPluswcurvedistanceWidthRadius, wcurve.thickness);
          drawLine(context, horizontalToSquare1xMinusWcurveDistanceWidthRadius, square1yMinuswcurvedistanceWidthRadius, horizontalToSquare3xMinusWcurveDistanceWidthRadius, square3yMinuswcurvedistanceWidthRadius, wcurve.thickness);
        }
        if (direction == wcurve.direction.LEFT_UP) {
          drawLine(context, square3xPluswcurvedistanceWidthRadius, verticalToSquare3yMinusWcurveDistanceWidthRadius, square4xPluswcurvedistanceWidthRadius, verticalToSquare4yMinusWcurveDistanceWidthRadius, wcurve.thickness);
          drawLine(context, square3xMinuswcurvedistanceWidthRadius, verticalToSquare3yPlusWcurveDistanceWidthRadius, square4xMinuswcurvedistanceWidthRadius, verticalToSquare4yPlusWcurveDistanceWidthRadius, wcurve.thickness);
          drawLine(context, horizontalToSquare2xMinusWcurveDistanceWidthRadius, square2yPluswcurvedistanceWidthRadius, horizontalToSquare4xMinuswcurvedistanceWidthRadius, square4yPluswcurvedistanceWidthRadius, wcurve.thickness);
          drawLine(context, horizontalToSquare2xPlusWcurveDistanceWidthRadius, square2yMinuswcurvedistanceWidthRadius, horizontalToSquare4xPlusWcurveDistanceWidthRadius, square4yMinuswcurvedistanceWidthRadius, wcurve.thickness);
        }
        if (direction == wcurve.direction.LEFT_DOWN) {
          drawLine(context, square1xMinuswcurvedistanceWidthRadius, verticalToSquare1yMinusWcurveDistanceWidthRadius, square2xMinuswcurvedistanceWidthRadius, verticalToSquare2yMinusWcurveDistanceWidthRadius, wcurve.thickness);
          drawLine(context, square1xPluswcurvedistanceWidthRadius, verticalToSquare1yPlusWcurveDistanceWidthRadius, square2xPluswcurvedistanceWidthRadius, verticalToSquare2yPlusWcurveDistanceWidthRadius, wcurve.thickness);
          drawLine(context, horizontalToSquare2xMinusWcurveDistanceWidthRadius, square2yMinuswcurvedistanceWidthRadius, horizontalToSquare4xMinuswcurvedistanceWidthRadius, square4yMinuswcurvedistanceWidthRadius, wcurve.thickness);
          drawLine(context, horizontalToSquare2xPlusWcurveDistanceWidthRadius, square2yPluswcurvedistanceWidthRadius, horizontalToSquare4xPlusWcurveDistanceWidthRadius, square4yPluswcurvedistanceWidthRadius, wcurve.thickness);
        }
        if (direction == wcurve.direction.LEFT_UP) {
          if (condOfDirLeftUp == false) {
            drawLine(context, square1xMinuswcurvedistanceWidthRadius, verticalToSquare1yMinusWcurveDistanceWidthRadius, square2xPluswcurvedistanceWidthRadius, verticalToSquare2yMinusWcurveDistanceWidthRadius, wcurve.thickness);
            drawLine(context, square1xPluswcurvedistanceWidthRadius, verticalToSquare1yPlusWcurveDistanceWidthRadius, square2xMinuswcurvedistanceWidthRadius, verticalToSquare2yPlusWcurveDistanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare1xPlusWcurveDistanceWidthRadius, square1yPluswcurvedistanceWidthRadius, horizontalToSquare3xPlusWcurveDistanceWidthRadius, square3yMinuswcurvedistanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare1xMinusWcurveDistanceWidthRadius, square1yMinuswcurvedistanceWidthRadius, horizontalToSquare3xMinusWcurveDistanceWidthRadius, square3yPluswcurvedistanceWidthRadius, wcurve.thickness);
          } else {
            drawLine(context, square1xMinuswcurvedistanceWidthRadius, verticalToSquare1yPlusWcurveDistanceWidthRadius, square2xMinuswcurvedistanceWidthRadius, verticalToSquare2yPlusWcurveDistanceWidthRadius, wcurve.thickness);
            drawLine(context, square1xPluswcurvedistanceWidthRadius, verticalToSquare1yMinusWcurveDistanceWidthRadius, square2xPluswcurvedistanceWidthRadius, verticalToSquare2yMinusWcurveDistanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare1xMinusWcurveDistanceWidthRadius, square1yPluswcurvedistanceWidthRadius, horizontalToSquare3xMinusWcurveDistanceWidthRadius, square3yPluswcurvedistanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare1xPlusWcurveDistanceWidthRadius, square1yMinuswcurvedistanceWidthRadius, horizontalToSquare3xPlusWcurveDistanceWidthRadius, square3yMinuswcurvedistanceWidthRadius, wcurve.thickness);
          }
        }
        if (direction == wcurve.direction.RIGHT_UP) {
          if (condOfDirRightUp == false) {
            drawLine(context, square1xMinuswcurvedistanceWidthRadius, verticalToSquare1yMinusWcurveDistanceWidthRadius, square2xPluswcurvedistanceWidthRadius, verticalToSquare2yMinusWcurveDistanceWidthRadius, wcurve.thickness);
            drawLine(context, square1xPluswcurvedistanceWidthRadius, verticalToSquare1yPlusWcurveDistanceWidthRadius, square2xMinuswcurvedistanceWidthRadius, verticalToSquare2yPlusWcurveDistanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare2xMinusWcurveDistanceWidthRadius, square2yPluswcurvedistanceWidthRadius, horizontalToSquare4xMinuswcurvedistanceWidthRadius, square4yMinuswcurvedistanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare2xPlusWcurveDistanceWidthRadius, square2yMinuswcurvedistanceWidthRadius, horizontalToSquare4xPlusWcurveDistanceWidthRadius, square4yPluswcurvedistanceWidthRadius, wcurve.thickness);
          } else {
            drawLine(context, square1xMinuswcurvedistanceWidthRadius, verticalToSquare1yMinusWcurveDistanceWidthRadius, square2xMinuswcurvedistanceWidthRadius, verticalToSquare2yMinusWcurveDistanceWidthRadius, wcurve.thickness);
            drawLine(context, square1xPluswcurvedistanceWidthRadius, verticalToSquare1yPlusWcurveDistanceWidthRadius, square2xPluswcurvedistanceWidthRadius, verticalToSquare2yPlusWcurveDistanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare2xPlusWcurveDistanceWidthRadius, square2yPluswcurvedistanceWidthRadius, horizontalToSquare4xPlusWcurveDistanceWidthRadius, square4yPluswcurvedistanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare2xMinusWcurveDistanceWidthRadius, square2yMinuswcurvedistanceWidthRadius, horizontalToSquare4xMinuswcurvedistanceWidthRadius, square4yMinuswcurvedistanceWidthRadius, wcurve.thickness);
          }
        }
        ;
                if (direction == wcurve.direction.LEFT_DOWN) {
          ;
                    if (condOfDirLeftDown == false) {
            drawLine(context, square3xMinuswcurvedistanceWidthRadius, verticalToSquare3yPlusWcurveDistanceWidthRadius, square4xPluswcurvedistanceWidthRadius, verticalToSquare4yPlusWcurveDistanceWidthRadius, wcurve.thickness);
            drawLine(context, square3xPluswcurvedistanceWidthRadius, verticalToSquare3yMinusWcurveDistanceWidthRadius, square4xMinuswcurvedistanceWidthRadius, verticalToSquare4yMinusWcurveDistanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare1xPlusWcurveDistanceWidthRadius, square1yPluswcurvedistanceWidthRadius, horizontalToSquare3xPlusWcurveDistanceWidthRadius, square3yMinuswcurvedistanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare1xMinusWcurveDistanceWidthRadius, square1yMinuswcurvedistanceWidthRadius, horizontalToSquare3xMinusWcurveDistanceWidthRadius, square3yPluswcurvedistanceWidthRadius, wcurve.thickness);
          }
          if (condOfDirLeftDown != false) {
            drawLine(context, square3xPluswcurvedistanceWidthRadius, verticalToSquare3yPlusWcurveDistanceWidthRadius, square4xPluswcurvedistanceWidthRadius, verticalToSquare4yPlusWcurveDistanceWidthRadius, wcurve.thickness);
            drawLine(context, square3xMinuswcurvedistanceWidthRadius, verticalToSquare3yMinusWcurveDistanceWidthRadius, square4xMinuswcurvedistanceWidthRadius, verticalToSquare4yMinusWcurveDistanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare1xPlusWcurveDistanceWidthRadius, square1yPluswcurvedistanceWidthRadius, horizontalToSquare3xPlusWcurveDistanceWidthRadius, square3yPluswcurvedistanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare1xMinusWcurveDistanceWidthRadius, square1yMinuswcurvedistanceWidthRadius, horizontalToSquare3xMinusWcurveDistanceWidthRadius, square3yMinuswcurvedistanceWidthRadius, wcurve.thickness);
          }
        }
        ;
        logData("4-1-2", square3x - wcurve.distanceWidthRadius, "__iteration_" + iteration.toString() + "");
logData("4-1-2", direction, "direction");
        if (direction == wcurve.direction.RIGHT_DOWN) {
          if (condOfDirRightDown == false) {
            drawLine(context, square3xMinuswcurvedistanceWidthRadius, verticalToSquare3yPlusWcurveDistanceWidthRadius, square4xPluswcurvedistanceWidthRadius, verticalToSquare4yPlusWcurveDistanceWidthRadius, wcurve.thickness);
            drawLine(context, square3xPluswcurvedistanceWidthRadius, verticalToSquare3yMinusWcurveDistanceWidthRadius, square4xMinuswcurvedistanceWidthRadius, verticalToSquare4yMinusWcurveDistanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare2xPlusWcurveDistanceWidthRadius, square2yMinuswcurvedistanceWidthRadius, horizontalToSquare4xPlusWcurveDistanceWidthRadius, square4yPluswcurvedistanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare2xMinusWcurveDistanceWidthRadius, square2yPluswcurvedistanceWidthRadius, horizontalToSquare4xMinuswcurvedistanceWidthRadius, square4yMinuswcurvedistanceWidthRadius, wcurve.thickness);
          } else {
            drawLine(context, square3xPluswcurvedistanceWidthRadius, verticalToSquare3yMinusWcurveDistanceWidthRadius, square4xPluswcurvedistanceWidthRadius, verticalToSquare4yMinusWcurveDistanceWidthRadius, wcurve.thickness);
            drawLine(context, square3xMinuswcurvedistanceWidthRadius, verticalToSquare3yPlusWcurveDistanceWidthRadius, square4xMinuswcurvedistanceWidthRadius, verticalToSquare4yPlusWcurveDistanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare2xPlusWcurveDistanceWidthRadius, square2yMinuswcurvedistanceWidthRadius, horizontalToSquare4xPlusWcurveDistanceWidthRadius, square4yMinuswcurvedistanceWidthRadius, wcurve.thickness);
            drawLine(context, horizontalToSquare2xMinusWcurveDistanceWidthRadius, square2yPluswcurvedistanceWidthRadius, horizontalToSquare4xMinuswcurvedistanceWidthRadius, square4yPluswcurvedistanceWidthRadius, wcurve.thickness);
          }
        }
      }
      if (iteration !== 1) {
        drawLine(context, square1xMinuswcurvedistanceWidthRadius, verticalToSquare1yPlusWcurveDistanceWidthRadius, square2xPluswcurvedistanceWidthRadius, verticalToSquare1yPlusWcurveDistanceWidthRadius, wcurve.thickness);
        drawLine(context, square1xPluswcurvedistanceWidthRadius, verticalToSquare1yMinusWcurveDistanceWidthRadius, square2xMinuswcurvedistanceWidthRadius, verticalToSquare2yMinusWcurveDistanceWidthRadius, wcurve.thickness);
        drawLine(context, square3xMinuswcurvedistanceWidthRadius, verticalToSquare3yMinusWcurveDistanceWidthRadius, square4xPluswcurvedistanceWidthRadius, verticalToSquare4yMinusWcurveDistanceWidthRadius, wcurve.thickness);
        drawLine(context, square3xPluswcurvedistanceWidthRadius, verticalToSquare3yPlusWcurveDistanceWidthRadius, square4xMinuswcurvedistanceWidthRadius, verticalToSquare4yPlusWcurveDistanceWidthRadius, wcurve.thickness);
        drawLine(context, horizontalToSquare1xPlusWcurveDistanceWidthRadius, square1yMinuswcurvedistanceWidthRadius, horizontalToSquare3xPlusWcurveDistanceWidthRadius, square3yPluswcurvedistanceWidthRadius, wcurve.thickness);
        drawLine(context, horizontalToSquare1xMinusWcurveDistanceWidthRadius, square1yPluswcurvedistanceWidthRadius, horizontalToSquare3xMinusWcurveDistanceWidthRadius, square3yMinuswcurvedistanceWidthRadius, wcurve.thickness);
        drawLine(context, horizontalToSquare2xMinusWcurveDistanceWidthRadius, square2yMinuswcurvedistanceWidthRadius, horizontalToSquare4xMinuswcurvedistanceWidthRadius, square4yPluswcurvedistanceWidthRadius, wcurve.thickness);
        drawLine(context, horizontalToSquare2xPlusWcurveDistanceWidthRadius, square2yPluswcurvedistanceWidthRadius, horizontalToSquare4xPlusWcurveDistanceWidthRadius, square4yMinuswcurvedistanceWidthRadius, wcurve.thickness);
      }
      drawWCurve(iteration - 1, moveRatioIteration / 2.0, newLeftUpX, newLeftUpY, wcurve, context, wcurve.direction.LEFT_UP, condOfDirLeftUp);
      // DRAWS W-CURVE PART ON THE TOP RIGHT
      drawWCurve(iteration - 1, moveRatioIteration / 2.0, newRightUpX, newRightUpY, wcurve, context, wcurve.direction.RIGHT_UP, condOfDirRightUp);
      // DRAWS W-CURVE PART ON THE LEFT DOWN
      drawWCurve(iteration - 1, moveRatioIteration / 2.0, newLeftBottomX, newLeftBottomY, wcurve, context, wcurve.direction.LEFT_DOWN, condOfDirLeftDown);
      // DRAWS W-CURVE PART ON THE RIGHT DOWN
      drawWCurve(iteration - 1, moveRatioIteration / 2.0, newRightBottomX, newRightBottomY, wcurve, context, wcurve.direction.RIGHT_DOWN, condOfDirRightDown);
      //drawWCurve(iteration - 2, moveRatioIteration / 2.0, newRightUpX, newRightBottomY, wcurve, context, wcurve.direction.RIGHT_UP, condOfDirRightUp);
    }
    popData();
  }  /*
		INITIAL FUNCTION
		
		1. Obtains context from HTML element
		2. Prepares/initializes W-curve fractal structure
		3. Draws W-curve fractal
	*/

  function init() {
    const canvas = document.getElementById("game");
    const context = canvas.getContext('2d');
    const circleRadius = 5;
    var numberIterations = 2;
    const lineLength = 10;
    const thickness = 1;
    const distanceWidthRadius = 2;
    const wcurve = new Wcurve(200, lineLength, thickness, distanceWidthRadius);
    var divisionStrength = 4;
    wcurve.initialize(numberIterations);
    
    ;
    let divisionValue = wcurve.moveRatio / divisionStrength;
let k = 4;
for(var j=0; j<k; j++) { 
numberIterations = j;
logData("6-1-2", wcurve.moveRatio, "wcurve.moveRatio");
logData("6-1-2", numberIterations, "numberIterations");
    if (true) {
      drawWCurve(numberIterations, wcurve.moveRatio / divisionStrength, 200, 200, wcurve, context, wcurve.direction.ALL, false, false, false, false);
    }
}
    drawWCurve(numberIterations, wcurve.moveRatio / 16, 200, 200, wcurve, context, wcurve.direction.ALL, false, false, false, false);
  }  init();
}
