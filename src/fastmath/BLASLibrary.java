package fastmath;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface BLASLibrary extends
                             Library
{
  public static final BLASLibrary instance = (BLASLibrary) Native.loadLibrary((String) "blas", BLASLibrary.class);

  public void daxpy_(int var1, double var2, Pointer var4, int var5, Pointer var6, int var7);
}
