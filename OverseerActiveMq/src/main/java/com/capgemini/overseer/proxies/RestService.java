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
import com.capgemini.overseer.drools.MasterEngine;
import com.capgemini.overseer.entities.Rule;
import com.capgemini.overseer.services.ConsumerMessageService;
import com.capgemini.overseer.services.RuleService;

@RestController
public class RestService {

	String urlApiRules;
	String urlApiAlerts;

	public RestService() {
		super();
		urlApiRules = UrlConfig.getInstance().getProperty("urlApiRules");
		urlApiAlerts = UrlConfig.getInstance().getProperty("urlApiAlerts");
	}

	@Autowired
	RuleService ruleService;

	@RequestMapping(value = "/addRule")
	public Boolean addRule(@RequestBody String ruleStr) {
		return ruleService.addRule(ruleStr);
	}
	
	@RequestMapping(value = "/countFact")
	public long countFact(){
		return MasterEngine.getInstance().countFact();
	}

	@RequestMapping(value = "/isActive")
	public boolean getStatus() {
		return ruleService.getStatus();
	}
	
	@RequestMapping(value = "/checkActiveMQ")
	public boolean checkActiveMQ() {
		return ConsumerMessageService.getInstance().testConnection();
	}
	
	@RequestMapping(value = "/listRule")
	public ResponseEntity<List<String>> listAllRules() {
		List<String> rules = ruleService.getAll();
		System.out.println("get list of rules");
		if (rules.isEmpty()) {
			System.out.println("list of rules is empty");
			return new ResponseEntity<List<String>>(HttpStatus.NO_CONTENT);
		}
		System.out.println("list of rules not empty");
		System.out.println(new ResponseEntity<List<String>>(rules, HttpStatus.OK));
		return new ResponseEntity<List<String>>(rules, HttpStatus.OK);
	}

	@RequestMapping(value = "/removeRule")
	public Boolean removeRule(@RequestBody String ruleStr) {
		return ruleService.delete(ruleStr);
	}
	
	@RequestMapping(value = "/")
	public Boolean init() {
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.postForObject(urlApiRules, ruleService.encodeUrlData(), String.class);
		System.out.println("result" + result);
		if (result.compareTo("\"ok\"") == 0) {
			return ruleService.initEngine();
		} else {
			return false;
		}
	}
	
	@RequestMapping(value = "/stop")
	public boolean stopEngine(){
		return ruleService.stopEngine();
	}
	
	@RequestMapping(value = "/lastLogDate")
	public String getlastLogMessageReceivedTime(){
		return ruleService.getlastLogMessageReceivedTime();
	}
	
	@RequestMapping(value = "/restart")
	public boolean restartEngine(){
		System.out.println("redémarrage du moteur");
		if (stopEngine()){
			System.out.println("le moteur a bien été arrété et va redémarrer");
			return init();
		}
		return false;
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
