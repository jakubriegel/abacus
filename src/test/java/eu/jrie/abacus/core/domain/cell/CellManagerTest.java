package eu.jrie.abacus.core.domain.cell;

import eu.jrie.abacus.core.domain.expression.Formula;
import eu.jrie.abacus.core.domain.expression.TextValue;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CellManagerTest {

    private final Map<Position, Cell> cells = new HashMap<>();
    private final Position position = new Position(1, 2);

    private final CellManager manager = new CellManager(cells);

    @Test
    void shouldInsertAndGetNewCellWhenRequestingNotExistingCell() {
        // when
        var result = manager.getCell(position);

        // then
        assertEquals(new Cell(position), result);
        assertEquals(1, cells.size());
    }

    @Test
    void shouldGetCellWithNoFormula() {
        // given
        var cell = new Cell(position, null, "abc", new TextValue("abc"));
        cells.put(position, cell);

        // when
        var result = manager.getCell(position);

        // then
        assertEquals(cell, result);
        assertEquals(1, cells.size());
    }

    @Test
    void shouldUpdateAndGetCellWithFormula() {
        // given
        var formulaCalled = new AtomicBoolean(false);
        var updatedValue = new TextValue("updated");
        var formula = new Formula("formula", emptyList(), () -> {
            formulaCalled.set(true);
            return updatedValue;
        });
        var cell = new Cell(position, formula, "abc", new TextValue("abc"));
        cells.put(position, cell);

        // when
        var result = manager.getCell(position);

        // then
        assertEquals(new Cell(position, formula, updatedValue.value(), updatedValue), result);
        assertEquals(1, cells.size());
    }

}
