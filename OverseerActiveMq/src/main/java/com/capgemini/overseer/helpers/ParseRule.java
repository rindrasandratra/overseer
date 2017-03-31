package com.capgemini.overseer.helpers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.capgemini.overseer.entities.Rule;
import com.capgemini.overseer.services.MapperService;

public class ParseRule {
	String ruleStr;
	Rule ruleObj;
		
	public ParseRule(String ruleStr) {
		super();
		this.ruleStr = MapperService.getInstance().replaceField(ruleStr);
		parseToObj();
	}
	
	public void parseToObj(){
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(ruleStr);
			JSONObject jsonObject = (JSONObject) obj;
			ruleObj = new Rule();
			String id = String.valueOf(jsonObject.get("Id"));
			ruleObj.setId(Integer.valueOf(id));
			ruleObj.setName((String) jsonObject.get("Name"));
			setContentRule((String) jsonObject.get("Content"));
			SetPackageName();
			setRuleNotification();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void setContentRule(String ruleContent){
		ruleContent = ruleContent.trim();
		ruleObj.setContent(ruleContent);
	}
	
	public void SetPackageName(){
		System.out.println("rule : "+ruleObj.getContent());
		String sbstr = ruleObj.getContent().substring("package".length(), ruleObj.getContent().indexOf("\n"));
		System.out.println("res : "+ sbstr);
		ruleObj.setPackageName(sbstr.trim());
	}
	
	public void setRuleNotification(){
		String strCreateNotif = "";// "Response response = new Response("+String.valueOf(ruleObj.getId())+",logMessage.getMessage());";
		int index = ruleObj.getContent().indexOf("then") + "then".length();
		String ruleContent = ruleObj.getContent().substring(0, index);
		ruleContent += "\n"+strCreateNotif +"\n"+ ruleObj.getContent().substring(index);
		ruleObj.setContent(ruleContent);
	}
	
	public String getRuleStr() {
		return ruleStr;
	}
	public void setRuleStr(String ruleStr) {
		this.ruleStr = ruleStr;
	}
	public Rule getRuleObj() {
		return ruleObj;
	}
	public void setRuleObj(Rule ruleObj) {
		this.ruleObj = ruleObj;
	}
	
	
}
