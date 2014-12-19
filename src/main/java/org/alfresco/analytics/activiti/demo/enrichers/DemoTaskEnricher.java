package org.alfresco.analytics.activiti.demo.enrichers;

import org.alfresco.analytics.activiti.BulkActivitiManager;
import org.alfresco.analytics.activiti.DemoActivitiProcess;
import org.alfresco.analytics.core.Calculator;
import org.alfresco.analytics.core.CommonsCalculator;
import org.alfresco.events.activiti.TaskEvent;
import org.alfresco.events.enrichers.EventEnricher;
import org.alfresco.events.types.Event;
import org.joda.time.DateTime;

/**
 * DemoTaskEnricher
 *
 * @author Gethin James
 */
public class DemoTaskEnricher implements EventEnricher<TaskEvent>
{

    private BulkActivitiManager manager;
    private Calculator calc = new CommonsCalculator();
    
    @Override
    public Event enrich(TaskEvent event)
    {
        DemoActivitiProcess demoProcess = manager.lookupProcess(event.getProcessInstanceId(), null);
        if (demoProcess != null)
        {
            event.setCreateTime(demoProcess.getStartTime().getMillis());
            event.setAssignee(demoProcess.getUser());
            
            //Default is ontime
            DateTime end = demoProcess.getDueTime();
            switch (demoProcess.getState())
            {
                case EARLY:
                    end = calc.randomTime(demoProcess.getStartTime().plus(1), demoProcess.getDueTime().minus(1));          
                    break;
                case LATE:
                    end = calc.randomTime(demoProcess.getDueTime().plus(1), demoProcess.getEndTime());                    
                    break;
                default:
                    //Don't do anything because the default is on-time
                    break;
            }
            event.setEndTime(end.getMillis());
        }
        return event;
    }

    public void setManager(BulkActivitiManager manager)
    {
        this.manager = manager;
    }



}
