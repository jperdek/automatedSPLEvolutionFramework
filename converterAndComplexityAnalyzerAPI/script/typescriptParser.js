
const code = `
 function foo(value: int) { }
`;
//https://codesandbox.io/s/hardcore-fire-4nwo7?file=/src/index.ts:0-205/
//https://stackoverflow.com/questions/65213952/how-to-generate-code-from-ast-typescript-parser

var ts = require('typescript');

const fs = require('fs');

fs.readFile('E://aspects/puzzle/dist/puzzleToPlay/main.js', 'utf8', (err, data) => {
	if (err) {
		console.error(err);
		return;
	}

	const ast = ts.createSourceFile("x.ts", code, ts.ScriptTarget.Latest);
	
	fs.writeFile('./AST.json', JSON.stringify(ast), err => {
	  if (err) {
		console.error(err);
	  }
	  // file written successfully
	});
	console.log(JSON.stringify({ast}));
});
