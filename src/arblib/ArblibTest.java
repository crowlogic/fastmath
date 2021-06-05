package arblib;

import junit.framework.TestCase;
import static arblib.arblib.*;

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

  public void testComplexPlot()
  {
    long x, y, xnum, ynum, prec, i;
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
  }
}
