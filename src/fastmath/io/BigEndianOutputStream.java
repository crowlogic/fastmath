/*
 * Decompiled with CFR 0.144.
 */
package fastmath.io;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public class BigEndianOutputStream extends
                                   DataOutputStream implements
                                   DataOutput,
                                   UnsignedDataOutput
{
  public static final BigInteger UNSIGNED_LONG_MAX_VALUE = new BigInteger("18446744073709551615");
  public static final long UNSIGNED_INT_MAX_VALUE = 0xFFFFFFFFL;
  public static final int UNSIGNED_SHORT_MAX_VALUE = 65535;
  private DataOutputStream _dataOutputStream;

  public BigEndianOutputStream(OutputStream outputStream)
  {
    super(outputStream);
  }

  @Override
  public void close() throws IOException
  {
    this._dataOutputStream.close();
  }

  @Override
  public void flush() throws IOException
  {
    this._dataOutputStream.flush();
  }

  @Override
  public void writeUnsignedInt(long v) throws IOException
  {
    if (v < 0L || v > 0xFFFFFFFFL)
    {
      throw new IllegalArgumentException("argument out of range for unsigned int");
    }
    long b1 = (v & 0xFF000000L) >> 24;
    long b2 = (v & 0xFF0000L) >> 16;
    long b3 = (v & 65280L) >> 8;
    long b4 = v & 255L;
    this.write((int) b1);
    this.write((int) b2);
    this.write((int) b3);
    this.write((int) b4);
  }

  @Override
  public void writeUnsignedShort(int v) throws IOException
  {
    if (v < 0 || v > 65535)
    {
      throw new IllegalArgumentException("int argument out of range for unsigned short");
    }
    long b1 = ((long) v & 65280L) >> 8;
    long b2 = (long) v & 255L;
    this.write((int) b1);
    this.write((int) b2);
  }

  @Override
  public void writeUnsignedLong(BigInteger value) throws IOException
  {
    if (value.compareTo(UNSIGNED_LONG_MAX_VALUE) == -1 || value.compareTo(BigInteger.ZERO) == -1)
    {
      throw new IllegalArgumentException("BigInteger argument out of bounds for unsigned long");
    }
    byte[] buffer = value.toByteArray();
    if (buffer.length > 9)
    {
      throw new RuntimeException("length = " + buffer.length);
    }
    if (buffer.length == 9 && buffer[0] != 1)
    {
      throw new RuntimeException("buffer[0] = " + buffer[0]);
    }
    BufferTools.reverseBytes(buffer, 1, 8);
    this.write(buffer, 1, 8);
  }

  @Override
  public boolean isLE()
  {
    return false;
  }

  @Override
  public void writeDoubles(double[] d) throws IOException
  {
    byte[] bytes = new byte[d.length * 8];
    int pos = 0;
    for (double dval : d)
    {
      long val = Double.doubleToLongBits(dval);
      for (int i = 0; i < 8; ++i)
      {
        bytes[pos++] = (byte) val;
        val >>= 8;
      }
    }
    this.write(bytes);
  }
}
