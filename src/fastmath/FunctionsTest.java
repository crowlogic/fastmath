/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  junit.framework.TestCase
 */
package fastmath;

import junit.framework.TestCase;

public class FunctionsTest
extends TestCase {
    private static double \u03b518 = Math.pow(10.0, -18.0);
    private static double \u03b515 = Math.pow(10.0, -15.0);
    private static double \u03b510 = Math.pow(10.0, -10.0);
    private static double \u03b57 = Math.pow(10.0, -7.0);

    public void testProd() {
        double[] a = new double[]{1.0, 2.0, 4.0};
        double x = Functions.product(i -> a[i], 0, 2);
        FunctionsTest.assertEquals((Object)8.0, (Object)x);
    }

    public void testProdExcluding() {
        double[] a = new double[]{1.0, 2.0, 4.0, 5.0};
        double x = Functions.productExcluding(i -> a[i], 0, 3, 2);
        FunctionsTest.assertEquals((Object)10.0, (Object)x);
    }

    public void testMaxExp() {
    }

    public void testOuterProduct() {
        Vector a = new Vector(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        Vector b = new Vector(new double[]{6.0, 7.0, 8.0, 9.0, 10.0});
        DoubleMatrix op = Functions.outerProduct(a, b);
        FunctionsTest.assertTrue((boolean)op.row(0).equals(b));
        FunctionsTest.assertTrue((boolean)op.row(1).equals(new Vector(new double[]{12.0, 14.0, 16.0, 18.0, 20.0})));
        FunctionsTest.assertTrue((boolean)op.row(2).equals(new Vector(new double[]{18.0, 21.0, 24.0, 27.0, 30.0})));
        FunctionsTest.assertTrue((boolean)op.row(3).equals(new Vector(new double[]{24.0, 28.0, 32.0, 36.0, 40.0})));
        FunctionsTest.assertTrue((boolean)op.row(4).equals(new Vector(new double[]{30.0, 35.0, 40.0, 45.0, 50.0})));
    }

    public void testUniformRandom() {
        Pair<Double, Double> range = new Pair<Double, Double>(2.2, 6.7);
        double minx = Double.MAX_VALUE;
        double maxx = Double.MIN_VALUE;
        for (int i = 0; i < 1000; ++i) {
            double x = Functions.uniformRandom(range);
            maxx = x > maxx ? x : maxx;
            minx = x < minx ? x : minx;
            FunctionsTest.assertTrue((boolean)((Double)range.left <= x && x <= (Double)range.right));
        }
    }
}

