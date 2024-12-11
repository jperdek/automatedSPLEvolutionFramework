package evolutionSimulation.orchestrationOfEvolutionIterations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;

import astFileProcessor.ASTLoader;
import asynchronousPublisher.evolvedSPLPublishing.EvolvedSPLPublisher;
import evolutionSimulation.EvolutionConfiguration;
import evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.imageContentQualityAssignment.VariationPointsDataAggregations;
import positiveVariabilityManagement.ProjectCopier;
import splEvolutionCore.SPLEvolutionCore;


/**
 * Loads and parses the information about actual SPL candididates for the new evolution iteration
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class SPLCandidateLoadingMechanism {

	/**
	 * The mapping of the file with variation points to the file name
	 */
	private Map<String, String> vpDataFileNameToProjectPath;
	
	
	/**
	 * Instantiates the SPL candidate loading mechanism
	 */
	public SPLCandidateLoadingMechanism() {
		this.vpDataFileNameToProjectPath = new HashMap<String, String>();
	}
	
	/**
	 * Returns the list of SPL candidate names from each previous population member
	 * 
	 * @return the list of SPL candidate point names from each previous population member
	 */
	public List<String> getListOfCandidateSPLFileNamesFromEachPopulationMember() { 
		return new ArrayList<String>(this.vpDataFileNameToProjectPath.keySet());
	}

	/**
	 * Returns path to SPL project named according to candidate name
	 * 
	 * @param candidateName - get path to SPL project named according to candidate name
	 * @return the path to SPL project named according to candidate name
	 */
	public String getCandidatePath(String candidateName) { return this.vpDataFileNameToProjectPath.get(candidateName); }
	
	/**
	 * Loads and parse SPL candidates
	 * -TO DO: if variation points data JSON file  is not available for them then this functionality should be incorporated
	 * 
	 * @param previousEvolutionDirectoryPath - the path with SPLs from the previous evolution process
	 * @param variationPointsDataAggregations - the aggregations of variation point data
	 * @throws IOException
	 */
	public void loadAndParseSPLCandidates(String previousEvolutionDirectoryPath,
			VariationPointsDataAggregations variationPointsDataAggregations, EvolutionConfiguration evolutionConfiguration) throws IOException {
		System.out.println("Loading candidates for new evolution iteration from directory: " + previousEvolutionDirectoryPath);
		 File dir = new File(previousEvolutionDirectoryPath);
		 File[] directoryListing = dir.listFiles();
		 String vpDataAbsolutePath, vpDataGrandChildAbsolutePath;
		 String directoryWithSameName;
		 String newFilePath;
		 String childName, grandChildName;
		 String affectedProjectPath;
		 int projectCounter = 0;
		 boolean containsVersions = false;
		 if (directoryListing != null) {
		    for (File childFile: directoryListing) {
		    	vpDataAbsolutePath = childFile.getAbsolutePath();
		    	if (childFile.isDirectory()) {
		    		for (File grandchildFile: childFile.listFiles()) {
		    			vpDataGrandChildAbsolutePath = grandchildFile.getAbsolutePath();
		    			if (!grandchildFile.exists()) { System.out.println("File: " + vpDataGrandChildAbsolutePath + " does not exist. Skipping..."); continue; }
		    			if (!grandchildFile.isDirectory() && vpDataGrandChildAbsolutePath.contains(SPLEvolutionCore.VARIATION_POINTS_DATA_NAME_ID_ENDING)) {
		    				containsVersions = true;
		    				childName = childFile.getName();
		    				grandChildName = grandchildFile.getName();
		    				directoryWithSameName = "";
		    				affectedProjectPath = "";
		    				for (File grandchildFile2: childFile.listFiles()) {
		    					if (grandchildFile2.isDirectory() && grandChildName.contains(grandchildFile2.getName())) {
		    						directoryWithSameName = grandchildFile2.getName();
		    						affectedProjectPath = grandchildFile2.getAbsolutePath();
		    						break;
		    					}
		    				}
		    				// MOVING ALL WHOLE PROJECTS INTO FLATTENED FORM
		    				if (!affectedProjectPath.equals("")) {
		    					projectCounter = projectCounter + 1;
			    				newFilePath = dir.getAbsolutePath() + "/" + childName + "_XXX_" + projectCounter + "_XXX_" + SPLEvolutionCore.VARIATION_POINTS_DATA_NAME_ID_ENDING;
			    				Files.move(grandchildFile.toPath(), Path.of(newFilePath), StandardCopyOption.REPLACE_EXISTING);
			    				
		    					ProjectCopier.copyExistingProject(affectedProjectPath, vpDataAbsolutePath + "_XXX_" + projectCounter, true, true);
		    					
		    					String targetDestinationPath = vpDataAbsolutePath + "_XXX_" + projectCounter;
		    					String projectId = childName;
		    					EvolvedSPLPublisher.publishMessageAboutEvolvedSPL(evolutionConfiguration, projectId, targetDestinationPath, true);
		    				} else {
		    					System.out.println("Cannot move data. Variation point data are missing... Removing data.");
		    					Files.delete(grandchildFile.toPath());
		    				}
		    			}
		    		}
		    	}
		    }
		    if (containsVersions) {
		    	directoryListing = dir.listFiles();
		    	for (File childFile: directoryListing) {
		    		if (childFile.listFiles() != null) {
				    	for (File grandchildFileSame: childFile.listFiles()) {
			    			vpDataGrandChildAbsolutePath = grandchildFileSame.getAbsolutePath();
			    
		    				if (!grandchildFileSame.isDirectory() && vpDataGrandChildAbsolutePath.endsWith(".json") 
	    						&& !vpDataGrandChildAbsolutePath.contains(SPLEvolutionCore.VARIATION_POINTS_DATA_NAME_ID_ENDING)) { //COPY REMAINING NON-DIRECTORY FILES
			    				childName = childFile.getName();
			    				grandChildName = grandchildFileSame.getName();
			    				directoryWithSameName = "";
			    				for (File grandchildFile2Same: childFile.listFiles()) {
			    					if (grandchildFile2Same.isDirectory() && grandChildName.contains(grandchildFile2Same.getName())) {
			    						directoryWithSameName = grandchildFile2Same.getName();
			    						break;
			    					}
			    				}
			    				newFilePath = dir.getAbsolutePath() + "/" + childName + grandChildName.replace(directoryWithSameName, "");
			    				Files.move(grandchildFileSame.toPath(), Path.of(newFilePath), StandardCopyOption.REPLACE_EXISTING);
			    				
			    			}
						}
				    	if (childFile.listFiles().length == 0) { Files.delete(childFile.toPath()); }
		    		}
		    	}
		    } 
		    directoryListing = dir.listFiles();
	    	for (File childFile: directoryListing) {
		    	vpDataAbsolutePath = childFile.getAbsolutePath();
		    	if (!childFile.isDirectory() && vpDataAbsolutePath.contains(SPLEvolutionCore.VARIATION_POINTS_DATA_NAME_ID_ENDING)) {
		    		this.loadAndParseSPLCandidate(childFile, childFile.getAbsolutePath(), variationPointsDataAggregations, directoryListing);
		    	}
		    }
		 } else {
			 throw new IOException("The previous evolution path does not lead to directory!");
		 }
	}
	
	
	/**
	 *  Loads and parse SPL candidate
	 *  
	 * @param childFile
	 * @param variationPointsDataAggregations - the aggregations of variation point data
	 * @param directoryListing
	 * @param isParent - true if requested directory is the parent
	 * @param parentDirectoryName
	 * @throws IOException
	 */
	private void loadAndParseSPLCandidate(File childFile, String vpDataAbsolutePath, VariationPointsDataAggregations variationPointsDataAggregations, 
			File[] directoryListing) throws IOException {
		String fileContent, fileName, vpDataAbsolutePath2, vpDataAbsolutePath2ToCompare;
		String insertedPath;
		String pathAndVersion[];
		JSONArray variationPointsData;
		vpDataAbsolutePath = vpDataAbsolutePath.replace("\\", "/");
    	System.out.println("Processing the candidate file: " + vpDataAbsolutePath);
    	if (!childFile.isDirectory() && vpDataAbsolutePath.contains(SPLEvolutionCore.VARIATION_POINTS_DATA_NAME_ID_ENDING)) {
    		System.out.println("Found variation point data.");
    		fileContent = String.join(" ", Files.readAllLines(Path.of(vpDataAbsolutePath)));
    		variationPointsData = ASTLoader.loadJSONArrayFromString(fileContent);
    	  	fileName = vpDataAbsolutePath.split(SPLEvolutionCore.VARIATION_POINTS_DATA_NAME_ID_ENDING)[0];
    	  	fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
    	  	variationPointsDataAggregations.processVariationPointsData(variationPointsData, fileName);
    	  	for (File childFile2: directoryListing) {
		    	vpDataAbsolutePath2 = childFile2.getAbsolutePath().replace("\\", "/");
		    	pathAndVersion = vpDataAbsolutePath2.split("_XXX_");
		    	vpDataAbsolutePath2ToCompare = pathAndVersion[0];
		    	if (pathAndVersion.length > 1) { vpDataAbsolutePath2ToCompare = vpDataAbsolutePath2ToCompare + "_XXX_" + pathAndVersion[1]; }
		    	
		    	System.out.println("Path version length: " + pathAndVersion.length);
		    	System.out.println("IN: " + vpDataAbsolutePath2 + " for comparison is used: " + vpDataAbsolutePath2ToCompare);
		    	if (!vpDataAbsolutePath.equals(vpDataAbsolutePath2ToCompare) && vpDataAbsolutePath.contains(vpDataAbsolutePath2ToCompare)) {
		    		insertedPath = vpDataAbsolutePath2;
		    		System.out.println("INSERTED PATH: " + insertedPath.replace("//", "/"));
		    		this.vpDataFileNameToProjectPath.put(fileName, insertedPath.replace("//", "/"));
		    		break;
    	  		}
    	  	}
    	}
	}
}
