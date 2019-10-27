/*
 * Decompiled with CFR 0.144.
 */
package fastmath.io;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public interface UnsignedDataInput
{
  public InputStream getInputStream();

  public BigInteger readUnsignedLong() throws IOException;

  public double[] readDoubles(int var1) throws IOException;

  public long readUnsignedInt() throws IOException;
}
