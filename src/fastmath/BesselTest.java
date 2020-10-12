package fastmath;

import junit.framework.TestCase;

public class BesselTest extends
                        TestCase
{

  public void testBessel()
  {
    double wtf = Bessel.i(30, 1, true);
    assertEquals( 0.07191633059864756, wtf, 0.000001 );
    System.out.println( "wtf=" + wtf );
    double wtfe = Bessel.i(30, 1, false);
    assertEquals(768532038938.957, wtfe, 0.000001 );
    System.out.println( "wtfe=" + wtfe );
  }

}
