package org.alfresco.analytics.core;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;


public interface Calculator
{

    public long[] distributeDates(LocalDate startDate, LocalDate endDate, int numberOfValues);
    public <T> List<T> distributeValues(List<T> values, int numberOfValues);
    public DateTime randomTime(DateTime startDate, DateTime endDate);
    int random(int lower, int upper);
}
