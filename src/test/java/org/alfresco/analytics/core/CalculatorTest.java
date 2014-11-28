package org.alfresco.analytics.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;

/**
 * Just exercises the distribute method
 *
 * @author Gethin James
 */
public class CalculatorTest
{

    Calculator calc = new CommonsCalculator();

    @Test(expected=NumberFormatException.class)
    public void testDistributeSameDay()
    {
        long[] distribution = calc.distributeDates(LocalDate.parse("2014-09-01"), LocalDate.parse("2014-09-01"), 5);
    }
    
    @Test(expected=NumberFormatException.class)
    public void testDistributeLessThan()
    {
        long[] distribution = calc.distributeDates(LocalDate.parse("2014-09-01"), LocalDate.parse("2014-08-01"), 5);
    }    
    
    @Test
    public void testDistributeDates()
    {
        long[] distribution = calc.distributeDates(LocalDate.parse("2014-09-01"), LocalDate.parse("2014-10-01"), 15);
        assertEquals(15, distribution.length);
        
        distribution = calc.distributeDates(LocalDate.parse("2014-09-01"), LocalDate.parse("2014-09-02"), 5);
        assertEquals(5, distribution.length);
        for (int i = 0; i < distribution.length; i++)
        {
            System.out.println("D: "+new Date(distribution[i]));
        }
    }
    
    @Test
    public void testDistributeValues()
    {
        List<String> source = Arrays.asList("Red", "Green", "Blue");
        List<String> distribution = calc.distributeValues(source, 10);
        assertEquals(10, distribution.size());
        for (String dist : distribution)
        {
            assertTrue(source.contains(dist));
        }
        System.out.println(distribution);
    }

}
