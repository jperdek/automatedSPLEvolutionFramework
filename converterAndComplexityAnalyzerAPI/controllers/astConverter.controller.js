const stringify = require('safe-json-stringify');
const ts = require('typescript');
const fs = require('fs');
const commentCleaner = require("comment-cleaner")
const PATH_TO_TEMP_DIRECTORY = process.env.PATH_TO_TEMP_DIRECTORY || "./public/tmp";
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

function processFileExchangeRequest(request, response, callback) {
	fs.readFile(request.query.url, "utf8", (error, loadedFile) => {
		if (error) {
			response.status(500);
			response.json({"response": "Error occured: " + error.toString()})
			return;
		}
		const dataToSave = callback(loadedFile, request, response);
		
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

router.get("/cleanComments", function (request, response) {
	processFileExchangeRequest(request, response, function(source, request, response) {
		const cleanedCode = commentCleaner.clean("ts", source);
		return cleanedCode;
	});
});


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
	const uuid = cryptoLib.randomUUID();
	
	const savedFileLocation = PATH_TO_TEMP_DIRECTORY + "/convertLarge/" + uuid + ".txt";
	fs.mkdir(path.basename(path.dirname(savedFileLocation)), { recursive: true }, (err) => {if (err) throw err;});
	//const buffer = Buffer.from(stringify({ast}));
	fs.writeFile(savedFileLocation, stringify({ast}), function(error) {
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

function censor(censor) {
  var i = 0;
  
  return function(key, value) {
    if(i !== 0 && typeof(censor) === 'object' && typeof(value) == 'object' && censor == value) 
      return '[Circular]'; 
    
    if(i >= 29) // seems to be a harded maximum of 30 serialized objects?
      return '[Unknown]';
    
    ++i; // so we know we aren't using the original object anymore
    
    return value;  
  }
}
function handleCircular(key, value) {
    if (typeof value === 'object' && value !== null) {
        if (value instanceof Array) {
            return value.map(
                (item, index) => 
                (index === value.length - 1 ? 
                    'circular reference' : item));
        }
        return { ...value, circular: 'circular reference' };
    }
    return value;
}

router.get("/convertLarge", function (request, response) {
	fs.readFile(request.query.url, "utf8", (error, loadedFile) => {
		if (error) {
			response.status(500);
			response.json({"response": "Error occured: " + error.toString()})
			return;
		}

		const cleanedCode = commentCleaner.clean("ts", loadedFile).replaceAll("//\s*@ts[^\n]+\n", "");;
		const ast = ts.createSourceFile("x.ts", cleanedCode, ts.ScriptTarget.Latest);
		const uuid = cryptoLib.randomUUID();
		
		const savedFileLocation = PATH_TO_TEMP_DIRECTORY + "/convertLarge/" + uuid + ".txt";
		
		//fs.unlink(request.query.url, (err) => {if (err) throw err;});
		fs.mkdir(path.basename(path.dirname(savedFileLocation)), { recursive: true }, (err) => {if (err) throw err;});
		fs.writeFile(savedFileLocation, stringify({ast}), function(error) {
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
	const savedFileLocation = PATH_TO_TEMP_DIRECTORY + "/convertLarge/" + uuid + ".txt";
	fs.mkdir(path.basename(path.dirname(savedFileLocation)), { recursive: true }, (err) => {if (err) throw err;});
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
		const ast = request.query.is_file? JSON.parse(loadedFile)["ast"] : JSON.parse(loadedFile.replace("\r\n", ""));
		const printer = ts.createPrinter({ newLine: ts.NewLineKind.LineFeed });
		const code = printer.printNode(ts.EmitHint.Unspecified, ast, ast);
		
		const uuid = cryptoLib.randomUUID();
		const savedFileLocation = PATH_TO_TEMP_DIRECTORY + "/convertLarge/" + uuid + ".txt";

		//fs.unlink(request.query.url, (err) => {if (err) throw err;});
		fs.mkdir(path.basename(path.dirname(request.query.url)), { recursive: true }, (err) => {if (err) throw err;});
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