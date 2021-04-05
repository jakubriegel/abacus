package eu.jrie.abacus.core.domain.cell.style;

import eu.jrie.abacus.core.domain.cell.Position;

import java.util.HashMap;
import java.util.Map;

import static eu.jrie.abacus.core.domain.cell.style.CellTextAlignment.LEFT;
import static eu.jrie.abacus.core.domain.cell.style.CellTextPosition.MIDDLE;
import static eu.jrie.abacus.ui.infra.FontProvider.DEFAULT_FONT_SIZE;
import static java.awt.Color.black;
import static java.awt.Color.white;

public class CellStyleManager implements CellStyleProvider {

    private static final CellStyle DEFAULT_STYLE = new CellStyle(
            DEFAULT_FONT_SIZE,
            false,
            false,
            false,
            black,
            white,
            LEFT,
            MIDDLE
    );

    private final Map<Position, CellStyle> styles;

    public CellStyleManager() {
        this(new HashMap<>());
    }

    CellStyleManager(Map<Position, CellStyle> styles) {
        this.styles = styles;
    }

    public void setDefaultStyle(Position position) {
        setStyle(position, DEFAULT_STYLE);
    }

    public void setStyle(Position position, CellStyle style) {
        if (style.equals(DEFAULT_STYLE)) {
            styles.remove(position);
        } else {
            styles.put(position, style);
        }
    }

    @Override
    public CellStyle getStyle(Position position) {
        return styles.getOrDefault(position, DEFAULT_STYLE);
    }

}
