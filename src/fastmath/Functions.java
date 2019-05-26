/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  org.apache.commons.math3.random.JDKRandomGenerator
 *  org.apache.commons.math3.random.RandomGenerator
 */
package fastmath;

import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;

public class Functions {
    public static final double \u03c0 = 3.141592653589793;
    private static final String hex = "0123456789ABCDEF";
    private static final double ONE_HUNDRED = 100.0;
    static RandomGenerator uniRng = new JDKRandomGenerator(1);
    @Deprecated
    public static final double EPSILON = 1.0E-9;

    public static final double \u03b4(double z) {
        return z == 0.0 ? 1.0 : 0.0;
    }

    public static native void \u03d1d(double var0, double var2, double[] var4);

    public static void main(String[] args) {
    }

    public static long trimMilli(long t) {
        return t - t % 1000L;
    }

    public static double round(double x, int decimals) {
        double y = Math.pow(10.0, decimals);
        return (double)Math.round(x * y) / y;
    }

    public static boolean isReal(double x) {
        return !Double.isInfinite(x) && !Double.isNaN(x);
    }

    public static double unixTimeToHours(double x, double timezoneOffset) {
        return x % 8.64E7 / 1000.0 / 60.0 / 60.0 + timezoneOffset;
    }

    public static double unixTimeToMinutes(double x, double timezoneOffset) {
        return x % 8.64E7 / 1000.0 / 60.0 + timezoneOffset * 60.0;
    }

    public static int sign(double x) {
        return x == 0.0 ? 0 : (x > 0.0 ? 1 : -1);
    }

    public static double roundToZero(double x) {
        return Math.signum(x) >= 0.0 ? Math.floor(x) : Math.ceil(x);
    }

    public static double frac(double x) {
        return x - Math.floor(x);
    }

    public static double uniformRandom(Pair<Double, Double> range) {
        return uniRng.nextDouble() * ((Double)range.right - (Double)range.left) + (Double)range.left;
    }

    public static DoubleMatrix outerProduct(Vector a, Vector b) {
        DoubleMatrix amatrix = a.asMatrix().trans();
        DoubleMatrix bmatrix = b.asMatrix();
        return amatrix.prod(bmatrix);
    }

    public static Vector range(double xmin, double xmax, double step) {
        return Functions.range(xmin, xmax, step, (int)((xmax - xmin) / step) + 1);
    }

    public static Vector range(double xmin, double xmax, double step, int n) {
        assert (step > 0.0);
        Vector data = new Vector(n);
        double t = xmin;
        for (int row = 0; row < n; ++row) {
            data.set(row, t);
            t += step;
        }
        return data;
    }

    public static DoubleColMatrix eye(int n) {
        DoubleColMatrix eye = new DoubleColMatrix(n, n);
        eye.diag().assign(1.0);
        return eye;
    }

    public static DoubleColMatrix eye(int n, DoubleColMatrix eye) {
        eye.assign(0.0);
        eye.diag().assign(1.0);
        return eye;
    }

    public static double sum(IntToDoubleFunction elements, int lowerIndex, int upperIndex) {
        if (upperIndex < lowerIndex) {
            return 0.0;
        }
        return IntStream.rangeClosed(lowerIndex, upperIndex).mapToDouble(elements).sum();
    }

    public static DoubleStream grid(double left, double right, int n) {
        double dt = (right - left) / (double)n;
        return IntStream.rangeClosed(0, n).mapToDouble(t -> left + (double)t * dt);
    }

    public static Stream<?> seq(IntFunction<?> elements, int lowerIndex, int upperIndex) {
        return IntStream.rangeClosed(lowerIndex, upperIndex).mapToObj(elements);
    }

    public static DoubleStream seq(IntToDoubleFunction elements, int lowerIndex, int upperIndex) {
        return IntStream.rangeClosed(lowerIndex, upperIndex).mapToDouble(elements);
    }

    public static double sumExcluding(IntToDoubleFunction elements, int lowerIndex, int upperIndex, int excluding) {
        return IntStream.rangeClosed(lowerIndex, upperIndex).filter(i -> i != excluding).mapToDouble(elements).sum();
    }

    public static double product(IntToDoubleFunction elements, int lowerIndex, int upperIndex) {
        double \u03a0 = 1.0;
        for (int i = lowerIndex; i <= upperIndex; ++i) {
            \u03a0 *= elements.applyAsDouble(i);
        }
        return \u03a0;
    }

    public static double productExcluding(IntToDoubleFunction elements, int lowerIndex, int upperIndex, int excluding) {
        return IntStream.rangeClosed(lowerIndex, upperIndex).filter(i -> i != excluding).mapToDouble(elements).reduce(1.0, (a, b) -> a * b);
    }
}

