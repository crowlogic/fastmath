/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  dnl.utils.text.table.TextTable
 */
package fastmath;

import dnl.utils.text.table.TextTable;
import fastmath.AbstractMatrix;
import fastmath.BLAS1;
import fastmath.ColIterator;
import fastmath.DoubleColMatrix;
import fastmath.DoubleRowMatrix;
import fastmath.EigenDecomposition;
import fastmath.Functions;
import fastmath.IntVector;
import fastmath.RowIterator;
import fastmath.Vector;
import fastmath.VectorContainer;
import fastmath.exceptions.FastMathException;
import fastmath.exceptions.IllegalValueError;
import fastmath.exceptions.SingularFactorException;
import fastmath.io.ThreadLocalNumberFormat;
import fastmath.matfile.NamedWritable;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.text.NumberFormat;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import com.sleepycat.persist.model.Persistent;

@Persistent
public abstract class DoubleMatrix
extends AbstractMatrix
implements NamedWritable {
    private static final long serialVersionUID = 1L;
    protected static IntVector tmpIvec1 = new IntVector(1);
    protected IntVector ipiv;
    private int optimalQRWorkspace = 0;
    private Vector workspace;

    public double add(int i, int j, double x) {
        double updatedValue = this.get(i, j) + x;
        this.set(i, j, updatedValue);
        return updatedValue;
    }

    private static int dgeqp3(int m, int n, ByteBuffer A, int Aoff, int lda, ByteBuffer buffer, ByteBuffer tau, int tauOff, ByteBuffer work, int lwork) {
        throw new UnsupportedOperationException("TODO");
    }

    private static native int dorgqr(int var0, int var1, int var2, ByteBuffer var3, int var4, int var5, ByteBuffer var6, ByteBuffer var7, int var8);

    private static native int dpotri(char var0, int var1, ByteBuffer var2, int var3);

    private static native int dsytrf(char var0, int var1, ByteBuffer var2, int var3, int var4, IntBuffer var5, ByteBuffer var6, int var7);

    private static native int dsytri(char var0, int var1, ByteBuffer var2, int var3, int var4, IntVector var5, ByteBuffer var6, int var7);

    protected DoubleMatrix(int m, int n, BiFunction<Integer, Integer, Double> x) {
        this(m, n);
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                this.set(i, j, x.apply(i, j));
            }
        }
    }

    protected DoubleMatrix() {
    }

    protected DoubleMatrix(ByteBuffer buffer, int numRows, int numCols) {
        super(buffer);
        this.numRows = numRows;
        this.numCols = numCols;
    }

    public DoubleMatrix(int m, int n) {
        super(m * n * 8);
        this.numRows = m;
        this.numCols = n;
    }

    public DoubleMatrix add(AbstractMatrix x) {
        for (Vector col : this.cols()) {
            Vector src = x.col(col.getIndex());
            assert (src.size() == col.size()) : String.format("src.size=%d this.colSize=%d", src.size(), col.size());
            col.add(src);
        }
        return this;
    }

    private void allocateWorkspace(int size) {
        if (this.workspace == null || this.workspace.size() < size) {
            this.workspace = new Vector(size);
        }
    }

    public <T extends DoubleMatrix> T assign(double x) {
        this.cols().forEach(c -> c.assign(x));
        return (T)this;
    }

    @Override
    public <T extends DoubleMatrix> T assign(AbstractMatrix x) {
        assert (this.numRows == x.numRows && this.numCols == x.getColCount()) : "Matrices must be of compatible dimension: " + String.format("%dx%d != %dx%d", this.getRowCount(), this.getColCount(), x.getRowCount(), x.getColCount());
        for (int i = 0; i < this.numCols; ++i) {
            this.col(i).assign(x.col(i));
        }
        return (T)this;
    }

    public AbstractMatrix assignAboveDiag(double d) {
        for (int i = 1; i < this.numCols; ++i) {
            this.col(i).slice(0, i).assign(d);
        }
        return this;
    }

    public AbstractMatrix assignBelowDiag(double d) {
        for (int i = 0; i < this.numCols - 1; ++i) {
            this.col(i).slice(i + 1, this.getRowCount()).assign(d);
        }
        return this;
    }

    @Override
    public Vector asVector() {
        Vector.Sub vec = new Vector.Sub(this.buffer, this.numRows * this.numCols, this.getOffset(0, 0), 1);
        vec.setName(this.getName());
        return vec;
    }

    public abstract int getMainIncrement();

    @Override
    public abstract <M extends AbstractMatrix> M copy(boolean var1);

    public AbstractMatrix copyUpToLow() {
        assert (this.isSquare()) : "matrix is not square";
        for (int i = 0; i < this.getRowCount() - 1; ++i) {
            this.col(i).slice(i + 1, this.getRowCount()).assign(this.row(i).slice(i + 1, this.getRowCount()));
        }
        return this;
    }

    public Vector diag() {
        assert (this.isSquare()) : "Matrix must be square";
        return new Vector.Sub(this.buffer, this.numRows, this.getOffset(0, 0), this.getRowIncrement() + this.getColIncrement());
    }

    public double trace() {
        return this.diag().sum();
    }

    public AbstractMatrix diffCols() {
        for (Vector col : this.cols()) {
            col.slice(1, this.getRowCount()).assign(col.diff());
            col.set(0, 0.0);
        }
        return this;
    }

    public AbstractMatrix diffColsTrim() {
        for (Vector col : this.cols()) {
            col.slice(1, this.getRowCount()).assign(col.diff());
        }
        return this.sliceRows(1, this.getRowCount());
    }

    public EigenDecomposition eig() throws FastMathException {
        return this.eig(false, true);
    }

    public EigenDecomposition eig(boolean left, boolean right) throws FastMathException {
        return new EigenDecomposition(this, left, right);
    }

    public DoubleMatrix elementWiseMultiply(DoubleMatrix m) {
        assert (this.numRows == m.numRows) : "number of rows does not agree";
        assert (this.numCols == m.getColCount()) : "number of columns does not agree";
        this.asVector().multiply(m.asVector());
        return this;
    }

    public DoubleMatrix elementWiseDivide(DoubleMatrix m) {
        assert (this.numRows == m.numRows) : "number of rows does not agree";
        assert (this.numCols == m.getColCount()) : "number of columns does not agree";
        this.asVector().divide(m.asVector());
        return this;
    }

    public boolean equals(AbstractMatrix m, double bounds) {
        if (this.numRows != m.getRowCount() || this.numCols != m.getColCount()) {
            return false;
        }
        for (int i = 0; i < this.numRows; ++i) {
            if (this.row(i).equals(m.row(i), bounds)) continue;
            return false;
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (DoubleMatrix.class.isAssignableFrom(obj.getClass())) {
            AbstractMatrix m = (AbstractMatrix)obj;
            if (this.numRows != m.getRowCount() || this.numCols != m.getColCount()) {
                return false;
            }
            for (int i = 0; i < this.numRows; ++i) {
                if (this.row(i).equals(m.row(i))) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    public DoubleMatrix exp() {
        for (Vector col : this.cols()) {
            col.exp();
        }
        return this;
    }

    public DoubleMatrix log() {
        for (Vector col : this.cols()) {
            col.log();
        }
        return this;
    }

    @Override
    public double get(int i, int j) {
        if (i >= this.numRows || j >= this.numCols || i < 0 || j < 0) {
            return Double.NaN;
        }
        assert (i < this.numRows) : String.format("i=%d >= numRows=%d", i, this.numRows);
        assert (j < this.numCols) : String.format("j=%d >= numCols=%d", j, this.numCols);
        int offset = this.getOffset(i, j);
        return this.get1D(offset);
    }

    public double get1D(int offset) {
        return this.buffer.getDouble(offset);
    }

    public int getItemCount(int arg0) {
        return this.getRowCount();
    }

    public AbstractMatrix identity() {
        this.assign(0.0);
        this.diag().assign(1.0);
        return this;
    }

    public AbstractMatrix lagCols(int k) {
        for (Vector col : this.cols()) {
            col.lag(k);
        }
        return this;
    }

    public DoubleMatrix ldivide(DoubleMatrix B) throws SingularFactorException, IllegalValueError {
        Vector S = new Vector(Math.min(this.numRows, this.numCols));
        this.allocateWorkspace(1);
        BLAS1.dgelss(this, B, S, tmpIvec1, this.workspace, -1);
        this.allocateWorkspace((int)this.workspace.get(0));
        int retCode = BLAS1.dgelss(this, B, S, tmpIvec1, this.workspace, this.workspace.size());
        assert (retCode == 0) : "dgelss returned " + retCode;
        return B.slice(0, 0, this.numCols, B.getColCount());
    }

    public DoubleMatrix ldivide(DoubleMatrix B, VectorContainer container) throws SingularFactorException, IllegalValueError {
        Vector S = container.getVector(Math.min(this.numRows, this.numCols));
        this.allocateWorkspace(1);
        BLAS1.dgelss(this, B, S, tmpIvec1, this.workspace, -1);
        this.allocateWorkspace((int)this.workspace.get(0));
        int retCode = BLAS1.dgelss(this, B, S, tmpIvec1, this.workspace, this.workspace.size());
        assert (retCode == 0) : "dgelss returned " + retCode;
        return B.slice(0, 0, this.numCols, B.getColCount());
    }

    public int leadingDimension() {
        return this.isTranspose() ? this.getColCount() : this.getRowCount();
    }

    public Vector max() {
        Vector m = new Vector(this.numCols);
        for (int i = 0; i < this.numCols; ++i) {
            m.set(i, this.col(i).fmax());
        }
        return m;
    }

    public Vector mean() {
        Vector m = new Vector(this.numCols);
        for (int i = 0; i < this.numCols; ++i) {
            m.set(i, this.col(i).mean());
        }
        return m;
    }

    public Vector min() {
        Vector m = new Vector(this.numCols);
        for (int i = 0; i < this.numCols; ++i) {
            m.set(i, this.col(i).fmin());
        }
        return m;
    }

    public AbstractMatrix mlag(int k) {
        assert (k > 0) : "k must be > 0";
        DoubleColMatrix y = new DoubleColMatrix(this.getRowCount(), this.getColCount() * k);
        int idx = 0;
        for (int i = 0; i < this.getColCount(); ++i) {
            for (int j = 1; j <= k; ++j) {
                y.col(idx++).assign(this.col(i)).lag(j);
            }
        }
        return y;
    }

    public AbstractMatrix prod(double x) {
        for (Vector col : this.cols()) {
            col.multiply(x);
        }
        return this;
    }

    public <X extends DoubleMatrix> X divide(double x) {
        this.asVector().multiply(1.0 / x);
        return (X)this;
    }

    public DoubleMatrix multiply(double x) {
        this.asVector().multiply(x);
        return this;
    }

    public DoubleMatrix multiply(Vector x) {
        assert (this.numCols == x.size()) : "vector length must be equal to number of columns";
        for (int i = 0; i < this.numCols; ++i) {
            this.col(i).multiply(x.get(i));
        }
        return this;
    }

    public AbstractMatrix qr() {
        return this.qr(null);
    }

    public AbstractMatrix qr(DoubleMatrix q) {
        IntVector jpvt = new IntVector(this.getColCount());
        for (int i = 0; i < jpvt.size(); ++i) {
            jpvt.setElementAt(i, i + 1);
        }
        int minMN = Math.min(this.getRowCount(), this.getColCount());
        Vector tau = new Vector(minMN);
        if (this.optimalQRWorkspace == 0) {
            this.allocateWorkspace(1);
            DoubleMatrix.dgeqp3(this.getRowCount(), this.getColCount(), this.getBuffer(), this.getOffset(0, 0), this.getRowCount(), jpvt.getBuffer(), tau.getBuffer(), tau.getOffset(0), this.workspace.getBuffer(), -1);
            this.optimalQRWorkspace = (int)this.workspace.get(0);
        }
        this.allocateWorkspace(this.optimalQRWorkspace);
        int ret = DoubleMatrix.dgeqp3(this.getRowCount(), this.getColCount(), this.getBuffer(), this.getOffset(0, 0), this.isTranspose() ? this.getColCount() : this.getRowCount(), jpvt.getBuffer(), tau.getBuffer(), tau.getOffset(0), this.workspace.getBuffer(), this.workspace.size());
        assert (ret == 0) : "dgeqp3 returned " + ret;
        if (q != null) {
            q.assign((AbstractMatrix)this.slice(0, 0, minMN, minMN));
            ret = DoubleMatrix.dorgqr(q.getRowCount(), q.getColCount(), tau.size(), q.getBuffer(), q.getOffset(0, 0), q.getRowCount(), tau.getBuffer(), this.workspace.getBuffer(), this.workspace.size());
            assert (ret == 0) : "dorgqr returned " + ret;
        }
        return ((DoubleMatrix)this.slice(0, 0, minMN, this.getColCount())).assignBelowDiag(0.0);
    }

    public AbstractMatrix mrdivide(DoubleMatrix B) throws SingularFactorException, IllegalValueError {
        Vector S = new Vector(Math.min(B.getRowCount(), B.getColCount()));
        DoubleMatrix Btrans = B.trans();
        DoubleMatrix thisTrans = this.trans();
        this.allocateWorkspace(1);
        BLAS1.dgelss(Btrans, thisTrans, S, tmpIvec1, this.workspace, -1);
        this.allocateWorkspace((int)this.workspace.get(0));
        int retCode = BLAS1.dgelss(Btrans, thisTrans, S, tmpIvec1, this.workspace, this.workspace.size());
        assert (retCode == 0) : "dgelss returned " + retCode;
        return this.slice(0, 0, this.getRowCount(), B.getRowCount());
    }

    @Override
    public <X extends DoubleMatrix> X slice(int beginRow, int beginCol, int endRow, int endCol) {
        assert (beginRow >= 0 && endRow <= this.numRows && endRow >= beginRow) : String.format("beginRow=%d endRow=%d numRows=%d", beginRow, endRow, this.numRows);
        assert (beginCol >= 0 && endCol <= this.numCols && endCol >= beginCol) : String.format("beginCol=%d endCol=%d numCols=%d", beginCol, endCol, this.numCols);
        Sub subset = new Sub(this.buffer, endRow - beginRow, endCol - beginCol, this.getOffset(beginRow, beginCol), this.getRowIncrement(), this.getColIncrement(), this.isTranspose());
        return (X)subset;
    }

    public <X extends DoubleMatrix> X sliceCols(int start, int end) {
        return this.slice(0, start, this.numRows, end);
    }

    public DoubleMatrix sliceRows(int start, int end) {
        return this.slice(start, 0, end, this.getColCount());
    }

    public DoubleMatrix subtract(AbstractMatrix x) {
        for (Vector col : this.cols()) {
            col.subtract(x.col(col.getIndex()));
        }
        return this;
    }

    public AbstractMatrix subtract(DoubleMatrix x, double alpha) {
        this.asVector().subtract(x.asVector(), alpha);
        return this;
    }

    public AbstractMatrix subtractFromEachCol(Vector x, double alpha) {
        assert (x.size() == this.getRowCount()) : "dimensions do not agree";
        for (Vector col : this.cols()) {
            col.subtract(x, alpha);
        }
        return this;
    }

    @Override
    public Vector sum() {
        Vector m = new Vector(this.numCols);
        for (int i = 0; i < this.numCols; ++i) {
            m.set(i, this.col(i).sum());
        }
        return m;
    }

    public Vector rowSum() {
        Vector m = new Vector(this.numRows);
        for (int i = 0; i < this.numRows; ++i) {
            m.set(i, this.row(i).sum());
        }
        return m;
    }

    public Vector sum(VectorContainer container) {
        Vector m = container.getVector(this.numCols);
        for (int i = 0; i < this.numCols; ++i) {
            m.set(i, this.col(i).sum());
        }
        return m;
    }

    public AbstractMatrix tanh() {
        this.asVector().tanh();
        return this;
    }

    public double[][] toArray() {
        double[][] x = new double[this.numRows][this.numCols];
        for (int i = 0; i < this.numRows; ++i) {
            for (int j = 0; j < this.numCols; ++j) {
                x[i][j] = this.get(i, j);
            }
        }
        return x;
    }

    public String toString() {
        Object[][] strings = new String[this.numRows][this.numCols];
        NumberFormat format = ThreadLocalNumberFormat.getFormat();
        int maxLength = 0;
        int maxDecimal = 0;
        for (int i = 0; i < this.numRows; ++i) {
            for (int j = 0; j < this.numCols; ++j) {
                String string = Double.toString(this.get(i, j));
                int decimal = string.indexOf(46);
                if (decimal > maxDecimal) {
                    maxDecimal = decimal;
                }
                if (string.length() > maxLength) {
                    maxLength = string.length();
                }
                strings[i][j] = string;
            }
        }
        maxLength += 2;
        String name = this.getName() != null ? this.getName() : "";
        IntFunction<?> func = k -> name + "[m," + (k + 1) + "]";
		TextTable table = new TextTable((String[]) Functions.seq(func, 0, this.numCols - 1).toArray(l -> new String[l]), strings);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        StringBuffer sb = new StringBuffer();
        table.setAddRowNumbering(true);
        table.printTable(ps, 0);
        ps.close();
        try {
            return os.toString("UTF8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private String getDimString() {
        String dimString = "(" + this.getRowCount() + "," + this.getColCount() + ")";
        return dimString;
    }

    @Override
    public DoubleMatrix trans() {
        return new Sub(this.buffer, this.numCols, this.numRows, this.getOffset(0, 0), this.getColIncrement(), this.getRowIncrement(), !this.isTranspose());
    }

    public AbstractMatrix transInPlace() {
        assert (this.isSquare()) : "matrix is not square";
        for (int i = 0; i < this.numRows - 1; ++i) {
            Vector colSlice = this.col(i).slice(i + 1, this.numRows);
            Vector rowSlice = this.row(i).slice(i + 1, this.numRows);
            colSlice.swap(rowSlice);
        }
        return this;
    }

    public AbstractMatrix trimRows(int n1, int n2) {
        return this.sliceRows(n1, this.getRowCount() - n2);
    }

    @Override
    public DoubleMatrix pow(double x) {
        return (DoubleMatrix)super.pow(x);
    }

    public DoubleMatrix reverseRows() {
        for (Vector row : this.rows()) {
            row.assign(row.reverse().copy());
        }
        return this;
    }

    public DoubleMatrix reverseCols() {
        for (Vector col : this.cols()) {
            col.assign(col.reverse().copy());
        }
        return this;
    }

    public DoubleColMatrix prod(DoubleMatrix b) {
        throw new UnsupportedOperationException("TODO");
    }

    static class Sub
    extends DoubleMatrix {
        private final int columnIncrement;
        private final boolean isTranspose;
        private final int baseOffset;
        private final int rowIncrement;

        public Sub(ByteBuffer buffer, int numRows, int numColumns, int offset, int rowIncrement, int columnIncrement, boolean isTranspose) {
            super(buffer, numRows, numColumns);
            this.baseOffset = offset;
            this.rowIncrement = rowIncrement;
            this.columnIncrement = columnIncrement;
            this.isTranspose = isTranspose;
        }

        @Override
        public Vector asVector() {
            assert (this.isDense()) : "Underlying matrix storage must be contiguous";
            return super.asVector();
        }

        @Override
        public Vector.Sub col(int i) {
            int offset = this.getOffset(0, i);
            Vector.Sub colVector = new Vector.Sub(this.buffer, this.numRows, offset, this.getRowIncrement(), i);
            if (this.getName() != null) {
                colVector.setName(this.getName() + "[" + i + "]");
            }
            return colVector;
        }

        public DoubleMatrix copy() {
            return this.copy(false);
        }

        public DoubleMatrix copy(boolean reuseBuffer) {
            if (this.getColIncrement() == 1) {
                return new DoubleRowMatrix(this);
            }
            return new DoubleColMatrix(this);
        }

        @Override
        public int getColIncrement() {
            return this.columnIncrement;
        }

        @Override
        public int getOffset(int i, int j) {
            return this.baseOffset + i * this.rowIncrement * 8 + j * this.columnIncrement * 8;
        }

        @Override
        public int getRowIncrement() {
            return this.rowIncrement;
        }

        @Override
        public boolean isTranspose() {
            return this.isTranspose;
        }

        @Override
        public int getMainIncrement() {
            throw new UnsupportedOperationException();
        }
    }

}

