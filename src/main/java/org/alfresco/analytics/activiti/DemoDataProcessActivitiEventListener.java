package org.alfresco.analytics.activiti;

import java.util.Date;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.alfresco.events.activiti.ProcessDefinition;
import org.alfresco.events.activiti.ProcessEvent;
import org.alfresco.events.activiti.ProcessEventImpl;
import org.alfresco.events.activiti.ProcessInstance;
import org.alfresco.repo.events.activiti.listeners.ProcessActivitiEventListener;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.tenant.TenantUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DemoDataProcessActivitiEventListener extends ProcessActivitiEventListener
{
    private static Log logger = LogFactory.getLog(DemoDataProcessActivitiEventListener.class);
    private BulkActivitiManager manager;
    
    @Override
    protected ProcessEvent createProcessEvent(String eventType, ActivitiEvent activitiEvent, ExecutionEntity processInstanceEntity)
    {
        ProcessDefinition def = createProcessDefinition((ProcessDefinitionEntity) processInstanceEntity.getProcessDefinition(),
                                                        processInstanceEntity.getProcessDefinitionVersion());
        
        String user = AuthenticationUtil.getFullyAuthenticatedUser();
        long now = new Date().getTime();

        ProcessInstance process  = createProcess(processInstanceEntity);

        switch (eventType)
        {
            case PROCESS_COMPLETED: 
                process.setState(ProcessEvent.COMPLETED);
                process.setEndTime(now);
                break;
            case PROCESS_STARTED:
                process.setState(ProcessEvent.ACTIVE);
                process.setCreateTime(now);
                process.setStartUser(user);
                break;
            default:
                logger.warn("Unknown/unhandled event type: "+eventType);
                return null;
        }
        
        DemoActivitiProcess demoProcess = manager.lookupProcess(process.getId(), def.getKey());
        if (demoProcess != null)
        {
            process.setEndTime(demoProcess.getEndTime().getMillis());
            process.setCreateTime(demoProcess.getStartTime().getMillis());
            process.setStartUser(demoProcess.getUser());
        }
        
        return new ProcessEventImpl(eventType,user,now,TenantUtil.getCurrentDomain(),def,process);
    }

    public void setManager(BulkActivitiManager manager)
    {
        this.manager = manager;
    }
}
