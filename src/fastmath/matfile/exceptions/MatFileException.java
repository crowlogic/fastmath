package fastmath.matfile.exceptions;

public class MatFileException extends
                              Exception
{
  private static final long serialVersionUID = 1L;

  public MatFileException(Exception e)
  {
    super(e);
  }

  public MatFileException(Exception e, String msg)
  {
    super(msg == null ? e.getMessage() : e.getMessage() + ":" + msg);
    this.initCause(e);
  }

  public MatFileException(String string)
  {
    super(string);
  }
}
