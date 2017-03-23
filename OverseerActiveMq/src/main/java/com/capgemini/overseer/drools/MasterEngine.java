package com.capgemini.overseer.drools;

import java.io.Reader;
import java.io.StringReader;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import com.capgemini.overseer.entities.LogMessage;
import com.capgemini.overseer.entities.Rule;

public class MasterEngine {

	public static MasterEngine instance;
	private KnowledgeBuilder kbuilder;
	private KnowledgeBase kbase;
	private StatefulKnowledgeSession ksession;

	public StatefulKnowledgeSession getKsession() {
		return ksession;
	}

	public void executeRule() {
		ksession.fireAllRules();
	}

	private MasterEngine() {
		kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbase = KnowledgeBaseFactory.newKnowledgeBase();
	}

	public static MasterEngine getInstance() {
		if (instance == null) {
			instance = new MasterEngine();
		}
		return instance;
	}

	public Boolean initEngine() {
		kbuilder.add(ResourceFactory.newClassPathResource("rules/evaluation_level.drl"), ResourceType.DRL);
		if (kbuilder.hasErrors()) {
			System.out.println(kbuilder.getErrors());
			return false;
		}
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		ksession = kbase.newStatefulKnowledgeSession();
		ksession.addEventListener(new TrackingAgendaListner());
		return true;
	}

	public void addLog(LogMessage logMessage) {
		ksession.insert(logMessage);
	}

	public Boolean addRule(Rule rule) {
		Resource myRule = ResourceFactory.newReaderResource((Reader) new StringReader(rule.getContent()));
		kbuilder.add(myRule, ResourceType.DRL);
		if (kbuilder.hasErrors()) {
			System.out.println(kbuilder.getErrors());
			return false;
		}
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		ksession = kbase.newStatefulKnowledgeSession();
		System.out.println("liste : "+ksession.getKnowledgeBase().getKnowledgePackages().toString());
		ksession.addEventListener(new TrackingAgendaListner());
		return true;
	}

	public void removeRule(Rule rule) {
		kbase.removeRule(rule.getPackageName(), rule.getName());
	}
}
