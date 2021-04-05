package eu.jrie.abacus.ui.domain;

import eu.jrie.abacus.ui.UITest;
import eu.jrie.abacus.ui.domain.components.space.Space;
import eu.jrie.abacus.ui.domain.components.space.UtilsMenu;
import eu.jrie.abacus.ui.domain.components.toolbar.LogoLabel;
import eu.jrie.abacus.ui.domain.components.toolbar.Toolbar;
import eu.jrie.abacus.ui.infra.ResourcesProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

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
    private final Toolbar toolbar = spy(new Toolbar(spy(LogoLabel.class), cellEditorSpy(), textToolsSpy()));
    private final Space space = spy(new Space(spy(UtilsMenu.class), workbenchScrollSpy()));

    private AppFrame appFrame;

    @BeforeEach
    void setup() {
        appFrame = new AppFrame(resourcesProvider, toolbar, space);
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "ABACUS_TEST_HEADLESS", matches = "1")
    void shouldStartAppFrame() {
        // given
        when(resourcesProvider.getIcon("abacus.png")).thenReturn(new ImageIcon());

        // when
        appFrame.start();

        // then
        verify(resourcesProvider).getIcon("abacus.png");

        // and
        assertEquals(new Dimension(1200, 800), appFrame.getSize());
        assertEquals(new Dimension(600, 120), appFrame.getMinimumSize());
        assertEquals(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE), appFrame.getMaximumSize());
        assertHasBoxLayout(appFrame.getContentPane(), Y_AXIS);

        // and
        assertIterableEquals(List.of(toolbar, space), asList(appFrame.getContentPane().getComponents()));
        assertEquals("untitled.abcsv - Abacus", appFrame.getTitle());
    }
}
