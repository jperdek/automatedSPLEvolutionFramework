var aaa1 = `

/*
	CIRCLE OBJECT - definition
	
	@param {number} x - center x coordinate of circle
	@param {number} y - center y coordinate of circle
	@param {number} radius - radius of circle
*/
class Circle {
  x: number;
  y: number;
  radius: number;

  constructor(x: number, y: number, radius: number) {
    this.x = x;
    this.y = y;
    this.radius = radius;
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
function drawCircle(context: CanvasRenderingContext2D, x: number, y: number, radius: number): void {
	context.fillStyle = "rgba(200, 200, 100, .9)";
	context.beginPath();
	context.arc(x, y, radius, 0, Math.PI*2, true);
	context.closePath();
	context.fill();
}


/*
	Object where data for Sierpinski shape are stored - similar to class representation
	
	@param {number} initialX - initial x coordinate where drawing starts
	@param {number} initialY - initial y coordinate where drawing starts
	@param {number} numberIterations - number iteration of fractal generation (number extensions/levels of original shape)
	@param {number} initialSideLength - initial length of side of koch shape which should be recalculated according number of iteration later
*/
class SierpinskyMapInfo {
  initialX: number;
  initialY: number; 
  numberIterations: number;
  sideLength: number;
  sierpinskyMapString: string = "";

  constructor(initialX: number, initialY: number, numberIterations: number, initialSideLength: number){
    this.initialX = initialX;
    this.initialY = initialY;
    this.numberIterations = numberIterations;
    this.sideLength = initialSideLength;
    this.sierpinskyMapString;
  }

  setFinalSierpinskyMapString(sierpinskyMapString: string) { this.sierpinskyMapString = sierpinskyMapString; }
  setFinalSideLength(sideLength: number) { this.sideLength = sideLength; }
}


export class SierpinskiFractal {
  /*
    Initial class where Koch rules are defined
    createKochRecurrenceString() - creates whole string which should be drawn
    drawKochRecurrence() - draws Koch according given Koch string

    + - left rotation
    - - right rotation
    R - string to be expanded, creates new point after rotation
    
    @param {Context} context - context from Canvas to perform paint operations
    @param {Canvas} canvas - canvas where shape will be drawn (associated with context)
    @param {KochMapInfo} kochMapInfo - koch mapping representation
    @param {number} circleRadius - radius of circle (newly created points)
    @param {number} initialSideLength - initial length of side of koch shape which should be recalculated according number of iteration later
  */
  drawSierpinsky(context: CanvasRenderingContext2D, sierpinskyMapInfo: SierpinskyMapInfo, circleRadius: number, sideLength: number): void {
    const SIERPINSKY_RULE: string = "RSR++RR++RR";
    const SIERPINSKY_RULE_ROT: string = "RR";
    const SIERPINSKY_RULE_SUB: string = "++RSR--RSR--RSR++";
    let sierpinskyMapString: string = SIERPINSKY_RULE; 
      
    this.createSierpinskyRecurrenceString(sierpinskyMapInfo, sierpinskyMapString, sideLength, SIERPINSKY_RULE_ROT, SIERPINSKY_RULE_SUB);
    this.drawSierpinskyRecurrence(context, sierpinskyMapInfo, sierpinskyMapInfo.sierpinskyMapString, sierpinskyMapInfo.sideLength, circleRadius);
  }

  /*
    EXPANDS SIERPINSKI recurrence string according number iterations
    
    @param {SierpinskiMapInfo} sierpinskyMapInfo - Sierpinski mapping representation
    @param {String} sierpinskyMapString - base/initial Sierpinski string (which should be extended in each iteration)
    @param {number} sideLength - initial length of side of koch shape 
    @param {String} SIERPINSKY_RULE_ROT - Sierpinski rotation rule which should be applied on every R in recursion
    @param {String} SIERPINSKY_RULE_SUB - Sierpinski substitution rule which should be applied on every S in recursion
    
  */
  createSierpinskyRecurrenceString(sierpinskyMapInfo: SierpinskyMapInfo, sierpinskyMapString: string, sideLength: number,
            SIERPINSKY_RULE_ROT: string, SIERPINSKY_RULE_SUB: string): void {
    var toHelp: string = "";
    for(let j: number = 0; j < sierpinskyMapInfo.numberIterations; j++){
      toHelp = "";
      for(let i: number = 0; i<sierpinskyMapString.length; i++){
        if(sierpinskyMapString[i] == 'R'){
          toHelp =  toHelp + SIERPINSKY_RULE_ROT;
        } else if(sierpinskyMapString[i] == 'S'){
          toHelp = toHelp + SIERPINSKY_RULE_SUB;
        }else {
          toHelp = toHelp + sierpinskyMapString[i];
        }
      }
      sierpinskyMapString = toHelp;
    }

    sideLength = sideLength / Math.pow(2,sierpinskyMapInfo.numberIterations);
    sierpinskyMapInfo.setFinalSierpinskyMapString(sierpinskyMapString);
    sierpinskyMapInfo.setFinalSideLength(sideLength);
  }


  /*
    CONVERTS DEGREES TO RADIANS
    
    @param {number} angle - angle which should be converted to radians
  */
  degreeToRadians(angle: number): number {
    return Math.PI * angle / 180.0;
  }

  /*
    DRAWS SIERPINSKI ACCORDING PREPARED STRING
    
    @param {Context} context - context from Canvas to perform paint operations
    @param {Canvas} canvas - canvas where shape will be drawn (associated with context)
    @param {SierpinskiMapInfo} sierpinskyMapInfo - Sierpinski mapping representation
    @param {String} sierpinskyMapString - base/initial Sierpinski string (which should be extended in each iteration)
    @param {number} sideLength - initial length of side of koch shape 
    @param {number} radius - radius for newly created points
  */
  drawSierpinskyRecurrence(context: CanvasRenderingContext2D, sierpinskyMapInfo: SierpinskyMapInfo, 
        sierpinskyMapString: string, sideLength: number, radius: number): void {
    let x1: number = sierpinskyMapInfo.initialX;	//initial x
    let y1: number = sierpinskyMapInfo.initialY;	//initial y
    let x2: number, y2: number;
    let angle: number = 0.0; //initial angle
    let point1 = new Circle(x1,y1, radius);
    let point2;
    let thickness: number = 2;

    drawCircle(context, x1, y1, radius);
    
    for(var i=0; i<sierpinskyMapString.length; i++){
      
      // NEW POINT 
      if(sierpinskyMapString[i] == 'R'){
        // EVALUATES NEW COORDINATES (x2, y2)
        x2 = x1 + Math.cos(this.degreeToRadians(angle))*sideLength;
        y2 = y1 + Math.sin(this.degreeToRadians(angle))*sideLength;
        
        // CREATES NEW POINT (x2, y2)
        point2 = new Circle(x2, y2, radius);
        drawCircle(context, x2, y2, radius);
        
        // CONNECTS NEW POINT WITH PREVIOUS ONE
        //if(x2 > 0.0 && y2 > 0.0 && x2 < canvasWidth && y2 < canvasHeight){
          drawLine(context, x1, y1, x2, y2, thickness);
        //}
        
        //old points will be second and previous will be overwritten
        // SETS NEW POINT AS OLD ONE
        x1 = x2;
        y1 = y2;
        point1 = point2;
        
      // LEFT ROTATION
      } else if(sierpinskyMapString[i] == '+'){
        angle = angle + 60.0;
        if(angle >= 360.0) {
          angle = 0.0;
        }
      
      // RIGHT ROTATION
      } else if(sierpinskyMapString[i] == '-'){
        angle = angle - 60.0;
        if(angle <= - 60.0) {
          angle = 300.0;
        }
      }
      
    }
  }
}

export function drawSierpinski(initialX: number, initialY: number, context: CanvasRenderingContext2D,  
  circleRadius: number, numberIterations: number, initialSideLength: number): void {
    const sierpinskyMapInfo = new SierpinskyMapInfo(initialX, initialY, numberIterations, initialSideLength);
    const sierpinskiFractal = new SierpinskiFractal();
    sierpinskiFractal.drawSierpinsky(context, sierpinskyMapInfo, circleRadius, initialSideLength);
}
`;