/*
 * Decompiled with CFR 0.144.
 */
package fastmath;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RowIterator<V extends Vector>
implements Iterator<V>,
Iterable<V> {
    final AbstractMatrix x;
    int i;

    public RowIterator(AbstractMatrix abstractMatrix) {
        this.x = abstractMatrix;
        this.i = 0;
    }

    @Override
    public boolean hasNext() {
        return this.i < this.x.getRowCount();
    }

    @Override
    public V next() {
        ++this.i;
        return (V)this.x.row(this.i - 1);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<V> iterator() {
        return this;
    }

    public Stream<V> stream(boolean parallel) {
        return StreamSupport.stream(this.spliterator(), parallel);
    }
}

