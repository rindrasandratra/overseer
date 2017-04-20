package com.capgemini.overseer.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
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

	public List<Rule> getAll() {
		List<Rule> rules = new ArrayList<Rule>();
		return rules;
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

	public void delete(String ruleStr) {
		// TODO Auto-generated method stub
		Rule rule = parseRule(ruleStr);
		System.out.println("delete rule "+ rule);
	}
	


}
