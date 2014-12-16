package org.alfresco.analytics.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;

/**
 * Calculator using Apache Commons Math
 *
 * @author Gethin James
 */
public class CommonsCalculator implements Calculator
{

    RandomDataGenerator randomHour = new RandomDataGenerator();
    RandomDataGenerator randomMinute = new RandomDataGenerator();
    RandomDataGenerator randomInterval = new RandomDataGenerator();
    
    @Override
    public long[] distributeDates(LocalDate startDate, LocalDate endDate, int numberOfValues)
    {
        DateTime startTime = startDate.toDateTimeAtStartOfDay();
        Duration duration = new Duration(startTime, endDate.toDateTimeAtStartOfDay());
        int days = duration.toStandardDays().getDays();
        if (days < 1) throw new NumberFormatException("End date must be at least two days after start date.");
        
        IntegerDistribution d = new UniformIntegerDistribution(0,days);
        int[] sample = d.sample(numberOfValues);
        long[] dates = new long[numberOfValues];
        for (int i = 0; i < sample.length; i++)
        {
            DateTime sampledTime = startTime.plusDays(sample[i]);
            sampledTime = sampledTime.plusHours(randomHour.nextInt(0, 23)).plusMinutes(randomMinute.nextInt(0, 59));
            dates[i] = sampledTime.getMillis();
        }
        return dates;
    }

    @Override
    public <T> List<T> distributeValues(List<T> values, int numberOfValues)
    {
        if (values.size()==1)
        {
            //There's only 1 value so lets just return it as a list.
            List<T> oneSizeFitsAll = new ArrayList<T>(numberOfValues);
            T val = values.get(0);
            for (int i = 0; i < numberOfValues; i++)
            {
                oneSizeFitsAll.add(val);
            }
            return oneSizeFitsAll;
        }
        
        IntegerDistribution d = new UniformIntegerDistribution(0,values.size()-1);
        int[] sample = d.sample(numberOfValues);
        List<T> toReturn = new ArrayList<T>(numberOfValues);
        for (int i = 0; i < sample.length; i++)
        {
            toReturn.add(values.get(sample[i]));
        }
        return toReturn;
    }

    @Override
    public DateTime randomTime(DateTime startTime, DateTime endTime)
    {
        Duration duration = new Duration(startTime,endTime);
        int minutes = duration.toStandardMinutes().getMinutes();
        if (minutes < 15) minutes += 15;
        return startTime.plusMinutes(randomInterval.nextInt(1, minutes));

    }

}
