package eu.jrie.abacus.ui.domain.components.space.workbench;

import eu.jrie.abacus.core.domain.cell.Cell;
import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.ui.UITest;
import eu.jrie.abacus.ui.domain.workbench.WorkbenchAccessor;
import eu.jrie.abacus.ui.infra.event.Event;
import eu.jrie.abacus.ui.infra.event.EventAction;
import eu.jrie.abacus.ui.infra.event.EventBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import static eu.jrie.abacus.ui.infra.event.EventType.CELL_EDITOR_UPDATED;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_FOCUS;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_UPDATED;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_VALUE_CHANGED;
import static javax.swing.JTable.AUTO_RESIZE_OFF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WorkbenchTableTest extends UITest {

    private final Position position = new Position(1, 2);

    private final WorkbenchAccessor workbench = spy(WorkbenchAccessor.class);
    private final WorkbenchTableModel model = spy(WorkbenchTableModel.class);
    private final EventBus bus = mock(EventBus.class);

    @Test
    void shouldCreateWorkbenchTable() {
        // when
        var table = new WorkbenchTable(workbench, model, bus);

        // then
        assertHasStandardFont(table);
        assertEquals(AUTO_RESIZE_OFF, table.getAutoResizeMode());
        assertFalse(table.getTableHeader().getReorderingAllowed());

        // and
        assertEquals(model, table.getModel());
    }

    @Test
    void shouldRegisterCellValueChangedHandler() {
        // given
        var actionCaptor = ArgumentCaptor.forClass(EventAction.class);

        // when
        new WorkbenchTable(workbench, model, bus);

        // then
        verify(bus).register(eq("workbenchTableCellValueUpdate"), eq(CELL_VALUE_CHANGED), actionCaptor.capture());

        // given
        var action = actionCaptor.getValue();
        var event = new Event(CELL_VALUE_CHANGED, position, null);

        // and
        var value = "newValue";
        when(workbench.getCell(position)).thenReturn(new Cell(position, false, null, new TextValue(value)));
        doNothing().when(model).setValueAt(value, position.y(), position.x()+1);

        // when
        action.accept(event);

        // then
        verify(workbench).getValueAsString(position);
        verify(model).setValueAt(value, position.y(), position.x()+1);
        verify(bus, never()).accept(any());
    }

    @Test
    void shouldRegisterEditorUpdatedHandler() {
        // given
        var actionCaptor = ArgumentCaptor.forClass(EventAction.class);

        // when
        var table = new WorkbenchTable(workbench, model, bus);
        addCells(table);

        // then
        verify(bus).register(eq("workbenchTableEditorUpdate"), eq(CELL_EDITOR_UPDATED), actionCaptor.capture());

        // given
        var action = actionCaptor.getValue();
        selectCell(table);
        var eventText = "eventText";
        var event = new Event(CELL_EDITOR_UPDATED, position, eventText);

        // when
        action.accept(event);

        // then
        assertTrue(table.isEditing());
        assertEquals(eventText, ((JTextField) table.getEditorComponent()).getText());
        verify(bus, atLeast(1)).accept(new Event(CELL_UPDATED, position, eventText));
    }

    @Test
    void shouldRegisterEditionStartListener() {
        // given
        var table = new WorkbenchTable(workbench, model, bus);
        addCells(table);

        // and
        selectCell(table);
        var text = "text";
        doReturn(text).when(workbench).getText(position);
        var event = new Event(CELL_UPDATED, position, text);
        doNothing().when(bus).accept(event);

        // when
        table.editCellAt(position.y(), position.x()+1);

        // then
        verify(workbench).getText(position);
        verify(bus).accept(event);
    }

    @Test
    void shouldRegisterEditionStopListener() {
        // given
        var table = new WorkbenchTable(workbench, model, bus);
        addCells(table);

        // and
        selectCell(table);
        var text = "text";
        when(model.getValueAt(position.y(), position.x()+1)).thenReturn(text);
        var event = new Event(CELL_UPDATED, position, text);
        doNothing().when(bus).accept(event);

        // when
        table.firePropertyChange("tableCellEditor", 0, 1);

        // then
        verify(model).getValueAt(position.y(), position.x()+1);
        verify(bus).accept(event);
    }

    @ParameterizedTest(name = "should register key listener for navigation - {0}")
    @ValueSource(ints = {10, 37, 38, 39, 40})
    void shouldRegisterKeyListenerForNavigation(int keyCode) {
        // when
        var table = new WorkbenchTable(workbench, model, bus);
        addCells(table);

        // then
        assertEquals(2, table.getKeyListeners().length);

        // given
        var listener = table.getKeyListeners()[1];

        // and
        selectCell(table);
        var text = "text";
        doReturn(text).when(workbench).getText(position);
        var event = new Event(CELL_FOCUS, position, text);
        doNothing().when(bus).accept(event);

        // when
        listener.keyReleased(new KeyEvent(mock(Component.class), -1, -1, -1, keyCode, 'x'));

        // then
        verify(workbench).getText(position);
        verify(bus).accept(event);
    }

    @Test
    void shouldRegisterKeyListenerForInput() {
        // when
        var table = new WorkbenchTable(workbench, model, bus);
        addCells(table);

        // then
        assertEquals(2, table.getKeyListeners().length);

        // given
        var listener = table.getKeyListeners()[1];

        // and
        selectCell(table);

        // when
        listener.keyReleased(new KeyEvent(mock(Component.class), -1, -1, -1, 1, 'x'));

        // then
        assertTrue(table.isEditing());
    }

    @Test
    void shouldRegisterClickListener() {
        // when
        var table = new WorkbenchTable(workbench, model, bus);
        addCells(table);

        // then
        assertEquals(3, table.getMouseListeners().length);

        // given
        var listener = table.getMouseListeners()[2];

        // and
        selectCell(table);
        var text = "text";
        doReturn(text).when(workbench).getText(position);
        var event = new Event(CELL_FOCUS, position, text);
        doNothing().when(bus).accept(event);

        // when
        listener.mouseClicked(null);

        // then
        verify(workbench).getText(position);
        verify(bus).accept(event);
    }

    private void selectCell(WorkbenchTable table) {
        table.setColumnSelectionInterval(position.x()+1, position.x()+1);
        table.setRowSelectionInterval(position.y(), position.y());

    }

    private void addCells(WorkbenchTable table) {
        var value = new Cell(null, false, "text", new TextValue("value"));
        when(workbench.getCell(any())).thenReturn(value);
        table.newColumn();
        table.newColumn();
        table.newColumn();
        table.newRow();
        table.newRow();
        table.newRow();
    }
}
