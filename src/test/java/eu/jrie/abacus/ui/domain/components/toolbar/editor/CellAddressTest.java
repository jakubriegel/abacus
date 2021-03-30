package eu.jrie.abacus.ui.domain.components.toolbar.editor;

import eu.jrie.abacus.ui.UITest;
import org.junit.jupiter.api.Test;

import static javax.swing.SwingConstants.CENTER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CellAddressTest extends UITest {
    @Test
    void shouldCreateCellAddress() {
        // when
        var address = new CellAddress();

        // then
        assertHasConstantSize(address, 25, 24);
        assertEquals(CENTER, address.getHorizontalAlignment());
        assertEquals(CENTER, address.getVerticalAlignment());
        assertHasStandardFont(address);
    }

    @Test
    void shouldSetAddress() {
        // given
        var address = new CellAddress();
        var text = "A1";

        // when
        address.set(text);

        // then
        assertEquals(text, address.getText());
    }
}
