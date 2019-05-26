/*
 * Decompiled with CFR 0.144.
 */
package fastmath;

import fastmath.exceptions.FastMathException;

public class EigenDecomposition {
    private static Vector tmpWork = new Vector(1);
    private Vector wi;
    private Vector wr;
    private DoubleMatrix vl;
    private DoubleMatrix vr;
    private Vector tmpVec;
    private int n;

    public Vector getRealEigenvalues() {
        return this.wr;
    }

    public Vector getImaginaryEigenvalues() {
        return this.wi;
    }

    public DoubleMatrix getLeftEigenvectors() {
        return this.vl;
    }

    public DoubleMatrix getRightEigenvectors() {
        return this.vr;
    }

    protected EigenDecomposition(DoubleMatrix A, boolean computeLeft, boolean computeRight) throws FastMathException {
        assert (A.isSquare()) : "A is not square";
        this.n = A.getRowCount();
        this.tmpVec = new Vector(this.n);
        if (A.isSymmetric()) {
            this.wr = new Vector(this.n);
            this.vr = A;
            int info = BLAS1.dsyev('V', 'U', A, this.wr, tmpWork, -1);
            if (info != 0) {
                throw new FastMathException("Failed to query temp workspace size, info = " + info);
            }
            Vector work = new Vector((int)tmpWork.get(0));
            info = BLAS1.dsyev('V', 'U', A, this.wr, work, work.size());
            if (info < 0) {
                throw new FastMathException("parameter " + -info + " to dsyev had an illegal value");
            }
            if (info > 0) {
                throw new FastMathException("the algorithm failed to converge; " + info + " off-diagonal elements of an intermediate tridiagonal form did not converge to zero");
            }
            this.quicksort(0, this.wr.size() - 1);
        } else {
            int info;
            this.wr = new Vector(this.n);
            this.wi = new Vector(this.n);
            if (computeLeft) {
                this.vl = new DoubleColMatrix(this.n, this.n);
            }
            if (computeRight) {
                this.vr = new DoubleColMatrix(this.n, this.n);
            }
            if ((info = BLAS1.dgeev(A, this.wr, this.wi, this.vl, this.vr, tmpWork, -1)) != 0) {
                throw new FastMathException("Failed to query temp workspace size");
            }
            Vector work = new Vector((int)tmpWork.get(0));
            info = BLAS1.dgeev(A, this.wr, this.wi, this.vl, this.vr, work, work.size());
            if (info < 0) {
                throw new FastMathException("parameter " + -info + " to dgeev had an illegal value");
            }
            if (info > 0) {
                throw new FastMathException("The QR algorithm failed to compute all the eigenvalues");
            }
            for (int i = 0; i < this.wi.size(); ++i) {
                if (this.wi.get(i) == 0.0) continue;
                throw new UnsupportedOperationException("Results are imaginary, implement me");
            }
            this.quicksort(0, this.wr.size() - 1);
        }
    }

    private void quicksort(int left, int right) throws FastMathException {
        if (right <= left) {
            return;
        }
        int i = this.partition(left, right);
        this.quicksort(left, i - 1);
        this.quicksort(i + 1, right);
    }

    private int partition(int left, int right) {
        int i = left - 1;
        int j = right;
        do {
            if (this.wr.get(++i) > this.wr.get(right)) {
                continue;
            }
            while (this.wr.get(right) > this.wr.get(--j) && j != left) {
            }
            if (i >= j) break;
            this.exch(i, j);
        } while (true);
        if (i != right) {
            this.exch(i, right);
        }
        return i;
    }

    private void exch(int i, int j) {
        double tmp = this.wr.get(i);
        this.wr.set(i, this.wr.get(j));
        this.wr.set(j, tmp);
        if (this.wi != null) {
            tmp = this.wi.get(i);
            this.wi.set(i, this.wi.get(j));
            this.wi.set(j, tmp);
        }
        if (this.vr != null) {
            this.tmpVec.assign(this.vr.col(i));
            this.vr.col(i).assign(this.vr.col(j));
            this.vr.col(j).assign(this.tmpVec);
        }
        if (this.vl != null) {
            this.tmpVec.assign(this.vl.col(i));
            this.vl.col(i).assign(this.vl.col(j));
            this.vl.col(j).assign(this.tmpVec);
        }
    }

    public double cond() {
        return this.getRealEigenvalues().fmax() / this.getRealEigenvalues().fmin();
    }
}

