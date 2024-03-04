# Automated SPL Evolution Framework  
Automated SPL evolution framework - evolving variability on code level: Application on fractals  

## CAPABILITIES 
   - to focus and select a suitable variation point for a particular evolution activity  
   - choose from existing variation points beneficially and wisely (negative variability)  
   - to create code constructs from a particular context related to positive variability and ensure their execution   
   - to select variation points, then constructs on these points, and finally select some of these chosen constructs from some selected variation points to model features  
   - to restrict the number of final products  
         

## CONTENTS      

### Automated SPL Evolution Framework  
- in folder /EvolutionSPLFramework
- the framework for automated aspect-oriented knowledge-driven SPL evolution  
      
### Converter And Complexity Analysis API  
- in folder /ConverterAndComplexityAnalyzerAPI  
- the API to convert JavaScript/TypeScript code into AST and back  
- The API to evaluate code complexity metrics, including Halstead measures and LOC   
    
### Fractal Dynamic Data Collector  
- in folder /fractalDynamicDataCollector  
- tools to extract various data representations of variability from code or process existing ones  
- supported data representations:  
  - taking screenshots  
  - getting logged values, groups of values  
  - creating a graph according to the sequence of instantiated variable code objects  

### Diagrams and Visualization  
- in folder /diagramsAndVisualization  
- diagrams covering the essential components of the Automated SPL Evolution Framework  
    - a simple overview of the flow with a flowchart  
    - activity diagram of the initialization and applicability of Automated SPL Evolution Framework to a particular base script and incorporation of functionality from other scripts  
    - flowcharts of variation point divisioner and extractor with a focus on configuration artifacts  
    - flowchart of the evolution core process  
    - activity diagram of configured decisions in the evolution core process  

      

## LAUNCH STEPS  

1.) Unpack the project ZIP to the chosen directory  
	cd chosenDirectory  



### a.) NODE JS CONVERTER AND COMPLEXITY EVALUATOR   

1a.) Switch to Converter and complexity evaluator folder    
	cd astConverterAndComplexityEvaluator   

2a.) install dependecies  
	-you should have npm already installed or install it using https://nodejs.org/en/download/  
	npm i --save-dev   

3a.) Substitute the following files from installed libraries (if skipped or set improperly, then Babel will have problems processing some decorators)  
	cp ./changed defaults/BabelParser.js ./node_modules/@typhonjs/babel-parser/dist/BabelParser.js   
	cp ./changed defaults/eslint-patches.js ./node_modules/eslintcc/source/lib/eslint-patches.js   

	or  

	copy "changedDefaults\BabelParser.js" "node_modules\@typhonjs\babel-parser\dist\BabelParser.js"  
	copy "changedDefaults\eslint-patches.js" "node_modules\eslintcc\source\lib\eslint-patches.js"  

4a.) Launch server  
- the default port is set to 5001  
	npm start  

5a.) Optionally test functionality with Postman  
	- load testAPI.postman_collection.json  
	- run each of 9 tests and verify outputs (each should return 200 status code)  


 ### b) FRAMEWORK FOR AUTOMATED SPL EVOLUTION  

 
1b.) Switch to Automated Aspect-oriented Knowledge-driven SPL Evolution framework folder  
	cd EvolutionSPLFramework 


2b.) Open project with Eclipse (steps will follow Eclipse, but it can be launched from the command line)  
	-open the project workspace inside the copied projects directory   
	-File -> open project from file system -> directory > EvolutionSPLFramework -> finish   
	-load used/associated libraries (if are not)   
		-left click on project name in Package Explorer -> properties -> Java Build Path -> libraries -> ModulePath -> add External Jars -> lib (+select all) -> open -> apply and close   
	-add JRE library  
		-left click on project name in Package Explorer -> properties -> Java Build Path -> libraries -> ModulePath -> add Library -> JRE system library -> next -> finish  (I used Java 18.0.1.1)
	-set compiler to newer Java (18 and better)   
		-left click on project name in Package Explorer -> properties -> Java Compiler -> Compiler compliance level > 18 or more > apply > apply and close  

2b.) Set configuration variables to base and destination TypeScript scripts:  
	in [./QualityChecker/src/scenarios/Scenario.java] change line [String pathToProjectTree = "file:///E:/aspects/canvasSPLforSPA/canvasSPLforSPA/src";] to your path ["file:///C:ABSOLUTE PATH TO YOUR/canvasSPLforSPA/canvasSPLforSPA/src"]  
	in [./QualityChecker/src/SPLComplexityEvaluator.java] change line [String pathToProjectTree = "file:///E:/aspects/canvasSPLforSPA/canvasSPLforSPA/src";] to your path ["file:///C:ABSOLUTE PATH TO YOUR/canvasSPLforSPA/canvasSPLforSPA/src"]  


3b.) Run all forms:  
	-left click on one ./QualityChecker/scenarions/TransformationForms.java -> run > TransformationForms.java    
	launch of specific FORM can be done commenting this lines except demanded one in 
		./QualityChecker/scenarions/TransformationForms.java [also to prevent file overwriting:  
		TransformationForms.evaluateForm1();  
		TransformationForms.evaluateForm2();  
		TransformationForms.evaluateForm3();  
		TransformationForms.evaluateForm4();  
		TransformationForms.evaluateForm5();  


4b.) See results in:  
	-evaluated differences in CSV located in: ./QualityChecker (such as generalAGGREGATETYPHONE.csv) 
	-generated files in two forms (BEFORE and AFTER in file name) in: ./QualityChecker/fileComparison [FOR THIS CASE THEY WILL OVERWRITTEN, but this is useful for Scenarios - this functionality is intended for  comparison]  


5b.) Run specific scenario (1-7):   
	-left click on one ./QualityChecker/scenarions/scenario1.java-scenario7.java -> run > scenario1.java    


6b.) See results in:  
	-generated files in two forms (AFTER AND BEFORE) in: ./QualityChecker/fileComparison  
	-evaluated differences in CSV located in ./QualityChecker (such as generalAGGREGATETYPHONE.csv)    


7b.) Erase or move created files to launch new scenario according to steps 5-6  
