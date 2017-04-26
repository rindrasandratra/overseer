package com.capgemini.overseer.drools;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.core.time.SessionPseudoClock;
import org.drools.definition.KnowledgePackage;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.runtime.rule.FactHandle;

import com.capgemini.overseer.entities.LogMessage;
import com.capgemini.overseer.entities.Response;
import com.capgemini.overseer.entities.Rule;
import com.capgemini.overseer.services.ConsumerMessageService;

public final class MasterEngine{

	static final MasterEngine instance = new MasterEngine();
	static final KnowledgeBuilder kbuilderStateful = KnowledgeBuilderFactory.newKnowledgeBuilder();
	static KnowledgeBase kbaseStateful;
	//static KnowledgeBuilder kbuilderStateless = KnowledgeBuilderFactory.newKnowledgeBuilder();
	static KnowledgeBase kbaseStateless = KnowledgeBaseFactory.newKnowledgeBase();
	StatefulKnowledgeSession statefulSession;
	static final StatelessKnowledgeSession statelessSession = kbaseStateless.newStatelessKnowledgeSession();
	KnowledgeBaseConfiguration  config = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
	KnowledgeSessionConfiguration configSession = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
	SessionPseudoClock clock;
	CountDownLatch latch = new CountDownLatch(1);
	private static Boolean isStarted = false;

	public StatefulKnowledgeSession getStatefulSession() {
		return statefulSession;
	}

	public void execute(){
		statefulSession.fireAllRules();
	}

	private MasterEngine() {
		config.setOption(EventProcessingOption.STREAM);
		kbaseStateful = KnowledgeBaseFactory.newKnowledgeBase(config);
		configSession.setOption( ClockTypeOption.get("pseudo") );
	}

	public static MasterEngine getInstance() {
		return instance;
	}

	public Boolean getIsStarted() {
		return isStarted;
	}

	public Boolean addRule(Rule rule) {

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (rule.getIsStateful())
			return addRuleToStateFulSession(rule) ;
		else
			return addRuleToStateLessSession(rule);
	}

	public void addLogToStatelessSession(LogMessage logMessage) {
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("log added to stateless session" + logMessage.getMessageID());
		statelessSession.execute(logMessage);
	}

	public void addLogToStateFulSession(LogMessage logMessage) {
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("log added to stateful session" + logMessage.getMessageID());
		statefulSession.insert(logMessage);
		clock.advanceTime(logMessage.getCreationDate().getTime() - clock.getCurrentTime(), TimeUnit.MILLISECONDS);
		statefulSession.fireAllRules();
	}

	public Boolean initEngine() {
		//kbuilderStateless.add(ResourceFactory.newClassPathResource("rules/evaluation_level.drl"), ResourceType.DRL);

		//statelessSession = kbaseStateless.newStatelessKnowledgeSession();
		statelessSession.addEventListener(new TrackingAgendaListnerForStateLessSession());

		statefulSession = kbaseStateful.newStatefulKnowledgeSession(configSession,null);
		statefulSession.addEventListener(new TrackingAgendaListnerForStateFulSession());
		clock = statefulSession.getSessionClock();
		latch.countDown();

		ConsumerMessageService.getInstance().init();
		isStarted = true;
		return true;
	}
	
	public void removeAllFact(){
		for (FactHandle fact : statefulSession.getFactHandles()) {
			statefulSession.retract(fact);
		}
	}
	
	public boolean stopEngine(){
		ConsumerMessageService.getInstance().stopConsumerMessageService();
		removeAllRules();
		removeAllFact();
		statefulSession.dispose();
		statefulSession.halt();
		System.out.println("Engine has stopped");
		isStarted = false;
		return true;
	}
	
	public void removeAllRules(){
		for (KnowledgePackage knowledgePackage : kbaseStateless.getKnowledgePackages()) {
			for (org.drools.definition.rule.Rule rule_ : knowledgePackage.getRules()) {
				kbaseStateless.removeRule(knowledgePackage.getName(), rule_.getName());
			}
		}
		
		for (KnowledgePackage knowledgePackage : statefulSession.getKnowledgeBase().getKnowledgePackages()) {
			for (org.drools.definition.rule.Rule rule_ : knowledgePackage.getRules()) {
				statefulSession.getKnowledgeBase().removeRule(knowledgePackage.getName(), rule_.getName());
			}
		}
	}

