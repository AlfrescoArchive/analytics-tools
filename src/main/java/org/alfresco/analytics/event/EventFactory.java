
package org.alfresco.analytics.event;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.analytics.activiti.DemoActivitiProcess;
import org.alfresco.analytics.core.Calculator;
import org.alfresco.analytics.core.CommonsCalculator;
import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.repo.Client;
import org.alfresco.service.cmr.security.PersonService.PersonInfo;
import org.alfresco.service.cmr.site.SiteInfo;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value="bulkEventFactory")
public class EventFactory
{
    @Autowired
    private EventHelper helper;
    private Calculator calc = new CommonsCalculator();

    /**
     * Create activities based on the data.
     * @param activities
     * @param users
     * @param sites
     * @param nodes
     * @param clients
     * @param times
     */
    public void createBulkNodeActivityEvents(List<String> activities, List<String> users,
                List<String> sites, List<String> nodes, List<Client> clients, long[] times)
    {
        int numberOfevents = times.length;
        activities = calc.distributeValues(activities, numberOfevents);
        users =  calc.distributeValues(users, numberOfevents);
        sites =  calc.distributeValues(sites, numberOfevents);
        nodes =  calc.distributeValues(nodes, numberOfevents);
        clients =  calc.distributeValues(clients, numberOfevents);
        
        if (activities.size() == numberOfevents && sites.size() == numberOfevents
                    && users.size() == numberOfevents && nodes.size() == numberOfevents
                    && clients.size() == numberOfevents)
        {
            for (int i = 0; i < times.length; i++)
            {
                helper.publishActivity(activities.get(i), users.get(i), sites.get(i), nodes.get(i), clients.get(i), times[i], null);
            }

        }
        else
        {
            throw new AlfrescoRuntimeException("All the data needs to be the same size");

        }
    }
    
    public void createBulkNodeActivityEvents(List<String> activities, List<String> users,
                List<String> sites, List<String> nodes, List<Client> clients, LocalDate startDate, LocalDate endDate, int numberOfValues)
    {
        long[] times = calc.distributeDates(startDate, endDate, numberOfValues);
        createBulkNodeActivityEvents(activities,users,sites, nodes, clients, times);

    }
    
    public List<DemoActivitiProcess> createActivitiDemoProcesses(List<String> definitions, List<String> users, LocalDate startDate, LocalDate endDate, int numberOfProcesses)
    {
        List<DemoActivitiProcess> processes = new ArrayList<DemoActivitiProcess>(numberOfProcesses);
        users =  calc.distributeValues(users, numberOfProcesses);
        definitions =  calc.distributeValues(definitions, numberOfProcesses);
        DateTime startTime = startDate.toDateTimeAtStartOfDay();
        DateTime endTime = endDate.plusDays(1).toDateTimeAtStartOfDay();
        
        for (int i = 0; i < numberOfProcesses; i++)
        {
            DateTime processStart = calc.randomTime(startTime, endDate.toDateTimeAtStartOfDay().minus(1));
            DateTime processEnd = calc.randomTime(processStart.plusMinutes(30), endTime);
            DateTime processDue = calc.randomTime(processStart.plusMinutes(5), processEnd);
            processes.add(new DemoActivitiProcess(null, definitions.get(i), processStart, processEnd, processDue, users.get(i)));
        }
        return processes;
    }

    public void updateSite(SiteInfo info, LocalDate when)
    {
        helper.publishSiteUpdate(info.getShortName(), info.getTitle(), info.getDescription(), info.getVisibility().toString(), info.getSitePreset(), when.toDate());
    }

    public void updateUser(PersonInfo info, LocalDate when)
    {
        helper.publishUserUpdate(info.getUserName(), info.getFirstName(), info.getLastName(), when.toDate());
    }
}
