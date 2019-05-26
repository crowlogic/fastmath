/*
 * Decompiled with CFR 0.144.
 */
package fastmath;

import java.nio.ByteBuffer;

import fastmath.exceptions.FastMathException;

public class BLAS1 {
    public static int dgetri(boolean colMajor, int n, ByteBuffer A, int offA, int ldA, ByteBuffer ipiv) {
        throw new UnsupportedOperationException("TODO");
    }

    public static int dsyev(char jobz, char uplo, int n, ByteBuffer A, int offA, int ldA, ByteBuffer W, ByteBuffer work, int lwork) {
        throw new UnsupportedOperationException("TODO");
    }

    public static int dsyev(char jobz, char uplo, AbstractMatrix A, Vector W, Vector work, int lwork) {
        assert (work.getOffset(0) == 0) : "work offset must be 0";
        assert (A.isColMajor()) : "A must be col major";
        assert (W.isContiguous()) : "W must be contiguous";
        assert (W.getOffset(0) == 0) : "W offset must be 0";
        assert (W.size() == A.numRows) : "A.length != A.rows";
        return BLAS1.dsyev(jobz, uplo, A.getRowCount(), A.getBuffer(), A.getOffset(0, 0), A.getRowCount(), W.getBuffer(), work.getBuffer(), lwork);
    }

    public static int dpotrf(boolean colMajor, boolean upper, int n, ByteBuffer A, int offA, int ldA) {
        throw new UnsupportedOperationException("TODO");
    }

    public static int dgetrf(boolean colMajor, int M, int N, ByteBuffer A, int offA, int lda, ByteBuffer buffer) {
        throw new UnsupportedOperationException("TODO");
    }

    public static double dlassq(int N, ByteBuffer X, int offX, int incX) {
        throw new UnsupportedOperationException("TODO");
    }

    public static double dasum(int N, ByteBuffer X, int incX) {
        throw new UnsupportedOperationException("TODO");
    }

    public static void dgeadd(int M, int N, double alpha, ByteBuffer A, int lda, double beta, ByteBuffer C, int ldc) {
        throw new UnsupportedOperationException("TODO");
    }

    public static void dcopy(int N, ByteBuffer X, int offX, int incX, ByteBuffer Y, int offY, int incY) {
        throw new UnsupportedOperationException("TODO");
    }

    public static void zcopy(int N, ByteBuffer X, int offX, int incX, ByteBuffer Y, int offY, int incY) {
        throw new UnsupportedOperationException("TODO");
    }

    public static void dgemm(boolean colMajor, boolean TransA, boolean TransB, int M, int N, int K, double alpha, ByteBuffer A, int offA, int lda, ByteBuffer B, int offB, int ldb, double beta, ByteBuffer C, int offC, int ldc) {
        throw new UnsupportedOperationException("TODO");
    }

    public static int dgeev(char jobvl, char jobvr, int n, ByteBuffer A, int offA, int ldA, ByteBuffer WR, ByteBuffer WI, ByteBuffer VL, int offVL, int ldVL, ByteBuffer VR, int offVR, int ldVR, ByteBuffer work, int lwork) {
        throw new UnsupportedOperationException("TODO");
    }

    public static int dgeev(AbstractMatrix A, Vector wr, Vector wi, AbstractMatrix vl, AbstractMatrix vr, Vector work, int workSize) throws FastMathException {
        if (wr.getOffset(0) != 0 || wi.getOffset(0) != 0 || wr.getIncrement() != 1 || wi.getIncrement() != 1) {
            throw new FastMathException("wr and wi cannot be subvectors");
        }
        return BLAS1.dgeev(vl != null ? (char)'V' : 'N', vr != null ? (char)'V' : 'N', A.getRowCount(), A.getBuffer(), A.getOffset(0, 0), A.getRowCount(), wr.getBuffer(), wi.getBuffer(), vl == null ? null : vl.getBuffer(), vl == null ? 0 : vl.getOffset(0, 0), A.getRowCount(), vr == null ? null : vr.getBuffer(), vr == null ? 0 : vr.getOffset(0, 0), A.getRowCount(), work.getBuffer(), workSize);
    }

    public static int zgeev(char jobvl, char jobvr, int n, ByteBuffer A, int offA, int ldA, ByteBuffer W, ByteBuffer VL, int offVL, int ldVL, ByteBuffer VR, int offVR, int ldVR, ByteBuffer work, int lwork, ByteBuffer rwork) {
        throw new UnsupportedOperationException("TODO");
    }

    public static void dcopy(Vector X, Vector Y) {
        assert (X.size() == Y.size()) : String.format("Dimensions of X and Y must be the same: %d != %d", X.size(), Y.size());
        Y.assign(X);
    }

    public static int dgetrf(AbstractMatrix A, IntVector ipiv) {
        return BLAS1.dgetrf(A.isColMajor(), A.getRowCount(), A.getColCount(), A.getBuffer(), A.getOffset(0, 0), A.getRowCount(), ipiv.getBuffer());
    }

    public static int dpotrf(boolean upper, DoubleMatrix A) {
        return BLAS1.dpotrf(A.isColMajor(), upper, A.getRowCount(), A.getBuffer(), A.getOffset(0, 0), A.getRowCount());
    }

    public static int dgelss(DoubleMatrix A, DoubleMatrix B, Vector S, IntVector rank, Vector work, int workSize) {
        assert (B.getRowCount() >= Math.max(A.getRowCount(), A.getColCount())) : "B.rows < max(A.rows,A.cols)";
        assert (S.size() == Math.min(A.getRowCount(), A.getColCount())) : "S.length < min(A.rows,A.cols)";
        assert (A.getRowIncrement() == 1) : "A must be col major, 1 != rowIncrement = " + A.getRowIncrement();
        throw new UnsupportedOperationException("TODO");
    }
}

