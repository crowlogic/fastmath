package util;

import java.util.Map;

public interface AutoMap<K, V> extends
                        Map<K, V>
{
  public V getOrCreate(K var1);

  public V newValueInstance(K var1);
}
