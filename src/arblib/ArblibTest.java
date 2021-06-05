package arblib;

import junit.framework.TestCase;
import static arblib.arblib.*;
import static arblib.Constants.*;
import static java.lang.Math.*;

public class ArblibTest extends
                        TestCase
{
  static
  {
    System.loadLibrary("arblib");
  }

  public void testHardyZFunction()
  {

    acb_struct result = new acb_struct();
    acb_struct input = new acb_struct();
    acb_set_d(input, 13.2);
    acb_print(input);
    String inputStr = arb_get_str(input.getReal(), 20, 0);
    System.err.println("Input: " + inputStr);

    System.err.println();
    System.err.flush();
    // TODO: check that this is equal to ((3715469692580659 * 2^-48) +/- (0), (0)
    // +/- (0))=13.2

    acb_dirichlet_hardy_z(result, input, null, null, 1, 20);
    System.err.println("Result :" + arb_get_str(result.getReal(), 20, 0));
    acb_print(result);
    System.err.println();
    System.err.flush();
    System.err.println("Input: " + inputStr);

    // TODO: check that this is equal to ((-351261 * 2^-19) +/- (690458335 * 2^-45),
    // (0) +/- (0))=-.6699780149

  }

  /**
   * tanh(ln(1+Z(t)^2))
   */
  void Y(acb_struct res, acb_struct z, int prec)
  {
    acb_dirichlet_hardy_z(res, z, null, null, 1, prec);
    acb_pow_ui(res, res, 2, prec);
    acb_add_ui(res, res, 1, prec);
    acb_log(res, res, prec);
    acb_tanh(res, res, prec);
  }

  public void testComplexPlot()
  {
    int prec = 256;
    long x, y, xnum, ynum, i;
    double R, G, B, dxa, dxb, dya, dyb;

    dxa = 2;
    dxb = 2;
    dya = 2;
    dyb = 2;

    acb_struct z = new acb_struct();
    acb_struct w = new acb_struct();
    arf_struct xa = new arf_struct();
    arf_struct xb = new arf_struct();
    arf_struct ya = new arf_struct();
    arf_struct yb = new arf_struct();

    acb_init(z);
    acb_init(w);

    arf_init(xa);
    arf_init(xb);
    arf_init(ya);
    arf_init(yb);

    arf_set_d(xa, dxa);
    arf_set_d(xb, dxb);
    arf_set_d(ya, dya);
    arf_set_d(yb, dyb);

    for (y = ynum - 1; y >= 0; y--)
    {
      if (y % (ynum / 16) == 0)
        System.out.printf("row %ld\n", y);

      for (x = 0; x < xnum; x++)
      {
        for (prec = 30; prec < 500; prec *= 2)
        {
          arf_sub(arb_midref(acb_imagref(z)), yb, ya, prec, ARF_RND_DOWN);
          arf_mul_ui(arb_midref(acb_imagref(z)), arb_midref(acb_imagref(z)), y, prec, ARF_RND_DOWN);
          arf_div_ui(arb_midref(acb_imagref(z)), arb_midref(acb_imagref(z)), ynum - 1, prec, ARF_RND_DOWN);
          arf_add(arb_midref(acb_imagref(z)), arb_midref(acb_imagref(z)), ya, prec, ARF_RND_DOWN);

          arf_sub(arb_midref(acb_realref(z)), xb, xa, prec, ARF_RND_DOWN);
          arf_mul_ui(arb_midref(acb_realref(z)), arb_midref(acb_realref(z)), x, prec, ARF_RND_DOWN);
          arf_div_ui(arb_midref(acb_realref(z)), arb_midref(acb_realref(z)), xnum - 1, prec, ARF_RND_DOWN);

          arf_add(arb_midref(acb_realref(z)), arb_midref(acb_realref(z)), xa, prec, ARF_RND_DOWN);

          func(w, z, prec);

          if (acb_rel_accuracy_bits(w) > 4)
            break;
        }

        // color_function(&R, &G, &B, w, color_mode);

        int red = (int) min(255, floor(R * 255));
        int green = (int) min(255, floor(R * 255));
        int blue = (int) min(255, floor(R * 255));
        // put to file, 0 to 255 representing intensity
      }
    }
  }

  public static arf_struct arb_midref(arb_struct acb_imagref)
  {
    return acb_imagref.getMid();
  }

  public static arb_struct acb_realref(acb_struct z)
  {
    return z.getReal();
  }

  public static arb_struct acb_imagref(acb_struct z)
  {
    return z.getImag();
  }
}
