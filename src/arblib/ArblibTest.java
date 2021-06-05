package arblib;

import static arblib.Constants.ARF_RND_DOWN;
import static arblib.arblib.acb_add_ui;
import static arblib.arblib.acb_dirichlet_hardy_z;
import static arblib.arblib.acb_init;
import static arblib.arblib.acb_log;
import static arblib.arblib.acb_pow_ui;
import static arblib.arblib.acb_print;
import static arblib.arblib.acb_rel_accuracy_bits;
import static arblib.arblib.acb_set_d;
import static arblib.arblib.acb_tanh;
import static arblib.arblib.arb_get_str;
import static arblib.arblib.arf_add;
import static arblib.arblib.arf_div_ui;
import static arblib.arblib.arf_init;
import static arblib.arblib.arf_mul_ui;
import static arblib.arblib.arf_set_d;
import static arblib.arblib.arf_sub;
import static java.lang.Math.floor;
import static java.lang.Math.min;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import junit.framework.TestCase;

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

}
