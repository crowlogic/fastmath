package fastmath;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.DoubleByReference;

public interface MittagLefflerFunction extends
                                       Library
{
  static MittagLefflerFunction lib = (MittagLefflerFunction) Native.loadLibrary("mlf", MittagLefflerFunction.class);

  public double mlf(double alpha, double beta, double z);

}
