package fastmath.matfile;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import java.util.Iterator;

import fastmath.AbstractBufferedObject;
import fastmath.AbstractMatrix;
import fastmath.DoubleColMatrix;

public class MxDouble extends
                      MxClass
{
  private static final long serialVersionUID = 1L;
  public static final int mxDOUBLE_CLASS = 6;
  private final MiElement realPart;
  private final MiElement imagPart;

  @Override
  public int limit()
  {
    int lim = 0;
    if (this.realPart != null)
    {
      lim += this.realPart.limit();
    }
    if (this.imagPart != null)
    {
      lim += this.imagPart.limit();
    }
    return lim;
  }

  @Override
  public MxClass.Type getArrayType()
  {
    return MxClass.Type.DOUBLE;
  }

  public AbstractBufferedObject getRealPart()
  {
    return this.realPart;
  }

  public AbstractBufferedObject getImagPart()
  {
    return this.imagPart;
  }

  public MxDouble(MiDouble realPart, MiInt32 dimensions)
  {
    super(null);
    this.realPart = realPart;
    this.imagPart = null;
  }

  public MxDouble(Iterator<MiElement> iter, boolean isComplex, MiInt32 dimensions)
  {
    super(null);
    this.realPart = iter.next();
    this.imagPart = isComplex ? iter.next() : null;
  }

  @Override
  public long numBytes(long pos)
  {
    long startPos = pos;
    pos += this.realPart.totalSize(pos);
    if (this.imagPart != null)
    {
      pos += this.imagPart.totalSize(pos);
    }
    return pos - startPos;
  }

  public String toString()
  {
    return "mxDouble{ realPart = " + this.realPart + " imagPart = " + this.imagPart + " }";
  }

  @Override
  public void write(SeekableByteChannel channel) throws IOException
  {
    if (this.realPart != null)
    {
      this.realPart.write(channel);
    }
    if (this.imagPart != null)
    {
      this.imagPart.write(channel);
    }
  }

  public AbstractMatrix toDenseDoubleMatrix(MiInt32 dimensions)
  {
    if (!(this.realPart instanceof MiDouble))
    {
      throw new UnsupportedOperationException("realPart.class = " + this.realPart.getClass().getName());
    }
    if (this.imagPart != null)
    {
      if (!(this.imagPart instanceof MiDouble))
      {
        throw new UnsupportedOperationException("imagPart.class = " + this.imagPart.getClass().getName());
      }
      throw new UnsupportedOperationException("Complex types not supported yet");
    }
    if (dimensions.getSize() != 2)
    {
      throw new UnsupportedOperationException("Number of dimensions, " + dimensions.getSize() + " != 2");
    }
    return new DoubleColMatrix(((MiDouble) this.realPart).getVector().getBuffer().order(ByteOrder.nativeOrder()),
                               dimensions.elementAt(0),
                               dimensions.elementAt(1));
  }
}
