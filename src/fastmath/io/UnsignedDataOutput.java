package fastmath.io;

import java.io.IOException;
import java.math.BigInteger;

public interface UnsignedDataOutput
{
  public void writeUnsignedInt(long var1) throws IOException;

  public boolean isLE();

  public void writeDoubles(double[] var1) throws IOException;

  public void writeUnsignedLong(BigInteger var1) throws IOException;

  public void writeUnsignedShort(int var1) throws IOException;
}
