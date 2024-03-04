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

 
1b.) Switch to the Automated Aspect-oriented Knowledge-driven SPL Evolution framework folder  
	cd EvolutionSPLFramework 


2b.) Open project with Eclipse (steps will follow Eclipse, but it can be launched from the command line)  
	-open the project workspace inside the copied projects directory   
	-File -> open project from file system -> directory > EvolutionSPLFramework -> finish   
	-load used/associated libraries (if they are not)   
		-left click on project name in Package Explorer -> properties -> Java Build Path -> libraries -> ModulePath -> add External Jars -> lib (+select all) -> open -> apply and close   
	-add JRE library  
		-left click on project name in Package Explorer -> properties -> Java Build Path -> libraries -> ModulePath -> add Library -> JRE system library -> next -> finish  (I used Java 18.0.1.1)
	-set compiler to newer Java (18 and better)   
		-left click on project name in Package Explorer -> properties -> Java Compiler -> Compiler compliance level > 18 or more > apply > apply and close  

2b.) Set configuration variables to base and destination TypeScript scripts:  
	In [./EvolutionSPLFramework\src\evolutionSimulation\productAssetsInitialization.java] change:
 	- line [public static final String PROJECT_PATH = "E://aspects/automatedSPLEvolutionFramework/EvolutionSPLFramework";] to your path ["file:///C:ABSOLUTE PATH TO YOUR/EvolutionSPLFramework"]  
  	- line [public static final String PATH_TO_EVOLUTION_DIRECTORY = "E://aspects/automatedSPLEvolutionFramework/EvolutionSPLFramework/evolutionDirectory";] to your path ["file:///C:ABSOLUTE PATH TO YOUR/EvolutionSPLFramework/evolutionDirectory"]
  	- line [public final static String CANTO_SCRIPT_RESOURCE_LOCATION = "E://aspects/automatedSPLEvolutionFramework/EvolutionSPLFramework/resources/canto/canto-0.15.js";] to your path ["file:///C:ABSOLUTE PATH TO YOUR/EvolutionSPLFramework/resources/canto/canto-0.15.js"]  

3b.) Configure evolution:
	3.1b.) Manual configuration
 		-left click on one ./EvolutionSPLFramework/src/splEvolutionCore/SPLEvolutionCore.java
   		-variable and decisions are described, some are still unimplemented
   	
    	3.2b.) Automated configuration
 		-create instance of ./EvolutionSPLFramework/src/evolutionSimulation/EvolutionCoreSettings.java and use it in evolution similarly as in ./EvolutionSPLFramework/src/evolutionSimulation/tests/EvolutionSimulationTest.java

4b.) Control and debug framework execution:  
	- manage to debug information printed in the console from ./EvolutionSPLFramework/src/evolutionSimulation/DebugInformation.java   
 	- manage CantoJS extension from ./EvolutionSPLFramework/src/evolutionSimulation/CanvasBasedApplicationConfiguration.java   
  	- manage the configuration of divisioning process and recognition of your own annotations and markers from ./EvolutionSPLFramework/src/divisioner/VariationPointDivisionConfiguration.java  
 
5b.) Run test evolution iteration:  
	- left click on one ./EvolutionSPLFramework/src/evolutionSimulation/tests/EvolutionSimulationTest.java -> run > EvolutionSimulationTest.java      

6b.) See results/SPL products/derived products in:  
	- open ./EvolutionSPLFramework/evolutionDirectory and launch one of the index.html files from SPL project directories inside
 	- if logging is set to true, then it will take a few seconds more  

