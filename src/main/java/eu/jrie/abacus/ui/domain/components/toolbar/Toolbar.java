package eu.jrie.abacus.ui.domain.components.toolbar;

import eu.jrie.abacus.ui.domain.components.toolbar.editor.CellEditor;

import javax.swing.*;

import static eu.jrie.abacus.ui.domain.Colors.PRIMARY_COLOR;
import static eu.jrie.abacus.ui.infra.helper.ComponentHelper.setHorizontallyFlexibleSize;
import static java.awt.Color.white;
import static javax.swing.BorderFactory.createMatteBorder;
import static javax.swing.BoxLayout.X_AXIS;

public class Toolbar extends JPanel {

    private static final int BORDER_SIZE = 2;

    public Toolbar(LogoLabel logoLabel, CellEditor cellEditor, TextTools textTools) {
        setLayout(new BoxLayout(this, X_AXIS));
        setHorizontallyFlexibleSize(this, 800, 50);
        setBackground(white);
        setBorder(createMatteBorder(BORDER_SIZE, 0, BORDER_SIZE, 0, PRIMARY_COLOR));

        add(logoLabel);
        add(cellEditor);
        add(textTools);
    }
}
