var aaa1dd8ce = `/*
export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
    let fiveSideFractal = new FiveSideFractal();
    fiveSideFractal.custom(context, radius, iterations, thickness, side);
}
*/
var globalResult = {};
function logData(variabilityPointIdentifier, data, name) {
    if (globalResult[variabilityPointIdentifier] === undefined) {
        globalResult[variabilityPointIdentifier] = {};
    }
    ;
    if (globalResult[variabilityPointIdentifier][name] === undefined) {
        globalResult[variabilityPointIdentifier][name] = [];
    }
    ;
    globalResult[variabilityPointIdentifier][name].push(data);
}
/*
export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
    let fiveSideFractal = new FiveSideFractal();
    fiveSideFractal.custom(context, radius, iterations, thickness, side);
}
*/
export function drawCirclePrevCoords(context: CanvasRenderingContext2D, radius: number) {
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), radius, "__context_" + context + "__radius_" + radius + "");
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), radius, "__radius_" + radius + "");
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), radius, "__context_" + context + "");
    if (context.currentX !== undefined && context.currentY !== undefined) {
        context.fillStyle = "rgba(300, 250, 150, .9)";
        let x = context.currentX;
        let y = context.currentY;
        context.beginPath();
        context.arc(x, y, radius, 0, Math.PI * 2, true);
        console.log(x);
        console.log(y);
        context.closePath();
        context.fill();
    }
}
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
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), radius, "__x_" + x + "__y_" + y + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), radius, "__x_" + x + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), radius, "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), radius, "__y_" + y + "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), radius, "__x_" + x + "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), radius, "__y_" + y + "");
        this.x = x;
        this.y = y;
        this.radius = radius;
    }
}
/*
    LINE OBJECT - definition
    
    @param {Circle} startPoint - start point of line
    @param {Circle} endPoint - end point of line
    @param {number} thickness - thickness/width of line
*/
class Line {
    startPoint: number;
    endPoint: number;
    thickness: number;
    constructor(startPoint: number, endPoint: number, thickness: number) {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), startPoint, "__thickness_" + thickness + "__startPoint_" + startPoint + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), startPoint, "__thickness_" + thickness + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), startPoint, "__endPoint_" + endPoint + "__thickness_" + thickness + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), startPoint, "__startPoint_" + startPoint + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), startPoint, "__endPoint_" + endPoint + "__startPoint_" + startPoint + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), startPoint, "__endPoint_" + endPoint + "");
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.thickness = thickness;
    }
}
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
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__thickness_" + thickness + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__context_" + context + "__y1_" + y1 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__context_" + context + "__y2_" + y2 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__thickness_" + thickness + "__x2_" + x2 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__x1_"
        + x1 + "__y2_" + y2 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__thickness_" + thickness + "__x1_" + x1 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__x1_"
        + x1 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__x2_"
        + x2 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__thickness_" + thickness + "__context_" + context + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__context_" + context + "__x1_" + x1 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__context_" + context + "__x2_" + x2 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__y2_"
        + y2 + "__x2_" + x2 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__thickness_" + thickness + "__y2_" + y2 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__x1_"
        + x1 + "__x2_" + x2 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__thickness_" + thickness + "__y1_" + y1 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__y1_"
        + y1 + "__y2_" + y2 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__y1_"
        + y1 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__y2_"
        + y2 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__y1_"
        + y1 + "__x2_" + x2 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__y1_"
        + y1 + "__x1_" + x1 + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), x2, "__context_" + context + "");
    context.beginPath();
    context.moveTo(x1, y1);
    context.lineTo(x2, y2);
    //window.cordX = x2;
    //window.cordY = y2;
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
function drawCircle(context: CanvasRenderingContext2D, cordX: number, cordY: number, radius: number) {
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), cordX, "__cordX_" + cordX + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), cordX, "__context_" + context + "__cordX_" + cordX + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), cordX, "__context_" + context + "__radius_" + radius + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), cordX, "__cordY_" + cordY + "__radius_" + radius + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), cordX, "__cordY_" + cordY + "__cordX_" + cordX + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), cordX, "__radius_" + radius + "__cordX_" + cordX + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), cordX, "__cordY_" + cordY + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), cordX, "__context_" + context + "__cordY_" + cordY + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), cordX, "__radius_" + radius + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), cordX, "__context_" + context + "");
    context.fillStyle = "rgba(200, 200, 100, .9)";
    context.beginPath();
    context.arc(cordX, cordY, radius, 0, Math.PI * 2, true);
    context.closePath();
    context.fill();
}
/*
    VECTOR 2D REPRESENTATION
    
    - stores information about its components
    
    @param {number} x - component x of the vector
    @param {number} y - component y of the vector
*/
class Vector {
    x: number;
    y: number;
    constructor(x: number, y: number) {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), y, "__x_" + x + "__y_" + y + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), y, "__x_" + x + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), y, "__y_" + y + "");
        this.x = x;
        this.y = y;
    }
}
/*
    CONVERTS DEGREES TO RADIANS
    
    @param {number} angle - angle which should be converted to radians
*/
function degreeToRadians(angle: number): number {
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), angle, "__angle_" + angle + "");
    return Math.PI * angle / 180;
}
/*
    FIVE SIDE FRACTAL REPRESENTATION
    
    @param {number} dimension - number of points which should result shape have
    @param {number} initialX - initial x coordinate where drawing starts
    @param {number} initialY - initial y coordinate where drawing starts
    @param {number} initialSideLength - initial length of side of polygon side
*/
class FiveSideMapInfo {
    initialX: number;
    initialY: number;
    sideLength: number;
    dimension: number;
    angleForPoint: number;
    ruleString: string;
    radius: number = 5;
    centerPoint: Circle;
    /*
      CREATE RULE TO GENERATE POLYGONS LYING ON CIRCUMCIRCLE
      
      @param {number} dimension - number of points which should result shape have (5 in this case)
    */
    createRule(dimension: number): string {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__dimension_" + dimension + "");
        if (dimension < 3) {
            console.log("WRONG DIMENSION");
        }
        let string = "";
        for (let i = 0; i < dimension - 1; i++) {
            string = string + "R+";
        }
        return string + "R";
    }
    constructor(context: CanvasRenderingContext2D, dimension: number, initialX: number, initialY: number, initialSideLength: number) {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), initialSideLength, "__initialX_" + initialX + "__initialSideLength_" + initialSideLength + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), initialSideLength, "__initialY_" + initialY + "__dimension_" + dimension +
            "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), initialSideLength, "__initialY_" + initialY + "__initialSideLength_" + initialSideLength + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), initialSideLength, "__dimension_" + dimension + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), initialSideLength, "__dimension_" + dimension + "__initialSideLength_" + initialSideLength + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), initialSideLength, "__context_" + context + "__dimension_" + dimension + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), initialSideLength, "__context_" + context + "__initialSideLength_" + initialSideLength + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), initialSideLength, "__initialY_" + initialY + "__initialX_" + initialX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), initialSideLength, "__dimension_" + dimension + "__initialX_" + initialX +
            "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), initialSideLength, "__initialSideLength_" + initialSideLength + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), initialSideLength, "__context_" + context + "__initialY_" + initialY + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), initialSideLength, "__context_" + context + "__initialX_" + initialX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), initialSideLength, "__initialX_" + initialX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), initialSideLength, "__context_" + context + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), initialSideLength, "__initialY_" + initialY + "");
        this.initialX = initialX;
        this.initialY = initialY;
        this.sideLength = initialSideLength;
        this.dimension = dimension;
        this.angleForPoint = 360 / dimension;
        this.ruleString = this.createRule(dimension);
    }
    setFinalSideLength(sideLength: number) {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), sideLength, "__sideLength_" + sideLength + "");
        this.sideLength = sideLength;
    }
    /*
      FINDS CENTER POINT OF POLYGON - ONLY FOR THOSE POLYGONS WHERE ALL POINTS LYING ON CIRCUMCIRCLE AND WITH EQUAL EDGES
      
      1. finds middle point from ordered points where ordering starts from initial one (average)
      2. creates vector from initial and middle point and gets its size
      a) EVEN POINT POLYGONS
        1. half is radius for even numbered polygons)
      b) ODD POINT POLYGONS
        1. finds next point (behind inital) and middle point
        2. creates vector from these points and gets their size
        3. evaluates angle between these two vectors: Alfa = acos((ca.x*cb.x + ca.y * cb.y) / (|ca| * |cb|))
        4. evaluates average by using sinus equation: 2*r = |BA| / SIN(<ABC)
        5. final coordinates of resulting middle point where first line is horizontal are:
            [x,y] = [surX(A) + |BA| / 2, surY(A) + r]
  
  
      @param {Polygon} polygonClass - class with defined equal side polygon parameters
      @param {number} initialX - initial coordinate x of polygon
      @param {number} initialY - initial coordinate y of polygon
      @param {number} sideLength - length of side of polygon side
      @param {number} angleForPoint - angle to get next point of polygon
      @param {number} dimension - number of points which should result shape have
      @param {number} radius - radius of circle
    */
    findCenterPoint(polygonClass: FiveSideMapInfo, context: CanvasRenderingContext2D, initialX: number, initialY: number, sideLength: number, angleForPoint: number, dimension: number, radius: number): Circle {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__polygonClass_" + polygonClass + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__radius_" + radius + "__dimension_" + dimension + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__angleForPoint_" + angleForPoint + "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__context_" + context + "__angleForPoint_" + angleForPoint + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__polygonClass_" + polygonClass + "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__radius_" + radius + "__initialX_" + initialX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__polygonClass_" + polygonClass + "__initialY_" + initialY + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__polygonClass_" + polygonClass + "__initialX_" + initialX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__context_" + context + "__initialY_" + initialY + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__context_" + context + "__initialX_" + initialX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__polygonClass_" + polygonClass + "__dimension_" + dimension + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__sideLength_" + sideLength + "__polygonClass_" + polygonClass + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__sideLength_" + sideLength + "__dimension_" + dimension + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__polygonClass_" + polygonClass + "__context_" + context + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__initialX_" + initialX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__context_" + context + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__initialY_" + initialY + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__angleForPoint_" + angleForPoint + "__dimension_" + dimension + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__initialY_" + initialY + "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__angleForPoint_" + angleForPoint + "__initialY_" + initialY + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__angleForPoint_" + angleForPoint + "__initialX_" + initialX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__context_" + context + "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__initialY_" + initialY + "__dimension_" + dimension + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__sideLength_" + sideLength + "__angleForPoint_" + angleForPoint +
            "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__dimension_" + dimension + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__context_" + context + "__dimension_" + dimension + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__initialY_" + initialY + "__initialX_" + initialX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__sideLength_" + sideLength + "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__angleForPoint_" + angleForPoint + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__sideLength_" + sideLength + "__initialY_" + initialY + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__sideLength_" + sideLength + "__initialX_" + initialX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__sideLength_" + sideLength + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__polygonClass_" + polygonClass + "__angleForPoint_" + angleForPoint
            + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__sideLength_" + sideLength + "__context_" + context + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), dimension, "__initialX_" + initialX + "__dimension_" + dimension + "");
        //FOR ODD DIMENSION
        //finds first line 
        //finds next point
        let nextX: number = initialX + Math.cos(degreeToRadians(0)) * sideLength; //coordinate x of next point
        let nextY: number = initialY + Math.sin(degreeToRadians(0)) * sideLength; //coordinate y of next point
        let x1: number = nextX;
        let y1: number = nextY;
        let x2: number = nextX;
        let y2: number = nextY;
        let middleX: number, middleY: number;
        let angle: number = angleForPoint;
        //get middle and last point from points on circumcircle
        for (let i = 1; i < Math.round(dimension / 2); i++) {
            x2 = x1 + Math.cos(degreeToRadians(angle)) * sideLength;
            y2 = y1 + Math.sin(degreeToRadians(angle)) * sideLength;
            //var canvas = document.getElementById("game");
            //var context = canvas.getContext('2d');
            //var x3, y3;
            //x3 = x1 - Math.cos(degreeToRadians(2*angle))*sideLength;
            //y3 = y1 + Math.sin(degreeToRadians(2*angle))*sideLength;
            //drawLine(context, x1, y1, x3, y3, 1);
            angle = angle + angleForPoint;
            x1 = x2;
            y1 = y2;
        }
        //get middle from points on circumcircle
        middleX = x2;
        middleY = y2;
        const bottom1Vector = new Vector(middleX - initialX, middleY - initialY);
        const sizeOfBottom1V = Math.sqrt(Math.pow(bottom1Vector.x, 2) + Math.pow(bottom1Vector.y, 2));
        if (dimension % 2 == 0) {
            polygonClass.radius = sizeOfBottom1V / 2;
            drawCircle(context, initialX + (middleX - initialX) / 2, initialY + (middleY - initialY) / 2, polygonClass.radius);
            return new Circle(initialX + (middleX - initialX) / 2, initialY + (middleY - initialY) / 2, radius);
        }
        else {
            const bottom2Vector = new Vector(middleX - nextX, middleY - nextY);
            const sizeOfBottom2V = Math.sqrt(Math.pow(bottom2Vector.x, 2) + Math.pow(bottom2Vector.y, 2));
            const angleOfVectors = (Math.acos((bottom1Vector.x * bottom2Vector.x + bottom1Vector.y * bottom2Vector.y) / (sizeOfBottom1V * sizeOfBottom2V)));
            //SINUS RULE  2R = a / SIN(FI), where a is top side and FI is angle from bottom vector
            polygonClass.radius = sideLength / (2 * Math.sin(angleOfVectors));
            //drawCircleInfo(initialX + sideLength / 2, middleY - polygonClass.radius, polygonClass.radius); //should be equal with:
            drawCircle(context, initialX + sideLength / 2, initialY + polygonClass.radius, polygonClass.radius);
            //return new Circle(initialX + sideLength / 2, middleY - polygonClass.radius, radius); //should be equal with:
            return new Circle(initialX + sideLength / 2, initialY + polygonClass.radius, radius);
        }
    }
}
/*
    POINT 2D REPRESENTATION
    
    @param {number} x - coordinate x
    @param {number} y - coordinate y
*/
class Point {
    x: number;
    y: number;
    constructor(x: number, y: number) {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), y, "__x_" + x + "__y_" + y + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), y, "__x_" + x + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), y, "__y_" + y + "");
        this.x = x;
        this.y = y;
    }
}
/*
    FIVE SIDE FRACTAL REPRESENTATION
    
    @param {number} iteration - actual processed iteration
    @param {number} sideLength - initial length of side of five side fractal shape
    @param {number} thickness - line thickness/width
*/
class FiveSide {
    vertices: any[];
    iterations: number;
    sideLengthInitial: number;
    thickness: number;
    constructor(fiveSideMapInfo: FiveSideMapInfo, iterations: number, thickness: number) {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), iterations, "__iterations_" + iterations + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), iterations, "__thickness_" + thickness + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), iterations, "__thickness_" + thickness + "__iterations_" + iterations + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), iterations, "__fiveSideMapInfo_" + fiveSideMapInfo + "__iterations_" + iterations
            +
                "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), iterations, "__thickness_" + thickness + "__fiveSideMapInfo_" + fiveSideMapInfo +
            "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classConstructorsLogger", "fname": "" }), iterations, "__fiveSideMapInfo_" + fiveSideMapInfo + "");
        this.vertices = [];
        this.iterations = iterations;
        this.sideLengthInitial = fiveSideMapInfo.sideLength;
        this.thickness = thickness;
    }
    /*
        EVALUATES NEW POINT FOR NEXT RECURSION INSIDE FIVE SIDE FRACTAL ON THE PLANE
        
        @param {number} x1 - coordinate x of start point
        @param {number} y1 - cordinate y of start point
        @param {number} x2 - coordinate x of end point
        @param {number} y2 - cordinate y of end point
        @param {number} vectorHeightX - compound X of the vector
        @param {number} vectorHeightY - compound Y of the vector
        @param {number} oppositeX - coordinate of perpendicular point x (to decide which side of plane is inside)
        @param {number} oppositeY - coordinate of perpendicular point y (to decide which side of plane is inside)
        @param {Point} new point inside five side fractal on the plane with first two point for the next recursion
    */
    getNewPoint(x1: number, y1: number, x2: number, y2: number, vectorHeightX: number, vectorHeightY: number, oppositeX: number, oppositeY: number): Point {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__vectorHeightY_" + vectorHeightY + "__oppositeX_" + oppositeX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__oppositeX_" + oppositeX + "__vectorHeightX_" + vectorHeightX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__oppositeX_" + oppositeX + "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__x1_" + x1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__vectorHeightY_" + vectorHeightY + "__vectorHeightX_" + vectorHeightX +
            "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__vectorHeightX_" + vectorHeightX + "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__vectorHeightX_" + vectorHeightX + "__oppositeY_" + oppositeY + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__vectorHeightY_" + vectorHeightY + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__x1_" + x1 + "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__y2_" + y2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__vectorHeightY_" + vectorHeightY + "__y2_" + y2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__y1_" + y1 + "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__y1_" + y1 + "__oppositeX_" + oppositeX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__y1_" + y1 + "__oppositeY_" + oppositeY + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__vectorHeightY_" + vectorHeightY + "__x1_" + x1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__vectorHeightX_" + vectorHeightX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__x2_" + x2 + "__oppositeY_" + oppositeY + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__oppositeX_" + oppositeX + "__y2_" + y2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__oppositeX_" + oppositeX + "__oppositeY_" + oppositeY + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__y2_" + y2 + "__vectorHeightX_" + vectorHeightX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__oppositeX_" + oppositeX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__x1_" + x1 + "__y2_" + y2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__oppositeX_" + oppositeX + "__x1_" + x1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__oppositeY_" + oppositeY + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__y2_" + y2 + "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__y1_" + y1 + "__y2_" + y2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__y1_" + y1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__y1_" + y1 + "__vectorHeightX_" + vectorHeightX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__y1_" + y1 + "__vectorHeightY_" + vectorHeightY + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__vectorHeightY_" + vectorHeightY + "__oppositeY_" + oppositeY + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__x1_" + x1 + "__vectorHeightX_" + vectorHeightX + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__y1_" + y1 + "__x1_" + x1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__vectorHeightY_" + vectorHeightY + "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__y2_" + y2 + "__oppositeY_" + oppositeY + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), oppositeY, "__x1_" + x1 + "__oppositeY_" + oppositeY + "");
        let normalPoint1x: number, normalPoint1y: number, normalPoint2x: number, normalPoint2y: number;
        let vectorMiddleHeightX: number, vectorMiddleHeightY: number;
        let middle12x: number, middle12y: number;
        // EVALUATES MIDDLE POINT between given points
        middle12x = this.getMiddlePoint(x1, x2);
        middle12y = this.getMiddlePoint(y1, y2);
        // CREATES VECTOR ON THE LINE FROM THE FIRST POINT [lengthToTheMiddle of line - first point] (F1 -> S) 
        vectorMiddleHeightX = vectorHeightX - x1;
        vectorMiddleHeightY = vectorHeightY - y1;
        // EVALUATES NEW NORMAL POINTS (from created vector in place of the middle point)
        normalPoint1x = middle12x + (-vectorMiddleHeightY);
        normalPoint1y = middle12y + (vectorMiddleHeightX);
        normalPoint2x = middle12x + (vectorMiddleHeightY);
        normalPoint2y = middle12y + (-vectorMiddleHeightX);
        // DECISION WHICH POINT IS INSIDE FIVE POINT FRACTAL (WHICH NORMAL IS CORRECT - CORRECT DIRECTION)
        if (this.countDistance(normalPoint1x, normalPoint1y, oppositeX, oppositeY) <
            this.countDistance(normalPoint2x, normalPoint2y, oppositeX, oppositeY)) {
            return new Point(normalPoint1x, normalPoint1y);
        }
        return new Point(normalPoint2x, normalPoint2y);
    }
    /*
        EVALUATES HIGHT OF MIDDLE AREA IN NEXT RECURSION
        
        @param {number} smallerSideLength - length of the side in next recursion
        @param {number} middleSpace - middle space between two sides occuring in next recursion
        @return {number} height/length of the middle area
    */
    countMiddleAreaHeight(smallerSideLength: number, middleSpace: number): number {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), smallerSideLength, "__middleSpace_" + middleSpace + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), smallerSideLength, "__smallerSideLength_" + smallerSideLength + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), smallerSideLength, "__middleSpace_" + middleSpace + "__smallerSideLength_" + smallerSideLength + "");
        return Math.round(Math.sqrt(Math.pow(smallerSideLength, 2) - Math.pow(middleSpace / 2, 2)));
    }
    /*
        EVALUATES MIDDLE SPACE BETWEEN TWO SMALL SIDES IN NEXT RECURSION
        
        @param {number} sideLength - length of the side in actual recursion
        @return {number} length of middle space between two small sides occuring in the next recursion
    */
    countMiddleSpace(smallerSideLength: number): number {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), smallerSideLength, "__smallerSideLength_" + smallerSideLength + "");
        return ((smallerSideLength * Math.sin(degreeToRadians(36))) / Math.sin(degreeToRadians(72)));
    }
    /*
        EVALUATES LENGTH OF FIVE SIDE IN NEXT RECURSION
        
        @param {number} sideLength - length of the side in actual recursion
        @return {number} length of side in the next recursion (will be smaller)
    */
    countSmallerSideLength(sideLength: number): number {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), sideLength, "__sideLength_" + sideLength + "");
        return (Math.sin(degreeToRadians(72)) * sideLength / (2 * Math.sin(degreeToRadians(72)) + Math.sin(degreeToRadians(36))));
    }
    /*
        EVALUATES POINT between two points ACCORDING RATIO FROM <0, 1> (KNOWN AS INTERPOLATION)
        
        @param {number} x1 - coordinate of start point
        @param {number} x2 - coordinate of end point
        @param {number} ratio - ratio between <0, 1>
        @param {number} inverseRatio - inverseRatio between <0, 1> (should be: 1 - ratio)
        @return {number} coordinate of point between specified coordinates according ratio
    */
    getPointOnLine(x1: number, x2: number, ratio: number, inverseRatio: number): number {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), ratio, "__inverseRatio_" + inverseRatio + "__x1_" + x1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), ratio, "__inverseRatio_" + inverseRatio + "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), ratio, "__x1_" + x1 + "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), ratio, "__x1_" + x1 + "__ratio_" + ratio + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), ratio, "__x2_" + x2 + "__ratio_" + ratio + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), ratio, "__inverseRatio_" + inverseRatio + "__ratio_" + ratio + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), ratio, "__x1_" + x1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), ratio, "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), ratio, "__inverseRatio_" + inverseRatio + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), ratio, "__ratio_" + ratio + "");
        return x1 * inverseRatio + x2 * ratio;
    }
    /*
        EVALUATES DISTANCE between two points
        
        @param {number} x1 - coordinate x of start point
        @param {number} y1 - cordinate y of start point
        @param {number} x2 - coordinate x of end point
        @param {number} y2 - cordinate y of end point
    */
    countDistance(x1: number, y1: number, x2: number, y2: number): number {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x2, "__x1_" + x1 + "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x2, "__y1_" + y1 + "__y2_" + y2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x2, "__y1_" + y1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x2, "__y2_" + y2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x2, "__x1_" + x1 + "__y2_" + y2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x2, "__y1_" + y1 + "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x2, "__x1_" + x1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x2, "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x2, "__y1_" + y1 + "__x1_" + x1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x2, "__y2_" + y2 + "__x2_" + x2 + "");
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    /*
        EVALUATES MIDDLE POINT for given coordinate
        
        @param {number} coordStart - coordinate of beginning point
        @param {number} coordEnd - coordinate of ending point
        @return {number} evaluated middle coordinate
    */
    getMiddlePoint(coordStart: number, coordEnd: number): number {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), coordStart, "__coordStart_" + coordStart + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), coordStart, "__coordEnd_" + coordEnd + "__coordStart_" + coordStart + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), coordStart, "__coordEnd_" + coordEnd + "");
        return coordStart + (coordEnd - coordStart) / 2;
    }
}
export class FiveSideFractal {
    /*
      DRAWS FIVE SIDES FRACTAL
      
      getting side:
      k =(𝒏 ∗ 𝐬𝐢𝐧⁡(𝟕𝟐°))/(𝟐 ∗ 𝐬𝐢𝐧⁡(𝟕𝟐°)  + 𝒔𝒊𝒏(𝟑𝟔°) )
      
      |ST| = T-S = rp, where rp is vector pointing to new point T from point S (middle point of line)
      New point T:
      T [rp_x + S_x, rp_y + S_y]
  
      
      @param {number} iteration - actual processed iteration
      @param {number} sideLength - initial length of side of Five side fractal shape
      @param {number} x1 - coordinate x of first point
      @param {number} y1 - coordinate y of first point
      @param {number} x2 - coordinate x of second point
      @param {number} y2 - coordinate y of second point
      @param {number} x3 - coordinate x of third point
      @param {number} y3 - coordinate y of third point
      @param {number} x4 - coordinate x of fourth point
      @param {number} y4 - coordinate y of fourth point
      @param {number} x5 - coordinate x of fifth point
      @param {number} y5 - coordinate y of fifth point
      @param {FiveSide} fiveSide - five side data structure
      @param {Context} context - context from Canvas to perform paint operations
      
    */
    getFiveSideShapes(iteration: number, sideLength: number, x1: number, y1: number, x2: number, y2: number, x3: number, y3: number, x4: number, y4: number, x5: number, y5: number, fiveSide: FiveSide, context: CanvasRenderingContext2D): void {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x4_" + x4 + "__y5_" + y5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y2_" + y2 + "__y4_" + y4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__fiveSide_" + fiveSide + "__y4_" + y4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__sideLength_" + sideLength + "__y4_" + y4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y2_" + y2 + "__x3_" + x3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__fiveSide_" + fiveSide + "__context_" + context + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__fiveSide_" + fiveSide + "__x3_" + x3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y1_" + y1 + "__y5_" + y5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__sideLength_" + sideLength + "__x3_" + x3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__fiveSide_" + fiveSide + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x3_" + x3 + "__x5_" + x5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y1_" + y1 + "__x4_" + x4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y2_" + y2 + "__y3_" + y3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__fiveSide_" + fiveSide + "__y3_" + y3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x1_" + x1 + "__y2_" + y2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__sideLength_" + sideLength + "__y3_" + y3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y2_" + y2 + "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x3_" + x3 + "__y5_" + y5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__fiveSide_" + fiveSide + "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y1_" + y1 + "__y4_" + y4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__sideLength_" + sideLength + "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x3_" + x3 + "__x4_" + x4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y1_" + y1 + "__x3_" + x3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y3_" + y3 + "__x4_" + x4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__iteration_" + iteration + "__x1_" + x1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__context_" + context + "__y3_" + y3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y5_" + y5 + "__x5_" + x5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__iteration_" + iteration + "__y2_" + y2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__sideLength_" + sideLength + "__y2_" + y2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x5_" + x5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__context_" + context + "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x3_" + x3 + "__y4_" + y4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__fiveSide_" + fiveSide + "__x5_" + x5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__sideLength_" + sideLength + "__x1_" + x1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__iteration_" + iteration + "__y5_" + y5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__context_" + context + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__iteration_" + iteration + "__x4_" + x4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y2_" + y2 + "__y5_" + y5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x1_" + x1 + "__y4_" + y4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__fiveSide_" + fiveSide + "__y1_" + y1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__fiveSide_" + fiveSide + "__iteration_" + iteration + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__sideLength_" + sideLength + "__y5_" + y5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y2_" + y2 + "__x4_" + x4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__context_" + context + "__iteration_" + iteration + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__context_" + context + "__x5_" + x5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x1_" + x1 + "__x3_" + x3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y1_" + y1 + "__y2_" + y2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y1_" + y1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__sideLength_" + sideLength + "__x4_" + x4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__sideLength_" + sideLength + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y3_" + y3 + "__y5_" + y5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__iteration_" + iteration + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y1_" + y1 + "__x1_" + x1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x2_" + x2 + "__y4_" + y4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__iteration_" + iteration + "__x3_" + x3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x2_" + x2 + "__x3_" + x3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__context_" + context + "__y5_" + y5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x1_" + x1 + "__y5_" + y5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__sideLength_" + sideLength + "__iteration_" + iteration + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y1_" + y1 + "__context_" + context + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__iteration_" + iteration + "__y4_" + y4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y4_" + y4 + "__y5_" + y5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x3_" + x3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__context_" + context + "__x4_" + x4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x1_" + x1 + "__x4_" + x4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y4_" + y4 + "__x4_" + x4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y4_" + y4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x2_" + x2 + "__y3_" + y3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y3_" + y3 + "__x5_" + x5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__iteration_" + iteration + "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x2_" + x2 + "__x4_" + x4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__context_" + context + "__y4_" + y4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__iteration_" + iteration + "__y3_" + y3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__context_" + context + "__x3_" + x3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y1_" + y1 + "__iteration_" + iteration + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x1_" + x1 + "__x5_" + x5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y4_" + y4 + "__x5_" + x5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y3_" + y3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__iteration_" + iteration + "__x5_" + x5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x2_" + x2 + "__x5_" + x5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x1_" + x1 + "__y3_" + y3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__fiveSide_" + fiveSide + "__y2_" + y2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x1_" + x1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y2_" + y2 + "__x5_" + x5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x1_" + x1 + "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__fiveSide_" + fiveSide + "__x1_" + x1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y1_" + y1 + "__y3_" + y3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__sideLength_" + sideLength + "__x5_" + x5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y2_" + y2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y3_" + y3 + "__y4_" + y4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y1_" + y1 + "__x2_" + x2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x2_" + x2 + "__y5_" + y5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y3_" + y3 + "__x3_" + x3 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__context_" + context + "__y2_" + y2 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__fiveSide_" + fiveSide + "__y5_" + y5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x4_" + x4 + "__x5_" + x5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__x4_" + x4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__sideLength_" + sideLength + "__y1_" + y1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__context_" + context + "__x1_" + x1 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__fiveSide_" + fiveSide + "__x4_" + x4 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y5_" + y5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__y1_" + y1 + "__x5_" + x5 + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__sideLength_" + sideLength + "__context_" + context + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), x5, "__sideLength_" + sideLength + "__fiveSide_" + fiveSide + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        drawCirclePrevCoords(context, radiusVerySmall);
        let new12x: number, new12y: number, new23x: number, new23y: number, new34x: number;
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        drawCirclePrevCoords(context, radiusMedium);
        let new34y: number, new45x: number, new45y: number, new51x: number, new51y: number;
        let left12x: number, left12y: number, right12x: number, right12y: number;
        let left23x: number, left23y: number, right23x: number, right23y: number;
        let left34x: number, left34y: number, right34x: number, right34y: number;
        let left45x: number, left45y: number, right45x: number, right45y: number;
        let left51x: number, left51y: number, right51x: number, right51y: number;
        let forHeight12x: number, forHeight12y: number, forHeight23x: number;
        let forHeight23y: number, forHeight34x: number, forHeight34y: number;
        let forHeight45x: number, forHeight45y: number, forHeight51x: number, forHeight51y: number;
        let smallerSideLength: number = fiveSide.countSmallerSideLength(sideLength);
        let middleSpace: number = fiveSide.countMiddleSpace(smallerSideLength);
        let ratioForLeft: number = smallerSideLength / sideLength;
        let ratioForRight: number = (smallerSideLength + middleSpace) / sideLength;
        let inverseRatioForLeft: number = 1 - ratioForLeft;
        let inverseRatioForRight: number = 1 - ratioForRight;
        let middleAreaHeight: number = fiveSide.countMiddleAreaHeight(smallerSideLength, middleSpace);
        let ratioMiddleHeight: number = middleAreaHeight / sideLength;
        let inverseRatioMiddleHeight: number = 1 - ratioMiddleHeight;
        let foundPoint: Point;
        if (iteration > 0) {
            // FIND FIRST POINT
            left12x = fiveSide.getPointOnLine(x1, x2, ratioForLeft, inverseRatioForLeft);
            left12y = fiveSide.getPointOnLine(y1, y2, ratioForLeft, inverseRatioForLeft);
            right12x = fiveSide.getPointOnLine(x1, x2, ratioForRight, inverseRatioForRight);
            right12y = fiveSide.getPointOnLine(y1, y2, ratioForRight, inverseRatioForRight);
            forHeight12x = fiveSide.getPointOnLine(x1, x2, ratioMiddleHeight, inverseRatioMiddleHeight);
            forHeight12y = fiveSide.getPointOnLine(y1, y2, ratioMiddleHeight, inverseRatioMiddleHeight);
            //drawCircle(context, left12x, left12y, 6);
            //drawCircle(context, right12x, right12y, 6);
            foundPoint = fiveSide.getNewPoint(x1, y1, x2, y2, forHeight12x, forHeight12y, x4, y4);
            new12x = foundPoint.x;
            new12y = foundPoint.y;
            //alert(new12y);
            // FIND SECOND POINT
            left23x = fiveSide.getPointOnLine(x2, x3, ratioForLeft, inverseRatioForLeft);
            left23y = fiveSide.getPointOnLine(y2, y3, ratioForLeft, inverseRatioForLeft);
            right23x = fiveSide.getPointOnLine(x2, x3, ratioForRight, inverseRatioForRight);
            right23y = fiveSide.getPointOnLine(y2, y3, ratioForRight, inverseRatioForRight);
            forHeight23x = fiveSide.getPointOnLine(x2, x3, ratioMiddleHeight, inverseRatioMiddleHeight);
            forHeight23y = fiveSide.getPointOnLine(y2, y3, ratioMiddleHeight, inverseRatioMiddleHeight);
            foundPoint = fiveSide.getNewPoint(x2, y2, x3, y3, forHeight23x, forHeight23y, x5, y5);
            new23x = foundPoint.x;
            new23y = foundPoint.y;
            // FIND THIRD POINT
            left34x = fiveSide.getPointOnLine(x3, x4, ratioForLeft, inverseRatioForLeft);
            left34y = fiveSide.getPointOnLine(y3, y4, ratioForLeft, inverseRatioForLeft);
            right34x = fiveSide.getPointOnLine(x3, x4, ratioForRight, inverseRatioForRight);
            right34y = fiveSide.getPointOnLine(y3, y4, ratioForRight, inverseRatioForRight);
            forHeight34x = fiveSide.getPointOnLine(x3, x4, ratioMiddleHeight, inverseRatioMiddleHeight);
            forHeight34y = fiveSide.getPointOnLine(y3, y4, ratioMiddleHeight, inverseRatioMiddleHeight);
            foundPoint = fiveSide.getNewPoint(x3, y3, x4, y4, forHeight34x, forHeight34y, x1, y1);
            new34x = foundPoint.x;
            new34y = foundPoint.y;
            // FIND FOURTH POINT
            left45x = fiveSide.getPointOnLine(x4, x5, ratioForLeft, inverseRatioForLeft);
            left45y = fiveSide.getPointOnLine(y4, y5, ratioForLeft, inverseRatioForLeft);
            right45x = fiveSide.getPointOnLine(x4, x5, ratioForRight, inverseRatioForRight);
            right45y = fiveSide.getPointOnLine(y4, y5, ratioForRight, inverseRatioForRight);
            forHeight45x = fiveSide.getPointOnLine(x4, x5, ratioMiddleHeight, inverseRatioMiddleHeight);
            forHeight45y = fiveSide.getPointOnLine(y4, y5, ratioMiddleHeight, inverseRatioMiddleHeight);
            foundPoint = fiveSide.getNewPoint(x4, y4, x5, y5, forHeight45x, forHeight45y, x2, y2);
            new45x = foundPoint.x;
            new45y = foundPoint.y;
            // FIND FIFTH POINT
            left51x = fiveSide.getPointOnLine(x5, x1, ratioForLeft, inverseRatioForLeft);
            left51y = fiveSide.getPointOnLine(y5, y1, ratioForLeft, inverseRatioForLeft);
            right51x = fiveSide.getPointOnLine(x5, x1, ratioForRight, inverseRatioForRight);
            right51y = fiveSide.getPointOnLine(y5, y1, ratioForRight, inverseRatioForRight);
            forHeight51x = fiveSide.getPointOnLine(x5, x1, ratioMiddleHeight, inverseRatioMiddleHeight);
            forHeight51y = fiveSide.getPointOnLine(y5, y1, ratioMiddleHeight, inverseRatioMiddleHeight);
            foundPoint = fiveSide.getNewPoint(x5, y5, x1, y1, forHeight51x, forHeight51y, x3, y3);
            new51x = foundPoint.x;
            new51y = foundPoint.y;
            // MAKE FINAL LINES
            if (iteration == 1) {
                drawLine(context, left12x, left12y, new12x, new12y, fiveSide.thickness);
                drawLine(context, right12x, right12y, new12x, new12y, fiveSide.thickness);
                drawLine(context, left23x, left23y, new23x, new23y, fiveSide.thickness);
                drawLine(context, right23x, right23y, new23x, new23y, fiveSide.thickness);
                drawLine(context, left34x, left34y, new34x, new34y, fiveSide.thickness);
                drawLine(context, right34x, right34y, new34x, new34y, fiveSide.thickness);
                drawLine(context, left45x, left45y, new45x, new45y, fiveSide.thickness);
                drawLine(context, right45x, right45y, new45x, new45y, fiveSide.thickness);
                drawLine(context, left51x, left51y, new51x, new51y, fiveSide.thickness);
                drawLine(context, right51x, right51y, new51x, new51y, fiveSide.thickness);
                drawLine(context, new12x, new12y, new23x, new23y, fiveSide.thickness);
                drawLine(context, new23x, new23y, new34x, new34y, fiveSide.thickness);
                drawLine(context, new34x, new34y, new45x, new45y, fiveSide.thickness);
                drawLine(context, new45x, new45y, new51x, new51y, fiveSide.thickness);
                drawLine(context, new51x, new51y, new12x, new12y, fiveSide.thickness);
                drawLine(context, x1, y1, x2, y2, fiveSide.thickness);
                drawLine(context, x2, y2, x3, y3, fiveSide.thickness);
                drawLine(context, x3, y3, x4, y4, fiveSide.thickness);
                drawLine(context, x4, y4, x5, y5, fiveSide.thickness);
                drawLine(context, x5, y5, x1, y1, fiveSide.thickness);
            }
            else {
                // LEFT UP
                this.getFiveSideShapes(iteration - 1, smallerSideLength, right12x, right12y, x2, y2, left23x, left23y, new23x, new23y, new12x, new12y, fiveSide, context);
                // LETF DOWN
                this.getFiveSideShapes(iteration - 1, smallerSideLength, right23x, right23y, x3, y3, left34x, left34y, new34x, new34y, new23x, new23y, fiveSide, context);
                // RIGHT DOWN
                this.getFiveSideShapes(iteration - 1, smallerSideLength, right34x, right34y, x4, y4, left45x, left45y, new45x, new45y, new34x, new34y, fiveSide, context);
                // RIGHT UP
                this.getFiveSideShapes(iteration - 1, smallerSideLength, right45x, right45y, x5, y5, left51x, left51y, new51x, new51y, new45x, new45y, fiveSide, context);
                // UP
                this.getFiveSideShapes(iteration - 1, smallerSideLength, right51x, right51y, x1, y1, left12x, left12y, new12x, new12y, new51x, new51y, fiveSide, context);
                // MIDDLE
                this.getFiveSideShapes(iteration - 1, smallerSideLength, new12x, new12y, new23x, new23y, new34x, new34y, new45x, new45y, new51x, new51y, fiveSide, context);
            }
        }
    }
    /*
      DRAWS EQUAL SIDE POLYGON ACCORDING PREPARED STRING
      
      @param {Context} context - context from Canvas to perform paint operations
      @param {Canvas} canvas - canvas where shape will be drawn (associated with context)
      @param {PolygonsMapInfo} polygonsMapInfo - equal side polygon mapping representation
      @param {String} ruleString - base/initial equal side polygon rule string (which should be extended in each iteration)
      @param {number} sideLength - initial length of side of koch shape
      @param {number} radius - radius for newly created points
    */
    drawShapeAndStoreVertices(context: CanvasRenderingContext2D, fiveSideMapInfo: FiveSideMapInfo, ruleString: string, angleForPoint: number, sideLength: number, radius: number, fiveSide: FiveSide) {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__fiveSide_" + fiveSide + "__angleForPoint_" + angleForPoint + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__fiveSideMapInfo_" + fiveSideMapInfo + "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__fiveSide_" + fiveSide + "__ruleString_" + ruleString + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__angleForPoint_" + angleForPoint + "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__ruleString_" + ruleString + "__fiveSideMapInfo_" + fiveSideMapInfo
            + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__ruleString_" + ruleString + "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__context_" + context + "__angleForPoint_" + angleForPoint + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__ruleString_" + ruleString + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__fiveSide_" + fiveSide + "__context_" + context + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__fiveSide_" + fiveSide + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__sideLength_" + sideLength + "__fiveSideMapInfo_" + fiveSideMapInfo
            + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__context_" + context + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__fiveSideMapInfo_" + fiveSideMapInfo + "__angleForPoint_" + angleForPoint +
            "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__context_" + context + "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__fiveSide_" + fiveSide + "__fiveSideMapInfo_" + fiveSideMapInfo + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__sideLength_" + sideLength + "__angleForPoint_" + angleForPoint + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__ruleString_" + ruleString + "__angleForPoint_" + angleForPoint + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__ruleString_" + ruleString + "__context_" + context + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__sideLength_" + sideLength + "__ruleString_" + ruleString + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__sideLength_" + sideLength + "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__angleForPoint_" + angleForPoint + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__sideLength_" + sideLength + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__sideLength_" + sideLength + "__context_" + context + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__context_" + context + "__fiveSideMapInfo_" + fiveSideMapInfo + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__sideLength_" + sideLength + "__fiveSide_" + fiveSide + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__fiveSide_" + fiveSide + "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), radius, "__fiveSideMapInfo_" + fiveSideMapInfo + "");
        let x1: number = fiveSideMapInfo.initialX; //initial x
        let y1: number = fiveSideMapInfo.initialY; //initial y
        let x2: number, y2: number;
        let angle: number = 0; //initial angle
        let point1: Circle = new Circle(x1, y1, radius);
        let point2: Circle;
        //let thickness = 1;
        fiveSide.vertices.push(new Point(x1, y1));
        //gameInfo.circles.push(point1);
        drawCircle(context, x1, y1, radius);
        for (let i = 0; i < ruleString.length; i++) {
            // NEW POINT 
            if (ruleString[i] == "R") {
                // EVALUATES NEW COORDINATES (x2, y2)
                x2 = x1 + Math.cos(degreeToRadians(angle)) * sideLength;
                y2 = y1 + Math.sin(degreeToRadians(angle)) * sideLength;
                point2 = new Circle(x2, y2, radius);
                // CREATES NEW POINT (x2, y2)
                fiveSide.vertices.push(new Point(x2, y2));
                drawCircle(context, x2, y2, radius);
                // CONNECTS NEW POINT WITH PREVIOUS ONE
                //if(x2 > 0.0 && y2 > 0.0 && x2 < canvasWidth && y2 < canvasHeight){
                //gameInfo.lines.push(new Line(point1, point2, thickness));
                //drawLine(context, x1, y1, x2, y2, thickness);
                //}
                //old points will be second and previous will be overwritten
                // SETS NEW POINT AS OLD ONE
                x1 = x2;
                y1 = y2;
                point1 = point2;
            }
            else if (ruleString[i] == "+") {
                angle = angle + angleForPoint;
                if (angle >= 360) {
                    angle = 0;
                }
            }
            else if (ruleString[i] == "-") {
                angle = angle - angleForPoint;
                if (angle <= -angleForPoint) {
                    angle = 360 - angleForPoint;
                }
            }
        }
    }
    /*
      INITIAL FUNCTION
      
      1. Obtains context from HTML element
      2. Prepares/initializes Five side fractal structure
      3. Draws five side fractal
    */
    drawFiveStar(conttext: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number) {
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), iterations, "__conttext_" + conttext + "__iterations_" + iterations + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), iterations, "__iterations_" + iterations + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), iterations, "__thickness_" + thickness + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), iterations, "__radius_" + radius + "__iterations_" + iterations + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), iterations, "__conttext_" + conttext + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), iterations, "__thickness_" + thickness + "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), iterations, "__thickness_" + thickness + "__conttext_" + conttext + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), iterations, "__thickness_" + thickness + "__iterations_" + iterations + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), iterations, "__radius_" + radius + "");
        /*
        export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
            let fiveSideFractal = new FiveSideFractal();
            fiveSideFractal.custom(context, radius, iterations, thickness, side);
        }
        */
        logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "classFunctionLogger", "fname": "" }), iterations, "__conttext_" + conttext + "__radius_" + radius + "");
        const fiveSideMapInfo = new FiveSideMapInfo(conttext, 5, 300, 300, 400);
        const fiveSide = new FiveSide(fiveSideMapInfo, iterations, thickness);
        this.drawShapeAndStoreVertices(conttext, fiveSideMapInfo, fiveSideMapInfo.ruleString, fiveSideMapInfo.angleForPoint, fiveSideMapInfo.sideLength, radius, fiveSide);
        this.getFiveSideShapes(fiveSide.iterations, fiveSide.sideLengthInitial, fiveSide.vertices[0].x, fiveSide.vertices[0].y, fiveSide.vertices[1].x, fiveSide.vertices[1].y, fiveSide.vertices[2].x, fiveSide.vertices[2].y, fiveSide.vertices[3].x, fiveSide.vertices[3].y, fiveSide.vertices[4].x, fiveSide.vertices[4].y, fiveSide, conttext);
    }
}
function drawAnkletModMain(conttextMain: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number): void {
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), conttextMain, "__iterations_" + iterations + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), conttextMain, "__thickness_" + thickness + "__conttextMain_" + conttextMain + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), conttextMain, "__thickness_" + thickness + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), conttextMain, "__radius_" + radius + "__iterations_" + iterations + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), conttextMain, "__thickness_" + thickness + "__radius_" + radius + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), conttextMain, "__thickness_" + thickness + "__iterations_" + iterations + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), conttextMain, "__radius_" + radius + "__conttextMain_" + conttextMain + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), conttextMain, "__radius_" + radius + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), conttextMain, "__conttextMain_" + conttextMain + "");
    /*
    export function ccustom(context: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number, side: number): void {
        let fiveSideFractal = new FiveSideFractal();
        fiveSideFractal.custom(context, radius, iterations, thickness, side);
    }
    */
    logData(JSON.stringify({ "fname": arguments.callee.toString().substring(0, arguments.callee.toString().split("(")[0].length), "logCategory": "functionLogger", "fname": "" }), conttextMain, "__iterations_" + iterations + "__conttextMain_" + conttextMain + "");
    let fiveSideFractal = new FiveSideFractal;
    fiveSideFractal.drawFiveStar(conttextMain, radius, iterations, thickness);
}
`