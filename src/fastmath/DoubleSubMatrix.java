/*
 * Decompiled with CFR 0.144.
 */
package fastmath;

import java.nio.ByteBuffer;

public class DoubleSubMatrix
extends DoubleMatrix {
    private final int columnIncrement;
    private final boolean isTranspose;
    private final int baseOffset;
    private final int rowIncrement;

    public DoubleSubMatrix(ByteBuffer buffer, int numRows, int numColumns, int offset, int rowIncrement, int columnIncrement, boolean isTranspose) {
        super(buffer, numRows, numColumns);
        this.baseOffset = offset;
        this.rowIncrement = rowIncrement;
        this.columnIncrement = columnIncrement;
        this.isTranspose = isTranspose;
    }

    @Override
    public Vector asVector() {
        return super.asVector();
    }

    public DoubleMatrix copy(boolean reuseBuffer) {
        if (this.getColIncrement() == 1) {
            return new DoubleRowMatrix(this);
        }
        return new DoubleColMatrix(this);
    }

    @Override
    public int getColIncrement() {
        return this.columnIncrement;
    }

    @Override
    public int getOffset(int i, int j) {
        return this.baseOffset + i * this.rowIncrement * 8 + j * this.columnIncrement * 8;
    }

    @Override
    public int getRowIncrement() {
        return this.rowIncrement;
    }

    @Override
    public boolean isTranspose() {
        return this.isTranspose;
    }

    @Override
    public int getMainIncrement() {
        throw new UnsupportedOperationException();
    }
}

