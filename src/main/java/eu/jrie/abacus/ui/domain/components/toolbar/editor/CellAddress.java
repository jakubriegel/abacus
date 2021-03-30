package eu.jrie.abacus.ui.domain.components.toolbar.editor;

import javax.swing.*;

import static eu.jrie.abacus.ui.infra.FontProvider.standardFont;
import static eu.jrie.abacus.ui.infra.helper.ComponentHelper.setConstantSize;

public class CellAddress extends JLabel {

    public CellAddress() {
        setConstantSize(this, 25, 24);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        setFont(standardFont());
    }

    void set(String address) {
        setText(address);
    }
}
