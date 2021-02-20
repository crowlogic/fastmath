package fastmath.markovchains;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import org.apache.commons.math3.random.RandomGenerator;

/**
 * Sample from a discrete distribution
 *
 */
public final class AliasMethod
{
  @Override
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("AliasMethod[probability=");
    for (int i = 0; i < probability.length; i++)
    {
      sb.append(probability(i) + ",");
    }
    sb.append("]");
    return sb.toString();
  }

  /* The random number generator used to sample from the distribution. */
  private final RandomGenerator generator;

  /* The probability and alias tables. */
  private final int[] alias;
  public final double[] probability;

  public double[] origProbabilities;

  /**
   * Constructs a new AliasMethod to sample from a discrete distribution and hand
   * back outcomes based on the probability distribution.
   * <p>
   * Given as input a list of probabilities corresponding to outcomes 0, 1, ..., n
   * - 1, this constructor creates the probability and alias tables needed to
   * efficiently sample from this distribution.
   *
   * @param probabilities The list of probabilities.
   */
  public AliasMethod(double[] probabilities)
  {
//    this(probabilities,
//         new Well19937a());
    this(probabilities,
         null);
  }

  /**
   * Constructs a new AliasMethod to sample from a discrete distribution and hand
   * back outcomes based on the probability distribution.
   * <p>
   * Given as input a list of probabilities corresponding to outcomes 0, 1, ..., n
   * - 1, along with the random number generator that should be used as the
   * underlying generator, this constructor creates the probability and alias
   * tables needed to efficiently sample from this distribution.
   *
   * @param probabilities The list of probabilities.
   * @param random        The random number generator
   */
  public AliasMethod(double[] probabilities, RandomGenerator random)
  {
    origProbabilities = probabilities;
    probabilities = Arrays.copyOf(probabilities, probabilities.length);
    /* Begin by doing basic structural checks on the inputs. */
//    if (probabilities == null || random == null)
//      throw new NullPointerException();
    if (probabilities.length == 0)
      throw new IllegalArgumentException("Probability vector must be nonempty.");

    /* Allocate space for the probability and alias tables. */
    probability = probabilities;
    alias = new int[probabilities.length];

    /* Store the underlying generator. */
    this.generator = random;

    /* Compute the average probability and cache it for later use. */
    final double average = 1.0 / probabilities.length;

    /* Create two stacks to act as worklists as we populate the tables. */
    Deque<Integer> small = new ArrayDeque<Integer>();
    Deque<Integer> large = new ArrayDeque<Integer>();

    /* Populate the stacks with the input probabilities. */
    for (int i = 0; i < probabilities.length; ++i)
    {
      /*
       * If the probability is below the average probability, then we add it to the
       * small list; otherwise we add it to the large list.
       */
      if (probabilities[i] >= average)
        large.add(i);
      else
        small.add(i);
    }

    /*
     * As a note: in the mathematical specification of the algorithm, we will always
     * exhaust the small list before the big list. However, due to floating point
     * inaccuracies, this is not necessarily true. Consequently, this inner loop
     * (which tries to pair small and large elements) will have to check that both
     * lists aren't empty.
     */
    while (!small.isEmpty() && !large.isEmpty())
    {
      /* Get the index of the small and the large probabilities. */
      int less = small.removeLast();
      int more = large.removeLast();

      /*
       * These probabilities have not yet been scaled up to be such that 1/n is given
       * weight 1.0. We do this here instead.
       */
      probability[less] = probabilities[less] * probabilities.length;
      alias[less] = more;

      /*
       * Decrease the probability of the larger one by the appropriate amount.
       */
      probabilities[more] = (probabilities[more] + probabilities[less]) - average;

      /*
       * If the new probability is less than the average, add it into the small list;
       * otherwise add it to the large list.
       */
      if (probabilities[more] >= 1.0 / probabilities.length)
        large.add(more);
      else
        small.add(more);
    }

    /*
     * At this point, everything is in one list, which means that the remaining
     * probabilities should all be 1/n. Based on this, set them appropriately. Due
     * to numerical issues, we can't be sure which stack will hold the entries, so
     * we empty both.
     */
    while (!small.isEmpty())
      probability[small.removeLast()] = 1.0;
    while (!large.isEmpty())
      probability[large.removeLast()] = 1.0;
  }

  /**
   * Samples a value from the underlying distribution.
   *
   * @return A random value sampled from the underlying distribution.
   */
  public int next()
  {
    /* Generate a fair die roll to determine which column to inspect. */
    int column = generator.nextInt(probability.length);

    /* Generate a biased coin toss to determine which option to pick. */
    boolean coinToss = generator.nextDouble() < probability[column];

    /* Based on the outcome, return either the column or its alias. */
    return coinToss ? column : alias[column];
  }

  public double probability(int i)
  {
    return origProbabilities[i];
  }
}