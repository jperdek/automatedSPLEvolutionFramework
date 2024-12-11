package asynchronousPublisher;


/**
 * Configuration to exchange messages using RabbitMQ
 * 
 * 
 * @author Jakub Perdek
 *
 */
public class MessageExchangeConnectionConfiguration {

	/**
	 * Login name to reach rabbit MQ exchange queue
	 */
	public static final String MESSAGE_EXCHANGE_CUSTOM_USER = System.getenv().getOrDefault("MQ_CONSUMER_USER_NAME", "guest");
	
	/**
	 * Password associated with custom user
	 */
	public static final String MESSAGE_EXCHANGE_CUSTOM_PASSWORD = System.getenv().getOrDefault("MQ_CONSUMER_USER_PASSWORD", "guest");
	
	/**
	 * Vhost to reach processing queue and exchange messages
	 */
	public static final String MESSAGE_EXCHANGE_VHOST = System.getenv().getOrDefault("PROCESSING_QUEUE_VHOST", "rabbitmq");
	
	/**
	 * Host to reach processing queue and exchange messages
	 */
	public static final String MESSAGE_EXCHANGE_HOST = System.getenv().getOrDefault("PROCESSING_QUEUE_HOST", "localhost");
	
	/**
	 * Port to reach rabbit MQ service
	 */
	public static final int MESSAGE_EXCHANGE_PORT = Integer.parseInt(System.getenv().getOrDefault("PROCESSING_QUEUE_PORT", "5672"));
}
