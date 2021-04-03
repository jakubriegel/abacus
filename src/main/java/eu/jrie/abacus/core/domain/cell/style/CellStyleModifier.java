package eu.jrie.abacus.core.domain.cell.style;

import eu.jrie.abacus.core.domain.cell.Position;

public interface CellStyleModifier {
    void setStyle(Position position, CellStyle style);
}
