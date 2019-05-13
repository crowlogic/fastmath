/*
 * Decompiled with CFR 0.144.
 */
package fastmath.matfile;

import fastmath.matfile.MiElement;
import java.nio.ByteBuffer;

public class MiUInt8
extends MiElement {
    private static final long serialVersionUID = 1L;
    public static final int miUINT8 = 2;

    @Override
    public int getDataType() {
        return 2;
    }

    public MiUInt8(ByteBuffer slice) {
        super(slice);
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer(this.getClass().getName() + "[");
        ByteBuffer buffer = this.getBuffer();
        int bufferLimit = buffer.limit();
        for (int i = 0; i < bufferLimit; ++i) {
            if (i > 0) {
                strBuf.append(",");
            }
            strBuf.append(buffer.getChar(i));
        }
        strBuf.append("]");
        return strBuf.toString();
    }

    public Object toObject() {
        throw new UnsupportedOperationException();
    }
}

