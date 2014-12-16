package org.alfresco.analytics.activiti.demo.executor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.alfresco.analytics.activiti.DemoActivitiProcess;
import org.alfresco.repo.workflow.WorkflowModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.workflow.WorkflowDefinition;
import org.alfresco.service.namespace.QName;

/**
 * Executes the pooled review process
 *
 * @author Gethin James
 */
public class PooledReviewProcessExecutor extends ReviewProcessExecutor
{

    protected AuthorityService authorityService;
    
    @Override
    protected void setStartProperties(DemoActivitiProcess demoProcess,
                WorkflowDefinition reviewDef, Map<QName, Serializable> properties)
    {
        super.setStartProperties(demoProcess, reviewDef, properties);
        NodeRef auth = authorityService.getAuthorityNodeRef("GROUP_ANALYTICS_DEMO_USERS");
        properties.put(WorkflowModel.ASSOC_GROUP_ASSIGNEE, auth);
        Collection<NodeRef> assignees = Arrays.asList(auth);
        properties.put(WorkflowModel.ASSOC_GROUP_ASSIGNEES, (Serializable) assignees);
    }

    public void setAuthorityService(AuthorityService authorityService)
    {
        this.authorityService = authorityService;
    }

}
