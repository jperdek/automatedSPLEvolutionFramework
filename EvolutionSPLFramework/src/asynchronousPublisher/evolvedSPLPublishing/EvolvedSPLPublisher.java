package asynchronousPublisher.evolvedSPLPublishing;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import asynchronousPublisher.RabbitMQAdapter;


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
	 */
	public EvolvedSPLPublisher() {
		super();
		this.exchangeName = "EVOLVED_SPL";
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
}
