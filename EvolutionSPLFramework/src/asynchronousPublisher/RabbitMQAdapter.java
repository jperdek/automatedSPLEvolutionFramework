package asynchronousPublisher;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


/**
 * Adapter for RabbitMQ message queue connection
 * 
 * @author Jakub Perdek
 *
 */
public class RabbitMQAdapter {

	/**
	 * Connection factory to create connections
	 */
	protected ConnectionFactory factory;
	
	/**
	 * The name of exchange used to share messages
	 */
	protected String exchangeName;
	
	
	/**
	 * Instantiates general RabbitMQ adapter instance
	 */
	public RabbitMQAdapter() {
		this.factory = new ConnectionFactory();
		this.exchangeName = "ABSTRACT";
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
	
	public void publishMessageToQueue(String message) throws IOException, TimeoutException {
		Channel channel = this.createChannel();
        channel.exchangeDeclare(this.exchangeName, "fanout");

        channel.basicPublish(this.exchangeName, "", null, message.getBytes("UTF-8"));
        System.out.println(" Publishing information " + this.exchangeName + ": '" + message + "'");
	}
}
