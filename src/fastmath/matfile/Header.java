/*
 * Decompiled with CFR 0.144.
 */
package fastmath.matfile;

import java.nio.ByteBuffer;

import fastmath.AbstractBufferedObject;
import fastmath.BufferUtils;

public class Header extends
                    AbstractBufferedObject
{
  private static final long serialVersionUID = 1L;
  public static final int TEXT_LEN = 124;
  public static final int HEADER_LEN = 128;

  public Header()
  {
    super(128);
  }

  public Header(ByteBuffer byteBuffer)
  {
    super(byteBuffer);
  }

  public Header(ByteBuffer buffer, String header)
  {
    this(buffer);
    BufferUtils.copy(header, buffer);
  }

  public Header(String header)
  {
    this();
    int i;
    int length = header.length();
    for (i = 0; i < length && i < 124; ++i)
    {
      this.buffer.put((byte) header.charAt(i));
    }
    while (i < 124)
    {
      this.buffer.put((byte) 32);
      ++i;
    }
    this.buffer.putShort((short) 256);
    this.buffer.putShort((short) 19785);
    this.buffer.flip();
  }

  @Override
  public ByteBuffer getBuffer()
  {
    return this.buffer;
  }
}
