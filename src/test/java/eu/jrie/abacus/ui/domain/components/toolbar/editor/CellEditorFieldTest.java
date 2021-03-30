package eu.jrie.abacus.ui.domain.components.toolbar.editor;

import eu.jrie.abacus.ui.UITest;
import org.junit.jupiter.api.Test;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

import static eu.jrie.abacus.ui.domain.Colors.PRIMARY_COLOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CellEditorFieldTest extends UITest {
    @Test
    void shouldCreateCellEditorField() {
        // when
        var cellEditorField = new CellEditorField();

        // then
        assertHasHorizontallyFlexibleSize(cellEditorField, 375, 50);
        assertHasStandardFont(cellEditorField);
        assertTrue(cellEditorField.getLineWrap());
        assertTrue(cellEditorField.getBorder() instanceof CompoundBorder);
        var border = (CompoundBorder) cellEditorField.getBorder();
        assertTrue(border.getOutsideBorder() instanceof MatteBorder);
        var outsideBorder = (MatteBorder) border.getOutsideBorder();
        assertEquals(new Insets(0, 2, 0, 2), outsideBorder.getBorderInsets());
        assertEquals(PRIMARY_COLOR, outsideBorder.getMatteColor());
        assertTrue(border.getInsideBorder() instanceof EmptyBorder);
        var insideBorder = (EmptyBorder) border.getInsideBorder();
        assertEquals(new Insets(5, 5, 5, 5), insideBorder.getBorderInsets());
    }

    @Test
    void shouldSetText() {
        // given
        var cellEditorField = new CellEditorField();
        var text = "text";

        // when
        cellEditorField.set(text);

        // then
        assertEquals(text, cellEditorField.getText());
    }
}
