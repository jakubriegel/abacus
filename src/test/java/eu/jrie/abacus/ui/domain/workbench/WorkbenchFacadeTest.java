package eu.jrie.abacus.ui.domain.workbench;

import eu.jrie.abacus.core.domain.cell.Cell;
import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.workbench.CellReadException;
import eu.jrie.abacus.core.domain.workbench.FormulaExecutionException;
import eu.jrie.abacus.core.domain.workbench.Workbench;
import eu.jrie.abacus.ui.infra.event.Event;
import eu.jrie.abacus.ui.infra.event.EventAction;
import eu.jrie.abacus.ui.infra.event.EventBus;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static eu.jrie.abacus.ui.infra.event.EventType.CELL_UPDATED;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_VALUE_CHANGED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WorkbenchFacadeTest {

    private final Position position = new Position(1, 2);

    private final Workbench workbench = mock(Workbench.class);
    private final EventBus eventBus = spy(EventBus.class);

    private final WorkbenchFacade workbenchFacade = new WorkbenchFacade(workbench, eventBus);

    @Test
    void shouldGetCell() {
        // given
        var cell = new Cell(position, false, null, null);
        when(workbench.getCell(position)).thenReturn(cell);

        // when
        var result = workbenchFacade.getCell(position);

        // then
        assertEquals(cell, result);
    }

    @Test
    void shouldGetCellText() {
        // given
        var text = "text";
        var cell = new Cell(position, false, text, null);
        when(workbench.getCell(position)).thenReturn(cell);

        // when
        var result = workbenchFacade.getText(position);

        // then
        assertEquals(text, result);
    }

    @Test
    void shouldGetCellValueAsString() {
        // given
        var text = "text";
        var value = new TextValue("value");
        var cell = new Cell(position, false, text,  value);
        when(workbench.getCell(position)).thenReturn(cell);

        // when
        var result = workbenchFacade.getValueAsString(position);

        // then
        assertEquals(value.getAsString(), result);
    }

    @Test
    void shouldRegisterCellUpdatedHandler() throws CellReadException, FormulaExecutionException {
        // given
        var actionCaptor = ArgumentCaptor.forClass(EventAction.class);

        // expect
        verify(eventBus).register(eq("updateWorkbench"), eq(CELL_UPDATED), actionCaptor.capture());
        var action = actionCaptor.getValue();

        // given
        var event = new Event(CELL_UPDATED, position, "updatedText");

        // when
        action.accept(event);

        // then
        verify(workbench).setTextAt(position, event.text());
        verify(eventBus).accept(new Event(CELL_VALUE_CHANGED, position, event.text()));
    }
}
