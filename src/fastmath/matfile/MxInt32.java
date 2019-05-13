/*
 * Decompiled with CFR 0.144.
 */
package fastmath.matfile;

import fastmath.matfile.MiElement;
import fastmath.matfile.MiInt32;
import fastmath.matfile.MxClass;
import java.nio.ByteBuffer;
import java.util.Iterator;

public class MxInt32
extends MxClass {
    private static final long serialVersionUID = 1L;
    public static final int mxINT32_CLASS = 12;
    private MiElement realPart;
    private MiElement imagPart;

    @Override
    public MxClass.Type getArrayType() {
        return MxClass.Type.INT32;
    }

    public MxInt32(MiInt32 rp) {
        super(null);
        this.realPart = rp;
        this.imagPart = null;
    }

    public MxInt32(int[] values) {
        super(null);
        this.realPart = new MiInt32(values);
        this.imagPart = null;
    }

    public MxInt32(Iterator<MiElement> iter, boolean isComplex) {
        super(null);
        this.realPart = iter.next();
        this.imagPart = isComplex ? iter.next() : null;
    }

    @Override
    public long numBytes(long pos) {
        long startPos = pos;
        pos += this.realPart.totalSize(pos);
        if (this.imagPart != null) {
            pos += this.imagPart.totalSize(pos);
        }
        return pos - startPos;
    }

    public Object toObject() {
        throw new UnsupportedOperationException();
    }
}

