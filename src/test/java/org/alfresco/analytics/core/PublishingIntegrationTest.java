package org.alfresco.analytics.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.alfresco.analytics.activiti.DemoActivitiProcess;
import org.alfresco.analytics.event.EventFactory;
import org.alfresco.analytics.event.EventHelper;
import org.alfresco.analytics.event.EventPublisherForTestingOnly;
import org.alfresco.repo.Client;
import org.alfresco.repo.Client.ClientType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:unittests/analytics-test-context.xml")
public class PublishingIntegrationTest
{
    private static Log logger = LogFactory.getLog(PublishingIntegrationTest.class);
    
    @Autowired
    EventFactory factory;
    
    @Autowired
    EventPublisherForTestingOnly testingPub;

    @Test
    public void testPublishEvents()
    {
        List<String> activities = EventHelper.getFileActivities();
        List<String> users = Arrays.asList("ken","barbie","bob","fred");
        List<String> sites = Arrays.asList("clothes","food", "gadgets");
        List<String> nodes = Arrays.asList("c66a1277-35f9-47fb-90a9-411c3bfc896a", "65bd55c9-9da1-425a-b62b-506ec8f23448", "dbd0bc92-0801-4b5c-8cf8-c4c549d8f1e9", "c66a1277-35f9-47fb-90a9-411c3bfc896a");
        List<Client> clients = Arrays.asList(Client.asType(ClientType.webclient),Client.asType(ClientType.cmis));
        LocalDate startDate = LocalDate.parse("2014-10-01");
        LocalDate endDate= LocalDate.parse("2014-10-07");
        int numberOfValues = 100;
        
        factory.createBulkNodeActivityEvents(activities, users, sites, nodes, clients, startDate, endDate, numberOfValues);
        assertEquals(numberOfValues, testingPub.getQueue().size());
        testingPub.getQueue().clear();  //clean up
        
    }
    
    @Test
    public void testActivitiDemoProcessesCreation()
    {
       List<String> users = Arrays.asList("ken","barbie","bob","fred");
       List<String> definitions = Arrays.asList("activitiAdhoc","activitiReview", "activitiReviewPooled");
       LocalDate startDate = LocalDate.parse("2014-10-01");
       LocalDate endDate= LocalDate.parse("2014-10-14");
       int numberOfProcesses = 500;       
       List<DemoActivitiProcess> process = factory.createActivitiDemoProcesses(definitions, users, startDate, endDate, numberOfProcesses);
       for (DemoActivitiProcess demoActivitiProcess : process)
       {
           logger.debug(demoActivitiProcess);
           assertTrue(demoActivitiProcess.getStartTime().isAfter(startDate.toDateTimeAtStartOfDay().minus(1)));
           assertTrue(demoActivitiProcess.getEndTime().isBefore(endDate.plusDays(1).toDateTimeAtStartOfDay().plus(1)));
           assertTrue(demoActivitiProcess.getDueTime().isAfter(demoActivitiProcess.getStartTime()));
           assertTrue(demoActivitiProcess.getDueTime().isBefore(demoActivitiProcess.getEndTime())||demoActivitiProcess.getDueTime().isEqual(demoActivitiProcess.getEndTime()));
       }
    }

}
