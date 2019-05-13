/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  junit.framework.TestCase
 */
package fastmath;

import fastmath.IntVector;
import fastmath.Vector;
import java.io.PrintStream;
import junit.framework.TestCase;

public class VectorTest
extends TestCase {
    public void testAssign() {
        Vector x = new Vector(new double[]{0.1, 0.2, 0.3, 0.4});
        Vector y = new Vector(4);
        Vector z = new Vector(new double[]{0.1, 0.2, 0.3, 0.4});
        y.assign(x);
        VectorTest.assertEquals((Object)z, (Object)y);
    }

    public void testFindAll() {
        Vector x = new Vector(new double[]{0.1, 0.2, 0.3, 0.4, 0.15, 0.17, 0.6, -0.3, 0.0, -0.1, -0.2, 3.4, 0.2});
        IntVector idx = x.findAll(0.0, Vector.Condition.LT);
        System.out.println(idx);
        VectorTest.assertEquals((int)idx.elementAt(0), (int)7);
        VectorTest.assertEquals((int)idx.elementAt(1), (int)9);
        VectorTest.assertEquals((int)idx.elementAt(2), (int)10);
    }

    public void testSwap() {
        Vector a = new Vector(new double[]{1.0, 2.0, 3.0});
        Vector b = new Vector(new double[]{4.0, 5.0, 6.0});
        VectorTest.assertEquals((Object)6.0, (Object)a.sum());
        VectorTest.assertEquals((Object)15.0, (Object)b.sum());
        a.swap(b);
        VectorTest.assertEquals((Object)15.0, (Object)a.sum());
        VectorTest.assertEquals((Object)6.0, (Object)b.sum());
    }

    public void testSlice() {
        Vector a = new Vector(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0, 19.0, 20.0});
        Vector firstSlice = a.slice(0, 10);
        Vector secondSlice = a.slice(10, a.size());
        double firstSum = firstSlice.sum();
        double secondSum = secondSlice.sum();
    }

    public void testSliceReverse() {
        Vector a = new Vector(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0, 19.0, 20.0});
        Vector firstSlice = a.slice(0, 10).reverse();
        Vector secondSlice = a.slice(10, a.size()).reverse();
        double firstSum = firstSlice.sum();
        double secondSum = secondSlice.sum();
    }

    public void testAdd() {
        Vector a = new Vector(new double[]{5.0, 10.0, 15.0, 20.0, 30.0});
        Vector b = new Vector(new double[]{1.0, 5.0, 3.0, 10.0, 6.0});
        b.add(a);
        Vector c = new Vector(new double[]{6.0, 15.0, 18.0, 30.0, 36.0});
        VectorTest.assertEquals((Object)c, (Object)b);
    }

    public void testMultiply() {
        Vector a = new Vector(new double[]{5.0, 10.0, 15.0, 20.0, 30.0});
        Vector b = new Vector(new double[]{1.0, 5.0, 3.0, 10.0, 6.0});
        Vector c = new Vector(new double[]{5.0, 50.0, 45.0, 200.0, 180.0});
        VectorTest.assertEquals((Object)c, (Object)a.multiply(b));
    }

    public void testDivide() {
        Vector a = new Vector(new double[]{5.0, 10.0, 15.0, 20.0, 30.0});
        Vector b = new Vector(new double[]{1.0, 5.0, 3.0, 10.0, 6.0});
        Vector c = new Vector(new double[]{5.0, 2.0, 5.0, 2.0, 5.0});
        VectorTest.assertEquals((Object)c, (Object)a.divide(b));
    }
}

