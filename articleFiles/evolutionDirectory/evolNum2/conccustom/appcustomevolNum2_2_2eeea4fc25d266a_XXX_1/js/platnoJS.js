var aaa1066cb26e8f = `var stackStub = [];
var initialGraphRoot = { "pointsTo": [] };
function pushData(insertedObject) {
    if (stackStub.length === 0) {
        if (initialGraphRoot["pointsTo"] === undefined) {
            initialGraphRoot["pointsTo"] = [];
        }
        initialGraphRoot["pointsTo"].push(insertedObject);
    }
    else {
        if (stackStub[stackStub.length - 1]["pointsTo"] === undefined) {
            stackStub[stackStub.length - 1]["pointsTo"] = [];
        }
        stackStub[stackStub.length - 1]["pointsTo"].push(insertedObject);
    }
    stackStub.
        push(insertedObject);
}
function popData() {
    if (stackStub
        .length > 0) {
        stackStub.pop();
    }
}
function createJSON(iteratedObject, flattened) {
    let identifier = 0;
    let createdObjectId = identifier;
    iteratedObject["mergeId"] = createdObjectId.toString();
    let stackForJSON = [];
    stackForJSON.push(iteratedObject);
    while (stackForJSON.length !== 0) {
        let processedObject = stackForJSON.pop();
        let newRecord = { "pointsTo": [], "mergeId": processedObject["mergeId"] };
        for (let processedObjectField in processedObject) {
            if (processedObject.hasOwnProperty(processedObjectField)) {
                let processedObjectFieldPart = processedObject[processedObjectField];
                if (processedObjectField === "pointsTo") {
                    for (let innerObjectIndex = 0; innerObjectIndex < processedObject[processedObjectField].length; innerObjectIndex++) {
                        let newChildInstance = processedObjectFieldPart[innerObjectIndex];
                        identifier = identifier + 1;
                        createdObjectId = identifier;
                        newChildInstance["mergeId"] = createdObjectId.toString();
                        newRecord["pointsTo"].push(createdObjectId.
                            toString());
                        stackForJSON.push(newChildInstance);
                    }
                }
                else if (typeof (name) === "object") {
                    newRecord[processedObjectField] = JSON.parse(JSON.stringify(processedObjectFieldPart));
                }
                else {
                    newRecord[processedObjectField] = processedObjectFieldPart;
                }
            }
        }
        flattened.push(newRecord);
    }
}
function reorderMergeIdsToArray(flattenedArray) {
    let helperVariable;
    for (let i = 0; i < flattenedArray.length; i++) {
        do {
            helperVariable = flattenedArray[i];
            mergedIndex = parseInt(helperVariable["mergeId"]);
            flattenedArray[i] = flattenedArray[mergedIndex];
            flattenedArray[mergedIndex] = helperVariable;
        } while (mergedIndex != i);
    }
}
function harvestStackData() {
    let flattened = [];
    createJSON(initialGraphRoot, flattened);
    reorderMergeIdsToArray(flattened);
    return flattened;
}
export function drawCirclePrevCoords(context: CanvasRenderingContext2D, radius: number) {
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
function propagateCoordinatesThreePositions(context: CanvasRenderingContext2D) {
    let x1 = context.currentX;
    let y1 = context.currentY;
    let x2 = context.currentX2;
    let y2 = context.currentY2;
    let x3 = context.currentX3;
    let y3 = context.currentY3;
    let x4 = context.currentX4;
    let y4 = context.currentY4;
    let threshold = 50;
    if (Math.abs(Math.abs(x1) - Math.abs(x3)) > threshold || Math.abs(Math.abs(y1) - Math.abs(y3)) > threshold ||
        Math.abs(Math.abs(y1) - Math.abs(y4)) > threshold || Math.abs(Math.abs(x1) - Math.abs(x4)) > threshold ||
        Math.abs(Math.abs(y2) - Math.abs(y3)) > threshold || Math.abs(Math.abs(x2) - Math.abs(x3)) > threshold) {
        context.currentX4 = undefined;
        context.currentY4 = undefined;
        context.currentX3 = undefined;
        context.currentY3 = undefined;
        context.currentX2 = undefined;
        context.currentY2 = undefined;
        context.currentX = undefined;
        context.currentY = undefined;
    }
    else {
        context.currentX4 = x3;
        context.currentY4 = y3;
        context.currentX3 = x2;
        context.currentY3 = y2;
        context.currentX2 = x1;
        context.currentY2 = y1;
    }
}
function saveState(context: CanvasRenderingContext2D, unused: number) {
    context.savedX = context.currentX;
    context.savedY = context.currentY;
    context.savedX2 = context.currentX2;
    context.savedY2 = context.currentY2;
    context.savedX3 = context.currentX3;
    context.savedY3 = context.currentY3;
    context.savedX4 = context.currentX4;
    context.savedY4 = context.currentY4;
    context.savedX5 = context.currentX5;
    context.savedY5 = context.currentY5;
    context.savedX6 = context.currentX6;
    context.savedY6 = context.currentY6;
    context.savedX7 = context.currentX7;
    context.savedY7 = context.currentY7;
}
function loadState(context: CanvasRenderingContext2D, unused: number) {
    context.currentX = context.savedX;
    context.currentY = context.savedY;
    context.currentX2 = context.savedX2;
    context.currentY2 = context.savedY2;
    context.currentX3 = context.savedX3;
    context.currentY3 = context.savedY3;
    context.currentX4 = context.savedX4;
    context.currentY4 = context.savedY4;
    context.currentX5 = context.savedX5;
    context.currentY5 = context.savedY5;
    context.currentX6 = context.savedX6;
    context.currentY6 = context.savedY6;
    context.currentX7 = context.savedX7;
    context.currentY7 = context.savedY7;
}
function propagateCoordinatesSixPositions(context: CanvasRenderingContext2D) {
    let x1 = context.currentX;
    let y1 = context.currentY;
    let x2 = context.currentX2;
    let y2 = context.currentY2;
    let x3 = context.currentX3;
    let y3 = context.currentY3;
    let x4 = context.currentX4;
    let y4 = context.currentY4;
    let x5 = context.currentX5;
    let y5 = context.currentY5;
    let x6 = context.currentX6;
    let y6 = context.currentY6;
    let x7 = context.currentX7;
    let y7 = context.currentY7;
    let threshold = 60;
    if (Math.abs(Math.abs(x1) - Math.abs(x3)) > threshold || Math.abs(Math.abs(y1) - Math.abs(y3)) > threshold ||
        Math.abs(Math.abs(y1) - Math.abs(y4)) > threshold || Math.abs(Math.abs(x1) - Math.abs(x4)) > threshold ||
        Math.abs(Math.abs(y2) - Math.abs(y3)) > threshold || Math.abs(Math.abs(x2) - Math.abs(x3)) > threshold) {
        context.currentX7 = undefined;
        context.currentY7 = undefined;
        context.currentX6 = undefined;
        context.currentY6 = undefined;
        context.currentX5 = undefined;
        context.currentY5 = undefined;
        context.currentX4 = undefined;
        context.currentY4 = undefined;
        context.currentX3 = undefined;
        context.currentY3 = undefined;
        context.currentX2 = undefined;
        context.currentY2 = undefined;
        context.currentX = undefined;
        context.currentY = undefined;
    }
    else {
        context.currentX7 = x6;
        context.currentY7 = y6;
        context.currentX6 = x5;
        context.currentY6 = y5;
        context.currentX5 = x4;
        context.currentY5 = y4;
        context.currentX4 = x3;
        context.currentY4 = y3;
        context.currentX3 = x2;
        context.currentY3 = y2;
        context.currentX2 = x1;
        context.currentY2 = y1;
        context.currentX = x7;
        context.currentY = y7;
    }
}
export function drawCover2(context: CanvasRenderingContext2D, radius: number) {
    if (context.currentX !== undefined && context.currentY !== undefined) {
        propagateCoordinatesThreePositions(context);
    }
    saveState(context);
    context.beginPath();
    context.moveTo(x1, y1 - radius);
    context.lineTo(x2, y2 - radius);
    context.lineTo(x2 + radius, y2);
    context.lineTo(x2, y2);
    context.lineTo(x1, y1);
    context.closePath();
    context.fill();
    loadState(context);
}
export function drawCover7(context: CanvasRenderingContext2D) {
    if (context.currentX !== undefined && context.currentY !== undefined) {
        propagateCoordinatesSixPositions(context);
    }
    console.log("1c " + context.currentX + " <----> " + context.currentY);
    console.log("1c " + context.currentX2 + " <----> " + context.currentY2);
    console.log("1c " + context.currentX3 + " <----> " + context.currentY3);
    console.log("1c " + context.currentX4 + " <----> " + context.currentY4);
    console.log("------------------------------------------------------");
    if (context.currentX !== undefined && context.currentY !== undefined &&
        context.currentX3 !== undefined && context.currentY3 !== undefined) {
        let x1 = context.currentX;
        let y1 = context.currentY;
        let x2 = context.currentX3;
        let y2 = context.currentY3;
        let x3 = context.currentX4;
        let y3 = context.currentY4;
        let x4 = context.currentX5;
        let y4 = context.currentY5;
        let x5 = context.currentX6;
        let y5 = context.currentY6;
        saveState(context);
        context.beginPath();
        context.strokeStyle = "rgba(250, 0, 0, .9)";
        context.moveTo(y1, x1);
        context.lineTo(y2, x2);
        context.lineTo(y3, x3);
        context.lineTo(y4, x4);
        context.lineTo(y5, x5);
        context.lineTo(y1, x1);
        context.closePath();
        context.stroke();
        loadState(context);
    }
}
export function drawCover5(context: CanvasRenderingContext2D) {
    if (context.currentX !== undefined && context.currentY !== undefined) {
        propagateCoordinatesSixPositions(context);
    }
    console.log("1b " + context.currentX + " <----> " + context.currentY);
    console.log("1b " + context.currentX2 + " <----> " + context.currentY2);
    console.log("1b " + context.currentX3 + " <----> " + context.currentY3);
    if (context.currentX !== undefined && context.currentY !== undefined &&
        context.currentX2 !== undefined && context.currentY2 !== undefined &&
        context.currentX3 !== undefined && context.currentY3 !== undefined) {
        let x1 = context.currentX;
        let y1 = context.currentY;
        let x2 = context.currentX3;
        let y2 = context.currentY3;
        let x3 = context.currentX4;
        let y3 = context.currentY4;
        let x4 = context.currentX5;
        let y4 = context.currentY5;
        let x5 = context.currentX6;
        let y5 = context.currentY6;
        saveState(context);
        context.beginPath();
        context.moveTo(x1, y1);
        context.lineTo(x2, y2);
        context.lineTo(x3, y3);
        context.lineTo(x4, y4);
        context.lineTo(x5, y5);
        context.lineTo(x1, y1);
        context.strokeStyle = "rgba(50, 50, 250, .9)";
        context.closePath();
        context.stroke();
        loadState(context);
    }
}
export function drawCover3(context: CanvasRenderingContext2D, x4: number, y4: number) {
    if (context.currentX !== undefined && context.currentY !== undefined) {
        propagateCoordinatesThreePositions(context);
    }
    if (context.currentX !== undefined && context.currentY !== undefined &&
        context.currentX2 !== undefined && context.currentY2 !== undefined &&
        context.currentX3 !== undefined && context.currentY3 !== undefined) {
        let x1 = x4;
        let y1 = y4;
        let x2 = context.currentX;
        let y2 = context.currentY;
        let x3 = context.currentX4;
        let y3 = context.currentY4;
        saveState(context);
        context.beginPath();
        context.strokeStyle = "rgba(0, 0, 250, .9)";
        context.moveTo(x1, y1);
        context.lineTo(x2, y2);
        context.lineTo(x3, y3);
        context.lineTo(x1, y1);
        context.closePath();
        context.stroke();
        loadState(context);
    }
}
export function drawRectFromPrev(context: CanvasRenderingContext2D) {
    if (context.currentX !== undefined && context.currentY !== undefined) {
        propagateCoordinatesSixPositions(context);
    }
    if (context.currentX !== undefined && context.currentY !== undefined &&
        context.currentX2 !== undefined && context.currentY2 !== undefined &&
        context.currentX3 !== undefined && context.currentY3 !== undefined) {
        let x1 = context.currentX;
        let y1 = context.currentY;
        let x2 = context.currentX3;
        let y2 = context.currentY3;
        let x3 = context.currentX4;
        let y3 = context.currentY4;
        let x4 = context.currentX5;
        let y4 = context.currentY5;
        let x5 = context.currentX6;
        let y5 = context.currentY6;
        saveState(context);
        context.beginPath();
        context.strokeStyle = "rgba(50, 100, 250, .9)";
        context.moveTo(x1, y1);
        context.lineTo(x2, y2);
        context.lineTo(x3, y3);
        context.lineTo(x4, y4);
        context.lineTo(x1, y1);
        context.closePath();
        context.stroke();
        loadState(context);
    }
}
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
class Line {
    startPoint: number;
    endPoint: number;
    thickness: number;
    constructor(startPoint: number, endPoint: number, thickness: number) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.thickness = thickness;
    }
}
function drawLine(context: CanvasRenderingContext2D, x1: number, y1: number, x2: number, y2: number, thickness: number) {
    context.beginPath();
    context.moveTo(x1, y1);
    context.lineTo(x2, y2);
    context.lineWidth = thickness;
    context.strokeStyle = "#cfc";
    context.stroke();
}
function drawCircle(context: CanvasRenderingContext2D, cordX: number, cordY: number, radius: number) {
    context.fillStyle = "rgba(200, 200, 100, .9)";
    context.beginPath();
    context.arc(cordX, cordY, radius, 0, Math.PI * 2, true);
    context.closePath();
    context.fill();
}
class Vector {
    x: number;
    y: number;
    constructor(x: number, y: number) {
        this.x = x;
        this.y = y;
    }
}
function degreeToRadians(angle: number): number {
    return Math.PI * angle / 180;
}
class FiveSideMapInfo {
    initialX: number;
    initialY: number;
    sideLength: number;
    dimension: number;
    angleForPoint: number;
    ruleString: string;
    radius: number = 5;
    centerPoint: Circle;
    createRule(dimension: number): string {
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
        this.initialX = initialX;
        this.initialY = initialY;
        this.sideLength = initialSideLength;
        this.dimension = dimension;
        this.angleForPoint = 360 / dimension;
        this.ruleString = this.createRule(dimension);
    }
    setFinalSideLength(sideLength: number) { this.sideLength = sideLength; }
    findCenterPoint(polygonClass: FiveSideMapInfo, context: CanvasRenderingContext2D, initialX: number, initialY: number, sideLength: number, angleForPoint: number, dimension: number, radius: number): Circle {
        let nextX: number = initialX + Math.cos(degreeToRadians(0)) * sideLength;
        let nextY: number = initialY + Math.sin(degreeToRadians(0)) * sideLength;
        let x1: number = nextX;
        let y1: number = nextY;
        let x2: number = nextX;
        let y2: number = nextY;
        let middleX: number, middleY: number;
        let angle: number = angleForPoint;
        for (let i = 1; i < Math.round(dimension / 2); i++) {
            x2 = x1 + Math.cos(degreeToRadians(angle)) * sideLength;
            y2 = y1 + Math.sin(degreeToRadians(angle)) * sideLength;
            angle = angle + angleForPoint;
            x1 = x2;
            y1 = y2;
        }
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
            polygonClass.radius = sideLength / (2 * Math.sin(angleOfVectors));
            drawCircle(context, initialX + sideLength / 2, initialY + polygonClass.radius, polygonClass.radius);
            return new Circle(initialX + sideLength / 2, initialY + polygonClass.radius, radius);
        }
    }
}
class Point {
    x: number;
    y: number;
    constructor(x: number, y: number) {
        this.x = x;
        this.y = y;
    }
}
@DecoratorTypesService.wholeClass({ "fiveSide": "true" })
class FiveSide {
    vertices: any[];
    iterations: number;
    sideLengthInitial: number;
    thickness: number;
    constructor(fiveSideMapInfo: FiveSideMapInfo, iterations: number, thickness: number) {
        this.vertices = [];
        this.iterations = iterations;
        this.sideLengthInitial = fiveSideMapInfo.sideLength;
        this.thickness = thickness;
    }
    getNewPoint(x1: number, y1: number, x2: number, y2: number, vectorHeightX: number, vectorHeightY: number, oppositeX: number, oppositeY: number): Point {
        let normalPoint1x: number, normalPoint1y: number, normalPoint2x: number, normalPoint2y: number;
        let vectorMiddleHeightX: number, vectorMiddleHeightY: number;
        let middle12x: number, middle12y: number;
        middle12x = this.getMiddlePoint(x1, x2);
        middle12y = this.getMiddlePoint(y1, y2);
        vectorMiddleHeightX = vectorHeightX - x1;
        vectorMiddleHeightY = vectorHeightY - y1;
        normalPoint1x = middle12x + (-vectorMiddleHeightY);
        normalPoint1y = middle12y + (vectorMiddleHeightX);
        normalPoint2x = middle12x + (vectorMiddleHeightY);
        normalPoint2y = middle12y + (-vectorMiddleHeightX);
        if (this.countDistance(normalPoint1x, normalPoint1y, oppositeX, oppositeY) <
            this.countDistance(normalPoint2x, normalPoint2y, oppositeX, oppositeY)) {
            return new Point(normalPoint1x, normalPoint1y);
        }
        return new Point(normalPoint2x, normalPoint2y);
    }
    countMiddleAreaHeight(smallerSideLength: number, middleSpace: number): number {
        return Math.round(Math.sqrt(Math.pow(smallerSideLength, 2) - Math.pow(middleSpace / 2, 2)));
    }
    countMiddleSpace(smallerSideLength: number): number {
        return ((smallerSideLength * Math.sin(degreeToRadians(36))) / Math.sin(degreeToRadians(72)));
    }
    countSmallerSideLength(sideLength: number): number {
        return (Math.sin(degreeToRadians(72)) * sideLength / (2 * Math.sin(degreeToRadians(72)) + Math.sin(degreeToRadians(36))));
    }
    getPointOnLine(x1: number, x2: number, ratio: number, inverseRatio: number): number {
        return x1 * inverseRatio + x2 * ratio;
    }
    countDistance(x1: number, y1: number, x2: number, y2: number): number {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    getMiddlePoint(coordStart: number, coordEnd: number): number {
        return coordStart + (coordEnd - coordStart) / 2;
    }
}
@DecoratorTypesService.wholeClass({ "fiveSide": "true" })
export class FiveSideFractal {
    getFiveSideShapes(iteration: number, sideLength: number, x1: number, y1: number, x2: number, y2: number, x3: number, y3: number, x4: number, y4: number, x5: number, y5: number, fiveSide: FiveSide, context: CanvasRenderingContext2D): void {
        propagateCoordinatesThreePositions(context);
        drawCover5(context);
        let new12x: number, new12y: number, new23x: number, new23y: number, new34x: number;
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
            left12x = fiveSide.getPointOnLine(x1, x2, ratioForLeft, inverseRatioForLeft);
            left12y = fiveSide.getPointOnLine(y1, y2, ratioForLeft, inverseRatioForLeft);
            right12x = fiveSide.getPointOnLine(x1, x2, ratioForRight, inverseRatioForRight);
            right12y = fiveSide.getPointOnLine(y1, y2, ratioForRight, inverseRatioForRight);
            forHeight12x = fiveSide.getPointOnLine(x1, x2, ratioMiddleHeight, inverseRatioMiddleHeight);
            forHeight12y = fiveSide.getPointOnLine(y1, y2, ratioMiddleHeight, inverseRatioMiddleHeight);
            foundPoint = fiveSide.getNewPoint(x1, y1, x2, y2, forHeight12x, forHeight12y, x4, y4);
            new12x = foundPoint.x;
            new12y = foundPoint.y;
            left23x = fiveSide.getPointOnLine(x2, x3, ratioForLeft, inverseRatioForLeft);
            left23y = fiveSide.getPointOnLine(y2, y3, ratioForLeft, inverseRatioForLeft);
            right23x = fiveSide.getPointOnLine(x2, x3, ratioForRight, inverseRatioForRight);
            right23y = fiveSide.getPointOnLine(y2, y3, ratioForRight, inverseRatioForRight);
            forHeight23x = fiveSide.getPointOnLine(x2, x3, ratioMiddleHeight, inverseRatioMiddleHeight);
            forHeight23y = fiveSide.getPointOnLine(y2, y3, ratioMiddleHeight, inverseRatioMiddleHeight);
            foundPoint = fiveSide.getNewPoint(x2, y2, x3, y3, forHeight23x, forHeight23y, x5, y5);
            new23x = foundPoint.x;
            new23y = foundPoint.y;
            left34x = fiveSide.getPointOnLine(x3, x4, ratioForLeft, inverseRatioForLeft);
            left34y = fiveSide.getPointOnLine(y3, y4, ratioForLeft, inverseRatioForLeft);
            right34x = fiveSide.getPointOnLine(x3, x4, ratioForRight, inverseRatioForRight);
            right34y = fiveSide.getPointOnLine(y3, y4, ratioForRight, inverseRatioForRight);
            forHeight34x = fiveSide.getPointOnLine(x3, x4, ratioMiddleHeight, inverseRatioMiddleHeight);
            forHeight34y = fiveSide.getPointOnLine(y3, y4, ratioMiddleHeight, inverseRatioMiddleHeight);
            foundPoint = fiveSide.getNewPoint(x3, y3, x4, y4, forHeight34x, forHeight34y, x1, y1);
            new34x = foundPoint.x;
            new34y = foundPoint.y;
            left45x = fiveSide.getPointOnLine(x4, x5, ratioForLeft, inverseRatioForLeft);
            left45y = fiveSide.getPointOnLine(y4, y5, ratioForLeft, inverseRatioForLeft);
            right45x = fiveSide.getPointOnLine(x4, x5, ratioForRight, inverseRatioForRight);
            right45y = fiveSide.getPointOnLine(y4, y5, ratioForRight, inverseRatioForRight);
            forHeight45x = fiveSide.getPointOnLine(x4, x5, ratioMiddleHeight, inverseRatioMiddleHeight);
            forHeight45y = fiveSide.getPointOnLine(y4, y5, ratioMiddleHeight, inverseRatioMiddleHeight);
            foundPoint = fiveSide.getNewPoint(x4, y4, x5, y5, forHeight45x, forHeight45y, x2, y2);
            new45x = foundPoint.x;
            new45y = foundPoint.y;
            left51x = fiveSide.getPointOnLine(x5, x1, ratioForLeft, inverseRatioForLeft);
            left51y = fiveSide.getPointOnLine(y5, y1, ratioForLeft, inverseRatioForLeft);
            right51x = fiveSide.getPointOnLine(x5, x1, ratioForRight, inverseRatioForRight);
            right51y = fiveSide.getPointOnLine(y5, y1, ratioForRight, inverseRatioForRight);
            forHeight51x = fiveSide.getPointOnLine(x5, x1, ratioMiddleHeight, inverseRatioMiddleHeight);
            forHeight51y = fiveSide.getPointOnLine(y5, y1, ratioMiddleHeight, inverseRatioMiddleHeight);
            foundPoint = fiveSide.getNewPoint(x5, y5, x1, y1, forHeight51x, forHeight51y, x3, y3);
            new51x = foundPoint.x;
            new51y = foundPoint.y;
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
                this.getFiveSideShapes(iteration - 1, smallerSideLength, right12x, right12y, x2, y2, left23x, left23y, new23x, new23y, new12x, new12y, fiveSide, context);
                this.getFiveSideShapes(iteration - 1, smallerSideLength, right23x, right23y, x3, y3, left34x, left34y, new34x, new34y, new23x, new23y, fiveSide, context);
                this.getFiveSideShapes(iteration - 1, smallerSideLength, right34x, right34y, x4, y4, left45x, left45y, new45x, new45y, new34x, new34y, fiveSide, context);
                this.getFiveSideShapes(iteration - 1, smallerSideLength, right45x, right45y, x5, y5, left51x, left51y, new51x, new51y, new45x, new45y, fiveSide, context);
                this.getFiveSideShapes(iteration - 1, smallerSideLength, right51x, right51y, x1, y1, left12x, left12y, new12x, new12y, new51x, new51y, fiveSide, context);
                this.getFiveSideShapes(iteration - 1, smallerSideLength, new12x, new12y, new23x, new23y, new34x, new34y, new45x, new45y, new51x, new51y, fiveSide, context);
            }
        }
    }
    drawShapeAndStoreVertices(context: CanvasRenderingContext2D, fiveSideMapInfo: FiveSideMapInfo, ruleString: string, angleForPoint: number, sideLength: number, radius: number, fiveSide: FiveSide) {
        let x1: number = fiveSideMapInfo.initialX;
        let y1: number = fiveSideMapInfo.initialY;
        let x2: number, y2: number;
        let angle: number = 0;
        let point1: Circle = new Circle(x1, y1, radius);
        let point2: Circle;
        fiveSide.vertices.push(new Point(x1, y1));
        drawCircle(context, x1, y1, radius);
        for (let i = 0; i < ruleString.length; i++) {
            if (ruleString[i] == "R") {
                x2 = x1 + Math.cos(degreeToRadians(angle)) * sideLength;
                y2 = y1 + Math.sin(degreeToRadians(angle)) * sideLength;
                point2 = new Circle(x2, y2, radius);
                fiveSide.vertices.push(new Point(x2, y2));
                drawCircle(context, x2, y2, radius);
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
    drawFiveStar(conttext: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number) {
        const fiveSideMapInfo = new FiveSideMapInfo(conttext, 5, 300, 300, 400);
        const fiveSide = new FiveSide(fiveSideMapInfo, iterations, thickness);
        this.drawShapeAndStoreVertices(conttext, fiveSideMapInfo, fiveSideMapInfo.ruleString, fiveSideMapInfo.angleForPoint, fiveSideMapInfo.sideLength, radius, fiveSide);
        this.getFiveSideShapes(fiveSide.iterations, fiveSide.sideLengthInitial, fiveSide.vertices[0].x, fiveSide.vertices[0].y, fiveSide.vertices[1].x, fiveSide.vertices[1].y, fiveSide.vertices[2].x, fiveSide.vertices[2].y, fiveSide.vertices[3].x, fiveSide.vertices[3].y, fiveSide.vertices[4].x, fiveSide.vertices[4].y, fiveSide, conttext);
    }
}
function drawAnkletModMain(conttextMain: CanvasRenderingContext2D, radius: number, iterations: number, thickness: number): void {
    let fiveSideFractal = new FiveSideFractal;
    fiveSideFractal.drawFiveStar(conttextMain, radius, iterations, thickness);
}
`