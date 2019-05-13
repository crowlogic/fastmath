/*
 * Decompiled with CFR 0.144.
 */
package fastmath.matfile;

import fastmath.BufferUtils;
import fastmath.matfile.MiElement;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

public class MiUInt16
extends MiElement {
    private static final long serialVersionUID = 1L;
    public static final int miUINT16 = 4;
    private ShortBuffer shortBuffer;

    public MiUInt16(ByteBuffer slice) {
        super(slice);
        this.shortBuffer = slice.asShortBuffer();
    }

    public MiUInt16(String values) {
        super(BufferUtils.newNativeBuffer(values.length()));
        BufferUtils.copy(values, this.getBuffer());
    }

    @Override
    public int getDataType() {
        return 4;
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer(this.getClass().getName() + "[");
        for (int i = 0; i < this.getSize(); ++i) {
            if (i > 0) {
                strBuf.append(",");
            }
            strBuf.append(BufferUtils.getUnsignedShort(this.shortBuffer, i));
        }
        strBuf.append("]");
        return strBuf.toString();
    }

    public int getSize() {
        return this.shortBuffer.limit();
    }

    public Object toObject() {
        throw new UnsupportedOperationException();
    }
}

