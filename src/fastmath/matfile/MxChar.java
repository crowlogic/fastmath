/*
 * Decompiled with CFR 0.144.
 */
package fastmath.matfile;

import fastmath.matfile.MiElement;
import fastmath.matfile.MiUInt16;
import fastmath.matfile.MxClass;
import java.nio.ByteBuffer;
import java.util.Iterator;

public class MxChar
extends MxClass {
    private static final long serialVersionUID = 1L;
    public static final int mxCHAR_CLASS = 4;
    private MiElement realPart;
    private MiElement imagPart;

    @Override
    public MxClass.Type getArrayType() {
        return MxClass.Type.CHAR;
    }

    public MxChar(Iterator<MiElement> iter, boolean isComplex) {
        super(null);
        this.realPart = iter.next();
        this.imagPart = isComplex ? iter.next() : null;
    }

    public MxChar(String values) {
        super(null);
        this.realPart = new MiUInt16(values);
        this.imagPart = null;
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

