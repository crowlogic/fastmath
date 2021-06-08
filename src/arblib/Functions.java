package arblib;

import static arblib.arblib.arf_get_d;

public final class Functions
{
  public static final int prec = 128;
  
  public static String point(acb_struct faze)
  {
    return point(faze.getReal()) + " + I" + point(faze.getImag());
  }

  public static double point(arb_struct faze)
  {
    return arf_get_d(faze.getMid(), prec);
  }
}
