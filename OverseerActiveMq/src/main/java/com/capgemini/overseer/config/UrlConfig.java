package com.capgemini.overseer.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class UrlConfig {
	public static UrlConfig instance;
	String configfilepath = "config/config.properties";
	Properties properties = new Properties();
	
	public static UrlConfig getInstance() {
		if (instance == null){
			instance = new UrlConfig();
		}
		return instance;
	}

	private UrlConfig() {
		super();
		loadUrlConfigFile();
	}
	
	private void loadUrlConfigFile(){
		try {
			InputStream in = UrlConfig.class.getClassLoader().getResourceAsStream(configfilepath);
			properties.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getProperty(String key){
		return properties.getProperty(key);
	}
}
