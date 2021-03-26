package eu.jrie.abacus.ui;

import eu.jrie.abacus.core.domain.workbench.Workbench;
import eu.jrie.abacus.ui.domain.components.space.workbench.WorkbenchScroll;
import eu.jrie.abacus.ui.domain.components.space.workbench.WorkbenchTable;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public abstract class UITest {

    protected static WorkbenchTable workbenchTableSpy() {
        return spy(new WorkbenchTable(mock(Workbench.class)));
    }

    protected static WorkbenchScroll workbenchScrollSpy() {
        return spy(new WorkbenchScroll(workbenchTableSpy()));
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
