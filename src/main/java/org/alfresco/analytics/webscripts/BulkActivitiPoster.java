
package org.alfresco.analytics.webscripts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.analytics.activiti.BulkActivitiManager;
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
        int numberOfValues = getNumberOfValues(req.getParameter("count"));

        List<String> users = Arrays.asList("ken","barbie");
        //, "activiti$activitiReviewPooled"
        List<String> definitions = Arrays.asList("activiti$activitiAdhoc","activiti$activitiReview");
     
        int processedCount = activitiManager.startProcesses(definitions, users, startAndEnd.getFirst(), startAndEnd.getSecond(),  numberOfValues);
        
        model.put("requested", numberOfValues);
        model.put("resultSize", processedCount);
        model.put("from", startAndEnd.getFirst().toString());
        model.put("to", startAndEnd.getSecond().toString());
        return model;
    }

    public void setActivitiManager(BulkActivitiManager activitiManager)
    {
        this.activitiManager = activitiManager;
    }

 }
