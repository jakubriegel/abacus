package eu.jrie.abacus.ui.domain.workbench;

import eu.jrie.abacus.core.domain.cell.Cell;
import eu.jrie.abacus.core.domain.cell.Position;

public interface WorkbenchAccessor {
    Cell getCell(Position position);

    default String getText(Position position) {
        return getCell(position).getText();
    }

    default String getValueAsString(Position position) {
        return getCell(position).getValueAsString();
    }
}
