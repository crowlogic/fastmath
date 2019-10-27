/*
 * Decompiled with CFR 0.144.
 */
package fastmath;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.TreeSet;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class IntVector extends
                       AbstractBufferedObject implements
                       Iterable<Integer>
{
  protected int size;
  private int capacity;
  private double incrementalCapacityExpansionFactor = 1.75;

  public boolean equals(Object obj)
  {
    if (!(obj instanceof IntVector))
    {
      return false;
    }
    IntVector other = (IntVector) obj;
    if (other.size() != this.size)
    {
      return false;
    }
    for (int i = 0; i < this.size; ++i)
    {
      if (this.get(i) == other.get(i))
        continue;
      return false;
    }
    return true;
  }

  public IntVector()
  {
  }

  public IntVector(int m)
  {
    super(BufferUtils.newNativeBuffer(m * 4));
    this.size = m;
  }

  public IntVector(int... x)
  {
    this(x.length);
    for (int i = 0; i < x.length; ++i)
    {
      this.setElementAt(i, x[i]);
    }
  }

  public IntVector(int size, ByteBuffer buffer)
  {
    super(buffer);
    this.size = size;
  }

  public IntVector(Collection<Integer> x)
  {
    super(BufferUtils.newNativeBuffer(x.size() * 4));
    this.size = x.size();
    int i = 0;
    for (Integer num : x)
    {
      this.set(i++, num);
    }
  }

  public IntVector(Vector x)
  {
    this(x.size);
    for (int i = 0; i < x.size; i++)
    {
      set(i, (int) x.get(i));
    }
  }

  public void set(int i, Integer num)
  {
    this.setElementAt(i, num);
  }

  public int size()
  {
    return this.size;
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.size; ++i)
    {
      sb.append(this.elementAt(i)).append(" ");
    }
    return sb.toString();
  }

  public int elementAt(int i)
  {
    return this.buffer.getInt(this.getOffset(i));
  }

  public int getOffset(int i)
  {
    return i * 4;
  }

  public void setElementAt(int i, int x)
  {
    this.buffer.putInt(this.getOffset(i), x);
  }

  public IntVector append(int x)
  {
    if (this.size == this.capacity)
    {
      this.capacity += this.getNewElementsIncrement();
      this.resizeBuffer(this.size, this.capacity);
    }
    ++this.size;
    this.setElementAt(this.size - 1, x);
    return this;
  }

  private int getNewElementsIncrement()
  {
    return Math.max(1, (int) ((double) this.size * this.incrementalCapacityExpansionFactor));
  }

  public IntVector reverse()
  {
    return new Sub(this.buffer,
                   this.size,
                   this.getOffset(this.size() - 1),
                   this.getIncrement() * -1);
  }

  public int getIncrement()
  {
    return 1;
  }

  public IntVector slice(int beginIndex, int endIndex)
  {
    assert (beginIndex >= 0) : String.format("beginIndex %d must be >= 0", beginIndex);
    assert (endIndex <= this.size()) : String.format("endIndex %d must be <= %d", endIndex, this.size());
    return new Sub(this.buffer,
                   endIndex - beginIndex,
                   this.getOffset(beginIndex),
                   this.getIncrement());
  }

  public IntVector unique()
  {
    TreeSet<Integer> values = new TreeSet<Integer>();
    for (int i = 0; i < this.size(); ++i)
    {
      values.add(this.get(i));
    }
    return new IntVector(values);
  }

  public int get(int i)
  {
    return this.elementAt(i);
  }

  @Override
  public Iterator<Integer> iterator()
  {
    return new PrimitiveIterator.OfInt()
    {
      int i = 0;

      @Override
      public boolean hasNext()
      {
        return this.i < IntVector.this.size();
      }

      @Override
      public int nextInt()
      {
        return IntVector.this.get(this.i++);
      }

      @Override
      public void remove()
      {
        throw new UnsupportedOperationException();
      }
    };
  }

  public IntVector extend(int i)
  {
    IntVector newVector = new IntVector(this.size() + i);
    newVector.slice(0, this.size()).assign(this);
    return newVector;
  }

  public IntVector set(int i, int x)
  {
    assert (i < this.size() && i >= 0) : String.format("i=%d size()=%d", i, this.size());
    int offset = this.getOffset(i);
    try
    {
      this.buffer.putInt(offset, x);
    }
    catch (IndexOutOfBoundsException e)
    {
      IndexOutOfBoundsException moreInformativeException = new IndexOutOfBoundsException(String.format("offset=%d > %d",
                                                                                                       offset,
                                                                                                       this.buffer.limit()));
      moreInformativeException.addSuppressed(e);
      throw moreInformativeException;
    }
    return this;
  }

  public IntVector assign(IntVector x)
  {
    assert (this.size() == x.size()) : String.format("dimensions do not match in assignment: this.size=%d != %d",
                                                     this.size(),
                                                     x.size());
    for (int i = 0; i < x.size(); ++i)
    {
      this.set(i, x.get(i));
    }
    return this;
  }

  public IntVector copyAndAppend(int d)
  {
    int len = this.size();
    IntVector newVec = this.extend(1);
    newVec.set(len, d);
    return newVec;
  }

  static class Sub extends
                   IntVector
  {
    private int baseOffset;
    private int increment;
    private int index = -1;

    public Sub(ByteBuffer buffer, int size, int offset, int increment)
    {
      super(size,
            buffer);
      this.baseOffset = offset;
      this.increment = increment;
    }

    public Sub(ByteBuffer buffer, int numRows, int offset2, int rowIncrement, int index)
    {
      this(buffer,
           numRows,
           offset2,
           rowIncrement);
      this.index = index;
    }

    @Override
    public int getIncrement()
    {
      return this.increment;
    }

    public int getIndex()
    {
      return this.index;
    }

    @Override
    public int getOffset(int i)
    {
      return this.baseOffset + this.increment * i * 4;
    }
  }

}
