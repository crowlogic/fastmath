/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Library
 *  com.sun.jna.Native
 *  com.sun.jna.Pointer
 */
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
