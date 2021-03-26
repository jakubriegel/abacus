package eu.jrie.abacus.ui.domain.components.space;

import eu.jrie.abacus.ui.UITest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.awt.Color.white;
import static java.util.Arrays.asList;
import static javax.swing.BoxLayout.X_AXIS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.spy;

class SpaceTest extends UITest {
    @Test
    void shouldCreateSpace() {
        // given
        var menu = spy(UtilsMenu.class);
        var workbenchScroll = workbenchScrollSpy();

        // when
        var space = new Space(menu, workbenchScroll);

        // then
        assertEquals(white, space.getBackground());
        assertHasBoxLayout(space, X_AXIS);

        // and
        assertIterableEquals(List.of(menu, workbenchScroll), asList(space.getComponents()));
    }
}