	public synchronized Boolean addRuleToStateLessSession(Rule rule) {
		KnowledgeBuilder kbuilderStateless = KnowledgeBuilderFactory.newKnowledgeBuilder();
		removeExistantRuleInStatelessSession(rule);
		Resource myRule = ResourceFactory.newReaderResource((Reader) new StringReader(rule.getContent()));
		System.out.println("rule to inject in stateless : "+ rule.getName());
		kbuilderStateless = KnowledgeBuilderFactory.newKnowledgeBuilder(); 
		kbuilderStateless.add(myRule, ResourceType.DRL);

		if (kbuilderStateless.hasErrors()) {
			System.out.println(kbuilderStateless.getErrors());
			return false;
		}
		kbaseStateless.addKnowledgePackages(kbuilderStateless.getKnowledgePackages());
		return true;
	}
	
	
	private List<String> getActiveRuleInStatelessSession(){
		List<String> ruleNames = new ArrayList<String>();
		for (KnowledgePackage knowledgePackage : kbaseStateless.getKnowledgePackages()) {
			for (org.drools.definition.rule.Rule rule_ : knowledgePackage.getRules()) {
				ruleNames.add(rule_.getName());
			}
		}
		return ruleNames;
	}

	private List<String> getActiveRuleInStatefulSession(){
		List<String> ruleNames = new ArrayList<String>();
		for (KnowledgePackage knowledgePackage : kbaseStateless.getKnowledgePackages()) {
			for (org.drools.definition.rule.Rule rule_ : knowledgePackage.getRules()) {
				ruleNames.add(rule_.getName());
			}
		}
		return ruleNames;
	}

	public synchronized Boolean addRuleToStateFulSession(Rule rule) {
		removeExistantRuleInStatefulSession(rule);
		Resource myRule = ResourceFactory.newReaderResource((Reader) new StringReader(rule.getContent()));
		System.out.println("rule to inject in stateful : "+ rule.getName());
		kbuilderStateful.add(myRule, ResourceType.DRL);
		if (kbuilderStateful.hasErrors()) {
			System.err.println(kbuilderStateful.getErrors());
			return false;
		}
		statefulSession.getKnowledgeBase().addKnowledgePackages(kbuilderStateful.getKnowledgePackages());
		return true;
	}
	
	private Boolean removeExistantRuleInStatefulSession(Rule rule){
		for (KnowledgePackage knowledgePackage : statefulSession.getKnowledgeBase().getKnowledgePackages()) {
			for (org.drools.definition.rule.Rule rule_ : knowledgePackage.getRules()) {
				if (rule_.getName().equals(rule.getName())){
					statefulSession.getKnowledgeBase().removeRule(knowledgePackage.getName(), rule.getName());
					return true;
				}
			}
		}
		return false;
	}
	
	private Boolean removeExistantRuleInStatelessSession(Rule rule){
		for (KnowledgePackage knowledgePackage : kbaseStateless.getKnowledgePackages()) {
			for (org.drools.definition.rule.Rule rule_ : knowledgePackage.getRules()) {
				if (rule_.getName().equals(rule.getName())){
					kbaseStateless.removeRule(knowledgePackage.getName(), rule.getName());
					return true;
				}
			}
		}
		return false;
	}
	

	synchronized public Boolean removeRule(Rule rule) {
		if (removeExistantRuleInStatefulSession(rule) || removeExistantRuleInStatelessSession(rule)){
			System.out.println("########## La regle : "+ rule.getName() + " a ete supprimee ##########");
			return true;
		}
		System.out.println("########## La regle : "+ rule.getName() + " n'a pas ete supprimee ##########");
		return false;
	}

	public long countFact(){
		return statefulSession.getFactCount();
	}

	public List<String> getActiveRule(){
		List<String> ruleNames = new ArrayList<String>();
		ruleNames.addAll(getActiveRuleInStatelessSession());
		ruleNames.removeAll(getActiveRuleInStatefulSession());
		ruleNames.addAll(getActiveRuleInStatefulSession());
		return ruleNames;
	}
}
