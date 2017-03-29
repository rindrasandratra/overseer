package com.capgemini.overseer.entities;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.capgemini.overseer.drools.MasterEngine;
import com.capgemini.overseer.helpers.ParseLogMessage;

public class MessageJMSListner implements MessageListener {

	public void onMessage(Message message) {
		System.out.println("Processing message in statelessSession");
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			ParseLogMessage parseLogMessage = new ParseLogMessage(textMessage);
			LogMessage logMessage = parseLogMessage.getLogMessage();
			System.out.println("addLogToStatelessSession");
			MasterEngine.getInstance().addLogToStatelessSession(logMessage);
//			MasterEngine.getInstance().executeRule();
		}
	}

}
