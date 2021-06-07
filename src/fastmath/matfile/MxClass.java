package fastmath.matfile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

import fastmath.AbstractBufferedObject;
import fastmath.AbstractMatrix;

public abstract class MxClass extends
                              AbstractBufferedObject
{
  protected MxClass()
  {
    super(null);
  }

  protected MxClass(ByteBuffer buffer)
  {
    super(buffer);
  }

  public abstract Type getArrayType();

  @Override
  public abstract long numBytes(long var1);

  public <M extends AbstractMatrix> M toDenseDoubleMatrix(MiInt32 dimensionsArray)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void write(SeekableByteChannel channel) throws IOException
  {
    this.writeBuffer(channel);
  }

  public static enum Type
  {
   CELL(1),
   STRUCT(2),
   CHAR(4),
   DOUBLE(6),
   INT32(12);

    private final int type;

    private Type(int type)
    {
      this.type = type;
    }

    public int getType()
    {
      return this.type;
    }

    public static Type valueOf(int l)
    {
      switch (l)
      {
      case 1:
      {
        return CELL;
      }
      case 2:
      {
        return STRUCT;
      }
      case 4:
      {
        return CHAR;
      }
      case 6:
      {
        return DOUBLE;
      }
      case 12:
      {
        return INT32;
      }
      }
      throw new IllegalArgumentException("Invalid value(" + l + ")");
    }
  }

}
