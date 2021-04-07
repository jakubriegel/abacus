package eu.jrie.abacus.core.domain.workbench;

import eu.jrie.abacus.core.domain.cell.Cell;
import eu.jrie.abacus.core.domain.cell.CellManager;
import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.domain.cell.style.CellStyle;
import eu.jrie.abacus.core.domain.cell.style.CellStyleManager;
import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.Formula;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.lang.domain.exception.InvalidInputException;
import eu.jrie.abacus.lang.domain.exception.formula.FormulaParsingException;
import eu.jrie.abacus.lang.domain.parser.Parser;

public class Workbench {

    private final CellManager cellManager;
    private final CellStyleManager cellStyleManager;
    private final Parser parser;

    public Workbench(CellManager cellManager, CellStyleManager cellStyleManager, Parser parser) {
        this.cellManager = cellManager;
        this.cellStyleManager = cellStyleManager;
        this.parser = parser;
    }

    public Cell getCell(Position position) {
        return cellManager.getCell(position);
    }

    public void setTextAt(Position position, String text) throws CellReadException, FormulaExecutionException {
        var cell = cellManager.getCell(position);
        cell.setText(text);
        updateCell(cell);
    }

    public CellStyle getCellStyle(Position position) {
        return cellStyleManager.getStyle(position);
    }

    public void setDefaultCellStyle(Position position) {
        cellStyleManager.setDefaultStyle(position);
    }

    public void setCellStyle(Position position, CellStyle cellStyle) {
        cellStyleManager.setStyle(position, cellStyle);
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
            if (e instanceof FormulaParsingException) {
                cell.setValue(new TextValue("E: " + e.getMessage()));
            } else {
                cell.setValue(new TextValue("Value Error"));
            }
            cell.setFormula(null);
            throw new CellReadException(e);
        } catch (Exception e) {
            cell.setValue(new TextValue("ERROR"));
            cell.setFormula(null);
            throw new CellReadException(e);
        }
    }

    private void updateCellWithFormula(Formula formula, Cell cell) throws FormulaExecutionException {
        cell.setFormula(formula);
        try {
            var value = formula.calculateValue();
            cell.setValue(value);
        } catch (Exception e) {
            cell.setValue(new TextValue("Formula Error"));
            throw new FormulaExecutionException(e);
        }
    }

    private void updateCellWithValue(Value value, Cell cell) {
        cell.setFormula(null);
        cell.setValue(value);
    }
}
