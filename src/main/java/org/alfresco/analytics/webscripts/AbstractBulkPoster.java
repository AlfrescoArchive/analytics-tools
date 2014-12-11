
package org.alfresco.analytics.webscripts;

import org.alfresco.analytics.event.EventFactory;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.cmr.security.PersonService.PersonInfo;
import org.alfresco.service.cmr.site.SiteInfo;
import org.alfresco.service.cmr.site.SiteService;
import org.alfresco.util.Pair;
import org.joda.time.LocalDate;
import org.springframework.extensions.webscripts.DeclarativeWebScript;

/**
 * Posts bulk events
 *
 * @author Gethin James
 */
public abstract class AbstractBulkPoster extends DeclarativeWebScript
{

    protected EventFactory factory;
    private PersonService personService;
    private SiteService siteService;

    protected int getNumberOfValues(String number)
    {
        int numberOfValues = 100; // default
        if (number != null) { return Integer.parseInt(number); }
        return numberOfValues;
    }

    /**
     * Parses ISO8601 formatted Date Strings.
     * 
     * @param start If start is null then defaults to 1 month
     * @param end If end is null then it defaults to now();
     * @return Pair <Start,End>
     */
    public static Pair<LocalDate, LocalDate> getStartAndEndDates(String start, String end)
    {
        LocalDate startDate;
        if (start == null)
        {
            startDate = LocalDate.now().minusMonths(6);
        }
        else
        {
            startDate = LocalDate.parse(start);
        }
        LocalDate endDate = end != null ? LocalDate.parse(end) : LocalDate.now();
        return new Pair<LocalDate, LocalDate>(startDate, endDate);
    }


    protected void updateSites(String[] sites, LocalDate when)
    {
        for (int i = 0; i < sites.length; i++)
        {
           SiteInfo info = siteService.getSite(sites[i]);
           factory.updateSite(info, when);
        }
    }
 
    protected void updateUsers(String[] users, LocalDate when)
    {
        for (int i = 0; i < users.length; i++)
        {
            PersonInfo info = personService.getPerson(personService.getPersonOrNull(users[i]));
            factory.updateUser(info, when);
        }
    }
    
    public void setFactory(EventFactory factory)
    {
        this.factory = factory;
    }

    public void setPersonService(PersonService personService)
    {
        this.personService = personService;
    }

    public void setSiteService(SiteService siteService)
    {
        this.siteService = siteService;
    }

}
