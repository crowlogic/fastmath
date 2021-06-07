package arblib;

import static arblib.arblib.acb_init;

public final class ComplexReference extends
                                           ThreadLocal<acb_struct>
{
  @Override
  protected acb_struct initialValue()
  {
    acb_struct a = new acb_struct();
    acb_init(a);
    return a;
  }
}