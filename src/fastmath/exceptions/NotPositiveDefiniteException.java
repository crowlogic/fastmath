/*
 * Decompiled with CFR 0.144.
 */
package fastmath.exceptions;

import fastmath.AbstractMatrix;
import fastmath.exceptions.FastMathException;

public class NotPositiveDefiniteException
extends FastMathException {
    private static final long serialVersionUID = 1L;
    private final AbstractMatrix matrix;

    public NotPositiveDefiniteException(AbstractMatrix m) {
        super("Matrix is not positive definite");
        this.matrix = m;
    }

    public AbstractMatrix getMatrix() {
        return this.matrix;
    }
}

