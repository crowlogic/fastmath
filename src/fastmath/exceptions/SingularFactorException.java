package fastmath.exceptions;

import fastmath.AbstractMatrix;

public class SingularFactorException extends
                                     FastMathException
{
  private final AbstractMatrix factor;
  private static final long serialVersionUID = 1L;

  public SingularFactorException(String msg, AbstractMatrix factor)
  {
    super(msg);
    this.factor = factor;
  }

  public AbstractMatrix getFactor()
  {
    return this.factor;
  }
}
