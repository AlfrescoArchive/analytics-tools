package org.alfresco.analytics.activiti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.analytics.activiti.demo.executor.DemoProcessExecutor;
import org.alfresco.analytics.event.EventFactory;
import org.alfresco.error.AlfrescoRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Creates bulk activiti processes
 *
 * @author Gethin James
 */
public class BulkActivitiManager implements InitializingBean, ApplicationContextAware
{

    private static Log logger = LogFactory.getLog(BulkActivitiManager.class);
    
    private ApplicationContext applicationContext;
    private EventFactory factory; 
    private Map<String, DemoProcessExecutor> demoExecutors;
    private Map<String, DemoActivitiProcess> inFlightProcesses;
    private DemoActivitiProcess currentProcess;

    public DemoActivitiProcess lookupProcess(String processId, String definitionKey)
    {
        if (inFlightProcesses.containsKey(processId)) return inFlightProcesses.get(inFlightProcesses);
        if (currentProcess.getDefinitionKey().equals(definitionKey))
        {
            currentProcess.setProcessId(processId);
            inFlightProcesses.put(processId, currentProcess);
            return currentProcess;
        }
        throw new AlfrescoRuntimeException("Unknown demo process for "+definitionKey);
    }
    
    /**
     * Starts the number of processes
     * @param numberOfProcesses
     * @return the actual processes created
     */
    public int startProcesses(List<String> definitions, List<String> users, LocalDate startDate, LocalDate endDate, int numberOfProcesses)
    {
        
        List<DemoActivitiProcess> processes = factory.createActivitiDemoProcesses(definitions, users, startDate, endDate, numberOfProcesses);
        int processedCount = 0;
        for (DemoActivitiProcess demoActivitiProcess : processes)
        {
            currentProcess = demoActivitiProcess;
            logger.info("Processing  "+currentProcess);
            DemoProcessExecutor executor = demoExecutors.get(demoActivitiProcess.getDefinitionKey());
            if (executor == null)
            {
                logger.warn("Unable to find an Activit demo executor for "+demoActivitiProcess.getDefinitionKey()+", so I am ignoring it."); 
            }
            else
            {
                executor.executeDemoProcess(demoActivitiProcess, true);
                processedCount ++;
            }
        }
        currentProcess = null;
        logger.info("Processed. "+processes);
        logger.info("Number processed: "+processedCount);
        return processedCount;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception
    {
        logger.info("Discovering DemoProcessExecutors...");
        Map<String, DemoProcessExecutor> demoExecutorBeans = applicationContext.getBeansOfType(DemoProcessExecutor.class);
        demoExecutors = new HashMap<String, DemoProcessExecutor>(demoExecutorBeans.size());
        for (DemoProcessExecutor executor : demoExecutorBeans.values())
        {
            logger.info("Found a demo executor for "+executor.getProcessDefinitionKey());
            demoExecutors.put(executor.getProcessDefinitionKey(), executor);     
        }
        logger.info("..found "+demoExecutors.size()+" DemoProcessExecutors");
    }

    protected DemoProcessExecutor getExecutor(String processDefinitionKey)
    {
        return demoExecutors.get(processDefinitionKey);
    }
    
    public void setFactory(EventFactory factory)
    {
        this.factory = factory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
}
