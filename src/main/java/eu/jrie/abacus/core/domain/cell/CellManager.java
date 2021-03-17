package eu.jrie.abacus.core.domain.cell;

import eu.jrie.abacus.core.domain.expression.TextValue;

import java.util.HashMap;
import java.util.Map;

public class CellManager {
    private final Map<Position, Cell> cells = new HashMap<>();

    public CellManager() {
        cells.put(new Position(3, 5), new Cell(new Position(3, 5), "=add(a, b)", new TextValue("test")));
    }

    public Cell getCell(Position position) {
        return cells.computeIfAbsent(position, Cell::new);
    }
}
