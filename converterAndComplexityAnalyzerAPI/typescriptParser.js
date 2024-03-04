
const express = require('express');
const app = express();
const cors = require('cors');
const escomplex = require('typhonjs-escomplex');
const escomplex2 = require('escomplex');
const { Complexity } = require('eslintcc');


const bodyParser = require('body-parser');
//app.use(bodyParser.urlencoded({ extended: true }));
//app.use(bodyParser.json());
app.use(bodyParser.text({limit: '50mb'}));

const ts = require('typescript');
const fs = require('fs');

app.post('/convert', function (request, response) {
	const ast = ts.createSourceFile("x.ts", request.body, ts.ScriptTarget.Latest);
	const printer = ts.createPrinter({ newLine: ts.NewLineKind.LineFeed });
	const code = printer.printNode(ts.EmitHint.Unspecified, JSON.parse(JSON.stringify(ast)), JSON.parse(JSON.stringify(ast)));
	response.json({ast});
	response.setHeader('Content-Type', 'application/json');
	response.status(200);
});

app.post('/convertBack', function (request, response) {
	console.log(request.body);
	const ast = JSON.parse(request.body);
	const printer = ts.createPrinter({ newLine: ts.NewLineKind.LineFeed });
	const code = printer.printNode(ts.EmitHint.Unspecified, ast, ast);
	response.set('Content-Type', 'text/html');
	response.send(code);
	response.status(200);
});



app.post('/analyzeTyphonJS', function (request, response) {
	const source = request.body;
	console.log(source);
	const report = escomplex.analyzeModule(source);
	response.set('Content-Type', 'text/html');
	console.log(report);
	response.send(report);
	response.status(200);
});


app.post('/analyzeESLintCC', function (request, response) {
	const source = request.body;
	const complexity = new Complexity();
	complexity.lintFiles(source).then((report) => {
		response.set('Content-Type', 'text/html');
		console.log(report);
		response.send(report);
		response.status(200);
	}).catch((error) => {
		console.log(error);
	});
});


app.post('/analyzeEScomplex', function (request, response) {
	const source = request.body;
	const report = escomplex2.analyse(source);
	response.set('Content-Type', 'text/html');
	console.log(report);
	response.send(report);
	response.status(200);
});


app.engine('html', require('ejs').renderFile);
app.set('view engine', 'html');

app.use(cors());
//


app.use(function(req, res, next) {
  res.header('Access-Control-Allow-Origin', 'YOUR-DOMAIN.TLD'); // update to match the domain you will make the request from
  res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');
  res.header('Access-Control-Allow-Methods','GET,PUT,POST,DELETE');
  res.header('Content-Type', 'application/json');
  next();
});

app.listen(5001);

console.log("Server up and running on port 5001");