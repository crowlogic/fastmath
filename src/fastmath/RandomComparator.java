/*
 * Decompiled with CFR 0.144.
 */
package fastmath;

import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public final class RandomComparator<T>
implements Comparator<T> {
    private final Map<T, Integer> map = new IdentityHashMap<T, Integer>();
    private final Random random;

    public RandomComparator() {
        this(new Random());
    }

    public RandomComparator(Random random) {
        this.random = random;
    }

    @Override
    public int compare(T t1, T t2) {
        return Integer.compare(this.valueFor(t1), this.valueFor(t2));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private int valueFor(T t) {
        Map<T, Integer> map = this.map;
        synchronized (map) {
            return this.map.computeIfAbsent(t, ignore -> this.random.nextInt());
        }
    }
}

