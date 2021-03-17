package eu.jrie.abacus.core.domain;

import eu.jrie.abacus.core.domain.expression.Formula;
import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.formula.FormulaManager;
import eu.jrie.abacus.lang.Parser;
import eu.jrie.abacus.lang.domain.exception.InvalidInputException;

import java.util.HashMap;
import java.util.Map;

public class Workbench {
    private final Map<Position, Cell> cells = new HashMap<>();
    private final FormulaManager formulaManager = new FormulaManager();
    private final Parser parser = new Parser(formulaManager.getFormulas());

    public Workbench() {
        cells.put(new Position(3, 5), new Cell(new Position(3, 5), "=add(a, b)", "test"));
    }

    public String getTextAt(Position position) {
        return getCellAt(position).getText();
    }

    public void setTextAt(Position position, String text) {
        var cell = getCellAt(position);
        cell.setText(text);
        updateCell(cell);
    }

    private void updateCell(Cell cell) {
        try {
            final var expression = parser.parse(cell.getText());
            if (expression instanceof Formula formula) {
                var value = formula.calculateValue();
                cell.setValue(value);
            } else if (expression instanceof Value value) {
                cell.setValue(value.calculateValue());
            }
        } catch (InvalidInputException e) {
            cell.setValue("ERROR");
        }
    }

    public String getValueAt(Position position) {
        return getCellAt(position).getValue();
    }

    private Cell getCellAt(Position position) {
        return cells.computeIfAbsent(position, Cell::new);
    }

}
