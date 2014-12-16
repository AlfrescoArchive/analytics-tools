/*
 * Copyright 2014 Alfresco Software, Ltd.  All rights reserved.
 *
 * License rights for this program may be obtained from Alfresco Software, Ltd. 
 * pursuant to a written agreement and any use of this program without such an 
 * agreement is prohibited. 
 */
package org.alfresco.analytics.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.test.TestHelper;
import org.alfresco.repo.events.activiti.listeners.ActivitiEventListenerBridge;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.apache.camel.CamelContext;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests routes defined via Spring with Camel
 *
 * @author Gethin James
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/alfresco/events/spring-events-messaging-context.xml",
                                   "/alfresco/extension/activiti-enrichers-context.xml"})
@DirtiesContext
public class ActivitiDemoRoutesSpringTest
{
    protected static String configurationResource = "activiti.cfg.xml";
    static ProcessEngine engine = TestHelper.getProcessEngine(configurationResource);

    @Configuration
    @ImportResource(value = {"/alfresco/extension/activiti-enrichers-context.xml",
                             "/alfresco/extension/activiti-executor-test-context",                      
                             "/alfresco/subsystems/Events/default/events-activiti-context.xml"})
    static class Config {//implements ApplicationContextAware { 
        
        ApplicationContext applicationContext;
        
        @Bean @Qualifier("activitiRuntimeService")
        public RuntimeService activitiRuntimeService() {
            return engine.getRuntimeService();
        }
            
        @Bean //Initialize AuthenticationUtil
        public AuthenticationUtil authUtil() {
            return new AuthenticationUtil();
        }
        
        @Bean
        public ActivitiEventListenerBridge bridge() {
            ActivitiEventListenerBridge bridge = new ActivitiEventListenerBridge();
            bridge.setRuntimeService(engine.getRuntimeService());
            //bridge.setApplicationContext(applicationContext);
            return bridge;
        }
//
//        @Override
//        public void setApplicationContext(ApplicationContext appContext) throws BeansException
//        {
//            applicationContext = appContext;
//        }
    }

    @Autowired
    protected CamelContext camelContext;
    
    @Test
    public void testProcessStart() throws Exception {

        String key = "activitiReview:1:3";
        
       engine.getRepositoryService().createDeployment()
               .addClasspathResource("alfresco/activiti/review.test.bpmn20.xml")
               .deploy();

       engine.getRuntimeService().startProcessInstanceById(key);
                
                DateTime start = DateTime.parse("2014-09-01");
                DateTime due = start.plusDays(5);
                DateTime end = DateTime.parse("2014-10-02T23:59:59.999Z");        
                DemoActivitiProcess demoProcess = new DemoActivitiProcess(null, key, start, end, due, "bob", null);
    }
}
