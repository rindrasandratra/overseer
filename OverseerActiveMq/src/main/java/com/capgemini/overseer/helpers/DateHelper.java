package com.capgemini.overseer.helpers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
	public Timestamp convertStringToTimestamp(String strDate){
		strDate = strDate.substring(0, strDate.length() - 1);
		strDate = strDate.replace("T", " ");
		Timestamp timestamp = null;
	    Date parsedDate;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		try {
			parsedDate = dateFormat.parse(strDate);
		    timestamp = new java.sql.Timestamp(parsedDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return timestamp;
	}
}
