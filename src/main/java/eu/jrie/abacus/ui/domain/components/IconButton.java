package eu.jrie.abacus.ui.domain.components;

import javax.swing.*;

import static eu.jrie.abacus.ui.infra.FontProvider.standardFont;
import static java.awt.Color.white;
import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;

public class IconButton extends JButton {
    public IconButton(String text, Icon icon) {
        setBackground(white);
        setFont(standardFont());
        setBorder(createCompoundBorder(createEmptyBorder(), createEmptyBorder()));

        setToolTipText(text);
        setIcon(icon);
    }
}
