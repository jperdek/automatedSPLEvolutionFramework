package asynchronousPublisher.evolvedSPLPublishing;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.json.simple.JSONObject;

import asynchronousPublisher.RabbitMQAdapter;
import asynchronousPublisher.UnknownMessageTypeException;
import asynchronousPublisher.MessageQueueManager.PublishedMessageTypes;
import evolutionSimulation.EvolutionConfiguration;
import splEvolutionCore.SPLEvolutionCore;


/**
 * Specialized class responsible of publishing information about newly evolved SPL instance
 * -providing path to created instance
 * 
 * @author Jakub Perdek
 *
 */
public class EvolvedSPLPublisher extends RabbitMQAdapter {

	/**
	 * Instantiating class to publish information about newly evolved SPL instance - type "EVOLVED_SPL"
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	public EvolvedSPLPublisher() throws IOException, TimeoutException {
		super(System.getenv().getOrDefault("QUEUE_EVOLVED_SPL", "EVOLVED_SPL"));
	}
	
	/**
	 * Publishing message about newly evolved SPL instance by providing path to created instance
	 * 
	 * @param pathToCreatedSPL - path to created evolved SPL instance
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public void publishMessage(String pathToCreatedSPL) throws IOException, TimeoutException {
		this.publishMessageToQueue(pathToCreatedSPL);
	}
	
	/**
	 * Publishes information about currently evolved SPL, especially its location to create diverse representations
	 * 
	 * @param evolutionConfiguration - object that manages evolution configuration
	 * @param projectId - unique project identifier
	 * @param targetDestinationPath - destination path to resulting directory
	 * @param iterationBefore - true if actual iteration is one iteration behind produced SPLs otherwise false 
	 * @param currentScriptPath
	 */
	public static void publishMessageAboutEvolvedSPL(EvolutionConfiguration evolutionConfiguration, String projectId, String targetDestinationPath, boolean iterationBefore) {
		int iterationNumber = evolutionConfiguration.getIteration();
		if (SPLEvolutionCore.PRODUCE_MESSAGES_INTO_MQ_AFTER_DERIVATION) {
			JSONObject messageContent = new JSONObject();
			if (iterationBefore) { iterationNumber = iterationNumber - 1; }
			messageContent.put("evolutionIteration", String.valueOf(iterationNumber));
			messageContent.put("projectId", projectId);
			messageContent.put("targetPath", targetDestinationPath);
			
			try {
				evolutionConfiguration.publishMessageThroughQueueManager(
						PublishedMessageTypes.SPL_EVOLVED, messageContent.toString());
			} catch (IOException | TimeoutException | UnknownMessageTypeException e) {
				e.printStackTrace();
			}
		}
	}
}
