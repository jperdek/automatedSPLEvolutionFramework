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
	Object where data for Koch shape are stored - similar to class representation
	
	@param {number} initialX - initial x coordinate where drawing starts
	@param {number} initialY - initial y coordinate where drawing starts
	@param {number} numberIterations - number iteration of fractal generation (number extensions/levels of original shape)
	@param {number} initialSideLength - initial length of side of koch shape which should be recalculated according number of iteration later
*/
class KochMapInfo {
  initialX: number;
  initialY: number;
  numberIterations: number;
  sideLength: number;
  kochMapString: string = "";

  constructor(initialX: number, initialY: number, numberIterations: number, initialSideLength: number) {
    this.initialX = initialX;
    this.initialY = initialY;
    this.numberIterations = numberIterations;
    this.sideLength = initialSideLength;
  }

  setFinalKochMapString(kochMapString: string): void { 
    this.kochMapString = kochMapString; 
  }

  setFinalSideLength(sideLength: number): void { 
    this.sideLength = sideLength; 
  }
}


export class Koch {
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
    @param {number} radius - radius of circle (newly created points)
    
  */
  drawKoch(context: CanvasRenderingContext2D, kochMapInfo: KochMapInfo, circleRadius: number): void {
    const KOCH_RULE: string = "R++R++R";
    const KOCH_RULE2: string = "R-R++R-R";
    let kochMapString: string = KOCH_RULE; 
    let sideLength: number = 500;
    
    this.createKochRecurrenceString(kochMapInfo, kochMapString, sideLength, KOCH_RULE2);
    this.drawKochRecurrence(context, kochMapInfo, kochMapInfo.kochMapString, kochMapInfo.sideLength, circleRadius);
  }


  drawKochOwn(KOCH_RULE: string, KOCH_RULE2: string, context: CanvasRenderingContext2D, 
          kochMapInfo: KochMapInfo, circleRadius: number): void {
    let kochMapString: string = KOCH_RULE; 
    let sideLength: number = 500;
    
    this.createKochRecurrenceString(kochMapInfo, kochMapString, sideLength, KOCH_RULE2);
    this.drawKochRecurrence(context, kochMapInfo, kochMapInfo.kochMapString, kochMapInfo.sideLength, circleRadius);
  }

  
  /*
    EXPANDS Koch recurrence string according number iterations
    
    @param {KochMapInfo} kochMapInfo - koch mapping representation
    @param {String} kochMapString - base/initial Koch string (which should be extended in each iteration)
    @param {number} sideLength - initial length of side of koch shape 
    @param {String} KOCH_RULE2 - Koch rule which should be applied on every R in recursion
    
  */
  private createKochRecurrenceString(kochMapInfo: KochMapInfo, kochMapString: string, 
        sideLength: number, KOCH_RULE2: string): void {
    let toHelp: string = "";
    for(let j=0; j< kochMapInfo.numberIterations; j++){
      toHelp = "";
      for(let i=0; i<kochMapString.length; i++){
        if(kochMapString[i] == 'R'){
          toHelp =  toHelp + KOCH_RULE2;
        } else {
          toHelp = toHelp + kochMapString[i];
        }
      }
      kochMapString = toHelp;
    }

    sideLength = sideLength / Math.pow(3,kochMapInfo.numberIterations);
    kochMapInfo.setFinalKochMapString(kochMapString);
    kochMapInfo.setFinalSideLength(sideLength);
  }


  /*
    CONVERTS DEGREES TO RADIANS
    
    @param {number} angle - angle which should be converted to radians
  */
  private degreeToRadians(angle: number): number {
    return Math.PI * angle / 180.0;
  }


  /*
    DRAWS KOCH ACCORDING PREPARED STRING
    
    @param {Context} context - context from Canvas to perform paint operations
    @param {Canvas} canvas - canvas where shape will be drawn (associated with context)
    @param {KochMapInfo} kochMapInfo - koch mapping representation
    @param {String} kochMapString - base/initial Koch string (which should be extended in each iteration)
    @param {number} sideLength - initial length of side of koch shape 
    @param {number} radius - radius for newly created points
  */
  private drawKochRecurrence(context: CanvasRenderingContext2D, kochMapInfo: KochMapInfo,
        kochMapString: string, sideLength: number, radius: number): void {
    let x1 = kochMapInfo.initialX;	//initial x
    let y1 = kochMapInfo.initialY;	//initial y
    let x2, y2;
    let angle = 0.0; //initial angle
    let point1 = new Circle(x1,y1, radius);
    let point2;
    let thickness = 2;
    //gameInfo.circles.push(point1);
    drawCircle(context, x1, y1, radius);
    
    for (let i=0; i<kochMapString.length; i++){
      
      // NEW POINT 
      if (kochMapString[i] == 'R'){
        // EVALUATES NEW COORDINATES (x2, y2)
        x2 = x1 + Math.cos(this.degreeToRadians(angle))*sideLength;
        y2 = y1 + Math.sin(this.degreeToRadians(angle))*sideLength;
        
        // CREATES NEW POINT (x2, y2)
        point2 = new Circle(x2, y2, radius);
        drawCircle(context, x2, y2, radius);
        
        // CONNECTS NEW POINT WITH PREVIOUS ONE
        //if(x2 > 0.0 && y2 > 0.0 && x2 < canvasWidth && y2 < canvasHeight){
          //gameInfo.lines.push(new Line(point1, point2, thickness));  //saves line to array
          drawLine(context, x1, y1, x2, y2, thickness);
        //}
        
        //old points will be second and previous will be overwritten
        // SETS NEW POINT AS OLD ONE
        x1 = x2;
        y1 = y2;
        point1 = point2;
        
      // LEFT ROTATION
      } else if (kochMapString[i] == '+') {
        angle = angle + 60.0;
        if (angle >= 360.0) {
          angle = 0.0;
        }
        
      // RIGHT ROTATION
      } else if (kochMapString[i] == '-') {
        angle = angle - 60.0;
        if (angle <= - 60.0) {
          angle = 300.0;
        }
      }
      
    }
  }
}


export function drawKoch(initialX: number, initialY: number, context: CanvasRenderingContext2D,  
      circleRadius: number, numberIterations: number): void {
  const kochMapInfo = new KochMapInfo(initialX, initialY, numberIterations, 300);
  const koch = new Koch();
  koch.drawKoch(context, kochMapInfo, circleRadius);
}
`;