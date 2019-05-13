/*
 * Decompiled with CFR 0.144.
 */
package fastmath;

import java.io.Serializable;

public class Pair<A, B>
implements Serializable {
    private static final long serialVersionUID = 1L;
    public A left;
    public B right;

    public Pair() {
    }

    public Pair(A firstValue, B secondValue) {
        this.left = firstValue;
        this.right = secondValue;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair otherPair = (Pair)obj;
        return this.leftEquals(otherPair) && this.rightEquals(otherPair);
    }

    public boolean leftEquals(Pair<A, B> otherPair) {
        return this.left != null ? this.left.equals(otherPair.left) : otherPair.left == null;
    }

    public final A getLeft() {
        return this.left;
    }

    public final B getRight() {
        return this.right;
    }

    public int hashCode() {
        return this.safeHashCode(this.left) + 31 * this.safeHashCode(this.right);
    }

    protected final int safeHashCode(Object obj) {
        return obj == null ? 1 : obj.hashCode();
    }

    public boolean rightEquals(Pair<A, B> otherPair) {
        return this.right != null ? this.right.equals(otherPair.right) : otherPair.right == null;
    }

    public final void setLeft(A first) {
        this.left = first;
    }

    public final void setRight(B second) {
        this.right = second;
    }

    public String toString() {
        return String.format("[%s,%s]", this.left, this.right);
    }
}

