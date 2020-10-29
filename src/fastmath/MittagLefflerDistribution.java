package fastmath;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

public class MittagLefflerDistribution
{
  public static void main(String[] args) throws Exception
  {
    MittagLefflerDistribution dist = new MittagLefflerDistribution(0.1, 0.9);
    double[] xData = new double[1000];
    double[] yData = new double[1000];  
    for (int i = 0; i < xData.length; i++)
    {
      double t;
      t = xData[i] = (double) i / xData.length;
      yData[i] =       dist.pdf(t);
    }

    // Create Chart
    XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);

    // Show it
    new SwingWrapper(chart).displayChart();

  }

  public MittagLefflerDistribution(double α, double λ)
  {
    super();
    assert 0 <= α && α <= 1;
    assert 0 <= λ && λ <= 1;
    this.α = α;
    this.λ = λ;
  }

  public double α;

  public double λ;

  /**
   * The Mittag-Leffler distribution has CDF is 1 – Eα(-xα) and so reduces to the
   * exponential distribution when α = 1. For 0 < α < 1, the Mittag-Leffler
   * distribution is a fat-tailed generalization of the exponential. [1
   * 
   * @param t
   * @return
   */
  public double pdf(double t)
  {
    if (α == 0)
    {
      return exp(-λ * t);
    }
    else
    {
      return λ * pow(t, α - 1) * MittagLefflerFunction.lib.mlf(α, α, -λ * pow(t, α));
    }
  }

  public double cdf(double t)
  {
    return 1 - MittagLefflerFunction.lib.mlf(α, 0, -λ * pow(t, α));
  }

}
