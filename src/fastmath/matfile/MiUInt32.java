/*
 * Decompiled with CFR 0.144.
 */
package fastmath.matfile;

import java.nio.ByteBuffer;

import fastmath.BufferUtils;

public class MiUInt32
extends MiElement {
    private static final long serialVersionUID = 1L;
    public static final int miUINT32 = 6;
    public static final long MAX_VALUE = -1L;
    public static final int SIZE = 4;

    public MiUInt32(ByteBuffer buffer) {
        super(buffer);
    }

    public MiUInt32(int i) {
        super(4 * i);
    }

    @Override
    public int getDataType() {
        return 6;
    }

    public long elementAt(int index) {
        return BufferUtils.toUnsignedInt(this.getBuffer().getInt(index * 4));
    }

    public void setElementAt(int index, long value) {
        BufferUtils.putUnsignedInt(this.getBuffer(), index * 4, value);
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer(this.getClass().getName() + " [");
        for (int i = 0; i < this.getSize(); ++i) {
            if (i > 0) {
                strBuf.append(",");
            }
            strBuf.append(this.elementAt(i));
        }
        strBuf.append("]");
        return strBuf.toString();
    }

    public int getSize() {
        return this.getBuffer().limit() / 4;
    }

    public Object toObject() {
        throw new UnsupportedOperationException();
    }
}

