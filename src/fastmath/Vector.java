/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Native
 *  com.sun.jna.Pointer
 *  org.apache.commons.math3.util.CombinatoricsUtils
 */
package fastmath;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.apache.commons.math3.util.CombinatoricsUtils;

import com.sleepycat.persist.model.Persistent;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import fastmath.matfile.MiMatrix;
import fastmath.matfile.Writable;

@Persistent
public class Vector
extends AbstractBufferedObject
implements Writable,
Iterable<Double>,
Collection<Double> {
    private static final long serialVersionUID = 1L;
    private String name;
    protected int size;
    private double incrementalCapacityExpansionFactor = 0.25;

    public Vector() {
    }

    @Override
    public Pointer getPointer() {
        return Native.getDirectBufferPointer((Buffer)this.buffer);
    }

    public Vector xor(double p) {
        return this.pow(p);
    }

    public Vector xor(int p) {
        return this.pow(p);
    }

    public Vector negate() {
        return this.copy().multiply(-1.0);
    }

    private static native double ddot(int var0, ByteBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6);

    public Vector(ByteBuffer slice) {
        super(slice);
        assert (slice != null) : "slice is null";
        this.size = slice.capacity() / 8;
    }

    public Vector(double[] x) {
        this(BufferUtils.newNativeBuffer(x.length * 8));
        this.size = x.length;
        for (int i = 0; i < x.length; ++i) {
            this.set(i, x[i]);
        }
    }

    public Vector(Collection<Double> x) {
        super(BufferUtils.newNativeBuffer(x.size() * 8));
        this.size = x.size();
        int i = 0;
        for (Double num : x) {
            this.set(i++, (double)num);
        }
    }

    public Vector(int m) {
        super(BufferUtils.newNativeBuffer(m * 8));
        this.size = m;
    }

    protected Vector(int m, ByteBuffer buffer) {
        super(buffer == null ? BufferUtils.newNativeBuffer(m * 8) : buffer);
        assert (buffer != null) : "slice is null";
        this.size = m;
    }

    public Vector(Vector v) {
        this(v.toDoubleArray());
        this.setName(v.getName());
    }

    public Vector(String name) {
        this(0);
        this.setName(name);
    }

    public Vector(String name, int dim) {
        this(dim);
        this.setName(name);
    }

    public Vector(String string, double[] array) {
        this(array);
        this.setName(string);
    }

    public Vector(DoubleStream ds) {
        this(ds.toArray());
    }

    public Vector(IntVector k) {
        this(k.size);
        for (int i = 0; i < k.size; ++i) {
            this.set(i, (double)k.get(i));
        }
    }

    public Vector abs() {
        for (int i = 0; i < this.size(); ++i) {
            this.set(i, Math.abs(this.get(i)));
        }
        return this;
    }

    public double moment(int n) {
        return this.doubleStream().map(x -> Math.pow(x, n)).average().getAsDouble();
    }

    public Vector moments(int n) {
        return new Vector(IntStream.rangeClosed(1, n).mapToDouble(i -> this.moment(i)));
    }

    public Vector normalizedMoments(int n) {
        return new Vector(IntStream.rangeClosed(1, n).mapToDouble(i -> this.normalizedMoment(i)));
    }

    private double normalizedMoment(int i) {
        return this.moment(i) / (double)CombinatoricsUtils.factorial((int)i);
    }

    public double centralMoment(int n) {
        switch (n) {
            case 1: {
                return this.mean();
            }
        }
        double m = this.mean();
        return this.doubleStream().map(x -> Math.pow(x - m, n)).average().getAsDouble();
    }

    public double add(int i, double x) {
        assert (i < this.size) : String.format("%d >= %d", i, this.size);
        double updatedValue = this.get(i) + x;
        this.set(i, updatedValue);
        return updatedValue;
    }

    public Vector addVector(Vector x) {
        assert (this.size() == x.size());
        return this.add(x);
    }

    public Vector add(Vector x) {
        for (int i = 0; i < this.size(); ++i) {
            this.set(i, this.get(i) + x.get(i));
        }
        return this;
    }

    public Vector add(Vector x, double alpha) {
        assert (this.size == x.size()) : "Dimensions must be equal";
        for (int i = 0; i < this.size; ++i) {
            this.set(i, this.get(i) + x.get(i) * alpha);
        }
        return this;
    }

    public DoubleMatrix asDenseDoubleMatrix(int rows, int cols) {
        DoubleMatrix.Sub matrix = new DoubleMatrix.Sub(this.buffer, rows, cols, this.getOffset(0), 8, rows, false);
        matrix.setName(this.getName());
        return matrix;
    }

    public DoubleMatrix asMatrix() {
        return this.asDenseDoubleMatrix(1, this.size);
    }

    public DoubleMatrix asRowMatrix() {
        return new DoubleMatrix.Sub(this.buffer, 1, this.size, this.getOffset(0), this.size, this.getIncrement(), false);
    }

    public Vector reassign(Vector x) {
        this.buffer = x.buffer;
        this.size = x.size;
        this.name = x.name;
        this.incrementalCapacityExpansionFactor = x.incrementalCapacityExpansionFactor;
        return this;
    }

    public Vector assign(Vector x) {
        assert (this.size() == x.size()) : String.format("dimensions do not match in assignment: this.size=%d != %d", this.size(), x.size());
        for (int i = 0; i < x.size(); ++i) {
            this.set(i, x.get(i));
        }
        return this;
    }

    public Vector copy() {
        return new Vector(this);
    }

    public Vector copy(VectorContainer container) {
        return new Vector(this);
    }

    public MiMatrix createMiMatrix() {
        return this.asMatrix().createMiMatrix();
    }

    public Vector diff() {
        if (this.size == 0) {
            return new Vector();
        }
        Vector fd = this.copy().slice(1, this.size());
        fd.subtract(this.slice(0, this.size() - 1));
        return fd.setName("d" + this.getName());
    }

    public Vector divide(double x) {
        for (int i = 0; i < this.size(); ++i) {
            this.set(i, this.get(i) / x);
        }
        return this;
    }

    public Vector divide(Vector x) {
        assert (this.size() == x.size());
        for (int i = 0; i < this.size(); ++i) {
            this.set(i, this.get(i) / x.get(i));
        }
        return this;
    }

    public double dotProduct(Vector x) {
        assert (x.size == this.size) : "Dimensions do not agree";
        if (x.getIncrement() >= 0 && this.getIncrement() >= 0) {
            return Vector.ddot(x.size, x.getBuffer(), x.getOffset(0), x.getIncrement(), this.getBuffer(), this.getOffset(0), this.getIncrement());
        }
        return IntStream.range(0, this.size()).mapToDouble(i -> this.get(i) * x.get(i)).sum();
    }

    @Override
    public boolean equals(Object obj) {
        if (Vector.class.isAssignableFrom(obj.getClass())) {
            Vector v = (Vector)obj;
            if (this.size != v.size()) {
                return false;
            }
            for (int i = 0; i < this.size; ++i) {
                if (this.get(i) == v.get(i)) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean equals(Vector v, double bounds) {
        if (this.size != v.size()) {
            return false;
        }
        for (int i = 0; i < this.size; ++i) {
            if (!(Math.abs(this.get(i) - v.get(i)) > bounds)) continue;
            return false;
        }
        return true;
    }

    public native Vector exp();

    public Vector floor() {
        for (int i = 0; i < this.size(); ++i) {
            this.set(i, Math.floor(this.get(i)));
        }
        return this;
    }

    public IntVector findAll(double val, Condition cond) {
        IntVector indices = new IntVector();
        int i = 0;
        while ((i = this.find(val, cond, i + 1)) > 0 && i < this.size()) {
            indices.append(i);
        }
        return indices;
    }

    public int find(double val, Condition cond, int start) {
        for (int i = start; i < this.size(); ++i) {
            double x = this.get(i);
            if (!(cond == Condition.EQUAL && x == val || cond == Condition.GT && x > val || cond == Condition.LT && x < val || cond == Condition.GTE && x >= val) && (cond != Condition.LTE || !(x <= val))) continue;
            return i;
        }
        return -1;
    }

    public int findLast(double val, Condition cond) {
        int reverseResult = this.reverse().find(val, cond, 0);
        return reverseResult == -1 ? -1 : this.size() - reverseResult - 1;
    }

    public boolean hasAnyInfinities() {
        return this.find(Double.POSITIVE_INFINITY, Condition.EQUAL, 0) != -1;
    }

    public double get(int i) {
        assert (i >= 0) : "index=" + i + " is negative";
        assert (i < this.size()) : "Index out of bounds, " + i + " >= " + this.size();
        assert (this.buffer != null) : "buffer is null";
        int offset = this.getOffset(i);
        double doubleVal = this.buffer.getDouble(offset);
        return doubleVal;
    }

    public int getIncrement() {
        return 1;
    }

    public int getIndex() {
        return -1;
    }

    public String getName() {
        return this.name;
    }

    public int getOffset(int i) {
        return i * 8;
    }

    public Vector replaceInfinity(double with) {
        for (int i = 0; i < this.size; ++i) {
            double x = this.get(i);
            this.set(i, Double.isInfinite(x) ? with : x);
        }
        return this;
    }

    public Vector replaceNaN(double with) {
        for (int i = 0; i < this.size; ++i) {
            double x = this.get(i);
            this.set(i, Double.isNaN(x) ? with : x);
        }
        return this;
    }

    public Vector inv() {
        for (int i = 0; i < this.size; ++i) {
            this.set(i, 1.0 / this.get(i));
        }
        return this;
    }

    public boolean isContiguous() {
        return this.getIncrement() == 1;
    }

    public boolean isDense() {
        return this.getIncrement() == 1;
    }

    public Vector lag(int k) {
        return this.lag(k, 0.0);
    }

    public Vector lag(int k, double x) {
        assert (k > 0) : "k must be > 0";
        this.slice(k, this.size()).assign(this.slice(0, this.size() - k).copy());
        this.slice(0, k).assign(x);
        return this;
    }

    public Vector log() {
        for (int i = 0; i < this.size; ++i) {
            this.set(i, Math.log(this.get(i)));
        }
        return this;
    }

    public double fmax() {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < this.size; ++i) {
            double x = this.get(i);
            if ( x > max )
            {
            	max = x;
            }
        }
        return max;
    }

    public double fmax(int[] idx) {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < this.size; ++i) {
            double d = 0;
            double d2 = this.get(i);
            if (!(d > max)) continue;
            max = d2;
            idx[0] = i;
        }
        return max;
    }

    public double mean() {
        return this.sum() / (double)this.size;
    }

    public double fmin() {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < this.size; ++i) {
            double d = 0;
            double d2 = this.get(i);
            if (!(d < min)) continue;
            min = d2;
        }
        return min;
    }

    public Vector multiply(double x) {
        for (int i = 0; i < this.size; ++i) {
            this.set(i, this.get(i) * x);
        }
        return this;
    }

    public Vector multiply(Vector x) {
        throw new UnsupportedOperationException("TODO");
    }

    public Vector pow(double x) {
        for (int i = 0; i < this.size; ++i) {
            this.set(i, Math.pow(this.get(i), x));
        }
        return this;
    }

    public Vector replace(double x, double y) {
        for (int i = 0; i < this.size; ++i) {
            if (this.get(i) != x) continue;
            this.set(i, y);
        }
        return this;
    }

    public DoubleMatrix reshape(int m, int n) {
        assert (m * n == this.size()) : "dimensions do not agree, m=" + m + ", n=" + n + ", m*n != " + this.size();
        return new DoubleMatrix.Sub(this.buffer, m, n, this.getOffset(0), this.getIncrement(), m, false);
    }

    public Vector reverse() {
        return new Sub(this.buffer, this.size, this.getOffset(this.size() - 1), this.getIncrement() * -1);
    }

    public Vector round(double x) {
        double y = 1.0 / x;
        for (int i = 0; i < this.size(); ++i) {
            this.set(i, (double)Math.round(this.get(i) * y) / y);
        }
        return this;
    }

    public Vector set(int i, double x) {
        assert (i < this.size() && i >= 0) : String.format("i=%d size()=%d", i, this.size());
        int offset = this.getOffset(i);
        try {
            this.buffer.putDouble(offset, x);
        }
        catch (IndexOutOfBoundsException e) {
            IndexOutOfBoundsException moreInformativeException = new IndexOutOfBoundsException(String.format("offset=%d > %d", offset, this.buffer.limit()));
            moreInformativeException.addSuppressed(e);
            throw moreInformativeException;
        }
        return this;
    }

    public Vector set(double ... x) {
        return this.assign(x);
    }

    public Vector setName(String name) {
        this.name = name;
        return this;
    }

    public final int dimension() {
        return this.size;
    }

    public final int dim() {
        return this.size;
    }

    @Override
    public final int size() {
        return this.size;
    }

    public Vector slice(int beginIndex, int endIndex) {
        assert (beginIndex >= 0) : String.format("beginIndex %d must be >= 0", beginIndex);
        assert (endIndex <= this.size()) : String.format("endIndex %d must be <= %d", endIndex, this.size());
        Sub subset = new Sub(this.buffer, endIndex - beginIndex, this.getOffset(beginIndex), this.getIncrement());
        subset.setName(this.getName());
        return subset;
    }

    public Vector sqrt() {
        Vector sqv = new Vector(this.size);
        for (int i = 0; i < this.size; ++i) {
            sqv.set(i, Math.sqrt(this.get(i)));
        }
        return sqv;
    }

    public Vector subtract(Vector x) {
        return this.subtract(x, 1.0);
    }

    public Vector subtract(Vector x, double alpha) {
        assert (this.size == x.size()) : "Dimensions must agree, this.size=" + this.size + " != x.size = " + x.size();
        return this.add(x, -alpha);
    }

    public double sum() {
        return this.doubleStream().sum();
    }

    public Vector swap(Vector x) {
        int len = this.size();
        assert (len == x.size()) : "Dimensions must agree";
        throw new UnsupportedOperationException("TODO");
    }

    public void tanh() {
        for (int i = 0; i < this.size; ++i) {
            this.set(i, Math.tanh(this.get(i)));
        }
    }

    public double[] toDoubleArray() {
        double[] x = new double[this.size];
        for (int i = 0; i < this.size; ++i) {
            x[i] = this.get(i);
        }
        return x;
    }

    public AbstractMatrix toDiagMatrix() {
        DoubleColMatrix cm = new DoubleColMatrix(this.size, this.size);
        cm.diag().assign(this);
        return cm;
    }

    public String toString() {
        return this.print(10);
    }

    public String print(int digits) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        if (this.getName() != null) {
            pw.append(this.getName() + " = ");
        }
        this.asRowMatrix().print(pw, digits);
        pw.flush();
        return sw.toString();
    }

    public Vector assign(double scalar) {
        for (int i = 0; i < this.size(); ++i) {
            this.set(i, scalar);
        }
        return this;
    }

    public Vector assign(double ... data) {
        assert (this.size() == data.length) : String.format("dimensions do not match in assignment: this.size=%d != %d", this.size(), data.length);
        for (int i = 0; i < data.length; ++i) {
            this.set(i, data[i]);
        }
        return this;
    }

    public Vector unique() {
        TreeSet<Double> values = new TreeSet<Double>();
        for (int i = 0; i < this.size(); ++i) {
            values.add(this.get(i));
        }
        return new Vector(values);
    }

    public Vector extend(int i) {
        Vector newVector = new Vector(this.size() + i);
        newVector.slice(0, this.size()).assign(this);
        return newVector;
    }

    public PrimitiveIterator.OfDouble iterator() {
        return new PrimitiveIterator.OfDouble(){
            int i = 0;

            @Override
            public boolean hasNext() {
                return this.i < Vector.this.size();
            }

            @Override
            public Double next() {
                return Vector.this.get(this.i++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public double nextDouble() {
                return Vector.this.get(this.i++);
            }
        };
    }

    public int getDimension() {
        return this.size();
    }

    public double getIncrementalCapacityExpansionFactor() {
        return this.incrementalCapacityExpansionFactor;
    }

    public void setIncrementalCapacityExpansionFactor(double incrementalCapacityExpansionFactor) {
        this.incrementalCapacityExpansionFactor = incrementalCapacityExpansionFactor;
    }

    public double getLeftmostValue() {
        return this.get(0);
    }

    public double getRightmostValue() {
        return this.isEmpty() ? Double.NaN : this.get(this.size() - 1);
    }

    public Vector add(double x) {
        for (int i = 0; i < this.size(); ++i) {
            this.set(i, this.get(i) + x);
        }
        return this;
    }

    public Spliterator.OfDouble spliterator() {
        return new VectorSpliterator();
    }

    public DoubleStream doubleStream() {
        return StreamSupport.doubleStream(this.spliterator(), false);
    }

    public Vector apply(IntFunction<Double> func) {
        for (int i = 0; i < this.size; ++i) {
            this.set(i, (double)func.apply(i));
        }
        return this;
    }

    @Override
    public void write(SeekableByteChannel channel) throws IOException {
        this.createMiMatrix().write(channel);
    }

    public double variance() {
        int n = this.size();
        double mean = this.mean();
        return Functions.sum(i -> Math.pow(this.get(i) - mean, 2.0), 0, n - 1) / (double)n;
    }

    public double getStdev() {
        return Math.sqrt(this.variance());
    }

    public Vector cumulativeSum() {
        Vector x = new Vector(this.size());
        double d = 0.0;
        for (int i = 0; i < this.size(); ++i) {
            x.set(i, d += this.get(i));
        }
        return x;
    }

    public Vector subtract(double subtrahend) {
        for (int i = 0; i < this.size(); ++i) {
            this.set(i, this.get(i) - subtrahend);
        }
        return this;
    }

    public double autocovAtLag(int lag) {
        int N = this.size();
        double m = this.mean();
        double autocov = 0.0;
        for (int i = 0; i < N - lag; ++i) {
            autocov += (this.get(i) - m) * (this.get(i + lag) - m);
        }
        return autocov *= 1.0 / (double)(N - lag);
    }

    public double autocorAtLag(int lag) {
        return this.autocovAtLag(lag) / this.variance();
    }

    public Vector autocor(int maxLag) {
        double var = this.variance();
        return new Vector(IntStream.rangeClosed(0, maxLag).mapToDouble(lag -> this.autocovAtLag(lag) / var));
    }

    public double getLjungBoxStatistic(int maxLag) {
        Vector ac = this.autocor(maxLag);
        int n = this.size();
        return (double)(n * (n + 2)) * Functions.sum(k -> Math.pow(ac.get(k), 2.0) / (double)(n - k), 1, maxLag);
    }

    public Vector copyAndAppend(double d) {
        int len = this.size();
        Vector newVec = this.extend(1);
        newVec.set(len, d);
        return newVec;
    }

    @Override
    public boolean add(Double arg0) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean addAll(Collection<? extends Double> arg0) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean contains(Object arg0) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean containsAll(Collection<?> arg0) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean remove(Object arg0) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean removeAll(Collection<?> arg0) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean retainAll(Collection<?> arg0) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Object[] toArray() {
        Object[] x = new Double[this.size];
        for (int i = 0; i < this.size; ++i) {
            x[i] = this.get(i);
        }
        return x;
    }

    @Override
    public <T> T[] toArray(T[] arg0) {
        throw new UnsupportedOperationException("TODO");
    }

    public IntVector toIntVector() {
        IntVector iv = new IntVector(this.size());
        for (int i = 0; i < this.size(); ++i) {
            iv.set(i, (int)Math.round(this.get(i)));
        }
        return iv;
    }

    static class Sub
    extends Vector {
        private int baseOffset;
        private int increment;
        private int index = -1;

        public Sub(ByteBuffer buffer, int size, int offset, int increment) {
            super(size, buffer);
            this.baseOffset = offset;
            this.increment = increment;
        }

        public Sub(ByteBuffer buffer, int numRows, int offset2, int rowIncrement, int index) {
            this(buffer, numRows, offset2, rowIncrement);
            this.index = index;
        }

        @Override
        public int getIncrement() {
            return this.increment;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        @Override
        public int getOffset(int i) {
            return this.baseOffset + this.increment * i * 8;
        }
    }

    public static enum Condition {
        EQUAL,
        GT,
        GTE,
        LT,
        LTE;
        
    }

    public final class VectorSpliterator
    extends Spliterators.AbstractDoubleSpliterator {
        int n;

        public VectorSpliterator() {
            super(Vector.this.size, 16448);
            this.n = 0;
        }

        @Override
        public boolean tryAdvance(DoubleConsumer action) {
            if (this.n >= Vector.this.size) {
                return false;
            }
            action.accept(Vector.this.get(this.n++));
            return true;
        }

        @Override
        public boolean tryAdvance(Consumer<? super Double> action) {
            if (this.n >= Vector.this.size) {
                return false;
            }
            action.accept((Double)Vector.this.get(this.n++));
            return true;
        }
    }

}

