package fastmath;

import static java.lang.Math.pow;

public class MittagLefflerDistribution
{
  public MittagLefflerDistribution(double α, double λ)
  {
    super();
    this.α = α;
    this.λ = λ;
  }

  public double α;
  
  public double λ;
  
  public double pdf( double t )
  {
    // TODO: derivative here
    return MittagLefflerFunction.lib.mlf(α, α, -λ*pow(t,α) );
  }
  
  public double cdf( double t )
  {
    return MittagLefflerFunction.lib.mlf(α, α, -λ*pow(t,α) );
  }

}
