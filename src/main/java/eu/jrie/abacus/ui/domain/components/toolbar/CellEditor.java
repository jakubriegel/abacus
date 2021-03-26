package eu.jrie.abacus.ui.domain.components.toolbar;

import javax.swing.*;

import static eu.jrie.abacus.ui.domain.Colors.PRIMARY_COLOR;
import static eu.jrie.abacus.ui.infra.ComponentHelper.setHorizontallyFlexibleSize;
import static eu.jrie.abacus.ui.infra.FontProvider.standardFont;
import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createMatteBorder;

public class CellEditor extends JTextArea {

    private static final int BORDER_SIZE = 2;
    private static final int PADDING_SIZE = 5;

    public CellEditor() {
        setHorizontallyFlexibleSize(this, 400, 50);
        setFont(standardFont());
        setBorder(createCompoundBorder(
                createMatteBorder(0, BORDER_SIZE, 0, BORDER_SIZE, PRIMARY_COLOR),
                createEmptyBorder(PADDING_SIZE, PADDING_SIZE, PADDING_SIZE, PADDING_SIZE))
        );
        setLineWrap(true);

        setText("Select cell to edit...");
    }
}
