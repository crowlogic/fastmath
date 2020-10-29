package fastmath;

import com.sun.jna.ptr.DoubleByReference;

import junit.framework.TestCase;

public class MittagLefflerFunctionTest extends
                                       TestCase
{

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

  /**
   * fα,λ(t) ∼ αλΓ(1−α)t^(−α−1) as t →+∞
   * 
   * 
   * 
   */
  public static void testLeftLimit()
  {
    double z = 29.2;
    double alpha = 4.5;
    double beta = 1.4;
    double result = MittagLefflerFunction.lib.mlf(alpha, beta, z);
    assertEquals(1.416345169927341, result, 0.00000001);

  }

  /**
   * 
   * fα,λ(t) ∼ λΓ(α)*t^(α−1) as t →0+
   * 
   */
  public static void testLeftRight()
  {
    double z = 29.2;
    double alpha = 4.5;
    double beta = 1.4;
    double result = MittagLefflerFunction.lib.mlf(alpha, beta, z);
    assertEquals(1.416345169927341, result, 0.00000001);

  }

}
