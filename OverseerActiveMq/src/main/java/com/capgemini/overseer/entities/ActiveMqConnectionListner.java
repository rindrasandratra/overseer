package com.capgemini.overseer.entities;

import java.io.IOException;

import org.apache.activemq.transport.TransportListener;

import com.capgemini.overseer.services.ConsumerMessageService;

public class ActiveMqConnectionListner implements TransportListener {

	public void transportResumed() {
		System.out.println("transportResumed");
		ConsumerMessageService.getInstance().setState(true);
	}
	
	public void transportInterupted() {
		System.out.println("transportInterupted");
		ConsumerMessageService.getInstance().setState(false);
	}
	
	public void onException(IOException error) {
		System.out.println("onException");
		ConsumerMessageService.getInstance().setState(false);
	}
	
	public void onCommand(Object command) {
		// TODO Auto-generated method stub
		
	}

}
