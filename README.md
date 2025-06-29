# Automated SPL Evolution Framework  
Automated SPL evolution framework - evolving variability on code level: Application on fractals  

if you use this work, please cite it as: 

@ARTICLE{10877839,
  author={Perdek, Jakub and Vranić, Valentino},
  journal={IEEE Access}, 
  title={Fully Automated Software Product Line Evolution with Diverse Artifacts}, 
  year={2025},
  volume={},
  number={},
  pages={1-1},
  keywords={Software product lines;Codes;Software;Fractals;Annotations;Quality assurance;Automation;Testing;Source coding;Software algorithms;aspect-oriented;configuration expressions;knowledge-driven;software product line evolution;variability modeling},
  doi={10.1109/ACCESS.2025.3539868}}

  or

  J. Perdek and V. Vranić, "Fully Automated Software Product Line Evolution with Diverse Artifacts," in IEEE Access, doi: [10.1109/ACCESS.2025.3539868](https://ieeexplore.ieee.org/document/10877839/)
  

## CAPABILITIES 
   - to focus and select a suitable variation point for a particular evolution activity  
   - choose from existing variation points beneficially and wisely (negative variability)  
   - to create code constructs from a particular context related to positive variability and ensure their execution   
   - to select variation points, then constructs on these points, and finally select some of these chosen constructs from some selected variation points to model features  
   - to restrict the number of final products
   - to produce various data representations and analyze them with various models (to enable comparison of various models under differently represented data)
	- graph data, relation data, semi-relational data (logs), raster data (screenshots), and vector data (captured commands into XML)
   - the possibility of inserting graph data from the entire dataset to Neo4J
   - application on various machine learning and deep learning models
         

## CONTENTS      

### Automated SPL Evolution Framework  
- in folder /EvolutionSPLFramework
- the framework for automated aspect-oriented knowledge-driven SPL evolution  
      
### AST Converter And Complexity Analysis API  
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



## LAUNCH STEPS - MICROSERVICE ARCHITECTURE  

We are proposing following microservice architecture:  

![Deployment Diagram](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/diagrams/deploymentDiagramDocker.png)  



### A) All in One using Docker-Compose    

```docker-compose build .```  

```docker-compose up```  


### B) The main Framework using Docker  
- the .jar from the application should be exported to run new version inside docker container  
- we recommend to use eclipse for this purpose   

![Exporting .jar in Eclipse](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/exportingJarInEclipse.png)


#### B.1) BUILD from dockerfile  

```docker build -t sampleapp:v1 . --no-cache```  


#### B.2) CREATING DATA VOLUMES to store evolved content  

```docker volume create evolution-volume```  
```docker volume create source-volume```  
```docker volume create dataset-products-volume```  


#### B.3) PREPARING VOLUMES with resources  

Creation of helper busybox container with previously created volumes:  
```docker run -v source-volume:/EvolutionSPLFramework -v evolution-volume:/evolution --name helper busybox true```  

Copying evolved data:  
```docker cp ./splsToMerge helper:/EvolutionSPLFramework```   
```docker cp ./resources helper:/EvolutionSPLFramework```  

Removing helper container:  
```docker rm helper```  


#### B.4) LAUNCHING NODE JS SERVER with API for AST conversion and complexity analysis  

```docker rm helper```  


#### B.5) RUNNING FRAMEWORK along with provided data  

```docker run --network="host" -v evolution-volume:/evolution -v source-volume:/EvolutionSPLFramework sampleapp:v1```  




## LAUNCH STEPS - STANDALONE FILES 

- 1.) Unpack the project ZIP to the chosen directory  
	```cd chosenDirectory```  



### a.) NODE JS CONVERTER AND COMPLEXITY EVALUATOR   

- 1a.) Switch to Converter and complexity evaluator folder    
	```cd astConverterAndComplexityEvaluator```  

- 2a.) install dependecies  
	- you should have npm already installed or install it using https://nodejs.org/en/download/  
	```npm i --save-dev```   

