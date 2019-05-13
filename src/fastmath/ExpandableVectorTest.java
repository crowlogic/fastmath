/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  junit.framework.TestCase
 */
package fastmath;

import fastmath.ExpandableVector;
import fastmath.Vector;
import junit.framework.TestCase;

public class ExpandableVectorTest
extends TestCase {
    public void testBasicFunctionality() {
        ExpandableVector vec = new ExpandableVector();
        vec.set(5, 12.0);
        ExpandableVectorTest.assertTrue((boolean)(vec.get(5) == 12.0));
    }
}

