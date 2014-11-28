package org.alfresco.analytics.event;

import java.util.Arrays;
import java.util.List;

import org.alfresco.events.enrichers.EnricherHelper;
import org.alfresco.events.types.ActivityEvent;
import org.alfresco.events.types.Event;
import org.alfresco.repo.Client;
import org.alfresco.repo.events.EventPreparator;
import org.alfresco.repo.events.EventPublisher;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.ContentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Helps create/publish events
 *
 * @author Gethin James
 */
@Component
public class EventHelper
{
    @Autowired
    private EventPublisher eventPublisher;
    
    @Autowired
    private EnricherHelper enricherHelper;
    
    /**
     * Publish an activity
     * @param activityType
     * @param username
     * @param siteId
     * @param nodeId
     * @param client
     * @param timestamp
     * @param activityData
     */
    public void publishActivity(String activityType, String username, String siteId, String nodeId, Client client, long timestamp, String activityData)
    {
        FileInfo fileInfo = null;
        if (nodeId != null) fileInfo = enricherHelper.getFileInfo(nodeId);
        
        publishActivity(activityType, username, siteId, nodeId, client, timestamp, activityData, fileInfo);
    }
    
    /**
     * Publish an activity 
     * @param activityType
     * @param username
     * @param siteId
     * @param nodeId
     * @param client
     * @param timestamp
     * @param activityData
     * @param fileInfo
     */
    public void publishActivity(final String activityType, final String username, final String siteId, 
                                final String nodeId, final Client client, final long timestamp,
                                final String activityData, final FileInfo fileInfo)
    {
        eventPublisher.publishEvent(new EventPreparator(){
            
            @Override
            public Event prepareEvent(String user, String networkId, String transactionId)
            {   
                String filename = null, nodeType = null, mime = null, encoding = null;
                long size = 0l;
                
                //Use content info
                if (fileInfo != null)
                {
                    filename = fileInfo.getName();
                    nodeType = fileInfo.getType().toString();
                    
                    if (!fileInfo.isFolder())
                    {
                        //It's a file so get more info
                        ContentData contentData = fileInfo.getContentData();
                        if (contentData!=null)
                        {
                            mime = contentData.getMimetype();
                            size = contentData.getSize();
                            encoding = contentData.getEncoding();
                        }
                    }

                }
                
                return new ActivityEvent(activityType, transactionId, networkId, timestamp, username, 
                            nodeId, siteId, nodeType, client,
                            activityData, filename, mime, size, encoding);             
            }
        });
    }

    public static List<String> getFolderActivities()
    {
        return Arrays.asList("org.alfreschelpero.documentlibrary.folder-added",
                    "org.alfresco.documentlibrary.folder-deleted",
                    "org.alfresco.documentlibrary.folder-liked");
    }
    
    public static List<String> getFileActivities()
    {
        return Arrays.asList("org.alfresco.comments.comment-created",
                    "org.alfresco.comments.comment-updated",
                    "org.alfresco.comments.comment-deleted",
                    "org.alfresco.documentlibrary.file-added",
                    "org.alfresco.documentlibrary.file-created",
                    "org.alfresco.documentlibrary.file-deleted",
                    "org.alfresco.documentlibrary.file-updated",
                    "org.alfresco.documentlibrary.inline-edit",
                    "org.alfresco.documentlibrary.file-previewed",
                    "org.alfresco.documentlibrary.file-downloaded",
                    "org.alfresco.documentlibrary.file-liked");
    }
}
