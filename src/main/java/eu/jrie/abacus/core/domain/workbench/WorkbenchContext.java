package eu.jrie.abacus.core.domain.workbench;

import eu.jrie.abacus.core.domain.cell.CellManager;
import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.formula.FormulaDefinition;
import eu.jrie.abacus.core.domain.formula.FormulaManager;

import java.util.List;

public class WorkbenchContext {

    private final CellManager cellManager;
    private final FormulaManager formulaManager;

    WorkbenchContext(CellManager cellManager, FormulaManager formulaManager) {
        this.cellManager = cellManager;
        this.formulaManager = formulaManager;
    }

    public Value getCellValue(Position position) {
        return cellManager.getCell(position)
                .getValue();
    }

    public List<FormulaDefinition> findFormulasDefinition(String name) {
        return formulaManager.findDefinitions(name);
    }
}
