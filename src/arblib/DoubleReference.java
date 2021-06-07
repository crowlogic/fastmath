package arblib;

public final class DoubleReference extends
                                          ThreadLocal<double[]>
{
  @Override
  protected double[] initialValue()
  {
    return new double[1];
  }
}