@variableDeclarationGlobal({"newFunctionParamGallery": "true"})
var newVariable: string = "Hallo";

@wholeBlockMethod({"outsideGalleryLost": "true"})
function a(param1: number, param2: number) {
	@wholeClass({"outsideGallery": "true"})
    class GG {
		@wholeClassMethod({"outsideGallery": "true"})
		callMe() {
			console.log("Called");
		}
	}
	new GG().callMe();
	@variableDeclarationLocal({"newFunctionParamGallery": "true"})
	let local = 5;
	@variableDeclarationGlobal({"newFunctionParamGallery": "true"})
    var globalOne = 6;
	@variableDeclarationLocal({"newFunctionParamGallery": "true"})
	let local2 = 5;
	@variableDeclarationGlobal({"newFunctionParamGallery": "true"})
	var globalOne2 = 6;
	return self;
}
console.log(a(null, null).local);

@wholeClass({"outsideGallery": "true"})
class BB {
	@variableDeclarationClass({"outsideGallery": "true"})
    nnnnn: number = 4;
	
	@wholeClassMethod({"outsideGallery": "true"})
    constructor(ccc: number, ddd: number) {

    }
	
	@wholeClassMethod({"outsideGallery": "true"})
    funnnc(eee: string): string {

    }
	
	@wholeClassMethod({"outsideGallery": "true"})
	inner(): void {
		@variableDeclarationLocal({"newFunctionParamGallery": "true"})
		let wwwww: number = 4;

		@wholeBlockMethod({"outsideGallery": "true"})
		function cfunc(pr: number, pr2: number) {
			console.log(pr + pr2);
			console.log("wwwww");
			console.log(wwwww);
	
			@wholeBlockMethod({"outsideGallery": "true"})
			function sssss() {
				console.log("Done");
			}
		}
		sssss();
		cfunc(wwwww, this.nnnnn);
	}
	@variableDeclarationClass({"outsideGallery": "true"})
	rrrr: number = 4; 
}

new BB().inner();