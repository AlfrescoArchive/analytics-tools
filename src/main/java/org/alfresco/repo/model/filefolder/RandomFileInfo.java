package org.alfresco.repo.model.filefolder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.ContentData;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.namespace.QName;

/**
 * Gets arounds FileInfoImpl package scope constructors 
 *
 * @author Gethin James
 */
public class RandomFileInfo extends FileInfoImpl
{
    private static final long serialVersionUID = -2204175749712897684L;

    public RandomFileInfo(
            NodeRef nodeRef,
            QName typeQName,
            boolean isFolder,
            boolean isHidden,
            Map<QName, Serializable> properties)
    {
        super(nodeRef, typeQName, isFolder, isHidden, properties);
    }

    public static FileInfo createMock (String nodeId, String name, QName typeQName, boolean isFolder)
    {

        ContentData contentData = new ContentData("abc://xxx", MimetypeMap.MIMETYPE_EXCEL, 600L, "UTF-8", Locale.ENGLISH);
        Map<QName, Serializable> properties = new HashMap<QName, Serializable>();
        properties.put(ContentModel.PROP_NAME, name);
        properties.put(ContentModel.PROP_CONTENT, contentData);
        return new FileInfoImpl(new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, nodeId), typeQName, isFolder, false, properties);
    }
}
