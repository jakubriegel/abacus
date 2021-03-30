package eu.jrie.abacus.ui.domain.components.toolbar.editor;

import javax.swing.*;
import java.util.Objects;

import static eu.jrie.abacus.ui.domain.Colors.PRIMARY_COLOR;
import static eu.jrie.abacus.ui.infra.FontProvider.standardFont;
import static eu.jrie.abacus.ui.infra.helper.ComponentHelper.setHorizontallyFlexibleSize;
import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;

public class CellEditorField extends JTextArea {

    private static final int BORDER_SIZE = 2;
    private static final int PADDING_SIZE = 5;

    public CellEditorField() {
        setHorizontallyFlexibleSize(this, 375, 50);
        setFont(standardFont());
        setBorder(createCompoundBorder(
                BorderFactory.createMatteBorder(0, BORDER_SIZE, 0, BORDER_SIZE, PRIMARY_COLOR),
                createEmptyBorder(PADDING_SIZE, PADDING_SIZE, PADDING_SIZE, PADDING_SIZE))
        );
        setLineWrap(true);
    }

    void set(String text) {
        if (!Objects.equals(getText(), text)) {
            setText(text);
        }
    }
}