- 3a.) Substitute the following files from installed libraries (if skipped or set improperly, then Babel will have problems processing some decorators)  
	```
	cp ./changed defaults/BabelParser.js ./node_modules/@typhonjs/babel-parser/dist/BabelParser.js   
	cp ./changed defaults/eslint-patches.js ./node_modules/eslintcc/source/lib/eslint-patches.js   
	```
	or  
  	```
	copy "changedDefaults\BabelParser.js" "node_modules\@typhonjs\babel-parser\dist\BabelParser.js"  
	copy "changedDefaults\eslint-patches.js" "node_modules\eslintcc\source\lib\eslint-patches.js"  
	```

- 4a.) Launch server  
   - the default port is set to 5001  
	```npm start```  

- 5a.) Optionally test functionality with Postman  
	- load testAPI.postman_collection.json  
	- run each of 9 tests and verify outputs (each should return 200 status code)  



### b) FRAMEWORK FOR AUTOMATED SPL EVOLUTION  

 
- 1b.) Switch to the Automated Aspect-oriented Knowledge-driven SPL Evolution framework folder  
	```cd EvolutionSPLFramework``` 


- 2b.) Open project with Eclipse (steps will follow Eclipse, but it can be launched from the command line)  
	- open the project workspace inside the copied projects directory   
	- File -> open project from file system -> directory > EvolutionSPLFramework -> finish   
	- load used/associated libraries (if they are not)   
		- left click on project name in Package Explorer -> properties -> Java Build Path -> libraries -> ModulePath -> add External Jars -> lib (+select all) -> open -> apply and close   
	- add JRE library  
		- left click on project name in Package Explorer -> properties -> Java Build Path -> libraries -> ModulePath -> add Library -> JRE system library -> next -> finish  (I used Java 18.0.1.1)
	- set compiler to newer Java (18 and better)   
		- left click on project name in Package Explorer -> properties -> Java Compiler -> Compiler compliance level > 18 or more > apply > apply and close  

- 3b.) Set configuration variables to base and destination TypeScript scripts:  
	- In [./EvolutionSPLFramework\src\evolutionSimulation\productAssetsInitialization\SharedConfiguration.java] change:
 	  - line [public static final String PROJECT_PATH = "E://aspects/automatedSPLEvolutionFramework/EvolutionSPLFramework";] to your path ["file:///C:ABSOLUTE PATH TO YOUR/EvolutionSPLFramework"]  
  	  - line [public static final String PATH_TO_EVOLUTION_DIRECTORY = "E://aspects/automatedSPLEvolutionFramework/EvolutionSPLFramework/evolutionDirectory";] to your path ["file:///C:ABSOLUTE PATH TO YOUR/EvolutionSPLFramework/evolutionDirectory"]
  	  - line [public final static String CANTO_SCRIPT_RESOURCE_LOCATION = "E://aspects/automatedSPLEvolutionFramework/EvolutionSPLFramework/resources/canto/canto-0.15.js";] to your path ["file:///C:ABSOLUTE PATH TO YOUR/EvolutionSPLFramework/resources/canto/canto-0.15.js"]  

- 4b.) Configure evolution:
	- 4.1b.) Manual configuration
 		-left click on one ./EvolutionSPLFramework/src/splEvolutionCore/SPLEvolutionCore.java
   		-variable and decisions are described, some are still unimplemented
   - 4.2b.) Automated configuration
 		-create instance of ./EvolutionSPLFramework/src/evolutionSimulation/EvolutionCoreSettings.java and use it in evolution similarly as in ./EvolutionSPLFramework/src/evolutionSimulation/tests/EvolutionSimulationTest.java

- 5b.) Control and debug framework execution:  
	- manage to debug information printed in the console from ./EvolutionSPLFramework/src/evolutionSimulation/DebugInformation.java   
 	- manage CantoJS extension from ./EvolutionSPLFramework/src/evolutionSimulation/CanvasBasedApplicationConfiguration.java   
  	- manage the configuration of divisioning process and recognition of your own annotations and markers from ./EvolutionSPLFramework/src/divisioner/VariationPointDivisionConfiguration.java  
 
