@variableDeclarationGlobal({"newFunctionParamGallery": "true"})
var newVariable = "Hallo";

@wholeBlockMethod({"outsideGalleryLost": "true"})
function a(param1, param2) {
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
    nnnnn = 4;
	
	@wholeClassMethod({"outsideGallery": "true"})
    constructor(ccc: number, ddd: number) {

    }
	
	@wholeClassMethod({"outsideGallery": "true"})
    funnnc(eee): string {

    }
	
	@wholeClassMethod({"outsideGallery": "true"})
	inner(): void {
		@variableDeclarationLocal({"newFunctionParamGallery": "true"})
		let wwwww = 4;

		@wholeBlockMethod({"outsideGallery": "true"})
		function cfunc(pr, pr2) {
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
	rrrr = 4; 
}

new BB().inner();