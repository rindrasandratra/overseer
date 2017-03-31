package com.capgemini.overseer.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import com.capgemini.overseer.interfaces.IMapper;

public class MapperService implements IMapper{
	
	String mapfilepath = "config/mappingRuleFields.properties";
	public static MapperService instance;

	Properties properties = new Properties();

	private MapperService() {
		super();
		loadPropertyFile();
	}

	public static MapperService getInstance() {
		if (instance == null){
			instance = new MapperService();
		}
		return instance;
	}

	public void loadPropertyFile() {
		try {
			InputStream in = MapperService.class.getClassLoader().getResourceAsStream(mapfilepath);
			properties.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String replaceField(String strRule){
		Enumeration<Object> listkeys = fetchKeys();
		while(listkeys.hasMoreElements()){
			String key = (String) listkeys.nextElement();
			strRule = strRule.replace(key, getValue(key));
		}
		return strRule;
	}
	
	public Enumeration<Object> fetchKeys() {
		return properties.keys();
	}
	
	public String getValue(String key){
		return (String) properties.get(key);
	}



}
