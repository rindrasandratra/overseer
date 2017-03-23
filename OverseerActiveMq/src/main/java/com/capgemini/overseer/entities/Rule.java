package com.capgemini.overseer.entities;

import com.capgemini.overseer.helpers.ParseRule;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Rule {
	Integer id;
	String packageName;
	String name;
	String content;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public Rule(Integer id, String name, String content) {
		super();
		this.id = id;
		this.name = name;
		this.content = content;
	}
	
	public Rule(String ruleStr){
		ParseRule parseRule = new ParseRule(ruleStr);
		this.id = parseRule.getRuleObj().getId();
		this.name = parseRule.getRuleObj().getName();
		this.content = parseRule.getRuleObj().getContent();
		this.packageName = parseRule.getRuleObj().getPackageName();
	}

	public Rule() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public String toString() {
		return "Rule [Id=" + id + ", Name=" + name + ", Content=" + content + "]";
	}
}
