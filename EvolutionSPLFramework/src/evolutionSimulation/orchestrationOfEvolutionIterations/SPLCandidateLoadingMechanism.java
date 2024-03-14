package evolutionSimulation.orchestrationOfEvolutionIterations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;

import astFileProcessor.ASTLoader;
import evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies.imageContentQualityAssignment.VariationPointsDataAggregations;
import splEvolutionCore.SPLEvolutionCore;


public class SPLCandidateLoadingMechanism {

	private Map<String, String> vpDataFileNameToProjectPath;
	
	
	public SPLCandidateLoadingMechanism() {
		this.vpDataFileNameToProjectPath = new HashMap<String, String>();
	}
	
	public void loadAndParseSPLCandidates(String previousEvolutionDirectoryPath,
			VariationPointsDataAggregations variationPointsDataAggregations) throws IOException {
		 File dir = new File(previousEvolutionDirectoryPath);
		 File[] directoryListing = dir.listFiles();
		 String fileContent, fileName, vpDataAbsolutePath;
		 JSONArray variationPointsData;
		 if (directoryListing != null) {
		    for (File childFile: directoryListing) {
		    	vpDataAbsolutePath = childFile.getAbsolutePath();
		    	if (!childFile.isDirectory() && vpDataAbsolutePath.contains(SPLEvolutionCore.VARIATION_POINTS_DATA_NAME_ID_ENDING)) {
		    		fileContent = String.join(" ", Files.readAllLines(childFile.toPath()));
		    		variationPointsData = ASTLoader.loadJSONArrayFromString(fileContent);
		    	  	fileName = vpDataAbsolutePath.split(SPLEvolutionCore.VARIATION_POINTS_DATA_NAME_ID_ENDING)[0];
		    	  	fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
		    	  	variationPointsDataAggregations.processVariationPointsData(variationPointsData, fileName);
		    	  	this.vpDataFileNameToProjectPath.put(fileName, vpDataAbsolutePath);
		    	}
		    }
		 } else {
			 throw new IOException("The previous evolution path does not lead to directory!");
		 }
	}
}
