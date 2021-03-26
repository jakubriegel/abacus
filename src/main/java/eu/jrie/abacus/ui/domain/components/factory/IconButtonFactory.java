package eu.jrie.abacus.ui.domain.components.factory;

import javax.swing.*;

public interface IconButtonFactory {
    JComponent buildButton(String text, String iconPath, int iconSize);
}
