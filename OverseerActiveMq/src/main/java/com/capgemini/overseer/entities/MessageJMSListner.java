package com.capgemini.overseer.entities;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.capgemini.overseer.drools.MasterEngine;
import com.capgemini.overseer.helpers.ParseLogMessage;

public class MessageJMSListner implements MessageListener {

	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			ParseLogMessage parseLogMessage = new ParseLogMessage(textMessage);
			LogMessage logMessage = parseLogMessage.getLogMessage();
			MasterEngine.getInstance().addLogToStatelessSession(logMessage);
		}
	}

}
