package eu.jrie.abacus.ui.infra.event;

import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.domain.cell.style.CellStyle;

public record Event (
        EventType type,
        Position position,
        String text,
        CellStyle cellStyle
) {}
