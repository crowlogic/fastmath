package fastmath.io;

public class ByteUtils
{
  private static final long lowerEight = 255L;

  public static short swap(short value)
  {
    int b1 = value & 255;
    int b2 = value >> 8 & 255;
    return (short) (b1 << 8 | b2 << 0);
  }

  public static int swap(int value)
  {
    int b1 = value >> 0 & 255;
    int b2 = value >> 8 & 255;
    int b3 = value >> 16 & 255;
    int b4 = value >> 24 & 255;
    return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;
  }

  public static long swap(long value)
  {
    long b1 = value >> 0 & 255L;
    long b2 = value >> 8 & 255L;
    long b3 = value >> 16 & 255L;
    long b4 = value >> 24 & 255L;
    long b5 = value >> 32 & 255L;
    long b6 = value >> 40 & 255L;
    long b7 = value >> 48 & 255L;
    long b8 = value >> 56 & 255L;
    return b1 << 56 | b2 << 48 | b3 << 40 | b4 << 32 | b5 << 24 | b6 << 16 | b7 << 8 | b8 << 0;
  }

  public static float swap(float value)
  {
    int intValue = Float.floatToIntBits(value);
    intValue = ByteUtils.swap(intValue);
    return Float.intBitsToFloat(intValue);
  }

  public static double swap(double value)
  {
    long longValue = Double.doubleToLongBits(value);
    longValue = ByteUtils.swap(longValue);
    return Double.longBitsToDouble(longValue);
  }

  public static void swap(short[] array)
  {
    for (int i = 0; i < array.length; ++i)
    {
      array[i] = ByteUtils.swap(array[i]);
    }
  }

  public static void swap(int[] array)
  {
    for (int i = 0; i < array.length; ++i)
    {
      array[i] = ByteUtils.swap(array[i]);
    }
  }

  public static void swap(long[] array)
  {
    for (int i = 0; i < array.length; ++i)
    {
      array[i] = ByteUtils.swap(array[i]);
    }
  }

  public static void swap(float[] array)
  {
    for (int i = 0; i < array.length; ++i)
    {
      array[i] = ByteUtils.swap(array[i]);
    }
  }

  public static void swap(double[] array)
  {
    for (int i = 0; i < array.length; ++i)
    {
      array[i] = ByteUtils.swap(array[i]);
    }
  }

  public static long bytesToLong(byte[] bytes)
  {
    if (bytes.length > 8)
    {
      throw new IllegalArgumentException("length cannot be greater than 8 ");
    }
    long val = 0L;
    for (int i = 0; i < bytes.length; ++i)
    {
      val |= (long) bytes[i] << (7 - i) * 8 & 255L << (7 - i) * 8;
    }
    return val;
  }

  public static long bytesToLongTrimmed(byte[] bytes)
  {
    byte b;
    if (bytes.length > 8)
    {
      throw new IllegalArgumentException("length cannot be greater than 8 ");
    }
    long val = 0L;
    for (int i = 0; i < bytes.length && (b = bytes[i]) != 0 && b != 32; ++i)
    {
      val |= (long) b << (7 - i) * 8 & 255L << (7 - i) * 8;
    }
    return val;
  }

  public static byte[] longToBytes(long val)
  {
    return ByteUtils.longToBytes(val, new byte[8]);
  }

  public static byte[] longToBytes(long val, byte[] bytes)
  {
    for (int i = 0; i < 8 && i < bytes.length; ++i)
    {
      bytes[i] = (byte) (val >> (7 - i) * 8 & 255L);
    }
    return bytes;
  }

  public static String hexString(byte[] bytes)
  {
    StringBuffer sb = new StringBuffer("0x");
    for (byte b : bytes)
    {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  public static String longToString(long key)
  {
    return new String(ByteUtils.longToBytes(key)).trim();
  }
}
