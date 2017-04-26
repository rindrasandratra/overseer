package com.capgemini.overseer.services;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;

import com.capgemini.overseer.config.UrlConfig;
import com.capgemini.overseer.drools.MasterEngine;
import com.capgemini.overseer.entities.Rule;
import com.capgemini.overseer.helpers.ParseRule;
import com.capgemini.overseer.interfaces.IRuleService;

@Repository
public class RuleService implements IRuleService {
	

	public Boolean initEngine() {
		// TODO Auto-generated method stub
		System.out.println("master engine started");
		System.out.println("MasterEngine.getInstance() " + MasterEngine.getInstance());
		return MasterEngine.getInstance().initEngine();
	}
	
	public Rule parseRule(String ruleStr){
		ParseRule parseRule = new ParseRule(ruleStr);
		Rule rule = parseRule.getRuleObj();
		return rule;
	}

	public List<String> getAll() {
		return MasterEngine.getInstance().getActiveRule();
	}

	public Rule get(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Boolean addRule(String ruleStr) {
		return addRule(parseRule(ruleStr));
	}

	public Boolean addRule(Rule rule) {
		// TODO Auto-generated method stub
		if ( MasterEngine.getInstance().addRule(rule)){
			System.out.println("rule added : "+ rule.getName() );
			return true;
		}
		System.out.println("rule not added : "+ rule.getName() );
		return false;
	}

	public void update(Rule rule) {
		// TODO Auto-generated method stub
		
	}

	public Boolean delete(String ruleStr) {
		// TODO Auto-generated method stub
		Rule rule = parseRule(ruleStr);
		return MasterEngine.getInstance().removeRule(rule);
	}

	public JSONObject encodeUrlData() {
		JSONObject obj = new JSONObject();
		obj.put("BaseUrl", UrlConfig.getInstance().getProperty("urlApiBaseRule"));
		System.out.println("data  : "+ obj.toJSONString());
		return obj;
	}
	
	public boolean stopEngine(){
		return MasterEngine.getInstance().stopEngine();
	}
	
	public boolean restartEngine(){
		if (MasterEngine.getInstance().stopEngine()){
			System.out.println("le moteur a bien �t� arr�t� et va red�marrer");
			return MasterEngine.getInstance().initEngine();
		}
		return false;
	}
	
	public long countFact(){
		return MasterEngine.getInstance().countFact();
	}

	public boolean getStatus() {
		return MasterEngine.getInstance().getIsStarted();
	}
	
}
