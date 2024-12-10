package asynchronousPublisher;

import asynchronousPublisher.MessageQueueManager.PublishedMessageTypes;


/**
 * Exception informing about unprocessed message type that is not handled by asynchronous queue
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class UnknownMessageTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates exception informing about unprocessed message type that is not handled by asynchronous queue
	 * 
	 * @param publishedMessageType - message type that is left unhandled
	 */
	public UnknownMessageTypeException(PublishedMessageTypes publishedMessageType) {
		super(publishedMessageType.label);
	}
}
