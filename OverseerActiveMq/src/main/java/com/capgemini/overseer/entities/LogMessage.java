package com.capgemini.overseer.entities;

import java.sql.Timestamp;

public class LogMessage {
	String messageID;
	String date;
	String level;
	String temps;
	Integer version;
	String host;
	String pid;
	String source;
	String message;
	String type;
	String code_erreur;
	Timestamp creationDate;
	
	
	public LogMessage() {
		// TODO Auto-generated constructor stub
	}

	
	public String getMessageID() {
		return messageID;
	}


	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}


	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getTemps() {
		return temps;
	}
	public void setTemps(String temps) {
		this.temps = temps;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode_erreur() {
		return code_erreur;
	}
	public void setCode_erreur(String code_erreur) {
		this.code_erreur = code_erreur;
	}

	@Override
	public String toString() {
		return "LogMessage: \n"
				+ "id=" + messageID + 
				"\ndate=" + date + 
				"\nlevel=" + level + 
				"\ntemps=" + temps + 
				"\nversion=" + version + 
				"\nhost=" + host + 
				"\npid=" + pid + 
				"\nsource=" + source + 
				"\nmessage=" + message + 
				"\ntype=" + type +
				"\ntimestamp=" + creationDate
				+ "\ncode_erreur=" + code_erreur;
	}


	public Timestamp getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Timestamp timestamp) {
		this.creationDate = timestamp;
	}
	
	/* EXEMPLE
	 * "date" => "Mon Mar 06 15:05:32.620996 2017",
           "path" => "C:/wamp64/logs/apache_error.log",
     "@timestamp" => 2017-03-06T14:06:55.383Z,
          "level" => "mpm_winnt:notice",
       "@version" => "1",
           "host" => "LFR003053",
            "pid" => "8556:tid 504",
         "source" => "Child",
        "message" => "Starting 64 worker threads.\r",
           "type" => "apache_error",
    "code_erreur" => "AH00354"
	 */
	
}
