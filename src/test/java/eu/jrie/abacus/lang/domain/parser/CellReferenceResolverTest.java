package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CellReferenceResolverTest {

    private final CellReferenceResolver resolver = new CellReferenceResolver();

    @Test
    void shouldResolveCellPosition() {
        // given
        var cell = "C3";

        // and
        var cellPosition = new Position(2, 2);
        var cellValue = new TextValue("text");
        var context = mock(WorkbenchContext.class);
        when(context.getCellValue(cellPosition)).thenReturn(cellValue);

        // when
        var result = resolver.resolve(cell);

        // and
        var resolvedValue = result.get(context);

        // then
        assertEquals(cellValue, resolvedValue);
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenReceivedInvalidInput() {
        // given
        var cell = "invalid";

        // expect
        assertThrows(IllegalStateException.class, () -> resolver.resolve(cell));
    }
}
