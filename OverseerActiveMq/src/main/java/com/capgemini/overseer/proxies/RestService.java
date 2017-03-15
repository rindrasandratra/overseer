package com.capgemini.overseer.proxies;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.capgemini.overseer.entities.Rule;
import com.capgemini.overseer.helpers.ParseRule;
import com.capgemini.overseer.services.RuleService;

@RestController
public class RestService {

	@Autowired
	RuleService ruleService;

	@RequestMapping(value = "/addRule")
	public void addRule(@RequestBody String ruleStr){
		ParseRule parseRule = new ParseRule(ruleStr);
		Rule rule = parseRule.getRuleObj();
		System.out.println("Inserting a rule:" + rule);
		ruleService.create(rule);
	}

	@RequestMapping(value = "/rule")
	public ResponseEntity<List<Rule>> listAllRules() {
		List<Rule> rules = ruleService.getAll();
		if (rules.isEmpty()) {
			return new ResponseEntity<List<Rule>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Rule>>(rules, HttpStatus.OK);
	}

	@RequestMapping(value = "/init")
	public void init() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://overseer/api/rules/";
		String url_arg = "http://localhost:8080/rest-ws/addRule";
		url += Base64.getUrlEncoder().encodeToString(url_arg.getBytes());
		System.out.println("url = " +url);
		String result = restTemplate.getForObject(url, String.class);
		System.out.println(result);
	}
}
