package com.capgemini.overseer.helpers;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.capgemini.overseer.entities.*;

public class ParseLogMessage {
	String StrLogMessage;
	LogMessage logMessage;
	TextMessage textMessage;
	DateHelper dateHelper = new DateHelper();
	
	
	public ParseLogMessage(TextMessage textMessage) {
		super();
		this.textMessage = textMessage;
		try {
			StrLogMessage = textMessage.getText();
			parse_str();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}



	public void parse_str() throws JMSException {
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(StrLogMessage);
			JSONObject jsonObject = (JSONObject) obj;
			logMessage = new LogMessage();
			logMessage.setMessageID(textMessage.getJMSMessageID());
			logMessage.setDate((String) jsonObject.get("date"));
			logMessage.setTemps((String) jsonObject.get("@timestamp"));
			logMessage.setLevel((String) jsonObject.get("level"));
			logMessage.setVersion(Integer.valueOf( (String) jsonObject.get("@version")));
			logMessage.setHost((String) jsonObject.get("host"));
			logMessage.setPid((String) jsonObject.get("pid"));
			logMessage.setSource((String) jsonObject.get("source"));
			logMessage.setMessage((String) jsonObject.get("message"));
			logMessage.setType((String) jsonObject.get("type"));
			logMessage.setCode_erreur((String) jsonObject.get("code_erreur"));
			logMessage.setCreationDate(dateHelper.convertStringToTimestamp(logMessage.getTemps()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getStrLogMessage() {
		return StrLogMessage;
	}

	public void setStrLogMessage(String strLogMessage) {
		StrLogMessage = strLogMessage;
	}

	public LogMessage getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(LogMessage logMessage) {
		this.logMessage = logMessage;
	}

}
