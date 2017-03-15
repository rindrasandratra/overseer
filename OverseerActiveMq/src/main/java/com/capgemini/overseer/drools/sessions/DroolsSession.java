package com.capgemini.overseer.drools.sessions;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsSession {
	public static DroolsSession instance;
	private KieSession ksession;
	KieServices kieServices;
	KieContainer kcontainer;

	private DroolsSession() {
		kieServices = KieServices.Factory.get();
		kcontainer = kieServices.getKieClasspathContainer();
		ksession = kcontainer.newKieSession("ksession-rules");
	}

	public void execute_rule() {
		ksession.fireAllRules();
	}
	
	

	public KieServices getKieServices() {
		return kieServices;
	}

	public void setKieServices(KieServices kieServices) {
		this.kieServices = kieServices;
	}

	public KieContainer getKcontainer() {
		return kcontainer;
	}

	public void setKcontainer(KieContainer kcontainer) {
		this.kcontainer = kcontainer;
	}

	public KieSession getKsession() {
		return ksession;
	}

	public static DroolsSession getInstance() {
		if (instance == null) {
			instance = new DroolsSession();
		}
		return instance;
	}

}
