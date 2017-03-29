package com.capgemini.overseer.proxies;

import java.util.Base64;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.capgemini.overseer.config.UrlConfig;
import com.capgemini.overseer.entities.Rule;
import com.capgemini.overseer.services.RuleService;

@RestController
public class RestService {
	
	String urlApiRules = UrlConfig.getInstance().getProperty("urlApiRules");
	String urlApiAlerts = UrlConfig.getInstance().getProperty("urlApiAlerts");

	@Autowired
	RuleService ruleService;

	@RequestMapping(value = "/addRule")
	public Boolean addRule(@RequestBody String ruleStr) {
		return ruleService.addRule(ruleStr);
	}

	@RequestMapping(value = "/fire")
	public void fireRule() {
		ruleService.executeAllRulesToLog();
	}

	@RequestMapping(value = "/rule")
	public ResponseEntity<List<Rule>> listAllRules() {
		List<Rule> rules = ruleService.getAll();
		if (rules.isEmpty()) {
			return new ResponseEntity<List<Rule>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Rule>>(rules, HttpStatus.OK);
	}

	@RequestMapping(value = "/remove")
	public void removeRule(@RequestBody String ruleStr) {
		ruleService.delete(ruleStr);
	}

	@RequestMapping(value = "/")
	public Boolean init() {
		RestTemplate restTemplate = new RestTemplate();
		String urlApiAddRule = UrlConfig.getInstance().getProperty("urlApiAddRule");
		String urlAPI = urlApiRules + Base64.getUrlEncoder().encodeToString(urlApiAddRule.getBytes());
		String result = restTemplate.getForObject(urlAPI, String.class);
		System.out.println("result" + result);
		if (result.compareTo("\"ok\"") == 0) {
			return ruleService.initEngine();
		} else {
			return false;
		}
	}

	public void sendNotification(JSONObject responseJSON) {
		try {
			System.out.println("send notif");
			RestTemplate restTemplate = new RestTemplate();
			String result = restTemplate.postForObject(urlApiAlerts, responseJSON, String.class);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
