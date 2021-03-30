package eu.jrie.abacus.ui.domain.components.toolbar;

import eu.jrie.abacus.ui.UITest;
import org.junit.jupiter.api.Test;

import static eu.jrie.abacus.ui.domain.Colors.DARK_COLOR;
import static javax.swing.SwingConstants.CENTER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LogoLabelTest extends UITest {
    @Test
    void shouldCreateLogoLabel() {
        // when
        var label = new LogoLabel();

        // then
        assertHasConstantSize(label, 150, 50);
        assertEquals(CENTER, label.getHorizontalAlignment());
        assertEquals(DARK_COLOR, label.getForeground());
        assertHasStandardFont(label, 30f);

        // and
        assertEquals("Abacus", label.getText());
    }
}
