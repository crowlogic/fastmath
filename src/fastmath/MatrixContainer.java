/*
 * Decompiled with CFR 0.144.
 */
package fastmath;

public class MatrixContainer {
    private DoubleColMatrix matrix;

    public DoubleColMatrix getMatrix(int m, int n) {
        if (this.matrix == null) {
            this.matrix = new DoubleColMatrix(m, n);
        }
        assert (m == this.matrix.getRowCount());
        assert (n == this.matrix.getColCount());
        return this.matrix;
    }
}

