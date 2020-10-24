package fastmath;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.DoubleByReference;

public interface MittagLefflerFunction extends
                                       Library
{
  MittagLefflerFunction lib = (MittagLefflerFunction) Native.loadLibrary("mlf", MittagLefflerFunction.class);

  public ComplexByValue mlf(DoubleByReference alpha, DoubleByReference beta, Complex z);

}
