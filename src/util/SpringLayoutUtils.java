/*
 * Decompiled with CFR 0.144.
 */
package util;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.io.PrintStream;
import javax.swing.Spring;
import javax.swing.SpringLayout;

public class SpringLayoutUtils {
    public static void makeGrid(Container parent, int rows, int cols, int initialX, int initialY, int xPad, int yPad) {
        int i;
        SpringLayout layout;
        SpringLayout.Constraints cons;
        try {
            layout = (SpringLayout)parent.getLayout();
        }
        catch (ClassCastException exc) {
            System.err.println("The first argument to makeGrid must use SpringLayout.");
            return;
        }
        Spring xPadSpring = Spring.constant(xPad);
        Spring yPadSpring = Spring.constant(yPad);
        Spring initialXSpring = Spring.constant(initialX);
        Spring initialYSpring = Spring.constant(initialY);
        int max = rows * cols;
        Spring maxWidthSpring = layout.getConstraints(parent.getComponent(0)).getWidth();
        Spring maxHeightSpring = layout.getConstraints(parent.getComponent(0)).getHeight();
        for (i = 1; i < max; ++i) {
            cons = layout.getConstraints(parent.getComponent(i));
            maxWidthSpring = Spring.max(maxWidthSpring, cons.getWidth());
            maxHeightSpring = Spring.max(maxHeightSpring, cons.getHeight());
        }
        for (i = 0; i < max; ++i) {
            cons = layout.getConstraints(parent.getComponent(i));
            cons.setWidth(maxWidthSpring);
            cons.setHeight(maxHeightSpring);
        }
        SpringLayout.Constraints lastCons = null;
        SpringLayout.Constraints lastRowCons = null;
        for (int i2 = 0; i2 < max; ++i2) {
            SpringLayout.Constraints cons2 = layout.getConstraints(parent.getComponent(i2));
            if (i2 % cols == 0) {
                lastRowCons = lastCons;
                cons2.setX(initialXSpring);
            } else {
                cons2.setX(Spring.sum(lastCons.getConstraint("East"), xPadSpring));
            }
            if (i2 / cols == 0) {
                cons2.setY(initialYSpring);
            } else {
                cons2.setY(Spring.sum(lastRowCons.getConstraint("South"), yPadSpring));
            }
            lastCons = cons2;
        }
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint("South", Spring.sum(Spring.constant(yPad), lastCons.getConstraint("South")));
        pCons.setConstraint("East", Spring.sum(Spring.constant(xPad), lastCons.getConstraint("East")));
    }
}

