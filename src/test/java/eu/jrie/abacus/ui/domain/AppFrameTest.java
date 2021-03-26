package eu.jrie.abacus.ui.domain;

import eu.jrie.abacus.ui.UITest;
import eu.jrie.abacus.ui.domain.components.space.Space;
import eu.jrie.abacus.ui.domain.components.space.UtilsMenu;
import eu.jrie.abacus.ui.domain.components.toolbar.CellEditor;
import eu.jrie.abacus.ui.domain.components.toolbar.LogoLabel;
import eu.jrie.abacus.ui.domain.components.toolbar.TextTools;
import eu.jrie.abacus.ui.domain.components.toolbar.Toolbar;
import eu.jrie.abacus.ui.infra.ResourcesProvider;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static java.util.Arrays.asList;
import static javax.swing.BoxLayout.Y_AXIS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AppFrameTest extends UITest {

    private final ResourcesProvider resourcesProvider = mock(ResourcesProvider.class);
    private final Toolbar toolbar = spy(new Toolbar(spy(LogoLabel.class), spy(CellEditor.class), spy(TextTools.class)));
    private final Space space = spy(new Space(spy(UtilsMenu.class), workbenchScrollSpy()));

    private final AppFrame appFrame = new AppFrame(resourcesProvider, toolbar, space);

    @Test
    void shouldStartAppFrame() {
        // given
        when(resourcesProvider.getIcon("abacus.png")).thenReturn(new ImageIcon());

        // when
        appFrame.start();

        // then
        verify(resourcesProvider).getIcon("abacus.png");

        // and
        assertEquals(new Dimension(800, 600), appFrame.getSize());
        assertEquals(new Dimension(600, 120), appFrame.getMinimumSize());
        assertEquals(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE), appFrame.getMaximumSize());
        assertHasBoxLayout(appFrame.getContentPane(), Y_AXIS);

        // and
        assertIterableEquals(List.of(toolbar, space), asList(appFrame.getContentPane().getComponents()));
        assertEquals("untitled.abcsv - Abacus", appFrame.getTitle());
    }

}
