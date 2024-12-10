package asynchronousPublisher;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import asynchronousPublisher.evolvedSPLPublishing.EvolvedSPLPublisher;
import splEvolutionCore.SPLEvolutionCore;


/**
 * Manager responsible to handle communication with message queue
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class MessageQueueManager {

	/**
	 * Types of published messages
	 * 
	 * @author Jakub Perdek
	 *
	 */
	public static enum PublishedMessageTypes {
		SPL_EVOLVED("splEvolved");
		
		/**
		 * String identifier of given strategy
		 */
		public final String label;

		/**
		 * Creates the given Evolution configuration strategy with specified name
		 * 
		 * @param label - string identifier of given strategy
		 */
	    private PublishedMessageTypes(String label) {
	        this.label = label;
	    }
	}
	
	
	/**
	 * Publishes messages to message queue about newly derived SPLs to produce their diverse representations
	 */
	private EvolvedSPLPublisher evolvedSPLProducer = null;
	
	
	/**
	 * Instantiates message queue manager to handle communication with message queue
	 */
	public MessageQueueManager() {
		if (SPLEvolutionCore.PRODUCE_MESSAGES_INTO_MQ_AFTER_DERIVATION) {
			this.evolvedSPLProducer = new EvolvedSPLPublisher();
		}
	}
	
	/**
	 * Publishes message to message queue according to provided type for further processing
	 * 
	 * @param publishedMessageType - type of published message to be sent into asynchronous message queue
	 * @param message - message that has to be sent into asynchronous queue
	 * 
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws UnknownMessageTypeException
	 */
	public void publish(PublishedMessageTypes publishedMessageType, String message) throws IOException, TimeoutException, UnknownMessageTypeException {
		if (evolvedSPLProducer != null) {
			if (publishedMessageType == PublishedMessageTypes.SPL_EVOLVED) {
				this.evolvedSPLProducer.publishMessage(message);
			} else {
				throw new UnknownMessageTypeException(publishedMessageType);
			}
		}
	}
}
