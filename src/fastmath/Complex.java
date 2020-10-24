package fastmath;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class Complex extends
                     Structure 
{

  public double realPart;
  public double imaginaryPart;

  @Override
  protected List<String> getFieldOrder()
  {
    return Arrays.asList("realPart", "imaginaryPart");
  }
  
  public Complex()
  {
    
  }

  public Complex(double real, double imag)
  {
    this.realPart = real;
    this.imaginaryPart = imag;
  }

  public Complex(Pointer p)
  {
    super(p);
  }

}
