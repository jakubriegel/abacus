package eu.jrie.abacus.ui.domain.components.toolbar.editor;

import javax.swing.*;
import java.util.Optional;

import static eu.jrie.abacus.ui.infra.helper.ComponentHelper.setConstantSize;
import static java.awt.Image.SCALE_SMOOTH;

public class Symbol extends JLabel {

    public Symbol() {
        setConstantSize(this, 25, 24);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    void set(ImageIcon icon) {
        Optional.ofNullable(icon)
                .map(ImageIcon::getImage)
                .map(img -> img.getScaledInstance(20, 20, SCALE_SMOOTH))
                .map(ImageIcon::new)
                .ifPresentOrElse(this::setIcon, () -> setIcon(null));
    }
}
