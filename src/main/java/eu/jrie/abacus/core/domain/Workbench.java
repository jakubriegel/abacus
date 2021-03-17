package eu.jrie.abacus.core.domain;

import eu.jrie.abacus.core.domain.cell.Cell;
import eu.jrie.abacus.core.domain.cell.CellManager;
import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.domain.expression.Formula;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.formula.FormulaManager;
import eu.jrie.abacus.lang.domain.exception.InvalidInputException;
import eu.jrie.abacus.lang.domain.parser.Parser;

public class Workbench {

    private final CellManager cellManager = new CellManager();
    private final FormulaManager formulaManager = new FormulaManager();
    private final Parser parser = new Parser(formulaManager.getFormulas());



    public String getTextAt(Position position) {
        return cellManager.getCell(position).getText();
    }

    public void setTextAt(Position position, String text) {
        var cell = cellManager.getCell(position);
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
                cell.setValue(value);
            }
        } catch (InvalidInputException e) {
            cell.setValue(new TextValue("ERROR"));
        }
    }

    public String getValueAt(Position position) {
        return cellManager.getCell(position).getValueAsString();
    }



}
