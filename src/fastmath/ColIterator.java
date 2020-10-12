/*
 * Decompiled with CFR 0.144.
 */
package fastmath;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ColIterator<V extends Vector> implements
                        Iterator<V>,
                        Iterable<V>
{
  final AbstractMatrix x;
  int i;

  public ColIterator(AbstractMatrix x)
  {
    this.x = x;
    reset();
  }

  public void reset()
  {
    this.i = 0;
  }
  
  @Override
  public boolean hasNext()
  {
    return this.i < this.x.getColCount();
  }

  @Override
  public V next()
  {
    ++this.i;
    return (V) this.x.col(this.i - 1);
  }

  @Override
  public void remove()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public Iterator<V> iterator()
  {
    return this;
  }

  public Stream<V> stream(boolean parallel)
  {
    return StreamSupport.stream(this.spliterator(), parallel);
  }
}
