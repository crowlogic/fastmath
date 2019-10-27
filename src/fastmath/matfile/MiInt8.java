/*
 * Decompiled with CFR 0.144.
 */
package fastmath.matfile;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

import fastmath.BufferUtils;
import fastmath.matfile.exceptions.MatFileException;

public class MiInt8 extends
                    MiElement
{
  private static final long serialVersionUID = 1L;
  public static final int miINT8 = 1;
  private String bufferAsString = null;

  protected MiInt8(ByteBuffer slice)
  {
    super(slice);
  }

  protected MiInt8(String string)
  {
    this(ByteBuffer.allocateDirect(string.length()));
    BufferUtils.copy(CharBuffer.wrap(string), this.getBuffer());
  }

  public MiInt8(ByteBuffer buffer, long numberOfBytes, long pos) throws MatFileException
  {
    super(buffer.slice().limit(BufferUtils.toUnsignedInt(numberOfBytes)));
  }

  @Override
  public int getDataType()
  {
    return 1;
  }

  public String asString()
  {
    if (this.bufferAsString == null)
    {
      byte[] ab = new byte[this.getBuffer().limit()];
      for (int i = 0; i < this.getBuffer().limit(); ++i)
      {
        ab[i] = this.getBuffer().get(i);
      }
      this.bufferAsString = new String(ab,
                                       StandardCharsets.UTF_8);
    }
    return this.bufferAsString;
  }

  public CharBuffer getCharBuffer()
  {
    return this.getBuffer().asCharBuffer();
  }

  public String toString()
  {
    return this.asString();
  }

  public Object toObject()
  {
    throw new UnsupportedOperationException();
  }
}
