
package org.alfresco.analytics.webscripts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.analytics.event.EventFactory;
import org.alfresco.analytics.event.EventHelper;
import org.alfresco.repo.Client;
import org.alfresco.repo.Client.ClientType;
import org.alfresco.util.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

/**
 * Posts bulk Activity Events
 *
 * @author Gethin James
 */
public class BulkEventPoster extends DeclarativeWebScript
{

    private EventFactory factory;

    private static Log logger = LogFactory.getLog(BulkEventPoster.class);

    @Override
    protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache)
    {
        Map<String, Object> model = new HashMap<String, Object>(2, 1.0f);
        Pair<LocalDate, LocalDate> startAndEnd = getStartAndEndDates(req.getParameter("startDate"),
                    req.getParameter("endDate"));
        int numberOfValues = getNumberOfValues(req.getParameter("count"));

        List<String> activities = EventHelper.getFileActivities();
        List<String> users = Arrays.asList("ken", "barbie", "bob", "fred");
        List<String> sites = Arrays.asList("clothes", "food", "gadgets");
        List<String> nodes = Arrays.asList("c66a1277-35f9-47fb-90a9-411c3bfc896a",
                    "65bd55c9-9da1-425a-b62b-506ec8f23448", "dbd0bc92-0801-4b5c-8cf8-c4c549d8f1e9",
                    "c66a1277-35f9-47fb-90a9-411c3bfc896a");
        List<Client> clients = Arrays.asList(Client.asType(ClientType.webclient),
                    Client.asType(ClientType.cmis));

        factory.createBulkNodeActivityEvents(activities, users, sites, nodes, clients, startAndEnd
                    .getFirst().plusDays(1), startAndEnd.getSecond(), numberOfValues);
        model.put("resultSize", numberOfValues);
        model.put("from", startAndEnd.getFirst().toString());
        model.put("to", startAndEnd.getSecond().toString());
        return model;
    }

    private int getNumberOfValues(String number)
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

    public void setFactory(EventFactory factory)
    {
        this.factory = factory;
    }

}
