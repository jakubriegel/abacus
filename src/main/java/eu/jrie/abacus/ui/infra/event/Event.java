package eu.jrie.abacus.ui.infra.event;

import eu.jrie.abacus.core.domain.cell.Position;

public record Event (
        EventType type,
        Position position,
        String text
) {}
