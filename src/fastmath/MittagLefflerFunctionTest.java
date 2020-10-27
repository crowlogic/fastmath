package fastmath;

import com.sun.jna.ptr.DoubleByReference;

import junit.framework.TestCase;

public class MittagLefflerFunctionTest extends
                                       TestCase
{

  public MittagLefflerFunctionTest()
  {
    // TODO Auto-generated constructor stub
  }

  public static void testMLF()
  {
    Complex z = new Complex(29.2,
                            50);

    double alpha = 4.5;
    double beta = 1.4;
    Complex result = MittagLefflerFunction.lib.mlf(alpha, beta, z );
    System.out.println("z=" + z);
    System.out.println("result=" + result);
    assertEquals( 1.4135659887023517, result.realPart, 0.00000000001 );
    assertEquals( 0.49697099521531124, result.imaginaryPart, 0.00000000001 );
    
  }

}
