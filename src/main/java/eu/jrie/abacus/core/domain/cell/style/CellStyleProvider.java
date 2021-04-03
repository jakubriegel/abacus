package eu.jrie.abacus.core.domain.cell.style;

import eu.jrie.abacus.core.domain.cell.Position;

public interface CellStyleProvider {
    CellStyle getStyle(Position position);
}
