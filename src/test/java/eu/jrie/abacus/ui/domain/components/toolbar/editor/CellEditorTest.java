package eu.jrie.abacus.ui.domain.components.toolbar.editor;

import eu.jrie.abacus.core.domain.cell.Cell;
import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.domain.expression.LogicValue;
import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.ui.UITest;
import eu.jrie.abacus.ui.domain.workbench.WorkbenchAccessor;
import eu.jrie.abacus.ui.infra.ResourcesProvider;
import eu.jrie.abacus.ui.infra.event.Event;
import eu.jrie.abacus.ui.infra.event.EventAction;
import eu.jrie.abacus.ui.infra.event.EventBus;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.ArgumentCaptor;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static eu.jrie.abacus.ui.infra.event.EventType.CELL_EDITOR_UPDATED;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_FOCUS;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_UPDATED;
import static java.awt.Color.white;
import static java.util.Arrays.asList;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CellEditorTest extends UITest {

    private final ResourcesProvider resourcesProvider = mock(ResourcesProvider.class);
    private final Symbol symbol = spy(Symbol.class);
    private final CellAddress cellAddress = spy(CellAddress.class);
    private final CellEditorField cellEditorField = spy(CellEditorField.class);
    private final EventBus bus = spy(EventBus.class);
    private final WorkbenchAccessor workbench = mock(WorkbenchAccessor.class);

    private final Position position = new Position(1, 2);
    private final ImageIcon icon = new ImageIcon();
    private final String text = "text";
    private final Event event = new Event(CELL_FOCUS, position, text);
    private final String address = "B3";

    @Test
    void shouldCreateCellEditor() {
        // when
        var cellEditor = new CellEditor(resourcesProvider, symbol, cellAddress, cellEditorField, bus, workbench);

        // then
        assertEquals(white, cellEditor.getBackground());
        assertHasBoxLayout(cellEditor, X_AXIS);

        // and
        assertEquals(2, cellEditor.getComponents().length);

        // and
        var panel = (JComponent) cellEditor.getComponents()[0];
        assertTrue(panel instanceof JPanel);
        assertHasBoxLayout(panel, Y_AXIS);
        assertHasConstantSize(panel, 25, 50);
        assertEquals(white, panel.getBackground());
        assertIterableEquals(List.of(symbol, cellAddress), asList(panel.getComponents()));

        // and
        assertEquals(cellEditorField, cellEditor.getComponents()[1]);
    }

    private static record TestCase(
            String name,
            Cell cell,
            String iconPath
    ) {}

    @TestFactory
    Stream<DynamicTest> shouldRegisterUpdateCellEditorOnFocusHandler() {
        return Stream.of(
                new TestCase(
                        "should register update cell editor on focus handler for Formula",
                        new Cell(position, true, "oldText", new TextValue("value")),
                        "editor/round_functions_black_48dp.png"
                ),
                new TestCase(
                        "should register update cell editor on focus handler for NumberValue",
                        new Cell(position, false, "123", new NumberValue(new BigDecimal(123))),
                        "editor/round_looks_one_black_48dp.png"
                ),
                new TestCase(
                        "should register update cell editor on focus handler for TextValue",
                        new Cell(position, false, "oldText", new TextValue("value")),
                        "editor/round_text_fields_black_48dp.png"
                ),
                new TestCase(
                        "should register update cell editor on focus handler for TextValue",
                        new Cell(position, false, "oldText", new LogicValue(true)),
                        "editor/outline_toll_black_48dp.png"
                )
        ).peek(it -> reset(resourcesProvider, cellAddress, bus, workbench, symbol, cellEditorField))
                .map(testCase -> dynamicTest(testCase.name, () -> {
                    // given
                    var actionCaptor = ArgumentCaptor.forClass(EventAction.class);
                    when(resourcesProvider.getIcon(testCase.iconPath)).thenReturn(icon);

                    // when
                    new CellEditor(resourcesProvider, symbol, cellAddress, cellEditorField, bus, workbench);

                    // then
                    verify(cellAddress).set("--");
                    verify(bus).register(eq("updateCellEditorOnFocus"), eq(CELL_FOCUS), actionCaptor.capture());

                    // given
                    var action = actionCaptor.getValue();

                    // and
                    when(workbench.getCell(position)).thenReturn(testCase.cell);
                    doNothing().when(symbol).set(icon);

                    // when
                    action.accept(event);

                    // then
                    verify(workbench).getCell(position);
                    verify(symbol).set(icon);
                    verify(cellAddress).set(address);
                    verify(cellEditorField).set(text);
                }));
    }

    @TestFactory
    Stream<DynamicTest> shouldRegisterUpdateCellEditorOnUpdateHandler() {
        return Stream.of(
                new TestCase(
                        "should register update cell editor on update handler - for Formula",
                        new Cell(position, true, "oldText", new TextValue("value")),
                        "editor/round_functions_black_48dp.png"
                ),
                new TestCase(
                        "should register update cell editor on update handler - for NumberValue",
                        new Cell(position, false, "123", new NumberValue(new BigDecimal(123))),
                        "editor/round_looks_one_black_48dp.png"
                ),
                new TestCase(
                        "should register update cell editor on update handler - for TextValue",
                        new Cell(position, false, "oldText", new TextValue("value")),
                        "editor/round_text_fields_black_48dp.png"
                )
        ).peek(it -> reset(resourcesProvider, cellAddress, bus, workbench, symbol, cellEditorField))
                .map(testCase -> dynamicTest(testCase.name, () -> {
                    // given
                    var actionCaptor = ArgumentCaptor.forClass(EventAction.class);
                    when(resourcesProvider.getIcon("editor/round_text_fields_black_48dp.png")).thenReturn(icon);

                    // when
                    new CellEditor(resourcesProvider, symbol, cellAddress, cellEditorField, bus, workbench);

                    // then
                    verify(cellAddress).set("--");
                    verify(bus).register(eq("updateCellEditorOnUpdate"), eq(CELL_UPDATED), actionCaptor.capture());

                    // given
                    var action = actionCaptor.getValue();

                    // and
                    var cell = new Cell(position, false, "oldText", new TextValue("value"));
                    when(workbench.getCell(position)).thenReturn(cell);
                    doNothing().when(symbol).set(icon);

                    // when
                    action.accept(event);

                    // then
                    verify(workbench).getCell(position);
                    verify(symbol).set(icon);
                    verify(cellAddress).set(address);
                    verify(cellEditorField).set(text);
                }));
    }

    @Test
    void shouldAddKeyReleasedListenerOnEditorFieldAndDoNothingWhenCellIsNull() {
        // given
        var listenerCaptor = ArgumentCaptor.forClass(KeyListener.class);

        // when
        new CellEditor(resourcesProvider, symbol, cellAddress, cellEditorField, bus, workbench);

        // then
        assertEquals(1, cellEditorField.getKeyListeners().length);
        verify(cellEditorField).addKeyListener(listenerCaptor.capture());
        var listener = listenerCaptor.getValue();

        // when
        listener.keyReleased(null);

        // then
        verify(bus, never()).accept(any());
    }

    @Test
    void shouldAddKeyReleasedListenerOnEditorField() {
        // given
        var actionCaptor = ArgumentCaptor.forClass(EventAction.class);
        when(resourcesProvider.getIcon("editor/round_text_fields_black_48dp.png")).thenReturn(icon);

        // and
        var listenerCaptor = ArgumentCaptor.forClass(KeyListener.class);

        // when
        new CellEditor(resourcesProvider, symbol, cellAddress, cellEditorField, bus, workbench);

        // then
        verify(cellAddress).set("--");
        verify(bus).register(eq("updateCellEditorOnUpdate"), eq(CELL_UPDATED), actionCaptor.capture());

        // and
        verify(cellEditorField).addKeyListener(listenerCaptor.capture());

        // given
        var action = actionCaptor.getValue();

        // and
        var cell = new Cell(position, false, "oldText", new TextValue("value"));
        when(workbench.getCell(position)).thenReturn(cell);
        doNothing().when(symbol).set(icon);

        // when
        action.accept(event);

        // then
        verify(workbench).getCell(position);
        verify(symbol).set(icon);
        verify(cellAddress).set(address);
        verify(cellEditorField).set(text);

        // and
        verify(cellEditorField).addKeyListener(listenerCaptor.capture());
        assertEquals(1, cellEditorField.getKeyListeners().length);
        verify(cellEditorField).addKeyListener(listenerCaptor.capture());

        // given
        var listener = listenerCaptor.getValue();
        var editedText = "edited";
        cellEditorField.setText(editedText);

        // when
        listener.keyReleased(null);

        // then
        verify(bus).accept(new Event(CELL_EDITOR_UPDATED, cell.getPosition(), editedText));
    }
}
