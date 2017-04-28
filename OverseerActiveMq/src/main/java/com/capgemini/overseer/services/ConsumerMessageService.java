package com.capgemini.overseer.services;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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

	public void init() {
		if (consumer == null) {
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
		if (connection == null) {
			System.out.println("trying to create connection factory");
			connectionFactory = new ActiveMQConnectionFactory(url);
			connection = connectionFactory.createConnection();
			connection.start();
			System.out.println("connection has started");
		}
		if (session == null)
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	private void CreateDestinationQueue() throws JMSException {
		if (destination == null)
			destination = session.createQueue(queue_name);
	}

	private void CreateMessageConsumer() throws JMSException {
		if (consumer == null) {
			consumer = session.createConsumer(destination);
			consumer.setMessageListener(new MessageJMSListner());
			System.out.println("Consumer created");
		}
	}

	public void stopConsumerMessageService() {
		try {
			if (consumer != null) {
				consumer.close();
				connection.close();
			}
			connection = null;
			session = null;
			destination = null;
			consumer = null;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean testConnection(){
		try {
			System.out.println("try to ping");
			InetAddress.getByName(url).isReachable(1000);
			return true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
