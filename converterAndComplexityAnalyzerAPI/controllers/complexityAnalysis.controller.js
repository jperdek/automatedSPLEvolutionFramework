
const escomplex = require("typhonjs-escomplex");
const escomplex2 = require("escomplex");
const { Complexity } = require("eslintcc");
const PATH_TO_TEMP_DIRECTORY = process.env.PATH_TO_TEMP_DIRECTORY || "./public/tmp";
const cryptoLib = require("crypto");
const express = require("express");
const router = express.Router();
const path = require("path");
const fs = require('fs');

router.use(
  express.urlencoded({
    extended: true
  })
);

router.use(express.json());

function processFileExchangeRequest(request, response, callback) {
	fs.readFile(request.query.url, "utf8", (error, loadedFile) => {
		if (error) {
			response.status(500);
			response.json({"response": "Error occured: " + error.toString()})
			return;
		}
		const dataToSave = callback(loadedFile, request, response);
		console.log(dataToSave);
		const uuid = cryptoLib.randomUUID();
		const savedFileLocation = PATH_TO_TEMP_DIRECTORY + "/convertLarge/" + uuid + ".txt";

		fs.unlink(request.query.url, (err) => {if (err) throw err;});
		fs.mkdir(path.basename(path.dirname(request.query.url)), { recursive: true }, (err) => {if (err) throw err;});
		fs.writeFile(savedFileLocation, dataToSave, function(error) {
			response.setHeader("Content-Type", "text/plain");
			if (error) {
				response.status(500);
				response.json({"response": "Error occured: " + error.toString()})
				return;
			}
			response.json({"response": "The file has been saved!", "location": savedFileLocation})
			response.status(200);
		}); 
	});
}

router.post("/analyzeTyphonJS", function (request, response) {
	const source = request.body;

	const report = escomplex.analyzeModule(source);
	response.set("Content-Type", "text/plain");
	response.send(report);
	response.status(200);
});


router.get("/analyzeTyphonJS", function (request, response) {
	processFileExchangeRequest(request, response, function(source, request, response) {
		const report = escomplex.analyzeModule(source);
		return JSON.stringify(report);
	});
});



router.post("/analyzeESLintCC", function (request, response) {
	const source = request.body;
	const complexity = new Complexity();
	complexity.lintFiles(source).then((report) => {
		response.set("Content-Type", "text/plain");
		response.send(report);
		response.status(200);
	}).catch((error) => {
		console.log(error);
	});
});

router.get("/analyzeESLintCC", function (request, response) {
	processFileExchangeRequest(request, response, function(source, request, response) {
		const complexity = new Complexity();
		return JSON.stringify(complexity.lintFiles(source));
	});
});


router.post("/analyzeEScomplex", function (request, response) {
	const source = request.body;
	const report = escomplex2.analyse(source);
	response.set("Content-Type", "text/plain");
	response.send(report);
	response.status(200);
});


router.get("/analyzeEScomplex", function (request, response) {
	processFileExchangeRequest(request, response, function(source, request, response) {
		const report = escomplex2.analyse(source);
		return JSON.stringify(report);;
	});
});


module.exports = router;