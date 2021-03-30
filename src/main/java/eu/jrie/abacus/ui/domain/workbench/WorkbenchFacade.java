package eu.jrie.abacus.ui.domain.workbench;

import eu.jrie.abacus.core.domain.cell.Cell;
import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.domain.workbench.CellReadException;
import eu.jrie.abacus.core.domain.workbench.FormulaExecutionException;
import eu.jrie.abacus.core.domain.workbench.Workbench;
import eu.jrie.abacus.ui.infra.event.Event;
import eu.jrie.abacus.ui.infra.event.EventBus;

import static eu.jrie.abacus.ui.infra.event.EventType.CELL_UPDATED;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_VALUE_CHANGED;

public class WorkbenchFacade implements WorkbenchAccessor {

    private final Workbench workbench;
    private final EventBus bus;

    public WorkbenchFacade(Workbench workbench, EventBus bus) {
        this.workbench = workbench;
        this.bus = bus;

        bus.register("updateWorkbench", CELL_UPDATED, event -> updateCell(event.position(), event.text()));
    }

    @Override
    public Cell getCell(Position position) {
        return workbench.getCell(position);
    }

    private void updateCell(Position position, String text) {
        try {
            workbench.setTextAt(position, text);
        } catch (CellReadException | FormulaExecutionException e) {
            // ignore
        } finally {
            bus.accept(new Event(CELL_VALUE_CHANGED, position, text));
        }
    }
}
