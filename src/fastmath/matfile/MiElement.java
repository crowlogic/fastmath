/*
 * Decompiled with CFR 0.144.
 */
package fastmath.matfile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;

import fastmath.AbstractBufferedObject;
import fastmath.BufferUtils;

public abstract class MiElement
extends AbstractBufferedObject
implements NamedWritable {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void write(SeekableByteChannel channel) throws IOException {
        this.writeHeader(channel);
        this.writeBuffer(channel);
    }

    public MiElement(ByteBuffer buffer) {
        super(buffer);
    }

    public MiElement(int bytes) {
        super(bytes);
    }

    public long totalSize(long pos) {
        long startPos = pos;
        pos += (long)this.headerSize();
        pos += this.numBytes(pos);
        return MatFile.pad(pos) - startPos;
    }

    public int padding(long pos) {
        pos += (long)this.headerSize();
        pos += this.numBytes(pos);
        return (int)(MatFile.pad(pos) - pos);
    }

    public abstract int getDataType();

    protected final boolean compressedHeader() {
        return false;
    }

    public final int headerSize() {
        return this.compressedHeader() ? 4 : 8;
    }

    protected final long writeHeader(SeekableByteChannel channel) throws IOException {
        ByteBuffer headerBuffer = ByteBuffer.allocateDirect(this.headerSize()).order(ByteOrder.nativeOrder());
        long bytes = this.numBytes(channel.position());
        if (!this.compressedHeader()) {
            headerBuffer.putInt(this.getDataType());
            BufferUtils.putUnsignedInt(headerBuffer, bytes);
        } else if (headerBuffer.order() == ByteOrder.LITTLE_ENDIAN) {
            headerBuffer.putShort((short)this.getDataType());
            BufferUtils.putUnsignedShort(headerBuffer, (int)bytes);
        } else {
            throw new UnsupportedOperationException("Native bigendian support needs work");
        }
        headerBuffer.flip();
        channel.write(headerBuffer);
        return channel.position() + (long)this.headerSize();
    }
}

