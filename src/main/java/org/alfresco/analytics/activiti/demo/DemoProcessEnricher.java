package org.alfresco.analytics.activiti.demo;

import org.alfresco.analytics.activiti.BulkActivitiManager;
import org.alfresco.analytics.activiti.DemoActivitiProcess;
import org.alfresco.events.activiti.ProcessEvent;
import org.alfresco.events.enrichers.EventEnricher;
import org.alfresco.events.types.Event;

/**
 * DemoProcessEnricher
 *
 * @author Gethin James
 */
public class DemoProcessEnricher implements EventEnricher<ProcessEvent>
{

    private BulkActivitiManager manager;
    
    @Override
    public Event enrich(ProcessEvent event)
    {
        DemoActivitiProcess demoProcess = manager.lookupProcess(event.getProcess().getId(), event.getProcessDefinition().getKey());
        if (demoProcess != null)
        {
            event.getProcess().setEndTime(demoProcess.getEndTime().getMillis());
            event.getProcess().setCreateTime(demoProcess.getStartTime().getMillis());
            event.getProcess().setStartUser(demoProcess.getUser());
        }
        return event;
    }

    public void setManager(BulkActivitiManager manager)
    {
        this.manager = manager;
    }

}
