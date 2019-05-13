/*
 * Decompiled with CFR 0.144.
 */
package fastmath;

import fastmath.AbstractMatrix;
import fastmath.DoubleColMatrix;
import fastmath.DoubleMatrix;
import java.nio.ByteBuffer;

public class ExpandableDenseDoubleMatrix
extends DoubleColMatrix {
    private int currentRow = -1;
    private double rowStorageExpansionFactor = 1.5;

    public ExpandableDenseDoubleMatrix(String name, int m, int n) {
        super(m, n);
        this.name = name;
    }

    public ExpandableDenseDoubleMatrix(int m, int n) {
        super(m, n);
    }

    public int compactify() {
        int shrunkNumRows = this.currentRow + 1;
        if (shrunkNumRows != this.getRowCount()) {
            int numTrimmed = this.getRowCount() - shrunkNumRows;
            this.safelyChangeNumRows(this.currentRow + 1);
            return numTrimmed;
        }
        return 0;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public int getCurrentRow() {
        return this.currentRow;
    }

    public int addRow() {
        ++this.currentRow;
        if (this.currentRow >= this.getRowCount()) {
            this.growRows();
        }
        return this.currentRow;
    }

    public void growRows() {
        this.safelyChangeNumRows((int)((double)(this.getRowCount() + 1) * this.rowStorageExpansionFactor));
    }

    private void safelyChangeNumRows(int newNumRows) {
        ExpandableDenseDoubleMatrix newMatrix = new ExpandableDenseDoubleMatrix(this.name, newNumRows, this.getColCount());
        int minRows = Math.min(this.getRowCount(), newNumRows);
        newMatrix.sliceRows(0, minRows).assign(this.sliceRows(0, minRows));
        this.numCols = newMatrix.numCols;
        this.numRows = newMatrix.numRows;
        this.buffer = newMatrix.buffer;
    }

    public void setRowStorageExpansionFactor(double rowStorageExpansionFactor) {
        this.rowStorageExpansionFactor = rowStorageExpansionFactor;
    }

    public double getRowStorageExpansionFactor() {
        return this.rowStorageExpansionFactor;
    }
}

