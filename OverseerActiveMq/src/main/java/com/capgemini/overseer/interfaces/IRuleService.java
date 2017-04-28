package com.capgemini.overseer.interfaces;

import java.util.List;

import org.json.simple.JSONObject;

import com.capgemini.overseer.entities.Rule;

public interface IRuleService {
	
	Boolean initEngine();
	
	List<String> getAll();
	
	Boolean addRule(String ruleStr);

	Boolean addRule(Rule rule);

	Boolean delete(String rule);
}
