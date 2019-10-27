/*
 * Decompiled with CFR 0.144.
 */
package fastmath.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class BigEndianInputStream extends
                                  DataInputStream implements
                                  UnsignedDataInput
{
  byte[] b = new byte[9];
  public static final long UNSIGNED_INT_MAX_VALUE = 0xFFFFFFFFL;
  public static final BigInteger UNSIGNED_LONG_MAX_VALUE = new BigInteger("18446744073709551615");
  public static final int UNSIGNED_SHORT_MAX_VALUE = 65535;

  @Override
  public InputStream getInputStream()
  {
    return this;
  }

  public BigEndianInputStream(InputStream in)
  {
    super(in);
  }

  @Override
  public long readUnsignedInt() throws IOException
  {
    long b3;
    long b2;
    long b1;
    long v = this.readInt();
    long b4 = (v & 0xFF000000L) >> 24;
    long newLong = (b4 << 24) + ((b3 = (v & 0xFF0000L) >> 16) << 16) + ((b2 = (v & 65280L) >> 8) << 8)
                  + (b1 = v & 255L);
    if (newLong < 0L || newLong > 0xFFFFFFFFL)
    {
      throw new RuntimeException("postcondition failed");
    }
    return newLong;
  }

  @Override
  public BigInteger readUnsignedLong() throws IOException
  {
    this.b[0] = 0;
    this.readFully(this.b, 1, 8);
    BufferTools.reverseBytes(this.b, 1, 8);
    BigInteger bi = new BigInteger(this.b);
    if (bi.compareTo(UNSIGNED_LONG_MAX_VALUE) == 1)
    {
      throw new RuntimeException("postcondition failed");
    }
    return bi;
  }

  @Override
  public double[] readDoubles(int len) throws IOException
  {
    double[] d = new double[len];
    for (int i = 0; i < len; ++i)
    {
      d[i] = this.readDouble();
    }
    return d;
  }
}
