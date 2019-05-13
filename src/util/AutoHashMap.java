/*
 * Decompiled with CFR 0.144.
 */
package util;

import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import util.AutoMap;

public class AutoHashMap<K, V>
extends ConcurrentHashMap<K, V>
implements AutoMap<K, V> {
    private static final long serialVersionUID = 1L;
    final Class<? super V> valueClass;
    private Function<K, V> constructor;

    public AutoHashMap(Class<? super V> valueClass) {
        this.valueClass = valueClass;
    }

    public AutoHashMap(Function<K, V> constructor) {
        this.constructor = constructor;
        this.valueClass = null;
    }

    public AutoHashMap() {
        this.valueClass = null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public V getOrCreate(K key) {
        V value = this.get(key);
        if (value == null) {
            AutoHashMap autoHashMap = this;
            synchronized (autoHashMap) {
                try {
                    value = this.valueClass == null ? this.newValueInstance(key) : (V) this.valueClass.getConstructor(new Class[0]).newInstance(new Object[0]);
                    V previousValue = this.putIfAbsent(key, value);
                    if (previousValue != null) {
                        value = previousValue;
                    } else {
                        this.newValueInserted(key, value);
                    }
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return value;
    }

    protected void newValueInserted(K key, V value) {
    }

    @Override
    public V newValueInstance(K key) {
        if (this.constructor != null) {
            return this.constructor.apply(key);
        }
        throw new UnsupportedOperationException("newInstance() must be overriden if not instantiated with a value class or constructor function");
    }
}

