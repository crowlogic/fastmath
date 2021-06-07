package fastmath;

public class ExpandableVector extends
                              Vector
{
  @Override
  public Vector assign(double... x)
  {
    try
    {
      return super.assign(x);
    }
    catch (Throwable t)
    {
      this.expandVector(x.length);
      return super.assign(x);
    }
  }

  public ExpandableVector()
  {
    super(0);
  }

  public ExpandableVector(int newSize)
  {
    super(newSize);
  }

  public ExpandableVector(String name, int size)
  {
    super(name,
          size);
  }

  @Override
  public Vector set(int i, double x)
  {
    if (i >= this.size())
    {
      this.expandVector(i + 1);
    }
    return super.set(i, x);
  }

  @Override
  public double get(int i)
  {
    try
    {
      return super.get(i);
    }
    catch (Throwable t)
    {
      return 0.0;
    }
  }

  @Override
  public double add(int i, double x)
  {
    if (i >= this.size())
    {
      this.expandVector(i + 1);
    }
    return super.add(i, x);
  }

  private void expandVector(int newSize)
  {
    if (newSize <= this.size)
    {
      return;
    }
    ExpandableVector newVector = new ExpandableVector(newSize);
    newVector.setName(this.getName());
    int minLength = Math.min(this.size(), newSize);
    newVector.slice(0, minLength).assign(this.slice(0, minLength));
    this.size = newVector.size;
    this.buffer = newVector.buffer;
  }

  @Override
  public Vector copyAndAppend(double x)
  {
    this.set(this.size, x);
    return this;
  }
}
