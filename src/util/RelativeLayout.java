/*
 * Decompiled with CFR 0.144.
 */
package util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.io.Serializable;
import java.util.HashMap;

public class RelativeLayout
implements LayoutManager2,
Serializable {
    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    public static final float LEADING = 0.0f;
    public static final float CENTER = 0.5f;
    public static final float TRAILING = 1.0f;
    public static final float COMPONENT = -1.0f;
    public static final int DO_NOTHING = 0;
    public static final int FIRST = 1;
    public static final int LAST = 2;
    public static final int LARGEST = 3;
    public static final int EQUAL = 4;
    private static final int MINIMUM = 0;
    private static final int PREFERRED = 1;
    private HashMap<Component, Float> constraints = new HashMap();
    private int axis;
    private float alignment = 0.5f;
    private int gap;
    private int borderGap;
    private boolean fill = false;
    private int fillGap;
    private int roundingPolicy = 3;

    public RelativeLayout() {
        this(0, 0);
    }

    public RelativeLayout(int axis) {
        this(axis, 0);
    }

    public RelativeLayout(int axis, int gap) {
        this.setAxis(axis);
        this.setGap(gap);
        this.setBorderGap(gap);
    }

    public int getAxis() {
        return this.axis;
    }

    public void setAxis(int axis) {
        if (axis != 0 && axis != 1) {
            throw new IllegalArgumentException("invalid axis specified");
        }
        this.axis = axis;
    }

    public int getGap() {
        return this.gap;
    }

    public void setGap(int gap) {
        this.gap = gap < 0 ? 0 : gap;
    }

    public int getBorderGap() {
        return this.borderGap;
    }

    public void setBorderGap(int borderGap) {
        this.borderGap = borderGap < 0 ? 0 : borderGap;
    }

    public float getAlignment() {
        return this.alignment;
    }

    public void setAlignment(float alignment) {
        this.alignment = alignment > 1.0f ? 1.0f : (alignment < 0.0f ? -1.0f : alignment);
    }

    public boolean isFill() {
        return this.fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }

    public int getFillGap() {
        return this.fillGap;
    }

    public void setFillGap(int fillGap) {
        this.fillGap = fillGap;
    }

    public int getRoundingPolicy() {
        return this.roundingPolicy;
    }

    public void setRoundingPolicy(int roundingPolicy) {
        this.roundingPolicy = roundingPolicy;
    }

    public Float getConstraints(Component component) {
        return this.constraints.get(component);
    }

    @Override
    public void addLayoutComponent(String name, Component component) {
    }

    @Override
    public void addLayoutComponent(Component component, Object constraint) {
        if (constraint != null && !(constraint instanceof Float)) {
            throw new IllegalArgumentException("Constraint parameter must be of type Float");
        }
        this.constraints.put(component, (Float)constraint);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Object object = parent.getTreeLock();
        synchronized (object) {
            return this.getLayoutSize(parent, 1);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        Object object = parent.getTreeLock();
        synchronized (object) {
            return this.getLayoutSize(parent, 0);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void layoutContainer(Container parent) {
        Object object = parent.getTreeLock();
        synchronized (object) {
            if (this.axis == 0) {
                this.layoutContainerHorizontally(parent);
            } else {
                this.layoutContainerVertically(parent);
            }
        }
    }

    private void layoutContainerHorizontally(Container parent) {
        int components = parent.getComponentCount();
        int visibleComponents = this.getVisibleComponents(parent);
        if (components == 0) {
            return;
        }
        float relativeTotal = 0.0f;
        Insets insets = parent.getInsets();
        int spaceAvailable = parent.getSize().width - insets.left - insets.right - (visibleComponents - 1) * this.gap - 2 * this.borderGap;
        for (int i = 0; i < components; ++i) {
            Component component = parent.getComponent(i);
            if (!component.isVisible()) continue;
            Float constraint = this.constraints.get(component);
            if (constraint == null) {
                Dimension d = component.getPreferredSize();
                spaceAvailable -= d.width;
                continue;
            }
            relativeTotal = (float)((double)relativeTotal + constraint.doubleValue());
        }
        int[] relativeSpace = this.allocateRelativeSpace(parent, spaceAvailable, relativeTotal);
        int x = insets.left + this.borderGap;
        int y = insets.top;
        int insetGap = insets.top + insets.bottom;
        int parentHeight = parent.getSize().height - insetGap;
        for (int i = 0; i < components; ++i) {
            Float constraint;
            Component component = parent.getComponent(i);
            if (!component.isVisible()) continue;
            if (i > 0) {
                x += this.gap;
            }
            Dimension d = component.getPreferredSize();
            if (this.fill) {
                d.height = parentHeight - this.fillGap;
            }
            if ((constraint = this.constraints.get(component)) == null) {
                component.setSize(d);
                int locationY = this.getLocationY(component, parentHeight) + y;
                component.setLocation(x, locationY);
                x += d.width;
                continue;
            }
            int width = relativeSpace[i];
            component.setSize(width, d.height);
            int locationY = this.getLocationY(component, parentHeight) + y;
            component.setLocation(x, locationY);
            x += width;
        }
    }

    private int getLocationY(Component component, int height) {
        float alignmentY = this.alignment;
        if (alignmentY == -1.0f) {
            alignmentY = component.getAlignmentY();
        }
        float y = (float)(height - component.getSize().height) * alignmentY;
        return (int)y;
    }

    private void layoutContainerVertically(Container parent) {
        int components = parent.getComponentCount();
        int visibleComponents = this.getVisibleComponents(parent);
        if (components == 0) {
            return;
        }
        float relativeTotal = 0.0f;
        Insets insets = parent.getInsets();
        int spaceAvailable = parent.getSize().height - insets.top - insets.bottom - (visibleComponents - 1) * this.gap - 2 * this.borderGap;
        for (int i = 0; i < components; ++i) {
            Component component = parent.getComponent(i);
            if (!component.isVisible()) continue;
            Float constraint = this.constraints.get(component);
            if (constraint == null) {
                Dimension d = component.getPreferredSize();
                spaceAvailable -= d.height;
                continue;
            }
            relativeTotal = (float)((double)relativeTotal + constraint.doubleValue());
        }
        int[] relativeSpace = this.allocateRelativeSpace(parent, spaceAvailable, relativeTotal);
        int x = insets.left;
        int y = insets.top + this.borderGap;
        int insetGap = insets.left + insets.right;
        int parentWidth = parent.getSize().width - insetGap;
        for (int i = 0; i < components; ++i) {
            Float constraint;
            Component component = parent.getComponent(i);
            if (!component.isVisible()) continue;
            if (i > 0) {
                y += this.gap;
            }
            Dimension d = component.getPreferredSize();
            if (this.fill) {
                d.width = parentWidth - this.fillGap;
            }
            if ((constraint = this.constraints.get(component)) == null) {
                component.setSize(d);
                int locationX = this.getLocationX(component, parentWidth) + x;
                component.setLocation(locationX, y);
                y += d.height;
                continue;
            }
            int height = relativeSpace[i];
            component.setSize(d.width, height);
            int locationX = this.getLocationX(component, parentWidth) + x;
            component.setLocation(locationX, y);
            y += height;
        }
    }

    private int getLocationX(Component component, int width) {
        float alignmentX = this.alignment;
        if (alignmentX == -1.0f) {
            alignmentX = component.getAlignmentX();
        }
        float x = (float)(width - component.getSize().width) * alignmentX;
        return (int)x;
    }

    private int[] allocateRelativeSpace(Container parent, int spaceAvailable, float relativeTotal) {
        int spaceUsed = 0;
        int components = parent.getComponentCount();
        int[] relativeSpace = new int[components];
        for (int i = 0; i < components; ++i) {
            int space;
            Component component;
            Float constraint;
            relativeSpace[i] = 0;
            if (!(relativeTotal > 0.0f) || spaceAvailable <= 0 || (constraint = this.constraints.get(component = parent.getComponent(i))) == null) continue;
            relativeSpace[i] = space = Math.round((float)spaceAvailable * constraint.floatValue() / relativeTotal);
            spaceUsed += space;
        }
        int spaceRemaining = spaceAvailable - spaceUsed;
        if (relativeTotal > 0.0f && spaceRemaining != 0) {
            this.adjustForRounding(relativeSpace, spaceRemaining);
        }
        return relativeSpace;
    }

    protected void adjustForRounding(int[] relativeSpace, int spaceRemaining) {
        switch (this.roundingPolicy) {
            case 0: {
                break;
            }
            case 1: {
                this.adjustFirst(relativeSpace, spaceRemaining);
                break;
            }
            case 2: {
                this.adjustLast(relativeSpace, spaceRemaining);
                break;
            }
            case 3: {
                this.adjustLargest(relativeSpace, spaceRemaining);
                break;
            }
            case 4: {
                this.adjustEqual(relativeSpace, spaceRemaining);
                break;
            }
            default: {
                this.adjustLargest(relativeSpace, spaceRemaining);
            }
        }
    }

    private void adjustFirst(int[] relativeSpace, int spaceRemaining) {
        for (int i = 0; i < relativeSpace.length; ++i) {
            if (relativeSpace[i] <= 0) continue;
            int[] arrn = relativeSpace;
            int n = i;
            arrn[n] = arrn[n] + spaceRemaining;
            break;
        }
    }

    private void adjustLast(int[] relativeSpace, int spaceRemaining) {
        for (int i = relativeSpace.length - 1; i > 0; --i) {
            if (relativeSpace[i] <= 0) continue;
            int[] arrn = relativeSpace;
            int n = i;
            arrn[n] = arrn[n] + spaceRemaining;
            break;
        }
    }

    private void adjustLargest(int[] relativeSpace, int spaceRemaining) {
        int largest = 0;
        int largestSpace = 0;
        for (int i = 0; i < relativeSpace.length; ++i) {
            int space = relativeSpace[i];
            if (space <= 0 || largestSpace > space) continue;
            largestSpace = space;
            largest = i;
        }
        int[] arrn = relativeSpace;
        int n = largest;
        arrn[n] = arrn[n] + spaceRemaining;
    }

    private void adjustEqual(int[] relativeSpace, int spaceRemaining) {
        for (int i = 0; i < relativeSpace.length; ++i) {
            if (relativeSpace[i] <= 0) continue;
            if (spaceRemaining > 0) {
                int[] arrn = relativeSpace;
                int n = i;
                arrn[n] = arrn[n] + 1;
                --spaceRemaining;
            } else {
                int[] arrn = relativeSpace;
                int n = i;
                arrn[n] = arrn[n] - 1;
                ++spaceRemaining;
            }
            if (spaceRemaining == 0) break;
        }
    }

    private Dimension getLayoutSize(Container parent, int type) {
        int width = 0;
        int height = 0;
        int components = parent.getComponentCount();
        int visibleComponents = this.getVisibleComponents(parent);
        for (int i = 0; i < components; ++i) {
            Component component = parent.getComponent(i);
            if (!component.isVisible()) continue;
            Dimension d = this.getDimension(component, type);
            if (this.axis == 0) {
                width += d.width;
                height = Math.max(height, d.height);
                continue;
            }
            width = Math.max(width, d.width);
            height += d.height;
        }
        Insets insets = parent.getInsets();
        int totalGap = (visibleComponents - 1) * this.gap + 2 * this.borderGap;
        if (this.axis == 0) {
            width += insets.left + insets.right + totalGap;
            height += insets.top + insets.bottom;
        } else {
            width += insets.left + insets.right;
            height += insets.top + insets.bottom + totalGap;
        }
        Dimension size = new Dimension(width, height);
        return size;
    }

    private int getVisibleComponents(Container container) {
        int visibleComponents = 0;
        for (Component component : container.getComponents()) {
            if (!component.isVisible()) continue;
            ++visibleComponents;
        }
        return visibleComponents;
    }

    private Dimension getDimension(Component component, int type) {
        switch (type) {
            case 1: {
                return component.getPreferredSize();
            }
            case 0: {
                return component.getMinimumSize();
            }
        }
        return new Dimension(0, 0);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public float getLayoutAlignmentX(Container parent) {
        return 0.5f;
    }

    @Override
    public float getLayoutAlignmentY(Container parent) {
        return 0.5f;
    }

    @Override
    public void invalidateLayout(Container target) {
    }

    public String toString() {
        return this.getClass().getName() + "[axis=" + this.axis + ",gap=" + this.gap + "]";
    }
}

