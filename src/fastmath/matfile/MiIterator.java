/*
 * Decompiled with CFR 0.144.
 */
package fastmath.matfile;

import fastmath.BufferUtils;
import fastmath.io.ByteUtils;
import fastmath.matfile.MatFile;
import fastmath.matfile.MiDouble;
import fastmath.matfile.MiElement;
import fastmath.matfile.MiInt32;
import fastmath.matfile.MiInt8;
import fastmath.matfile.MiMatrix;
import fastmath.matfile.MiUInt16;
import fastmath.matfile.MiUInt32;
import fastmath.matfile.MiUInt8;
import fastmath.matfile.exceptions.MatFileParsingException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MiIterator
implements Iterator<MiElement>,
Iterable<MiElement> {
    private Exception exception;
    private int dataType = 0;
    private long size = 0L;
    private boolean hasMore = true;
    private boolean hasRead = false;
    private final ByteBuffer buffer;
    private MatFile matFile;
    private long firstSize;
    private int firstDataType;
    private boolean smallDataTypeFormat;

    public MiIterator(MatFile matFile, ByteBuffer buffer) {
        this.buffer = buffer;
        this.matFile = matFile;
    }

    public int getDataType() {
        return this.dataType;
    }

    public long getNumberOfBytes() {
        return this.size;
    }

    private void readElementHeader() throws IOException {
        this.hasRead = true;
        this.buffer.mark();
        int dataTypeBeforeSwap = this.buffer.getInt();
        this.dataType = ByteUtils.swap(dataTypeBeforeSwap);
        this.size = ByteUtils.swap((int)BufferUtils.getUnsignedInt(this.buffer));
        this.smallDataTypeFormat = false;
        if ((this.dataType & -65536) != 0) {
            this.smallDataTypeFormat = true;
            this.buffer.reset();
            this.firstSize = this.size;
            short unswappedSize = (short)BufferUtils.getUnsignedShort(this.buffer);
            this.size = ByteUtils.swap(unswappedSize);
            this.firstDataType = this.dataType;
            short unswappedDatatype = this.buffer.getShort();
            this.dataType = ByteUtils.swap(unswappedDatatype);
        }
        if (this.size < 0L) {
            throw new IllegalArgumentException("parsing error, size=" + this.size + " firstSize=" + this.firstSize + " dataType+" + this.dataType + " firstDataType=" + this.firstDataType);
        }
    }

    @Override
    public boolean hasNext() {
        if (!this.hasMore) {
            return false;
        }
        if (this.buffer.remaining() == 0) {
            this.hasMore = false;
            return false;
        }
        if (!this.hasRead) {
            try {
                this.hasMore = false;
                this.readElementHeader();
                this.hasMore = true;
            }
            catch (Exception e) {
                this.exception = e;
            }
        }
        return this.hasMore;
    }

    public <E extends MiElement> E nextElement() {
        return (E)this.next();
    }

    @Override
    public MiElement next() throws NoSuchElementException {
        if (!this.hasRead) {
            this.hasNext();
        }
        if (!this.hasMore) {
            NoSuchElementException nse = new NoSuchElementException();
            if (this.exception != null) {
                nse.initCause(this.exception);
            }
            throw nse;
        }
        MiElement obj = null;
        int padding = 0;
        long debugOffset = 0L;
        int paddedOffset = 0;
        try {
            int sliceSize = BufferUtils.toUnsignedInt(this.size);
            ByteBuffer chunk = this.buffer.slice();
            int chunkSize = chunk.limit();
            if (chunkSize < sliceSize) {
                throw new MatFileParsingException(String.format("sliceSize=%d but remaining chunk is only %d", sliceSize, chunkSize));
            }
            ByteBuffer slice = chunk.limit(sliceSize);
            int offset = sliceSize % 8;
            padding = offset != 0 ? 8 - offset : 0;
            debugOffset = MatFile.pad(sliceSize);
            paddedOffset = this.buffer.position() + sliceSize + padding;
            this.buffer.position(paddedOffset);
            switch (this.dataType) {
                case 1: {
                    obj = new MiInt8(slice);
                    break;
                }
                case 2: {
                    obj = new MiUInt8(slice);
                    break;
                }
                case 4: {
                    obj = new MiUInt16(slice);
                    break;
                }
                case 14: {
                    obj = new MiMatrix(this.matFile, slice, this.smallDataTypeFormat);
                    break;
                }
                case 6: {
                    obj = new MiUInt32(slice);
                    break;
                }
                case 5: {
                    obj = new MiInt32(slice);
                    break;
                }
                case 9: {
                    obj = new MiDouble(slice);
                    break;
                }
                default: {
                    int fileOffset = paddedOffset + 128;
                    throw new NoSuchElementException(String.format("padding=%d padding2=%d size=%d dataType=%d, fileOffset=0x%x: %s", paddedOffset, debugOffset, this.size, this.dataType, fileOffset, this.matFile));
                }
            }
        }
        catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                throw (NoSuchElementException)e;
            }
            NoSuchElementException nse = new NoSuchElementException(e.getMessage());
            nse.initCause(e);
            this.hasMore = false;
            throw nse;
        }
        try {
            this.hasMore = false;
            if (this.buffer.remaining() > 0) {
                this.readElementHeader();
                this.hasMore = true;
            }
        }
        catch (Exception e) {
            this.exception = e;
        }
        return obj;
    }

    @Override
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<MiElement> iterator() {
        return this;
    }
}

