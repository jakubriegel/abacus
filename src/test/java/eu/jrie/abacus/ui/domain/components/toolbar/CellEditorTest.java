package eu.jrie.abacus.ui.domain.components.toolbar;

import eu.jrie.abacus.ui.UITest;
import org.junit.jupiter.api.Test;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

import static eu.jrie.abacus.ui.domain.Colors.PRIMARY_COLOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CellEditorTest extends UITest {
    @Test
    void shouldCreateCellEditor() {
        // when
        var cellEditor = new CellEditor();

        // then
        assertHasHorizontallyFlexibleSize(cellEditor, 400, 50);
        assertHasStandardFont(cellEditor);
        assertTrue(cellEditor.getLineWrap());
        assertTrue(cellEditor.getBorder() instanceof CompoundBorder);
        var border = (CompoundBorder) cellEditor.getBorder();
        assertTrue(border.getOutsideBorder() instanceof MatteBorder);
        var outsideBorder = (MatteBorder) border.getOutsideBorder();
        assertEquals(new Insets(0, 2, 0, 2), outsideBorder.getBorderInsets());
        assertEquals(PRIMARY_COLOR, outsideBorder.getMatteColor());
        assertTrue(border.getInsideBorder() instanceof EmptyBorder);
        var insideBorder = (EmptyBorder) border.getInsideBorder();
        assertEquals(new Insets(5, 5, 5, 5), insideBorder.getBorderInsets());

        // and
        assertEquals("Select cell to edit...", cellEditor.getText());
    }
}
