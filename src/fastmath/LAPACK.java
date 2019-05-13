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

public interface LAPACK
extends Library {
    public static final LAPACK instance = (LAPACK)Native.loadLibrary((String)"lapack", LAPACK.class);

    public void ilaenv_(Pointer var1, Pointer var2, Pointer var3, Pointer var4, Pointer var5, Pointer var6, Pointer var7);

    public void dgetrf_(Pointer var1, Pointer var2, Pointer var3, Pointer var4, Pointer var5, Pointer var6);

    public void dgetri_(Pointer var1, Pointer var2, Pointer var3, Pointer var4, Pointer var5, Pointer var6, Pointer var7);

    public void dgemm_(Pointer var1, Pointer var2, Pointer var3, Pointer var4, Pointer var5, Pointer var6, Pointer var7, Pointer var8, Pointer var9, Pointer var10, Pointer var11, Pointer var12, Pointer var13);
}

