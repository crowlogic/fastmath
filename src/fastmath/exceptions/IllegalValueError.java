/*
 * Decompiled with CFR 0.144.
 */
package fastmath.exceptions;

public class IllegalValueError extends
                               FastMathException
{
  private static final long serialVersionUID = 1L;
  private final int param;

  public IllegalValueError(String msg, int p)
  {
    super(msg + ": param " + p + " had an illegal value");
    this.param = p;
  }

  public int getParam()
  {
    return this.param;
  }
}