- 6b.) a) Run test evolution iteration:  
	- left click on one ./EvolutionSPLFramework/src/evolutionSimulation/tests/EvolutionSimulationTest.java -> run > EvolutionSimulationTest.java      

       b.) Run sample evolution pipeline:  
	- left click on one ./EvolutionSPLFramework/src/evolutionSimulation/orchestrationOfEvolutionIterations/test/CompletePositiveVariabilityEvolutionFocusedEvolutionTest.java -> run > CompletePositiveVariabilityEvolutionFocusedEvolutionTest.java

- 7b.) See results/SPL products/derived products in:  
	- open ./EvolutionSPLFramework/evolutionDirectory and launch one of the index.html files from SPL project directories inside
 	- if logging is set to true, then it will take a few seconds more  


#### b) CONFIGURING EVOLUTION PIPELINE  

- 1.1b.) Creating configuration for each evolution iteration  
	``` SPLNextEvolutionIterationCandidateSelectionStrategy evolution2IterationStrategy = new RandomCandidateSelection(); ```  
	``` SPLNextEvolutionIterationCandidateSelectionStrategy evolution3IterationStrategy = new RandomCandidateSelection(); ```  
	``` ... ```  

- 2.1b.) Instantiating each evolution iteration that will be inserted into sequence/pipeline  
  	``` EvolutionIteration evolutionIteration1 = new EvolutionIteration(); ```  
  	+ applying customized configurations:     
  	``` EvolutionIteration evolutionIteration2 = new EvolutionIteration(evolution2IterationStrategy); ```  
  	``` EvolutionIteration evolutionIteration3 = new EvolutionIteration(evolution3IterationStrategy); ```  

- 3.1b.) Preparing/instantiating object handling the execution of strategies in sequence  
  	``` EvolutionIterationsPipeline evolutionIterationsPipeline = new EvolutionIterationsPipeline(); ```  

- 4.1b.) Connecting iterations in the sequence as part of evolution pipeline  
  	``` evolutionIterationsPipeline.addEvolutionIterationToSequence(evolutionIteration1); ```  
  	``` evolutionIterationsPipeline.addEvolutionIterationToSequence(evolutionIteration2); ```  
  	``` evolutionIterationsPipeline.addEvolutionIterationToSequence(evolutionIteration3); ```  

- 5.1b.) Preparing assets planner to manage injection of the same functionality accross the evolution (configuration of how assets are planned)  
	- Strategies for planning assets are available in evolutionSimulation.orchestrationOfEvolutionIterations.assetsInIterationsManagment.strategies: 	
  		- new PlanAssetOnce() - particular exported assets can be injected only once - in the one evolution iteration for entire instantiated pipeline  
  		- new PlanAssetWithSkips(5) - particular exported assets can be injected multiple times, but after injection the following n iterations is forbidden to use previously injected asset (5 - in this case) for entire instantiated pipeline
     	- new NonRestrictiveAssetPlanning - particular exported assets can be injected without restriction - as default, no restrictions  
  	``` ExportAssetPlanner exportAssetPlanner = new AssetPlannerBaseStrategy(new PlanAssetOnce()); ```  

- 6.1b.) Preparing strategies for variability handling - (the presented example si focused on positive variability handling)  
  	``` CompletePositiveVariabilityFocusedEvolutionTest completeIyterativeDevelopment = new  CompletePositiveVariabilityFocusedEvolutionTest(); ```  
  	``` EvolutionConfiguration evolutionConfiguration = completeIyterativeDevelopment.prepareInitialConfiguration(); ```  
  
- 7.1b.) Running (variability handling/software product line evolution) pipeline  
  	``` evolutionIterationsPipeline.runEvolutionPipeline(evolutionConfiguration, exportAssetPlanner); ```  


### c) DATA EXTRACTION AND PROCESSING   

#### INSTALLATION (For Windows, but quite similar for Linux)  

- 1b.) Install python programming language from https://www.python.org/downloads/  

- 2b.) Switch to the Fractal Dynamic Data Collector folder  
	```cd FractalDynamicDataCollector```  

- 3b.) Create virtual environment and install dependencies stored in dependencies.txt  
	```python -m virtualenv venv```  
	```"./venv/Scripts/python.exe" -m pip install -r requirements.txt```  

- 4b.) Initialize Playwrigth  
	```"./venv/Scripts/python.exe" -m playwright install```


