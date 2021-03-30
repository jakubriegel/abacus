package eu.jrie.abacus.core.domain.workbench;

import eu.jrie.abacus.core.domain.cell.Cell;
import eu.jrie.abacus.core.domain.cell.CellManager;
import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.Formula;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.formula.FormulaManager;
import eu.jrie.abacus.lang.domain.exception.InvalidInputException;
import eu.jrie.abacus.lang.domain.parser.Parser;

import static eu.jrie.abacus.lang.domain.parser.ParserFactory.buildParser;

public class Workbench {

    private final CellManager cellManager = new CellManager();
    private final FormulaManager formulaManager = new FormulaManager();
    private final Parser parser = buildParser(new WorkbenchContext(cellManager, formulaManager));

    public Cell getCell(Position position) {
        return cellManager.getCell(position);
    }

    public void setTextAt(Position position, String text) throws CellReadException, FormulaExecutionException {
        var cell = cellManager.getCell(position);
        cell.setText(text);
        updateCell(cell);
    }

    private void updateCell(Cell cell) throws CellReadException, FormulaExecutionException {
        final var expression = parseExpression(cell);
        if (expression instanceof Formula formula) {
            updateCellWithFormula(formula, cell);
        } else if (expression instanceof Value value) {
            updateCellWithValue(value, cell);
        }
    }

    private Expression parseExpression(Cell cell) throws CellReadException {
        try {
            return parser.parse(cell.getText());
        } catch (InvalidInputException e) {
            cell.setValue(new TextValue("VALUE ERROR"));
            throw new CellReadException(e);
        }
    }

    private void updateCellWithFormula(Formula formula, Cell cell) throws FormulaExecutionException {
        cell.setFormula(true);
        try {
            var value = formula.calculateValue();
            cell.setValue(value);
        } catch (Exception e) {
            cell.setValue(new TextValue("FORMULA ERROR"));
            throw new FormulaExecutionException(e);
        }
    }

    private void updateCellWithValue(Value value, Cell cell) {
        cell.setFormula(false);
        cell.setValue(value);
    }

    public String getValueAt(Position position) {
        return cellManager.getCell(position).getValueAsString();
    }



}
