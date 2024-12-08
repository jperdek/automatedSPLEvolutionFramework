
const express = require("express");
const app = express();
const cors = require("cors");
const bodyParser = require("body-parser");

const astConverterAPI = require("./controllers/astConverter.controller");
const complexityAnalysisAPI = require("./controllers/complexityAnalysis.controller");
//app.use(bodyParser.urlencoded({ extended: true }));
//app.use(bodyParser.json());
app.use(bodyParser.text({limit: '50mb'}));

app.engine("html", require("ejs").renderFile);
app.set("view engine", 'html');
app.use("/", astConverterAPI);
app.use("/", complexityAnalysisAPI);
app.use(cors());
app.use(express.static(__dirname + '/public/'));

/*
app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "YOUR-DOMAIN.TLD"); // update to match the domain you will make the request from
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  res.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE");
  res.header("Content-Type", "application/json");
  next();
});*/

app.listen(5001, "0.0.0.0");

console.log("Server up and running on port 5001");
