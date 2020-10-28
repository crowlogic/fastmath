package fastmath;

import com.sun.jna.ptr.DoubleByReference;

import junit.framework.TestCase;

public class MittagLefflerFunctionTest extends
                                       TestCase
{

  /**
   * fα,λ(t) ∼ αλΓ(1−α)t^(−α−1) as t →+∞
   * 
   * and
   * 
   * fα,λ(t) ∼ λΓ(α)*t^(α−1) as t →0+
   * 
   */
  public MittagLefflerFunctionTest()
  {
  }

  public static void testMLF()
  {
    double z = 29.2;
    double alpha = 4.5;
    double beta = 1.4;
    double result = MittagLefflerFunction.lib.mlf(alpha, beta, z);
    assertEquals(1.416345169927341, result, 0.00000001);

  }

}
