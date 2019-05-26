/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  junit.framework.TestCase
 */
package util;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

public class DateUtilsTest
extends TestCase {
    public static final double millisecondsPerHour = TimeUnit.MILLISECONDS.convert(1L, TimeUnit.HOURS);

    public void testConvertFractionalTime() {
        double millis = 300.0;
        double h = DateUtils.convertTimeUnits(millis, TimeUnit.MILLISECONDS, TimeUnit.HOURS);
        double j = h * millisecondsPerHour;
        DateUtilsTest.assertEquals((Object)millis, (Object)j);
        DateUtilsTest.assertEquals((Object)3600.0, (Object)DateUtils.convertTimeUnits(1.0, TimeUnit.HOURS, TimeUnit.SECONDS));
        DateUtilsTest.assertEquals((Object)3600000.0, (Object)DateUtils.convertTimeUnits(1.0, TimeUnit.HOURS, TimeUnit.MILLISECONDS));
        DateUtilsTest.assertEquals((Object)2.777777777777778E-4, (Object)DateUtils.convertTimeUnits(1.0, TimeUnit.SECONDS, TimeUnit.HOURS));
    }

    public void testConversionBetweenMicrosecondsAndHours() {
        long midnight = System.currentTimeMillis();
        midnight -= midnight % 86400000L;
    }

    protected long getTimeToday(int hour, int minute) {
        Calendar cal = Calendar.getInstance(DateUtils.EAST_COAST_TIME);
        cal.set(11, hour);
        cal.set(12, minute);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime().getTime() * 1000L;
    }
}

