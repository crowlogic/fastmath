package fastmath;

import com.sun.jna.Library;

public interface MittagLefflerFunction extends
                                       Library
{
  public Complex mlf(double alpha, double beta, Complex z);

}
