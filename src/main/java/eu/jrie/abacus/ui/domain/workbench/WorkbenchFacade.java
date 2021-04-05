package eu.jrie.abacus.ui.domain.workbench;

import eu.jrie.abacus.core.domain.cell.Cell;
import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.domain.cell.style.CellStyle;
import eu.jrie.abacus.core.domain.workbench.CellReadException;
import eu.jrie.abacus.core.domain.workbench.FormulaExecutionException;
import eu.jrie.abacus.core.domain.workbench.Workbench;
import eu.jrie.abacus.ui.infra.event.Event;
import eu.jrie.abacus.ui.infra.event.EventBus;

import static eu.jrie.abacus.ui.infra.event.EventType.CELL_FOCUS;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_STYLE_CHANGED;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_STYLE_UPDATED;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_UPDATED;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_VALUE_CHANGED;

public class WorkbenchFacade implements WorkbenchAccessor {

    private final Workbench workbench;
    private final EventBus bus;

    public WorkbenchFacade(Workbench workbench, EventBus bus) {
        this.workbench = workbench;
        this.bus = bus;

        bus.register(
                "updateWorkbench",
                CELL_UPDATED,
                event -> updateCell(event.position(), event.text())
        );
        bus.register(
                "updateCellStyle",
                CELL_STYLE_UPDATED,
                event -> updateCellStyle(event.position(), event.cellStyle())
        );
    }

    @Override
    public Cell getCell(Position position) {
        return workbench.getCell(position);
    }

    @Override
    public CellStyle getCellStyle(Position position) {
        return workbench.getCellStyle(position);
    }

    private void updateCell(Position position, String text) {
        try {
            workbench.setTextAt(position, text);
        } catch (CellReadException | FormulaExecutionException e) {
            // ignore
        } finally {
            bus.accept(new Event(CELL_VALUE_CHANGED, position, text, null));
        }
    }

    private void updateCellStyle(Position position, CellStyle cellStyle) {
        if (cellStyle == null) {
            workbench.setDefaultCellStyle(position);
            var updatedStyle = workbench.getCellStyle(position);
            bus.accept(new Event(CELL_STYLE_CHANGED, position, null, updatedStyle));
            bus.accept(new Event(CELL_FOCUS, position, null, updatedStyle));
        } else {
            workbench.setCellStyle(position, cellStyle);
            var updatedStyle = workbench.getCellStyle(position);
            bus.accept(new Event(CELL_STYLE_CHANGED, position, null, updatedStyle));
        }
    }
}
