# Automated SPL Evolution Framework  
Automated SPL evolution framework - evolving variability on code level: Application on fractals  

## Capabilities  
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



a.) NODE JS CONVERTER AND COMPLEXITY EVALUATOR   

1a.) Switch to complexity evaluator folder    
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
 
