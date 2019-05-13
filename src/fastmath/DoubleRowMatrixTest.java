/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  junit.framework.TestCase
 */
package fastmath;

import fastmath.DoubleRowMatrix;
import java.io.PrintStream;
import junit.framework.TestCase;

public class DoubleRowMatrixTest
extends TestCase {
    public static void testGetSparse() {
        DoubleRowMatrix row = new DoubleRowMatrix(new double[][]{{1400.0, 2.3}, {1900.4, 2.5}, {2000.0, 5.5}, {4000.0, 7.6}, {4001.0, 7.8}});
        double getFirst = row.getSparse(1405.0, 0);
        System.out.println("getFirst=" + getFirst);
        DoubleRowMatrixTest.assertEquals((Object)2.3, (Object)getFirst);
        double getSecond = row.getSparse(1901.0, 0);
        System.out.println("getSecond=" + getSecond);
        DoubleRowMatrixTest.assertEquals((Object)2.5, (Object)getSecond);
        double getThird = row.getSparse(2001.0, 0);
        System.out.println("getThird=" + getThird);
        DoubleRowMatrixTest.assertEquals((Object)5.5, (Object)getThird);
        double getZeroth = row.getSparse(0.0, 0);
        System.out.println("getZeroth=" + getZeroth);
        DoubleRowMatrixTest.assertEquals((Object)0.0, (Object)getZeroth);
    }
}

