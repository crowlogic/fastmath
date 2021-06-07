package util;

import java.util.Comparator;

import fastmath.Pair;

public class DoublePair extends
                        Pair<Double, Double>
{
  private static final long serialVersionUID = 1L;
  public static Comparator<DoublePair> compareLeft = (a, b) -> Double.compare((Double) a.left, (Double) b.left);
  public static Comparator<DoublePair> rightComparator = (a, b) -> Double.compare((Double) a.right,
                                                                                  (Double) b.right);

  @Override
  public String toString()
  {
    return String.format("[%f,%f]", this.left, this.right);
  }

  public DoublePair()
  {
  }

  public DoublePair(Double firstValue, Double secondValue)
  {
    super(firstValue,
          secondValue);
  }

  public double getLeftValue()
  {
    return (Double) this.left;
  }

  public double getRightValue()
  {
    return (Double) this.right;
  }
}
