package eu.jrie.abacus.ui.domain.components.factory;

import javax.swing.*;

public interface IconButtonFactory {

    default JComponent buildButton(String text, String iconPath, int iconSize) {
        return buildButton(text, iconPath, iconSize, () -> {});
    }

    JComponent buildButton(String text, String iconPath, int iconSize, Runnable onClick);
}
