package com.capgemini.overseer.entities;

import org.json.simple.JSONObject;

import com.capgemini.overseer.proxies.RestService;

public class Response {
	Integer ruleId;
	String content;
	RestService restService;
	JSONObject responseJSON;
	
	public Response(Integer ruleId, String content) {
		super();
		this.ruleId = ruleId;
		this.content = content;
		sendResponse();
	}
	
	public void parseToJson(){
		responseJSON = new JSONObject();
		responseJSON.put("ruleID", ruleId);
		responseJSON.put("content", content);
	}
	
	public void sendResponse(){
		restService = new RestService();
		parseToJson();
		restService.sendNotification(responseJSON);
	}
	
}
