package org.alfresco.repo.site;

import org.alfresco.service.cmr.site.SiteVisibility;

/**
 * Just allows me to create a SiteInfoImpl (because its got a package scoped constructor
 *
 * @author Gethin James
 */
public class TestSiteInfoImpl extends SiteInfoImpl
{

    public TestSiteInfoImpl(String sitePreset, String shortName, String title, String description,
                SiteVisibility visibility)
    {
        super(sitePreset, shortName, title, description, visibility, null);
    }

}
