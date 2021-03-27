package eu.jrie.abacus.ui.infra.helper;

import javax.swing.*;
import java.awt.*;

public abstract class ComponentHelper {

    private ComponentHelper() {}

    public static void setConstantSize(JComponent component, int width, int height) {
        setConstantSize(component, new Dimension(width, height));
    }

    private static void setConstantSize(JComponent component, Dimension dimension) {
        component.setMinimumSize(dimension);
        component.setPreferredSize(dimension);
        component.setMaximumSize(dimension);
    }

    public static void setHorizontallyFlexibleSize(JComponent component, int width, int height) {
        component.setMinimumSize(new Dimension(0, height));
        component.setPreferredSize(new Dimension(width, height));
        component.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
    }

}
