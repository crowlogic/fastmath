/*
 * Decompiled with CFR 0.144.
 */
package fastmath.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class BufferTools {
    public static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void reverseBytes(byte[] b, int i, int j) {
        throw new UnsupportedOperationException("TODO");
    }

    public static void fastChannelCopy(ReadableByteChannel src, WritableByteChannel dest) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(16384);
        while (src.read(buffer) != -1) {
            buffer.flip();
            dest.write(buffer);
            buffer.compact();
        }
        buffer.flip();
        while (buffer.hasRemaining()) {
            dest.write(buffer);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void fastChannelCopy(InputStream inputStream, OutputStream outputStream) throws IOException {
        ReadableByteChannel inputChannel = Channels.newChannel(inputStream);
        WritableByteChannel outputChannel = Channels.newChannel(outputStream);
        try {
            BufferTools.fastChannelCopy(inputChannel, outputChannel);
        }
        finally {
            inputChannel.close();
            outputChannel.close();
        }
    }
}

