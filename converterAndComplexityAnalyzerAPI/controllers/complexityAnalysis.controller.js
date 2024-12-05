
const escomplex = require("typhonjs-escomplex");
const escomplex2 = require("escomplex");
const { Complexity } = require("eslintcc");

const express = require("express");
const router = express.Router();

router.use(
  express.urlencoded({
    extended: true
  })
);

router.use(express.json());


router.post("/analyzeTyphonJS", function (request, response) {
	const source = request.body;

	const report = escomplex.analyzeModule(source);
	response.set("Content-Type", "text/plain");
	response.send(report);
	response.status(200);
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


router.post("/analyzeEScomplex", function (request, response) {
	const source = request.body;
	const report = escomplex2.analyse(source);
	response.set("Content-Type", "text/plain");
	response.send(report);
	response.status(200);
});

