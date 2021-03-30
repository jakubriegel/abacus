package eu.jrie.abacus.ui.domain.components.toolbar.editor;

import eu.jrie.abacus.ui.UITest;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static javax.swing.SwingConstants.CENTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SymbolTest extends UITest {
    @Test
    void shouldCreateSymbol() {
        // when
        var symbol = new Symbol();

        // then
        assertHasConstantSize(symbol, 25, 24);
        assertEquals(CENTER, symbol.getHorizontalAlignment());
        assertEquals(CENTER, symbol.getVerticalAlignment());
    }

    @Test
    void shouldFormatAndSetIcon() {
        // given
        var symbol = new Symbol();
        var icon = new ImageIcon(getClass().getResource("/graphics/icons/abacus.png" ));

        // when
        symbol.set(icon);

        // then
        var result =symbol.getIcon();
        assertEquals(20, result.getIconHeight());
        assertEquals(20, result.getIconWidth());
    }

    @Test
    void shouldSetNullIcon() {
        // given
        var symbol = new Symbol();

        // when
        symbol.set(null);

        // then
        assertNull(symbol.getIcon());
    }
}
