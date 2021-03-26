package eu.jrie.abacus.ui.domain.components;

import eu.jrie.abacus.ui.UITest;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static java.awt.Color.white;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IconButtonTest extends UITest {
    @Test
    void shouldCreateIconButton() {
        // given
        var text = "text";
        var icon = new ImageIcon();
        var emptyInsets = new Insets(0, 0, 0, 0);

        // when
        var button = new IconButton(text, icon);

        // then
        assertEquals(white, button.getBackground());
        assertHasStandardFont(button);
        assertTrue(button.getBorder() instanceof CompoundBorder);
        var border = (CompoundBorder) button.getBorder();
        assertTrue(border.getOutsideBorder() instanceof EmptyBorder);
        var outsideBorder = (EmptyBorder) border.getOutsideBorder();
        assertEquals(emptyInsets, outsideBorder.getBorderInsets());
        assertTrue(border.getInsideBorder() instanceof EmptyBorder);
        var insideBorder = (EmptyBorder) border.getInsideBorder();
        assertEquals(emptyInsets, insideBorder.getBorderInsets());

        // and
        assertEquals(text, button.getToolTipText());
        assertEquals(icon, button.getIcon());
    }
}
