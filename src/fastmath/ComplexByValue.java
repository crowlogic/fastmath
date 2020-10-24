package fastmath;

import com.sun.jna.Pointer;
import com.sun.jna.Structure.ByValue;

public class ComplexByValue extends
                            Complex implements
                            ByValue
{

  public ComplexByValue()
  {
    super();
  }

  public ComplexByValue(double real, double imag)
  {
    super(real,
          imag);
  }

  public ComplexByValue(Pointer p)
  {
    super(p);
  }


}
