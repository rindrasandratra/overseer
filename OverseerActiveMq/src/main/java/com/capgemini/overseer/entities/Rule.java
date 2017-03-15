package com.capgemini.overseer.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Rule {
	Integer id;
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

	public Rule() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Rule [Id=" + id + ", Name=" + name + ", Content=" + content + "]";
	}
}
