/*
 * Decompiled with CFR 0.144.
 */
package fastmath.matfile;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;

public interface Writable {
    public void write(SeekableByteChannel var1) throws IOException;
}

