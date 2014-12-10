
package org.alfresco.analytics.webscripts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.repo.Client;
import org.alfresco.repo.Client.ClientType;
import org.alfresco.service.cmr.model.FileFolderService;
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
public class BulkEventPoster extends AbstractBulkPoster
{

    private static Log logger = LogFactory.getLog(BulkEventPoster.class);
    private FileFolderService fileService;
    
    @Override
    protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache)
    {
        Map<String, Object> model = new HashMap<String, Object>(2, 1.0f);
        
        Pair<LocalDate, LocalDate> startAndEnd = getStartAndEndDates(req.getParameter("startDate"),
                    req.getParameter("endDate"));
        int numberOfValues = getNumberOfValues(req.getParameter("numberOfValues"));
        List<String> nodes = getNodes(req.getParameter("content"));
        String[] activities = req.getParameterValues("events");
        String[] users = req.getParameterValues("people");
        String[] sites = req.getParameterValues("sites");
        List<Client> clients = getClientTypes(req.getParameterValues("clients"));

        factory.createBulkNodeActivityEvents(Arrays.asList(activities), Arrays.asList(users), Arrays.asList(sites), nodes, clients, startAndEnd
                    .getFirst().plusDays(1), startAndEnd.getSecond(), numberOfValues);
        model.put("resultSize", numberOfValues);
        model.put("from", startAndEnd.getFirst().toString());
        model.put("to", startAndEnd.getSecond().toString());
        return model;
    }

    private List<Client> getClientTypes(String[] clientTypes)
    {
        List<Client> clients = new ArrayList<Client>(clientTypes.length);
        for (int i = 0; i < clientTypes.length; i++)
        {
            clients.add(Client.asType(ClientType.valueOf(clientTypes[i])));
        }
        return clients;
    }

    protected List<String> getNodes(String noderef)
    {
        List<String> nodes = new ArrayList<String>();
        NodeRef nodeRef = new NodeRef(noderef);
        List<FileInfo> files = fileService.listFiles(nodeRef);
        for (FileInfo fileInfo : files)
        {
            nodes.add(fileInfo.getNodeRef().getId());
        }
        return nodes;
    }
    
    public void setFileService(FileFolderService fileService)
    {
        this.fileService = fileService;
    }


 }
