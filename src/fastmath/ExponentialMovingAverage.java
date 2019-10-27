/*
 * Decompiled with CFR 0.144.
 */
package fastmath;

public class ExponentialMovingAverage
{
  private double \u03b1;
  private double oldValue = Double.NaN;
  long lastTick;

  public ExponentialMovingAverage(double \u03b1)
  {
    this.\u03b1 = \u03b1;
  }

  public ExponentialMovingAverage(double \u03b1, double initialValue)
  {
    this.\u03b1 = \u03b1;
    this.oldValue = initialValue;
  }

  public double average(double value)
  {
    double newValue;
    if (Double.isNaN(this.oldValue))
    {
      this.oldValue = value;
      return value;
    }
    this.oldValue = newValue = this.oldValue + this.\u03b1 * (value - this.oldValue);
    return newValue;
  }

  public double tick()
  {
    long thisTick = System.currentTimeMillis();
    double avg = this.average(thisTick - this.lastTick);
    this.lastTick = thisTick;
    return avg;
  }
}