#### TAKING SCREENSHOTS  

- open ./FractalDynamicDataCollector/screenshoot_dataset.py and specify/change the arguments of the call on line 52:  
  - dataset_directory_path - the path pointing to the directory where derived SPLs or products are stored \[CHANGE IS REQUIRED DURING SETUP]  
  - final_location_path - the final location where taken screenshots are going to be stored, defaults to "./generated_dataset" \[CHANGE IS REQUIRED DURING SETUP]  
     - it can be placed to derived SPLs or products which are located in ../EvolutionSPLFramework/evolutionDirectory/evolNum1/conccustom  
     - the directory should contain directories with products, each with its own index.html file  
  - max_size - changes the size of the image to this tuple (width, height), default value is None - the screenshot is not resized  
  - canvas_id - the id of the element from which the screenshot should be taken, defaults to "#game"  
  - browser_timeout - the timeout to load the browser page and take screenshots (higher values are necessary where logging is enabled - factorials of data are logged in this case)

- run the following command:    
   ```"./venv/Scripts/python.exe" "./FractalDynamicDataCollector/screenshoot_dataset.py"```

  ![Taking screenshots](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/misc/screenshots/screenshoting.png)  


  
#### GETTING LOGS FROM TYPESCRIPT SCRIPTS USED IN HTML TEMPLATES  

- the script will load the html page and get logs from the instantiated product (TypeScript cannot be quickly processed by the js2py library)  
- open ./FractalDynamicDataCollector/dataset_variability_point_data_extractor_from_typescript_web_page.py and specify/change the arguments of the call on line 40:  
  - dataset_directory_path - the path pointing to the directory where derived SPLs or products are stored \[CHANGE IS REQUIRED DURING SETUP]  
     - it can be placed to derived SPLs or products which are located in ../EvolutionSPLFramework/evolutionDirectory/evolNum1/conccustom   
     - the directory should contain directories with products, each with its own index.html file   
  - final_location_path - the final location where extracted logs are going to be stored, defaults to "./generated_dataset_vp_data" \[CHANGE IS REQUIRED DURING SETUP]  
  - browser_timeout - the timeout to load the browser page and execute related scripts (higher values are necessary where logging is enabled - factorials of data are logged in this case)  
- run the following command:    
   ```"./venv/Scripts/python.exe" "./FractalDynamicDataCollector/dataset_variability_point_data_extractor_from_typescript_web_page.py"```


  
#### GETTING LOGS FROM JAVASCRIPT SCRIPTS (FASTER BUT WITHOUT TYPESCRIPT TRANSPILATION SUPPORT)   

- the script will load the main script using the js2py library  
- open ./FractalDynamicDataCollector/dataset_variability_point_data_extractor.py and specify/change the arguments of the call on line 38:   
  - dataset_directory_path - the path pointing to the directory where derived SPLs or products are stored \[CHANGE IS REQUIRED DURING SETUP]  
     - it can be placed to derived SPLs or products which are located in ../EvolutionSPLFramework/evolutionDirectory/evolNum1/conccustom    
     - the directory should contain directories with products, each with its own index.html file  
  - final_location_path - the final location where extracted logs are going to be stored, defaults to "./generated_dataset_vp_data" \[CHANGE IS REQUIRED DURING SETUP]     
  - is_wrapped - if the content script that should be executed is wrapped between backticks and stored in a variable as a string   
