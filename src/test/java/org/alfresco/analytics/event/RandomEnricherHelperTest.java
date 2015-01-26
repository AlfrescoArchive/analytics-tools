/*
 * Copyright (C) 2005-2015 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */

package org.alfresco.analytics.event;

import static org.junit.Assert.*;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.ContentData;
import org.junit.Test;

public class RandomEnricherHelperTest
{
    RandomEnricherHelper helper = new RandomEnricherHelper();

    @Test
    public void testGetFileInfo()
    {
        FileInfo fileInfo = helper.getFileInfo("qwertyuiopkljhgfdsTHIS");
        assertNotNull(fileInfo);
        assertNotNull(fileInfo.getName());
        assertEquals(ContentModel.PROP_CONTENT, fileInfo.getType());       
        assertTrue (!fileInfo.isFolder());
             
        //It's a file so get more info
        ContentData contentData = fileInfo.getContentData();
        assertNotNull(contentData);
        assertNotNull(contentData.getMimetype());
        assertNotNull(contentData.getSize());
        assertNotNull(contentData.getEncoding());     
    }

}
