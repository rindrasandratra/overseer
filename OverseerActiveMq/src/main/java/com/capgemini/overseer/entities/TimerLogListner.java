package com.capgemini.overseer.entities;

import java.sql.Timestamp;

public class TimerLogListner implements Runnable{
	
	Timestamp datelog;
	Timestamp datenotif;
	
	
	public Timestamp getDatelog() {
		return datelog;
	}

	public void setDatelog(Timestamp datelog) {
		this.datelog = datelog;
	}

	public Timestamp getDatenotif() {
		return datenotif;
	}

	public void setDatenotif(Timestamp datenotif) {
		this.datenotif = datenotif;
	}

	public void run() {
		System.out.println("run");
		// TODO Auto-generated method stub
		Timestamp datenow = new Timestamp(System.currentTimeMillis());
		if (datelog == null) datelog = datenow;
		long diff = (datenow.getTime() - datelog.getTime())/1000;
		while (diff > 600)
		{
			System.out.println("error : "+ diff);
		}
	}
}
