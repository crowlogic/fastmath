package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import fastmath.Pair;

public class ThreadLocalDateFormat extends
                                   ThreadLocal<Pair<Date, DateFormat>>
{
  private final String formatStr;
  public static final ThreadLocalDateFormat ymdFormat = new ThreadLocalDateFormat("yyyyMMdd");

  public static final ThreadLocalDateFormat dFormat = new ThreadLocalDateFormat("yyyy-MM-dd");
  public static final ThreadLocalDateFormat sFormat = new ThreadLocalDateFormat("yyyy-MM-dd kk:mm:ss");
  public static final ThreadLocalDateFormat msFormat = new ThreadLocalDateFormat("yyyy-MM-dd kk:mm:ss.SSS");
  public static final ThreadLocalDateFormat hmsFormat = new ThreadLocalDateFormat("kk:mm:ss");
  public static final ThreadLocalDateFormat hmsMillisFormat = new ThreadLocalDateFormat("kk:mm:ss.SSS");

  public static final String formatD(long time)
  {
    return dFormat.format(time);
  }

  public static final String formatD(Date time)
  {
    return dFormat.format(time);
  }

  public static final String formatS(long time)
  {
    return sFormat.format(time);
  }

  public static final String formatS(Date time)
  {
    return sFormat.format(time);
  }

  public static final String formatMS(long time)
  {
    return msFormat.format(time);
  }

  public static final String formatMS(Date time)
  {
    return msFormat.format(time);
  }

  public static final String formatHmsMillis(long l)
  {
    return hmsMillisFormat.format(l);
  }
  
  public ThreadLocalDateFormat(String format)
  {
    this.formatStr = format;
  }

  @Override
  protected Pair<Date, DateFormat> initialValue()
  {
    return new Pair<Date, DateFormat>(new Date(),
                                      new SimpleDateFormat(this.formatStr));
  }

  public String format(Date date)
  {
    return ((DateFormat) ((Pair<?, ?>) this.get()).right).format(date);
  }

  public String format(long time)
  {
    Pair<?, ?> pair = (Pair<?, ?>) this.get();
    ((Date) pair.left).setTime(time);
    return ((DateFormat) pair.right).format((Date) pair.left);
  }

  public Date parse(String timeString) throws ParseException
  {
    return ((DateFormat) ((Pair<?, ?>) this.get()).right).parse(timeString);
  }

  public static String formatNanos(double time)
  {
    long nanos = (long) (( time - Math.floor(time) ) * 1000000000.0);
    
    Instant instant = Instant.ofEpochMilli((long) time).plus(nanos, ChronoUnit.NANOS );
    return instant.toString();
  }
}
