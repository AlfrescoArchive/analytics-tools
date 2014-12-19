
package org.alfresco.analytics.webscripts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.analytics.activiti.BulkActivitiManager;
import org.alfresco.analytics.activiti.DemoActivitiProcess.TaskState;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.util.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

/**
 * Posts bulk Activity Events
 *
 * @author Gethin James
 */
public class BulkActivitiPoster extends AbstractBulkPoster
{
    private BulkActivitiManager activitiManager;
    private static Log logger = LogFactory.getLog(BulkActivitiPoster.class);
    
    @Override
    protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache)
    {
        Map<String, Object> model = new HashMap<String, Object>(2, 1.0f);
        Pair<LocalDate, LocalDate> startAndEnd = getStartAndEndDates(req.getParameter("startDate"),
                    req.getParameter("endDate"));
        int numberOfValues = getNumberOfValues(req.getParameter("numberOfValues"));
        String[] users = req.getParameterValues("people");
        List<NodeRef> nodes = getNodeRefs(req.getParameter("content"));
        String[] processes = req.getParameterValues("processes");
        List<Integer> priorities = getPriorities(req.getParameterValues("priorities"));
        List<String> processDefinitions = makeGlobalIds(processes);
        List<TaskState> state = getStates(req.getParameterValues("state"));
        
        int processedCount = activitiManager.startProcesses(processDefinitions, Arrays.asList(users), nodes, priorities, startAndEnd.getFirst(), startAndEnd.getSecond(),  numberOfValues, state);
        
        model.put("requested", numberOfValues);
        model.put("resultSize", processedCount);
        model.put("from", startAndEnd.getFirst().toString());
        model.put("to", startAndEnd.getSecond().toString());
        return model;
    }

    private List<TaskState> getStates(String[] state)
    {
        List<TaskState> states = new ArrayList<TaskState>();
        for (int i = 0; i < state.length; i++)
        {
            states.add(TaskState.valueOf(state[i].toUpperCase()));
        }
        return states;
    }

    private List<Integer> getPriorities(String[] prior)
    {
        List<Integer> priorities = new ArrayList<Integer>();
        for (int i = 0; i < prior.length; i++)
        {
            priorities.add(Integer.parseInt(prior[i]));
        }
        return priorities;
    }

    private List<String> makeGlobalIds(String[] processes)
    {
        List<String> definitions = new ArrayList<String>();
        for (int i = 0; i < processes.length; i++)
        {
            definitions.add(activitiManager.makeGlobalId(processes[i]));
        }
        return definitions;
    }

    protected List<NodeRef> getNodeRefs(String noderef)
    {
        List<NodeRef> nodes = new ArrayList<NodeRef>();
        NodeRef nodeRef = new NodeRef(noderef);
        List<FileInfo> files = fileFolderService.listFiles(nodeRef);
        for (FileInfo fileInfo : files)
        {
            nodes.add(fileInfo.getNodeRef());
        }
        return nodes;
    }
    
    
    public void setActivitiManager(BulkActivitiManager activitiManager)
    {
        this.activitiManager = activitiManager;
    }

 }
