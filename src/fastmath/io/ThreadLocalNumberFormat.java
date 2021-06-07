package fastmath.io;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ThreadLocalNumberFormat extends
                                     ThreadLocal<NumberFormat>
{
  public static ThreadLocalNumberFormat instance = new ThreadLocalNumberFormat(new DecimalFormat());
  private NumberFormat format;

  @Override
  public void set(NumberFormat value)
  {
    this.format = value;
    super.set(value);
  }

  public ThreadLocalNumberFormat getInstance()
  {
    return instance;
  }

  protected ThreadLocalNumberFormat(NumberFormat format)
  {
    this.format = format;
  }

  @Override
  protected NumberFormat initialValue()
  {
    return this.format;
  }

  public static NumberFormat getFormat()
  {
    return (NumberFormat) instance.get();
  }
}
