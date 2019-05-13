/*
 * Decompiled with CFR 0.144.
 */
package fastmath;

import fastmath.AbstractBufferedObject;
import fastmath.BufferUtils;
import fastmath.ColIterator;
import fastmath.DoubleColMatrix;
import fastmath.DoubleMatrix;
import fastmath.RowIterator;
import fastmath.Vector;
import fastmath.exceptions.FastMathException;
import fastmath.matfile.MiDouble;
import fastmath.matfile.MiInt32;
import fastmath.matfile.MiMatrix;
import fastmath.matfile.MxClass;
import fastmath.matfile.MxDouble;
import fastmath.matfile.Writable;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import util.AutoHashMap;

public abstract class AbstractMatrix
extends AbstractBufferedObject
implements Writable {
    protected String name;
    protected int numCols;
    protected int numRows;
    transient AutoHashMap<Integer, Vector> colVectors = new AutoHashMap<Integer, Vector>(i -> new Vector.Sub(this.buffer, this.numRows, this.getOffset(0, (int)i), this.getRowIncrement(), (int)i));
    Vector tmpVec = new Vector(this.getColCount());

    public AbstractMatrix() {
    }

    public AbstractMatrix(ByteBuffer buffer) {
        super(buffer);
    }

    public AbstractMatrix(int bufferSize) {
        super(bufferSize);
    }

    public abstract Vector asVector();

    public Vector col(int i) {
        assert (i <= this.numCols);
        return this.colVectors.getOrCreate(i);
    }

    public void print(PrintWriter printWriter) {
        this.print(printWriter, 15);
    }

    public void print(PrintWriter printWriter, int digits) {
        String format = String.format("%%5.%df ", digits);
        for (int i = 0; i < this.getRowCount(); ++i) {
            for (int j = 0; j < this.getColCount(); ++j) {
                printWriter.format(format, this.get(i, j));
            }
            if (this.getRowCount() <= 1) continue;
            printWriter.println();
        }
    }

    public ColIterator<Vector> cols() {
        return new ColIterator<Vector>(this);
    }

    public MiMatrix createMiMatrix() {
        MiInt32 dimensions = new MiInt32(this.numRows, this.numCols);
        if (this.name == null) {
            throw new IllegalArgumentException("name is null");
        }
        return new MiMatrix(new MxDouble(new MiDouble(this.toColMajorMatrix().asVector()), dimensions), dimensions, this.name);
    }

    public abstract int getColIncrement();

    public String getDimensionString() {
        return this.numRows + "x" + this.numCols;
    }

    public String getName() {
        return this.name;
    }

    public abstract int getOffset(int var1, int var2);

    public abstract int getRowIncrement();

    public final boolean isColMajor() {
        return this.getRowIncrement() == 1;
    }

    public boolean isDense() {
        return this.getRowIncrement() == 1 && this.getColIncrement() == this.numRows || this.getRowIncrement() == this.numCols && this.getColIncrement() == 1;
    }

    public boolean isSquare() {
        return this.numRows == this.numCols;
    }

    public boolean isSymmetric() {
        if (!this.isSquare()) {
            return false;
        }
        for (int i = 0; i < this.numRows; ++i) {
            if (this.col(i).equals(this.row(i))) continue;
            return false;
        }
        return true;
    }

    public boolean isTranspose() {
        return false;
    }

    public int getColCount() {
        return this.numCols;
    }

    public int getRowCount() {
        return this.numRows;
    }

    public abstract <X extends DoubleMatrix> X slice(int var1, int var2, int var3, int var4);

    public AbstractMatrix resize(int newRowCount, int newColCount) {
        DoubleMatrix backup = (DoubleMatrix)this.copy(true);
        try {
            this.buffer = BufferUtils.newNativeBuffer(newRowCount * newColCount * 8);
        }
        catch (OutOfMemoryError oom) {
            throw new RuntimeException(String.format("OutOfMemoryError encountered when trying to resize matrix from [%d,%d] to [%d,%d]: %s", this.getRowCount(), this.getColCount(), newRowCount, newColCount, oom.getMessage()), oom);
        }
        this.numRows = newRowCount;
        this.numCols = newColCount;
        int rowToCopy = Math.min(this.getRowCount(), backup.getRowCount());
        int colsToCopy = Math.min(this.getColCount(), backup.getColCount());
        ((DoubleMatrix)this.slice(0, 0, rowToCopy, colsToCopy)).assign((AbstractMatrix)backup.slice(0, 0, rowToCopy, colsToCopy));
        return this;
    }

    public abstract <T extends DoubleMatrix> T assign(AbstractMatrix var1);

    public Vector lastRow() {
        if (this.getRowCount() == 0) {
            return null;
        }
        return this.row(this.getRowCount() - 1);
    }

    public Vector lastCol() {
        if (this.getColCount() < 1) {
            return null;
        }
        return this.col(this.getColCount() - 1);
    }

    public Vector nextToLastCol() {
        if (this.getColCount() < 2) {
            return null;
        }
        return this.col(this.getColCount() - 2);
    }

    public Vector row(int i) {
        assert (i >= 0 && i < this.getRowCount()) : "illegal row, " + i + ", numRows = " + this.getRowCount();
        int offset = this.getOffset(i, 0);
        Vector.Sub rowVector = new Vector.Sub(this.buffer, this.numCols, offset, this.getColIncrement(), i);
        return rowVector;
    }

    public <V extends Vector> RowIterator<V> rows() {
        return new RowIterator<V>(this);
    }

    public AbstractMatrix set(int i, int j, double x) {
        assert (i >= 0) : String.format("row i=%d must be non-negative", i);
        assert (j >= 0) : String.format("col j=%d must be non-negative", i);
        assert (i < this.numRows) : String.format("i=%d >= numRows=%d", i, this.numRows);
        assert (j < this.numCols) : String.format("j=%d >= numCols=%d", j, this.numCols);
        int offset = this.getOffset(i, j);
        this.buffer.putDouble(offset, x);
        return this;
    }

    public double get(int i, int j) {
        return this.buffer.getDouble(this.getOffset(i, j));
    }

    public <T extends AbstractMatrix> T setName(String name) {
        this.name = name;
        return (T)this;
    }

    public DoubleMatrix toColMajorMatrix() {
        if (this instanceof DoubleColMatrix && this.isColMajor() && this.isDense() && !this.isTranspose()) {
            return (DoubleMatrix)this;
        }
        return new DoubleColMatrix(this);
    }

    public abstract <M extends AbstractMatrix> M copy(boolean var1);

    public Vector sum() {
        Vector sums = new Vector(this.getColCount());
        for (int i = 0; i < sums.size; ++i) {
            sums.set(i, this.col(i).sum());
        }
        return sums;
    }

    public abstract AbstractMatrix trans();

    public AbstractMatrix pow(double x) {
        this.asVector().pow(x);
        return this;
    }

    public void sort(Comparator<Vector> cmp) {
        try {
            this.sort(0, this.numRows - 1, cmp);
        }
        catch (FastMathException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    protected void sort(int left, int right, Comparator<Vector> cmp) throws FastMathException {
        if (right <= left) {
            return;
        }
        int i = this.partition(left, right, cmp);
        this.sort(left, i - 1, cmp);
        this.sort(i + 1, right, cmp);
    }

    private int partition(int left, int right, Comparator<Vector> cmp) {
        int i = left - 1;
        int j = right;
        do {
            if (cmp.compare(this.row(++i), this.row(right)) > 0) {
                continue;
            }
            while (cmp.compare(this.row(right), this.row(--j)) > 0 && j != left) {
            }
            if (i >= j) break;
            this.exch(i, j);
        } while (true);
        if (i != right) {
            this.exch(i, right);
        }
        return i;
    }

    private void exch(int i, int j) {
        Vector tmp = this.row(i).copy();
        this.row(i).assign(this.row(j));
        this.row(j).assign(tmp);
    }

    public String toMatrixString() {
        StringBuffer sb = new StringBuffer("[");
        this.rows().forEach(row -> {
            row.forEach(x -> sb.append(x + ","));
            sb.append(";");
        });
        sb.append("]");
        return sb.toString();
    }
}

