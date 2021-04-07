package eu.jrie.abacus.core.domain.cell;

import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.expression.Value;

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
            var updated = tryCalculate(cell);
            cell.setValue(updated);
        }
    }

    private static Value tryCalculate(Cell cell) {
        var formula = cell.getFormula();
        try {
            return formula.calculateValue();
        } catch (Exception e) {
            return new TextValue("Calculation Error");
        }
    }
}
