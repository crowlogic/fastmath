/*
 * Decompiled with CFR 0.144.
 */
package fastmath.matfile;

import java.nio.ByteBuffer;

import fastmath.Vector;

public class MiFloat extends
                     MiElement
{
  private static final long serialVersionUID = 1L;
  public static final int miDOUBLE = 9;
  public static final int BYTES = 4;
  final Vector vector;

  @Override
  public ByteBuffer getBuffer()
  {
    return this.vector.getBuffer();
  }

  public MiFloat(ByteBuffer slice)
  {
    super(slice);
    this.vector = new Vector(slice);
  }

  public MiFloat(Vector vector)
  {
    super(vector.getBuffer());
    this.vector = vector;
  }

  @Override
  public int getDataType()
  {
    return 9;
  }

  public Vector getVector()
  {
    return this.vector;
  }

  public double[] toArray()
  {
    return this.vector.toDoubleArray();
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer(this.getClass().getSimpleName() + "[" + this.vector.size() + "]={");
    for (int i = 0; i < this.vector.size(); ++i)
    {
      if (i > 0)
      {
        sb.append(",");
      }
      sb.append(this.vector.get(i));
    }
    sb.append("}");
    return sb.toString();
  }

  public Object toObject()
  {
    throw new UnsupportedOperationException();
  }
}
