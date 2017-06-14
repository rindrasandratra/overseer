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
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.transport.TransportListener;

import com.capgemini.overseer.entities.ActiveMqConnectionListner;
import com.capgemini.overseer.entities.MessageJMSListner;

public final class ConsumerMessageService {
	public static final ConsumerMessageService instance = new ConsumerMessageService();
	Session session;
	ConnectionFactory connectionFactory;
	static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	static String queue_name = "logstash_log";
	Destination destination;
	Message message;
	//Connection connection;
	ActiveMQConnection connection;
	MessageConsumer consumer;
	Boolean state = false;

	private ConsumerMessageService() {
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
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
			connection = (ActiveMQConnection) connectionFactory.createConnection();
			state = true;
			connection.start();
			connection.addTransportListener(new ActiveMqConnectionListner());
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

	public boolean testConnection(){
		Boolean res = false;
		if ((state == true) && (connection != null))
			res = connection.isStarted();
		return res;
	}
}
