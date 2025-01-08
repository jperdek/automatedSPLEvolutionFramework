package splEvolutionCore.derivation;

import codeConstructsEvaluation.transformation.ASTConverterClient;
import evolutionSimulation.EvolutionConfiguration;
import splEvolutionCore.DebugInformation;
import splEvolutionCore.SPLEvolutionCore;
import splEvolutionCore.SPLEvolutionCore.DuplicateProductPreventionOptions;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Manages resources of given created project/SPL as part of iteration phase
 * 
 * @author Jakub Perdek
 *
 */
public class DerivationResourcesManager {

	/**
	 * Logger to track creation of created hashes made from newly evolved sample software product lines
	 */
	private static final Logger logger = LoggerFactory.getLogger(DerivationResourcesManager.class);
	
	/**
	 * Array of processed/operational variation points
	 */
	private JSONArray harvestedVariationPoints;
	
	/**
	 * AST of given project
	 */
	private JSONObject usedAst;
	
	/**
	 * Configuration of given evolution phase - to manage certain decisions with resources such as destination/target paths,...
	 */
	private EvolutionConfiguration evolutionConfiguration;

	
	/**
	 * Stores information about resources of given project/SPL
	 * 
	 * @param usedAst - AST of given project
	 * @param evolutionConfiguration - the configuration of given evolution phase
	 * @param harvestedVariationPoints - array of processed/operational variation points
	 */
	public DerivationResourcesManager(JSONObject usedAst, EvolutionConfiguration evolutionConfiguration, JSONArray harvestedVariationPoints) {
		this.evolutionConfiguration = evolutionConfiguration;
		this.harvestedVariationPoints = harvestedVariationPoints;
		this.usedAst = usedAst;
	}
	
	/**
	 * Returns array of processed/operational variation points
	 * 
	 * @return array of processed/operational variation points
	 */
	public JSONArray getVariationPointData() { return this.harvestedVariationPoints; }
	
	/**
	 * Returns reference to evolution configuration data
	 * 
	 * @return reference to evolution configuration data
	 */
	public EvolutionConfiguration getEvolutionConfigurationReference() { return this.evolutionConfiguration; }
	
	/**
	 * Creates variation point ID from variation point name and affected code
	 * 
	 * @param variationPointObject - JSON representation of variation point from array of variation points
	 * @return string representation of variation point - its identifier
	 */
	private String getVariationPointID(JSONObject variationPointObject) {
		JSONObject variationPoint;
		String variationPointStringData = "";
		
		variationPoint = (JSONObject) variationPointObject;
		if (variationPoint.containsKey("variationPointName")) {
			 variationPointStringData = variationPointStringData + (String) variationPoint.get("variationPointName");
		} 
		if (variationPoint.containsKey("affectedCode")) {
			 variationPointStringData = variationPointStringData + (String) variationPoint.get("affectedCode");
		}
		return variationPointStringData;
	}
	
	/**
	 * Final conversion of application AST into Code
	 * 
	 * @return converted AST into code
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String synthesizeResultingCode() throws IOException, InterruptedException {
		return ASTConverterClient.convertFromASTToCode(this.usedAst.toString());
	}
	
	/**
	 * Returns the name of evolved content
	 * 
	 * @return string name of evolved content
	 */
	public String getEvolvedContentName() { return this.evolutionConfiguration.getEvolvedContentName(); }

	/**
	 * Creates string identifier from harvested variation points
	 * -getVariationPointID function is used to get identifier from each harvested configuration of variation point
	 * 
	 * @return string identifier of all used variation points
	 */
	private String getVariationPointsID() {
		String variationPointID = "";
		List<String> variationPointIdentifiers = new ArrayList<String>();
		for (Object variationPointObject: this.harvestedVariationPoints) {
			variationPointID = this.getVariationPointID((JSONObject) variationPointObject);
			variationPointIdentifiers.add(variationPointID);
		}
		Collections.sort(variationPointIdentifiers);
		return String.join("-", variationPointIdentifiers);
	}
	
	/**
	 * Generates identifier for project file name that is generated as hash from associated resources
	 * -harvested variation points array
	 * -updated AST of base project/SPL file
	 * 
	 * @param processedAst - JSON representation of processed AST
	 * @return trimmed MD5 hash generated from the projects/SPL main resources
	 */
	public String getEvolutionProjectFileNameIdentifier(JSONObject processedAst) {
		MessageDigest msgDst = null;
		String variationPointIds = this.getVariationPointsID();
		String variationPointsHash, sourceAstHash, overallHash = "";
		String concatenatedContent;
		try {
			msgDst = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if (msgDst != null) {
			variationPointsHash = new String(msgDst.digest(variationPointIds.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
			byte[] astBytes = msgDst.digest(processedAst.toString().getBytes(StandardCharsets.UTF_8));
			BigInteger no = new BigInteger(1, astBytes);  
			//converting message digest into hex value  
			sourceAstHash = no.toString(16);  
			while (sourceAstHash.length() < 32) {  sourceAstHash = "0" + sourceAstHash;  }  
			if (DebugInformation.SHOW_POLLUTING_INFORMATION) { logger.info("Source app hash:" + sourceAstHash); }
			concatenatedContent = variationPointsHash + sourceAstHash;
			//byte[] overallBytes = msgDst.digest(concatenatedContent.getBytes(StandardCharsets.UTF_8));
			overallHash = no.toString(16);  
			while (overallHash.length() < 32) { overallHash = "0" + overallHash;  }
			logger.info("Overall app hash:" + overallHash);
		}
		if (SPLEvolutionCore.DUPLICATE_PREVENTION_OPTION != DuplicateProductPreventionOptions.DISALLOW) {
			if (AlreadyCreatedProjectException.alreadyExists(overallHash)) {
				logger.warn("This SPL has been already generated!!! Hash: " + overallHash + " already exists. Skipping...");
				return null;
			} else {
				AlreadyCreatedProjectException.addHash(overallHash);
			}
		}
		
		return this.evolutionConfiguration.getEvolvedContentName() + "_" + Integer.toString(
				this.evolutionConfiguration.getIteration()) + "_" + evolutionConfiguration.wrapSourceSoftwareroductLineEvolutionId(overallHash.substring(0, 15));
	}
}
