package eu.jrie.abacus.core.domain.cell.style;

import eu.jrie.abacus.core.domain.cell.Position;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static java.awt.Color.red;
import static java.awt.Color.white;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CellStyleManagerTest {

    private static final Position POSITION = new Position(1, 2);

    private final Map<Position, CellStyle> styles = new HashMap<>();

    private final CellStyleManager manager = new CellStyleManager(styles);

    @Test
    void shouldSetStyle() {
        // given
        var style = new CellStyle(true, false, false, red);

        // when
        manager.setStyle(POSITION, style);

        // then
        assertEquals(1, styles.size());
        assertEquals(style, styles.get(POSITION));
    }

    @Test
    void shouldNotSaveDefaultStyle() {
        // given
        var defaultStyle = new CellStyle(false, false, false, white);

        // when
        manager.setStyle(POSITION, defaultStyle);

        // then
        assertEquals(0, styles.size());
    }

    @Test
    void shouldRemoveEntryWhenSettingDefaultStyle() {
        // given
        var style = new CellStyle(true, false, false, red);
        styles.put(POSITION, style);
        var defaultStyle = new CellStyle(false, false, false, white);

        // when
        manager.setStyle(POSITION, defaultStyle);

        // then
        assertEquals(0, styles.size());
    }

    @Test
    void shouldGetStyle() {
        // given
        var style = new CellStyle(true, false, false, red);
        styles.put(POSITION, style);

        // when
        var result = manager.getStyle(POSITION);

        // then
        assertEquals(1, styles.size());
        assertEquals(style, result);
    }

    @Test
    void shouldGetDefaultStyle() {
        // given
        var defaultStyle = new CellStyle(false, false, false, white);

        // when
        var result = manager.getStyle(POSITION);

        // then
        assertEquals(0, styles.size());
        assertEquals(defaultStyle, result);
    }
}