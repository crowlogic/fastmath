package fastmath;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BufferUtils
{
  public static final int BYTES_PER_INTEGER = 4;

  public static native ByteBuffer copy(ByteBuffer var0, ByteBuffer var1);

  public static void putUnsignedInt(ByteBuffer buffer, long v)
  {
    buffer.putInt((int) (v & 0xFFFFFFFFL));
  }

  public static void putUnsignedInt(ByteBuffer intBuffer, int byteOffset, long value)
  {
    intBuffer.putInt(byteOffset, (int) (value & 0xFFFFFFFFL));
  }

  public static void putUnsignedShort(ByteBuffer buffer, int s)
  {
    buffer.putShort((short) (s & 65535));
  }

  public static ByteBuffer newNativeBuffer(int numBytes)
  {
    assert (numBytes >= 0) : "size must be non-negative, was " + numBytes;
    return ByteBuffer.allocateDirect(numBytes).order(ByteOrder.nativeOrder());
  }

  public static IntBuffer newIntBuffer(int numInts)
  {
    return BufferUtils.newNativeBuffer(numInts * 4).asIntBuffer();
  }

  public static FloatBuffer newFloatBuffer(int numValues)
  {
    return BufferUtils.newNativeBuffer(numValues * 4).asFloatBuffer();
  }

  public static ByteBuffer copy(CharBuffer src, ByteBuffer dest)
  {
    int i;
    int length = src.length();
    for (i = 0; i < length; ++i)
    {
      dest.put(i, (byte) src.get(i));
    }
    while (i < dest.limit())
    {
      dest.put(i, (byte) 0);
      ++i;
    }
    return dest;
  }

  public static short getUnsignedByte(ByteBuffer buffer)
  {
    return (short) (buffer.get() & 255);
  }

  public static int getUnsignedShort(ByteBuffer buffer)
  {
    return buffer.getShort() & 65535;
  }

  public static long getUnsignedInt(ByteBuffer buffer)
  {
    return (long) buffer.getInt() & 0xFFFFFFFFL;
  }

  public static int getUnsignedShort(ShortBuffer shortBuffer, int index)
  {
    return shortBuffer.get(index) & 65535;
  }

  public static long getUnsignedInt(ByteBuffer intBuffer, int index)
  {
    return (long) intBuffer.getInt(index) & 0xFFFFFFFFL;
  }

  public static int toUnsignedInt(long x)
  {
    int cast = (int) x;
    if ((long) cast == x)
    {
      return cast;
    }
    throw new IllegalArgumentException("Signed long " + x + " cannot be cast to positive signed integer");
  }

  public static ByteBuffer copy(String string, ByteBuffer byteBuffer)
  {
    ByteBuffer dupe = byteBuffer.duplicate();
    for (int i = 0; i < string.length() && dupe.hasRemaining(); ++i)
    {
      dupe.put((byte) string.charAt(i));
    }
    return dupe;
  }

  public static String print(ByteBuffer buffer)
  {
    StringBuffer sb = new StringBuffer();
    int lim = buffer.limit();
    for (int i = 0; i < lim; ++i)
    {
      sb.append(String.format("%x", buffer.get(i)));
      if (i % 8 != 7)
        continue;
      sb.append(" ");
    }
    return sb.toString().trim();
  }

  public static IntBuffer allocateDirectIntBuffer(int numInts)
  {
    System.gc();
    return ByteBuffer.allocateDirect(numInts * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
  }

  public static HashSet<Integer> toHashSet(IntBuffer buffer)
  {
    int limit = buffer.limit();
    HashSet<Integer> ints = new HashSet<Integer>(limit);
    for (int i = 0; i < limit; ++i)
    {
      ints.add(buffer.get(i));
    }
    return ints;
  }

  public static List<Integer> toList(IntBuffer buffer)
  {
    int limit = buffer.limit();
    ArrayList<Integer> list = new ArrayList<Integer>(limit);
    for (int i = 0; i < limit; ++i)
    {
      list.add(buffer.get(i));
    }
    return list;
  }

  public static ByteBuffer toByteBuffer(IntBuffer buffer)
  {
    List<Integer> list = BufferUtils.toList(buffer);
    return BufferUtils.integerListToByteBuffer(list);
  }

  public static ByteBuffer integerListToByteBuffer(List<Integer> termIds)
  {
    ByteBuffer byteBuffer = ByteBuffer.allocate(termIds.size() * 4).order(ByteOrder.nativeOrder());
    IntBuffer intBuffer = byteBuffer.asIntBuffer();
    for (Integer termId : termIds)
    {
      intBuffer.put(termId);
    }
    return byteBuffer;
  }

  public static ByteBuffer arrayToBuffer(byte[] bytes)
  {
    return ByteBuffer.wrap(bytes).order(ByteOrder.nativeOrder());
  }

  public static native long bufferAddress(Buffer var0);
}
