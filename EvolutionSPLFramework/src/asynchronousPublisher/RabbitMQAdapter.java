package asynchronousPublisher;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * 
 * @author Jakub Perdek
 *
 */
public class RabbitMQAdapter {

	protected ConnectionFactory factory;
	protected String exchangeName;
	
	
	public RabbitMQAdapter() {
		this.factory = new ConnectionFactory();
		this.exchangeName = "ABSTRACT";
	}
	
	public Connection getConnection() throws IOException, TimeoutException { return this.factory.newConnection(); }
	
	public Channel createChannel() throws IOException, TimeoutException { return this.getConnection().createChannel(); }
	
	public void publishMessageToQueue(String message) throws IOException, TimeoutException {
		Channel channel = this.createChannel();
        channel.exchangeDeclare(this.exchangeName, "fanout");

        channel.basicPublish(this.exchangeName, "", null, message.getBytes("UTF-8"));
        System.out.println(" Publishing information " + this.exchangeName + ": '" + message + "'");
	}
}
