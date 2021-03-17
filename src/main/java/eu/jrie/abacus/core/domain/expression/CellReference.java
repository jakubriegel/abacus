package eu.jrie.abacus.core.domain.expression;

import eu.jrie.abacus.core.domain.cell.CellManager;
import eu.jrie.abacus.core.domain.cell.Position;

public final class CellReference implements Expression {

    private final Position cellPosition;
    private final CellManager cellManager;

    public CellReference(Position cellPosition, CellManager cellManager) {
        this.cellPosition = cellPosition;
        this.cellManager = cellManager;
    }

    @Override
    public Value calculateValue() {
        return cellManager.getCell(cellPosition).getValue();
    }
}
