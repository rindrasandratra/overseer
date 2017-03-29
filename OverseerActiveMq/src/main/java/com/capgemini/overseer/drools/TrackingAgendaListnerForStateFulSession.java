package com.capgemini.overseer.drools;

import org.drools.definition.rule.Rule;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.DefaultAgendaEventListener;

public class TrackingAgendaListnerForStateFulSession extends DefaultAgendaEventListener {
	@Override
	public void afterActivationFired(AfterActivationFiredEvent event) {
		Rule rule = event.getActivation().getRule();
		String ruleName = rule.getName();
		System.out.println("*************************rule name fired in stateful: " + ruleName + "*************************");
//		List<? extends FactHandle> factHandles = event.getActivation().getFactHandles();
		//MasterEngine.getInstance().getStatefulSession().retract(arg0);
//		List<Object> logMessages = event.getActivation().getObjects();
//		for (Object object : logMessages) {
//			LogMessage logMessage = (LogMessage) object;
//		}
	}
}
