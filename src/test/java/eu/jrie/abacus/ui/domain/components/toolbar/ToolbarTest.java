package eu.jrie.abacus.ui.domain.components.toolbar;

import eu.jrie.abacus.ui.UITest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static javax.swing.BoxLayout.X_AXIS;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.spy;

class ToolbarTest extends UITest {
    @Test
    void shouldCreateToolbar() {
        // given
        var logoLabel = spy(LogoLabel.class);
        var cellEditor = spy(CellEditor.class);
        var textTools = spy(TextTools.class);

        // when
        var toolbar = new Toolbar(logoLabel, cellEditor, textTools);

        // then
        assertHasHorizontallyFlexibleSize(toolbar, 800, 50);
        assertHasBoxLayout(toolbar, X_AXIS);

        // and
        assertIterableEquals(List.of(logoLabel, cellEditor, textTools), asList(toolbar.getComponents()));
    }
}
