package util;

import java.util.TreeMap;
import java.util.function.Function;

public class AutoTreeMap<K, V> extends
                        TreeMap<K, V> implements
                        AutoMap<K, V>
{
  @Override
  public synchronized V get(Object key)
  {
    return super.get(key);
  }

  private static final long serialVersionUID = 1L;
  final Class<? super V> valueClass;
  private Function<K, V> constructor;

  public AutoTreeMap(Class<? super V> valueClass)
  {
    this.valueClass = valueClass;
  }

  public AutoTreeMap(Function<K, V> constructor)
  {
    this.constructor = constructor;
    this.valueClass = null;
  }

  public AutoTreeMap()
  {
    this.valueClass = null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public synchronized V getOrCreate(K key)
  {
    V value = this.get(key);
    if (value == null)
    {
      AutoTreeMap<K, V> autoHashMap = this;
      synchronized (autoHashMap)
      {
        try
        {
          value = this.valueClass == null ? this.newValueInstance(key)
                        : (V) this.valueClass.getConstructor(new Class[0]).newInstance(new Object[0]);
          V previousValue = this.putIfAbsent(key, value);
          if (previousValue != null)
          {
            value = previousValue;
          }
          else
          {
            this.newValueInserted(key, value);
          }
        }
        catch (Exception e)
        {
          throw new RuntimeException(e);
        }
      }
    }
    return value;
  }

  protected void newValueInserted(K key, V value)
  {
  }

  @Override
  public V newValueInstance(K key)
  {
    if (this.constructor != null)
    {
      return this.constructor.apply(key);
    }
    throw new UnsupportedOperationException("newInstance() must be overriden if not instantiated with a value class or constructor function");
  }
}
