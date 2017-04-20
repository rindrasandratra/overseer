package com.capgemini.overseer.services;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import com.capgemini.overseer.entities.MessageJMSListner;

public final class ConsumerMessageService {
	public static final ConsumerMessageService instance = new ConsumerMessageService();
	Session session;
	ConnectionFactory connectionFactory;
	static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	static String queue_name = "logstash_log";
	Destination destination;
	Message message;
	Connection connection;
	MessageConsumer consumer;

	private ConsumerMessageService() {
	}

	public void init(){
		if (consumer == null){
			try {
				CreateConnectionFactory();
				CreateDestinationQueue();
				CreateMessageConsumer();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	public static ConsumerMessageService getInstance() {
		return instance;
	}

	private void CreateConnectionFactory() throws JMSException {
		if (connection == null){
			connectionFactory = new ActiveMQConnectionFactory(url);
			connection = connectionFactory.createConnection();
			connection.start();
		}
		if (session == null)
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	private void CreateDestinationQueue() throws JMSException {
		if (destination == null)
			destination = session.createQueue(queue_name);
	}

	private void CreateMessageConsumer() throws JMSException {
		if (consumer == null){
			consumer = session.createConsumer(destination);
			consumer.setMessageListener(new MessageJMSListner());
			System.out.println("Consumer created");
		}
	}
}
