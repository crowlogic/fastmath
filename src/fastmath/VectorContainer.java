/*
 * Decompiled with CFR 0.144.
 */
package fastmath;

import java.io.Serializable;

public class VectorContainer
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Vector vector;

    public Vector getVector(int size) {
        if (this.vector == null) {
            this.vector = new Vector(size);
        }
        assert (size == this.vector.size());
        return this.vector;
    }

    public Vector getVector() {
        return this.vector;
    }
}

