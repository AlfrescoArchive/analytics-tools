
package org.alfresco.analytics.webscripts;

import org.alfresco.analytics.event.EventFactory;
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

    public void setFactory(EventFactory factory)
    {
        this.factory = factory;
    }

}
