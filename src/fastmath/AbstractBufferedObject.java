/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Native
 *  com.sun.jna.Pointer
 */
package fastmath;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

import com.sleepycat.persist.model.Persistent;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import fastmath.matfile.Writable;

@Persistent
public abstract class AbstractBufferedObject
implements Writable {
    public transient ByteBuffer buffer;

    @Override
    public void write(SeekableByteChannel channel) throws IOException {
        this.writeBuffer(channel);
    }

    public AbstractBufferedObject() {
    }

    protected void resizeBuffer(int prevSize, int newSize) {
        ByteBuffer newBuffer = BufferUtils.newNativeBuffer(newSize * 8);
        newBuffer.mark();
        if (this.buffer != null) {
            newBuffer.put(this.buffer);
        }
        newBuffer.reset();
        this.buffer = newBuffer;
    }

    public AbstractBufferedObject(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public AbstractBufferedObject(int bufferSize) {
        this(BufferUtils.newNativeBuffer(bufferSize));
    }

    public void writeBuffer(SeekableByteChannel channel) throws IOException {
        this.buffer.mark();
        while (this.buffer.hasRemaining()) {
            channel.write(this.buffer);
        }
        this.buffer.reset();
    }

    public ByteBuffer getBuffer() {
        return this.buffer;
    }

    public Pointer getPointer() {
        return Native.getDirectBufferPointer((Buffer)this.getBuffer());
    }

    public long numBytes(long pos) {
        assert (this.getBuffer() != null) : "buffer is null. Class=" + this.getClass();
        return this.getBuffer().capacity();
    }

    public int limit() {
        assert (this.getBuffer() != null) : "buffer is null. Class=" + this.getClass();
        return this.getBuffer().limit();
    }

    public int capactity() {
        return this.getBuffer().capacity();
    }

    public int position() {
        return this.getBuffer().position();
    }
}

