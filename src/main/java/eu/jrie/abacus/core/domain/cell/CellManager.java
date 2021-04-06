package eu.jrie.abacus.core.domain.cell;

import java.util.HashMap;
import java.util.Map;

public class CellManager {

    private final Map<Position, Cell> cells;

    public CellManager() {
        this(new HashMap<>());
    }

    CellManager(Map<Position, Cell> cells) {
        this.cells = cells;
    }

    public Cell getCell(Position position) {
        var cell = cells.computeIfAbsent(position, Cell::new);
        updateValue(cell);
        return cell;
    }

    private static void updateValue(Cell cell) {
        if (cell.hasFormula()) {
            var formula = cell.getFormula();
            var value = formula.calculateValue();
            cell.setValue(value);
        }
    }
}
