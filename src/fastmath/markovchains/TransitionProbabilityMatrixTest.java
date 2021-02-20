package fastmath.markovchains;

import static java.lang.System.out;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fastmath.Vector;

public class TransitionProbabilityMatrixTest
{

  @Before
  public void setUp() throws Exception
  {
  }

  @Test
  public void test()
  {
    TransitionProbabilityMatrix tpm = new TransitionProbabilityMatrix();
    tpm.add(0, 0, 0.1);
    tpm.add(2, 3, 1.4);
    tpm.add(1, 1, 0.5);
    tpm.add(4, 2, 0.7);
    tpm.add(3, 2, 0.6);
    tpm.add(3, 4, 0.55);
    out.println("normalizing " + tpm);
    tpm.normalize();
    tpm.rowSum().forEach(x ->
    {
      assertEquals(1.0, x.doubleValue(), 0.000000001);
    });
  }

  @Test

  public void testStationaryDistribution()
  {
    TransitionProbabilityMatrix tpm = new TransitionProbabilityMatrix(new double[][]
    {
      { 0, 1, 2 },
      { 3, 0, 4 },
      { 5, 6, 0 } });
    tpm.normalize();

    final Vector π = tpm.getStationaryDistribution();
    out.println("π=" + π);

  }

  @Test

  public void testTransitions()
  {
    TransitionProbabilityMatrix tpm = new TransitionProbabilityMatrix(new double[][]
    {
      { 0, 1, 2 },
      { 3, 0, 4 },
      { 5, 6, 0 } });
    tpm.normalize();

    final Vector π = tpm.getStationaryDistribution();
    final AliasMethod aliasMethod = new AliasMethod(π.toDoubleArray());
    int sk = aliasMethod.next();
    double freqs[] = new double[π.size()];

    final int n = 50000000;
    for (int i = 0; i < n; i++)
    {
      freqs[sk]++;
      int newsk = tpm.transition(sk);
      sk = newsk;
    }
    for (int i = 0; i < π.size(); i++)
    {
      freqs[i] = freqs[i] / n;
      assertEquals(freqs[i], π.get(i), 0.001);

      out.format("p[%d]=%f should equal π[%d]=%f\n", i, freqs[i], i, π.get(i));
    }

  }

}
