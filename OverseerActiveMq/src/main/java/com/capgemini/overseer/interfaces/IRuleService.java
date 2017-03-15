package com.capgemini.overseer.interfaces;

import java.util.List;

import com.capgemini.overseer.entities.Rule;

public interface IRuleService {
	
	void init();
	
	List<Rule> getAll();

	Rule get(Integer id);

	void create(Rule rule);

	void update(Rule rule);

	void delete(Rule rule);
}
