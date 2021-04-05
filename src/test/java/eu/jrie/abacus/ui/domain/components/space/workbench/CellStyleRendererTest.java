package eu.jrie.abacus.ui.domain.components.space.workbench;

import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.domain.cell.style.CellStyle;
import eu.jrie.abacus.core.domain.cell.style.CellStyleProvider;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import static eu.jrie.abacus.core.domain.cell.style.CellTextPosition.TOP;
import static java.awt.Color.red;
import static java.awt.Font.BOLD;
import static java.awt.Font.ITALIC;
import static java.awt.Font.PLAIN;
import static java.awt.font.TextAttribute.UNDERLINE;
import static java.awt.font.TextAttribute.UNDERLINE_ON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CellStyleRendererTest {
    private final CellStyleProvider styleProvider = mock(CellStyleProvider.class);

    private final CellStyleRenderer renderer = new CellStyleRenderer(styleProvider);

    @Test
    void shouldApplyStyle() {
        // given
        var position = new Position(1, 2);
        var column = position.x() + 1;
        var row = position.y();

        // and
        var style = new CellStyle(true, true, true, red, TOP);
        when(styleProvider.getStyle(position)).thenReturn(style);

        // when
        var result = renderer.getTableCellRendererComponent(new JTable(), null, false, false, row, column);

        // then
        verify(styleProvider).getStyle(position);

        // and
        assertEquals(BOLD + ITALIC, result.getFont().getStyle());
        assertEquals(UNDERLINE_ON, result.getFont().getAttributes().get(UNDERLINE));
        assertEquals(style.backgroundColor(), result.getBackground());
        assertEquals(DefaultTableCellRenderer.TOP, ((DefaultTableCellRenderer) result).getVerticalAlignment());
    }

    @Test
    void shouldNotApplyBoldAndItalic() {
        // given
        var position = new Position(1, 2);
        var column = position.x() + 1;
        var row = position.y();

        // and
        var style = new CellStyle(false, false, true, red, TOP);
        when(styleProvider.getStyle(position)).thenReturn(style);

        // when
        var result = renderer.getTableCellRendererComponent(new JTable(), null, false, false, row, column);

        // then
        verify(styleProvider).getStyle(position);

        // and
        assertEquals(PLAIN, result.getFont().getStyle());
    }

    @Test
    void shouldNotApplyUnderline() {
        // given
        var position = new Position(1, 2);
        var column = position.x() + 1;
        var row = position.y();

        // and
        var style = new CellStyle(false, false, false, red, TOP);
        when(styleProvider.getStyle(position)).thenReturn(style);

        // when
        var result = renderer.getTableCellRendererComponent(new JTable(), null, false, false, row, column);

        // then
        verify(styleProvider).getStyle(position);

        // and
        assertNull(result.getFont().getAttributes().get(UNDERLINE));
    }
}
