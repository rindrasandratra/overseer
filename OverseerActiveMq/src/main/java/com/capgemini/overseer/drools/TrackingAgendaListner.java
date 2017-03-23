package com.capgemini.overseer.drools;

import org.drools.definition.rule.Rule;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.DefaultAgendaEventListener;

public class TrackingAgendaListner extends DefaultAgendaEventListener {
	@Override
    public void afterActivationFired(AfterActivationFiredEvent event) {
        Rule rule = event.getActivation().getRule();

        String ruleName = rule.getName();
        System.out.println("rule name : "+ ruleName);
    }
}
