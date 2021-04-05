package eu.jrie.abacus.ui.domain.components.toolbar;

import eu.jrie.abacus.ui.UITest;
import eu.jrie.abacus.ui.domain.components.toolbar.editor.CellAddress;
import eu.jrie.abacus.ui.domain.components.toolbar.editor.CellEditor;
import eu.jrie.abacus.ui.domain.components.toolbar.editor.CellEditorField;
import eu.jrie.abacus.ui.domain.components.toolbar.editor.Symbol;
import eu.jrie.abacus.ui.domain.workbench.WorkbenchAccessor;
import eu.jrie.abacus.ui.infra.ResourcesProvider;
import eu.jrie.abacus.ui.infra.event.EventBus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static javax.swing.BoxLayout.X_AXIS;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class ToolbarTest extends UITest {
    @Test
    void shouldCreateToolbar() {
        // given
        var logoLabel = spy(LogoLabel.class);
        var cellEditor = spy(
                new CellEditor(
                        mock(ResourcesProvider.class),
                        spy(Symbol.class), spy(CellAddress.class), spy(CellEditorField.class),
                        spy(EventBus.class), mock(WorkbenchAccessor.class)
                )
        );
        var textTools = textToolsSpy();

        // when
        var toolbar = new Toolbar(logoLabel, cellEditor, textTools);

        // then
        assertHasHorizontallyFlexibleSize(toolbar, 800, 50);
        assertHasBoxLayout(toolbar, X_AXIS);

        // and
        assertIterableEquals(List.of(logoLabel, cellEditor, textTools), asList(toolbar.getComponents()));
    }
}
