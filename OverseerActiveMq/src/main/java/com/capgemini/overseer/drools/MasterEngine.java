package com.capgemini.overseer.drools;

import java.io.Reader;
import java.io.StringReader;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import com.capgemini.overseer.entities.LogMessage;
import com.capgemini.overseer.entities.Rule;

public class MasterEngine {

	public static MasterEngine instance;
	private KnowledgeBuilder kbuilderStateful;
	private KnowledgeBase kbaseStateful;
	private KnowledgeBuilder kbuilderStateless;
	private KnowledgeBase kbaseStateless;
	private StatefulKnowledgeSession statefulSession;
	private StatelessKnowledgeSession statelessSession;
	KnowledgeBaseConfiguration config;
	KnowledgeRuntimeLogger loggerstateful;

	public StatefulKnowledgeSession getStatefulSession() {
		return statefulSession;
	}

	public StatelessKnowledgeSession getStatelessSession() {
		return statelessSession;
	}

	public void executeRule() {
		statefulSession.fireAllRules();
	}

	private MasterEngine() {
		config = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
		config.setOption(EventProcessingOption.STREAM);
		kbuilderStateful = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilderStateless = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbaseStateful = KnowledgeBaseFactory.newKnowledgeBase(config);
		kbaseStateless = KnowledgeBaseFactory.newKnowledgeBase();
	}

	public static MasterEngine getInstance() {
		if (instance == null) {
			instance = new MasterEngine();
		}
		return instance;
	}

	public Boolean addRule(Rule rule) {
		return (addRuleToStateFulSession(rule) && addRuleToStateLessSession(rule));
	}

	public void addLogToStatelessSession(LogMessage logMessage) {
		System.out.println("log added to stateless session" + logMessage.getMessageID());
		statelessSession.execute(logMessage);
	}

	public void addLogToStateFulSession(LogMessage logMessage) {
		System.out.println("log added to stateful session" + logMessage.getMessageID());
		statefulSession.insert(logMessage);
		executeRule();
	}

	public Boolean initEngine() {
		kbuilderStateless.add(ResourceFactory.newClassPathResource("rules/evaluation_level.drl"), ResourceType.DRL);
		return (restartStatelessSesstion() && restartStateFulSesstion());
	}

	public Boolean addRuleToStateLessSession(Rule rule) {
		Resource myRule = ResourceFactory.newReaderResource((Reader) new StringReader(rule.getContent()));
		kbuilderStateless.add(myRule, ResourceType.DRL);
		return restartStatelessSesstion();
	}

	public Boolean addRuleToStateFulSession(Rule rule) {
		Resource myRule = ResourceFactory.newReaderResource((Reader) new StringReader(rule.getContent()));
		kbuilderStateful.add(myRule, ResourceType.DRL);
		return restartStateFulSesstion();
	}

	public Boolean restartStatelessSesstion() {
		if (kbuilderStateless.hasErrors()) {
			System.out.println(kbuilderStateless.getErrors());
			return false;
		}
		kbaseStateless.addKnowledgePackages(kbuilderStateless.getKnowledgePackages());
		statelessSession = kbaseStateless.newStatelessKnowledgeSession();
		statelessSession.addEventListener(new TrackingAgendaListnerForStateLessSession());
		statelessSession.addEventListener(new DebugWorkingMemoryEventListener());
		return true;
	}

	public Boolean restartStateFulSesstion() {
		if (kbuilderStateful.hasErrors()) {
			System.out.println(kbuilderStateful.getErrors());
			return false;
		}
		kbaseStateful.addKnowledgePackages(kbuilderStateful.getKnowledgePackages());
		statefulSession = kbaseStateful.newStatefulKnowledgeSession();
		statefulSession.addEventListener(new TrackingAgendaListnerForStateFulSession());
		statefulSession.addEventListener(new DebugWorkingMemoryEventListener());
		loggerstateful = KnowledgeRuntimeLoggerFactory.newFileLogger(statefulSession, "log/statefulSession");
		return true;
	}

	public void removeRule(Rule rule) {
		kbaseStateful.removeRule(rule.getPackageName(), rule.getName());
	}
}
