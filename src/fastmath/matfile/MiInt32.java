/*
 * Decompiled with CFR 0.144.
 */
package fastmath.matfile;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MiInt32 extends
                     MiElement
{
  private static final long serialVersionUID = 1L;
  public static final int miINT32 = 5;
  public static final int BYTES = 4;

  public MiInt32(ByteBuffer slice)
  {
    super(slice);
    this.getBuffer().order(ByteOrder.nativeOrder());
  }

  public MiInt32(int... values)
  {
    this(values.length);
    int i = 0;
    for (int x : values)
    {
      this.getBuffer().putInt(i, x);
      i += 4;
    }
  }

  public MiInt32(int len)
  {
    super(4 * len);
  }

  @Override
  public int getDataType()
  {
    return 5;
  }

  public int elementAt(int index)
  {
    return this.getBuffer().getInt(index * 4);
  }

  public void setElementAt(int index, int value)
  {
    this.getBuffer().putInt(index * 4, value);
  }

  public int getSize()
  {
    return this.getBuffer().limit() / 4;
  }

  public String toString()
  {
    StringBuffer strBuf = new StringBuffer(this.getClass().getSimpleName() + "[" + this.getSize() + "]={");
    for (int i = 0; i < this.getSize(); ++i)
    {
      if (i > 0)
      {
        strBuf.append(",");
      }
      strBuf.append(this.elementAt(i));
    }
    strBuf.append("}");
    return strBuf.toString();
  }

  public Object toObject()
  {
    throw new UnsupportedOperationException();
  }
}
