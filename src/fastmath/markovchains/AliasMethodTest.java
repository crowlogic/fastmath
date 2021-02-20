package fastmath.markovchains;

import junit.framework.TestCase;

public class AliasMethodTest extends
                             TestCase
{

  public void testIt()
  {
    final double[] q = new double[]
    { 0.25, 0.1, 0.65 };
    AliasMethod am = new AliasMethod(q);
    double p[] = new double[3];
    final int n = 10000000;
    for (int i = 0; i < n; i++)
    {
      final int k = am.next();
      // System.out.println("k=" + k );
      p[k]++;
    }
    for (int i = 0; i < 3; i++)
    {
      System.out.println("p[i]=" + p[i] + " q[i]=" + q[i]);
      final double wtf = p[i] / (double) n;
      p[i] = wtf;
      double diff = Math.abs(p[i] - q[i]);
      System.out.println("p[i]=" + p[i] + " diff[i]=" + diff + "\n");
    }
    for (int i = 0; i < 3; i++)
    {
      System.out.println("p[" + i + "]=" + p[i] + " q[" + i + "]=" + q[i]);
    }

    // System.out.println("q=" + Arrays.toString(p) + " p=" + Arrays.toString(q));
    for (int i = 0; i < 3; i++)
    {
      final String msg = "p[" + i + "]=" + p[i] + " q[" + i + "]=" + q[i];
      System.out.println(msg);
      TestCase.assertEquals(msg, q[i], p[i], 0.001);
      // out.p
    }
  }

}
