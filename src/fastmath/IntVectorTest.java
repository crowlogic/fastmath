/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  junit.framework.TestCase
 */
package fastmath;

import fastmath.IntVector;
import junit.framework.TestCase;

public class IntVectorTest
extends TestCase {
    public void testReverse() {
        IntVector x = new IntVector(2, 4, 6, 8, 10);
        IntVector y = x.reverse();
        IntVector z = new IntVector(10, 8, 6, 4, 2);
        IntVectorTest.assertEquals((Object)z, (Object)y);
    }

    public void testSetAndGet() {
        IntVector v = new IntVector(1);
        v.set(0, 12);
        IntVectorTest.assertEquals((int)12, (int)v.get(0));
    }

    public void testExtend() {
        IntVector v = new IntVector(0);
        v = v.extend(1);
        v.set(0, 12);
        IntVectorTest.assertEquals((int)12, (int)v.get(0));
    }

    public void testCopyAndAppend() {
        IntVector v = new IntVector(0);
        v = v.copyAndAppend(12);
        IntVectorTest.assertEquals((int)12, (int)v.get(0));
    }
}

