package org.alfresco.analytics.activiti.demo.executor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.alfresco.analytics.activiti.DemoActivitiProcess;
import org.alfresco.repo.policy.BehaviourFilter;
import org.alfresco.repo.workflow.BPMEngineRegistry;
import org.alfresco.repo.workflow.PackageManager;
import org.alfresco.repo.workflow.WorkflowModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.cmr.workflow.WorkflowDefinition;
import org.alfresco.service.cmr.workflow.WorkflowPath;
import org.alfresco.service.cmr.workflow.WorkflowService;
import org.alfresco.service.cmr.workflow.WorkflowTask;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Executes the review process
 *
 * @author Gethin James
 */
public class ReviewProcessExecutor implements DemoProcessExecutor
{
    private String processDefinitionKey;
    protected RuntimeService runtimeService;
    protected WorkflowService workflowService;
    protected PersonService personService;
    protected BehaviourFilter behaviourFilter;
    protected NodeService nodeService;
    
    private static Log logger = LogFactory.getLog(ReviewProcessExecutor.class);
    
    @Override
    public void executeDemoProcess(DemoActivitiProcess demoProcess, boolean complete)
    {
        WorkflowDefinition reviewDef = workflowService.getDefinitionByName(processDefinitionKey);
        Map<QName, Serializable> properties = new HashMap<QName, Serializable>();
        
        setStartProperties(demoProcess, reviewDef, properties);
        WorkflowPath path = workflowService.startWorkflow(reviewDef.getId(), properties);
        String workflowInstanceId = path.getInstance().getId();
        
        String processInstanceId = BPMEngineRegistry.getLocalId(workflowInstanceId);
        WorkflowTask startTask = workflowService.getStartTask(workflowInstanceId);
        workflowService.endTask(startTask.getId(), null);
        
        setProcessVariables(processInstanceId);
        
        List<WorkflowTask> tasks = workflowService.getTasksForWorkflowPath(path.getId());
        for (WorkflowTask workflowTask : tasks)
        {
            workflowService.endTask(workflowTask.getId(), null);
        }
        
        workflowService.deleteWorkflow(workflowInstanceId);       
    }

    protected void setProcessVariables(String processInstanceId)
    {
        //Add variable wf_reviewOutcome 'Approve'
        runtimeService.setVariable(processInstanceId, "wf_reviewOutcome", "Approve");
    }

    protected void setStartProperties(DemoActivitiProcess demoProcess, WorkflowDefinition reviewDef, Map<QName, Serializable> properties)
    {
        NodeRef person = personService.getPerson(demoProcess.getUser());
        properties.put(WorkflowModel.ASSOC_ASSIGNEE, person);
        setupPackage(demoProcess, properties);
        
        properties.put(WorkflowModel.PROP_WORKFLOW_DUE_DATE, demoProcess.getDueTime().toDate());
        properties.put(WorkflowModel.PROP_PRIORITY, demoProcess.getPriority());
        properties.put(WorkflowModel.PROP_WORKFLOW_DESCRIPTION,reviewDef.getTitle()+" due on "+demoProcess.getDueTime().toLocalDate().toString()+" for "+demoProcess.getUser());
    }

    protected void setupPackage(DemoActivitiProcess demoProcess, Map<QName, Serializable> properties)
    {
        PackageManager pm = new PackageManager(workflowService, nodeService, behaviourFilter, logger);
        pm.addItems(demoProcess.getContentNodes());
        Serializable wfPackage = pm.create(null);
        properties.put(WorkflowModel.ASSOC_PACKAGE, wfPackage);
    }

    @Override
    public String getProcessDefinitionKey()
    {
        return this.processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey)
    {
        this.processDefinitionKey = processDefinitionKey;
    }
    
    @Override
    public void setRuntimeService(RuntimeService runtimeService)
    {
        this.runtimeService = runtimeService;
    }

    public void setWorkflowService(WorkflowService workflowService)
    {
        this.workflowService = workflowService;
    }

    public void setPersonService(PersonService personService)
    {
        this.personService = personService;
    }

    public void setBehaviourFilter(BehaviourFilter behaviourFilter)
    {
        this.behaviourFilter = behaviourFilter;
    }

    public void setNodeService(NodeService nodeService)
    {
        this.nodeService = nodeService;
    }

}
