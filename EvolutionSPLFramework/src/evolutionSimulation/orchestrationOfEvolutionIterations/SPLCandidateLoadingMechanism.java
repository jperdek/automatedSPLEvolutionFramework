package evolutionSimulation.orchestrationOfEvolutionIterations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;

import astFileProcessor.ASTLoader;
import evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.imageContentQualityAssignment.VariationPointsDataAggregations;
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
			VariationPointsDataAggregations variationPointsDataAggregations) throws IOException {
		System.out.println("Loading candidates for new evolution iteration from directory: " + previousEvolutionDirectoryPath);
		 File dir = new File(previousEvolutionDirectoryPath);
		 File[] directoryListing = dir.listFiles();
		 String fileContent, fileName, vpDataAbsolutePath, vpDataAbsolutePath2;
		 JSONArray variationPointsData;
		 if (directoryListing != null) {
		    for (File childFile: directoryListing) {
		    	vpDataAbsolutePath = childFile.getAbsolutePath();
		    	System.out.println("Processing the candidate file: " + vpDataAbsolutePath);
		    	if (!childFile.isDirectory() && vpDataAbsolutePath.contains(SPLEvolutionCore.VARIATION_POINTS_DATA_NAME_ID_ENDING)) {
		    		System.out.println("Found variation point data.");
		    		fileContent = String.join(" ", Files.readAllLines(childFile.toPath()));
		    		variationPointsData = ASTLoader.loadJSONArrayFromString(fileContent);
		    	  	fileName = vpDataAbsolutePath.split(SPLEvolutionCore.VARIATION_POINTS_DATA_NAME_ID_ENDING)[0];
		    	  	fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
		    	  	variationPointsDataAggregations.processVariationPointsData(variationPointsData, fileName);
		    	  	for (File childFile2: directoryListing) {
				    	vpDataAbsolutePath2 = childFile2.getAbsolutePath();
				    	if (!vpDataAbsolutePath.equals(vpDataAbsolutePath2) && vpDataAbsolutePath.contains(vpDataAbsolutePath2)) {
				    		this.vpDataFileNameToProjectPath.put(fileName, vpDataAbsolutePath2);
				    		break;
		    	  		}
		    	  	}
		    	}
		    }
		 } else {
			 throw new IOException("The previous evolution path does not lead to directory!");
		 }
	}
}
