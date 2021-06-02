package arblib;

import junit.framework.TestCase;

public class ArblibTest extends
                        TestCase
{
  public void testHardyZFunction()
  {
    System.loadLibrary("arblib");

    acb_struct result = new acb_struct();
    acb_struct input = new acb_struct();
    arblib.acb_set_d(input, 13.2);
    System.err.println("Input");
    arblib.acb_print(input);
    System.err.println();
    System.err.flush();
    // TODO: check that this is equal to ((3715469692580659 * 2^-48) +/- (0), (0)
    // +/- (0))=13.2

    arblib.acb_dirichlet_hardy_z(result, input, null, null, 1, 20);
    System.err.println("Result");
    arblib.acb_print(result);
    System.err.println();
    System.err.flush();
    // TODO: check that this is equal to ((-351261 * 2^-19) +/- (690458335 * 2^-45),
    // (0) +/- (0))=-.6699780149

  }
}
