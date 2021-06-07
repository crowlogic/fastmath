package fastmath;

import java.util.function.Consumer;

public class DoubleRowMatrix extends
                             DoubleMatrix
{
  private int rowCapacity;
  private double incrementalCapacityExpansionFactor = 1.25;

  public DoubleRowMatrix()
  {
  }

  public DoubleRowMatrix(int m, int n)
  {
    super(m,
          n);
    this.rowCapacity = m;
  }

  public DoubleRowMatrix(String name, int m, int n)
  {
    super(m,
          n);
    this.rowCapacity = m;
    this.setName(name);
  }

  public DoubleRowMatrix(AbstractMatrix x)
  {
    super(x.getRowCount(),
          x.getColCount());
    for (int i = 0; i < this.numRows; ++i)
    {
      this.row(i).assign(x.row(i));
    }
    this.setName(x.getName());
  }

  public DoubleRowMatrix(double[][] arr)
  {
    super(arr.length,
          arr[0].length);
    for (int i = 0; i < this.numRows; ++i)
    {
      for (int j = 0; j < this.numCols; ++j)
      {
        this.set(i, j, arr[i][j]);
      }
    }
  }

  public DoubleRowMatrix(int numRows, int numCols, String name)
  {
    this(numRows,
         numCols);
    this.setName(name);
  }

  public DoubleMatrix copy(boolean reuseBuffer)
  {
    return new DoubleRowMatrix(this);
  }

  public Vector appendRow()
  {
    if (this.numRows == this.rowCapacity)
    {
      this.rowCapacity += this.getNewRowsIncrement();
      int prevSize = this.numRows * this.numCols;
      int nextSize = this.numCols * this.rowCapacity;
      this.resizeBuffer(prevSize, nextSize);
    }
    ++this.numRows;
    return this.row(this.numRows - 1);
  }

  public Vector appendRow(Consumer<Vector> newRowInitializer)
  {
    Vector newRow = this.appendRow();
    newRowInitializer.accept(newRow);
    return newRow;
  }

  public synchronized Vector appendRow(Vector newRow)
  {
    if (this.numRows == this.rowCapacity)
    {
      int prevRowCapacity = this.rowCapacity;
      this.rowCapacity += this.getNewRowsIncrement();
      try
      {
        this.resizeBuffer(prevRowCapacity * this.numCols, this.rowCapacity * this.numCols);
      }
      catch (OutOfMemoryError oom)
      {
        throw new OutOfMemoryError(String.format("not enough free RAM to expand matrix buffer capacity to %d rows, numRows=%d ",
                                                 this.rowCapacity,
                                                 this.numRows));
      }
    }
    ++this.numRows;
    Vector dstRow = this.row(this.numRows - 1);
    BLAS1.dcopy(newRow, dstRow);
    return dstRow;
  }

  public void appendRow(double... newRow)
  {
    if (this.numRows >= this.rowCapacity)
    {
      int prevRowCapacity = this.rowCapacity;
      this.rowCapacity += this.getNewRowsIncrement();
      this.resizeBuffer(prevRowCapacity * this.numCols, this.rowCapacity * this.numCols);
    }
    ++this.numRows;
    this.row(this.numRows - 1).assign(newRow);
  }

  public DoubleRowMatrix trimCapacityToSize()
  {
    this.rowCapacity = this.numRows;
    this.resizeBuffer(this.rowCapacity * this.numCols, this.numRows * this.numCols);
    return this;
  }

  private int getNewRowsIncrement()
  {
    return Math.max(1, (int) ((double) this.numRows * this.incrementalCapacityExpansionFactor));
  }

  @Override
  public int getOffset(int i, int j)
  {
    assert (i >= 0 && i <= this.numRows) : "Row=" + i + ", numRows=" + this.numRows;
    assert (j >= 0 && j <= this.numCols) : "Column=" + j + ", numColumns=" + this.numCols;
    int offset = i * 8 * this.getRowIncrement() + this.getColIncrement() * j * 8;
    return offset;
  }

  @Override
  public int getColIncrement()
  {
    return 1;
  }

  @Override
  public int getRowIncrement()
  {
    return this.numCols;
  }

  @Override
  public int getMainIncrement()
  {
    return this.getRowIncrement();
  }

  public double getIncrementalCapacityExpansionFactor()
  {
    return this.incrementalCapacityExpansionFactor;
  }

  public void setIncrementalCapacityExpansionFactor(double incrementalCapacityExpansionFactor)
  {
    this.incrementalCapacityExpansionFactor = incrementalCapacityExpansionFactor;
  }

  public void append(DoubleMatrix q)
  {
    for (Vector vec : q.rows())
    {
      this.appendRow(vec);
    }
  }

  public Vector row(int i, boolean expand)
  {
    if (!expand)
    {
      return this.row(i);
    }
    while (i >= this.getRowCount())
    {
      this.appendRow();
    }
    return this.row(i);
  }

  public Vector row(int i, Consumer<Vector> newRowInitializer)
  {
    while (i >= this.getRowCount())
    {
      this.appendRow(newRowInitializer);
    }
    return this.row(i);
  }

  public double getSparse(double t, int yk)
  {
    int row = this.col(0).findLast(t, Vector.Condition.LTE);
    return row >= 0 ? this.get(row, yk + 1) : 0.0;
  }
}
