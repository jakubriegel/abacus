package eu.jrie.abacus.ui.domain.components.toolbar;

import javax.swing.*;

import static eu.jrie.abacus.ui.domain.Colors.DARK_COLOR;
import static eu.jrie.abacus.ui.infra.FontProvider.standardFont;
import static eu.jrie.abacus.ui.infra.helper.ComponentHelper.setConstantSize;

public class LogoLabel extends JLabel {
    public LogoLabel() {
        setConstantSize(this, 150, 50);
        setHorizontalAlignment(CENTER);
        setForeground(DARK_COLOR);
        setFont(standardFont().deriveFont(30f));

        setText("Abacus");
    }
}
