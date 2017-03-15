package com.capgemini.overseer.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.capgemini.overseer.entities.Rule;
import com.capgemini.overseer.interfaces.IRuleService;

@Repository
public class RuleService implements IRuleService {

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public List<Rule> getAll() {
		// TODO Auto-generated method stub
		List<Rule> rules = new ArrayList<Rule>();
		rules.add(new Rule(1,"name1","test1"));
		rules.add(new Rule(2,"name2","test2"));
		return rules;
	}

	public Rule get(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	public void create(Rule rule) {
		// TODO Auto-generated method stub
		
	}

	public void update(Rule rule) {
		// TODO Auto-generated method stub
		
	}

	public void delete(Rule rule) {
		// TODO Auto-generated method stub
		
	}


}
