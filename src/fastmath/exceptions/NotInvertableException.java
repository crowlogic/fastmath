/*
 * Decompiled with CFR 0.144.
 */
package fastmath.exceptions;

public class NotInvertableException extends
                                    FastMathException
{
  private static final long serialVersionUID = 1L;
  private final int factor;

  public NotInvertableException(int factor)
  {
    super("the (" + factor + "," + factor
                  + ") element of the factor U or L is zero, and the inverse could not be computed");
    this.factor = factor;
  }

  public int getFactor()
  {
    return this.factor;
  }
}
