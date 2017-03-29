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

public class ConsumerMessageService {
	public static ConsumerMessageService instance;
	Session session;
	ConnectionFactory connectionFactory;
	static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	static String queue_name = "logstash_log";
	Destination destination;
	Message message;
	Connection connection;
	MessageConsumer consumer;

	private ConsumerMessageService() {
		super();
	}

	public static ConsumerMessageService getInstance() {
		if (instance == null) {
			instance = new ConsumerMessageService();
		}
		return instance;
	}

	public void Execute() {
		try {
			CreateConnectionFactory();
			CreateDestinationQueue();
			CreateMessageConsumer();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void CreateConnectionFactory() throws JMSException {
		if (connection == null) {
			connectionFactory = new ActiveMQConnectionFactory(url);
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		}
	}

	public void CreateDestinationQueue() throws JMSException {
			destination = session.createQueue(queue_name);
	}

	public void CreateMessageConsumer() throws JMSException {
		if (consumer == null) {
			consumer = session.createConsumer(destination);
			System.out.println("createConsumer");
			consumer.setMessageListener(new MessageJMSListner());
			System.out.println("Consumer created");
		}
	}
}
