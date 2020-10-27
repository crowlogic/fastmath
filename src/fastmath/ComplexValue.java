package fastmath;

import com.sun.jna.Pointer;
import com.sun.jna.Structure.ByValue;

public class ComplexValue extends
                            Complex implements
                            ByValue
{

  public ComplexValue()
  {
    super();
  }

  public ComplexValue(double real, double imag)
  {
    super(real,
          imag);
  }

  public ComplexValue(Pointer p)
  {
    super(p);
  }


}