- run the following command:    
   ```"./venv/Scripts/python.exe" "./FractalDynamicDataCollector/dataset_variability_point_data_extractor.py"``


#### GETTING GRAPH DATA (FROM THE INTEGRATION OF EVOLVED SCRIPT IN HTML TEMPLATE)  

- all program dependencies and the main script itself is integrated into the HTML template  
- the script opens this HTML template with all required dependencies in the browser and executes the script to harvest data  
- if harvesting fails, then additional functionality with an adaptation of not recursive processing of the whole hierarchy with called instances is called and requires to be processed/merged after extraction back (also dobe be our script)  
- WARNING: taking images can take too long (more than 45 minutes)  
- open ./FractalDynamicDataCollector/graph_extractor_from_typescript_web_page.py and specify/change the arguments of the call on line 103:  
  - dataset_directory_path - the path pointing to the directory where derived SPLs or products are stored \[CHANGE IS REQUIRED DURING SETUP]  
     - it can be placed to derived SPLs or products which are located in ../EvolutionSPLFramework/evolutionDirectory/evolNum1/conccustom  
     - the directory should contain directories with products, each with its own index.html file  
  - final_location_path - the final location where extracted logs are going to be stored, defaults to "./generated_dataset_vp_graph_data" \[CHANGE IS REQUIRED DURING SETUP]  
  - skip_nodes_used_to_draw_image - other nodes are omitted if an image for a given node is created; otherwise, not (depends on the implementation, but if set to true, it can prevent the generation of entire graphs)  
  - image_settings - the instance with image settings to specify how objects for a particular node should be drawn  
     - disable_taking_images - disables taking images from each component (MAKE GETTING GRAPH DATA FASTER BUT WITHOUT IMAGES FROM PARTICULAR NODES)  
     - max_image_dimensions - tuple with maximal width and height of each extracted image - should cover canvas size due to proper drawing of each object   
     - result_dimensions - the final size of the generated/created image for a particular graph node - the final taken image is resized  
     - trim - if trimming of void parts should be applied  
     - color_to_strip - the color of empty pixels to trim the final image properly  
     - trim_treshold - the threshold for trimming  
     - line_thickness - the thickness of lines used during the drawing of the resulting image for a particular graph node    
  - browser_timeout - the timeout to load the browser page and execute related scripts (higher values are necessary where logging is enabled - factorials of data are logged in this case)  
- run the following command:    
   ```"./venv/Scripts/python.exe" "./FractalDynamicDataCollector/graph_extractor_from_typescript_web_page.py"```  

Getting stored image in Base64 for particular graph node from dataset:  
 ![Getting stored image in Base64 for particular graph node for dataset](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/misc/screenshots/gettingBase64Hash.png)   

Converting selected image in Base64 for particular graph node using [Base64 image converter](https://codebeautify.org/base64-to-image-converter):  
 ![Taking screenshots](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/misc/screenshots/convertingBase64HashToImage.png)    



# INTEGRATING DEFAULT KNOWLEDGE INTO KNOWLEDGE BASE  

All representations are integrated under the knowledge base under our default ontology for our Minimalistic and Automated Software Product Lines. Its schema is available at [fully-automated-spls-schema.ttl](https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl). We designed it using classes, subclasses, and properties from [rdf](http://www.w3.org/2000/01/rdf-schema#). The knowledge base is created, managed, and visualized directly inside the Neo4j graph database.  


The possible subjects, objects, and predicates of our default ontology are presented in the following Figure:  

![Schema of Our Fully Automated Software Product Lines Ontology](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/knowledgeBaseRelationsSchema.png)


## NEOSEMANTICS REPRESENTATION IN NEO4J GRAPH DATABASE  

The process of inserting entities is performed in batches, as shown in the following Figure, where each step is one batch:  

![Some updates during evolution](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/evolutionKnowledgeOneSplInEachIteration.png)


Namespace with semantics support is initialized as:  

![Namespace for Fully Automated Software Product Lines](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/semanticData/nodeProperties.png)


The first iteration and mapping of the respective created artifacts are shown in the following Figure:    

![First iteration and mapping of respective created artifacts](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/firstEvolvedSPLKnowledgeGraph.png)

where displayed entities look like the following where particular colors represent the semantics of a specific entity:     

### a) Code representation  

![Code representation in Neo4j](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/semanticData/codeRepr.png)

### b) Raster representation - screenshot   

![Raster representation in Neo4j](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/semanticData/screenshotRepr.png)

### c) Vector representation  

![Code representation in Neo4j](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/semanticData/svgRepr.png)

### d) Graph representation  

![Graph representation in Neo4j](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/semanticData/graphRepr.png)

### e) Positive variation point   

![Positive variation point](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/semanticData/positiveVPRepr.png)

### f) Negative variation point   

![Negative variation point](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/semanticData/negativeVPRepr.png)


## API  

### Initialization of knowledge base using neosemnatics in graph database  

GET /api/knowledge-base/init HTTP/1.1  
Host: localhost:5000  


### Clearing of knowledge base  

GET /api/knowledge-base/clear HTTP/1.1  
Host: localhost:5000  


### Inserting knowledge associated with initialization of new software product line evolution     

POST /api/knowledge-base/registerNewEvolution HTTP/1.1  
Host: localhost:5000  
Content-Type: application/json  

{  
    "evolution_id": "evol_1",  
    "initial_product_line_id": "prod_line_initial",  
    "evolved_script_path": "file:///path/to/evolved/script2.py",  
    "evolution_configuration_path": null,  
    "previous_evolution_id": null,  
    "previous_product_line_id": null  
}  


### Inserting knowledge after performed iteration of software product line evolution  

POST /api/knowledge-base/registerNewEvolutionIteration HTTP/1.1  
Host: localhost:5000  
Content-Type: application/json  

{  
    "evolution_id": "evol_AAA1",  
    "evolved_product_line_id": "prod_line_1",  
    "evolution_iteration": "1",  
    "code_path": "file:///path/to/code",  
    "screenshot_path": "file:///path/to/raster",  
    "vector_path": "file:///path/to/vector",  
    "json_graph_path": "file:///path/to/graph",  
    "evolved_script_path": "file:///path/to/evolved/script.py",  
    "variation_point_data_location": "file:///E:/aspects/automatedSPLEvolutionFramework/EvolutionSPLFramework/evolutionDirectory/evolNum1/conccustom/appcustomevolNum1_1_e103f174935c9aacustomevolNum1customevolNum1_VariationPointData.json",  
    "previous_product_line_id": null  
}  


### Insertion of test data

GET /api/knowledge-base/test?importVariationPoints=false HTTP/1.1  
Host: localhost:5000  
  
  
### EXPORTING AND IMPORTING TRIPLES TO KNOWLEDGE BASE  
  
### EXPORTING DATA FROM SEMANTIC BASE - API
  
POST /rdf/evolutionKnowledgeBase/cypher HTTP/1.1  
Host: localhost:7475  
Authorization: Basic bmVvNGo6ZmVhdHVyZU5lbzRq  
Content-Type: application/json  

{ "cypher" : "MATCH p = (n)-[a]-(b) RETURN p LIMIT 10000" , "format": "Turtle" }  
  
  
### IMPORTING DATA TO SEMANTIC BASE - API  

POST /api/knowledge-base/addTriples HTTP/1.1  
Host: localhost:5000  
Content-Type: text/plain  

@prefix faspls: <https://jakubperdek-26e24f.gitlab.io/fully-automated-spls-schema.ttl> .  
@prefix ns0: <https://jakubperdek-26e24f.gitlab.io/> .  

<evol_id_1> a faspls:Evolution .  
<evol_spl_id_1> a faspls:ProductLine .  
<evol_id_1> faspls:startFromSPL <evol_spl_id_1> .  
  
  
# MICROSERVICE ARCHITECTURE - SERVICES  
  
  
## MESSAGE QUEUE - SCALLING SOLUTION HORIZONTALY  

Message queue can be configured in MQ Admin available at http://localhost:15672/:   

![MQ Admin](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/rabbitMQLogin.png)


##  NEO4J DATABASE / KNOWLEDGE BASE - LOG IN  

Open http://localhost:7475/browser/ (even using Docker Compose) and as default credentials are used following:  

![Default credentials](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/knowledgeBaseNeo4jConnect.png)



# EXTRACTING ARTIFACTS FROM DOCKER COMPOSE ORCHESTRATION      

Run solution using Docker Compose according to steps specified above.  
In Docker Desktop you can view services as follows.  

1) Click on containers to view launched docker compose process:  

![Docker compose services](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/dockerDesktopIntegration.png)

2) Click on one of available services (such as dynamicDataCollector):  

![Docker compose services](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/services.png)

3) View shared volumes with artifacts along with source and evolved data:  

![Shared volumes with artifacts along with source and evolved data](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/sharedVolumes.png)  

4) Click on them and download them:  

![Downloading data from volumes](https://github.com/jperdek/automatedSPLEvolutionFramework/blob/master/documentation/evolvedFractalsDownload.png)
