package org.alfresco.analytics.activiti.demo.executor;

import org.activiti.engine.RuntimeService;
import org.alfresco.analytics.activiti.DemoActivitiProcess;

/**
 * Tries to execute an Activiti process for demo purposes
 *
 * @author Gethin James
 */
public interface DemoProcessExecutor
{
    String getProcessDefinitionKey();
    void executeDemoProcess(DemoActivitiProcess demoProcess, boolean complete);
    void setRuntimeService(RuntimeService runtimeService);
    
    static String ADHOC_WORKFLOW_DEFINITION_NAME = "activiti$activitiAdhoc";
    static String REVIEW_WORKFLOW_DEFINITION_NAME = "activiti$activitiReview";
    static String REVIEW_POOLED_WORKFLOW_DEFINITION_NAME = "activiti$activitiReviewPooled";
}
