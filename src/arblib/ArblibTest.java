package arblib;

import junit.framework.TestCase;

public class ArblibTest extends
                        TestCase
{
  public void testHardyZFunction()
  {
    acb_struct result = new acb_struct();
    acb_struct input = new acb_struct();
    arblib.acb_set_d(input, 13.2);
    System.out.println("Input");
    arblib.acb_print(input);
    arblib.acb_dirichlet_hardy_z(result, input, null, null, 1, 20);
    System.out.println("Result");
    arblib.acb_print(result);
  }
}
