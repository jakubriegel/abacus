package eu.jrie.abacus.ui;

import eu.jrie.abacus.ui.domain.components.factory.ComponentFactory;
import eu.jrie.abacus.ui.domain.components.space.workbench.CellStyleRenderer;
import eu.jrie.abacus.ui.domain.components.space.workbench.WorkbenchScroll;
import eu.jrie.abacus.ui.domain.components.space.workbench.WorkbenchTable;
import eu.jrie.abacus.ui.domain.components.space.workbench.WorkbenchTableModel;
import eu.jrie.abacus.ui.domain.components.toolbar.TextTools;
import eu.jrie.abacus.ui.domain.components.toolbar.editor.CellAddress;
import eu.jrie.abacus.ui.domain.components.toolbar.editor.CellEditor;
import eu.jrie.abacus.ui.domain.components.toolbar.editor.CellEditorField;
import eu.jrie.abacus.ui.domain.components.toolbar.editor.Symbol;
import eu.jrie.abacus.ui.domain.workbench.WorkbenchAccessor;
import eu.jrie.abacus.ui.infra.ResourcesProvider;
import eu.jrie.abacus.ui.infra.event.EventBus;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public abstract class UITest {

    protected static WorkbenchTable workbenchTableSpy() {
        var workbenchAccessor = mock(WorkbenchAccessor.class);
        var workbenchTableModel = mock(WorkbenchTableModel.class);
        var bus = mock(EventBus.class);
        var renderer = mock(CellStyleRenderer.class);
        return spy(new WorkbenchTable(workbenchAccessor, workbenchTableModel, bus, renderer));
    }

    protected static WorkbenchScroll workbenchScrollSpy() {
        return spy(new WorkbenchScroll(workbenchTableSpy()));
    }

    protected static eu.jrie.abacus.ui.domain.components.toolbar.editor.CellEditor cellEditorSpy() {
        var resourceProvider = spy(ResourcesProvider.class);
        var symbol = spy(Symbol.class);
        var cellAddress = spy(CellAddress.class);
        var cellEditorField = spy(CellEditorField.class);
        var eventBus = mock(EventBus.class);
        var workbench = mock(WorkbenchAccessor.class);
        return spy(new CellEditor(resourceProvider, symbol, cellAddress, cellEditorField, eventBus, workbench));
    }

    protected static TextTools textToolsSpy() {
        var bus = mock(EventBus.class);
        var factory = new ComponentFactory();
        return spy(new TextTools(bus, factory));
    }

    protected static void assertHasStandardFont(JComponent component) {
        assertHasStandardFont(component, 12f);
    }

    protected static void assertHasStandardFont(JComponent component, float size) {
        var font = component.getFont();
        assertEquals(size, font.getSize());
        assertEquals("JetBrains Mono Regular", font.getName());
    }

    protected static void assertHasBoxLayout(Container container, int axis) {
        assertTrue(container.getLayout() instanceof BoxLayout);
        assertEquals(axis, ((BoxLayout) container.getLayout()).getAxis());
    }

    protected static void assertHasConstantSize(JComponent component, int width, int height) {
        var dimension = new Dimension(width, height);
        component.setMinimumSize(dimension);
        component.setPreferredSize(dimension);
        component.setMaximumSize(dimension);
    }

    protected static void assertHasHorizontallyFlexibleSize(JComponent component, int width, int height) {
        assertEquals(new Dimension(0, height), component.getMinimumSize());
        assertEquals(new Dimension(width, height), component.getPreferredSize());
        assertEquals(new Dimension(Integer.MAX_VALUE, height), component.getMaximumSize());
    }

}
