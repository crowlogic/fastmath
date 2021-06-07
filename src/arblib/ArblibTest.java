package arblib;

import static arblib.Constants.*;
import static arblib.arblib.*;
import static java.lang.Math.floor;
import static java.lang.Math.min;

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

  public void testHardyZFunction()
  {
    
    acb_struct result = new acb_struct();
    acb_struct input = new acb_struct();
    acb_set_d(input, 13.2);
   

    acb_dirichlet_hardy_z(result, input, null, null, 1, 20);
    double res = arf_get_d(result.getReal().getMid(), ARF_RND_UP);
    assertEquals( -0.6699771881103516, res );
    
    // TODO: check that this is equal to ((-351261 * 2^-19) +/- (690458335 * 2^-45),
    // (0) +/- (0))=-.6699780149

  }

}
