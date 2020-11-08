/*
 * Decompiled with CFR 0.144.
 */
package util;

import static java.time.LocalDateTime.now;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import fastmath.Vector;

public class DateUtils
{
  static final ThreadLocalDateFormat millisecondTimeFormat = new ThreadLocalDateFormat("HH:mm:ss.SSS");
  public static final long MS_IN_DAY = 86400000L;
  public static final TimeZone EAST_COAST_TIME = TimeZone.getTimeZone("EST5EDT");
  public static final long MICROS_IN_DAY = 86400000000L;
  public static final List<Integer> WEEKDAY = Arrays.asList(2, 3, 4, 5, 6);

  public static long getTimeZoneOffset(long unixTime)
  {
    int offset = EAST_COAST_TIME.getOffset(unixTime);
    return TimeUnit.HOURS.convert(offset, TimeUnit.MILLISECONDS);
  }

  public static boolean isOnSameDay(Date date1, Date date2)
  {
    GregorianCalendar cal1 = new GregorianCalendar();
    cal1.setTime(date1);
    GregorianCalendar cal2 = new GregorianCalendar();
    cal2.setTime(date2);
    return cal1.get(1) == cal2.get(1) && cal1.get(2) == cal2.get(2) && cal1.get(5) == cal2.get(5);
  }

  protected static double getSecondsSinceMidnight(long unixTime)
  {
    unixTime -= (long) EAST_COAST_TIME.getOffset(TimeUnit.MILLISECONDS.convert(unixTime, TimeUnit.MICROSECONDS));
    return unixTime / TimeUnit.MICROSECONDS.convert(1L, TimeUnit.SECONDS)
                  % TimeUnit.SECONDS.convert(24L, TimeUnit.HOURS);
  }

  public static int getDayOfWeek()
  {
    return Calendar.getInstance().get(7);
  }

  public static boolean isWeekDay(int dayOfWeek)
  {
    return WEEKDAY.contains(dayOfWeek);
  }

  public static boolean isMarketOpenToday()
  {
    return DateUtils.isWeekDay(DateUtils.getDayOfWeek());
  }

  public static double convertToHours(TimeUnit fromUnit, double from)
  {
    double ratio = fromUnit.convert(1L, TimeUnit.HOURS);
    return from / ratio;
  }

  public static double convertTimeUnits(double from, TimeUnit fromUnit, TimeUnit toUnit)
  {
    double ratio = fromUnit.convert(1L, toUnit);
    if (!Double.isFinite(ratio) || ratio == 0.0)
    {
      ratio = toUnit.convert(1L, fromUnit);
      return from * ratio;
    }
    return from / ratio;
  }

  public static Vector convertTimeUnits(Vector w, TimeUnit from, TimeUnit to)
  {
    Vector x = new Vector(w.size()).setName(w.getName());
    for (int i = 0; i < x.size(); i++)
    {
      x.set(i, convertTimeUnits(w.get(i), from, to));
    }
    return x;
  }

  public static double daysUntil(LocalDateTime later)
  {
    return daysBetween(now(), later);
  }

  public static double daysBetween(LocalDateTime now, LocalDateTime later)
  {
    return convertTimeUnits(now.until(later, ChronoUnit.NANOS), TimeUnit.NANOSECONDS, TimeUnit.DAYS);
  }
}
