package com.capgemini.overseer.interfaces;

import java.util.List;

import com.capgemini.overseer.entities.Rule;

public interface IRuleService {
	
	Boolean initEngine();
	
	List<Rule> getAll();

	Rule get(Integer id);
	
	Boolean addRule(String ruleStr);

	Boolean addRule(Rule rule);

	void update(Rule rule);

	Boolean delete(String rule);
}
