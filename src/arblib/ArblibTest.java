package arblib;

import static arblib.Constants.*;
import static arblib.arblib.*;
import static java.lang.Math.floor;
import static java.lang.Math.min;
import static java.lang.Math.pow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.*;
import junit.framework.TestCase;

public class ArblibTest extends
                        TestCase
{
  static
  {
    System.loadLibrary("arblib");
  }

  public static final int prec = 128;

  public void testHardyZFunction()
  {

    acb_struct result = new acb_struct();
    acb_struct input = new acb_struct();
    acb_init(input);
    acb_init(result);
    acb_set_d(input, 13.2);

    acb_dirichlet_hardy_z(result, input, null, null, 1, prec);
    double res = point(result.getReal());
    assertEquals(-0.6699771881103516, res, 0.000001);

    // TODO: check that this is equal to ((-351261 * 2^-19) +/- (690458335 * 2^-45),
    // (0) +/- (0))=-.6699780149

  }

  public void testHardyTheta()
  {
    acb_struct z = new acb_struct();
    acb_struct faze = new acb_struct();
    acb_init(z);
    acb_init(faze);

    acb_set_d(z, 13.2);
    acb_dirichlet_hardy_theta(faze, z, null, null, 1, prec);
    double result = point(faze.getReal());
    out.println("theta(" + point(z.getReal()) + ")=" + result);
    assertEquals(-2.0916778325102459547, result);
    out.println("e^(" + point(faze) + ")=");
    faze.setImag(faze.getReal());
    arb_set_d(faze.getReal(), 0);
    acb_exp(faze, faze, prec);
    out.println(point(faze) + " should be\n -.497644932260514 + I-0.867380839882591 ");
    assertEquals( -0.497644932260514, point( faze.getReal() ), pow(10,-12) );
    assertEquals( -0.867380839882591, point( faze.getImag() ), pow(10,-12) );
    // -.497644932260514-.867380839882591*I

  }

  public static String point(acb_struct faze)
  {
    return point(faze.getReal()) + " + I" + point(faze.getImag());
  }

  public static double point(arb_struct faze)
  {
    return arf_get_d(faze.getMid(), prec);
  }

}
