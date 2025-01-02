package asynchronousPublisher;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import evolutionSimulation.EvolutionConfiguration;


/**
 * Adapter for RabbitMQ message queue connection
 * 
 * @author Jakub Perdek
 *
 */
public class RabbitMQAdapter {
	
	/**
	 * Logger to track exchange through Rabbit MQ adapter
	 */
	private static final Logger logger = LoggerFactory.getLogger(RabbitMQAdapter.class);

	/**
	 * Connection factory to create connections
	 */
	protected ConnectionFactory factory;
	
	/**
	 * Channel with message flow
	 */
	protected Channel channel;
	
	/**
	 * The name of exchange used to share messages
	 */
	protected String exchangeName;
	
	
	/**
	 * Instantiates general RabbitMQ adapter instance
	 * 
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	public RabbitMQAdapter(String exchangeName) throws IOException {
		this.factory = new ConnectionFactory();
		this.factory.setUsername(MessageExchangeConnectionConfiguration.MESSAGE_EXCHANGE_CUSTOM_USER);
		factory.setPassword(MessageExchangeConnectionConfiguration.MESSAGE_EXCHANGE_CUSTOM_PASSWORD);
		factory.setVirtualHost(MessageExchangeConnectionConfiguration.MESSAGE_EXCHANGE_VHOST);
		factory.setHost(MessageExchangeConnectionConfiguration.MESSAGE_EXCHANGE_HOST);
		factory.setPort(MessageExchangeConnectionConfiguration.MESSAGE_EXCHANGE_PORT);
		this.exchangeName = exchangeName;
		
		if (this.tryGetChannelAndConnect()) { this.channel.exchangeDeclare(this.exchangeName, "fanout"); }
		logger.info("Created exchange: " + this.channel);
	}
	
	/**
	 * Tries to connect and obtain channel
	 * 
	 * @return true if connection is successful otherwise false
	 */
	private boolean tryGetChannelAndConnect() {
		logger.info("Trying to connect to message queue...");
		try {
			this.channel = this.createChannel();
			return true;
		} catch(IOException e) {
			this.channel = null;
		} catch (TimeoutException te) {
			this.channel = null;
		}
		logger.info("Connecting to message queue failed...");
		return false;
	}
	
	
	/**
	 * Returns newly created RabbitMQ connection
	 * 
	 * @return rabbitMQ connection created using appropriate connection factory
	 * 
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public Connection getConnection() throws IOException, TimeoutException { return this.factory.newConnection(); }
	
	/**
	 * Creates channel from newly created connection
	 * 
	 * @return created channel from newly created connection
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public Channel createChannel() throws IOException, TimeoutException { return this.getConnection().createChannel(); }
	
	/**
	 * Publishes message to queue under respective channel and exchange name
	 * 
	 * @param message
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public void publishMessageToQueue(String message) throws IOException {
		if (this.channel == null && this.tryGetChannelAndConnect()) { this.channel.exchangeDeclare(this.exchangeName, "fanout"); }

		if (this.channel != null) {
	        this.channel.basicPublish(this.exchangeName, "", null, message.getBytes("UTF-8"));
	        logger.info("Publishing information using " + this.exchangeName + ": '" + message + "'");
		} else {
			logger.info("Cannot publish into queue... Connection is lost. Skipping...");
		}
	}
}
