package fastmath.matfile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.Iterator;

import fastmath.AbstractBufferedObject;
import fastmath.AbstractMatrix;

public class MiMatrix extends
                      MiElement
{
  public static final int miMATRIX = 14;
  public static final int mxCELL_CLASS = 1;
  public static final int mxOBJECT_CLASS = 3;
  public static final int mxSPARSE_CLASS = 5;
  public static final int mxSINGLE_CLASS = 7;
  public static final int mxINT8_CLASS = 8;
  public static final int mxUINT8_CLASS = 9;
  public static final int mxINT16_CLASS = 10;
  public static final int mxUINT16_CLASS = 11;
  public static final int mxUINT32_CLASS = 13;
  public static final int FLAG_COMPLEX = 2048;
  public static final int FLAG_GLOBAL = 1024;
  public static final int FLAG_LOGICAL = 512;
  private final MiUInt32 arrayFlags;
  private MiInt32 dimensionsArray;
  private final MiInt8 arrayName;
  private MxClass value;
  private long streamStart;
  private long streamStop;
  private String name;

  public MiMatrix(MxClass array, MiInt32 dimensions, String name)
  {
    super(null);
    this.arrayFlags = new MiUInt32(2);
    int arrayType = array.getArrayType().getType();
    this.arrayFlags.setElementAt(0, arrayType);
    this.arrayFlags.setElementAt(1, 0L);
    this.dimensionsArray = dimensions;
    this.arrayName = new MiInt8(name);
    this.name = name;
    this.value = array;
  }

  public MiMatrix(String values, String name)
  {
    super(null);
    this.value = new MxChar(values);
    this.arrayFlags = new MiUInt32(2);
    this.arrayFlags.setElementAt(0, this.value.getArrayType().getType());
    this.arrayFlags.setElementAt(1, 0L);
    this.dimensionsArray = new MiInt32(1,
                                       values.length());
    this.arrayName = new MiInt8(name);
  }

  public MiMatrix(int[] values, String name)
  {
    super(null);
    this.value = new MxInt32(values);
    this.arrayFlags = new MiUInt32(2);
    this.arrayFlags.setElementAt(0, this.value.getArrayType().getType());
    this.arrayFlags.setElementAt(1, 0L);
    this.dimensionsArray = new MiInt32(values.length,
                                       1);
    this.arrayName = new MiInt8(name);
  }

  public MiMatrix(MatFile matFile, ByteBuffer buffer, boolean swapped)
  {
    super(buffer);
    MiIterator iter = new MiIterator(matFile,
                                     buffer);
    this.arrayFlags = (MiUInt32) iter.nextElement();
    this.dimensionsArray = (MiInt32) iter.nextElement();
    this.arrayName = (MiInt8) iter.nextElement();
    this.name = this.arrayName.asString();
    this.readValue(iter);
  }

  protected void readValue(Iterator<MiElement> iter)
  {
    int arrayClass = this.getArrayClass();
    switch (arrayClass)
    {
    case 12:
    {
      this.value = new MxInt32(iter,
                               this.isComplex());
      break;
    }
    case 4:
    {
      this.value = new MxChar(iter,
                              this.isComplex());
      break;
    }
    case 6:
    {
      this.value = new MxDouble(iter,
                                this.isComplex(),
                                this.dimensionsArray);
      break;
    }
    case 2:
    {
      this.value = new MxStruct(iter);
      break;
    }
    default:
    {
      throw new UnsupportedOperationException("Unsupported type of arrayClass "
                    + (Object) ((Object) MxClass.Type.valueOf(arrayClass)));
    }
    }
  }

  @Override
  public int getDataType()
  {
    return 14;
  }

  public final boolean isComplex()
  {
    return (this.getFlags() & 2048L) == 2048L;
  }

  public long getFlags()
  {
    return (int) (this.arrayFlags.elementAt(0) >> 16 & 255L);
  }

  public void setFlags(long flags)
  {
    this.arrayFlags.setElementAt(1, flags);
  }

  public boolean isGlobal()
  {
    return (this.getFlags() & 1024L) == 1024L;
  }

  public boolean isLogical()
  {
    return (this.getFlags() & 512L) == 512L;
  }

  public final int getArrayClass()
  {
    return (int) (this.arrayFlags.elementAt(0) >> 24 & 255L);
  }

  @Override
  public String getName()
  {
    return this.name;
  }

  public AbstractBufferedObject getMxClass()
  {
    return this.value;
  }

  public String toString()
  {
    return "MiMatrix\"" + this.getName() + "\"[" + this.dimensionsArray + "]\"" + this.arrayName + "\"{" + this.value
                  + "}";
  }

  public <M extends AbstractMatrix> M toDenseDoubleMatrix()
  {
    M denseDoubleMatrix = this.value.toDenseDoubleMatrix(this.dimensionsArray);
    ((AbstractMatrix) denseDoubleMatrix).setName(this.getName());
    return denseDoubleMatrix;
  }

  @Override
  public final long numBytes(long pos)
  {
    long startPos = pos;
    long arrayFlagSize = this.arrayFlags.totalSize(pos);
    pos += arrayFlagSize;
    pos += this.dimensionsArray.totalSize(pos);
    pos += this.arrayName.totalSize(pos);
    if (this.value != null)
    {
      pos += this.value.numBytes(pos);
    }
    else
    {
      pos += 8L;
      pos += this.streamStop - this.streamStart;
    }
    long totalLen = pos - startPos;
    return totalLen;
  }

  @Override
  public void write(SeekableByteChannel channel) throws IOException
  {
    SeekableByteChannel seekableByteChannel = channel;
    synchronized (seekableByteChannel)
    {
      this.writeHeader(channel);
      this.arrayFlags.write(channel);
      this.dimensionsArray.write(channel);
      this.arrayName.write(channel);
      MatFile.pad(channel);
      this.value.write(channel);
      MatFile.pad(channel);
    }
  }

  public MiInt32 getDimensionsArray()
  {
    return this.dimensionsArray;
  }
}
