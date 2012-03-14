package com.chinarewards.qq.meishi;

import java.util.TimeZone;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	String timeZones[] = TimeZone.getAvailableIDs();
    	for (String tzs: timeZones) {
    		TimeZone tz = TimeZone.getTimeZone(tzs);
    		
    		System.out.println(tz.getDisplayName() + "(" + tzs + "): " + (double)tz.getRawOffset() / 1000 / 3600);
    	}
    }
}
