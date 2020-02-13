package fastmath;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

import fastmath.exceptions.NotInvertableException;

public class DoubleColMatrix extends
                             DoubleMatrix
{
  private int columnCapacity;
  private final int baseOffset;
  private double incrementalCapacityExpasionFactor = 1.75;
  private HashMap<String, Vector> columnMap = new HashMap<>();

  public DoubleColMatrix()
  {
    this(0,
         0);
    this.columnCapacity = 0;
  }

  public DoubleColMatrix(int m, int n)
  {
    super(m,
          n);
    this.columnCapacity = n;
    this.baseOffset = 0;
  }

  public DoubleColMatrix(int m, int n, BiFunction<Integer, Integer, Double> x)
  {
    super(m,
          n,
          x);
    this.columnCapacity = n;
    this.baseOffset = 0;
  }

  public DoubleColMatrix(List<Pair<Double, Double>> twoColMatrix)
  {
    this(twoColMatrix.size(),
         2);
    int i = 0;
    for (Pair<Double, Double> pair : twoColMatrix)
    {
      this.set(i, 0, (Double) pair.left);
      this.set(i, 1, (Double) pair.right);
      ++i;
    }
  }

  public DoubleColMatrix(int m, int n, String name)
  {
    this(m,
         n);
    this.columnCapacity = n;
    this.setName(name);
  }

  public DoubleColMatrix(ByteBuffer buffer, int m, int n)
  {
    super(buffer,
          m,
          n);
    this.columnCapacity = n;
    this.baseOffset = 0;
  }

  public DoubleColMatrix(double[][] arr)
  {
    this(arr.length,
         arr[0].length);
    for (int i = 0; i < this.numRows; ++i)
    {
      for (int j = 0; j < this.numCols; ++j)
      {
        this.set(i, j, arr[i][j]);
      }
    }
  }

  public DoubleColMatrix(AbstractMatrix x)
  {
    super(x.getRowCount(),
          x.getColCount());
    this.baseOffset = 0;
    this.columnCapacity = this.getColCount();
    this.setName(x.getName());
    this.assign(x);
//        for (int i = 0; i < this.numCols; ++i) {
//            Vector src = x.col(i);
//            Vector dst = this.col(i);
//            dst.assign(src);
//        }
  }

  public DoubleColMatrix(ByteBuffer buffer, int baseOffset, int numRows, int numCols)
  {
    super(buffer,
          numRows,
          numCols);
    this.columnCapacity = numCols;
    this.baseOffset = baseOffset;
  }

  public DoubleColMatrix(String string, double[][] ds)
  {
    this(ds);
    this.setName(string);
  }

  @SuppressWarnings("unchecked")
  public DoubleColMatrix copy(boolean reuseBuffer)
  {
    return reuseBuffer ? new DoubleColMatrix(this.buffer,
                                             this.getBaseOffset(),
                                             this.numRows,
                                             this.numCols)
                  : new DoubleColMatrix(this);
  }

  @Override
  public int getOffset(int i, int j)
  {
    assert (i >= 0 && i <= this.numRows) : "Row=" + i + ", numRows=" + this.numRows;
    assert (j >= 0 && j <= this.numCols) : "Column=" + j + ", numColumns=" + this.numCols;
    return this.getBaseOffset() + (i * 8 + this.getColIncrement() * j * 8);
  }

  public int getOffsetRow(int intOffset)
  {
    return Math.floorDiv(intOffset, this.getColIncrement());
  }

  public int getOffsetCol(int intOffset)
  {
    return intOffset % this.getColIncrement();
  }

  @Override
  public int getColIncrement()
  {
    return this.numRows;
  }

  @Override
  public int getRowIncrement()
  {
    return 1;
  }

  public Vector appendColumn()
  {
    if (this.numCols == this.columnCapacity)
    {
      this.columnCapacity += this.getNewColumnsIncrement();
      int prevSize = this.numRows * this.numCols;
      int nextSize = this.numRows * this.columnCapacity;
      this.resizeBuffer(prevSize, nextSize);
    }
    ++this.numCols;
    return this.col(this.numCols - 1);
  }

  public void trimToSize()
  {
    int prevColCapacity = this.columnCapacity;
    this.columnCapacity = this.numCols;
    this.resizeBuffer(prevColCapacity * this.numCols, this.numRows * this.numCols);
  }

  private int getNewColumnsIncrement()
  {
    return Math.max(1, (int) ((double) this.numCols * this.incrementalCapacityExpasionFactor));
  }

  @SuppressWarnings("unchecked")
  public DoubleColMatrix divide(double x)
  {
    for (Vector col : this.cols())
    {
      col.divide(x);
    }
    return this;
  }

  public static AbstractMatrix eye(int i, double v)
  {
    DoubleColMatrix x = new DoubleColMatrix(i,
                                            i);
    x.diag().assign(v);
    return x;
  }

  public static AbstractMatrix eye(int i)
  {
    return DoubleColMatrix.eye(i, 1.0);
  }

  @Override
  public int getMainIncrement()
  {
    return this.getColIncrement();
  }

  public int getBaseOffset()
  {
    return this.baseOffset;
  }

  public Vector appendColumn(String name)
  {
    Vector newColumn = this.appendColumn();
    newColumn.setName(name);
    this.columnMap.put(name, newColumn);
    return newColumn;
  }

  public Vector getColumn(String name)
  {
    return this.columnMap.get(name);
  }

  public Vector col(int i, boolean expand)
  {
    if (!expand)
    {
      return this.col(i);
    }
    while (i >= this.getColCount())
    {
      this.appendColumn();
    }
    return this.col(i);
  }

  public Vector row(int i, boolean resize)
  {
    if (resize && i >= this.getRowCount())
    {
      this.resize(i + 1, this.getColCount());
    }
    return this.row(i);
  }

  public DoubleColMatrix invert() throws NotInvertableException
  {
    IntVector pivots = new IntVector(this.getRowCount());
    Pointer rowCount = this.newIntParam(this.getRowCount());
    Pointer colCount = this.newIntParam(this.getColCount());
    IntVector output = new IntVector(1);
    Pointer outBuffer = Native.getDirectBufferPointer((Buffer) output.getBuffer());
    Pointer pivotBuffer = Native.getDirectBufferPointer((Buffer) pivots.getBuffer());
    LAPACK.instance.dgetrf_(rowCount, colCount, this.getPointer(), rowCount, pivotBuffer, outBuffer);
    if (output.elementAt(0) > 0)
    {
      throw new NotInvertableException(output.elementAt(0));
    }
    if (output.elementAt(0) < 0)
    {
      throw new IllegalArgumentException("the " + -output.elementAt(0) + "-th argument had an illegal value");
    }
    Vector workspace = new Vector(1024);
    Pointer lwork = this.newIntParam(workspace.size);
    LAPACK.instance.dgetri_(rowCount,
                            this.getPointer(),
                            rowCount,
                            pivotBuffer,
                            workspace.getPointer(),
                            lwork,
                            outBuffer);
    if (output.elementAt(0) > 0)
    {
      throw new NotInvertableException(output.elementAt(0));
    }
    if (output.elementAt(0) < 0)
    {
      throw new IllegalArgumentException("the " + -output.elementAt(0) + "-th argument had an illegal value");
    }
    return this;
  }

  private Pointer newIntParam(int rowCount)
  {
    IntVector ib = new IntVector(1);
    ib.setElementAt(0, rowCount);
    return Native.getDirectBufferPointer((Buffer) ib.getBuffer());
  }

  public double supNorm()
  {
    return this.rows()
               .stream(false)
               .mapToDouble(row -> row.doubleStream().map(val -> Math.abs(val)).sum())
               .max()
               .getAsDouble();
  }

  public DoubleColMatrix assign(double[][] ds)
  {
    for (int m = 0; m < this.numRows; ++m)
    {
      for (int n = 0; n < this.numCols; ++n)
      {
        this.set(m, n, ds[m][n]);
      }
    }
    return this;
  }

  public void ensureRowCount(int rows)
  {
    if (getRowCount() < rows + 1)
    {
      resize(rows + 1, getColCount());
    }
  }
}
