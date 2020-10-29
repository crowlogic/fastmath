package fastmath;

import static java.lang.System.out;

import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;

import junit.framework.TestCase;

public class MittagLefflerDistributionTest extends
                                           TestCase
{

  public static void testDensity()
  {
    MittagLefflerDistribution dist = new MittagLefflerDistribution(0.1, 0.8);
    double cdf01 = dist.cdf(0.1);
    double cdf1 = dist.cdf(1);
    double cdf10 = dist.cdf(10);
    out.println( "cdf001=" + cdf01 );
    out.println( "cdf1=" + cdf1 );
    out.println( "cdf10=" + cdf10 );
    
    UnivariateIntegrator integrator = new SimpsonIntegrator();
    double integral = integrator.integrate(1000000, dist::pdf, 0, 1);
    System.out.println( "integral=" + integral );
  }
}
