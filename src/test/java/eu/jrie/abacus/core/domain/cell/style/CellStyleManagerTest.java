package eu.jrie.abacus.core.domain.cell.style;

import eu.jrie.abacus.core.domain.cell.Position;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static eu.jrie.abacus.core.domain.cell.style.CellTextAlignment.LEFT;
import static eu.jrie.abacus.core.domain.cell.style.CellTextPosition.MIDDLE;
import static eu.jrie.abacus.core.domain.cell.style.CellTextPosition.TOP;
import static java.awt.Color.black;
import static java.awt.Color.green;
import static java.awt.Color.red;
import static java.awt.Color.white;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CellStyleManagerTest {

    private static final Position POSITION = new Position(1, 2);
    private static final CellStyle STYLE = new CellStyle(10f, true, false, false, green, red, LEFT, TOP);
    private static final CellStyle DEFAULT_STYLE = new CellStyle(12f, false, false, false, black, white, LEFT, MIDDLE);

    private final Map<Position, CellStyle> styles = new HashMap<>();

    private final CellStyleManager manager = new CellStyleManager(styles);

    @Test
    void shouldSetStyle() {
        // when
        manager.setStyle(POSITION, STYLE);

        // then
        assertEquals(1, styles.size());
        assertEquals(STYLE, styles.get(POSITION));
    }

    @Test
    void shouldNotSaveDefaultStyle() {
        // when
        manager.setStyle(POSITION, DEFAULT_STYLE);

        // then
        assertEquals(0, styles.size());
    }

    @Test
    void shouldRemoveEntryWhenSettingDefaultStyle() {
        // given
        styles.put(POSITION, STYLE);

        // when
        manager.setStyle(POSITION, DEFAULT_STYLE);

        // then
        assertEquals(0, styles.size());
    }

    @Test
    void shouldGetStyle() {
        // given
        styles.put(POSITION, STYLE);

        // when
        var result = manager.getStyle(POSITION);

        // then
        assertEquals(1, styles.size());
        assertEquals(STYLE, result);
    }

    @Test
    void shouldGetDefaultStyle() {
        // when
        var result = manager.getStyle(POSITION);

        // then
        assertEquals(0, styles.size());
        assertEquals(DEFAULT_STYLE, result);
    }
}