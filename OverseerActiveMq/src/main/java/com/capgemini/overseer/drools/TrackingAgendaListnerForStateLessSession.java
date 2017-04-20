package com.capgemini.overseer.drools;

import java.util.Collection;
import java.util.List;

import org.drools.definition.rule.Rule;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.BeforeActivationFiredEvent;
import org.drools.event.rule.DefaultAgendaEventListener;
import org.drools.runtime.ObjectFilter;

import com.capgemini.overseer.entities.LogMessage;

public class TrackingAgendaListnerForStateLessSession extends DefaultAgendaEventListener {
	@Override
	public void afterActivationFired(AfterActivationFiredEvent event) {
		Rule rule = event.getActivation().getRule();
		String ruleName = rule.getName();
		List<Object> logMessages = event.getActivation().getObjects();
		System.out.println("*************************rule name fired in stateless: " + ruleName + "*************************");
		for (Object object : logMessages) {
			LogMessage logMessage = (LogMessage) object;
			if (!checkIfFactIsAlreadyInSession(logMessage)) {
				System.out.println("inserting log to stateful session after stateless: " + logMessage.getMessageID());
				MasterEngine.getInstance().addLogToStateFulSession(logMessage);
			}
		}
	}
	
	@Override
	public void beforeActivationFired(BeforeActivationFiredEvent event) {
		Rule rule = event.getActivation().getRule();
		System.out.println("Rule beforeActivationFired(BeforeActivationFiredEvent event) " + rule.getName());
	}

	public Boolean checkIfFactIsAlreadyInSession(final LogMessage instance) {
		Collection<Object> objects = MasterEngine.getInstance().getStatefulSession().getObjects(new ObjectFilter() {
			public boolean accept(Object obj) {
				LogMessage logMessage = (LogMessage) obj;
				return logMessage.getMessageID().equals(instance.getMessageID());
			}
		});
		return (objects.size() > 0) ? true : false;
	}
}
