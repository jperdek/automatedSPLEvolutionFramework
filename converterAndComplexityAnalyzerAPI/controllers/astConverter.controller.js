
const ts = require('typescript');
const fs = require('fs');
const commentCleaner = require("comment-cleaner")
const LARGE_FILES_TMP_DIR = process.env.LARGE_FILES_TMP_DIR || "./public/tmp";
const cryptoLib = require("crypto");
const express = require("express");
const bodyParser = require("body-parser");

const path = require("path");
const router = express.Router();

router.use(
  express.urlencoded({
    extended: true
  })
);

router.use(express.json());

router.post("/cleanComments", function (request, response) {
	const cleanedCode = commentCleaner.clean("ts", request.body);
	response.set("Content-Type", "text/plain");
	response.send(cleanedCode);
	response.status(200);
});


router.post("/convert", function (request, response) {
	const ast = ts.createSourceFile("x.ts", request.body, ts.ScriptTarget.Latest);
	const printer = ts.createPrinter({ newLine: ts.NewLineKind.LineFeed });
	const code = printer.printNode(ts.EmitHint.Unspecified, JSON.parse(JSON.stringify(ast)), JSON.parse(JSON.stringify(ast)));
	response.json({ast});
	response.setHeader("Content-Type", "application/json");
	response.status(200);
});


router.post("/convertLarge", function (request, response) {
	const ast = ts.createSourceFile("x.ts", request.body, ts.ScriptTarget.Latest);
	const printer = ts.createPrinter({ newLine: ts.NewLineKind.LineFeed });
	const code = printer.printNode(ts.EmitHint.Unspecified, JSON.parse(JSON.stringify(ast)), JSON.parse(JSON.stringify(ast)));
	const uuid = cryptoLib.randomUUID();
	
	const savedFileLocation = LARGE_FILES_TMP_DIR + "/convertLarge/" + uuid + ".txt";
	fs.writeFile(savedFileLocation, JSON.stringify({ast}), function(error) {
		response.setHeader("Content-Type", "application/json");
		if(error) {
			response.status(500);
			response.json({"response": "Error occured: " + error.toString()})
			return;
		}
		response.json({"response": "The file has been saved!", "location": savedFileLocation})
		response.status(200);
	}); 
});

router.get("/convertLarge", function (request, response) {
	fs.readFile(request.query.url, "utf8", (error, loadedFile) => {
		if (error) {
			response.status(500);
			response.json({"response": "Error occured: " + error.toString()})
			return;
		}
		
		const cleanedCode = commentCleaner.clean("ts", loadedFile).replaceAll("//\s*@ts[^\n]+\n", "");;
		const ast = ts.createSourceFile("x.ts", cleanedCode, ts.ScriptTarget.Latest);
		const printer = ts.createPrinter({ newLine: ts.NewLineKind.LineFeed });

		const code = printer.printNode(ts.EmitHint.Unspecified, ast, ast);
		const uuid = cryptoLib.randomUUID();
		
		const savedFileLocation = LARGE_FILES_TMP_DIR + "/convertLarge/" + uuid + ".txt";
		fs.writeFile(savedFileLocation, JSON.stringify({ast}, (k, v) => { return ""; }), function(error) {
			response.setHeader("Content-Type", "application/json");
			if(error) {
				response.status(500);
				response.json({"response": "Error occured: " + error.toString()})
				return;
			}
			response.json({"response": "The file has been saved!", "location": savedFileLocation})
			response.status(200);
		}); 
	});
});

router.post("/convertBack", function (request, response) {
	const ast = JSON.parse(request.body);
	const printer = ts.createPrinter({ newLine: ts.NewLineKind.LineFeed });
	const code = printer.printNode(ts.EmitHint.Unspecified, ast, ast);
	response.set("Content-Type", "text/plain");
	response.send(code);
	response.status(200);
});


router.post("/convertLargeBack", function (request, response) {
	const ast = JSON.parse(request.body);
	const printer = ts.createPrinter({ newLine: ts.NewLineKind.LineFeed });
	const code = printer.printNode(ts.EmitHint.Unspecified, ast, ast);
	
	const uuid = cryptoLib.randomUUID();
	const savedFileLocation = LARGE_FILES_TMP_DIR + "/convertLarge/" + uuid + ".txt";
	fs.writeFile(savedFileLocation, code, function(error) {
		response.setHeader("Content-Type", "text/plain");
		if(error) {
			response.status(500);
			response.json({"response": "Error occured: " + error.toString()})
			return;
		}
		response.json({"response": "The file has been saved!", "location": savedFileLocation})
		response.status(200);
	}); 
});

router.get("/convertLargeBack", function (request, response) {
	fs.readFile(request.query.url, "utf8", (error, loadedFile) => {
		if (error) {
			response.status(500);
			response.json({"response": "Error occured: " + error.toString()})
			return;
		}
		const ast = request.query.is_file? JSON.parse(loadedFile)["ast"] : JSON.parse(loadedFile);
		const printer = ts.createPrinter({ newLine: ts.NewLineKind.LineFeed });
		const code = printer.printNode(ts.EmitHint.Unspecified, ast, ast);
		
		const uuid = cryptoLib.randomUUID();
		const savedFileLocation = LARGE_FILES_TMP_DIR + "/convertLarge/" + uuid + ".txt";
		fs.writeFile(savedFileLocation, code, function(error) {
			response.setHeader("Content-Type", "text/plain");
			if(error) {
				response.status(500);
				response.json({"response": "Error occured: " + error.toString()})
				return;
			}
			response.json({"response": "The file has been saved!", "location": savedFileLocation})
			response.status(200);
		}); 
	});
});

router.post("/transpile", bodyParser.text({type: "*/*"}), function (request, response) {
	const source = request.body;
	const report = ts.transpile(source.replaceAll("export", "window.export"));
	response.set("Content-Type", "text/plain");
	response.send(report);
	response.status(200);
});


router.get("/cleanLargeFiles", function (request, response) {
	const directory = "./public/tmp/convertLarge"
	fs.readdir(directory, (error, files) => {
	  if (error) {
		  throw error;
	  }
	  for (const file of files) {
		fs.unlink(path.join(directory, file), (error) => {
		  if (error) {
			  throw error;
		  }
		});
	  }
	});
	response.set("Content-Type", "text/plain");
	response.send({"response": "Done!"});
	response.status(200);
});

module.exports = router;