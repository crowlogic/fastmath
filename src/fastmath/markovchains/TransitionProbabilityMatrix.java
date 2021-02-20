package fastmath.markovchains;

import static java.util.stream.IntStream.range;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.atomic.DoubleAdder;

import org.apache.commons.math3.random.JDKRandomGenerator;

import fastmath.AbstractMatrix;
import fastmath.DoubleColMatrix;
import fastmath.Pair;
import fastmath.SquareDoubleColMatrix;
import fastmath.Vector;

public class TransitionProbabilityMatrix extends
                                         SquareDoubleColMatrix
{

  private JDKRandomGenerator rng = new JDKRandomGenerator();

  private AliasMethod[] samplers;

  @Override
  public double add(int i, int j, double x)
  {
    assert i >= 0 : "row, i, must be nonnegative";
    assert j >= 0 : "row, j, must be nonnegative";

    int r = Math.max(i, j);
    int prevr = numRows;
    if (r >= prevr)
    {
      resize(r + 1, r + 1);
    }
    return super.add(i, j, x);
  }

  public TransitionProbabilityMatrix()
  {
  }

  public TransitionProbabilityMatrix(AbstractMatrix x)
  {
    super(x);
  }

  public TransitionProbabilityMatrix(ByteBuffer buffer, int baseOffset, int dim)
  {
    super(buffer,
          baseOffset,
          dim);
  }

  public TransitionProbabilityMatrix(ByteBuffer buffer, int dim)
  {
    super(buffer,
          dim);
  }

  public TransitionProbabilityMatrix(double[][] arr)
  {
    super(arr);
  }

  public TransitionProbabilityMatrix(String name)
  {
    super(0,
          name);

  }

  public TransitionProbabilityMatrix(int dim, String name)
  {
    super(dim,
          name);

  }

  public TransitionProbabilityMatrix(int dim)
  {
    super(dim);
  }

  public TransitionProbabilityMatrix(List<Pair<Double, Double>> twoColMatrix)
  {
    super(twoColMatrix);
  }

  public TransitionProbabilityMatrix(ByteBuffer buffer, int baseOffset, int numRows, int numCols)
  {
    super(buffer,
          baseOffset,
          numRows,
          numCols);
  }

  @Override
  public TransitionProbabilityMatrix copy(boolean reuseBuffer)
  {
    return reuseBuffer ? new TransitionProbabilityMatrix(buffer,
                                                         getBaseOffset(),
                                                         numRows,
                                                         numCols) :
      new TransitionProbabilityMatrix(this);
  }

  /**
   * Normalize the matrix so the row sums are equal to 1 (conservation of
   * probability)
   * 
   * @return this
   */
  public Vector normalize()
  {
    Vector rowSums = new Vector(getRowCount());

    rows().forEach(row ->
    {
      double measure = row.sum();
      // System.out.format("normalizing row idx=%d measure=%.3f %s\n", row.getIndex(),
      // measure, row );
      rowSums.set(row.getIndex(), measure);
      if (measure > 0)
      {
        row.divide(measure);
      }
    });
    return rowSums;
  }

  public int transition(int current)
  {
    if (samplers == null)
    {
      samplers = new AliasMethod[getRowCount()];
      for (int i = 0; i < getRowCount(); i++)
      {
        samplers[i] = new AliasMethod(row(i).toDoubleArray(),
                                      rng);
      }
    }
    return samplers[current].next();
  }

  public Vector getStationaryDistribution()
  {
    DoubleColMatrix p = new DoubleColMatrix(this);
    DoubleColMatrix q = null;

    for (int i = 0; i < 100; i++)
    {
      q = p.prod(p);
      final int j = i;
      final DoubleAdder adder = new DoubleAdder();
      range(0, getColCount()).forEachOrdered(col ->
      {
        final double stdev = p.col(col).getStdev();
        adder.add(stdev);
        // out.println( "i=" + j + " row=" + col + " stdev=" + stdev );
      });
      if (p.row(0).sum() > 0)
      {
        p.assign(q);
        final double stdsum = adder.doubleValue();
        if (stdsum < 0.000000000000001)
        {
          break;
        }
        // System.out.println( "p=" + p + " stdsum=" + stdsum );

      }
      else
      {
        break;
      }
    }
//    new Exception().printStackTrace();
//
//    System.out.println("Returning stationary spread distribution " + p.row(0));   
    return p.row(0);
    // return p.row(0).divide(p.row(0).sum());
  }
}
