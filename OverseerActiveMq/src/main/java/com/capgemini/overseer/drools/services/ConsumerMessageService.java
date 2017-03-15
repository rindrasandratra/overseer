package com.capgemini.overseer.drools.services;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.capgemini.overseer.drools.sessions.*;
import com.capgemini.overseer.entities.LogMessage;
import com.capgemini.overseer.helpers.ParseLogMessage;

public class ConsumerMessageService {
	Session session;
	static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	static String queue_name = "logstash_log";
	Destination destination;
	Message message;
	Connection connection;
			
	public void Execute(){
		try {
			CreateConnectionFactory();
			CreateDestinationQueue();
			CreateMessageConsumer();
			ProcessMessage();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void CreateConnectionFactory() throws JMSException{
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		connection = connectionFactory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}
	
	public void CreateDestinationQueue() throws JMSException{
		destination = session.createQueue(queue_name);
	}
	
	public void CreateMessageConsumer() throws JMSException {
		// MessageConsumer is used for receiving (consuming) messages
		MessageConsumer consumer = session.createConsumer(destination);
		// wait until a message to arrive on the queue
		message = consumer.receive();
	}
	
	public void ProcessMessage(){
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			ParseLogMessage parseLogMessage = new ParseLogMessage(textMessage);
			LogMessage logMessage = parseLogMessage.getLogMessage();
			DroolsSession.getInstance().getKsession().insert(logMessage);
			DroolsSession.getInstance().execute_rule();
		}
	}
}
